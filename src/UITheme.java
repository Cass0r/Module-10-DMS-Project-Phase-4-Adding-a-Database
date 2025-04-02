import javax.swing.*;
import java.awt.*;

/**
 * The UITheme class is responsible for defining and applying consistent themes for pop-up messages in the movie application.
 * <p>
 * This class contains utility methods to customize the appearance of JOptionPane dialogs, including their background colors,
 * text colors, and button styles. It provides a default theme for pop-ups and a specialized theme for error messages.
 * </p>
 * <p>
 * The themes are specifically designed to match the overall movie theme, with a light cream background, dark brown text,
 * and gold buttons. Error messages are distinguished by a dark red color for the message text.
 * </p>
 *
 * <p>
 * The class contains two methods:
 * <ul>
 *     <li>{@link #applyPopupTheme()} - Applies the default movie-themed styling to pop-up dialogs.</li>
 *     <li>{@link #applyErrorTheme(Component, String, String)} - Applies the error theme and displays an error message in a pop-up.</li>
 * </ul>
 *<p>
 *
 * Note: This class does not require a constructor.
 */
public class UITheme {
    //This method will work as the Palate for all the pop-up screens to match the movie theme
    /**
     * Applies the default movie-themed styling to pop-up dialogs.
     * <p>
     * The default theme includes:
     * <ul>
     *     <li>A cream-colored background for the pop-up and panel (Color(255, 245, 225)).</li>
     *     <li>Dark brown text for the message and button labels (Color(75, 46, 46)).</li>
     *     <li>Gold-colored buttons with a raised bevel border (Color(255, 215, 0)).</li>
     * </ul>
     *
     * This method is used as a foundation for all pop-up windows and ensures a consistent look and feel across the application.
     */
    public static void applyPopupTheme() {
        UIManager.put("OptionPane.background", new Color(255, 245, 225)); // Creme Background
        UIManager.put("Panel.background", new Color(255, 245, 225)); // Creme Background
        UIManager.put("OptionPane.messageForeground", new Color(75, 46, 46)); // Dark Brown Text
        UIManager.put("Button.background", new Color(255, 215, 0)); // Gold Buttons
        UIManager.put("Button.foreground", new Color(75, 46, 46)); // Dark Brown Text
        UIManager.put("Button.border", BorderFactory.createRaisedBevelBorder());
    }

    // Use this method for errors (and then reset after)
    /**
     * Applies the error theme to pop-up dialogs and displays an error message.
     * <p>
     * The error theme includes:
     * <ul>
     *     <li>A dark red color for the message text to visually distinguish the error (Color(139, 0, 0)).</li>
     *     <li>Uses the default pop-up theme for the background and button styling.</li>
     * </ul>
     *
     * After the error message is displayed, the method resets the theme back to the default pop-up style.
     *
     * @param parent the parent component to display the dialog relative to.
     * @param message the error message to display in the pop-up dialog.
     * @param title title The title of the error message dialog (typically something like "Error").
     */
    public static void applyErrorTheme(Component parent, String message, String title) {
        applyPopupTheme(); // Apply normal theme
        UIManager.put("OptionPane.messageForeground", new Color(139, 0, 0)); // Dark Red for Errors
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);

        //Reset theme after error message
        applyPopupTheme();
    }
}


