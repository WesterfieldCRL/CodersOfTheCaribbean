package Cruise;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.*;

public class Cruise {
    private String name;
    private ArrayList<Room> roomList;

    private ArrayList<Destination> travelPath;

    private Clock clock;

    /**
     * Constructor for creating a Cruise object with the specified name.
     *
     * @param name The name of the cruise.
     */
    public Cruise(String name) {
        this.name = name;
        roomList = new ArrayList<>();


        this.clock = Clock.systemDefaultZone();

        if (name.equals("CRUISE1") || name.equals("CRUISE2") || name.equals("CRUISE3")
                || name.equals("cruise3") || name.equals("cruise2") || name.equals("cruise1")) {
            updateTravelPath();

            if (travelPath.get(travelPath.size() - 1).arrival.isBefore(LocalDate.now(clock))) {
                createTravelPath();
                saveTravelPath();
            }
        }

    }

    /**
     * Checks if the given start and end dates are valid for making a reservation on this cruise.
     *
     * @param start The start date of the reservation.
     * @param end   The end date of the reservation.
     * @return True if the dates are valid, false otherwise.
     */
    public boolean areDatesValid(LocalDate start, LocalDate end)
    {
        if (start.isAfter(LocalDate.now(clock))) {
            ArrayList<LocalDate> validDates = getValidReservationDates();

            if (start.isBefore(end)) {
                for (int i = 0; i < validDates.size(); i++) {
                    if (start.equals(validDates.get(i))) {
                        for (int j = i; j < validDates.size(); j++) {
                            if (end.equals(validDates.get(j))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Checks if a room with specified characteristics is available for reservation during the given dates.
     *
     * @param quality     The quality level of the room.
     * @param numBeds     The number of beds in the room.
     * @param bedType     The type of bed in the room.
     * @param isSmoking   True if the room is smoking, false otherwise.
     * @param startDate   The start date of the reservation.
     * @param endDate     The end date of the reservation.
     * @return An Optional containing an available room or empty if no room is available.
     */
    public Optional<Room> isRoomAvailable(Room.Quality quality, int numBeds, Room.BedType bedType,
                                          boolean isSmoking, LocalDate startDate, LocalDate endDate)
    {
        if (areDatesValid(startDate, endDate)) {

            Connection connection = null;
            if (!startDate.isAfter(endDate)) {
                try {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

                    connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

                    PreparedStatement roomQuery = connection.prepareStatement("SELECT * FROM " + name +
                            " WHERE BEDNUMBER = ? AND BEDTYPE = ? AND ROOMTYPE = ? AND ISSMOKING = ?");
                    //get all rooms matching the inputted query
                    roomQuery.setInt(1, numBeds);
                    roomQuery.setString(2, bedType.toString());
                    roomQuery.setString(3, quality.toString());
                    roomQuery.setBoolean(4, isSmoking);

                    ResultSet rooms = roomQuery.executeQuery();

                    while (rooms.next()) {

                        Room room = new Room(rooms.getInt("ID"),
                                rooms.getInt("BEDNUMBER"),
                                Room.BedType.valueOf(rooms.getString("BEDTYPE")),
                                Room.Quality.valueOf(rooms.getString("ROOMTYPE")),
                                rooms.getBoolean("ISSMOKING"));


                        PreparedStatement reservationQuery = connection.prepareStatement(
                                "SELECT * FROM RESERVATIONS WHERE CRUISE = ? AND ROOMID = ?");
                        //get all reservations for the specific room on this cruise
                        reservationQuery.setString(1, name);
                        reservationQuery.setInt(2, room.getID());

                        ResultSet rs = reservationQuery.executeQuery();

                        boolean isReserved = false;
                        while (rs.next()) {
                            LocalDate reservedStart = rs.getDate("STARTDATE").toLocalDate();
                            LocalDate reservedEnd = rs.getDate("ENDDATE").toLocalDate();

                            if (!reservedStart.isAfter(endDate) && !reservedEnd.isBefore(startDate)) {
                                isReserved = true;
                            }
                        }

                        if (!isReserved) {
                            connection.close();
                            return Optional.of(room);
                        }
                    }


                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Retrieves a list of available rooms for reservations between the specified start and end dates.
     *
     * @param startDate The start date for reservations.
     * @param endDate   The end date for reservations.
     * @return An ArrayList of available rooms.
     */
    public ArrayList<Room> getAvailableRoomsList(LocalDate startDate, LocalDate endDate)
    {
        ArrayList<Room> roomsList = new ArrayList<>();
        Connection connection = null;
        boolean roomReserved;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement roomQuery = connection.prepareStatement("SELECT * FROM " + name);

            ResultSet rooms = roomQuery.executeQuery();

            while (rooms.next())
            {
                roomReserved = false;
                //Create room
                Room room = new Room(rooms.getInt("ID"),
                        rooms.getInt("BEDNUMBER"),
                        Room.BedType.valueOf(rooms.getString("BEDTYPE")),
                        Room.Quality.valueOf(rooms.getString("ROOMTYPE")),
                        rooms.getBoolean("ISSMOKING"));

                //Check for any reservations for that room
                PreparedStatement reservationsQuery =
                        connection.prepareStatement("SELECT * FROM RESERVATIONS WHERE CRUISE = ? AND ROOMID = ?");
                reservationsQuery.setString(1, name);
                reservationsQuery.setInt(2, room.getID());

                ResultSet reserves = reservationsQuery.executeQuery();

                while (reserves.next())
                {
                    roomReserved = true;
                    LocalDate reservedStart = reserves.getDate("STARTDATE").toLocalDate();
                    LocalDate reservedEnd = reserves.getDate("ENDDATE").toLocalDate();

                    if (reservedStart.isAfter(endDate) || reservedEnd.isBefore(startDate)) {
                        roomsList.add(room);
                    }
                }

                if (!roomReserved)
                {
                    roomsList.add(room);
                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return roomsList;
    }

    /**
     * Test Function:
     * Prints information about the cruise, including its name and details of each room.
     */
    public void printCruise() {
        System.out.println(name);
        for (Room r : roomList) {
            r.printRoomInfo();
        }
    }

    /**
     * Adds a room to the cruise's database.
     *
     * @param room The room to be added.
     * @return True if the room is successfully added, false otherwise.
     */
    public boolean addRoom(Room room) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + name +
                            "(BEDNUMBER, BEDTYPE, ROOMTYPE, ISSMOKING) " +
                            "VALUES(?, ?, ?, ?)");

            statement.setInt(1, room.getNumBeds());
            statement.setString(2, room.getBedType().toString());
            statement.setString(3, room.getQuality().toString());
            statement.setBoolean(4, room.isSmoking());

            statement.executeUpdate();



        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Modifies the details of an existing room in the cruise's database.
     *
     * @param room The modified room.
     * @return True if the room is successfully modified, false otherwise.
     */
    public boolean modifyRoom(Room room) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            String sql = "UPDATE " + name +
                    " SET BEDNUMBER = ?, BEDTYPE = ?, ROOMTYPE = ?, ISSMOKING = ? " +
                    "WHERE ID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, room.getNumBeds());
            statement.setString(2, room.getBedType().toString());
            statement.setString(3, room.getQuality().toString());
            statement.setBoolean(4, room.isSmoking());
            statement.setInt(5,room.getID());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a Cruise object from the database based on the provided cruise name.
     *
     * @param cruiseName The name of the cruise.
     * @return An Optional containing the Cruise object or empty if not found.
     */
    public static Optional<Cruise> getCruise(String cruiseName)
    {
        Cruise cruise = new Cruise(cruiseName);
        Connection connection = null;
        ArrayList<Room> roomList = new ArrayList<Room>();

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + cruiseName);

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                Room room = new Room();
                room.setID(rs.getInt("ID"));
                room.setNumBeds(rs.getInt("BEDNUMBER"));
                room.setBedType(Room.BedType.valueOf(rs.getString("BEDTYPE")));
                room.setQuality(Room.Quality.valueOf(rs.getString("ROOMTYPE")));
                room.setSmoking(rs.getBoolean("ISSMOKING"));

                roomList.add(room);
            }

            cruise.setRoomList(roomList);
            connection.close();
            return Optional.of(cruise);

        } catch (SQLSyntaxErrorException e)
        {
            return Optional.empty();
        }catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieves the travel path for the cruise from the database and populates the 'travelPath' list with Destination objects.
     * The travel path is initially created with default values and updated with the actual database values.
     */
    public void createTravelPath()
    {
        travelPath = new ArrayList<>();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + name + "PATH"); //Ignore error, works fine

            ResultSet rs = statement.executeQuery();

            int daysElapsed = 7;

            while (rs.next())
            {
                String place = rs.getString("LOCATION");
                int stepTime = rs.getInt("TRAVELDAYS");
                daysElapsed += 1;
                LocalDate departure = LocalDate.now(clock).plusDays(daysElapsed);
                LocalDate arrival = LocalDate.now(clock).plusDays(stepTime+daysElapsed);
                daysElapsed += stepTime;
                Destination step = new Destination(place, departure, arrival);
                travelPath.add(step);
            }

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the 'travelPath' list with actual values from the database, considering arrival dates and travel times.
     */
    public void updateTravelPath()
    {
        travelPath = new ArrayList<>();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + name + "PATH"); //Ignore error, works fine

            ResultSet rs = statement.executeQuery();

            while (rs.next())
            {
                LocalDate arrival = rs.getDate("ARRIVALDATE").toLocalDate();
                String locationName = rs.getString("LOCATION");
                int stepTravelTime = rs.getInt("TRAVELDAYS");

                LocalDate departure = arrival.minusDays(stepTravelTime);

                Destination step = new Destination(locationName, departure, arrival);
                travelPath.add(step);

            }

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the current 'travelPath' list to the database by updating the arrival dates of each destination.
     */
    public void saveTravelPath()
    {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + name + "PATH SET ARRIVALDATE = ? WHERE LOCATION = ?"); //Ignore error, works fine

            for (Destination step : travelPath)
            {
                statement.setDate(1, Date.valueOf(step.arrival));
                statement.setString(2, step.location);
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test Function:
     * Prints the travel path of the cruise, including destinations, departure, and arrival dates.
     */
    public void printTravelPath()
    {
        for (Destination step : travelPath)
        {
            System.out.print(step.location + " -- ");
            System.out.print(step.departure + " -- ");
            System.out.println(step.arrival);
        }
    }

    /**
     * Retrieves the current travel path of the cruise.
     *
     * @return An ArrayList of Destination objects representing the travel path.
     */
    public ArrayList<Destination> getTravelPath(){
        return travelPath;
    }


    /**
     * Retrieves a list of valid reservation start dates based on the departure dates of the travel path destinations.
     *
     * @return An ArrayList of LocalDate objects representing valid reservation start dates.
     */
    public ArrayList<LocalDate> getValidReservationDates()
    {
        ArrayList<LocalDate> dates = new ArrayList<>();

        for (Destination step : travelPath)
        {
            dates.add(step.departure.minusDays(1));
        }

        return dates;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<Room> roomList) {
        this.roomList = roomList;
    }

    public class Destination {
        public LocalDate arrival;
        public LocalDate departure;
        public String location;

        /**
         * Constructor for creating a Destination object.
         *
         * @param location  The name of the destination.
         * @param departure The departure date from the destination.
         * @param arrival   The arrival date at the destination.
         */
        public Destination(String location, LocalDate departure, LocalDate arrival) {
            this.arrival = arrival;
            this.departure = departure;
            this.location = location;
        }

        /**
         * Retrieves the location name of the destination.
         *
         * @return The location name.
         */
        public String getLocation(){
            return location;
        }

    }

}