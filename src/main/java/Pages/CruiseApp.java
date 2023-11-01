package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.LandingPage.*;
import static Pages.LoginPage.*;
import static Pages.CreateAccountPage.*;
import static Pages.GuestAccountPage.*;
import static Pages.TravelAgentAccountPage.*;
import static Pages.TermsOfServicePanel.*;
import static Pages.AdminAccountPage.*;

public class CruiseApp {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = setupMainFrame();
            JPanel landingPagePanel = createLandingPagePanel(frame);
            JPanel loginPanel = createLoginPanel(frame);
            JPanel accountPanel = createAccountPanel(frame);
            JPanel guestViewPanel = createGuestViewPanel(frame);
            JPanel travelAgentViewPanel = createTravelAgentViewPane(frame);
            JPanel termsPanel = createTermsOfServicePanel(frame);
            JPanel adminViewPanel = createAdminViewPanel(frame);

            frame.add(termsPanel, "Terms of Service");
            frame.add(landingPagePanel, "Landing Page");
            frame.add(loginPanel, "Login");
            frame.add(accountPanel, "Create Account");
            frame.add(guestViewPanel, "Guest View");
            frame.add(travelAgentViewPanel, "Travel Agent");
            frame.add(adminViewPanel, "Admin");


            frame.setVisible(true);
        });
    }
    private static JFrame setupMainFrame() {
        JFrame frame = new JFrame("Cruise Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }
}