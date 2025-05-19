package room.booking.system;

import room.booking.system.BookingDAO;
import room.booking.system.Booking;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.List;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.util.Objects;
import javax.swing.table.TableCellEditor;

/**
 * UI for managing hotel bookings
 */
public class BookingForm extends javax.swing.JFrame {
    private JTextField txtRoom_ID, txtCustomer_ID, txtCheckin_Date, txtCheckout_Date, txtTotal_Price, txtStatus_Edit;
    private JButton btnBook, btnUpdate, btnCancelEdit, btnHome;
    private JTextField txtSearchCustomerId;
    private JButton btnSearchCustomer;
    private DefaultTableModel tableModel;
    private JTable table;
    private int editingBookingId = -1;
    private final BookingDAO bookingDAO = new BookingDAO();

    // Custom renderer for table cells
    class WhiteTextRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(Color.BLACK);
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(Color.WHITE);
            } else {
                Color color1 = UIManager.getColor("Table.background");
                Color color2 = UIManager.getColor("Table.alternateRowColor");
                c.setBackground(row % 2 == 0 ? color1 : color2);
            }
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
            }
            return c;
        }
    }

    // Button renderer for Edit/Delete buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Button editor for Edit/Delete actions
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JButton button;
        private String command;
        private int clickedRow;

        public ButtonEditor() {
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            button.setText((value == null) ? "" : value.toString());
            this.command = (value == null) ? "" : value.toString();
            this.clickedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return command;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
            Object idObj = tableModel.getValueAt(clickedRow, 0);
            int bookingId = -1;
            if (idObj != null) {
                try {
                    bookingId = Integer.parseInt(idObj.toString());
                } catch (NumberFormatException ex) {
                    System.err.println("Error parsing booking ID: " + idObj);
                    return;
                }
            }
            if ("Edit".equals(command)) {
                editSelectedBooking(bookingId);
            } else if ("Delete".equals(command)) {
                deleteSelectedBooking(bookingId);
            }
        }
    }

    // Constructor
    public BookingForm() {
        setTitle("Booking Hotel");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainContentPanel = new JPanel(new BorderLayout());
        setContentPane(mainContentPanel);

        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();

        mainContentPanel.add(tablePanel, BorderLayout.CENTER);
        mainContentPanel.add(formPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);

        btnUpdate.setVisible(false);
        btnCancelEdit.setVisible(false);
        txtStatus_Edit.setVisible(false);

        loadTableData();
    }

    // Create the form panel for booking details
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            private final Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/room/icons/Blue-Gradient-Background-For-Free-Download.jpg"))).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    MediaTracker mt = new MediaTracker(this);
                    mt.addImage(backgroundImage, 0);
                    try {
                        mt.waitForAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mt.checkAll()) {
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        System.err.println("Form Background image failed to load.");
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(350, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel formTitleLabel = new JLabel("Booking Details", SwingConstants.CENTER);
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        formTitleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(formTitleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.1;
        panel.add(Box.createVerticalStrut(10), gbc);

        JLabel lblRoom_ID = new JLabel("Room ID:");
        lblRoom_ID.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(lblRoom_ID, gbc);
        txtRoom_ID = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtRoom_ID, gbc);

        JLabel lblCustomer_ID = new JLabel("Customer ID:");
        lblCustomer_ID.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblCustomer_ID, gbc);
        txtCustomer_ID = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtCustomer_ID, gbc);

        JLabel lblStatus_Edit = new JLabel("Status:");
        lblStatus_Edit.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblStatus_Edit, gbc);
        txtStatus_Edit = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtStatus_Edit, gbc);
        txtStatus_Edit.setVisible(false);
        lblStatus_Edit.setVisible(false);

        JLabel lblCheckin_Date = new JLabel("Check-in Date (YYYY-MM-DD):");
        lblCheckin_Date.setForeground(Color.WHITE);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        panel.add(lblCheckin_Date, gbc);
        txtCheckin_Date = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtCheckin_Date, gbc);

        JLabel lblCheckout_Date = new JLabel("Check-out Date (YYYY-MM-DD):");
        lblCheckout_Date.setForeground(Color.WHITE);
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblCheckout_Date, gbc);
        txtCheckout_Date = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtCheckout_Date, gbc);

        JLabel lblTotal_Price = new JLabel("Total Price:");
        lblTotal_Price.setForeground(Color.WHITE);
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblTotal_Price, gbc);
        txtTotal_Price = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(txtTotal_Price, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.1;
        panel.add(Box.createVerticalStrut(10), gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        btnBook = new JButton("Book Now");
        btnBook.setBackground(new Color(60, 179, 113));
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);
        btnBook.setMargin(new Insets(8, 20, 8, 20));
        panel.add(btnBook, gbc);

        btnUpdate = new JButton("Update Booking");
        btnCancelEdit = new JButton("Cancel Edit");
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setMargin(new Insets(8, 20, 8, 20));
        btnCancelEdit.setBackground(new Color(220, 20, 60));
        btnCancelEdit.setForeground(Color.WHITE);
        btnCancelEdit.setFocusPainted(false);
        btnCancelEdit.setMargin(new Insets(8, 20, 8, 20));

        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnUpdate, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnCancelEdit, gbc);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnHome = new JButton("Home");
        btnHome.setBackground(new Color(255, 165, 0));
        btnHome.setForeground(Color.WHITE);
        btnHome.setFocusPainted(false);
        btnHome.setMargin(new Insets(8, 20, 8, 20));
        panel.add(btnHome, gbc);

        gbc.gridy = 12;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        btnBook.addActionListener(e -> bookRoom());
        btnUpdate.addActionListener(e -> updateBookingAction());
        btnCancelEdit.addActionListener(e -> cancelEditingAction());
        btnHome.addActionListener(e -> goToHome());

        return panel;
    }

    // Navigate to Home.java
    private void goToHome() {
        try {
            Home home = new Home();
            home.setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening Home screen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create the table panel for displaying bookings
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            private Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/room/icons/biru.jpg"))).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    MediaTracker mt = new MediaTracker(this);
                    mt.addImage(backgroundImage, 0);
                    try {
                        mt.waitForAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mt.checkAll()) {
                        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    } else {
                        System.err.println("Table Background image failed to load.");
                    }
                }
            }
        };

        JLabel titleLabel = new JLabel("Booking Table", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        titleLabel.setBorder(new EmptyBorder(40, 0, 0, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel spacerBelowTitle = new JPanel();
        spacerBelowTitle.setOpaque(false);
        spacerBelowTitle.setPreferredSize(new Dimension(1, 80));
        contentPanel.add(spacerBelowTitle);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search by Customer ID:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        txtSearchCustomerId = new JTextField(15);
        btnSearchCustomer = new JButton("Search");
        btnSearchCustomer.setBackground(new Color(70, 130, 180));
        btnSearchCustomer.setForeground(Color.WHITE);
        btnSearchCustomer.setFocusPainted(false);
        searchPanel.add(searchLabel);
        searchPanel.add(txtSearchCustomerId);
        searchPanel.add(btnSearchCustomer);
        contentPanel.add(searchPanel);

        JPanel spacerBetweenSearchAndTable = new JPanel();
        spacerBetweenSearchAndTable.setOpaque(false);
        spacerBetweenSearchAndTable.setPreferredSize(new Dimension(1, 5));
        contentPanel.add(spacerBetweenSearchAndTable);

        tableModel = new DefaultTableModel(new String[]{"ID", "Room_id", "Customer_id", "Checkin_Date", "Checkout_Date", "Status", "Total_Price", "Edit", "Delete"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7 || column == 8;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7 || columnIndex == 8) {
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        contentPanel.add(scrollPane);

        panel.add(contentPanel, BorderLayout.CENTER);

        javax.swing.table.TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(100);
        columnModel.getColumn(7).setPreferredWidth(80);
        columnModel.getColumn(8).setPreferredWidth(80);

        WhiteTextRenderer whiteTextRenderer = new WhiteTextRenderer();
        for (int i = 0; i <= 6; i++) {
            columnModel.getColumn(i).setCellRenderer(whiteTextRenderer);
        }
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor();
        columnModel.getColumn(7).setCellRenderer(buttonRenderer);
        columnModel.getColumn(7).setCellEditor((TableCellEditor) buttonEditor);
        columnModel.getColumn(8).setCellRenderer(buttonRenderer);
        columnModel.getColumn(8).setCellEditor(buttonEditor);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setForeground(Color.BLACK);

        btnSearchCustomer.addActionListener(e -> performSearch());

        return panel;
    }

    // Perform search by Customer ID
    private void performSearch() {
        String searchIdText = txtSearchCustomerId.getText().trim();
        if (searchIdText.isEmpty()) {
            loadTableData();
        } else {
            try {
                int customerId = Integer.parseInt(searchIdText);
                loadTableDataByCustomerId(customerId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Customer ID must be a number.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Load table data by Customer ID
    private void loadTableDataByCustomerId(int customerId) {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getBookingsByCustomerId(customerId);
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                    booking.getID(),
                    booking.getRoom_ID(),
                    booking.getCustomer_ID(),
                    booking.getCheckin_Date(),
                    booking.getCheckout_Date(),
                    booking.getStatus(),
                    booking.getTotal_Price(),
                    "Edit",
                    "Delete"
            });
        }
        tableModel.fireTableDataChanged();
    }

    // Load all bookings into the table
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                    booking.getID(),
                    booking.getRoom_ID(),
                    booking.getCustomer_ID(),
                    booking.getCheckin_Date(),
                    booking.getCheckout_Date(),
                    booking.getStatus(),
                    booking.getTotal_Price(),
                    "Edit",
                    "Delete"
            });
        }
        tableModel.fireTableDataChanged();
    }

    // Clear form fields
    private void clearFormFields() {
        txtRoom_ID.setText("");
        txtCustomer_ID.setText("");
        txtCheckin_Date.setText("");
        txtCheckout_Date.setText("");
        txtTotal_Price.setText("");
        txtStatus_Edit.setText("");
        txtCustomer_ID.setVisible(true);
        setLabelVisibility("Customer ID:", true);
        txtStatus_Edit.setVisible(false);
        setLabelVisibility("Status:", false);
        editingBookingId = -1;
        btnBook.setVisible(true);
        btnUpdate.setVisible(false);
        btnCancelEdit.setVisible(false);
    }

    // Set label visibility
    private void setLabelVisibility(String labelText, boolean visible) {
        Component[] components = ((JPanel) getContentPane().getComponent(1)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().equals(labelText)) {
                comp.setVisible(visible);
                return;
            }
        }
    }

    // Book a room
    private void bookRoom() {
        if (editingBookingId != -1) {
            JOptionPane.showMessageDialog(this, "Currently in edit mode. Complete or cancel the edit.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String roomIDText = txtRoom_ID.getText().trim();
            String customerIDText = txtCustomer_ID.getText().trim();
            String checkinDateStr = txtCheckin_Date.getText().trim();
            String checkoutDateStr = txtCheckout_Date.getText().trim();
            String totalPriceText = txtTotal_Price.getText().trim();

            if (roomIDText.isEmpty() || customerIDText.isEmpty() || checkinDateStr.isEmpty() || checkoutDateStr.isEmpty() || totalPriceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int room_ID = Integer.parseInt(roomIDText);
            int customer_ID = Integer.parseInt(customerIDText);
            double total_Price = Double.parseDouble(totalPriceText);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.sql.Date checkinDateSql, checkoutDateSql;

            try {
                java.util.Date checkinDateUtil = dateFormat.parse(checkinDateStr);
                java.util.Date checkoutDateUtil = dateFormat.parse(checkoutDateStr);
                checkinDateSql = new java.sql.Date(checkinDateUtil.getTime());
                checkoutDateSql = new java.sql.Date(checkoutDateUtil.getTime());

                if (checkinDateUtil.after(checkoutDateUtil)) {
                    JOptionPane.showMessageDialog(this, "Check-out Date must be after Check-in Date.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean isAvailable;
            try {
                isAvailable = bookingDAO.isRoomAvailable(room_ID, checkinDateStr, checkoutDateStr);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error checking room availability: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isAvailable) {
                JOptionPane.showMessageDialog(this, "Room " + room_ID + " is not available for " + checkinDateStr + " to " + checkoutDateStr + ".", "Room Unavailable", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Booking booking = new Booking(room_ID, customer_ID, checkinDateSql, checkoutDateSql, "Booked", total_Price);
            if (bookingDAO.BookingsScreen(booking)) {
                JOptionPane.showMessageDialog(this, "Booking successful!");
                clearFormFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric data for Room ID, Customer ID, and Total Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update a booking
    private void updateBookingAction() {
        if (editingBookingId == -1) {
            JOptionPane.showMessageDialog(this, "No booking selected to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String roomIDText = txtRoom_ID.getText().trim();
            String customerIDText = txtCustomer_ID.getText().trim();
            String status = txtStatus_Edit.getText().trim();
            String checkinDateStr = txtCheckin_Date.getText().trim();
            String checkoutDateStr = txtCheckout_Date.getText().trim();
            String totalPriceText = txtTotal_Price.getText().trim();

            if (roomIDText.isEmpty() || customerIDText.isEmpty() || status.isEmpty() || checkinDateStr.isEmpty() || checkoutDateStr.isEmpty() || totalPriceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int room_ID = Integer.parseInt(roomIDText);
            int customer_ID = Integer.parseInt(customerIDText);
            double total_Price = Double.parseDouble(totalPriceText);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.sql.Date checkinDateSql, checkoutDateSql;

            try {
                java.util.Date checkinDateUtil = dateFormat.parse(checkinDateStr);
                java.util.Date checkoutDateUtil = dateFormat.parse(checkoutDateStr);
                checkinDateSql = new java.sql.Date(checkinDateUtil.getTime());
                checkoutDateSql = new java.sql.Date(checkoutDateUtil.getTime());

                if (checkinDateUtil.after(checkoutDateUtil)) {
                    JOptionPane.showMessageDialog(this, "Check-out Date must be after Check-in Date.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Booking updatedBooking = new Booking(editingBookingId, room_ID, customer_ID, checkinDateSql, checkoutDateSql, status, total_Price);
            if (bookingDAO.updateBooking(updatedBooking)) {
                JOptionPane.showMessageDialog(this, "Booking updated successfully.");
                clearFormFields();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric data for Room ID, Customer ID, and Total Price.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating booking: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cancel editing
    private void cancelEditingAction() {
        clearFormFields();
        JOptionPane.showMessageDialog(this, "Edit mode canceled.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Delete a booking
    private void deleteSelectedBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete Booking ID: " + bookingId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingDAO.deleteBooking(bookingId)) {
                JOptionPane.showMessageDialog(this, "Booking ID " + bookingId + " deleted.");
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete Booking ID " + bookingId + ".", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Edit a booking
    private void editSelectedBooking(int bookingId) {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking != null) {
            editingBookingId = bookingId;
            txtRoom_ID.setText(String.valueOf(booking.getRoom_ID()));
            txtCustomer_ID.setText(String.valueOf(booking.getCustomer_ID()));
            txtStatus_Edit.setText(booking.getStatus());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            txtCheckin_Date.setText(dateFormat.format(booking.getCheckin_Date()));
            txtCheckout_Date.setText(dateFormat.format(booking.getCheckout_Date()));
            txtTotal_Price.setText(String.valueOf(booking.getTotal_Price()));
            txtCustomer_ID.setVisible(true);
            setLabelVisibility("Customer ID:", true);
            txtStatus_Edit.setVisible(true);
            setLabelVisibility("Status:", true);
            btnBook.setVisible(false);
            btnUpdate.setVisible(true);
            btnCancelEdit.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Booking ID " + bookingId + " not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Main method
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(BookingForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new BookingForm().setVisible(true));
    }
}