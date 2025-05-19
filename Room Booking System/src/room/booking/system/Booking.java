package room.booking.system;



import java.sql.Date;

/**
 * Represents a room booking in the hotel booking system.
 */
public class Booking {
    private int ID;
    private int Room_ID;
    private int Customer_ID;
    private Date Checkin_Date;
    private Date Checkout_Date;
    private String Status;
    private double Total_Price;
    
    /**
     * Constructor for creating a new booking (without ID)
     * @param Room_ID
     * @param Customer_ID
     * @param Checkin_Date
     * @param Checkout_Date
     * @param Status
     * @param Total_Price
     */
    public Booking(int Room_ID, int Customer_ID, Date Checkin_Date, Date Checkout_Date, 
                  String Status, double Total_Price) {
        this.Room_ID = Room_ID;
        this.Customer_ID = Customer_ID;
        this.Checkin_Date = Checkin_Date;
        this.Checkout_Date = Checkout_Date;
        this.Status = Status;
        this.Total_Price = Total_Price;
    }
    
    /**
     * Constructor for existing bookings (with ID)
     * @param ID
     * @param Room_ID
     * @param Customer_ID
     * @param Checkin_Date
     * @param Checkout_Date
     * @param Status
     * @param Total_Price
     */
    public Booking(int ID, int Room_ID, int Customer_ID, Date Checkin_Date, Date Checkout_Date, String Status, double Total_Price) {
        this.ID = ID;
        this.Room_ID = Room_ID;
        this.Customer_ID = Customer_ID;
        this.Checkin_Date = Checkin_Date;
        this.Checkout_Date = Checkout_Date;
        this.Status = Status;
        this.Total_Price = Total_Price;
    }
    
    // Getters and Setters
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRoom_ID() {
        return Room_ID;
    }

    public void setRoom_ID(int Room_ID) {
        this.Room_ID = Room_ID;
    }

    public int getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(int Customer_ID) {
        this.Customer_ID = Customer_ID;
    }

    public Date getCheckin_Date() {
        return Checkin_Date;
    }

    public void setCheckin_Date(Date Checkin_Date) {
        this.Checkin_Date = Checkin_Date;
    }

    public Date getCheckout_Date() {
        return Checkout_Date;
    }

    public void setCheckout_Date(Date Checkout_Date) {
        this.Checkout_Date = Checkout_Date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public double getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(double Total_Price) {
        this.Total_Price = Total_Price;
    }
    
    @Override
    public String toString() {
        return "Booking{" + "ID=" + ID + ", Room_ID=" + Room_ID + 
               ", Customer_ID=" + Customer_ID + ", Checkin_Date=" + Checkin_Date + 
               ", Checkout_Date=" + Checkout_Date + ", Status=" + Status + 
               ", Total_Price=" + Total_Price + '}';
    }

    Object getRoomNumber() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object getId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object getCheckinDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object getCustomerName() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object getCheckoutDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setCheckin_Date(String checkinDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}