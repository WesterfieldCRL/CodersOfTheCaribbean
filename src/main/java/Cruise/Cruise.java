package Cruise;

import Person.Admin;
import Person.Guest;
import Person.Manager;
import Person.TravelAgent;

import java.io.*;
import java.util.*;

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
        //Get first room that matches criteria
        Optional<Room> optionalRoom = getRoom(quality, numBeds, bedType, isSmoking);

        if (optionalRoom.isPresent())
        {
            //Check to see if the room is reserved



            //Check to see if dates work with travel path

        }
        return optionalRoom;
    }

    protected Optional<Room> getRoom(Room.Quality quality, int numBeds, Room.BedType bedType, boolean isSmoking)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader( this.name + ".txt"));
            String line;
            reader.readLine(); //TODO: get travel path here
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                if (Integer.parseInt(col[1]) == numBeds && Room.BedType.valueOf(col[2]).equals(bedType)
                        && Room.Quality.valueOf(col[3]).equals(quality) && Boolean.parseBoolean(col[4]) == isSmoking)
                {
                    Room room = new Room(Integer.parseInt(col[0]), numBeds, bedType, quality, isSmoking);
                    return Optional.of(room);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    //return true if room is reserved
    protected boolean isRoomReserved(Room room, Date startDate, Date endDate)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader( "Reservations.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");
                if (Integer.parseInt(col[1]) == room.getID())
                {

                }
            }
        } catch (IOException | IllegalArgumentException e) {
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

    public void addRoom(Room room, String fileName) {
        roomList.add(room);

        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append("\n" + room.getID() + "," + room.getNumBeds() + "," + room.getBedType() +
                      "," + room.getQuality() + "," + room.isSmoking());
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

    public static Optional<Cruise> getCruise(String cruiseName)
    {
        Cruise cruise = new Cruise(cruiseName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cruiseName + ".csv"));
            String line;
            reader.readLine(); //TODO: get travel path here
            while ((line = reader.readLine()) != null) {
                String[] col = line.split(",");

                int id = Integer.parseInt(col[0]);
                int numBeds = Integer.parseInt(col[1]);
                Room.BedType bedType = Room.BedType.valueOf(col[2]);
                Room.Quality quality = Room.Quality.valueOf(col[3]);
                boolean isSmoking = Boolean.parseBoolean(col[4]);


                Room r = new Room(id, numBeds, bedType, quality, isSmoking);

                cruise.roomList.add(r);
            }
            return Optional.of(cruise);
        } catch (IOException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}