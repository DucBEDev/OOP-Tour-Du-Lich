package gui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import entity.Customer;
import entity.Order;
import dao.Order_DAO;
import dao.Customer_DAO;

public class OrderManagement extends JPanel {
    private int totalPages = 1;
    private int currentPage = 1;
    private int rowPerPage = 10;

    private JPanel orderListPanel;
    private JPanel functionButtonPanel;
    private JPanel pageControlButtonPanel;

    private JTextField searchTextField;

    private JLabel pageNumber;

    private Order_DAO orderDAO = new Order_DAO();

    private ArrayList<Order> orders = new ArrayList<>();

    public OrderManagement() 
    {
        setLayout(new BorderLayout(10, 10));

        orderListPanel = new JPanel();
        orderListPanel.setLayout(new GridLayout(10, 1));

        pageControlButtonPanel = new JPanel();
        pageControlButtonPanel.setBackground(Color.PINK);
        pageControlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        functionButtonPanel = new JPanel();
        functionButtonPanel.setBackground(Color.CYAN);
        functionButtonPanel.setLayout(new BorderLayout());

        pageNumber = new JLabel("Page: " + currentPage);

        JButton addOrder = new JButton("Thêm Đơn Hàng");
        AddOrderDialog actionListenerAdd = new AddOrderDialog();
        addOrder.addActionListener(actionListenerAdd);

        JButton previousPage = new JButton("<");
        previousPage.addActionListener(evt -> PreviousPagePanel(evt));

        JButton nextPage = new JButton(">");
        nextPage.addActionListener(evt -> NextPagePanel(evt));

        searchTextField = new JTextField("Nhập mã đơn hàng để tìm");
        searchTextField.addActionListener(evt -> 
        {
            String tempOrderId = searchTextField.getText();
            if (orderDAO.checkExistById(tempOrderId)) 
            {
                orderListPanel.removeAll();
                JPanel row = CreateOrderRow(orderDAO.getById(tempOrderId), 0);
                orderListPanel.add(row);
                orderListPanel.revalidate();
                orderListPanel.repaint();
            }
            else if (tempOrderId.equals("")) 
            {
                LoadOrderData();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng nào có mã " + tempOrderId,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        functionButtonPanel.add(addOrder, BorderLayout.EAST);
        functionButtonPanel.add(searchTextField, BorderLayout.WEST);

        pageControlButtonPanel.add(previousPage);
        pageControlButtonPanel.add(pageNumber);
        pageControlButtonPanel.add(nextPage);

        JScrollPane scrollBar = new JScrollPane(orderListPanel);

        add(functionButtonPanel, BorderLayout.NORTH);
        add(scrollBar, BorderLayout.CENTER);
        add(pageControlButtonPanel, BorderLayout.SOUTH);

        LoadOrderData();
    }

    // Hiện thông tin đơn hàng
    private void LoadOrderData() 
    {
        // Lấy danh sách đơn hàng
        orders = orderDAO.getAll();
        totalPages = (int) Math.ceil((double) orders.size() / rowPerPage);
        UpdatePage(); // Chỉ tải dữ liệu của trang đầu tiên
    }

    // Cập nhật UI ở trang hiện tại
    private void UpdatePage() 
    {
        orderListPanel.removeAll();

        int start = (currentPage - 1) * rowPerPage;                // Vị trí đơn hàng đầu tiên trong trang
        int end = Math.min(start + rowPerPage, orders.size());     // Vị trí đơn hàng cuối cùng trong trang

        for (int i = start; i < end; i++) 
        {
            JPanel row = CreateOrderRow(orders.get(i), i - start);

            orderListPanel.add(row);
        }

        // Thêm các dòng trống nếu trang không đầy
        for (int i = end - start; i < rowPerPage; i++) 
        {
            JPanel emptyRow = new JPanel();
            emptyRow.setBackground(Color.WHITE);
            orderListPanel.add(emptyRow);
        }

        pageNumber.setText("Page: " + currentPage);
        orderListPanel.revalidate();
        orderListPanel.repaint();
    }

    // Tạo dòng hiển thị thông tin trên Page
    private JPanel CreateOrderRow(Order order, int indexInPage) 
    {
        JPanel row = new JPanel(new GridLayout(2, 4)); // 4 cột: orderId, customerId, totalAmount, orderTime
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel orderIdLabel = new JLabel(order.getOrderId());
        JLabel customerIdLabel = new JLabel(order.getCustomerId());
        JLabel totalAmountLabel = new JLabel(String.valueOf(order.getTotalAmount()));
        JLabel orderTimeLabel = new JLabel(order.getOrderTime().toString());

        // Căn chỉnh văn bản
        orderIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        customerIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        row.add(orderIdLabel);
        row.add(customerIdLabel);
        row.add(totalAmountLabel);
        row.add(orderTimeLabel);

        OrderDetailControl mouseListener = new OrderDetailControl(this);
        row.addMouseListener(mouseListener);
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.putClientProperty("order", order);

        return row;
    }

    // Tạo event chuyển trang trước
    private void PreviousPagePanel(ActionEvent e) 
    {
        if (currentPage > 1)
        {
            currentPage--;
            UpdatePage();
        }
    }

    // Tạo event chuyển trang sau
    private void NextPagePanel(ActionEvent e) 
    {
        if (currentPage < totalPages) 
        {
            currentPage++;
            UpdatePage();
        }
    }

    private class OrderDetailControl extends MouseAdapter 
    {
        private OrderManagement orderManagement;

        public OrderDetailControl(OrderManagement orderManagement) 
        {
            this.orderManagement = orderManagement;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel sourcePanel = (JPanel) e.getSource();
            Order order = (Order) sourcePanel.getClientProperty("order");

            orderManagement.removeAll();
            OrderDetail orderDetail = new OrderDetail(order);
            orderManagement.add(orderDetail);
            orderManagement.revalidate();
            orderManagement.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            JPanel tempPanel = (JPanel) e.getSource();
            tempPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            JPanel tempPanel = (JPanel) e.getSource();
            tempPanel.setBackground(Color.WHITE);
        }
    }
    
    private class AddOrderDialog implements ActionListener 
    {

    	private JLabel fullNameLabel;
		private JLabel phoneLabel;
		private JLabel emailLabel;
		private JLabel addressLabel;
        private JLabel tourIdLabel;
        private JLabel adultTicketsLabel;
        private JLabel childTicketsLabel;
        private JLabel totalAmountLabel;
        private JLabel statusLabel;
        private JLabel confirmedByLabel;

        private JTextField fullNameContent;
		private JTextField phoneContent;
		private JTextField emailContent;
		private JTextField addressContent;
        private JTextField tourIdContent;
        private JTextField adultTicketsContent;
        private JTextField childTicketsContent;
        private JTextField totalAmountContent;
        private JComboBox<String> statusContent;
        private JTextField confirmedByContent;

        private String fullName;
		private String phone;
		private String email;
		private String address;
        private String tourId;
        private int adultTickets;
        private int childTickets;
        private double totalAmount;
        private String status;
        private String confirmedBy;
        
        private Customer customerTemp;
        
        private Customer_DAO customerDAO= new Customer_DAO();
        
        private Order_DAO orderDAO= new Order_DAO();

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog addDialog = new JDialog();
            addDialog.setAlwaysOnTop(true);
            addDialog.setLayout(new GridLayout(11, 2, 10, 10));
            

            phoneLabel = new JLabel("Số điện thoại:");
			phoneContent = new JTextField();
			phoneContent.addActionListener(new ActionListener() 
			{
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	                String phoneTemp = phoneContent.getText();
	                if(phoneTemp.matches("\\d{10}")) 
	                {
	                	if(customerDAO.checkExistByPhone(phoneTemp))
	                	{
	                		customerTemp = customerDAO.getByPhone(phoneTemp);
	                		
	                		fullName = customerTemp.getFullName();
	                	    phone = customerTemp.getPhone();
	                	    email = customerTemp.getEmail();
	                	    address = customerTemp.getAddress();

	                	    fullNameContent.setText(fullName);
	                	    phoneContent.setText(phone);
	                	    emailContent.setText(email);
	                	    addressContent.setText(address);
	                	    	                	    
	                	}
	                	else
	                	{
	                		phone = phoneTemp;
		                	System.out.println("Phone: " + phone);
	                	}
	                	
	                }
	                else
	                	{
	                		System.out.println("Invalid input");
	                		phoneContent.setText("");
	                	}
	            }
	        });
			
			
            fullNameLabel = new JLabel("Họ tên:");
			fullNameContent = new JTextField();
			fullNameContent.addActionListener(new ActionListener() 
			{
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	                String fullNameTemp = fullNameContent.getText();
	                if(fullNameTemp.matches("^[A-Za-z ]+$")) 
	                {
	                	fullName = fullNameTemp;
	                	System.out.println("Full Name: " + fullName);
	                }
	                else
	                	{
	                		System.out.println("Invalid input");
	                		fullNameContent.setText("");
	                	}
	            }
	        });
			

			emailLabel = new JLabel("Email:");
			emailContent = new JTextField();
			emailContent.addActionListener(new ActionListener() 
			{
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	                String emailTemp = emailContent.getText();
	                if(emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) 
	                {
	                	email = emailTemp;
	                	System.out.println("Email: " + email);
	                }
	                else
	                	{
	                		System.out.println("Invalid input");
	                		emailContent.setText("");
	                	}
	            }
	        });


			addressLabel = new JLabel("Địa chỉ:");
			addressContent = new JTextField();
			addressContent.addActionListener(new ActionListener() 
			{
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	                String adressTemp = addressContent.getText();
	                if(adressTemp.matches("^[A-Za-z0-9 ]+$")) 
	                {
	                	address = adressTemp;
	                	System.out.println("Address: " + address);
	                }
	                else
	                	{
	                		System.out.println("Invalid input");
	                		addressContent.setText("");
	                	}
	            }
	        });


