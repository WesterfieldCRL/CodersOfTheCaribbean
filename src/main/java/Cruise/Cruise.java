package Cruise;

import java.io.*;
import java.util.*;

public class Cruise {
    private String name;
    private ArrayList<Room> roomList;
    private TravelPath travelPath;

    private Room.Quality getQualityEnum(String quality) {
        switch (quality) {
            case "ECONOMY":
                return Room.Quality.ECONOMY;
            case "COMFORT":
                return Room.Quality.COMFORT;
            case "BUSINESS":
                return Room.Quality.BUSINESS;
            case "EXECUTIVE":
                return Room.Quality.EXECUTIVE;
            default:
                throw new IllegalArgumentException("Invalid Quality: " + quality);
        }
    }

    private Room.BedType getBedTypeEnum(String bedType) {
        switch (bedType) {
            case "TWIN":
                return Room.BedType.TWIN;
            case "FULL":
                return Room.BedType.FULL;
            case "QUEEN":
                return Room.BedType.QUEEN;
            case "KING":
                return Room.BedType.KING;
            default:
                throw new IllegalArgumentException("Invalid Bed Type: " + bedType);
        }
    }

    private Room.RoomStatus getRoomStatusEnum(String status) {
        switch (status) {
            case "NOT_RESERVED":
                return Room.RoomStatus.NOT_RESERVED;
            case "RESERVED":
                return Room.RoomStatus.RESERVED;
            case "ON_BOARD":
                return Room.RoomStatus.ON_BOARD;
            case "DONE":
                return Room.RoomStatus.DONE;
            default:
                throw new IllegalArgumentException("Invalid Cruise.Room Status: " + status);
        }
    }

    public Cruise(String name) {
        this.name = name;
        roomList = new ArrayList<>();
        travelPath = new TravelPath();
    }

    public void readCruiseRooms(String fileName) {
        File f = new File(fileName);
        Scanner scanner = null;

        try {
            scanner = new Scanner(f);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.name = scanner.nextLine();
        scanner.nextLine();
        scanner.nextLine();
        // TODO: TRAVEL PATH
        //this.travelPath = getTravelPath(); implemented later

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] col = line.split(",");

            int roomNum = Integer.parseInt(col[0]);
            int numBeds = Integer.parseInt(col[1]);
            String bt = col[2];
            String q = col[3];
            boolean isSmoking = Boolean.parseBoolean(col[4]);
            String s = col[5];

            Room.Quality quality = getQualityEnum(q);
            Room.BedType bedType = getBedTypeEnum(bt);
            Room.RoomStatus status = getRoomStatusEnum(s);

            Room r = new Room(roomNum, numBeds, bedType, quality, isSmoking, status);

            roomList.add(r);
        }
    }

    public void printCruise() {
        System.out.println(name);
        // TODO: TRAVEL PATH
        for (Room r : roomList) {
            r.printRoomInfo();
        }
    }

    public void addRoom(Room room, String fileName) {
        roomList.add(room);

        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append("\n" + room.getRoomNum() + "," + room.getNumBeds() + "," + room.getBedType() +
                      "," + room.getQuality() + "," + room.getSmokingStatus() + "," +
                      room.isReserved());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
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

    public TravelPath getTravelPath() {
        return travelPath;
    }

    public void setTravelPath(TravelPath travelPath) {
        this.travelPath = travelPath;
    }

    public static Cruise searchCruise(String cruiseFile, String requested) {
        File f = new File(cruiseFile);
        Scanner scanner = null;

        try {
            scanner = new Scanner(f);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cruiseNames = line.split(",");

            Cruise c1 = new Cruise(cruiseNames[0]);
            c1.readCruiseRooms(cruiseNames[0] + ".txt");

            Cruise c2 = new Cruise(cruiseNames[1]);
            c2.readCruiseRooms(cruiseNames[1] + ".txt");

            Cruise c3 = new Cruise(cruiseNames[2]);
            c3.readCruiseRooms(cruiseNames[2] + ".txt");

            if(c1.getName().equalsIgnoreCase(requested)){
                return c1;
            }

            if(c2.getName().equalsIgnoreCase(requested)){
                return c2;
            }

            if(c3.getName().equalsIgnoreCase(requested)){
                return c3;
            }
        }

        return null;
    }
}