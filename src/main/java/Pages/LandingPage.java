package Pages;

import javax.swing.*;
import java.awt.*;

import static Pages.CruiseAppUtilities.*;

/**
 * The {@code LandingPage} class in the {@code Pages} package is responsible for creating and managing the landing page.
 *
 * <p>This class primarily consists of static methods that contribute to building the graphical user interface of the application's landing page.
 *
 * <p>Key features of the {@code LandingPage} class:</p>
 * <ul>
 *   <li>Creation of a landing page panel with a logo and a 'Login' button.</li>
 *   <li>Use of {@code GridBagLayout} and {@code GridBagConstraints} for arranging UI components.</li>
 *   <li>Transition functionality from the landing page to the login page, initiated through user interaction with the 'Login' button.</li>
 * </ul>
 *
 * <p>The class integrates with other components of the application, such as utility methods from the {@code CruiseAppUtilities} class.</p>
 */
public class LandingPage {
    /**
     * Constructs and returns the landing page panel for the application.
     *
     * <p>This method creates a {@code JPanel} to serve as the landing page of the application. The panel prominently features
     * a logo, loaded from the 'logo.png' file, and a 'Login' button. The logo image is scaled appropriately and displayed in a
     * {@code JLabel}. The 'Login' button, upon being clicked, triggers a transition to the login panel, enabling user authentication.</p>
     *
     * <p>The layout of the panel is managed using {@code GridBagLayout}.
     * {@code GridBagConstraints} are employed to define the layout constraints for each component.</p>
     *
     * @param frame the {@code JFrame} which houses the panel, utilized for transitioning to the login panel upon user interaction
     * @return a fully constructed {@code JPanel} that serves as the landing page of the application
     */
    public static JPanel createLandingPagePanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10,10,10,10);

        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(scaledIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(logoLabel, gbc);

        JButton loginButton = createStyledButton("Login", BUTTON_FONT, BUTTON_COLOR);
        loginButton.addActionListener(e -> switchToPanel(frame, "Login"));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        return panel;
    }
}
