package Person;

import java.sql.*;
import java.util.ArrayList;
import java.time.*;
import java.util.List;


import Billing.BillingInformation;
import Cruise.*;

/**
 * The {@code Guest} class represents a guest in the cruise booking system.
 *
 * <p>This class extends the {@code Person} class and provides additional functionality specific to guests,
 * such as account creation and handling password reset requests.</p>
 *
 * <p>Key features of the {@code Guest} class include:</p>
 * <ul>
 *   <li>Account creation for guests using the {@code createAccount} method.</li>
 *   <li>Retrieval of password reset requests from the database using the {@code getResetRequests} method.</li>
 * </ul>
 */
public class Guest extends Person {
    BillingInformation creditCard;
    private ArrayList<Reservation> reservations;

    private Clock clock;
    private String changedPassword;

    /**
     * Constructs a new {@code Guest} object with the specified attributes.
     * <p>
     * This constructor initializes a guest with the provided username, password,
     * name, address, and email by calling the constructor of the superclass,
     * {@code User}, with the same parameters.
     * Additionally, it initializes the reservations list as an empty ArrayList
     * and sets the system default time zone to the guest's clock.
     * </p>
     *
     * @param username the username for the guest.
     * @param password the password for the guest's account.
     * @param name the name of the guest.
     * @param address the address of the guest.
     * @param email the email address of the guest.
     */
    public Guest(String username, String password, String name, String address, String email, String number, YearMonth expirationDate, Boolean isCorporateGuest) {
        super(username, password, name, address, email);
        this.reservations = new ArrayList<>();
        clock = Clock.systemDefaultZone();
        creditCard= new BillingInformation(number, expirationDate, isCorporateGuest);
    }
    public Guest(String username, String password, String name, String address, String email){
        this(username,password,name,address,email,"",YearMonth.now(), false);
    }

