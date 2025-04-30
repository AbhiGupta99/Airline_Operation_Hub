package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;

public class Cancel extends JFrame implements ActionListener {
    
    private JTextField tfpnr;
    private JLabel tfname, cancellationno, lblfcode, lbldateoftravel;
    private JButton fetchButton, cancelButton;
    private final Random random = new Random();

    public Cancel() {
        setupUI();
    }

    private void setupUI() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = createLabel("CANCELLATION", 180, 20, 250, 35, 32);
        add(heading);

        JLabel lblpnr = createLabel("PNR Number", 60, 80, 150, 25, 16);
        add(lblpnr);

        tfpnr = new JTextField();
        tfpnr.setBounds(220, 80, 150, 25);
        add(tfpnr);

        fetchButton = createButton("Show Details", 380, 80, 120, 25);
        fetchButton.addActionListener(this);
        add(fetchButton);

        add(createLabel("Name", 60, 130, 150, 25, 16));
        tfname = createDataLabel(220, 130);

        add(createLabel("Cancellation No", 60, 180, 150, 25, 16));
        cancellationno = createDataLabel(220, 180);
        cancellationno.setText(String.valueOf(random.nextInt(1000000)));

        add(createLabel("Flight Code", 60, 230, 150, 25, 16));
        lblfcode = createDataLabel(220, 230);

        add(createLabel("Date", 60, 280, 150, 25, 16));
        lbldateoftravel = createDataLabel(220, 280);

        cancelButton = createButton("Cancel", 220, 330, 120, 25);
        cancelButton.addActionListener(this);
        add(cancelButton);

        setSize(800, 450);
        setLocation(350, 150);
        setVisible(true);
    }

    private JLabel createLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        return label;
    }

    private JLabel createDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 150, 25);
        add(label);
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchButton) {
            fetchDetails();
        } else if (ae.getSource() == cancelButton) {
            cancelTicket();
        }
    }

    private void fetchDetails() {
        String pnr = tfpnr.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a PNR number!");
            return;
        }
        try {
            Conn conn = new Conn();
            PreparedStatement pst = conn.getConnection().prepareStatement("SELECT * FROM reservation WHERE PNR = ?");
            pst.setString(1, pnr);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                lblfcode.setText(rs.getString("flightcode"));
                lbldateoftravel.setText(rs.getString("ddate"));
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid PNR number.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching details.");
            e.printStackTrace();
        }
    }

    private void cancelTicket() {
        String pnr = tfpnr.getText().trim();
        if (tfname.getText().isEmpty() || lblfcode.getText().isEmpty() || lbldateoftravel.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fetch details before canceling.");
            return;
        }
        try {
            Conn conn = new Conn();
            Connection dbConn = conn.getConnection();

            String insertQuery = "INSERT INTO cancel (PNR, name, cancellation_no, flightcode, date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst1 = dbConn.prepareStatement(insertQuery);
            pst1.setString(1, pnr);
            pst1.setString(2, tfname.getText());
            pst1.setString(3, cancellationno.getText());
            pst1.setString(4, lblfcode.getText());
            pst1.setString(5, lbldateoftravel.getText());
            pst1.executeUpdate();

            String deleteQuery = "DELETE FROM reservation WHERE PNR = ?";
            PreparedStatement pst2 = dbConn.prepareStatement(deleteQuery);
            pst2.setString(1, pnr);
            pst2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ticket Canceled Successfully.");
            setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error canceling ticket.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Cancel();
    }
}
