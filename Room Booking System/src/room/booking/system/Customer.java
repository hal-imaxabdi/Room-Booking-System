package room.booking.system;

import java.sql.Date;

/**
 * Customer entity class that represents a customer in the room booking system
 */
public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Date registrationDate;
    
    /**
     * Constructor with ID for existing customers
     * @param id
     * @param name
     * @param phone
     * @param email
     * @param address
     * @param registrationDate
     */
    public Customer(int id, String name, String phone, String email, String address, Date registrationDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.registrationDate = registrationDate;
    }
    
    /**
     * Constructor without ID for new customers
     */
    public Customer(String name, String phone, String email, String address, Date registrationDate) {
        this(-1, name, phone, email, address, registrationDate);
    }
    
    // Getters and setters
    public int getID() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", phone=" + phone + 
               ", email=" + email + ", address=" + address + ", registrationDate=" + registrationDate + '}';
    }

   
    
}