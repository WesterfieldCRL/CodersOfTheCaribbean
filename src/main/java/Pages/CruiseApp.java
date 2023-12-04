package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.LandingPage.*;
import static Pages.LoginPage.*;
import static Pages.CreateAccountPage.*;

/**
 * The {@code CruiseApp} class serves as the main driver for the application.
 *
 * <p>This class contains the application's {@code main} method, which initializes the graphical user interface
 * and sets up the primary JFrame for the application. The class is responsible for creating and managing
 * the different panels of the application, including the landing page, login, and account creation interfaces.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *   <li>Initializing the main application window and setting its properties.</li>
 *   <li>Adding different functional panels to the application's main frame.</li>
 * </ul>
 */
public class CruiseApp {
    /**
     * The main entry point for the cruise application.
     *
     * <p>The method constructs the main application window and initializes various functional panels:</p>
     *
     * <ul>
     *   <li>Creates the main application window (JFrame) using the {@code setupMainFrame} method.</li>
     *   <li>Initializes the 'Landing Page' panel as the initial view of the application.</li>
     *   <li>Initializes the 'Login' panel for user authentication.</li>
     *   <li>Initializes the 'Create Account' panel for new user account registration.</li>
     * </ul>
     *
     * @param args command line arguments passed to the application (not utilized)
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = setupMainFrame();
            JPanel landingPagePanel = createLandingPagePanel(frame);
            JPanel loginPanel = createLoginPanel(frame);
            JPanel accountPanel = createAccountPanel(frame);
            frame.add(landingPagePanel, "Landing Page");
            frame.add(loginPanel, "Login");
            frame.add(accountPanel, "Create Account");


            frame.setVisible(true);
        });
    }

    /**
     * Initializes and sets up the main {@code JFrame}.
     *
     * <p>This method configures the primary attributes of the application window:
     * <ul>
     *   <li>Sets the frame's title to "Coders of the Caribbean".</li>
     *   <li>Defines the frame size as 1200x800 pixels.</li>
     *   <li>Applies a {@code CardLayout} to manage panel transitions.</li>
     *   <li>Centers the frame on the screen.</li>
     * </ul>
     * Additionally, the default close operation is set to {@code JFrame.EXIT_ON_CLOSE}, ensuring
     * that the application terminates properly when the frame is closed.</p>
     *
     * @return the fully configured {@code JFrame} for the application
     */
    private static JFrame setupMainFrame() {
        JFrame frame = new JFrame("Coders of the Caribbean");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 900);
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }
}