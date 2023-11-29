package Pages;

import Person.Person;
import Person.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class CruiseAppUtilities {

    static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    static final Color BUTTON_COLOR = new Color(70, 130, 180);
    static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 14);

    public static Guest currentGuest;
    public static Admin currentAdmin;
    public static TravelAgent currentAgent;
    public static Manager currentManager;

    private static ImageIcon errorImage = new ImageIcon("ErrorImage.png");
    private static Image scaledErrorInstance = errorImage.getImage().getScaledInstance(150,90,Image.SCALE_SMOOTH);
    public static ImageIcon scaledErrorImage =  new ImageIcon(scaledErrorInstance);

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
                currentGuest = (Guest) user.get();
                addGuestPanelToFrame(frame);
                switchToPanel(frame, "Guest View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("TRAVELAGENT")){
                currentAgent = (TravelAgent) user.get();
                addAgentPanelToFrame(frame);
                switchToPanel(frame, "Agent View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("ADMIN")){
                currentAdmin = (Admin) user.get();
                addAdminPanelToFrame(frame);
                switchToPanel(frame, "Admin View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("MANAGER")){
                currentManager = (Manager) user.get();
                addManagerPanelToFrame(frame);
                switchToPanel(frame, "Manager View");
            }
            usernameField.setText("");
            passwordField.setText("");
            JOptionPane.showMessageDialog(null, "Login Successful. User type: "
                    + user.get().getClass().getSimpleName(),"Login Status",JOptionPane.DEFAULT_OPTION,scaledSuccessIcon);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error",
                    JOptionPane.ERROR_MESSAGE,scaledErrorImage);
        }
    }

    public static void addAdminPanelToFrame(JFrame frame) {
        JTabbedPane adminPanel = AdminAccountPage.createAdminTabbedPane(frame);
        frame.add(adminPanel, "Admin View");
        frame.revalidate();
        frame.repaint();
    }

    public static void addGuestPanelToFrame(JFrame frame){
        JTabbedPane guestPanel = GuestAccountPage.createGuestViewTabbedPane(frame);
        frame.add(guestPanel, "Guest View");
        frame.revalidate();
        frame.repaint();
    }

    public static void addAgentPanelToFrame(JFrame frame){
        JTabbedPane agentPanel = TravelAgentAccountPage.createTravelAgentViewTabbedPane(frame);
        frame.add(agentPanel, "Agent View");
        frame.revalidate();
        frame.repaint();
    }

    public static void addManagerPanelToFrame(JFrame frame){
        JPanel managerPanel = ManagerAccountPage.createManagerViewPanel(frame);
        frame.add(managerPanel, "Manager View");
        frame.revalidate();
        frame.repaint();
    }


    public static void switchToPanel(JFrame frame, String panelName) {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), panelName);
    }

    public static String validatePassword(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            return "Password must contain at least one letter.";
        }
        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
            return "Password must contain at least one special character (!@#$%^&*+=?-).";
        }
        return "";
    }

    //overloaded so it can be setup for blank password
    static void updateRequirementsLabel(java.util.List<String> requirements, JLabel requirementsLabel) {
        StringBuilder requirementsHtml = new StringBuilder("<html>");
        for (String requirement : requirements) {
            requirementsHtml.append(requirement).append("<br>");
        }
        requirementsHtml.append("</html>");
        requirementsLabel.setText(requirementsHtml.toString());
    }

    static void updateRequirementsLabel(String password, List<String> requirements, JLabel requirementsLabel) {
        requirements.clear();
        if (password.length() < 8) requirements.add("At least 8 characters");
        if (!password.matches(".*\\d.*")) requirements.add("At least one digit");
        if (!password.matches(".*[a-zA-Z].*")) requirements.add("At least one letter");
        if (!password.matches(".*[!@#$%^&*+=?-].*")) requirements.add("At least one special character (!@#$%^&*+=?-)");

        StringBuilder requirementsHtml = new StringBuilder("<html>");
        for (String requirement : requirements) {
            requirementsHtml.append(requirement).append("<br>");
        }
        requirementsHtml.append("</html>");
        requirementsLabel.setText(requirementsHtml.toString());
    }

    public static List<String> requirements = new ArrayList<>(Arrays.asList(
            "At least 8 characters",
            "At least one digit",
            "At least one letter",
            "At least one special character (!@#$%^&*+=?-)"
    ));

}