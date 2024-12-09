package gui.admin;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.*;

import dao.Customer_DAO;
import dao.Employee_DAO;
import dao.Order_DAO;
import dao.Tour_DAO;
import entity.Employee;
import entity.Order;

public class OrderDetail extends JPanel {
    private JPanel functionButton;
    private JPanel formPanel;

    private JLabel backButton;
    private JLabel editButton;

    private JButton saveButton;
    private JButton cancelButton;
    private JButton confirmButton;

    private JLabel orderIdLabel;
    private JLabel customerIdLabel;
    private JLabel tourIdLabel;
    private JLabel adultTicketsLabel;
    private JLabel childTicketsLabel;
    private JLabel orderTimeLabel;
    private JLabel totalAmountLabel;
    private JLabel statusLabel;
    private JLabel confirmedByLabel;

    private JTextField orderIdContent;
    private JTextField customerIdContent;
    private JTextField tourIdContent;
    private JTextField adultTicketsContent;
    private JTextField childTicketsContent;
    private JTextField orderTimeContent;
    private JTextField totalAmountContent;
    private JComboBox<String> statusContent;
    private JTextField confirmedByContent;

    private String orderId;
    private String customerId;
    private String tourId;
    private int adultTickets;
    private int childTickets;
    private LocalDateTime orderTime;
    private double totalAmount;
    private String status;
    private String confirmedBy;
    
    private Order_DAO orderDAO = new Order_DAO();
    
    private Customer_DAO customerDAO = new Customer_DAO();
    
    private Tour_DAO tourDAO = new Tour_DAO();
    
    private Employee_DAO employeeDAO = new Employee_DAO();
    
    private boolean isConfirmed;
    
    private Employee employee;

