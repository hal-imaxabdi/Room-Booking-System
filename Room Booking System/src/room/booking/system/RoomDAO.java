package room.booking.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class RoomDAO {

    static int countAvailableRooms() {
        int count = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM rooms WHERE availability = 1");
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting available rooms: " + e.getMessage());
        }
        return count;
    }

    // Add new room
    public boolean addRoom(room room) {
        String sql = "INSERT INTO rooms (number, type, availability, price, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setInt(3, room.getAvailability().equalsIgnoreCase("Available") ? 1 : 0); // Convert string to int
            stmt.setDouble(4, room.getPrice());
            stmt.setString(5, room.getDescription());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting room: " + e.getMessage());
            return false;
        }
    }

    // Update existing room
    public boolean updateRoom(room room) {
        String sql = "UPDATE rooms SET number = ?, type = ?, availability = ?, price = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getNumber());
            stmt.setString(2, room.getType());
            stmt.setInt(3, room.getAvailability().equalsIgnoreCase("Available") ? 1 : 0); // Convert string to int
            stmt.setDouble(4, room.getPrice());
            stmt.setString(5, room.getDescription());
            stmt.setInt(6, room.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    // Get all rooms from database
    public List<room> getAllRooms() {
        List<room> rooms = new ArrayList<>();
        String sql = "SELECT id, number, type, availability, price, description FROM rooms";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("number");
                String type = rs.getString("type");
                int availability = rs.getInt("availability"); // Read as int
                double price = rs.getDouble("price");
                String description = rs.getString("description");
                
                // Convert int to string for room constructor
                String availabilityStr = availability == 1 ? "Available" : "Occupied";
                room newRoom = new room(id, number, type, availabilityStr, price, description);
                rooms.add(newRoom);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
        
        return rooms;
    }
    
    // Delete a room by ID
    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }
    
    // Get a room by ID
    public room getRoomById(int roomId) {
        String sql = "SELECT id, number, type, availability, price, description FROM rooms WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String number = rs.getString("number");
                    String type = rs.getString("type");
                    int availability = rs.getInt("availability"); // Read as int
                    double price = rs.getDouble("price");
                    String description = rs.getString("description");
                    
                    // Convert int to string for room constructor
                    String availabilityStr = availability == 1 ? "Available" : "Occupied";
                    return new room(id, number, type, availabilityStr, price, description);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching room by ID: " + e.getMessage());
        }
        
        return null;
    }

    public DefaultTableModel getAllRoomsTable() {
        String[] columns = {"ID", "Number", "Type", "Availability", "Price", "Description"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (room r : getAllRooms()) {
            model.addRow(new Object[]{
                r.getId(),
                r.getNumber(),
                r.getType(),
                r.getAvailability(), // Will display "Available" or "Occupied"
                r.getPrice(),
                r.getDescription()
            });
        }
        return model;
    }
}