    /**
     * Checks whether a given username already exists in the 'LOGINDATA' table of the database.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC, executes a SELECT query
     * to search for the provided username in the 'LOGINDATA' table, and returns
     * {@code true} if the username exists, and {@code false} otherwise.
     * </p>
     *
     * @param username the username to check for existence in the database.
     * @return {@code true} if the username exists, {@code false} otherwise.
     */
    public static boolean usernameExists(String username) {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement searchQuery = connection.prepareStatement(
                    "SELECT USERNAME FROM LOGINDATA WHERE USERNAME = ?");
            searchQuery.setString(1, username);

            ResultSet rs = searchQuery.executeQuery();
            return rs.next();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new guest account by invoking the {@code createGenericAccount} method
     * with the account type set to "GUEST".
     * <p>
     * This method acts as a convenient wrapper for the generic account creation process,
     * specifically tailored for creating guest accounts.
     * </p>
     *
     * @return {@code true} if the guest account is successfully created, {@code false} otherwise.
     * @see #createGenericAccount(String)
     */
    public boolean createAccount()
    {

       if( !createGenericAccount("GUEST")){
           return false;
       }



            Connection connection = null;

            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
                PreparedStatement insertQuery = connection.prepareStatement("INSERT INTO BILLING_INFO " +
                        "(GUEST, NUMBER, EXPIRATION_DATE, IS_CORPORATE_GUEST) " +
                        "VALUES (?, ?, ?,?)");
                insertQuery.setString(1, this.getUsername());
                insertQuery.setString(2, creditCard.getCreditCardNumber());
                LocalDate localDate = creditCard.getCreditCardExpirationDate().atDay(1);
                insertQuery.setDate(3, Date.valueOf(localDate));
                insertQuery.setBoolean(4, creditCard.getIsCorporateGuest());

                insertQuery.executeUpdate();
                connection.close();
                return true;


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return false;


    }

    /**
     * Submits a password reset request for a given username in the 'LOGINDATA' table.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Checks if the provided username exists in the 'LOGINDATA' table.</li>
     *   <li>If the username exists, checks if a password reset request for the user already exists.</li>
     *   <li>If no existing password reset request is found, inserts a new entry in the 'PASSWORDRESETS' table.</li>
     * </ol>
     *
     * @param username the username for which the password reset request is submitted.
     * @param password the new password to be set after the reset.
     */
    public static void resetRequest(String username, String password)
    {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            //Check that username exists
            PreparedStatement searchQuery = connection.prepareStatement(
                    "SELECT USERNAME FROM LOGINDATA WHERE USERNAME = ?");

            searchQuery.setString(1, username);

            ResultSet rs = searchQuery.executeQuery();

            if (rs.next())
            {

                //Check if request has already been submitted exists
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM PASSWORDRESETS WHERE USERNAME = ?");
                statement.setString(1, username);

                ResultSet rs2 = statement.executeQuery();

                if (!rs2.next()) {

                    PreparedStatement insertQuery = connection.prepareStatement(
                            "INSERT INTO PASSWORDRESETS (USERNAME, NEWPASSWORD) VALUES (?, ?)");

                    insertQuery.setString(1, username);
                    insertQuery.setString(2, password);

                    insertQuery.executeUpdate();
                }
            }

        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The {@code resetPassword} method is responsible for updating the password
     * in the database for a given user and removing any corresponding password
     * reset entries.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Establishes a connection to the database.</li>
     *   <li>Updates the user's password in the 'LOGINDATA' table.</li>
     *   <li>Deletes any password reset entries for the user in the 'PASSWORDRESETS' table.</li>
     *   <li>Commits the changes to the database.</li>
     * </ol>
     * If an exception occurs during the process, it prints the stack trace and
     * attempts to rollback the changes in the case of a SQLException.
     * <p>
     * Finally, it ensures that the database connection is closed.
     */
    public void resetPassword() {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            connection.setAutoCommit(false);

            PreparedStatement updateQuery = connection.prepareStatement(
                    "UPDATE LOGINDATA SET PASSWORD = ? WHERE USERNAME = ?");
            updateQuery.setString(1, this.getChangedPassword());
            updateQuery.setString(2, this.getUsername());
            updateQuery.executeUpdate();

            PreparedStatement deleteQuery = connection.prepareStatement(
                    "DELETE FROM PASSWORDRESETS WHERE USERNAME = ?");
            deleteQuery.setString(1, this.getUsername());
            deleteQuery.executeUpdate();

            connection.commit();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Writes a new reservation entry to the 'RESERVATIONS' table in the database.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and inserts a new reservation
     * entry with the specified details into the 'RESERVATIONS' table. It includes the username,
     * room ID, total cost, start date, end date, reservation date, and cruise name.
     * </p>
     *
     * @param start  the start date of the reservation.
     * @param end    the end date of the reservation.
     * @param cruise the cruise associated with the reservation.
     * @param room   the room to be reserved.
     * @return {@code true} if the reservation is successfully written to the database, {@code false} otherwise.
     * @see Room#getTotalCost(LocalDate, LocalDate)
     */
    public boolean writeReservation(LocalDate start, LocalDate end, Cruise cruise, Room room) {
        Clock clock = Clock.systemDefaultZone();
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement insertReservationQuery = connection.prepareStatement(
                    "INSERT INTO RESERVATIONS (USERNAME, ROOMID, COST, STARTDATE, ENDDATE, DATERESERVED, CRUISE) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertReservationQuery.setString(1, this.getUsername());
            insertReservationQuery.setInt(2, room.getID());
            insertReservationQuery.setDouble(3, room.getTotalCost(start, end));
            insertReservationQuery.setDate(4, Date.valueOf(start));
            insertReservationQuery.setDate(5, Date.valueOf(end));
            insertReservationQuery.setDate(6, Date.valueOf(LocalDate.now(clock)));
            insertReservationQuery.setString(7, cruise.getName());

            insertReservationQuery.executeUpdate();

            ResultSet generatedKeys = insertReservationQuery.getGeneratedKeys();
            if (generatedKeys.next()) {
                int reservationId = generatedKeys.getInt(1);

                PreparedStatement insertCheckinQuery = connection.prepareStatement(
                        "INSERT INTO CHECKIN (ID, ISCHECKEDIN, ISCHECKEDOUT) VALUES (?, false, false)");
                insertCheckinQuery.setInt(1, reservationId);
                insertCheckinQuery.executeUpdate();
                insertCheckinQuery.close();
            }

            insertReservationQuery.close();
            connection.close();
            generateBilling(room.getTotalCost(start, end));
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
    public void generateBilling(double cost){

        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement insertQuery = connection.prepareStatement("INSERT INTO BILLS" +
                    "(GUEST, DATE, AMOUNT, ERROR_DESCRIPTION)" +
                    "VALUES (?, ?, ?, ?)");
            LocalDateTime currentDateTime = LocalDateTime.now();


            LocalDate currentDate = currentDateTime.toLocalDate();
            insertQuery.setString(1, this.getUsername());
            insertQuery.setDate(2, Date.valueOf(currentDate));
            insertQuery.setDouble(3, cost);
            insertQuery.setString(4, "");
            insertQuery.executeUpdate();
            connection.close();


        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public double calculateRefund(int reservationId){
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement selectQuery = connection.prepareStatement("SELECT * FROM RESERVATIONS WHERE ID = ?");
            selectQuery.setInt(1, reservationId);
            ResultSet rs = selectQuery.executeQuery();

            if (rs.next()) {
                LocalDate dateReservationMade = rs.getDate("DATERESERVED").toLocalDate();
                LocalDate currentDate = LocalDate.now();
                Period period = Period.between(dateReservationMade, currentDate);
                double refundCharge = 0.0;
                if (period.getDays() > 2) {
                    int ID= rs.getInt("ROOMID");
                    String cruiseNum = rs.getString("CRUISE");
                    PreparedStatement selectRoom;
                    if (cruiseNum.equals("cruise1")){
                        selectRoom = connection.prepareStatement("SELECT * FROM CRUISE1 WHERE ID = ?");
                    }else if (cruiseNum.equals("CRUISE2")){
                        selectRoom = connection.prepareStatement("SELECT * FROM CRUISE2 WHERE ID = ?");
                    }else {
                        selectRoom = connection.prepareStatement("SELECT * FROM CRUISE3 WHERE ID = ?");
                    }
                    selectRoom.setInt(1,ID);
                    ResultSet rsRoom = selectRoom.executeQuery();
                    int tempBedNum = rsRoom.getInt("BEDNUMBER");
                    Room.BedType tempBedType = Room.BedType.valueOf(rsRoom.getString("BEDTYPE"));
                    Room.Quality tempQuality = Room.Quality.valueOf(rsRoom.getString("ROOMTYPE"));
                    boolean tempSmoke = rsRoom.getBoolean("ISSMOKING");


                    Room temp = new Room(ID, tempBedNum, tempBedType, tempQuality, tempSmoke);
                    refundCharge = temp.getMaximumDailyRate() * 0.8;
                }
                return refundCharge;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1.0;

    }

    /**
     * Initiates the reservation process for a specified room, time period, and cruise.
     * <p>
     * This method facilitates the reservation process by invoking the {@code writeReservation}
     * method to create a reservation entry in the database. It then updates the local reservations
     * list by calling the {@code getReservations} method and returns a boolean value indicating
     * the success of the reservation.
     * </p>
     *
     * @param room   the room to be reserved.
     * @param start  the start date of the reservation.
     * @param end    the end date of the reservation.
     * @param cruise the cruise associated with the reservation.
     * @return {@code true} if the reservation is successfully made, {@code false} otherwise.
     * @see #writeReservation(LocalDate, LocalDate, Cruise, Room)
     * @see #getReservations()
     */
    public boolean makeReservation(Room room, LocalDate start, LocalDate end, Cruise cruise){
        boolean b = writeReservation(start, end, cruise, room);

        getReservations();

        return b;
    }

    /**
     * Cancels a reservation with the specified reservation ID.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Checks if a reservation with the provided reservation ID exists in the 'RESERVATIONS' table.</li>
     *   <li>If the reservation exists, checks if the start date of the reservation is before the current date.</li>
     *   <li>If the reservation's start date is before the current date, the cancellation is not allowed, and {@code false} is returned.</li>
     *   <li>Deletes the reservation entry from the 'RESERVATIONS' table.</li>
     *   <li>Removes the canceled reservation from the local reservations list.</li>
     *   <li>Returns {@code true} if the reservation is successfully canceled, {@code false} otherwise.</li>
     * </ol>
     *
     * @param reservationId the ID of the reservation to be canceled.
     * @return {@code true} if the reservation is successfully canceled, {@code false} otherwise.
     * @see #getReservations()
     * @see Reservation
     */
    public boolean cancelReservation(int reservationId) {
        Connection connection = null;

        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");

            PreparedStatement selectQuery = connection.prepareStatement("SELECT * FROM RESERVATIONS WHERE ID = ?");
            selectQuery.setInt(1, reservationId);
            ResultSet rs = selectQuery.executeQuery();

            if (rs.next()) {
                LocalDate reservedStart = rs.getDate("STARTDATE").toLocalDate();

                if (reservedStart.isBefore(LocalDate.now(clock))) {
                    return false;
                }

                double refundSubtractor = calculateRefund(reservationId);
                double refund = -1.0 * (rs.getDouble("COST") - refundSubtractor);
                generateBilling(refund);

                PreparedStatement deleteCheckinQuery = connection.prepareStatement("DELETE FROM CHECKIN WHERE ID = ?");
                deleteCheckinQuery.setInt(1, reservationId);
                deleteCheckinQuery.executeUpdate();
                deleteCheckinQuery.close();

                PreparedStatement deleteReservationQuery = connection.prepareStatement("DELETE FROM RESERVATIONS WHERE ID = ?");
                deleteReservationQuery.setInt(1, reservationId);
                deleteReservationQuery.executeUpdate();
                deleteReservationQuery.close();

                this.reservations.removeIf(r -> r.id == reservationId);

                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Modifies a reservation with the specified reservation ID, updating details such as the room, dates, and cruise.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC and performs the following steps:
     * </p>
     * <ol>
     *   <li>Checks if the provided start and end dates are valid for the given cruise using {@code Cruise.areDatesValid}.</li>
     *   <li>If the dates are valid, updates the reservation details in the 'RESERVATIONS' table.</li>
     *   <li>Calculates the total cost for the updated reservation based on the new room and date range.</li>
     *   <li>Returns {@code true} if the reservation is successfully modified, {@code false} otherwise.</li>
     * </ol>
     *
     * @param reservationId the ID of the reservation to be modified.
     * @param room the new room for the reservation.
     * @param start the new start date for the reservation.
     * @param end the new end date for the reservation.
     * @param cruise the new cruise for the reservation.
     * @return {@code true} if the reservation is successfully modified, {@code false} otherwise.
     * @see Cruise#areDatesValid(LocalDate, LocalDate)
     * @see Room#getTotalCost(LocalDate, LocalDate)
     */
    public boolean modifyReservation(int reservationId, Room room, LocalDate start, LocalDate end, Cruise cruise) {
        Clock clock = Clock.systemDefaultZone();
        if (cruise.areDatesValid(start,end)) {
            try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;")) {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

                String updateQuery = "UPDATE RESERVATIONS SET ROOMID = ?, COST = ?, STARTDATE = ?, ENDDATE = ?, DATERESERVED = ?, CRUISE = ? WHERE ID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setInt(1, room.getID());
                    preparedStatement.setDouble(2, room.getTotalCost(start, end));
                    preparedStatement.setDate(3, Date.valueOf(start));
                    preparedStatement.setDate(4, Date.valueOf(end));
                    preparedStatement.setDate(5, Date.valueOf(LocalDate.now(clock)));
                    preparedStatement.setString(6, cruise.getName());
                    preparedStatement.setInt(7, reservationId);
                    int affectedRows = preparedStatement.executeUpdate();
                    return affectedRows > 0;
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Retrieves all reservations for the guest from the 'RESERVATIONS' table.
     * <p>
     * This method connects to the 'cruiseDatabase' using JDBC, executes a SELECT query
     * to retrieve all reservations for the guest from the 'RESERVATIONS' table,
     * and returns a list of {@code Reservation} objects representing the guest's reservations.
     * </p>
     *
     * @return a list of {@code Reservation} objects representing the guest's reservations.
     * @see Reservation
     */
    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
            PreparedStatement selectQuery = connection.prepareStatement(
                    "SELECT * FROM RESERVATIONS WHERE USERNAME = ?");

            selectQuery.setString(1, this.getUsername());

            ResultSet rs = selectQuery.executeQuery();

            while (rs.next()) {
                String cruiseName = rs.getString("CRUISE");
                int roomID = rs.getInt("ROOMID");
                LocalDate startDate = rs.getDate("STARTDATE").toLocalDate();
                LocalDate endDate = rs.getDate("ENDDATE").toLocalDate();
                int id = rs.getInt("ID");

                Room room = Room.getRoom(cruiseName, roomID);//removed the connect

                Reservation reservation = new Reservation(cruiseName, room, startDate, endDate, id);
                reservations.add(reservation);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return reservations;
    }

    /**
     * Retrieves the changed password value associated with this object.
     *
     * @return the changed password as a String.
     */
    public String getChangedPassword() {
        return changedPassword;
    }

    /**
     * Sets the changed password for this object.
     *
     * @param changedPassword the new password value to be set.
     */
    public void setChangedPassword(String changedPassword) {
        this.changedPassword = changedPassword;
    }

    /**
     * Retrieves the clock instance associated with this object.
     *
     * @return the Clock instance.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Sets the clock instance for this object.
     *
     * @param clock the new Clock instance to be set.
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * The {@code Reservation} class represents a reservation in the cruise booking system.
     *
     * <p>This class provides a convenient way to store and retrieve reservation details,
     * such as the cruise name, room, start date, end date, and ID.</p>
     *
     * <p>Key features of the {@code Reservation} class include:</p>
     * <ul>
     *   <li>Retrieval of the cruise name using the {@code getCruiseName} method.</li>
     *   <li>Retrieval of the room using the {@code getRoom} method.</li>
     *   <li>Retrieval of the start date using the {@code getStartDate} method.</li>
     *   <li>Retrieval of the end date using the {@code getEndDate} method.</li>
     *   <li>Retrieval of the ID using the {@code getId} method.</li>
     * </ul>
     */
    public class Reservation {
        public String cruiseName;
        public Room room;
        public  LocalDate startDate;
        public LocalDate endDate;
        public int id;

        /**
         * Constructs a new {@code Reservation} object with the specified attributes.
         * <p>
         * This constructor initializes a reservation with the provided cruise name,
         * room, start date, end date, and ID.
         * </p>
         *
         * @param cruiseName the name of the cruise for the reservation.
         * @param room the room for the reservation.
         * @param startDate the start date for the reservation.
         * @param endDate the end date for the reservation.
         * @param id the ID for the reservation.
         */
        public Reservation(String cruiseName, Room room, LocalDate startDate, LocalDate endDate, int id) {
            this.cruiseName = cruiseName;
            this.room = room;
            this.startDate = startDate;
            this.endDate = endDate;
            this.id = id;
        }

        /**
         * Retrieves the cruise name associated with this reservation.
         *
         * @return the cruise name as a String.
         */
        public Room getRoom() {
            return room;
        }

        /**
         * Retrieves the cruise name associated with this reservation.
         *
         * @return the cruise name as a String.
         */
        public String getCruiseName() {
            return cruiseName;
        }

        /**
         * Retrieves the start date associated with this reservation.
         *
         * @return the start date as a LocalDate.
         */
        public LocalDate getStartDate() {
            return startDate;
        }

        /**
         * Retrieves the end date associated with this reservation.
         *
         * @return the end date as a LocalDate.
         */
        public LocalDate getEndDate() {
            return endDate;
        }

        /**
         * Retrieves the ID associated with this reservation.
         *
         * @return the ID as an int.
         */
        public int getId() {
            return id;
        }

        /**
         * Overloaded toString to print out all reservation details
         *
         * @return string of reservation details
         */
        public String toString() {
            return "Reservation ID: " + this.getId() +
                    ", Cruise: " + this.getCruiseName() +
                    ", Room ID: " + this.getRoom().getID() +
                    ", Quality: " + this.getRoom().getQuality() +
                    ", Number of Beds: " + this.getRoom().getNumBeds();
        }
    }
}