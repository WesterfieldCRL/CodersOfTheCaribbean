package Pages;

import Person.Person;
import Person.*;
import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



/**
 * Provides utility methods and constants.
 *
 * <p>This class serves as a central repository for common utility methods and constants used throughout the application.
 * It includes methods for creating styled UI components, handling user authentication, updating UI elements based on password
 * validation, and managing navigation between different panels in the application's interface.</p>
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>Methods for creating styled labels and buttons.</li>
 *   <li>A centralized authentication mechanism that handles user login and transitions to appropriate user-specific panels.</li>
 *   <li>Dynamic password requirement display for real-time feedback during account creation or password changes.</li>
 *   <li>Predefined criteria for password validation used throughout the application for consistency.</li>
 * </ul>
 *
 * <p>The class also maintains references to current user objects (such as {@code currentGuest}, {@code currentAgent}, etc.)
 * and global UI-related constants like colors and fonts.</p>
 */
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


    /**
     * Constructs a {@code JLabel} with the specified text and font, and sets a predefined background color.
     *
     * <p>This method creates a {@code JLabel}, sets its text to the provided value, applies the specified {@code Font},
     * and configures its background color to the {@code BACKGROUND_COLOR}. Additionally, the
     * label is set to opaque to ensure the background color is visible.</p>
     *
     * @param text the text to be displayed on the label
     * @param font the {@code Font} to be applied to the label's text
     * @return a {@code JLabel} instance styled with the given text, font, and background color
     */
    public static JLabel createStyledLabel(String text, Font font) {
        AppLogger.getLogger().info("Creating styled label");
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBackground(BACKGROUND_COLOR);
        label.setOpaque(true);
        return label;
    }

    /**
     * Constructs a {@code JButton} with specified text, font, and background color.
     *
     * <p>This method creates a {@code JButton}, sets its text to the provided value, applies the specified {@code Font},
     * and sets the background to the given {@code Color}.</p>
     *
     * @param text  the text to be displayed on the button
     * @param font  the {@code Font} to be used for the button's text
     * @param color the {@code Color} for the button's background
     * @return a {@code JButton} styled with the specified text, font, and background color
     */
    public static JButton createStyledButton(String text, Font font, Color color) {
        AppLogger.getLogger().info("Creating styled button");
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(color);
        return button;
    }

    /**
     * Authenticates a user and navigates to their respective panel based on their role.
     *
     * <p>This method authenticates a user using the {@code Person.login} method by retrieving text from
     * the specified username and password fields. Upon successful authentication, it navigates to the appropriate
     * user panel (Guest, Travel Agent, Admin, Manager) and displays a success message. If the credentials are invalid,
     * an error message is displayed.</p>
     *
     * <p>The user's role is determined during the login process, which sets the corresponding current user object
     * (e.g., {@code currentGuest}, {@code currentAgent}, {@code currentAdmin}, {@code currentManager}). The method then
     * adds the relevant panel to the frame and switches the view accordingly.</p>
     *
     * <p>After login processing, the username and password fields are cleared.</p>
     *
     * @param usernameField the {@code JTextField} containing the username
     * @param passwordField the {@code JPasswordField} containing the password
     * @param frame the {@code JFrame} to which user-specific panels are added and where the view is switched
     */
    public static void handleLogin(JTextField usernameField, JPasswordField passwordField, JFrame frame) {
        AppLogger.getLogger().info("Handling login info");

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Optional<Person> user = Person.login(username, password);

        if (user.isPresent()) {
            if (user.get().getClass().getSimpleName().equalsIgnoreCase("Guest")) {
                AppLogger.getLogger().info("Guest login handling");
                currentGuest = (Guest) user.get();
                addGuestPanelToFrame(frame);
                switchToPanel(frame, "Guest View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("TRAVELAGENT")){
                AppLogger.getLogger().info("Agent login handling");
                currentAgent = (TravelAgent) user.get();
                addAgentPanelToFrame(frame);
                switchToPanel(frame, "Agent View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("ADMIN")){
                AppLogger.getLogger().info("Admin login handling");
                currentAdmin = (Admin) user.get();
                addAdminPanelToFrame(frame);
                switchToPanel(frame, "Admin View");
            } else if (user.get().getClass().getSimpleName().equalsIgnoreCase("MANAGER")){
                AppLogger.getLogger().info("Manager login handling");
                currentManager = (Manager) user.get();
                addManagerPanelToFrame(frame);
                switchToPanel(frame, "Manager View");
            }
            usernameField.setText("");
            passwordField.setText("");
            JOptionPane.showMessageDialog(null, "Login Successful. User type: "
                    + user.get().getClass().getSimpleName(),"Login Status",JOptionPane.DEFAULT_OPTION,scaledSuccessIcon);
        } else {
            AppLogger.getLogger().info("Handling invalid login details");
            JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error",
                    JOptionPane.ERROR_MESSAGE,scaledErrorImage);
        }
    }

    /**
     * Adds the Admin panel to the specified {@code JFrame}.
     *
     * <p>This method creates the Admin panel by invoking {@code AdminAccountPage.createAdminTabbedPane}. The resulting
     * {@code JTabbedPane}, designated for administrative tasks, is then added to the provided {@code JFrame} with the identifier
     * "Admin View". After adding the panel, the frame is refreshed to display the new view.</p>
     *
     * @param frame the {@code JFrame} to which the Admin panel is added
     */
    public static void addAdminPanelToFrame(JFrame frame) {
        AppLogger.getLogger().info("Adding admin panel to frame");
        JTabbedPane adminPanel = AdminAccountPage.createAdminTabbedPane(frame);
        frame.add(adminPanel, "Admin View");
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Adds the Guest panel to the specified {@code JFrame}.
     *
     * <p>This method creates the Guest panel by invoking {@code GuestAccountPage.createGuestViewTabbedPane}. The resulting
     * {@code JTabbedPane}, designated for guest tasks, is then added to the provided {@code JFrame} with the identifier
     * "Guest View". After adding the panel, the frame is refreshed to display the new view.</p>
     *
     * @param frame the {@code JFrame} to which the Guest panel is added
     */
    public static void addGuestPanelToFrame(JFrame frame){
        AppLogger.getLogger().info("Adding guest panel to frame");
        JTabbedPane guestPanel = GuestAccountPage.createGuestViewTabbedPane(frame);
        frame.add(guestPanel, "Guest View");
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Integrates the Travel Agent panel into the specified {@code JFrame}.
     *
     * <p>This method creates the Travel Agent panel by invoking {@code TravelAgentAccountPage.createTravelAgentViewTabbedPane}. The resulting
     * {@code JTabbedPane}, designated for agent tasks, is then added to the provided {@code JFrame} with the identifier
     * "Agent View". After adding the panel, the frame is refreshed to display the new view.</p>
     *
     * @param frame the {@code JFrame} to which the Travel Agent panel is added
     */
    public static void addAgentPanelToFrame(JFrame frame){
        AppLogger.getLogger().info("adding agent panel to frame");
        JTabbedPane agentPanel = TravelAgentAccountPage.createTravelAgentViewTabbedPane(frame);
        frame.add(agentPanel, "Agent View");
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Adds the Manager panel to the specified {@code JFrame}.
     *
     * <p>This method creates the Manager panel by invoking {@code ManagerAccountPage.createManagerViewPanel}. The resulting
     * {@code JPanel}, designated for manager tasks, is then added to the provided {@code JFrame} with the identifier
     * "Manager View". After adding the panel, the frame is refreshed to display the new view.</p>
     *
     * @param frame the {@code JFrame} to which the Manager panel is added
     */
    public static void addManagerPanelToFrame(JFrame frame){
        AppLogger.getLogger().info("adding manager panel to frame");
        JPanel managerPanel = ManagerAccountPage.createManagerViewPanel(frame);
        frame.add(managerPanel, "Manager View");
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Switches to a specified panel in the given {@code JFrame}, identified by its name.
     *
     * <p>This method performs a view transition within a {@code JFrame} that uses a {@code CardLayout}. It casts
     * the layout manager of the frame's content pane to {@code CardLayout} and then employs the {@code show}
     * method to display the panel identified by the provided {@code panelName}.</p>
     *
     * @param frame     the {@code JFrame} that contains the panels to be switched
     * @param panelName the name identifier of the panel to be displayed, as defined when added to the frame
     */
    public static void switchToPanel(JFrame frame, String panelName) {
        AppLogger.getLogger().info("switching to panel");
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), panelName);
    }

    /**
     * Validates a password against predefined criteria.
     *
     * <p>This method checks the provided password to ensure it meets the following criteria:</p>
     * <ol>
     *   <li>Length: The password must be at least 8 characters long.</li>
     *   <li>Digit Inclusion: The password must include at least one digit.</li>
     *   <li>Letter Inclusion: The password must contain at least one letter.</li>
     *   <li>Special Character Inclusion: The password must have at least one special character
     *       from the set.</li>
     * </ol>
     *
     * <p>If the password does not meet any of these criteria, an appropriate error message is returned.
     * If it meets all criteria, an empty string is returned, indicating the password is valid.</p>
     *
     * @param password the password string to validate
     * @return an error message if the password fails validation, or an empty string if it passes
     */
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

    /**
     * Updates a {@code JLabel} with password requirements, formatted as an HTML list.
     *
     * <p>This method takes a list of password requirements and a {@code JLabel}, and constructs an HTML-formatted string
     * to display these requirements. It iterates through each requirement in the list,
     * appending them to a {@code StringBuilder} with HTML tags to create a visually structured list. The resulting HTML
     * string is then set as the text of the provided {@code JLabel}.</p>
     *
     * @param requirements a list of strings, each representing a specific password requirement
     * @param requirementsLabel the {@code JLabel} to be updated with the HTML-formatted requirements
     */
    static void updateRequirementsLabel(java.util.List<String> requirements, JLabel requirementsLabel) {
        AppLogger.getLogger().info("Updating requirements label");
        StringBuilder requirementsHtml = new StringBuilder("<html>");
        for (String requirement : requirements) {
            requirementsHtml.append(requirement).append("<br>");
        }
        requirementsHtml.append("</html>");
        requirementsLabel.setText(requirementsHtml.toString());
    }

    /**
     * Dynamically updates a {@code JLabel} with password requirements based on the evaluation of the provided password.
     *
     * <p>This method assesses the given password against specific criteria: minimum length, inclusion of at least one digit,
     * one letter, and one special character. For each criterion that the password fails to meet, a corresponding requirement
     * is added to the provided list. The method then uses this updated list to construct an HTML-formatted string, which
     * is set as the text of the specified {@code JLabel}.</p>
     *
     * @param password the password string to be analyzed
     * @param requirements a list of strings that is dynamically updated to reflect the password's adherence to criteria
     * @param requirementsLabel the {@code JLabel} to be updated with the dynamically generated list of password requirements
     */

    static void updateRequirementsLabel(String password, List<String> requirements, JLabel requirementsLabel) {
        AppLogger.getLogger().info("Overloaded update requirements label");
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

    /**
     * Predefined password requirements for validation in the application.
     *
     * <p>This static List outlines the initial criteria for password validation.
     * The password requirements are as follows:</p>
     * <ol>
     *   <li>"At least 8 characters" - Ensuring the password is a minimum of 8 characters long.</li>
     *   <li>"At least one digit" - Requiring the inclusion of at least one numerical digit.</li>
     *   <li>"At least one letter" - Mandating the presence of at least one alphabetic letter.</li>
     *   <li>"At least one special character" - Necessitating at least one specified special character.</li>
     * </ol>
     */
    public static List<String> requirements = new ArrayList<>(Arrays.asList(
            "At least 8 characters",
            "At least one digit",
            "At least one letter",
            "At least one special character (!@#$%^&*+=?-)"
    ));

}