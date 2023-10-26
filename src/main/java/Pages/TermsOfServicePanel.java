package Pages;

import javax.swing.*;
import java.awt.*;
import static Pages.CruiseAppUtilities.*;
public class TermsOfServicePanel {
    public static JPanel createTermsOfServicePanel(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

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
        panel.add(scrollPane, BorderLayout.CENTER);

        JCheckBox acceptCheckBox = new JCheckBox("I accept the terms and conditions.");
        acceptCheckBox.setFont(DEFAULT_FONT);
        acceptCheckBox.setBackground(BACKGROUND_COLOR);

        JButton acceptButton = createStyledButton("Proceed", BUTTON_FONT, BUTTON_COLOR);
        acceptButton.setEnabled(false);

        // Listener for checkbox to enable/disable button
        acceptCheckBox.addActionListener(e -> acceptButton.setEnabled(acceptCheckBox.isSelected()));
        acceptButton.addActionListener(e -> switchToPanel(frame, "Landing Page"));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(acceptCheckBox);
        bottomPanel.add(acceptButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }
}
