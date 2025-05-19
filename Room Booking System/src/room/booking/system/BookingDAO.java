package room.booking.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableModel;

/**
 * Data Access Object for handling Booking database operations
 */
public class BookingDAO {

   
    
    /**
     * Creates a new booking in the database
     * @param booking The booking to be added
     * @return true if successful, false otherwise
     */
    public boolean BookingsScreen(Booking booking) {
        String sql = "INSERT INTO bookings (Room_ID, Customer_ID, Checkin_Date, Checkout_Date, Status, Total_Price) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getRoom_ID());
            pstmt.setInt(2, booking.getCustomer_ID());
            pstmt.setDate(3, booking.getCheckin_Date());
            pstmt.setDate(4, booking.getCheckout_Date());
            pstmt.setString(5, booking.getStatus());
            pstmt.setDouble(6, booking.getTotal_Price());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing booking in the database
     * @param booking The booking with updated information
     * @return true if successful, false otherwise
     */
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET Room_ID = ?, Customer_ID = ?, Checkin_Date = ?, " +
                     "Checkout_Date = ?, Status = ?, Total_Price = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getRoom_ID());
            pstmt.setInt(2, booking.getCustomer_ID());
            pstmt.setDate(3, booking.getCheckin_Date());
            pstmt.setDate(4, booking.getCheckout_Date());
            pstmt.setString(5, booking.getStatus());
            pstmt.setDouble(6, booking.getTotal_Price());
            pstmt.setInt(7, booking.getID());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deletes a booking from the database
     * @param bookingId The ID of the booking to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM bookings WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves all bookings from the database
     * @return List of all bookings
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("ID"),
                    rs.getInt("Room_ID"),
                    rs.getInt("Customer_ID"),
                    rs.getDate("Checkin_Date"),
                    rs.getDate("Checkout_Date"),
                    rs.getString("Status"),
                    rs.getDouble("Total_Price")
                );
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all bookings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    /**
     * Retrieves a specific booking by ID
     * @param bookingId The ID of the booking to retrieve
     * @return The booking object or null if not found
     */
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(
                        rs.getInt("ID"),
                        rs.getInt("Room_ID"),
                        rs.getInt("Customer_ID"),
                        rs.getDate("Checkin_Date"),
                        rs.getDate("Checkout_Date"),
                        rs.getString("Status"),
                        rs.getDouble("Total_Price")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving booking by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Retrieves all bookings for a specific customer
     * @param customerId The ID of the customer
     * @return List of bookings for the customer
     */
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE Customer_ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                        rs.getInt("ID"),
                        rs.getInt("Room_ID"),
                        rs.getInt("Customer_ID"),
                        rs.getDate("Checkin_Date"),
                        rs.getDate("Checkout_Date"),
                        rs.getString("Status"),
                        rs.getDouble("Total_Price")
                    );
                    bookings.add(booking);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings by customer ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    /**
     * Checks if a room is available for a specific date range
     * @param roomId The ID of the room to check
     * @param checkinDateStr Check-in date in string format (YYYY-MM-DD)
     * @param checkoutDateStr Check-out date in string format (YYYY-MM-DD)
     * @return true if available, false if not available or error occurs
     * @throws SQLException if a database access error occurs
     */
    public boolean isRoomAvailable(int roomId, String checkinDateStr, String checkoutDateStr) throws SQLException {
        String sql = "SELECT COUNT(*) FROM bookings WHERE Room_ID = ? " +
                     "AND Status != 'Cancelled' " +
                     "AND ((Checkin_Date <= ? AND Checkout_Date >= ?) OR " +
                     "(Checkin_Date <= ? AND Checkout_Date >= ?) OR " +
                     "(Checkin_Date >= ? AND Checkout_Date <= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            pstmt.setString(2, checkoutDateStr);  // New check-in is before existing check-out
            pstmt.setString(3, checkinDateStr);   // New check-out is after existing check-in
            pstmt.setString(4, checkinDateStr);   // New check-in is before existing check-in
            pstmt.setString(5, checkinDateStr);   // New check-out is after existing check-in
            pstmt.setString(6, checkinDateStr);   // New check-in is after existing check-in
            pstmt.setString(7, checkoutDateStr);  // New check-out is before existing check-out
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, room is available
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking room availability: " + e.getMessage());
            throw e; // Re-throw the exception to be handled by the caller
        }
        
        return false; // Default to unavailable if query fails
    }
    
    /**
     * Get a list of bookings by status
     * @param status The status to filter by (e.g., "Booked", "Checked-in", "Completed", "Cancelled")
     * @return List of bookings with the specified status
     */
    public List<Booking> getBookingsByStatus(String status) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE Status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                        rs.getInt("ID"),
                        rs.getInt("Room_ID"),
                        rs.getInt("Customer_ID"),
                        rs.getDate("Checkin_Date"),
                        rs.getDate("Checkout_Date"),
                        rs.getString("Status"),
                        rs.getDouble("Total_Price")
                    );
                    bookings.add(booking);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings by status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    /**
     * Get a list of bookings for a specific room
     * @param roomId The ID of the room
     * @return List of bookings for the specified room
     */
    public List<Booking> getBookingsByRoomId(int roomId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE Room_ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                        rs.getInt("ID"),
                        rs.getInt("Room_ID"),
                        rs.getInt("Customer_ID"),
                        rs.getDate("Checkin_Date"),
                        rs.getDate("Checkout_Date"),
                        rs.getString("Status"),
                        rs.getDouble("Total_Price")
                    );
                    bookings.add(booking);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings by room ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    /**
     * Gets all active bookings (status is not 'Cancelled' or 'Completed')
     * @return List of active bookings
     */
    public List<Booking> getActiveBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE Status != 'Cancelled' AND Status != 'Completed'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("ID"),
                    rs.getInt("Room_ID"),
                    rs.getInt("Customer_ID"),
                    rs.getDate("Checkin_Date"),
                    rs.getDate("Checkout_Date"),
                    rs.getString("Status"),
                    rs.getDouble("Total_Price")
                );
                bookings.add(booking);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving active bookings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bookings;
    }
    
    /**
     * Updates the status of a booking
     * @param bookingId The ID of the booking to update
     * @param newStatus The new status to set
     * @return true if successful, false otherwise
     */
    public boolean updateBookingStatus(int bookingId, String newStatus) {
        String sql = "UPDATE bookings SET Status = ? WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bookingId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    boolean addbooking(Booking booking) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    TableModel getAllBookingsTableModel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean addBooking(Booking booking) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}