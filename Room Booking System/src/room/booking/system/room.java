package room.booking.system;

public class room {
    private int id;
    private String number;
    private String type;
    private String availability;
    private double price;
    private String description;

    // Constructor without ID (for new room inserts)
    public room(String number, String type, String availability, double price, String description) {
        this.number = number;
        this.type = type;
        this.availability = availability;
        this.price = price;
        this.description = description;
    }

    // Constructor with ID (for updates)
    public room(int id, String number, String type, String availability, double price, String description) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.availability = availability;
        this.price = price;
        this.description = description;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getNumber() { return number; }
    public String getType() { return type; }
    public String getAvailability() { return availability; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setNumber(String number) { this.number = number; }
    public void setType(String type) { this.type = type; }
    public void setAvailability(String availability) { this.availability = availability; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }

    Object getroomNumber() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    String getroomType() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    boolean isAvailable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setroomType(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setAvailable(boolean selected) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
