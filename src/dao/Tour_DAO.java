package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import connectDB.ConnectDB;

import entity.Tour;

public class Tour_DAO {
    private final ConnectDB db = ConnectDB.getInstance();
    private final Connection con;
  
    public static final String STATUS_AVAILABLE = "Còn vé";
    public static final String STATUS_SOLD_OUT = "Hết vé";
    public static final String STATUS_CANCELLED = "Đã hủy";
    
    // Constructor to initialize the connection
    public Tour_DAO() {
        db.connect();  // Initialize the database connection
        this.con = ConnectDB.getConnection();  // Get the connection object
    }

    public ArrayList<Tour> getAll() 
    {
        String query = "SELECT * FROM Tour WHERE STATUS = N'Còn vé' OR STATUS = N'Hết vé'";
        ArrayList<Tour> list = new ArrayList<>();
        Order_DAO orderDAO = new Order_DAO();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) 
        {
            while (rs.next()) 
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
                        
                int maxParticipants = orderDAO.getMaxParticipants(tourId); 
                                             		
                int currentParticipants = orderDAO.getCurrentParticipants(tourId);
                
                String status = rs.getNString(15);
                
                Image image = decodeBase64ToImage(base64Image);

                if(image !=null)
                {Tour temp = new Tour( tourId,  image,  tourName,  description,  departureDate,  duration,  departureLocation, 
                         departureTime,  destination,  transportInfo,  adultPrice,  childPrice, 
                         maxParticipants,  currentParticipants,  status);
                list.add(temp);
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
    
    public ArrayList<Tour> getLimitedTours(int limit) {
        String query = "SELECT TOP (?) * FROM Tour WHERE Status = ? ORDER BY DepartureDate";
        ArrayList<Tour> list = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setNString(2, Tour.STATUS_AVAILABLE);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String tourId = rs.getString("TourID");
                String base64Image = rs.getString("Images");
                String tourName = rs.getNString("TourName");
                String description = rs.getNString("Description");
                LocalDate departureDate = rs.getDate("DepartureDate").toLocalDate();
                int duration = rs.getInt("Duration");
                String departureLocation = rs.getNString("DepartureLocation");
                LocalTime departureTime = rs.getTime("DepartureTime").toLocalTime();
                String destination = rs.getNString("Destination");
                String transportInfo = rs.getNString("TransportInfo");
                double adultPrice = rs.getDouble("AdultPrice");
                double childPrice = rs.getDouble("ChildPrice");
                int maxParticipants = rs.getInt("MaxParticipants");
                int currentParticipants = rs.getInt("CurrentParticipants");
                String status = rs.getNString("Status");

                Image image = decodeBase64ToImage(base64Image);

                Tour temp = new Tour(tourId, image, tourName, description, departureDate, duration,
                    departureLocation, departureTime, destination, transportInfo,
                    adultPrice, childPrice, maxParticipants, currentParticipants, status);
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Tour> searchTours(String departureLocation, String destination, String departureDate, String transport) {
        ArrayList<Tour> results = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Tour WHERE Status = ?");
        ArrayList<Object> params = new ArrayList<>();
        params.add(Tour.STATUS_AVAILABLE);
        
        // Add search conditions if parameters are provided
        if (departureLocation != null && !departureLocation.trim().isEmpty()) {
            queryBuilder.append(" AND DepartureLocation LIKE ?");
            params.add("%" + departureLocation + "%");
        }
        
        if (destination != null && !destination.trim().isEmpty()) {
            queryBuilder.append(" AND Destination LIKE ?");
            params.add("%" + destination + "%");
        }
        
        if (departureDate != null && !departureDate.trim().isEmpty()) {
            queryBuilder.append(" AND CONVERT(varchar, DepartureDate, 103) = ?");
            params.add(departureDate);
        }
        
        if (transport != null && !transport.trim().isEmpty()) {
            queryBuilder.append(" AND TransportInfo LIKE ?");
            params.add("%" + transport + "%");
        }
        
        queryBuilder.append(" ORDER BY DepartureDate");
        
        try (PreparedStatement stmt = con.prepareStatement(queryBuilder.toString())) {
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tourId = rs.getString("TourID");
                String base64Image = rs.getString("Images");
                String tourName = rs.getNString("TourName");
                String description = rs.getNString("Description");
                LocalDate depDate = rs.getDate("DepartureDate").toLocalDate();
                int duration = rs.getInt("Duration");
                String depLocation = rs.getNString("DepartureLocation");
                LocalTime departureTime = rs.getTime("DepartureTime").toLocalTime();
                String dest = rs.getNString("Destination");
                String transportInfo = rs.getNString("TransportInfo");
                double adultPrice = rs.getDouble("AdultPrice");
                double childPrice = rs.getDouble("ChildPrice");
                int maxParticipants = rs.getInt("MaxParticipants");
                int currentParticipants = rs.getInt("CurrentParticipants");
                String status = rs.getNString("Status");
                
                Image image = decodeBase64ToImage(base64Image);
                
                Tour tour = new Tour(tourId, image, tourName, description, depDate, duration, 
                    depLocation, departureTime, dest, transportInfo, 
                    adultPrice, childPrice, maxParticipants, currentParticipants, status);
                results.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return results;
    }

    public Tour getByTourId(String tourIdTemp) 
    {
        String query = "SELECT * FROM Tour WHERE TourID = ?";
        Tour temp = null;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tourIdTemp);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) 
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
                
                Image image = decodeBase64ToImage(base64Image);
                
                temp = new Tour( tourId,  image,  tourName,  description,  departureDate,  duration,  departureLocation, 
                        departureTime,  destination,  transportInfo,  adultPrice,  childPrice, 
                        maxParticipants,  currentParticipants,  status);
               
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

        return temp;
    }
        
    public boolean add(Tour tour, String base64Image) {
        boolean result = false;
        String query = "INSERT INTO Tour (TourID, Images, TourName, Description, DepartureDate, Duration, DepartureLocation, DepartureTime, Destination, TransportInfo, AdultPrice, ChildPrice, MaxParticipants, CurrentParticipants, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try 
        {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tour.getTourId());
            stmt.setString(2, base64Image);
            stmt.setString(3, tour.getTourName());
            stmt.setString(4, tour.getDescription());
            stmt.setDate(5, Date.valueOf(tour.getDepartureDate()));
            stmt.setInt(6, tour.getDuration());
            stmt.setString(7, tour.getDepartureLocation());
            stmt.setTime(8, java.sql.Time.valueOf(tour.getDepartureTime()));
            stmt.setString(9, tour.getDestination());
            stmt.setString(10, tour.getTransportInfo());
            stmt.setDouble(11, tour.getAdultPrice());
            stmt.setDouble(12, tour.getChildPrice());
            stmt.setInt(13, tour.getMaxParticipants());
            stmt.setInt(14, tour.getCurrentParticipants());
            stmt.setString(15, tour.getStatus());
            
            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean update(Tour tour,String base64Image) {
        boolean result = false;
        if(base64Image!=null)
        {
        	String query = "UPDATE Tour SET TourID = ?, Images=?, TourName = ?, Description = ?, DepartureDate = ?, Duration = ?, DepartureLocation = ?, DepartureTime = ?, Destination = ?, TransportInfo = ?, AdultPrice = ?, ChildPrice = ?, MaxParticipants = ?,  CurrentParticipants = ?, Status = ? WHERE TourID = ?";

            try {
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, tour.getTourId());
                stmt.setString(2, base64Image);
                stmt.setString(3, tour.getTourName());
                stmt.setString(4, tour.getDescription());
                stmt.setDate(5, Date.valueOf(tour.getDepartureDate()));
                stmt.setInt(6, tour.getDuration());
                stmt.setString(7, tour.getDepartureLocation());
                stmt.setTime(8, java.sql.Time.valueOf(tour.getDepartureTime()));
                stmt.setString(9, tour.getDestination());
                stmt.setString(10, tour.getTransportInfo());
                stmt.setDouble(11, tour.getAdultPrice());
                stmt.setDouble(12, tour.getChildPrice());
                stmt.setInt(13, tour.getMaxParticipants());
                stmt.setInt(14, tour.getCurrentParticipants());
                stmt.setString(15, tour.getStatus());
                stmt.setString(16,tour.getTourId());

                if (stmt.executeUpdate() >= 1) 
                {
                    result = true;
                }
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
        else
        {
        	String query = "UPDATE Tour SET TourID = ?, TourName = ?, Description = ?, DepartureDate = ?, Duration = ?, DepartureLocation = ?, DepartureTime = ?, Destination = ?, TransportInfo = ?, AdultPrice = ?, ChildPrice = ?, MaxParticipants = ?,  CurrentParticipants = ?, Status = ? WHERE TourID = ?";

            try {
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, tour.getTourId());
                stmt.setString(2, tour.getTourName());
                stmt.setString(3, tour.getDescription());
                stmt.setDate(4, Date.valueOf(tour.getDepartureDate()));
                stmt.setInt(5, tour.getDuration());
                stmt.setString(6, tour.getDepartureLocation());
                stmt.setTime(7, java.sql.Time.valueOf(tour.getDepartureTime()));
                stmt.setString(8, tour.getDestination());
                stmt.setString(9, tour.getTransportInfo());
                stmt.setDouble(10, tour.getAdultPrice());
                stmt.setDouble(11, tour.getChildPrice());
                stmt.setInt(12, tour.getMaxParticipants());
                stmt.setInt(13, tour.getCurrentParticipants());
                stmt.setString(14, tour.getStatus());
                stmt.setString(15,tour.getTourId());

                if (stmt.executeUpdate() >= 1) 
                {
                    result = true;
                }
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
        

        return result;
    }
    
    
    public boolean updateStatus(String tourId, boolean isSoldOut) 
    {
        boolean result = false;
        	String querySoldOut = "UPDATE Tour SET Status = N'Hết vé' WHERE TourID = ?";
        	String queryAvailable="UPDATE Tour SET Status = N'Còn vé' WHERE TourID = ?";
        	
        	System.out.println(tourId);
            try {
            	PreparedStatement stmt = null;
            	if(isSoldOut)  stmt = con.prepareStatement(querySoldOut);
            	else stmt = con.prepareStatement(queryAvailable);

                stmt.setString(1, tourId);
                
                System.out.println(stmt);

                if (stmt.executeUpdate() >= 1) 
                {
                    result = true;
                }
            } 
            
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
            
        return result;
    }
    
    public boolean updateCurrentParticipants(String tourId, int ticketsAmount) {
        boolean result = false;
        	String query = "UPDATE Tour SET CurrentParticipants = CurrentParticipants+ ? WHERE TourID = ?";
        	
        	System.out.println(tourId);
        	System.out.println(ticketsAmount);
            try {
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(2, tourId);
                stmt.setInt(1, ticketsAmount);
                
                System.out.println(stmt);

                if (stmt.executeUpdate() >= 1) 
                {
                    result = true;
                }
            } 
            
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
            
        return result;
    }
    

    public boolean delete(String tourId) {
        boolean result = false;
        String query = "DELETE FROM Tour WHERE TourID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tourId);

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean checkExistById(String tourId) {
        String query = "SELECT * FROM Tour WHERE TourID = ?";
        boolean result = false;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tourId);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public static Image decodeBase64ToImage(String base64Image) throws Exception 

    {
    	if (base64Image == null || base64Image.isEmpty()) 
    	{
            throw new IllegalArgumentException("Base64 image string is null or empty");
        }
    	
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    public String generateNextTourId() {
        String query = "SELECT MAX(TourID) FROM Tour WHERE TourID LIKE 'TOUR%'";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId == null) {
                    return "TOUR001";
                }
                
                if (maxId.length() >= 4) {
                    try {
                        int currentNum = Integer.parseInt(maxId.substring(4).trim());
                        return String.format("TOUR%03d", currentNum + 1);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return "TOUR001";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "TOUR000";
    }
    
    public int getTotalTourCount() {
    	String status = STATUS_AVAILABLE;
        int count = 0;
        String query;
        
        // If status is null or empty, count all tours
        if (status == null || status.trim().isEmpty()) {
            query = "SELECT COUNT(*) FROM Tour";
        } else {
            query = "SELECT COUNT(*) FROM Tour WHERE Status = ?";
        }

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            if (status != null && !status.trim().isEmpty()) {
                stmt.setString(1, status);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