            // Tour ID
            tourIdLabel = new JLabel("Mã tour:");
            tourIdContent = new JTextField();
            tourIdContent.addActionListener(e1 -> {
                String temp = tourIdContent.getText();
                if (temp.matches("^[A-Za-z0-9]+$")) {
                    tourId = temp;
                    System.out.println("Tour ID: " + tourId);
                } else {
                    System.out.println("Invalid input for Tour ID");
                    tourIdContent.setText("");
                }
            });

            // Adult tickets
            adultTicketsLabel = new JLabel("Số vé người lớn:");
            adultTicketsContent = new JTextField();
            adultTicketsContent.addActionListener(e1 -> {
                String temp = adultTicketsContent.getText();
                if (temp.matches("\\d+")) {
                    adultTickets = Integer.parseInt(temp);
                    System.out.println("Adult Tickets: " + adultTickets);
                } else {
                    System.out.println("Invalid input for Adult Tickets");
                    adultTicketsContent.setText("");
                }
            });

            // Child tickets
            childTicketsLabel = new JLabel("Số vé trẻ em:");
            childTicketsContent = new JTextField();
            childTicketsContent.addActionListener(e1 -> {
                String temp = childTicketsContent.getText();
                if (temp.matches("\\d+")) {
                    childTickets = Integer.parseInt(temp);
                    System.out.println("Child Tickets: " + childTickets);
                } else {
                    System.out.println("Invalid input for Child Tickets");
                    childTicketsContent.setText("");
                }
            });


