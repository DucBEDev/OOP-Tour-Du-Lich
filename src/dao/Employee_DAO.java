package dao;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;
import java.util.ArrayList;

import connectDB.ConnectDB;

import entity.Employee;

public class Employee_DAO {
    private final ConnectDB db = ConnectDB.getInstance();
    private final Connection con;

    // Constructor to initialize the connection
    public Employee_DAO() {
        db.connect();  // Initialize the database connection
        this.con = ConnectDB.getConnection();  // Get the connection object
    }

    public ArrayList<Employee> getAll() {
        String query = "SELECT * FROM Employee WHERE STATUS = N'Đang làm việc'";
        ArrayList<Employee> list = new ArrayList<>();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String employeeId = rs.getString(1);
                String fullName = rs.getNString(2);
                String phone = rs.getString(3);
                String email = rs.getString(4);
                String address = rs.getNString(5);
                String userName = rs.getString(6);
                String password = rs.getString(7);
                String permissions = rs.getString(8);
                LocalDate hireDate = rs.getDate(9).toLocalDate();
                String status = rs.getNString(10);

                Employee temp = new Employee(employeeId, fullName, phone, email, address, userName, password, permissions, status);
                temp.setHireDate(hireDate);
                temp.setStatus(status);

                list.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Employee getById(String employeeId) {
        String query = "SELECT * FROM Employee WHERE EmployeeID = ?";
        Employee temp = null;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString(1);
                String fullName = rs.getNString(2);
                String phone = rs.getString(3);
                String email = rs.getString(4);
                String address = rs.getNString(5);
                String userName = rs.getString(6);
                String password = rs.getString(7);
                String permissions = rs.getString(8);
                LocalDate hireDate = rs.getDate(9).toLocalDate();
                String status = rs.getNString(10);


                temp = new Employee(id, fullName, phone, email, address, userName, password, permissions);
                temp.setHireDate(hireDate);
                temp.setStatus(status);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return temp;
    }
    
    public Employee getByUsername(String username) {
        String query = "SELECT * FROM Employee WHERE Username = ?";
        Employee temp = null;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString(1);
                String fullName = rs.getNString(2);
                String phone = rs.getString(3);
                String email = rs.getString(4);
                String address = rs.getNString(5);
                String userName = rs.getString(6);
                String password = rs.getString(7);
                String permissions = rs.getString(8);
                LocalDate hireDate = rs.getDate(9).toLocalDate();
                String status = rs.getNString(10);


                temp = new Employee(id, fullName, phone, email, address, userName, password, permissions, status);
                temp.setHireDate(hireDate);
                temp.setStatus(status);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public boolean add(Employee employee) {
        boolean result = false;
        String query = "INSERT INTO Employee (EmployeeID, FullName, Phone, Email, Address, Username, Password, Permissions, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employee.getEmployeeId());
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getPhone());
            stmt.setString(4, employee.getEmail());
            stmt.setString(5, employee.getAddress());
            stmt.setString(6, employee.getUserName());
            stmt.setString(7, employee.getPassWord());
            stmt.setString(8, employee.getPermissions());
            stmt.setString(9, employee.getStatus());
            
            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean update(Employee employee) {
        boolean result = false;
        String query = "UPDATE Employee SET FullName = ?, Phone = ?, Email = ?, Address = ?, Username = ?, Password = ?, Permissions = ?, Status = ? WHERE EmployeeID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employee.getFullName());
            stmt.setString(2, employee.getPhone());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getAddress());
            stmt.setString(5, employee.getUserName());
            stmt.setString(6, employee.getPassWord());
            stmt.setString(7, employee.getPermissions());
            stmt.setString(8, employee.getStatus());
            stmt.setString(9, employee.getEmployeeId());
            

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean delete(String employeeId) {
        boolean result = false;
        String query = "DELETE FROM Employee WHERE EmployeeID = ?";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employeeId);

            if (stmt.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean checkExistById(String employeeId) {
        String query = "SELECT * FROM Employee WHERE EmployeeID = ?";
        boolean result = false;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public boolean checkExistByUsername(String username, String password) {
        String query = "SELECT * FROM Employee WHERE Username = ? AND Password = ?";
        boolean result = false;

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public String generateNextEmployeeId() {
        String query = "SELECT MAX(EmployeeID) FROM Employee WHERE EmployeeID LIKE 'EMP%'";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId == null) {
                    return "EMP001";
                }
                
                if (maxId.length() >= 3) {
                    try {
                        int currentNum = Integer.parseInt(maxId.substring(3).trim());
                        return String.format("EMP%03d", currentNum + 1);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return "EMP001";
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "EMP000";
    }
}
