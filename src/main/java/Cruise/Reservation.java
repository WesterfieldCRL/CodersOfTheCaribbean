package Cruise;

import Person.Guest;

import java.util.ArrayList;
import java.util.Date;

public class Reservation {
    private Guest guest;
    //private Cruise cruise;
    private String cruiseName;
    private Room room;
    private Date startDate;
    private Date endDate;
    private Date reservedDate;
    private double totalCost;

    // Constructor
    public Reservation(Guest guest, String cruiseName, Room room, Date startDate, Date endDate, Date reservedDate) {
        this.guest = guest;
        this.cruiseName = cruiseName;
        //this.cruise = cruise;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = room.getTotalCost(startDate, endDate);
        this.reservedDate = reservedDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /*public Cruise getCruise() {
        return cruise;
    }

    public void setCruise(Cruise cruise) {
        this.cruise = cruise;
    }*/

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

    public String getCruiseName() {
        return cruiseName;
    }

    public void setCruiseName(String cruiseName) {
        this.cruiseName = cruiseName;
    }
}