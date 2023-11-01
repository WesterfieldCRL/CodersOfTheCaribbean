package Pages;

import javax.swing.*;

import java.awt.*;

import static Pages.CruiseAppUtilities.*;

public class AdminAccountPage {
    public static JPanel createAdminViewPanel(JFrame frame){
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);


        return panel;
    }
}
