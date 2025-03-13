import javax.swing.*;
import java.awt.*;

public class UITheme {
    //This method will work as the Palate for all the pop-up screens to match the movie theme
    public static void applyPopupTheme() {
        UIManager.put("OptionPane.background", new Color(255, 245, 225)); // Creme Background
        UIManager.put("Panel.background", new Color(255, 245, 225)); // Creme Background
        UIManager.put("OptionPane.messageForeground", new Color(75, 46, 46)); // Dark Brown Text
        UIManager.put("Button.background", new Color(255, 215, 0)); // Gold Buttons
        UIManager.put("Button.foreground", new Color(75, 46, 46)); // Dark Brown Text
        UIManager.put("Button.border", BorderFactory.createRaisedBevelBorder());
    }

    // Use this method for errors (and then reset after)
    public static void applyErrorTheme(Component parent, String message, String title) {
        applyPopupTheme(); // Apply normal theme
        UIManager.put("OptionPane.messageForeground", new Color(139, 0, 0)); // Dark Red for Errors
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);

        //Reset theme after error message
        applyPopupTheme();
    }
}


