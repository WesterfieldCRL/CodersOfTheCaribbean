import javax.swing.*;
import java.awt.*;
import Person.Person;

import java.awt.image.BufferedImage;
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10);
                                                            //modify this to your path_to_logo.png if different
        ImageIcon originalIcon = new ImageIcon("logo.png");
        // Scale the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        //! PLACHOLDERS
        JButton cruise1Button = createStyledButton("Cruise 1", BUTTON_FONT, BUTTON_COLOR);
        cruise1Button.addActionListener(e -> {/* Handle Cruise 1 selection */});
        gbc.gridx = 0;
        gbc.gridy = 1;
//        gbc.gridwidth = 1;
        panel.add(cruise1Button, gbc);

        JButton cruise2Button = createStyledButton("Cruise 2", BUTTON_FONT, BUTTON_COLOR);
        cruise2Button.addActionListener(e -> {/* Handle Cruise 2 selection */});
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(cruise2Button, gbc);

        JButton cruise3Button = createStyledButton("Cruise 3", BUTTON_FONT, BUTTON_COLOR);
        cruise3Button.addActionListener(e -> {/* Handle Cruise 3 selection */});
        gbc.gridx = 0;
        gbc.gridy = 3;
//        gbc.gridwidth = 2;
        panel.add(cruise3Button, gbc);

        JButton loginButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        loginButton.addActionListener(e -> switchToPanel(frame, "Login"));
        gbc.gridx = 0;
        gbc.gridy = 4;
//        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

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
                                    //modify this to your path_to_logo.png if different
        ImageIcon originalIcon = new ImageIcon("logo.png");//assuming you have "Coders of the Caribbean" logo stored locally
        // Scale the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        gbc.gridwidth = 1;
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
        ImageIcon emptyImage = new ImageIcon() ;
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Person> user = Person.login(username, password);

        if (user.isPresent()) {
            JOptionPane.showMessageDialog(null, "Login Successful. User type: "
                    + user.get().getClass().getSimpleName(),"Login Status",JOptionPane.DEFAULT_OPTION,emptyImage);
            //TODO
            // if(user.get().getClass() == Admin.class) {
            //     showAdminPanel();
            // }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error",
                    JOptionPane.ERROR_MESSAGE,emptyImage);
        }
    }

    private static void switchToPanel(JFrame frame, String panelName) {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), panelName);
    }

}