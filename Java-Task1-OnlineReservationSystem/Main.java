
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
 
/**
 * Oasis Infobyte SIP - Java Development
 * Task 1: Online Reservation System
 *
 * Demo logins:
 *   admin / admin123
 *   user1 / pass123
 */
public class Main {
 
    public static void main(String[] args) {
        try {
            DatabaseHelper.initialize();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not start the database:\n" + e.getMessage(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
 