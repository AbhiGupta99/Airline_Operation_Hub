package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Date;

import com.toedter.calendar.JDateChooser;
import java.util.*;

public class BookFlight extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    JTextField tfaadhar, tfpnr;
    JLabel tfname, tfnationality, tfaddress, labelgender, labelfname, labelfcode;
    JButton bookflight, fetchButton, flight, checkBooking;
    Choice source, destination;
    JDateChooser dcdate;

    public BookFlight() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("Book Flight");
        heading.setBounds(420, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        heading.setForeground(Color.BLUE);
        add(heading);

        addLabel("Aadhar", 60, 80);
        tfaadhar = addTextField(220, 80);
        
        fetchButton = addButton("Fetch User", 380, 80);
        fetchButton.addActionListener(this);

        addLabel("Name", 60, 130);
        tfname = addDataLabel(220, 130);

        addLabel("Nationality", 60, 180);
        tfnationality = addDataLabel(220, 180);

        addLabel("Address", 60, 230);
        tfaddress = addDataLabel(220, 230);

        addLabel("Gender", 60, 280);
        labelgender = addDataLabel(220, 280);

        addLabel("Source", 60, 330);
        source = addChoice(220, 330);

        addLabel("Destination", 60, 380);
        destination = addChoice(220, 380);

        try {
            Conn c = new Conn();
            String query = "SELECT DISTINCT source, destination FROM flight";
            PreparedStatement pstmt = c.getConnection().prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                source.add(rs.getString("source"));
                destination.add(rs.getString("destination"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        flight = addButton("Fetch Flights", 380, 380);
        flight.addActionListener(this);

        addLabel("Flight Name", 60, 430);
        labelfname = addDataLabel(220, 430);

        addLabel("Flight Code", 60, 480);
        labelfcode = addDataLabel(220, 480);

        addLabel("Date of Travel", 60, 530);
        dcdate = new JDateChooser();
        dcdate.setBounds(220, 530, 150, 25);
        add(dcdate);

        bookflight = addButton("Book Flight", 220, 580);
        bookflight.addActionListener(this);

        addLabel("Enter PNR:", 60, 630);
        tfpnr = addTextField(220, 630);

        checkBooking = addButton("Check Booking", 380, 630);
        checkBooking.addActionListener(this);

        setSize(1100, 700);
        setLocation(200, 50);
        setVisible(true);
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 25);
        label.setFont(new Font("Tahoma", Font.PLAIN, 16));
        add(label);
        return label;
    }

    private JLabel addDataLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 150, 25);
        add(label);
        return label;
    }

    private JTextField addTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 150, 25);
        add(textField);
        return textField;
    }

    private JButton addButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 150, 25);
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        add(button);
        return button;
    }

    private Choice addChoice(int x, int y) {
        Choice choice = new Choice();
        choice.setBounds(x, y, 150, 25);
        add(choice);
        return choice;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchButton) {
            fetchUserDetails();
        } else if (ae.getSource() == flight) {
            fetchFlightDetails();
        } else if (ae.getSource() == bookflight) {
            bookFlight();
        } else if (ae.getSource() == checkBooking) {
            checkBookingDetails();
        }
    }

    private void fetchUserDetails() {
        String aadhar = tfaadhar.getText();
        if (aadhar.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an Aadhar number.");
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM passenger WHERE aadhar = ?";
            PreparedStatement pst = conn.getConnection().prepareStatement(query);
            pst.setString(1, aadhar);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tfnationality.setText(rs.getString("nationality"));
                tfaddress.setText(rs.getString("address"));
                labelgender.setText(rs.getString("gender"));
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Aadhar. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchFlightDetails() {
        String src = source.getSelectedItem();
        String dest = destination.getSelectedItem();

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM flight WHERE source = ? AND destination = ?";
            PreparedStatement pst = conn.getConnection().prepareStatement(query);
            pst.setString(1, src);
            pst.setString(2, dest);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                labelfname.setText(rs.getString("f_name"));
                labelfcode.setText(rs.getString("f_code"));
            } else {
                JOptionPane.showMessageDialog(null, "No Flights Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bookFlight() {
        String aadhar = tfaadhar.getText();
        java.util.Date date = dcdate.getDate();

        if (aadhar.isEmpty() || date == null || labelfname.getText().isEmpty() || labelfcode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields before booking.");
            return;
        }

        int pnr = 100000 + new Random().nextInt(900000);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        try {
            Conn conn = new Conn();
            String query = "INSERT INTO booking (pnr, aadhar, name, nationality, address, gender, source, destination, flight_name, flight_code, travel_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.getConnection().prepareStatement(query);
            pst.setInt(1, pnr);
            pst.setString(2, aadhar);
            pst.setString(3, tfname.getText());
            pst.setString(4, tfnationality.getText());
            pst.setString(5, tfaddress.getText());
            pst.setString(6, labelgender.getText());
            pst.setString(7, source.getSelectedItem());
            pst.setString(8, destination.getSelectedItem());
            pst.setString(9, labelfname.getText());
            pst.setString(10, labelfcode.getText());
            pst.setDate(11, sqlDate);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Flight Booked! PNR: " + pnr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBookingDetails() {
        String pnr = tfpnr.getText().trim();

        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a PNR number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Conn conn = new Conn();
            String query = "SELECT * FROM booking WHERE PNR = ?";
            PreparedStatement pst = conn.getConnection().prepareStatement(query);
            pst.setString(1, pnr);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Fetch details
                String name = rs.getString("name");
                String flightName = rs.getString("flight_name");
                String flightCode = rs.getString("flight_code");
                String source = rs.getString("source");
                String destination = rs.getString("destination");
                Date travelDate = rs.getDate("travel_date");

                // Display confirmation
                String message = "Booking Confirmed!\n\n"
                        + "PNR: " + pnr + "\n"
                        + "Passenger Name: " + name + "\n"
                        + "Flight Name: " + flightName + " (" + flightCode + ")\n"
                        + "Route: " + source + " → " + destination + "\n"
                        + "Travel Date: " + travelDate;

                JOptionPane.showMessageDialog(null, message, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No booking found for PNR: " + pnr, "No Data", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error! Unable to fetch booking details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BookFlight();
    }
}
