package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import net.proteanit.sql.DbUtils;

public class JourneyDetails extends JFrame implements ActionListener {
    JTable table;
    JTextField pnrField;
    JButton showButton;

    public JourneyDetails() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblPnr = new JLabel("PNR:");
        lblPnr.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPnr.setBounds(50, 50, 120, 25);
        add(lblPnr);

        pnrField = new JTextField();
        pnrField.setBounds(160, 50, 120, 25);
        add(pnrField);

        showButton = new JButton("Show Details");
        showButton.setBackground(Color.BLACK);
        showButton.setForeground(Color.WHITE);
        showButton.setBounds(290, 50, 150, 25);
        showButton.addActionListener(this);
        add(showButton);

        table = new JTable();
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(10, 100, 760, 300);
        add(jsp);

        setSize(800, 500);
        setLocation(400, 150);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String pnrNumber = pnrField.getText().trim();

        if (pnrNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "PNR cannot be empty!", "Input Error", JOptionPane.WARNING_MESSAGE);
            pnrField.requestFocus();
            return;
        }

        try {
            Conn conn = new Conn();
            Connection dbConn = conn.getConnection();

            // Query to fetch both current and previously searched PNR details
            String query = "SELECT * FROM booking WHERE PNR = ? " +
                           "UNION " +
                           "SELECT * FROM booking WHERE PNR IN (SELECT DISTINCT PNR FROM booking_history)";

            try (PreparedStatement pst = dbConn.prepareStatement(query)) {
                pst.setString(1, pnrNumber);
                ResultSet rs = pst.executeQuery();

                if (!rs.isBeforeFirst()) { // No data found
                    JOptionPane.showMessageDialog(this, "No booking found for PNR: " + pnrNumber, "No Data", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    table.setModel(DbUtils.resultSetToTableModel(rs)); // Append data to table
                }
            }

            // Save PNR to history if it is not already present
            String historyQuery = "INSERT INTO booking_history (PNR) " +
                                  "SELECT ? WHERE NOT EXISTS (SELECT 1 FROM booking_history WHERE PNR = ?)";

            try (PreparedStatement pstHistory = dbConn.prepareStatement(historyQuery)) {
                pstHistory.setString(1, pnrNumber);
                pstHistory.setString(2, pnrNumber);
                pstHistory.executeUpdate();
            }

            pnrField.setText(""); // Clear PNR field after search
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error! Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(JourneyDetails::new);
    }
}
