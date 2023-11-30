import Cruise.*;
import Person.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class AddRoomTest {
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
    void testAddRoom() {

        Optional<Cruise> optionalCruise = Cruise.getCruise("CRUISE1");

        if (optionalCruise.isPresent())
        {
            Cruise cruise = optionalCruise.get();
            Room room = new Room();
            room.setQuality(Room.Quality.BUSINESS);
            room.setBedType(Room.BedType.FULL);
            room.setSmoking(false);
            room.setNumBeds(200);

            cruise.addRoom(room);

            Connection connection = null;

            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

                connection = DriverManager.getConnection("jdbc:derby:cruiseDatabase;");
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM CRUISE1 WHERE BEDNUMBER = ?"); //Ignore error, works fine

                statement.setInt(1, 200);

                ResultSet rs = statement.executeQuery();
                if (rs.next())
                {
                    assertTrue(true);
                    return;
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
        fail();

    }
}
