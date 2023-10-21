package Cruise;

import Person.Guest;

import java.util.ArrayList;
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
        this.totalCost = room.getTotalCost(startDate, endDate);
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