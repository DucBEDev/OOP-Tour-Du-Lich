package application;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import dao.Customer_DAO;
import dao.Employee_DAO;
import dao.Tour_DAO;
import dao.Order_DAO;
import dao.CustomerSupportMessage_DAO;

import entity.Customer;
import entity.CustomerSupportMessage;
import entity.Employee;
import entity.Order;
import entity.Tour;

import gui.admin.SignIn;

public class Tour_du_lich {
	public static void main(String[] args) 
	{
       SignIn si = new SignIn();
       si.setVisible(true);
    }
}

/* Phần kiểm tra
 * Test Employee_DAO
 	ArrayList<Employee> list = new ArrayList<>();
		
		Employee_DAO employee_dao = new Employee_DAO();
		
		list = employee_dao.getAll();
		
		for(Employee e : list)
		{
			System.out.println(e);
		}
		
		Employee employee = new Employee(
	            null,              // employeeId - passing null will auto-generate an ID
	            "Reec James",        // fullName
	            "0123456789",      // phone (10-digit number)
	            "john.doe@example.com",  // email
	            "123 Main St",     // address
	            "johndoe",         // userName
	            "password123",     // password
	            Employee.PERMISSION_STAFF // permissions
	        );
		employee_dao.update(employee);
		list = employee_dao.getAll();
		
		for(Employee e : list)
		{
			System.out.println(e);
		}
	// Test Cutomer_DAO
		ArrayList<Customer> customer_list = new ArrayList<>();
		
		Customer_DAO customer_dao = new Customer_DAO();
		
		Customer customer = new Customer (
				null, //customerId
				"Long",
				"0123456789",
				"john.doe@example.com",
				"123 Main St",
				"Khách",
				"hehe",
				"1234"
				);
		customer_dao.update(customer);
		customer_list = customer_dao.getAll();
		
		for(Customer e : customer_list)
		{
			System.out.println(e);
		}
		
		// Test Tour_DAO
		ArrayList<Tour> tour_list = new ArrayList<>();
		
		Tour_DAO tour_dao = new Tour_DAO();
		
		Tour tour = new Tour(
	            "TOUR001",                                    // tourId
	            "Du lịch Đà Nẵng",                         // tourName
	            "Khám phá thành phố biển xinh đẹp",        // description
	            LocalDate.of(2024, 12, 12),                // departureDate
	            5,                                         // duration (số ngày)
	            "Hà Nội",                                  // departureLocation
	            LocalTime.of(8, 30),                       // departureTime
	            "Đà Nẵng",                                 // destination
	            "Xe",            // transportInfo
	            new BigDecimal("2000000"),                 // adultPrice
	            new BigDecimal("1000000"),                 // childPrice
	            50,                                        // maxParticipants
	            20,                                        // currentParticipants
	            Tour.STATUS_AVAILABLE                      // status
	        );

		tour_dao.update(tour);
		tour_list = tour_dao.getAll();
		
		for(Tour e : tour_list)
		{
			System.out.println(e);
		}
		
		// Test Order_DAO
		ArrayList<Order> order_list = new ArrayList<>();
		
		Order_DAO order_dao = new Order_DAO();
		
		Order order = new Order(
				"ORD004",
				"CUS004",
				"TOUR004",
				2,
				1,
				LocalDateTime.of(2024, 12, 3, 9, 10),
				new BigDecimal("5000000"),
				"Chờ thanh toán",
				"EMP002"
				);
		order_dao.update(order);
		
		order_list = order_dao.getAll();
		
		for (Order e : order_list)
		{
			System.out.println(e);
		}
		
		// Test Message_DAO
       ArrayList<CustomerSupportMessage> message_list = new ArrayList<>();
		
       CustomerSupportMessage_DAO CustomerSupportMessage_dao = new CustomerSupportMessage_DAO();
		
       CustomerSupportMessage CustomerSupportMessage = new CustomerSupportMessage();
       CustomerSupportMessage_dao.update(CustomerSupportMessage);
		
		message_list = CustomerSupportMessage_dao.getAll(
				"CUS001",
				"EMP001",
				"Cần thông tin chi tiết về lịch trình tour Đà Lạt",
				"Đã xử lý"
				);
		
		for (CustomerSupportMessage e : message_list)
		{
			System.out.println(e);
		}
*/
