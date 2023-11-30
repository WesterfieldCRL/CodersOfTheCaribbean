import Cruise.*;
import Person.*;
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
import static org.junit.jupiter.api.Assertions.fail;

public class RoomManagementTest {
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
    void testRoomManagementSuccess() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            ArrayList<Room> rooms = cruise1.getRoomList();
            Room modifyRoom = rooms.get(0);

            modifyRoom.setNumBeds(200);
            boolean output = cruise1.modifyRoom(modifyRoom);
            assertTrue(output);
            return;
        }
        fail();
    }


    @Test
    void testRoomManagementFailure() {
        Optional<Cruise> cruiseOptional = Cruise.getCruise("CRUISE1");

        if (cruiseOptional.isPresent())
        {
            Cruise cruise1 = cruiseOptional.get();
            ArrayList<Room> rooms = cruise1.getRoomList();
            Room modifyRoom = rooms.get(0);

            modifyRoom.setID(-1);
            boolean output = cruise1.modifyRoom(modifyRoom);
            assertFalse(output);
            return;
        }
        fail();
    }
}