            // Total Amount
            totalAmountLabel = new JLabel("Tổng tiền:");
            totalAmountContent = new JTextField();
            totalAmountContent.addActionListener(e1 -> {
                String temp = totalAmountContent.getText();
                if (temp.matches("\\d+")) {
                    totalAmount = Double.parseDouble(temp);
                    System.out.println("Total Amount: " + totalAmount);
                } else {
                    System.out.println("Invalid input for Total Amount");
                    totalAmountContent.setText("");
                }
            });

            // Status
            statusLabel = new JLabel("Trạng thái:");
            statusContent = new JComboBox<>();
            statusContent.addItem("Đã thanh toán");
            statusContent.addItem("Chờ thanh toán");
            statusContent.addItem("Hủy");
            statusContent.addItem("Hoàn thành");
            statusContent.addActionListener(e1 -> {
                status = (String) statusContent.getSelectedItem();
                System.out.println("Status: " + status);
            });

            // Confirmed by (Employee)
            confirmedByLabel = new JLabel("Nhân viên phụ trách:");
            confirmedByContent = new JTextField();
            confirmedByContent.addActionListener(e1 -> 
            {
                confirmedBy = confirmedByContent.getText();
                System.out.println("Confirmed By: " + confirmedBy);
            });

            // Add components to dialog  
            addDialog.add(phoneLabel);
			addDialog.add(phoneContent);
			
            addDialog.add(fullNameLabel);
			addDialog.add(fullNameContent);			

			addDialog.add(emailLabel);
			addDialog.add(emailContent);

			addDialog.add(addressLabel);
			addDialog.add(addressContent);
            
            addDialog.add(tourIdLabel);
            addDialog.add(tourIdContent);

            addDialog.add(adultTicketsLabel);
            addDialog.add(adultTicketsContent);

            addDialog.add(childTicketsLabel);
            addDialog.add(childTicketsContent);

            addDialog.add(totalAmountLabel);
            addDialog.add(totalAmountContent);

            addDialog.add(statusLabel);
            addDialog.add(statusContent);

            addDialog.add(confirmedByLabel);
            addDialog.add(confirmedByContent);

            // Save and Cancel buttons
            JButton saveButton = new JButton("Lưu");
            JButton cancelButton = new JButton("Hủy");

            // Save button action
            saveButton.addActionListener(e1 -> 
            {
                
                // Save order to database or list
            	Customer customer = null;
            	        
            	if(customerTemp==null)
            	{
            		customer = new Customer(customerDAO.generateNextCustomerId(),  fullName,  phone,  email,  address, Customer.STATUS_INACTIVE, phone, "1");
            		customerDAO.add(customer);
            		System.out.println(customer);
            	}
            	else customer = customerTemp;
            	
            	Order order = new Order(orderDAO.generateNextOrderId(), customer.getCustomerId(),  tourId,  adultTickets,  childTickets, totalAmount,  status,  confirmedBy);
            	System.out.println(order);
            	orderDAO.add(order);
            	
                JOptionPane.showMessageDialog(addDialog, "Thêm đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                addDialog.dispose();
            });

            // Cancel button action
            cancelButton.addActionListener(e1 -> 
            {
            	customerTemp =null;
            	fullName= null;
        		phone =null;
        		email= null;
        		address=null;
                tourId = null;
                adultTickets = 0;
                childTickets = 0;
                totalAmount = 0;
                status = null;
                confirmedBy = null;
                addDialog.dispose();
            });

            // Add buttons to dialog
            addDialog.add(saveButton);
            addDialog.add(cancelButton);

            // Set dialog properties
            addDialog.setSize(400, 600);
            addDialog.setLocationRelativeTo(null);
            addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addDialog.setVisible(true);
        }
    }

}
