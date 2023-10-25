package Cruise;



import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            BufferedReader reader = new BufferedReader(new FileReader( name + ".csv"));
            String line;
            reader.readLine(); //TODO: get travel path here
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                if (Integer.parseInt(col[1]) == numBeds && Room.BedType.valueOf(col[2]).equals(bedType)
                        && Room.Quality.valueOf(col[3]).equals(quality) && Boolean.parseBoolean(col[4]) == isSmoking)
                {   //Find next room matching criteria
                    Room room = new Room(Integer.parseInt(col[0]), numBeds, bedType, quality, isSmoking);

                    if (!isRoomReserved(room, startDate, endDate))
                    {
                        return Optional.of(room);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Invalid data in " + name + ".csv");
            return Optional.empty();
        }



        return Optional.empty();
    }

    //return true if room is reserved
    private boolean isRoomReserved(Room room, Date startDate, Date endDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            BufferedReader reader2 = new BufferedReader(new FileReader( "Reservations.txt"));
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                String[] col2 = line2.split(",");
                if (col2[3].equals(name) && Integer.parseInt(col2[1]) == room.getID())
                {
                    Date reservedStart = simpleDateFormat.parse(col2[5]);
                    Date reservedEnd = simpleDateFormat.parse(col2[6]);

                    if(!reservedStart.after(endDate) && !reservedEnd.before(startDate))
                    {
                        return true;
                    }

                }
            }
        } catch (IOException | IllegalArgumentException | ParseException e) {
            System.out.println("Invalid data in Reservations.txt");
            return true;
        }
        return false;
    }

    public void printCruise() {
        System.out.println(name);
        // TODO: TRAVEL PATH
        for (Room r : roomList) {
            r.printRoomInfo();
        }
    }

    public void addRoom(Room room) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO \"" + name + "\"" +
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
        }

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
                    "SELECT * FROM \"" + cruiseName + "\"");

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