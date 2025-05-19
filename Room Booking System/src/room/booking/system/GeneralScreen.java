package room.booking.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;


public class GeneralScreen extends javax.swing.JFrame {

    // Main panels
    private JPanel mainContentPanel;
    private JTabbedPane tabbedPane;
    private JPanel customerPanel;
    private JPanel roomPanel;
    private JButton btnBackToHome;
    
    // Customer table components
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    // Room table components
    private JTable roomTable;
    private DefaultTableModel roomTableModel;
    private final RoomDAO roomDAO = new RoomDAO();
    
    // Constants for theme
    private final Color THEME_DARK_ORANGE = new Color(153, 51, 0); // Dark orange for headers
    private final Color THEME_LIGHT_ORANGE = new Color(255, 204, 153); // Light orange for background
    private final Color THEME_ACCENT_ORANGE = new Color(255, 102, 0); // Accent orange for buttons
    private final Color THEME_WHITE = Color.WHITE;
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Constructor
     */
    public GeneralScreen() {
        initializeScreen();
        createMainLayout();
        createCustomerPanel();
        createRoomPanel();
        createFooter();
        
        // Load initial data
        loadCustomerData();
        loadRoomData();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Initialize screen properties
     */
   private void initializeScreen() {
    setTitle("Hotel Management System");
    setSize(1200, 700); // Match Home window size
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setResizable(true); // Match Home's non-resizable property
    setLocationRelativeTo(null); // Center the window
    
    // Create main content panel
    mainContentPanel = new JPanel(new BorderLayout());
    mainContentPanel.setBackground(THEME_LIGHT_ORANGE);
    mainContentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(mainContentPanel);
}
    
    /**
     * Create the main layout with tabbed pane
     */
    private void createMainLayout() {
        // Header panel with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(THEME_DARK_ORANGE);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Hotel Management System", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(THEME_WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Tabbed pane for customer and room panels
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(THEME_DARK_ORANGE);
        tabbedPane.setForeground(THEME_WHITE);
        
        customerPanel = new JPanel(new BorderLayout());
        customerPanel.setOpaque(false);
        
        roomPanel = new JPanel(new BorderLayout());
        roomPanel.setOpaque(false);
        
        tabbedPane.addTab("Customer Management", customerPanel);
        tabbedPane.addTab("Room Management", roomPanel);
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the customer panel with table
     */
    private void createCustomerPanel() {
        String[] columns = {"ID", "Name", "Phone", "Email", "Address", "Registration Date", "Actions"};
        customerTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        customerTable = new JTable(customerTableModel);
        customerTable.setRowHeight(40);
        customerTable.setFont(TABLE_FONT);
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        customerTable.getTableHeader().setBackground(THEME_DARK_ORANGE);
        customerTable.getTableHeader().setForeground(THEME_WHITE);
        customerTable.setGridColor(THEME_ACCENT_ORANGE);
        
        setupCustomerTableButtons();
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel customerHeaderLabel = new JLabel("Customer Management", SwingConstants.CENTER);
        customerHeaderLabel.setFont(HEADER_FONT);
        customerHeaderLabel.setForeground(THEME_DARK_ORANGE);
        customerHeaderLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        customerPanel.add(customerHeaderLabel, BorderLayout.NORTH);
        customerPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create the room panel with table
     */
    private void createRoomPanel() {
        String[] columns = {"ID", "Number", "Type", "Availability", "Price", "Description", "Actions"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        
        roomTable = new JTable(roomTableModel);
        roomTable.setRowHeight(40);
        roomTable.setFont(TABLE_FONT);
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(THEME_DARK_ORANGE);
        roomTable.getTableHeader().setForeground(THEME_WHITE);
        roomTable.setGridColor(THEME_ACCENT_ORANGE);
        
        setupRoomTableButtons();
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel roomHeaderLabel = new JLabel("Room Management", SwingConstants.CENTER);
        roomHeaderLabel.setFont(HEADER_FONT);
        roomHeaderLabel.setForeground(THEME_DARK_ORANGE);
        roomHeaderLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        roomPanel.add(roomHeaderLabel, BorderLayout.NORTH);
        roomPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Create footer with back button
     */
    private void createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        btnBackToHome = new JButton("Back to Home");
        styleButton(btnBackToHome, THEME_ACCENT_ORANGE, THEME_WHITE);
        btnBackToHome.setPreferredSize(new Dimension(200, 40));
        
        btnBackToHome.addActionListener(e -> returnToHome());
        
        footerPanel.add(btnBackToHome);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Style a button with given colors
     */
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 1, true));
        button.setMargin(new Insets(10, 25, 10, 25));
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Setup customer table buttons for edit and delete
     */
    private void setupCustomerTableButtons() {
        customerTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        customerTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()) {
            @Override
            public void editButtonClicked(int row) {
                int customerId = (int) customerTable.getValueAt(row, 0);
                Customer customer = customerDAO.getCustomerById(customerId);
                if (customer != null) {
                    showCustomerEditDialog(customer);
                }
            }
            
            @Override
            public void deleteButtonClicked(int row) {
                int customerId = (int) customerTable.getValueAt(row, 0);
                String customerName = (String) customerTable.getValueAt(row, 1);
                
                int confirm = JOptionPane.showConfirmDialog(
                    GeneralScreen.this,
                    "Are you sure you want to delete customer: " + customerName + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (customerDAO.deleteCustomer(customerId)) {
                        JOptionPane.showMessageDialog(
                            GeneralScreen.this,
                            "Customer deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        loadCustomerData();
                    } else {
                        JOptionPane.showMessageDialog(
                            GeneralScreen.this,
                            "Failed to delete customer.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }
    
    /**
     * Show dialog to edit customer
     */
    private void showCustomerEditDialog(Customer customer) {
        JDialog editDialog = new JDialog(this, "Edit Customer", true);
        editDialog.setSize(400, 300);
        editDialog.setLayout(new GridLayout(6, 2, 10, 10));
        editDialog.setLocationRelativeTo(this);
        
        // Form fields
        JTextField nameField = new JTextField(customer.getName());
        JTextField phoneField = new JTextField(customer.getPhone());
        JTextField emailField = new JTextField(customer.getEmail());
        JTextField addressField = new JTextField(customer.getAddress());
        JTextField dateField = new JTextField(customer.getRegistrationDate().toString());
        
        JButton saveButton = new JButton("Save");
        styleButton(saveButton, THEME_ACCENT_ORANGE, THEME_WHITE);
        
        editDialog.add(new JLabel("Name:"));
        editDialog.add(nameField);
        editDialog.add(new JLabel("Phone:"));
        editDialog.add(phoneField);
        editDialog.add(new JLabel("Email:"));
        editDialog.add(emailField);
        editDialog.add(new JLabel("Address:"));
        editDialog.add(addressField);
        editDialog.add(new JLabel("Registration Date (YYYY-MM-DD):"));
        editDialog.add(dateField);
        editDialog.add(new JLabel());
        editDialog.add(saveButton);
        
        saveButton.addActionListener(e -> {
            try {
                Customer updatedCustomer = new Customer(
                    customer.getID(),
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    Date.valueOf(dateField.getText())
                );
                if (customerDAO.updateCustomer(updatedCustomer)) {
                    JOptionPane.showMessageDialog(
                        GeneralScreen.this,
                        "Customer updated successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    loadCustomerData();
                    editDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        GeneralScreen.this,
                        "Failed to update customer.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                    GeneralScreen.this,
                    "Invalid date format. Use YYYY-MM-DD.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        
        editDialog.setVisible(true);
    }
    
    /**
     * Setup room table buttons for edit and delete
     */
    private void setupRoomTableButtons() {
        roomTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        roomTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()) {
            @Override
            public void editButtonClicked(int row) {
                int roomId = (int) roomTable.getValueAt(row, 0);
                room room = roomDAO.getRoomById(roomId);
                if (room != null) {
                    showRoomEditDialog(room);
                }
            }
            
            @Override
            public void deleteButtonClicked(int row) {
                int roomId = (int) roomTable.getValueAt(row, 0);
                String roomNumber = (String) roomTable.getValueAt(row, 1);
                
                int confirm = JOptionPane.showConfirmDialog(
                    GeneralScreen.this,
                    "Are you sure you want to delete room: " + roomNumber + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (roomDAO.deleteRoom(roomId)) {
                        JOptionPane.showMessageDialog(
                            GeneralScreen.this,
                            "Room deleted successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        loadRoomData();
                    } else {
                        JOptionPane.showMessageDialog(
                            GeneralScreen.this,
                            "Failed to delete room.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }
    
    /**
     * Show dialog to edit room
     */
    private void showRoomEditDialog(room room) {
    JDialog editDialog = new JDialog(this, "Edit Room", true);
    editDialog.setSize(400, 300);
    editDialog.setLayout(new GridLayout(6, 2, 10, 10));
    editDialog.setLocationRelativeTo(this);
    
    // Form fields
    JTextField numberField = new JTextField(room.getNumber());
    JTextField typeField = new JTextField(room.getType());
    JComboBox<String> availabilityCombo = new JComboBox<>(new String[]{"Available", "Occupied"});
    availabilityCombo.setSelectedItem(room.getAvailability());
    JTextField priceField = new JTextField(String.valueOf(room.getPrice()));
    JTextField descriptionField = new JTextField(room.getDescription());
    
    JButton saveButton = new JButton("Save");
    styleButton(saveButton, THEME_ACCENT_ORANGE, THEME_WHITE);
    
    editDialog.add(new JLabel("Room Number:"));
    editDialog.add(numberField);
    editDialog.add(new JLabel("Type:"));
    editDialog.add(typeField);
    editDialog.add(new JLabel("Availability:"));
    editDialog.add(availabilityCombo);
    editDialog.add(new JLabel("Price:"));
    editDialog.add(priceField);
    editDialog.add(new JLabel("Description:"));
    editDialog.add(descriptionField);
    editDialog.add(new JLabel());
    editDialog.add(saveButton);
    
    saveButton.addActionListener(e -> {
        try {
            room updatedRoom = new room(
                room.getId(),
                numberField.getText(),
                typeField.getText(),
                (String) availabilityCombo.getSelectedItem(), // Still passes string
                Double.parseDouble(priceField.getText()),
                descriptionField.getText()
            );
            if (roomDAO.updateRoom(updatedRoom)) {
                JOptionPane.showMessageDialog(
                    GeneralScreen.this,
                    "Room updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                loadRoomData();
                editDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(
                    GeneralScreen.this,
                    "Failed to update room.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                GeneralScreen.this,
                "Invalid price format.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    });
    
    editDialog.setVisible(true);
}
    
    /**
     * Load customer data into the table
     */
    private void loadCustomerData() {
        customerTableModel.setRowCount(0);
        
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            for (Customer c : customers) {
                customerTableModel.addRow(new Object[]{
                    c.getID(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getRegistrationDate(),
                    "Edit/Delete"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error loading customer data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Load room data into the table
     */
   private void loadRoomData() {
    roomTableModel.setRowCount(0);
    
    try {
        List<room> rooms = roomDAO.getAllRooms();
        for (room r : rooms) {
            roomTableModel.addRow(new Object[]{
                r.getId(),
                r.getNumber(),
                r.getType(),
                r.getAvailability(), // Displays "Available" or "Occupied"
                r.getPrice(),
                r.getDescription(),
                "Edit/Delete"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Error loading room data: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
    
    /**
     * Return to home screen
     */
    private void returnToHome() {
        new Home().setVisible(true);
        this.dispose();
    }
    
    /**
     * Custom button renderer for the actions column
     */
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton editButton = new JButton("Edit");
        private JButton deleteButton = new JButton("Delete");
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(false);
            
            styleButton(editButton, THEME_ACCENT_ORANGE, THEME_WHITE);
            styleButton(deleteButton, new Color(220, 53, 69), THEME_WHITE);
            
            add(editButton);
            add(deleteButton);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    /**
     * Custom button editor for the actions column
     */
    abstract class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton editButton;
        protected JButton deleteButton;
        protected int clickedRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(false);
            
            editButton = new JButton("Edit");
            deleteButton = new JButton("Delete");
            styleButton(editButton, THEME_ACCENT_ORANGE, THEME_WHITE);
            styleButton(deleteButton, new Color(220, 53, 69), THEME_WHITE);
            
            editButton.addActionListener(e -> {
                fireEditingStopped();
                editButtonClicked(clickedRow);
            });
            
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                deleteButtonClicked(clickedRow);
            });
            
            panel.add(editButton);
            panel.add(deleteButton);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            clickedRow = row;
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Edit/Delete";
        }
        
        public abstract void editButtonClicked(int row);
        public abstract void deleteButtonClicked(int row);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        pack();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(GeneralScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> new GeneralScreen().setVisible(true));
    }
}