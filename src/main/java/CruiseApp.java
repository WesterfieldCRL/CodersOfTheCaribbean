import javax.swing.*;
import java.awt.*;

public class CruiseApp {

    public static void main(String[] args) {
        // Set the look and feel to the system's default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cruise Booking System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new CardLayout());

            // Styling
            Color backgroundColor = new Color(240, 248, 255);  // AliceBlue
            Color buttonColor = new Color(100, 149, 237);      // CornflowerBlue
            Font labelFont = new Font("Arial", Font.BOLD, 16);
            Font buttonFont = new Font("Arial", Font.BOLD, 14);

            // Landing Page Panel
            JPanel landingPagePanel = new JPanel(new GridLayout(4, 1, 10, 10));
            landingPagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            landingPagePanel.setBackground(backgroundColor);

            JButton loginButton = new JButton("Login");
            loginButton.setBackground(buttonColor);
            loginButton.setFont(buttonFont);

            landingPagePanel.add(getStyledLabel("Cruise 1", labelFont, backgroundColor));
            landingPagePanel.add(getStyledLabel("Cruise 2", labelFont, backgroundColor));
            landingPagePanel.add(getStyledLabel("Cruise 3", labelFont, backgroundColor));
            landingPagePanel.add(loginButton);

            // Login Panel
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            loginPanel.setBackground(backgroundColor);

            JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            fieldsPanel.setBackground(backgroundColor);

            fieldsPanel.add(getStyledLabel("Username:", labelFont, backgroundColor));
            fieldsPanel.add(new JTextField(15));
            fieldsPanel.add(getStyledLabel("Password:", labelFont, backgroundColor));
            fieldsPanel.add(new JPasswordField(15));

            JPanel buttonsPanel = new JPanel(new FlowLayout());
            buttonsPanel.setBackground(backgroundColor);
            JButton loginSubmitButton = new JButton("Login");
            JButton createAccountButton = new JButton("Create Account");

            loginSubmitButton.setBackground(buttonColor);
            createAccountButton.setBackground(buttonColor);
            loginSubmitButton.setFont(buttonFont);
            createAccountButton.setFont(buttonFont);

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

            // Center the frame on screen
            frame.setLocationRelativeTo(null);

            frame.setVisible(true);
        });
    }

    // Utility function to create styled labels
    private static JLabel getStyledLabel(String text, Font font, Color bgColor) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBackground(bgColor);
        label.setOpaque(true);  // to ensure the background color is visible
        return label;
    }
}
