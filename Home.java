package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {

    public Home() {
        setLayout(null);
        
        // Load and scale image properly
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/front.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1600, 800, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1600, 800);
        add(image);
        
        JLabel heading = new JLabel("AIR INDIA WELCOMES YOU");
        heading.setBounds(500, 40, 1000, 40);
        heading.setForeground(Color.BLUE);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 36));
        image.add(heading);
        
        // Create menu bar
        setJMenuBar(createMenuBar());
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        
        JMenu details = new JMenu("Details");
        menubar.add(details);
        
        addMenuItem(details, "Flight Details");
        addMenuItem(details, "Add Customer Details");
        addMenuItem(details, "Book Flight");
        addMenuItem(details, "Journey Details");
        addMenuItem(details, "Cancel Ticket");

        JMenu ticket = new JMenu("Ticket");
        menubar.add(ticket);
        
        JMenuItem boardingPass = new JMenuItem("Boarding Pass");
        boardingPass.addActionListener(this);
        ticket.add(boardingPass);

        return menubar;
    }

    private void addMenuItem(JMenu menu, String title) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(this);
        menu.add(menuItem);
    }

    public void actionPerformed(ActionEvent ae) {
        String text = ae.getActionCommand();
        
        switch (text) {
            case "Add Customer Details":
                new AddCustomer();
                break;
            case "Flight Details":
                new FlightInfo();
                break;
            case "Book Flight":
                new BookFlight();
                break;
            case "Journey Details":
                new JourneyDetails();
                break;
            case "Cancel Ticket":
                new Cancel();
                break;
            case "Boarding Pass":
                new BoardingPass();  // Ensure there's a `BoardingPass` class
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid Option Selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}
