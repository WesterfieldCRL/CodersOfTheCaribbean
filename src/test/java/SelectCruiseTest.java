import Cruise.Cruise;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SelectCruiseTest {
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
    void testSelectCruise1Success() {
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");
        assertTrue(optionalCruise.isPresent());
    }

    @Test
    void testSelectCruise2Success() {
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE2");
        assertTrue(optionalCruise.isPresent());
    }

    @Test
    void testSelectCruise3Success() {
        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE3");
        assertTrue(optionalCruise.isPresent());
    }

    @Test
    void testSelectCruiseFailure() {
        Optional<Cruise> optionalCruise = Cruise.getCruise("INVALIDCRUISE");
        assertFalse(optionalCruise.isPresent());
    }
}
