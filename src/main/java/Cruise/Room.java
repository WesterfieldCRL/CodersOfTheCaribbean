package Cruise;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
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

    /**
     * Default constructor for creating a Room object.
     */
    public Room() {}

    /**
     * Parameterized constructor for creating a Room object with specified attributes.
     *
     * @param ID          The room number.
     * @param numBeds     The number of beds in the room.
     * @param bedType     The type of bed in the room (TWIN, FULL, QUEEN, KING).
     * @param quality     The quality level of the room (ECONOMY, COMFORT, BUSINESS, EXECUTIVE).
     * @param isSmoking   True if the room is smoking, false otherwise.
     */
    public Room(int ID, int numBeds, Room.BedType bedType, Room.Quality quality,
                boolean isSmoking) {
        this.quality = quality;
        this.bedType = bedType;
        this.isSmoking = isSmoking;
        this.roomNum = ID;
        this.numBeds = numBeds;
        //this.roomStatus = rs;
    }

    /**
     * Prints information about the room including room number, number of beds, bed type, quality, and smoking status.
     */
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

    /**
     * Calculates and returns the maximum daily rate for the room based on its quality, number of beds, and a base rate for each quality level.
     *
     * @return The maximum daily rate for the room.
     */
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

    /**
     * Calculates and returns the total cost for staying in the room between the specified start and end dates.
     *
     * @param startDate The start date of the stay.
     * @param endDate   The end date of the stay.
     * @return The total cost for the stay in the room.
     */
    public double getTotalCost(LocalDate startDate, LocalDate endDate) {
        double rate = getMaximumDailyRate();
        long timeDifference = ChronoUnit.DAYS.between(startDate, endDate);

        BigDecimal totalCost = new BigDecimal(rate * timeDifference);
        totalCost = totalCost.setScale(2, RoundingMode.HALF_EVEN);

        return totalCost.doubleValue();
    }

    /**
     * Calculates and returns a factor based on the number of beds in the room.
     *
     * @return The factor based on the number of beds.
     */
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

    /**
     * Retrieves a Room object from the database based on the provided cruise name and room ID.
     *
     * @param cruiseName The name of the cruise.
     * @param id         The ID of the room.
     * @return A Room object retrieved from the database or null if not found.
     */
    public static Room getRoom(String cruiseName, int id) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

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


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the quality level of the room.
     *
     * @return The quality level of the room (ECONOMY, COMFORT, BUSINESS, EXECUTIVE).
     */
    public Quality getQuality() {
        return quality;
    }

    /**
     * Sets the quality level of the room.
     *
     * @param quality The new quality level to set for the room.
     */
    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    /**
     * Retrieves the type of bed in the room.
     *
     * @return The type of bed in the room (TWIN, FULL, QUEEN, KING).
     */
    public BedType getBedType() {
        return bedType;
    }

    /**
     * Sets the type of bed in the room.
     *
     * @param bedType The new bed type to set for the room (TWIN, FULL, QUEEN, KING).
     */
    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    /**
     * Retrieves the room number.
     *
     * @return The room number.
     */
    public int getID() {
        return roomNum;
    }

    /**
     * Sets the room number.
     *
     * @param ID The new room number to set.
     */
    public void setID(int ID) {
        this.roomNum = ID;
    }

    /**
     * Checks if the room is designated as a smoking room.
     *
     * @return True if the room is smoking, false otherwise.
     */
    public boolean isSmoking() {
        return isSmoking;
    }

    /**
     * Sets the smoking status of the room.
     *
     * @param smoking True to set the room as smoking, false for non-smoking.
     */
    public void setSmoking(boolean smoking) {
        isSmoking = smoking;
    }

    /**
     * Retrieves the number of beds in the room.
     *
     * @return The number of beds in the room.
     */
    public int getNumBeds() {
        return numBeds;
    }

    /**
     * Sets the number of beds in the room.
     *
     * @param numBeds The new number of beds to set for the room.
     */
    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }

}