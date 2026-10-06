import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
 
/**
 * Reservation form: books a ticket, saves it to the database and shows the PNR.
 */
public class ReservationForm extends JFrame {
 
    private static final String[] CLASS_TYPES = {
        "Sleeper", "AC 3 Tier", "AC 2 Tier", "First AC", "Chair Car"
    };
 
    private final String bookedBy;
 
    private final JTextField passengerField = new JTextField(20);
    private final JTextField trainNumberField = new JTextField(20);
    private final JTextField trainNameField = new JTextField(20);
    private final JComboBox<String> classCombo = new JComboBox<>(CLASS_TYPES);
    private final JTextField dateField = new JTextField(20);
    private final JTextField sourceField = new JTextField(20);
    private final JTextField destinationField = new JTextField(20);
 
    public ReservationForm(String bookedBy) {
        this.bookedBy = bookedBy;
        setTitle("Reservation Form");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 
        trainNameField.setEditable(false);
        dateField.setToolTipText("Format: yyyy-MM-dd");
 
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(15, 25, 15, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
 
        JLabel heading = new JLabel("Book a Ticket");
        heading.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(heading, gbc);
        gbc.gridwidth = 1;
 
        addRow(panel, gbc, 1, "Passenger Name:", passengerField);
        addRow(panel, gbc, 2, "Train Number:", trainNumberField);
        addRow(panel, gbc, 3, "Train Name (auto):", trainNameField);
        addRow(panel, gbc, 4, "Class Type:", classCombo);
        addRow(panel, gbc, 5, "Date of Journey (yyyy-MM-dd):", dateField);
        addRow(panel, gbc, 6, "From (Source):", sourceField);
        addRow(panel, gbc, 7, "To (Destination):", destinationField);
 
        JButton bookButton = new JButton("Insert / Book");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(bookButton, gbc);
 
        // Auto-fill the train name whenever the train number changes.
        trainNumberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTrainName();
            }
 
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTrainName();
            }
 
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTrainName();
            }
        });
 
        bookButton.addActionListener(e -> book());
 
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }
 
    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label,
                        java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
 
    private void updateTrainName() {
        String text = trainNumberField.getText().trim();
        if (text.isEmpty()) {
            trainNameField.setText("");
            return;
        }
        try {
            String name = DatabaseHelper.getTrainName(Integer.parseInt(text));
            trainNameField.setText(name != null ? name : "Train not found");
        } catch (NumberFormatException ex) {
            trainNameField.setText("Enter a numeric train number");
        } catch (SQLException ex) {
            trainNameField.setText("Database error");
        }
    }
 
    private void book() {
        String passenger = passengerField.getText().trim();
        String trainText = trainNumberField.getText().trim();
        String dateText = dateField.getText().trim();
        String source = sourceField.getText().trim();
        String destination = destinationField.getText().trim();
        String classType = (String) classCombo.getSelectedItem();
 
        // 1. Required fields
        if (passenger.isEmpty() || trainText.isEmpty() || dateText.isEmpty()
                || source.isEmpty() || destination.isEmpty()) {
            showError("All fields are required. Please fill in every field.");
            return;
        }
 
        // 2. Numeric train number
        int trainNumber;
        try {
            trainNumber = Integer.parseInt(trainText);
        } catch (NumberFormatException ex) {
            showError("Train number must be numeric.");
            return;
        }
 
        // 3. Valid date, not in the past
        try {
            LocalDate date = LocalDate.parse(dateText);
            if (date.isBefore(LocalDate.now())) {
                showError("Date of journey cannot be in the past.");
                return;
            }
        } catch (DateTimeParseException ex) {
            showError("Invalid date. Use the format yyyy-MM-dd (example: 2026-12-25).");
            return;
        }
 
        // 4. Source and destination must differ
        if (source.equalsIgnoreCase(destination)) {
            showError("Source and destination cannot be the same.");
            return;
        }
 
        try {
            String trainName = DatabaseHelper.getTrainName(trainNumber);
            if (trainName == null) {
                showError("Train number " + trainNumber + " does not exist.");
                return;
            }
 
            String pnr = DatabaseHelper.createReservation(passenger, trainNumber, trainName,
                    classType, dateText, source, destination, bookedBy);
 
            Reservation booking = DatabaseHelper.findReservation(pnr);
            JTextArea details = new JTextArea(booking.toDisplayText());
            details.setEditable(false);
            details.setFont(new Font("Monospaced", Font.PLAIN, 13));
            JOptionPane.showMessageDialog(this, details, "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }
 
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }
 
    private void clearForm() {
        passengerField.setText("");
        trainNumberField.setText("");
        trainNameField.setText("");
        classCombo.setSelectedIndex(0);
        dateField.setText("");
        sourceField.setText("");
        destinationField.setText("");
    }
}