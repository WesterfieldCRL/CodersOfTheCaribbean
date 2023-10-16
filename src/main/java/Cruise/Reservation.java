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

    public String printReceipt(double cost){
        StringBuilder sb = new StringBuilder();
        String receipt;

        sb.append("-----RECEIPT-----");
        sb.append('\n');
        sb.append("NAME: " + this.getGuest().getName());
        sb.append('\n');
        sb.append("COST: " + cost);
        sb.append('\n');
        //System.out.println("CRUISE: " + cruise.getName()); uncomment when pickCruise implemented
        sb.append('\n');
        sb.append("NIGHTS: " /*nights here*/);
        sb.append('\n');
        sb.append("ROOM: " + this.getRoom().getRoomNum());

        receipt = sb.toString();

        return receipt;
    }

    //specifically for guest making their own reservation
    public void makeReservation(Guest guest){
        //PREREQS: already completed "createGuestAccount()"
        //this function is not entirely finished

        Room room;
        Date nights[];
        Double cost;
        Cruise cruise;
        ArrayList<Room> roomList = new ArrayList<Room>();
        String creditCardNum, expDate;

        //cruise = pickCruise();   allows guest to pick their cruise

        System.out.println("Please select nights to stay:");

      //  roomList = cruise.getRoomList(); uncomment when pickCruise implemented
        //**ADD UI**
        //drop down menu to select dates (or any other way of displaying dates)
        //when chosen, nights = the array of dates chosen

        //payment stuff
        System.out.println("Input Credit Card Number: ");

        System.out.println("Input Expiration Date: ");

        this.setGuest(guest);

        cost = calculateTotalCost();
        //System.out.println("TOTAL COST: " + cost); uncomment when calculateTotalCost implemented

      /*  if(!isCorporate){
            //payment and generate billing
        }
        else{
            //bill to corporation
        }*/

       // this.setRoom(room); uncomment when picking room is implemented
        //this.setCruise(cruise); uncomment when pickCruise implemented
        //this.setStartDate(nights[0]); uncomment when picking room is implemented
       // this.setEndDate(nights[nights.length - 1]); uncomment when picking room is implemented

      //  room.setReserved(Cruise.Room.RoomStatus.RESERVED); uncomment when picking room is implemented

        System.out.println(this.printReceipt(cost));
    }
}