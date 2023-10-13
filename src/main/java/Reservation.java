import Person.Guest;

import java.util.Date;

public class Reservation {
    private Guest guest;
    private Cruise cruise;
    private Room room;
    private Date startDate;
    private Date endDate;
    private double totalCost;

    // Constructor
    public Reservation(Guest guest, Cruise cruise, Room room, Date startDate, Date endDate) {
        this.guest = guest;
        this.cruise = cruise;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = calculateTotalCost();
    }

    // Method to calculate the total cost based on room type and duration of stay
    private double calculateTotalCost() {
        // Implement this method to calculate the cost based on room type and duration
        // You can consider factors like promotion rate, group rate, etc.
        // Example: totalCost = room.getRate() * numberOfNights;
        return 0.0; // Placeholder
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Cruise getCruise() {
        return cruise;
    }

    public void setCruise(Cruise cruise) {
        this.cruise = cruise;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}