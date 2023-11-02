package Cruise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.time.*;

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

    private int roomNum;
    private int numBeds;
    private Room.BedType bedType;
    private Room.Quality quality;
    private boolean isSmoking;

    public Room() {}

    public Room(int ID, int numBeds, Room.BedType bedType, Room.Quality quality,
                boolean isSmoking) {
        this.quality = quality;
        this.bedType = bedType;
        this.isSmoking = isSmoking;
        this.roomNum = ID;
        this.numBeds = numBeds;
        //this.roomStatus = rs;
    }

    public void printRoomInfo() {
        System.out.println(roomNum);
        System.out.println("  # of Beds: " + numBeds);
        System.out.println("  Bed Type: " + bedType);
        System.out.println("  Cruise.Room Quality: " + quality);
        if (!isSmoking) {
            System.out.println("  Non-Smoking");
        }
        else {
            System.out.println("  Smoking");
        }
    }

    public double getMaximumDailyRate() {
        double maxDailyRate = 0.0, factor = 0.0, total = 0.0;
        if (this.quality == Quality.ECONOMY) {
            maxDailyRate = 154.99;
        }
        else if (this.quality == Quality.COMFORT) {
            maxDailyRate = 324.99;
        }
        else if (this.quality == Quality.BUSINESS) {
            maxDailyRate = 549.99;
        }
        else {
            maxDailyRate = 720.00;
        }

        total = maxDailyRate;
        factor = numBedsFactor();
        maxDailyRate *= factor;
        total += maxDailyRate;

        return total;
    }

    public double getTotalCost(LocalDate startDate, LocalDate endDate)
    {
        double rate = getMaximumDailyRate();

        long timeDifference = ChronoUnit.DAYS.between(startDate, endDate);

        return rate*timeDifference;

    }

    public double numBedsFactor() {
        double factor = 0.0;

        switch (numBeds) {
            case 0:
                factor = 0.09;
            case 1:
                factor = 0.10;
            case 2:
                factor = 0.11;
            case 3:
                factor = 0.12;
        }

        return factor;
    }

    public static Room getRoom(String cruiseName, int id, Connection connection)
    {
        try {
            PreparedStatement roomQuery = connection.prepareStatement(
                    "SELECT * FROM "+ cruiseName +" WHERE ID = ?");

            roomQuery.setInt(1, id);

            ResultSet roomSet = roomQuery.executeQuery();

            roomSet.next();

            return new Room(id,
                    roomSet.getInt("BEDNUMBER"),
                    BedType.valueOf(roomSet.getString("BEDTYPE")),
                    Quality.valueOf(roomSet.getString("ROOMTYPE")),
                    roomSet.getBoolean("ISSMOKING"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
}