package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.CustomerSupportMessage;

public class CustomerSupportMessage_DAO {
	private final ConnectDB db = ConnectDB.getInstance();
	private final Connection con;
	
	public CustomerSupportMessage_DAO() {
		db.connect();  // Initialize the database connection
        this.con = ConnectDB.getConnection();  // Get the connection object
	}
	
	public ArrayList<CustomerSupportMessage> getAll(String string, String string2, String string3, String string4) {
        String query = "SELECT * FROM CustomerSupportMessage";
        ArrayList<CustomerSupportMessage> list = new ArrayList<>();
        
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
            	String customerId = rs.getString(2);
                String employeeId = rs.getString(3);
                String content = rs.getNString(4);
                String status = rs.getNString(6);
                
                CustomerSupportMessage temp  = new CustomerSupportMessage(customerId, employeeId, content, status);
                
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
	}
	
	public CustomerSupportMessage getById(String customerId)
    {
    	String query = "SELECT * FROM CustomerSupportMessage where CustomerID=?";
    	CustomerSupportMessage temp = null;
    	
        try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,customerId); 
        	ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	String id = rs.getString(2);
                String employeeId = rs.getString(3);
                String content = rs.getNString(4);
                String status = rs.getNString(6);
                
                temp  = new CustomerSupportMessage(id, employeeId, content, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    public boolean add(CustomerSupportMessage CustomerSupportMessage)
    {
    	boolean result = false;
    	String query = "INSERT INTO CustomerSupportMessage (CustomerID, EmployeeID, Content, Status)\r\n"
    			+ "VALUES(?,?,?,?)";
    	try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,CustomerSupportMessage.getCustomerId()); 
        	stmt.setString(2,CustomerSupportMessage.getEmployeeId()); 
        	stmt.setString(3,CustomerSupportMessage.getContent()); 
        	stmt.setString(4,CustomerSupportMessage.getStatus()); 

            if (stmt.executeUpdate()>=1) 
            {
            	result = true;                
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public boolean update(CustomerSupportMessage CustomerSupportMessage)
    {
    	boolean result = false;
    	String query = "update CustomerSupportMessage set CustomerID=?, EmployeeID=?, Content=?, Status=? where CustomerID=?";
    	try {
        	PreparedStatement stmt = con.prepareStatement(query); 
        	stmt.setString(1,CustomerSupportMessage.getCustomerId()); 
        	stmt.setString(2,CustomerSupportMessage.getEmployeeId()); 
        	stmt.setString(3,CustomerSupportMessage.getContent()); 
        	stmt.setString(4,CustomerSupportMessage.getStatus());
        	stmt.setString(5,CustomerSupportMessage.getCustomerId()); 

            if (stmt.executeUpdate()>=1) 
            {
            	result = true;                
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }
    
    /*public boolean delete(String customerId)
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

    }*/
    
    public boolean checkExistById(String customerId)
    {
    	String query = "SELECT * FROM CustomerSupportMessage where CustomerID=?";
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
