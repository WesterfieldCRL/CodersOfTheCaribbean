import javax.swing.*;
import java.awt.*;

public class CruiseApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cruise Booking System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new CardLayout());

            // Landing Page Panel
            JPanel landingPagePanel = new JPanel(new GridLayout(4, 1, 10, 10));
            landingPagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JButton loginButton = new JButton("Login");
            landingPagePanel.add(new JLabel("Cruise 1"));
            landingPagePanel.add(new JLabel("Cruise 2"));
            landingPagePanel.add(new JLabel("Cruise 3"));
            landingPagePanel.add(loginButton);

            // Login Panel
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.add(new JLabel("Username:"));
            fieldsPanel.add(new JTextField(15));
            fieldsPanel.add(new JLabel("Password:"));
            fieldsPanel.add(new JPasswordField(15));

            JPanel buttonsPanel = new JPanel(new FlowLayout());
            JButton loginSubmitButton = new JButton("Login");
            JButton createAccountButton = new JButton("Create Account");
            buttonsPanel.add(loginSubmitButton);
            buttonsPanel.add(createAccountButton);

            loginPanel.add(fieldsPanel);
            loginPanel.add(buttonsPanel);

            // Add panels to frame
            frame.add(landingPagePanel, "Landing Page");
            frame.add(loginPanel, "Login");

            // Show Landing Page by default
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Landing Page");

            // Action to switch to Login Panel
            loginButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login"));

            frame.setVisible(true);
        });
    }
}
