import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
 
/**
 * Cancellation form: fetch a booking by PNR, then cancel it after confirmation.
 */
public class CancellationForm extends JFrame {
 
    private final JTextField pnrField = new JTextField(15);
    private final JTextArea detailsArea = new JTextArea(8, 35);
    private final JButton cancelButton = new JButton("Confirm Cancellation");
 
    private Reservation fetched;
 
    public CancellationForm() {
        setTitle("Cancellation Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton fetchButton = new JButton("Fetch");
        top.add(new JLabel("PNR Number:"));
        top.add(pnrField);
        top.add(fetchButton);
 
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        detailsArea.setText("Enter a PNR number and click Fetch to see booking details.");
 
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        cancelButton.setEnabled(false);
        bottom.add(cancelButton);
 
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 15, 10, 15));
        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
 
        fetchButton.addActionListener(e -> fetch());
        pnrField.addActionListener(e -> fetch());
        cancelButton.addActionListener(e -> cancel());
 
        add(root);
        pack();
        setLocationRelativeTo(null);
    }
 
    private void fetch() {
        fetched = null;
        cancelButton.setEnabled(false);
 
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PNR number.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pnr.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PNR must contain digits only.",
                    "Invalid PNR", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        try {
            Reservation reservation = DatabaseHelper.findReservation(pnr);
            if (reservation == null) {
                detailsArea.setText("No booking found for PNR " + pnr + ".");
                return;
            }
            fetched = reservation;
            detailsArea.setText(reservation.toDisplayText());
            cancelButton.setEnabled(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void cancel() {
        if (fetched == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel booking " + fetched.getPnr() + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
 
        try {
            if (DatabaseHelper.cancelReservation(fetched.getPnr())) {
                JOptionPane.showMessageDialog(this,
                        "Booking " + fetched.getPnr() + " has been cancelled.",
                        "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                detailsArea.setText("Booking cancelled.");
                pnrField.setText("");
                fetched = null;
                cancelButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Booking could not be found. It may already be cancelled.",
                        "Not Found", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}