package dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.time.LocalDate;

import connectDB.ConnectDB;

import entity.Customer;

@SuppressWarnings("unused")
public class Customer_DAO {
    private final ConnectDB db = ConnectDB.getInstance();
    private final Connection con;

    // Constructor to initialize the connection
    public Customer_DAO() {
        db.connect();  // Initialize the database connection
        this.con = ConnectDB.getConnection();  // Get the connection object
    }

    public ArrayList<Customer> getAll() {
        String query = "SELECT * FROM Customer";
        ArrayList<Customer> list = new ArrayList<>();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
            	String customerId = rs.getString(1);
                String fullName = rs.getNString(2);
                String phone = rs.getString(3);
                String email = rs.getString(4);
                String address = rs.getNString(5);
                String status = rs.getNString(6);
                String userName = rs.getString(7);
                String password = rs.getString(8);
                LocalDate createdAt = rs.getDate(9).toLocalDate();
                
                System.out.println("Retrieved status: " + status); 
                
                Customer temp  = new Customer(customerId, fullName, phone, email, address, status, userName, password);
                temp.setCreatedAt(createdAt);

                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public Customer getById(String customerId)
    {
    	String query = "SELECT * FROM Customer where CustomerID=?";
    	Customer temp = null;
    	
        try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customerId); 
        	ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	String customerIdTemp = rs.getString(1);
                String fullName = rs.getNString(2);
                String phone = rs.getString(3);
                String email = rs.getString(4);
                String address = rs.getNString(5);
                String status = rs.getNString(6);
                String userName = rs.getString(7);
                String password = rs.getString(8);
                LocalDate createdAt = rs.getDate(9).toLocalDate();
                
                temp  = new Customer(customerIdTemp, fullName, phone, email, address, status, userName, password);
                temp.setCreatedAt(createdAt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return temp;
    }
    
    public boolean add(Customer customer)
    {
    	boolean result = false;
    	String query = "INSERT INTO Customer (CustomerID, FullName, Phone, Email, Address, Status, Username, Password)\r\n"
    			+ "VALUES(?,?,?,?,?,?,?,?)";
    	try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customer.getCustomerId()); 
        	stmt.setString(2,customer.getFullName()); 
        	stmt.setString(3,customer.getPhone()); 
        	stmt.setString(4,customer.getEmail()); 
        	stmt.setString(5,customer.getAddress()); 
        	stmt.setString(6,customer.getStatus()); 
        	stmt.setString(7,customer.getUserName()); 
        	stmt.setString(8,customer.getPassword()); 

            if (stmt.executeUpdate()>=1) 
            {
            	result = true;                
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return result;

    }
    
    public boolean update(Customer customer)
    {
    	boolean result = false;
    	String query = "update Customer set CustomerID=?, FullName=?, Phone=?, Email=?, Address=?, Status=?, Username=?, Password=? where CustomerID=?";
    	try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customer.getCustomerId()); 
        	stmt.setString(2,customer.getFullName()); 
        	stmt.setString(3,customer.getPhone()); 
        	stmt.setString(4,customer.getEmail()); 
        	stmt.setString(5,customer.getAddress()); 
        	stmt.setString(6,customer.getStatus()); 
        	stmt.setString(7,customer.getUserName()); 
        	stmt.setString(8,customer.getPassword()); 
        	stmt.setString(9,customer.getCustomerId()); 


            if (stmt.executeUpdate()>=1) 
            {
            	result = true;                
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return result;

    }
    
    public boolean delete(String customerId)
    {
    	boolean result = false;
    	String query = "delete from Customer where CustomerID=?";
    	try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customerId); 
        	

            if (stmt.executeUpdate()>=1) 
            {
            	result = true;                
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return result;

    }
    
    public boolean checkExistById(String customerId)
    {
    	String query = "SELECT * FROM Customer where CustomerID=?";
    	boolean result=false;
    	
        try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customerId); 
        	ResultSet rs = stmt.executeQuery();
            result = rs.next();            
        }
         catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        return result;
    }
    
    

}

