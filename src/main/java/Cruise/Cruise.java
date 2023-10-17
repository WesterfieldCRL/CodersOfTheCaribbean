package Cruise;

import java.io.*;
import java.util.*;

public class Cruise {
    private String name;
    private ArrayList<Room> roomList;
    private TravelPath travelPath;

    private Room.Quality getQualityEnum(Integer quality) {
        switch (quality) {
            case 0:
                return Room.Quality.ECONOMY;
            case 1:
                return Room.Quality.COMFORT;
            case 2:
                return Room.Quality.BUSINESS;
            case 3:
                return Room.Quality.EXECUTIVE;
            default:
                throw new IllegalArgumentException("Invalid Quality: " + quality);
        }
    }

    private Room.BedType getBedTypeEnum(Integer bedType) {
        switch (bedType) {
            case 0:
                return Room.BedType.TWIN;
            case 1:
                return Room.BedType.FULL;
            case 2:
                return Room.BedType.QUEEN;
            case 3:
                return Room.BedType.KING;
            default:
                throw new IllegalArgumentException("Invalid Bed Type: " + bedType);
        }
    }

    private Room.RoomStatus getRoomStatusEnum(Integer status) {
        switch (status) {
            case 0:
                return Room.RoomStatus.NOT_RESERVED;
            case 1:
                return Room.RoomStatus.RESERVED;
            case 2:
                return Room.RoomStatus.ON_BOARD;
            case 3:
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
            Integer bt = Integer.parseInt(col[2]);
            Integer q = Integer.parseInt(col[3]);
            boolean isSmoking = Boolean.parseBoolean(col[4]);
            Integer s = Integer.parseInt(col[5]);

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
}