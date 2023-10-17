package Pages;

import Person.Person;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class CruiseAppUtilities {

    static final Color BACKGROUND_COLOR = new Color(240, 248, 255);  // AliceBlue
    static final Color BUTTON_COLOR = new Color(100, 149, 237);      // CornflowerBlue
    static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public static JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBackground(BACKGROUND_COLOR);
        label.setOpaque(true);
        return label;
    }

    public static JButton createStyledButton(String text, Font font, Color color) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        return button;
    }

    public static void handleLogin(JTextField usernameField, JPasswordField passwordField, JFrame frame) {
        ImageIcon errorImage = new ImageIcon("ErrorImage.png");
        Image scaledImage = errorImage.getImage().getScaledInstance(150,90,Image.SCALE_SMOOTH);
        ImageIcon scaledErrorImage =  new ImageIcon(scaledImage);

        ImageIcon successIcon = new ImageIcon("SuccessIcon.png");
        Image scaledInstance = successIcon.getImage().getScaledInstance(150,90,Image.SCALE_SMOOTH);
        ImageIcon scaledSuccessIcon =  new ImageIcon(scaledInstance);


        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Person> user = Person.login(username, password);

        if (user.isPresent()) {
            if (user.get().getClass().getSimpleName().equals("Guest")) {
                switchToPanel(frame, "Guest View");
            }
            JOptionPane.showMessageDialog(null, "Login Successful. User type: "
                    + user.get().getClass().getSimpleName(),"Login Status",JOptionPane.DEFAULT_OPTION,scaledSuccessIcon);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error",
                    JOptionPane.ERROR_MESSAGE,scaledErrorImage);
        }
    }

    public static void switchToPanel(JFrame frame, String panelName) {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), panelName);
    }
}
