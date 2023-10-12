import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Cruise {
    private String name;
    private ArrayList<Room> roomList;
    private TravelPath travelPath;
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.name = scanner.nextLine();
        scanner.nextLine();
        // TODO: TRAVEL PATH
        //this.travelPath = getTravelPath(); implemented later

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] col = line.split(",");

            int roomNum = Integer.parseInt(col[0]);
            int numBeds = Integer.parseInt(col[1]);
            Room.BedType bedType = Room.BedType.valueOf(col[2]);
            Room.Quality quality = Room.Quality.valueOf(col[3]);
            boolean isSmoking = Boolean.parseBoolean(col[4]);
            Room.RoomStatus status = Room.RoomStatus.valueOf(col[5]);

            Room r = new Room(quality, bedType, isSmoking, roomNum, numBeds, status);

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

// RANDOM METHOD OF MAKING ROOMS ON CRUISE
        /*
        for (int i = 0; i < main.numRooms; i++)
        {
            int randomQuality = (int) (Math.random() * 4) + 1;
            roomList[i].setNumBeds(randomQuality);
            roomList[i].setID(i+1);
            switch (randomQuality) {
                case 1 -> {
                    roomList[i].setQuality(Room.Quality.EXECUTIVE);
                    roomList[i].setBedType(Room.BedType.KING);
                }
                case 2 -> {
                    roomList[i].setQuality(Room.Quality.BUSINESS);
                    roomList[i].setBedType(Room.BedType.QUEEN);
                }
                case 3 -> {
                    roomList[i].setQuality(Room.Quality.COMFORT);
                    roomList[i].setBedType(Room.BedType.FULL);
                }
                case 4 -> {
                    roomList[i].setQuality(Room.Quality.ECONOMY);
                    roomList[i].setBedType(Room.BedType.TWIN);
                }
                default -> throw new IllegalStateException("Unexpected value: " + randomQuality);
            }
        }
         */