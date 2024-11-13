package entity;

import dao.Customer_DAO;

import java.time.*;
import java.time.LocalDate;

@SuppressWarnings("unused")
public final class Customer {
    public static final String STATUS_ACTIVE = "Đã đăng nhập";
    public static final String STATUS_INACTIVE = "Khách";

    private String customerId;
    private String fullName;	
    private static int nextCustomerId =1; // Long
    private String phone;
    private String email;
    private String address;
    private String status;
    private String userName;
    private LocalDate createdAt;

    @SuppressWarnings("unused")
    private String password;

    public Customer() {

    }
    
    public Customer(String customerId, String fullName, String phone, String email, String address, String status, String userName, String password)    {
    	if(customerId==null) setCustomerId(); else this.customerId = customerId;
        this.fullName = fullName;
        setPhone(phone);            // Kiểm tra tính hợp lệ của phone
        setEmail(email);            // Kiểm tra tính hợp lệ của email
        this.address = address;
        setStatus(status);      // Kiểm tra tính hợp lệ của status (ACTIVE / INACTIVE)
        this.userName = userName;
        setPassword(password);      // Kiểm tra password
    }
    
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId() 
    {
        this.customerId = String.format("EM%03d", nextCustomerId++); // Format the ID as EM001, EM002, etc.
    }

        //Generate Getter & Setter for fullName
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

        //Generate Getter for phone
    public String getPhone() {
        return phone;
    }

        //Check the phone number is valid
    public void setPhone(String phone) {
        if (phone.matches("\\d{10}")) {
            this.phone = phone;
        }
        else {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ, phải có 10 chữ số");
        }
    }

        //Generate Getter for email
    public String getEmail() {
        return email;
    }

        //Check the email number is valid
    public void setEmail(String email) {
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        }
        else {
            throw new IllegalArgumentException("Email không hợp lệ");
        }
    }

        //Generate Getter & Setter for address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

        //Generate Getter for status
    public String getStatus() {
        return status;
    }

        //Check if the status if Valid / Invalid
    public void setStatus(String status) {
        if (status.equals(STATUS_ACTIVE) || status.equals(STATUS_INACTIVE)) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException("Trạng thái không hợp lệ");
        }
    }

        //Generate Getter & Setter for username
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

  //Generate Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getCreatedAt() {
    	return createdAt;
    }

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
    public String toString()
    {
    	return "Customer ID: " +  customerId + ", Name: " + fullName + ", Phone: " + phone + ", Email: " + email + ", Address: " + address + ", Status: " + status + ", Username: " + userName + ", Password: " + password + ", Date Created: " +createdAt;
    }
}
