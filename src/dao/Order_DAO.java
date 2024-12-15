package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Image;
import java.math.BigDecimal;


import connectDB.ConnectDB;

import entity.Order;
import entity.Tour;

public class Order_DAO {
	private final ConnectDB db = ConnectDB.getInstance();
	private final Connection con;
	
	public Order_DAO() {
		db.connect();
		this.con = ConnectDB.getConnection();
	}
	
	public ArrayList<Order> getAll() {
		String query = "SELECT * FROM [Order] WHERE STATUS = N'Đã thanh toán' OR STATUS = N'Hoàn thành'";
		ArrayList<Order> list = new ArrayList<>();
		
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while(rs.next()) {
				String orderId = rs.getString(1);
                String customerId = rs.getString(2);
                String tourId = rs.getString(3);
                int adultTickets = rs.getInt(4);
                int childTickets = rs.getInt(5);
                LocalDateTime orderTime = rs.getTimestamp(6).toLocalDateTime();
                double totalAmount = rs.getDouble(7);
                String status = rs.getNString(9);
                String confirmedBy = rs.getString(10);

                Order temp = new Order(orderId, customerId, tourId, adultTickets, childTickets, orderTime, totalAmount, status, confirmedBy);
                System.out.println(temp);
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
	
	public ArrayList<Order> getAllUnconfirmed() {
		String query = "SELECT * FROM [Order] WHERE STATUS = N'Chờ thanh toán'";
		ArrayList<Order> list = new ArrayList<>();
		
		try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while(rs.next()) {
				String orderId = rs.getString(1);
                String customerId = rs.getString(2);
                String tourId = rs.getString(3);
                int adultTickets = rs.getInt(4);
                int childTickets = rs.getInt(5);
                LocalDateTime orderTime = rs.getTimestamp(6).toLocalDateTime();
                double totalAmount = rs.getDouble(7);
                String status = rs.getNString(9);
                String confirmedBy = rs.getString(10);

                Order temp = new Order(orderId, customerId, tourId, adultTickets, childTickets, orderTime, totalAmount, status, confirmedBy);
                System.out.println(temp);
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
                double totalAmount = rs.getDouble(7);
                String status = rs.getNString(9);
                String confirmedBy = rs.getString(10);

                temp = new Order(id, customerId, tourId, adultTickets, childTickets, orderTime, totalAmount, status, confirmedBy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return temp;
	}
	
	
	public ArrayList<Tour> getMostTravelTourByInputTime(LocalDate begin, LocalDate end)
    {
    	String query = " SELECT \r\n"
    			+ "    T.TourID,\r\n"
    			+ "	T.Images,\r\n"
    			+ "    T.TourName,\r\n"
    			+ "    T.Description,\r\n"
    			+ "    T.DepartureDate,\r\n"
    			+ "    T.Duration,\r\n"
    			+ "    T.DepartureLocation,\r\n"
    			+ "    T.DepartureTime,\r\n"
    			+ "    T.Destination,\r\n"
    			+ "    T.TransportInfo,\r\n"
    			+ "    T.AdultPrice,\r\n"
    			+ "    T.ChildPrice,\r\n"
    			+ "    T.MaxParticipants,\r\n"
    			+ "    T.CurrentParticipants,\r\n"
    			+ "    T.Status,\r\n"
    			+ "    COUNT(O.OrderID) AS FREQUENCY\r\n"
    			+ "FROM \r\n"
    			+ "    Tour AS T\r\n"
    			+ "JOIN \r\n"
    			+ "    [Order] AS O \r\n"
    			+ "ON \r\n"
    			+ "    T.TourID = O.TourID\r\n"
    			+ "WHERE \r\n"
    			+ "    CAST(O.OrderTime AS DATE) BETWEEN ? AND ?\r\n"
    			+ "GROUP BY \r\n"
    			+ "    T.TourID,\r\n"
    			+ "	T.Images,\r\n"
    			+ "    T.TourName,\r\n"
    			+ "    T.Description,\r\n"
    			+ "    T.DepartureDate,\r\n"
    			+ "    T.Duration,\r\n"
    			+ "    T.DepartureLocation,\r\n"
    			+ "    T.DepartureTime,\r\n"
    			+ "    T.Destination,\r\n"
    			+ "    T.TransportInfo,\r\n"
    			+ "    T.AdultPrice,\r\n"
    			+ "    T.ChildPrice,\r\n"
    			+ "    T.MaxParticipants,\r\n"
    			+ "    T.CurrentParticipants,\r\n"
    			+ "    T.Status\r\n"
    			+ "ORDER BY \r\n"
    			+ "    FREQUENCY DESC;";
    	ArrayList<Tour> list = new ArrayList<>();
    	
    	LocalDateTime dateTimeBegin = begin.atTime(LocalTime.MIDNIGHT);
    	LocalDateTime dateTimeEnd = end.atTime(LocalTime.MIDNIGHT);

	
         try 
         { 
        	 PreparedStatement stmt = con.prepareStatement(query);
        	 stmt.setTimestamp(1, Timestamp.valueOf(dateTimeBegin));
             stmt.setTimestamp(2, Timestamp.valueOf(dateTimeEnd));
             ResultSet rs = stmt.executeQuery();
             int count = 0; 
             while (rs.next() && count <10) 
             {
                 String tourId = rs.getString(1);
                 String base64Image = rs.getString(2);
                 String tourName = rs.getNString(3);
                 String description = rs.getNString(4);
                 LocalDate departureDate = rs.getDate(5).toLocalDate();
                 int duration = rs.getInt(6);
                 String departureLocation = rs.getNString(7);
                 LocalTime departureTime = rs.getTime(8).toLocalTime();
                 String destination = rs.getNString(9);
                 String transportInfo = rs.getNString(10);
                 double adultPrice = rs.getDouble(11);
                 double childPrice = rs.getDouble(12);
                 int maxParticipants = rs.getInt(13);
                 int currentParticipants = rs.getInt(14);
                 String status = rs.getNString(15);
                 
                 Image image = Tour_DAO.decodeBase64ToImage(base64Image);

                 if(image !=null)
                 {Tour temp = new Tour( tourId,  image,  tourName,  description,  departureDate,  duration,  departureLocation, 
                          departureTime,  destination,  transportInfo,  adultPrice,  childPrice, 
                          maxParticipants,  currentParticipants,  status);
                 list.add(temp);
                 count++;
                 }
                 else throw new IllegalArgumentException("image is null or empty");
             }
             
         } 
         catch (SQLException e)
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }
	
	
	public HashMap<String, Integer> getMostTravelDestinationAllTime()
    {
    	String query = "SELECT DESTINATION, COUNT(O.OrderID) AS FREQUENCY FROM Tour AS T, [Order] AS O WHERE T.TourID = O.TourID GROUP BY T.DESTINATION ORDER BY FREQUENCY DESC";
    	HashMap<String, Integer> list = new HashMap<String, Integer>();
	
         try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query))
         {             
             while (rs.next()) 
             {
             	String destination = rs.getString(1);
	            System.out.println(destination);

             	int frequency = rs.getInt(2);     
	            System.out.println( frequency);

             	list.put(destination, frequency);
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }
    	
    
    public HashMap<String, Integer> getMostTravelDestinationByYear(int year)
    {
    	String query = "SELECT DESTINATION, COUNT(O.OrderID) AS FREQUENCY FROM Tour AS T JOIN [Order] AS O ON T.TourID = O.TourID WHERE YEAR(O.ORDERTIME) = ? GROUP BY T.DESTINATION ORDER BY FREQUENCY DESC";
    	HashMap<String, Integer> list = new HashMap<String, Integer>();
	
         try 
         { 
        	 PreparedStatement stmt = con.prepareStatement(query);
             stmt.setInt(1, year);
             ResultSet rs = stmt.executeQuery();

             while (rs.next()) 
             {
             	String destination = rs.getString(1);
             	int frequency = rs.getInt(2);            	
             	list.put(destination, frequency);
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }
    	
    
    public HashMap<String, Integer> getMostTravelDestinationByInputTime(LocalDate begin, LocalDate end)
    {
    	String query = "SELECT DESTINATION, COUNT(O.OrderID) AS FREQUENCY FROM Tour AS T JOIN [Order] AS O ON T.TourID = O.TourID WHERE ORDERTIME BETWEEN ? AND ? GROUP BY T.DESTINATION ORDER BY FREQUENCY DESC";
    	HashMap<String, Integer> list = new HashMap<String, Integer>();
    	
    	LocalDateTime dateTimeBegin = begin.atTime(LocalTime.MIDNIGHT);
    	LocalDateTime dateTimeEnd = end.atTime(LocalTime.MIDNIGHT);

	
         try 
         { 
        	 PreparedStatement stmt = con.prepareStatement(query);
        	 stmt.setTimestamp(1, Timestamp.valueOf(dateTimeBegin));
             stmt.setTimestamp(2, Timestamp.valueOf(dateTimeEnd));
             ResultSet rs = stmt.executeQuery();

             while (rs.next()) 
             {
             	String destination = rs.getString(1);
	            System.out.println(destination);

             	int frequency = rs.getInt(2);    
	            System.out.println( frequency);

             	list.put(destination, frequency);
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }

    
    public HashMap<Integer, Double > getTotalAmountAllTime()
    {
    	String query = "SELECT MONTH(o.orderTime) AS month, SUM(o.totalAmount) AS totalAmount\r\n"
    			+ "FROM tour AS t\r\n"
    			+ "JOIN [order] AS o ON t.tourId = o.tourId\r\n"
    			+ "GROUP BY MONTH(o.orderTime)\r\n"
    			+ "ORDER BY MONTH(o.orderTime);";
    	HashMap<Integer, Double> list = new HashMap<Integer, Double>();
	
         try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query))
         {             
             while (rs.next()) 
             {            	
            	 int month = rs.getInt(1);      
            	 double totalAmount = rs.getDouble(2);
             	list.put(month, totalAmount);            	
             	
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }
    
    
    public HashMap<Integer, Double > getTotalAmountByYear(int year)
    {
    	String query = "SELECT MONTH(o.orderTime) AS month, SUM(o.totalAmount) AS totalAmount\r\n"
    			+ "FROM tour AS t\r\n"
    			+ "JOIN [order] AS o ON t.tourId = o.tourId\r\n"
    			+ "WHERE YEAR(o.orderTime) = ?  -- You can change the year as needed\r\n"
    			+ "GROUP BY MONTH(o.orderTime)\r\n"
    			+ "ORDER BY MONTH(o.orderTime);";
    	HashMap<Integer, Double> list = new HashMap<Integer, Double>();
	
         try
         {     
        	 PreparedStatement stmt = con.prepareStatement(query);
             stmt.setInt(1, year);
             ResultSet rs = stmt.executeQuery();
             
             while (rs.next()) 
             {            	
            	 int month = rs.getInt(1);      
            	 double totalAmount = rs.getDouble(2);
             	list.put(month, totalAmount);
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return list;
    }
    
    
    public double getTotalAmountInputTime(LocalDate begin, LocalDate end)
    {
    	String query = "SELECT SUM(O.TotalAmount)  AS FREQUENCY FROM Tour AS T JOIN [Order] AS O ON T.TourID = O.TourID WHERE ORDERTIME BETWEEN ? AND ?";
    	HashMap<String, Integer> list = new HashMap<String, Integer>();
    	
    	LocalDateTime dateTimeBegin = begin.atTime(LocalTime.MIDNIGHT);
    	LocalDateTime dateTimeEnd = end.atTime(LocalTime.MIDNIGHT);
    	double result = 0;
	
         try
         {     
        	 PreparedStatement stmt = con.prepareStatement(query);
        	 stmt.setTimestamp(1, Timestamp.valueOf(dateTimeBegin));
             stmt.setTimestamp(2, Timestamp.valueOf(dateTimeEnd));
             ResultSet rs = stmt.executeQuery();
             
             if (rs.next()) 
             {            	
            	 result = rs.getDouble(1);            	
             	
             }
         } 
         catch (SQLException e) 
         {
             e.printStackTrace();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }

         return result;
    }
	
	public boolean add(Order order) {
		boolean result = false;
        String query = "INSERT INTO [Order] (OrderID, CustomerID, TourID, AdultTickets, ChildTickets, TotalAmount, Status, ConfirmedBy) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getCustomerId());
            stmt.setString(3, order.getTourId());
            stmt.setInt(4, order.getAdultTickets());
            stmt.setInt(5, order.getChildTickets());
         //   stmt.setTimestamp(6, Timestamp.valueOf(order.getOrderTime()));
            stmt.setDouble(6, order.getTotalAmount());
            stmt.setString(7, order.getStatus());
            stmt.setString(8, order.getConfirmedBy());
            
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
            stmt.setDouble(7, order.getTotalAmount());
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
	
	public int getCurrentParticipants(String tourId) {
	    String query = "SELECT " +
	                   "    COALESCE(SUM(o.AdultTickets + o.ChildTickets), 0) AS CurrentParticipants " +
	                   "FROM " +
	                   "    Tour t " +
	                   "LEFT JOIN " +
	                   "    [Order] o ON t.TourID = o.TourID " +
	                   "WHERE " +
	                   "    t.TourID = ? " +
	                   "GROUP BY " +
	                   "    t.TourID;";
	    int result = 0;
	    
	    
	    System.out.println(tourId);
	    
	    try {
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setString(1, tourId); // Bind the tourId parameter
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) { // Use if since there's only one row expected
	            result = rs.getInt("CurrentParticipants");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    System.out.println(result);


	    return result;
	}
	
	
	public int getMaxParticipants(String tourId) {
	    String query = "SELECT MaxParticipants FROM Tour WHERE TourID = ?";
	    int result = 0;

	    try {
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setString(1, tourId); // Bind the tourId parameter
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) { // Use if since there's only one row expected
	            result = rs.getInt("MaxParticipants");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return result;
	}
}
