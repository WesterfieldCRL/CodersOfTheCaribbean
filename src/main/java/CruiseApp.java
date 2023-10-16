import javax.swing.*;
import java.awt.*;
import Person.Person;
import java.util.Optional;

public class CruiseApp {

    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);  // AliceBlue
    private static final Color BUTTON_COLOR = new Color(100, 149, 237);      // CornflowerBlue
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static void main(String[] args) {
        setSystemLookAndFeel();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = setupMainFrame();
            JPanel landingPagePanel = createLandingPagePanel(frame);
            JPanel loginPanel = createLoginPanel();

            frame.add(landingPagePanel, "Landing Page");
            frame.add(loginPanel, "Login");

            frame.setVisible(true);
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JFrame setupMainFrame() {
        JFrame frame = new JFrame("Cruise Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        return frame;
    }

    private static JPanel createLandingPagePanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JButton loginButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        loginButton.addActionListener(e -> switchToPanel(frame, "Login"));

        panel.add(createStyledLabel("Cruise 1", LABEL_FONT));
        panel.add(createStyledLabel("Cruise 2", LABEL_FONT));
        panel.add(createStyledLabel("Cruise 3", LABEL_FONT));
        panel.add(loginButton);

        return panel;
    }

    private static JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Changed to GridBagLayout for better positioning
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints for positioning
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10); // Padding

        JLabel logoLabel = new JLabel(new ImageIcon("path_to_logo.png")); // Assuming you have the "Coders of Caribbean" logo saved locally
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        gbc.gridwidth = 1; // Reset to default

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createStyledLabel("Username:", LABEL_FONT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createStyledLabel("Password:", LABEL_FONT), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        JButton loginSubmitButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        JButton createAccountButton = createStyledButton("Create Account", BUTTON_FONT, BUTTON_COLOR);

        loginSubmitButton.addActionListener(e -> handleLogin(usernameField, passwordField));

        buttonsPanel.add(loginSubmitButton);
        buttonsPanel.add(createAccountButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span two columns
        panel.add(buttonsPanel, gbc);

        return panel;
    }
    private static JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBackground(BACKGROUND_COLOR);
        label.setOpaque(true);
        return label;
    }

    private static JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        return button;
    }

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
            JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void switchToPanel(JFrame frame, String panelName) {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), panelName);
    }

}