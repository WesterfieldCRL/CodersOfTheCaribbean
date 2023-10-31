package Cruise;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class Cruise {
    private String name;
    private ArrayList<Room> roomList;

    //TODO: add travel path

    public Cruise(String name) {
        this.name = name;
        roomList = new ArrayList<>();
    }

    public Optional<Room> isRoomAvailable(Room.Quality quality, int numBeds, Room.BedType bedType,
                                     boolean isSmoking, Date startDate, Date endDate)
    {

        Connection connection = null;
        if (!startDate.after(endDate)) {
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
                        Date reservedStart = rs.getDate("STARTDATE");
                        Date reservedEnd = rs.getDate("ENDDATE");

                        if (!reservedStart.after(endDate) && !reservedEnd.before(startDate)) {
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

        return Optional.empty();
    }

    public ArrayList<Room> getRoomsList(Date startDate, Date endDate)
    {
        return null;
    }

    public void printCruise() {
        System.out.println(name);
        // TODO: TRAVEL PATH
        for (Room r : roomList) {
            r.printRoomInfo();
        }
    }

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
            return Optional.of(cruise);

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public class Destination {
        Date arrival;
        Date departure;
        String location;

    }

}