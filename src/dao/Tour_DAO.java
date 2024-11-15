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

    public ArrayList<Tour> getAll() {
        String query = "SELECT * FROM Tour";
        ArrayList<Tour> list = new ArrayList<>();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String tourId = rs.getString(1);
                String tourName = rs.getNString(3);
                String description = rs.getNString(4);
                LocalDate departureDate = rs.getDate(5).toLocalDate();
                int duration = rs.getInt(6);
                String departureLocation = rs.getNString(7);
                LocalTime departureTime = rs.getTime(8).toLocalTime();
                String destination = rs.getNString(9);
                String transportInfo = rs.getNString(10);
                BigDecimal adultPrice = rs.getBigDecimal(11);
                BigDecimal childPrice = rs.getBigDecimal(12);
                int maxParticipants = rs.getInt(13);
                int currentParticipants = rs.getInt(14);
                String status = rs.getNString(15);

                Tour temp = new Tour(tourId, tourName, description, departureDate, 0, departureLocation, departureTime, destination, transportInfo, adultPrice, childPrice, 0 ,0, status);
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public ArrayList<Tour> getLimitedTours(int limit) {
        String query = "SELECT TOP (?) * FROM Tour WHERE Status = ? ORDER BY DepartureDate";
        ArrayList<Tour> list = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, limit);
            stmt.setNString(2, STATUS_AVAILABLE);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String tourId = rs.getString(1);
                String tourName = rs.getNString(3);
                String description = rs.getNString(4);
                LocalDate departureDate = rs.getDate(5).toLocalDate();
                int duration = rs.getInt(6);
                String departureLocation = rs.getNString(7);
                LocalTime departureTime = rs.getTime(8).toLocalTime();
                String destination = rs.getNString(9);
                String transportInfo = rs.getNString(10);
                BigDecimal adultPrice = rs.getBigDecimal(11);
                BigDecimal childPrice = rs.getBigDecimal(12);
                int maxParticipants = rs.getInt(13);
                int currentParticipants = rs.getInt(14);
                String status = rs.getNString(15);

                Tour temp = new Tour(tourId, tourName, description, departureDate, duration,
                    departureLocation, departureTime, destination, transportInfo,
                    adultPrice, childPrice, maxParticipants, currentParticipants, status);
                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Tour getByTourId(String tourId) {
        String query = "SELECT * FROM Tour WHERE TourID = ?";
        Tour temp = null;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tourId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	String id = rs.getString(1);
                String tourName = rs.getNString(2);
                String description = rs.getNString(3);
                LocalDate departureDate = rs.getDate(4) != null ? rs.getDate(4).toLocalDate() : null;
                int duration = rs.getInt(5);
                String departureLocation = rs.getNString(6);
                LocalTime departureTime = rs.getTime(7).toLocalTime();
                String destination = rs.getNString(8);
                String transportInfo = rs.getNString(9);
                BigDecimal adultPrice = rs.getBigDecimal(10);
                BigDecimal childPrice = rs.getBigDecimal(11);
                int maxParticipants = rs.getInt(12);
                int currentParticipants = rs.getInt(13);
                String status = rs.getNString(14);

                temp = new Tour(id, tourName, description, departureDate, 0, departureLocation, departureTime, destination, transportInfo, adultPrice, childPrice, 0 ,0, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public boolean add(Tour tour) {
        boolean result = false;
        String query = "INSERT INTO Tour (TourID, TourName, Description, DepartureDate, Duration, DepartureLocation, DepartureTime, Destination, TransportInfo, AdultPrice, ChildPrice, MaxParticipants, CurrentParticipants, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            stmt.setBigDecimal(10, tour.getAdultPrice());
            stmt.setBigDecimal(11, tour.getChildPrice());
            stmt.setInt(12, tour.getMaxParticipants());
            stmt.setInt(13, tour.getCurrentParticipants());
            stmt.setString(14, tour.getStatus());
            
            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean update(Tour tour) {
        boolean result = false;
        String query = "UPDATE Tour SET TourID = ?, TourName = ?, Description = ?, DepartureDate = ?, Duration = ?, DepartureLocation = ?, DepartureTime = ?, Destination = ?, TransportInfo = ?, AdultPrice = ?, ChildPrice = ?, MaxParticipants = ?,  CurrentParticipants = ?, Status = ? WHERE TourID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, tour.getTourId());
            stmt.setString(2, tour.getTourName());
            stmt.setString(3, tour.getDescription());
            //stmt.setDate(4, Date.valueOf(tour.getDepartureDate()));
            stmt.setDate(4, java.sql.Date.valueOf(tour.getDepartureDate()));
            stmt.setInt(5, tour.getDuration());
            stmt.setString(6, tour.getDepartureLocation());
            stmt.setTime(7, java.sql.Time.valueOf(tour.getDepartureTime()));
            stmt.setString(8, tour.getDestination());
            stmt.setString(9, tour.getTransportInfo());
            stmt.setBigDecimal(10, tour.getAdultPrice());
            stmt.setBigDecimal(11, tour.getChildPrice());
            stmt.setInt(12, tour.getMaxParticipants());
            stmt.setInt(13, tour.getCurrentParticipants());
            stmt.setString(14, tour.getStatus());
            stmt.setString(15, tour.getTourId());

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
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
}
