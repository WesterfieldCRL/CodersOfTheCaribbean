import javax.swing.*;
import java.awt.*;
import Person.Person;
import java.util.Optional;

public class CruiseApp {
    private static void handleLogin(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Person> user = Person.login(username, password);

        if (user.isPresent()) {
            JOptionPane.showMessageDialog(null, "Login Successful. User type: " + user.get().getClass().getSimpleName());
            //TODO
            // if(user.get().getClass() == Admin.class) {
            //     showAdminPanel();
            // }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
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
            Color backgroundColor = new Color(240, 248, 255);
            Color buttonColor = new Color(100, 149, 237);
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

            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            fieldsPanel.add(getStyledLabel("Username:", labelFont, backgroundColor));
            fieldsPanel.add(usernameField);
            fieldsPanel.add(getStyledLabel("Password:", labelFont, backgroundColor));
            fieldsPanel.add(passwordField);



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

            loginSubmitButton.addActionListener(e -> handleLogin(usernameField, passwordField));

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

