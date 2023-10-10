public class Room {
    public enum Quality { //probably want to flip order so that economy has the lowest value etc.
        EXECUTIVE,
        BUSINESS,
        COMFORT,
        ECONOMY
    }
    public enum BedType {
        TWIN,
        FULL,
        QUEEN,
        KING
    }
    private Room.Quality quality;
    private Room.BedType bedType;
    private int ID;
    private boolean isSmoking;
    private int numBeds;
    private boolean isReserved;
    public Room(Room.Quality quality, Room.BedType bedType, boolean isSmoking, int ID, int numBeds, boolean isReserved) {
        this.quality = quality;
        this.bedType = bedType;
        this.isSmoking = isSmoking;
        this.ID = ID;
        this.numBeds = numBeds;
        this.isReserved = isReserved;
    }

    public double getDailyRate() //TODO: determine method for calculating daily rate
    {
        // THIS IS NOT RIGHT
        double x = 0;
        if (quality == Quality.EXECUTIVE) {
            x = 3.2;
        }
        return x;
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
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

}