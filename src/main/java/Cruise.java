public class Cruise
{
    private String name;
    private Room[] roomList;
    private TravelPath travelPath;
    public Cruise(String name)
    {
        this.name = name;
        roomList = new Room[main.numRooms];
        travelPath = new TravelPath();
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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room[] getRoomList() {
        return roomList;
    }

    public void setRoomList(Room[] roomList) {
        this.roomList = roomList;
    }

    public TravelPath getTravelPath() {
        return travelPath;
    }

    public void setTravelPath(TravelPath travelPath) {
        this.travelPath = travelPath;
    }
}