    public OrderDetail(Order order, boolean isConfirmed, Employee employee) 
    {
    	this.employee=employee;
    	this.isConfirmed=isConfirmed;
    	
        setLayout(new BorderLayout(10, 10));

        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 10, 10));

        functionButton = new JPanel();
        functionButton.setLayout(new BorderLayout());

        backButton = new JLabel("Thoát");
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createLineBorder(Color.black));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                removeAll();
        		add(new OrderManagement(employee));
                revalidate();
                repaint();
            }
        });
        
        if(!isConfirmed)
        {
        	editButton = new JLabel("Sửa");
            editButton.setPreferredSize(new Dimension(100, 50));
            editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editButton.setBorder(BorderFactory.createLineBorder(Color.black));
            editButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    enableEditing(true);
                }
            });
        }

      

        functionButton.add(backButton, BorderLayout.WEST);
        if(!isConfirmed) functionButton.add(editButton, BorderLayout.EAST);
       

       
    

    
        orderIdLabel = new JLabel("Mã đơn hàng:");
        orderIdContent = new JTextField();
        orderIdContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String orderIdTemp = orderIdContent.getText();
                if(orderDAO.checkExistById(orderIdTemp))
                {
                	orderId = orderIdTemp;
              
                	System.out.println("Order ID: " + orderId);
                }
                else
                	{
                		System.out.println("Order Not Found");
                		orderIdContent.setText("");
                	}
            }
        });

        customerIdLabel = new JLabel("Mã khách hàng:");
        customerIdContent = new JTextField();
        customerIdContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String customerIdTemp = customerIdContent.getText();
                if(customerDAO.checkExistById(customerIdTemp))
                {
                	customerId = customerIdTemp;
              
                	System.out.println("Customer ID: " + customerId);
                }
                else
                	{
                		System.out.println("Customer Not Found");
                		customerIdContent.setText("");
                	}
            }
        });
        

        tourIdLabel = new JLabel("Mã tour:");
        tourIdContent = new JTextField();
        tourIdContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String tourIdTemp = tourIdContent.getText();
                if(tourDAO.checkExistById(tourIdTemp))
                {
                	tourId = tourIdTemp;             
                	System.out.println("Tour ID: " + tourId);
                }
                else
                	{
                		System.out.println("Tour Not Found");
                		tourIdContent.setText("");
                	}
            }
        });
        

        adultTicketsLabel = new JLabel("Vé người lớn:");
        adultTicketsContent = new JTextField();
        adultTicketsContent.addActionListener(e -> 
        {
            String adultTicketsTemp = adultTicketsContent.getText();
            if (adultTicketsTemp.matches("\\d+"))
            {
                adultTickets = Integer.parseInt(adultTicketsTemp);
            	System.out.println("Adult Tickets: " + adultTickets);
            } 
            else 
            {
                showInvalidInputMessage(adultTicketsContent);
            }
        });
        

        childTicketsLabel = new JLabel("Vé trẻ em:");
        childTicketsContent = new JTextField();
        childTicketsContent.addActionListener(e -> 
        {
            String childTicketsTemp = childTicketsContent.getText();
            if (childTicketsTemp.matches("\\d+")) 
            {
                childTickets = Integer.parseInt(childTicketsTemp);
            	System.out.println("Child Tickets: " + childTickets);
            } 
            else 
            {
                showInvalidInputMessage(childTicketsContent);
            }
        });
        

        orderTimeLabel = new JLabel("Thời gian đặt:");
        orderTimeContent = new JTextField();
        orderTimeContent.addActionListener(evt->
        {
            
                String departureDateTemp = orderTimeContent.getText();
                if (departureDateTemp.matches("\\d{4}-\\d{2}-\\d{2-\\d{2}-\\d{2}"))
                {
                    String[] dateTimeParts = departureDateTemp.split("-");
                    
                    int year = Integer.parseInt(dateTimeParts[0]);
                    int month = Integer.parseInt(dateTimeParts[1]);
                    int day = Integer.parseInt(dateTimeParts[2]);
                    
                    int hour = Integer.parseInt(dateTimeParts[3]);
                    int minute = Integer.parseInt(dateTimeParts[4]);
                 
                    
                    
                    orderTime = LocalDateTime.of(year, month, day, hour, minute);
                    
                    System.out.println("Order Time: " + orderTime);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    orderTimeContent.setText("");
                }
            
        });
        

        totalAmountLabel = new JLabel("Tổng tiền:");
        totalAmountContent = new JTextField();
        totalAmountContent.addActionListener(e -> 
        {
            String input = totalAmountContent.getText();
            if (input.matches("\\d+(\\.\\d+)?")) 
            {
                totalAmount = Double.parseDouble(input);
            	System.out.println("Total Amount: " + totalAmount);
            } 
            else
            {
                showInvalidInputMessage(totalAmountContent);
            }
        });
        

        statusLabel = new JLabel("Trạng thái:");
        statusContent = new JComboBox<>(new String[]{"Đã thanh toán", "Chờ thanh toán", "Hủy", "Hoàn thành"});
        statusContent.addActionListener(e -> status = statusContent.getSelectedItem().toString());
        

        confirmedByLabel = new JLabel("Xác nhận bởi:");
        confirmedByContent = new JTextField();
        
        
        
        if (order != null) {
             orderId = order.getOrderId();
             customerId = order.getCustomerId();
             tourId = order.getTourId();
             adultTickets = order.getAdultTickets();
             childTickets = order.getChildTickets();
             orderTime = order.getOrderTime();
             totalAmount = order.getTotalAmount();
             status = order.getStatus();
             confirmedBy = order.getConfirmedBy();

            orderIdContent.setText(orderId);
            customerIdContent.setText(customerId);
            tourIdContent.setText(tourId);
            adultTicketsContent.setText(String.valueOf(adultTickets));
            childTicketsContent.setText(String.valueOf(childTickets));
            orderTimeContent.setText(orderTime.toString());
            totalAmountContent.setText(String.format("%.2f", totalAmount));
            statusContent.setSelectedItem(status);
            confirmedByContent.setText(confirmedBy);
        }
        
        formPanel.add(orderIdLabel);
        formPanel.add(orderIdContent);
        
        formPanel.add(customerIdLabel);
        formPanel.add(customerIdContent);
        
        formPanel.add(tourIdLabel);
        formPanel.add(tourIdContent);
        
        formPanel.add(adultTicketsLabel);
        formPanel.add(adultTicketsContent);
        
        formPanel.add(childTicketsLabel);
        formPanel.add(childTicketsContent);
        
        formPanel.add(orderTimeLabel);
        formPanel.add(orderTimeContent);
        
        formPanel.add(totalAmountLabel);
        formPanel.add(totalAmountContent);
        
        formPanel.add(statusLabel);
        formPanel.add(statusContent);

        formPanel.add(confirmedByLabel);
        formPanel.add(confirmedByContent);
        
        
        if(isConfirmed)
        {
        	confirmButton = new JButton("Xác nhận");
        	confirmButton.addActionListener(e->
        	{
        		order.setStatus(Order.STATUS_PAID);
        		order.setConfirmedBy(employee.getEmployeeId());
        		if(orderDAO.update(order))
            	{
                    JOptionPane.showMessageDialog(null, "Xác nhận đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            	}
            	else JOptionPane.showMessageDialog(null, "Xác nhận đơn hàng không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         	
            	
            	enableEditing(false);
            	formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving            	
        	});
        	
            cancelButton = new JButton("Hủy");        
            cancelButton.addActionListener(e->
            {
            	order.setStatus(Order.STATUS_CANCELLED);
            	order.setConfirmedBy(employee.getEmployeeId());
            	
        		if(orderDAO.update(order))
            	{
                    JOptionPane.showMessageDialog(null, "Hủy đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            	}
            	else JOptionPane.showMessageDialog(null, "Hủy đơn hàng không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         	
            	
            	enableEditing(false);
            	formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving
            });
            
            formPanel.add(confirmButton);
            formPanel.add(cancelButton);
        }
        else
        {
            saveButton = new JButton("Lưu");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                	order.setCustomerId(customerId);
                	order.setTourId(tourId);
                	order.setAdultTickets(adultTickets);
                	order.setChildTickets(childTickets);
                	order.setOrderTime(orderTime);
                	order.setTotalAmount(totalAmount);
                	order.setStatus(status);
//                	order.setConfirmedBy(confirmedBy);

                	
                	if(orderDAO.update(order))
                	{
                        JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                	}
                	else JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
             	
                	
                	enableEditing(false);
                	formPanel.revalidate();
         		    formPanel.repaint(); // Close the dialog after saving
                }
            });
            
            cancelButton = new JButton("Hủy");        

            // Action for the Cancel button
            cancelButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                	 orderId = order.getOrderId();
                     customerId = order.getCustomerId();
                     tourId = order.getTourId();
                     adultTickets = order.getAdultTickets();
                     childTickets = order.getChildTickets();
                     orderTime = order.getOrderTime();
                     totalAmount = order.getTotalAmount();
                     status = order.getStatus();
                     confirmedBy = order.getConfirmedBy();

            		
                	orderIdContent.setText(orderId);
                    customerIdContent.setText(customerId);
                    tourIdContent.setText(tourId);
                    adultTicketsContent.setText(String.valueOf(adultTickets));
                    childTicketsContent.setText(String.valueOf(childTickets));
                    orderTimeContent.setText(orderTime.toString());
                    totalAmountContent.setText(String.format("%.2f", totalAmount));
                    statusContent.setSelectedItem(status);
                    confirmedByContent.setText(confirmedBy);
                    
    		        
                    enableEditing(false);
            		
            		formPanel.revalidate();
         		    formPanel.repaint(); // Close the dialog after saving
                }
            });
            
            formPanel.add(saveButton);
            formPanel.add(cancelButton);
        }
        

        // Add buttons to the dialog

        
        enableEditing(false);

        
        add(formPanel, BorderLayout.CENTER);
        add(functionButton, BorderLayout.NORTH);
    }

    private void enableEditing(boolean isEnabled) 
    {
        orderIdContent.setEnabled(isEnabled);
        customerIdContent.setEnabled(isEnabled);
        tourIdContent.setEnabled(isEnabled);
        adultTicketsContent.setEnabled(isEnabled);
        childTicketsContent.setEnabled(isEnabled);
        orderTimeContent.setEnabled(isEnabled);
        totalAmountContent.setEnabled(isEnabled);
        statusContent.setEnabled(isEnabled);
        confirmedByContent.setEnabled(isEnabled);
        if(!isConfirmed) saveButton.setEnabled(isEnabled);
        if(!isConfirmed) cancelButton.setEnabled(isEnabled);
        
    }

    private void showInvalidInputMessage(JTextField field) {
        JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        field.setText("");
    }
}
