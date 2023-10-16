public class Room {

    public enum Quality {
        ECONOMY,
        COMFORT,
        BUSINESS,
        EXECUTIVE
    }
    public enum BedType {
        TWIN,
        FULL,
        QUEEN,
        KING
    }

    public enum RoomStatus {
        NOT_RESERVED,
        RESERVED,
        ON_BOARD,
        DONE
    }

    private Room.Quality quality;
    private Room.BedType bedType;
    private int roomNum;
    private boolean isSmoking;
    private int numBeds;
    private Room.RoomStatus roomStatus;
    public Room(Room.Quality quality, Room.BedType bedType, boolean isSmoking,
                int ID, int numBeds, Room.RoomStatus rs) {
        this.quality = quality;
        this.bedType = bedType;
        this.isSmoking = isSmoking;
        this.roomNum = ID;
        this.numBeds = numBeds;
        this.roomStatus = rs;
    }

    public void printRoomInfo() {
        System.out.println(roomNum);
        System.out.println("  # of Beds: " + numBeds);
        System.out.println("  Bed Type: " + bedType);
        System.out.println("  Room Quality: " + quality);
        if (!isSmoking) {
            System.out.println("  Non-Smoking");
        }
        else {
            System.out.println("  Smoking");
        }
        System.out.println("  Status: " + roomStatus);
    }

    public double getDailyRate()  { //TODO: determine method for calculating daily rate
        double dailyRate = 0.0;
        if (this.quality == Quality.ECONOMY) {

        }
        else if (this.quality == Quality.COMFORT) {

        }
        else if (this.quality == Quality.BUSINESS) {

        }
        else {

        }

        return dailyRate;
    }

    public double calculateNumBeds() {

        return 0.0;
    }


    public void modifyInfo() //TODO: implement function
    {

    }


    //Getters and setters
    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public int getID() {
        return roomNum;
    }

    public void setID(int ID) {
        this.roomNum = ID;
    }

    public boolean isSmoking() {
        return isSmoking;
    }

    public void setSmoking(boolean smoking) {
        isSmoking = smoking;
    }

    public int getNumBeds() {
        return numBeds;
    }

    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

    public RoomStatus isReserved() {
        return roomStatus;
    }

    public void setReserved(RoomStatus rs) {
        roomStatus = rs;
    }

    public void setRoomNum(int roomNum){
        this.roomNum = roomNum;
    }

    public int getRoomNum(){
        return this.roomNum;
    }

}