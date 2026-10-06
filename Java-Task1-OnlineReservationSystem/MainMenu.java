
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
 
/**
 * Menu shown after a successful login.
 */
public class MainMenu extends JFrame {
 
    public MainMenu(String username) {
        setTitle("Online Reservation System - Main Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
 
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(new EmptyBorder(20, 40, 20, 40));
 
        JLabel welcome = new JLabel("Welcome, " + username, JLabel.CENTER);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 18));
 
        JButton reserveButton = new JButton("Reservation Form (Book Ticket)");
        JButton cancelButton = new JButton("Cancellation Form (Cancel by PNR)");
        JButton logoutButton = new JButton("Logout");
 
        reserveButton.addActionListener(e -> new ReservationForm(username).setVisible(true));
        cancelButton.addActionListener(e -> new CancellationForm().setVisible(true));
        logoutButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
 
        panel.add(welcome);
        panel.add(reserveButton);
        panel.add(cancelButton);
        panel.add(logoutButton);
 
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }
}