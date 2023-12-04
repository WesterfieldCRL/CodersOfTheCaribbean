package Pages;

import Util.AppLogger;

import javax.swing.*;
import java.awt.*;
import static Pages.CruiseAppUtilities.*;
/**
 * The {@code TermsOfServicePanel} class in the {@code Pages} package is responsible for creating and managing a Terms of Service.
 *
 * <p>This class allows for the display of the ToS. It includes methods to create a dialog box that displays the ToS content, formatted as HTML.
 * The class is designed to ensure that users can easily access,read, and understand the terms and conditions.</p>
 *
 * <p>Key features of the {@code TermsOfServicePanel} class include:</p>
 * <ul>
 *   <li>Construction of a modal dialog box that contains the Terms of Service text.</li>
 *   <li>Use of a {@code JTextPane} to display the ToS content, formatted in HTML.</li>
 * </ul>
 *
 * <p>The class ensures that the dialog box is modal and anchored to a parent {@code JFrame}, focusing user attention on the ToS and preventing interaction with the
 * main application window until the dialog is closed.</p>
 */
public class TermsOfServicePanel {
    /**
     * Initializes and returns a modal dialog that presents the Terms of Service for the Cruise Booking System.
     *
     * <p>This method creates a {@code JDialog} containing the text of the Terms of Service, with the content formatted as HTML.
     * The dialog is composed of the following elements:</p>
     * <ul>
     *   <li>A {@code JTextPane} that displays the Terms of Service text. The content includes various sections such as user conduct guidelines,
     *       limitations of liability, and acceptance of terms, all formatted in HTML.</li>
     *   <li>A {@code JScrollPane} that allows users to scroll through the terms efficiently.</li>
     * </ul>
     *
     * <p>The dialog is set to be modal and is anchored to the provided {@code JFrame}, ensuring it remains in focus until closed by the user.
     * The {@code JTextPane} is configured to be non-editable.</p>
     *
     * @param frame the parent {@code JFrame} to which the dialog is attached
     * @return a {@code JDialog} containing the Terms of Service
     */
    public static JDialog createTermsOfServiceDialog(JFrame frame) {
        AppLogger.getLogger().info("TOS panel");
        JDialog dialog = new JDialog(frame, "Terms of Service", true);
        dialog.setLayout(new BorderLayout());
        dialog.setBackground(BACKGROUND_COLOR);

        JTextPane termsTextArea = new JTextPane();
        termsTextArea.setContentType("text/html");
        termsTextArea.setText("<html>" +
                "<h2>1. Acceptance of Terms</h2>" +
                "<p>By accessing and using the Cruise Booking System by Coders of the Caribbean (\"the Application\"), you accept and agree to be bound by these terms and conditions. If you do not agree to these terms, you cannot use the Application.</p>" +

                "<h2>2. Development Stage</h2>" +
                "<p>Please be aware that the Application is in a development phase. This means that it might not operate with the stability of a final-release product. While we have made every effort to ensure the Application works as intended, there might still be unfinished features, unexpected behaviors, or bugs.</p>" +

                "<h2>3. User Conduct</h2>" +
                "<p>As a user, you are expected to use the Application in the intended manner. Avoid any unexpected actions that might cause the Application to malfunction or produce errors. You should not:</p>" +
                "<ul>" +
                "<li>Attempt to break or circumvent any security features.</li>" +
                "<li>Use automated systems or software to extract data from the Application.</li>" +
                "<li>Engage in any activity that disrupts or interferes with the Application.</li>" +
                "</ul>" +

                "<h2>4. Limitation of Liability</h2>" +
                "<p>If you encounter a bug or error while using the Application, you understand and agree that it is the result of your actions and decisions. We are not responsible for any potential bugs or errors that may arise from your use of the Application.</p>" +
                "<p>Furthermore, any such bugs or errors you encounter shall not impact our grade for the development of this Application as of the current state. We expressly disclaim any liability or responsibility for any unforeseen issues or damages arising from the use of the Application.</p>" +

                "<h2>5. Changes to the Terms</h2>" +
                "<p>We reserve the right to change, modify, or revise these terms at any time. Any changes will be effective immediately upon posting, and by continuing to use the Application, you agree to any amended terms.</p>" +
                "</html>");

        termsTextArea.setCaretPosition(0);
        termsTextArea.setEditable(false);

        termsTextArea.getCaret().setBlinkRate(0);
        termsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                termsTextArea.getBorder()));

        JScrollPane scrollPane = new JScrollPane(termsTextArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.pack();
        return dialog;
    }
}
