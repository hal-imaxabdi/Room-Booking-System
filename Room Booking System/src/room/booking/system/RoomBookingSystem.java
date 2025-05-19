package room.booking.system;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.IOException;

public class RoomBookingSystem {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Room Booking System");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load and scale background image dynamically
        try {
            BufferedImage originalImage = ImageIO.read(RoomBookingSystem.class.getResource("/room/icons/4.jpg"));
            frame.setContentPane(new BackgroundPanel(originalImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
 // Create a panel for the button positioned at the bottom-left
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false); // Transparent background

        JButton nextButton = new JButton("ENTER");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBackground(new Color(50, 100, 200)); // Blue shade
        nextButton.setForeground(Color.WHITE);
        nextButton.setPreferredSize(new Dimension(150, 40));
        nextButton.setFocusPainted(false);

        buttonPanel.add(nextButton);
        frame.add(buttonPanel, BorderLayout.SOUTH); // Place at bottom

        nextButton.addActionListener(e -> {
            frame.dispose();
            new Home().setVisible(true);
        });
        nextButton.addActionListener(e -> {
            frame.dispose();
            new Home().setVisible(true);
        });

        frame.setVisible(true);
    }
}

// Custom JPanel for background scaling
class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel(BufferedImage img) {
        backgroundImage = img.getScaledInstance(1200, 700, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}