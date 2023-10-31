package Pages;

import Person.Person;
import Person.*;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;


public class CruiseAppUtilities {

    static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    static final Color BUTTON_COLOR = new Color(70, 130, 180);
    static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);
    //Track guest
    public static Guest currentGuest;

    //GLOBAL ERROR IMAGE
    private static ImageIcon errorImage = new ImageIcon("ErrorImage.png");
    private static Image scaledErrorInstance = errorImage.getImage().getScaledInstance(150,90,Image.SCALE_SMOOTH);
    public static ImageIcon scaledErrorImage =  new ImageIcon(scaledErrorInstance);

    //GLOBAL SUCCESS IMAGE
    private static ImageIcon successIcon = new ImageIcon("SuccessIcon.png");
    private static Image scaledSuccessInstance = successIcon.getImage().getScaledInstance(150,90,Image.SCALE_SMOOTH);
    public static ImageIcon scaledSuccessIcon =  new ImageIcon(scaledSuccessInstance);


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

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Person> user = Person.login(username, password);

        if (user.isPresent()) {
            if (user.get().getClass().getSimpleName().equalsIgnoreCase("Guest")) {
                switchToPanel(frame, "Guest View");
                currentGuest = (Guest) user.get();
            }
            else if (user.get().getClass().getSimpleName().equalsIgnoreCase("TRAVELAGENT")){
                switchToPanel(frame, "Travel Agent");
            }
            else if (user.get().getClass().getSimpleName().equalsIgnoreCase("ADMIN")){
                switchToPanel(frame, "Admin");
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
