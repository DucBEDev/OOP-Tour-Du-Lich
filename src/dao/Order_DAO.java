package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.math.BigDecimal;


import connectDB.ConnectDB;

import entity.Order;

public class Order_DAO {
	private final ConnectDB db = ConnectDB.getInstance();
	private final Connection con;
	
	public Order_DAO() {
		db.connect();
		this.con = ConnectDB.getConnection();
	}
	
	public ArrayList<Order> getAll() {
		String query = "SELECT * FROM [Order]";
		ArrayList<Order> list = new ArrayList<>();
		
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while(rs.next()) {
				String orderId = rs.getString(1);
                String customerId = rs.getString(2);
                String tourId = rs.getString(3);
                int adultTickets = rs.getInt(4);
                int childTickets = rs.getInt(5);
                LocalDateTime orderTime = rs.getTimestamp(6).toLocalDateTime();
                BigDecimal totalAmount = rs.getBigDecimal(7);
                String status = rs.getNString(9);
                String confirmedBy = rs.getString(10);

                Order temp = new Order(orderId, customerId, tourId, 0, 0, orderTime, totalAmount, status, confirmedBy);
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
	
	public Order getById(String orderId) {
		String query = "SELECT * FROM [Order] WHERE OrderID = ?";
		Order temp = null;
		
		try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
				String id = rs.getString(1);
                String customerId = rs.getString(2);
                String tourId = rs.getString(3);
                int adultTickets = rs.getInt(4);
                int childTickets = rs.getInt(5);
                LocalDateTime orderTime = rs.getTimestamp(6).toLocalDateTime();
                BigDecimal totalAmount = rs.getBigDecimal(7);
                String status = rs.getNString(8);
                String confirmedBy = rs.getString(9);

                temp = new Order(id, customerId, tourId, 0, 0, orderTime, totalAmount, status, confirmedBy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return temp;
	}
	
	public boolean add(Order order) {
		boolean result = false;
        String query = "INSERT INTO [Order] (OrderID, CustomerID, TourID, AdultTickets, ChildTickets, OrderTime, TotalAmount, Status, ConfirmedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getCustomerId());
            stmt.setString(3, order.getTourId());
            stmt.setInt(4, order.getAdultTickets());
            stmt.setInt(5, order.getChildTickets());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getOrderTime()));
            stmt.setBigDecimal(7, order.getTotalAmount());
            stmt.setString(8, order.getStatus());
            stmt.setString(9, order.getConfirmedBy());
            
            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public boolean update(Order order) {
        boolean result = false;
        String query = "UPDATE [Order] SET OrderID = ?, CustomerID = ?, TourID = ?, AdultTickets = ?, ChildTickets = ?, OrderTime = ?, TotalAmount = ?, Status = ?, ConfirmedBy = ? WHERE OrderID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getCustomerId());
            stmt.setString(3, order.getTourId());
            stmt.setInt(4, order.getAdultTickets());
            stmt.setInt(5, order.getChildTickets());
            stmt.setTimestamp(6, Timestamp.valueOf(order.getOrderTime()));
            stmt.setBigDecimal(7, order.getTotalAmount());
            stmt.setString(8, order.getStatus());
            stmt.setString(9, order.getConfirmedBy());
            stmt.setString(10, order.getOrderId());

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public boolean delete(String orderId) {
        boolean result = false;
        String query = "DELETE FROM [Order] WHERE OrderID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderId);

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public boolean checkExistById(String orderId) {
        String query = "SELECT * FROM [Order] WHERE OrderID = ?";
        boolean result = false;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public String generateNextOrderId() {
		String query = "SELECT MAX(OrderID) FROM [Order] WHERE OrderID LIKE 'ORD%'";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId == null) {
                    return "ORD001";
                }
                
                if (maxId.length() >= 3) {
                    try {
                        int currentNum = Integer.parseInt(maxId.substring(3).trim());
                        return String.format("ORD%03d", currentNum + 1);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return "ORD001";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ORD000";
    }
}
