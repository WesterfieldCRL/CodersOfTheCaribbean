import Person.*;
import Cruise.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RoomAvailabilityCheckTest {
    @BeforeEach
    public void backupDB()
    {
        try (Connection connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase")) {
            CallableStatement cs = connection.prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
            cs.setString(1, "backup");
            cs.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    public void restoreDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;shutdown=true");

        } catch (SQLException e) {
            //Shutting down db always produces SQLException
            //e.printStackTrace();
        }

        try {
            replaceDatabaseWithBackup();

            // Restart Derby
            DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void replaceDatabaseWithBackup() {
        try {
            File databaseDir = new File("cruiseDatabase");
            File backupDir = new File("backup/cruiseDatabase");

            FileUtils.copyDirectory(backupDir, databaseDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRoomAvailabilityCheckSuccess() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            Room newRoom = new Room(0, -1, Room.BedType.FULL, Room.Quality.BUSINESS, false);
            cruise1.addRoom(newRoom);

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(Room.Quality.BUSINESS, -1, Room.BedType.FULL, false, validDates.get(0), validDates.get(validDates.size()-1));

            assertTrue(optionalRoom.isPresent());
            return;
        }
        fail();
    }


    @Test
    void testRoomAvailabilityCheckFailure() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            Room newRoom = new Room(0, -1, Room.BedType.FULL, Room.Quality.BUSINESS, false);
            cruise1.addRoom(newRoom);

            ArrayList<LocalDate> validDates = cruise1.getValidReservationDates();

            Optional<Room> optionalRoom = cruise1.isRoomAvailable(Room.Quality.BUSINESS, -1, Room.BedType.FULL, false, validDates.get(0), LocalDate.now().plusDays(10000));

            assertFalse(optionalRoom.isPresent());
            return;
        }
        fail();
    }
}
