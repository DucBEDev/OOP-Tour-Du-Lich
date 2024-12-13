package gui.admin;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.swing.*;

import dao.Customer_DAO;
import dao.Employee_DAO;
import dao.Order_DAO;
import dao.Tour_DAO;
import entity.Employee;
import entity.Order;
import entity.Tour;

public class OrderDetail extends JPanel {
    private JPanel functionButton;
    private JPanel formPanel;
    private JPanel formButtonPanel;

    private JLabel backButton;
    private JLabel editButton;

    private JButton saveButton;
    private JButton cancelButton;
    private JButton confirmButton;
    private JButton totalAmountButton;
    
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
    private int currentParticipants;
    
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
        orderIdContent.setEnabled(false);

        customerIdLabel = new JLabel("Mã khách hàng:");
        customerIdContent = new JTextField();
        customerIdContent.setEnabled(false);       

        tourIdLabel = new JLabel("Mã tour:");
        tourIdContent = new JTextField();
        tourIdContent.setEnabled(false);

        adultTicketsLabel = new JLabel("Vé người lớn:");
        adultTicketsContent = new JTextField();      

        childTicketsLabel = new JLabel("Vé trẻ em:");
        childTicketsContent = new JTextField();      

        orderTimeLabel = new JLabel("Thời gian đặt:");
        orderTimeContent = new JTextField();
        orderTimeContent.setEnabled(false);        

        totalAmountLabel = new JLabel("Tổng tiền:");
        totalAmountContent = new JTextField();
        totalAmountContent.setEnabled(false);       

        statusLabel = new JLabel("Trạng thái:");
        statusContent = new JComboBox<>(new String[]{"Đã thanh toán", "Chờ thanh toán", "Hủy", "Hoàn thành"});
        statusContent.addActionListener(e -> status = statusContent.getSelectedItem().toString());
        

        confirmedByLabel = new JLabel("Xác nhận bởi:");
        confirmedByContent = new JTextField();
        confirmedByContent.setEnabled(false);

        
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
            DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
            String formatted = decimalFormat.format(totalAmount);
            totalAmountContent.setText(formatted);
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
        
        
        formButtonPanel = new JPanel();
        
        
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
     		    formPanel.repaint();           	
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
            
            formButtonPanel.add(confirmButton);
            formButtonPanel.add(cancelButton);
        }
        
        else
        {         
            saveButton = new JButton("Lưu");
            saveButton.addActionListener(new ActionListener()
            {
            	int adultTicketsTemp = 0;
            	int childTicketsTemp = 0;
                String statusTemp = null;
                double totalAmountTemp = 0;

                @Override
                public void actionPerformed(ActionEvent e) 
                {
                	if (!validateInput()) return;
                	else
                	{
                		adultTicketsTemp = Integer.parseInt(adultTicketsContent.getText().trim());
                        childTicketsTemp = Integer.parseInt(childTicketsContent.getText().trim());
                        statusTemp = (String) statusContent.getSelectedItem();
                        totalAmountTemp = Double.parseDouble(totalAmountContent.getText().trim().replace(",", ""));
                	}
                	
                	
                	if (currentParticipants == orderDAO.getMaxParticipants(tourId)) 
                    {
                    	tourDAO.updateStatus(tourId, true);
                    }
                	else if(currentParticipants < orderDAO.getMaxParticipants(tourId))
                	{
                    	tourDAO.updateStatus(tourId, false);
                		tourDAO.updateCurrentParticipants(tourId, currentParticipants);
                	}
                	
                	
    	            
                	order.setAdultTickets(adultTicketsTemp);
                	order.setChildTickets(childTicketsTemp);
                	order.setTotalAmount(totalAmountTemp);
                	order.setStatus(statusTemp);

                	System.out.println(order);
                	if(orderDAO.update(order))
                	{
                        JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                	}
                	else JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
             	             	
                	enableEditing(false);
                	formPanel.revalidate();
         		    formPanel.repaint(); 
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
                    
                    DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
                    String formatted = decimalFormat.format(totalAmount);
                    totalAmountContent.setText(formatted);
                    
                    statusContent.setSelectedItem(status);
                    confirmedByContent.setText(confirmedBy);                   
    		        
                    enableEditing(false);
            		
            		formPanel.revalidate();
         		    formPanel.repaint(); 
                }
            });
               
            formButtonPanel.add(saveButton);
            formButtonPanel.add(cancelButton);
        }
        

        // Chức năng tính tổng số tiền
        totalAmountButton = new JButton("Tính tổng tiền");
        totalAmountButton.addActionListener(e->{
        	String adultTicketsTemp = adultTicketsContent.getText().trim();
            String childTicketsTemp = childTicketsContent.getText().trim();

        	if(!adultTicketsTemp.isEmpty() && !childTicketsTemp.isEmpty())
        	{
        		int adultTicketsIntTemp = Integer.parseInt(adultTicketsTemp);
        		int childTicketsIntTemp = Integer.parseInt(childTicketsTemp);
        		
        		Tour tourTemp = tourDAO.getByTourId(this.tourId);          
        		
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                
        		this.totalAmount = adultTicketsIntTemp*tourTemp.getAdultPrice() + childTicketsIntTemp*tourTemp.getChildPrice();
                String formatted = decimalFormat.format(this.totalAmount);

        		totalAmountContent.setText(String.valueOf(formatted));
        		totalAmountContent.repaint();
        		totalAmountContent.revalidate();
        	}
        });
        
        formButtonPanel.add(totalAmountButton);
        
        enableEditing(false);

        add(formButtonPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.CENTER);
        add(functionButton, BorderLayout.NORTH);
    }
    
    private boolean validateInput() {
        String adultTicketsTemp = adultTicketsContent.getText().trim();
        String childTicketsTemp = childTicketsContent.getText().trim();

        if (adultTicketsTemp.isEmpty() || !adultTicketsTemp.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Số vé người lớn không hợp lệ! Vui lòng nhập số dương.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            adultTicketsContent.requestFocus();
            return false;
        }

        if (childTicketsTemp.isEmpty() || !childTicketsTemp.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Số vé trẻ em không hợp lệ! Vui lòng nhập số không âm.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            childTicketsContent.requestFocus();
            return false;
        }
        
        if ((orderDAO.getCurrentParticipants(tourId) + Integer.parseInt(adultTicketsTemp) + Integer.parseInt(childTicketsTemp) - (adultTickets + childTickets)) > orderDAO.getMaxParticipants(tourId)) {
	    	JOptionPane.showMessageDialog(null, "Số vé hiện tại đã vượt quá tổng số vé của tour này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	    	return false;
	    }   
        else {
        	currentParticipants = orderDAO.getCurrentParticipants(tourId) + Integer.parseInt(adultTicketsTemp) + Integer.parseInt(childTicketsTemp) - (adultTickets + childTickets);
        }

        return true;
    }
    
    
    // Vô hiệu các textfield sau khi xong sự kiện
    private void enableEditing(boolean isEnabled) 
    {
        adultTicketsContent.setEnabled(isEnabled);
        childTicketsContent.setEnabled(isEnabled);
        statusContent.setEnabled(isEnabled);

        if(!isConfirmed) saveButton.setEnabled(isEnabled);
        if(!isConfirmed) cancelButton.setEnabled(isEnabled);
        
    }
}
