package gui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import entity.Customer;
import entity.Employee;
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
    private JPanel listChangingPanel;
    private JPanel functionPanel;

    private JTextField searchTextField;

    private JLabel pageNumber;
    private JLabel orderListLabel;
    private JLabel orderListConfirmLabel;

    private Order_DAO orderDAO = new Order_DAO();

    private ArrayList<Order> orders = new ArrayList<>();
    
    private Employee employee;

    public OrderManagement(Employee employee) 
    {
    	this.employee = employee;
    	
        setLayout(new BorderLayout());
        
        listChangingPanel = new JPanel();
        listChangingPanel.setBackground(new Color(66, 165, 243));

        
        MouseListener listChanger = new OrderListChanger();

        orderListLabel = new JLabel("Danh sách đơn hàng");
        orderListLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderListLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        orderListLabel.addMouseListener(listChanger);
        
        orderListConfirmLabel = new JLabel("Danh sách đơn hàng chưa xác nhận");
        orderListConfirmLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderListConfirmLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        orderListConfirmLabel.addMouseListener(listChanger);
        
        listChangingPanel.add(orderListLabel);
        listChangingPanel.add(orderListConfirmLabel);
        
        orderListPanel = new JPanel();
        orderListPanel.setLayout(new GridLayout(10, 1));

        pageControlButtonPanel = new JPanel();
        pageControlButtonPanel.setBackground(Color.PINK);
        pageControlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        functionButtonPanel = new JPanel();
        functionButtonPanel.setBackground(new Color(66, 165, 243));

        pageNumber = new JLabel("Page: " + currentPage);

        JButton addOrder = new JButton("Thêm Đơn Hàng");
        AddOrderFrame actionListenerAdd = new AddOrderFrame();
        addOrder.addActionListener(actionListenerAdd);

        JButton previousPage = new JButton("<");
        previousPage.addActionListener(evt -> previousPagePanel(evt));

        JButton nextPage = new JButton(">");
        nextPage.addActionListener(evt -> nextPagePanel(evt));

        searchTextField = new JTextField("Nhập mã đơn hàng để tìm");
        searchTextField.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		searchTextField.setText("");
        	}
        });
        searchTextField.addActionListener(evt -> 
        {
            String tempOrderId = searchTextField.getText();
            if (orderDAO.checkExistById(tempOrderId)) 
            {
                orderListPanel.removeAll();
                JPanel row = CreateOrderRow(orderDAO.getById(tempOrderId), 0 ,false);
                orderListPanel.add(row);
                orderListPanel.revalidate();
                orderListPanel.repaint();
            }
            else if (tempOrderId.equals("")) 
            {
                loadOrderData(orderDAO.getAll(), false);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng nào có mã " + tempOrderId,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        functionButtonPanel.add(searchTextField);
        functionButtonPanel.add(addOrder);
        
        functionPanel= new JPanel();
        functionPanel.setLayout(new BorderLayout());
        functionPanel.setBackground(new Color(66, 165, 243));

        
        functionPanel.add(functionButtonPanel, BorderLayout.WEST);
        functionPanel.add(listChangingPanel, BorderLayout.EAST);
        

        pageControlButtonPanel.add(previousPage);
        pageControlButtonPanel.add(pageNumber);
        pageControlButtonPanel.add(nextPage);

        JScrollPane scrollBar = new JScrollPane(orderListPanel);

        add(functionPanel, BorderLayout.NORTH);
        add(scrollBar, BorderLayout.CENTER);
        add(pageControlButtonPanel, BorderLayout.SOUTH);
        
        loadOrderData(orderDAO.getAll(), false);
    }

    // Hiện thông tin đơn hàng
    private void loadOrderData(ArrayList<Order> orders, boolean isConfirmed) 
    {
        // Lấy danh sách đơn hàng
    	this.orders = orders;
        totalPages = (int) Math.ceil((double) orders.size() / rowPerPage);
        updatePage(isConfirmed); // Chỉ tải dữ liệu của trang đầu tiên
    }

    // Cập nhật UI ở trang hiện tại
    private void updatePage(boolean isConfirmed) 
    {
        orderListPanel.removeAll();

        int start = (currentPage - 1) * rowPerPage;                // Vị trí đơn hàng đầu tiên trong trang
        int end = Math.min(start + rowPerPage, orders.size());     // Vị trí đơn hàng cuối cùng trong trang

        for (int i = start; i < end; i++) 
        {
            JPanel row = CreateOrderRow(orders.get(i), i - start, isConfirmed);

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
    private JPanel CreateOrderRow(Order order, int indexInPage, boolean isConfirmed) 
    {
        JPanel row = new JPanel(new GridLayout(2, 3)); // 4 cột: orderId, customerId, totalAmount, orderTime
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel orderIdLabel = new JLabel("Mã đơn hàng: "+order.getOrderId());
        JLabel customerIdLabel = new JLabel("Mã khách hàng: "+order.getCustomerId());
        JLabel tourIdLabel = new JLabel("Mã Tour: "+ order.getTourId());
        JLabel totalAmountLabel = new JLabel("Tổng số tiền: "+String.valueOf(order.getTotalAmount()));
        JLabel orderTimeLabel = new JLabel("Thời gian đặt hàng: "+order.getOrderTime().toString());
        JLabel statusLabel = new JLabel("Trạng thái: " + order.getStatus());
        

        // Căn chỉnh văn bản
        orderIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        customerIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tourIdLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        row.add(orderIdLabel);
        row.add(tourIdLabel);
        row.add(orderTimeLabel);
        row.add(customerIdLabel);
        row.add(totalAmountLabel);
        row.add(statusLabel);
        
        OrderDetailControl mouseListener =null;

        if(isConfirmed) 
        	{
        		mouseListener = new OrderDetailControl(this, true);
        	}
        else
        {
    		 mouseListener = new OrderDetailControl(this, false);
        }

        row.addMouseListener(mouseListener);
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.putClientProperty("order", order);

        return row;
    }

    // Tạo event chuyển trang trước
    private void previousPagePanel(ActionEvent e) 
    {
        if (currentPage > 1)
        {
            currentPage--;
            updatePage(false);
        }
    }

    // Tạo event chuyển trang sau
    private void nextPagePanel(ActionEvent e) 
    {
        if (currentPage < totalPages) 
        {
            currentPage++;
            updatePage(false);
        }
    }

    private class OrderDetailControl extends MouseAdapter 
    {
        private OrderManagement orderManagement;
        private boolean isConfirmed;

        public OrderDetailControl(OrderManagement orderManagement, boolean isConfirmed) 
        {
            this.orderManagement = orderManagement;
            this.isConfirmed =isConfirmed;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel sourcePanel = (JPanel) e.getSource();
            Order order = (Order) sourcePanel.getClientProperty("order");

            orderManagement.removeAll();
            OrderDetail orderDetail = new OrderDetail(order, isConfirmed, employee);
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
    
    private class OrderListChanger extends MouseAdapter
    {
    	@Override
    	public void mouseClicked(MouseEvent e)
    	{
    		if(e.getSource()==orderListLabel)
    		{
    			loadOrderData(orderDAO.getAll(), false);
    		}
    		else if(e.getSource()==orderListConfirmLabel)
    		{
    			loadOrderData(orderDAO.getAllUnconfirmed(), true);
    		}
    	}
    }
    
    private class AddOrderFrame implements ActionListener 
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
            JFrame addFrame = new JFrame("Thêm đơn mới");
            addFrame.setAlwaysOnTop(true);
            addFrame.setLayout(new GridLayout(11, 2, 10, 10));
            

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
	                		//JOptionPane.showMessageDialog(addFrame, "Đã tìm được khách hàng tương ứng.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                		customerTemp = customerDAO.getByPhone(phoneTemp);
	                		if (customerTemp.getStatus().equals(Customer.STATUS_ACTIVE)) {
	                			fullName = customerTemp.getFullName();
		                	    phone = customerTemp.getPhone();
		                	    email = customerTemp.getEmail();
		                	    address = customerTemp.getAddress();

		                	    fullNameContent.setText(fullName);
		                	    phoneContent.setText(phone);
		                	    emailContent.setText(email);
		                	    addressContent.setText(address);
	                		}	    	                	    
	                	}
	                	
	                	else
	                	{
	                		//int choice = JOptionPane.showConfirmDialog(addFrame, "Không tìm thấy khách hàng tương ứng với số điện thoại. Bạn có muốn tạo số điện thoại mới không", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	                		//if (choice == JOptionPane.YES_OPTION) {
	                			phone = phoneTemp;
			                	System.out.println("Phone: " + phone);
	                		//}
	                		//else {
	                			//phoneContent.setText("");
	                			//phone = "";
	                		//}
	                	}
	                	
	                }
	                else
	                	{
	                		System.out.println("Invalid input");
	                		//JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ! Định dạng đúng: 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        		        //phoneContent.requestFocus();
	                		phoneContent.setText("");
	                	}
	            }
	        });
			
			
			// Full name
            fullNameLabel = new JLabel("Họ tên:");
			fullNameContent = new JTextField();
			

			emailLabel = new JLabel("Email:");
			emailContent = new JTextField();

			
			// Address
			addressLabel = new JLabel("Địa chỉ:");
			addressContent = new JTextField();


            // Tour ID
            tourIdLabel = new JLabel("Mã tour:");
            tourIdContent = new JTextField();


            // Adult tickets
            adultTicketsLabel = new JLabel("Số vé người lớn:");
            adultTicketsContent = new JTextField();


            // Child tickets
            childTicketsLabel = new JLabel("Số vé trẻ em:");
            childTicketsContent = new JTextField();


            // Total Amount
            totalAmountLabel = new JLabel("Tổng tiền:");
            totalAmountContent = new JTextField();


            // Status
            statusLabel = new JLabel("Trạng thái:");
            statusContent = new JComboBox<>();
            statusContent.addItem("Chờ thanh toán");
            statusContent.addItem("Đã thanh toán");
            statusContent.addItem("Hủy");
            statusContent.addItem("Hoàn thành");


            // Confirmed by (Employee)
            confirmedByLabel = new JLabel("Nhân viên phụ trách:");
            confirmedByContent = new JTextField();
            confirmedByContent.setText(employee.getEmployeeId());
            confirmedByContent.setEnabled(false);

            // Add components to dialog  
            addFrame.add(phoneLabel);
            addFrame.add(phoneContent);
			
            addFrame.add(fullNameLabel);
            addFrame.add(fullNameContent);			

            addFrame.add(emailLabel);
            addFrame.add(emailContent);

            addFrame.add(addressLabel);
            addFrame.add(addressContent);
            
            addFrame.add(tourIdLabel);
            addFrame.add(tourIdContent);

            addFrame.add(adultTicketsLabel);
            addFrame.add(adultTicketsContent);

            addFrame.add(childTicketsLabel);
            addFrame.add(childTicketsContent);

            addFrame.add(totalAmountLabel);
            addFrame.add(totalAmountContent);

            addFrame.add(statusLabel);
            addFrame.add(statusContent);

            addFrame.add(confirmedByLabel);
            addFrame.add(confirmedByContent);

            // Save and Cancel buttons
            JButton saveButton = new JButton("Lưu");
            JButton cancelButton = new JButton("Hủy");

            // Save button action
            saveButton.addActionListener(e1 -> 
            {
                if (!validateInput(addFrame)) {
                	return;
                }
                fullName = fullNameContent.getText().trim();
                phone = phoneContent.getText().trim();
                email = emailContent.getText().trim();
                address = addressContent.getText().trim();
                tourId = tourIdContent.getText().trim();
                adultTickets = Integer.parseInt(adultTicketsContent.getText().trim());
                childTickets = Integer.parseInt(childTicketsContent.getText().trim());
                totalAmount = Double.parseDouble(totalAmountContent.getText().trim());
                status = (String) statusContent.getSelectedItem();
                confirmedBy = confirmedByContent.getText().trim();
                // Save order to database or list
            	Customer customer = null;
            	        
            	if(customerTemp == null)
            	{
            		customer = new Customer(customerDAO.generateNextCustomerId(),  fullName,  phone,  email,  address, Customer.STATUS_INACTIVE, phone, "1");
            		customerDAO.add(customer);
            		System.out.println(customer);
            	}
            	else customer = customerTemp;
            	
            	try {
            		Order order = new Order(orderDAO.generateNextOrderId(), customer.getCustomerId(),  tourId,  adultTickets,  childTickets, totalAmount,  status,  confirmedBy);
            		System.out.println(order);
            		if (orderDAO.add(order)) {
            			JOptionPane.showMessageDialog(addFrame, "Thêm đơn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        			loadOrderData(orderDAO.getAll(), false);
	        			addFrame.dispose();
	        			
	        		} else {
	        			JOptionPane.showMessageDialog(addFrame, "Không thể thêm đơn!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            		}
            	} catch (Exception ex){
            		JOptionPane.showMessageDialog(addFrame, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            	}
            	
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
                addFrame.dispose();
            });

            // Add buttons to dialog
            addFrame.add(saveButton);
            addFrame.add(cancelButton);

            // Set dialog properties
            addFrame.setSize(600, 600);
            addFrame.setLocationRelativeTo(null);
            addFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addFrame.setVisible(true);
        }
        
    	// Kiểm tra định dạng các thông tin
		private boolean validateInput(JFrame parentFrame) 
		{
			String fullNameTemp = fullNameContent.getText().trim();
			//String phoneTemp = phoneContent.getText().trim();
			String emailTemp = emailContent.getText().trim();
			String addressTemp = addressContent.getText().trim();			
			String tourIdTemp = tourIdContent.getText().trim();
			String adultTicketsTemp = adultTicketsContent.getText().trim();
			String childTicketsTemp = childTicketsContent.getText().trim();
			String totalAmountTemp = totalAmountContent.getText().trim();
			
			
			if (fullNameTemp.isEmpty() || !fullNameTemp.matches("^\\p{L}+(\\s+\\p{L}+)*$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Họ tên không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        fullNameContent.requestFocus();
		        return false;
		    }

//		    if (phoneTemp.isEmpty() || !phoneTemp.matches("\\d{10}")) {
//		        JOptionPane.showMessageDialog(parentFrame, "Số điện thoại không hợp lệ! Định dạng đúng: 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
//		        phoneContent.requestFocus();
//		        return false;
//		    }

		    if (emailTemp.isEmpty() || !emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Email không hợp lệ! Vui lòng nhập đúng định dạng email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        emailContent.requestFocus();
		        return false;
		    }

		    if (addressTemp.isEmpty() || !addressTemp.matches("^[a-zA-ZÀ-ỹ0-9 .,-/]+$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Địa chỉ không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        addressContent.requestFocus();
		        return false;
		    }

		    if (tourIdTemp.isEmpty() || !tourIdTemp.matches("TOUR\\d{3}")) {
		        JOptionPane.showMessageDialog(parentFrame, "Mã tour phải đúng định dạng : TOURXXX", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        tourIdContent.requestFocus();
		        return false;
		    }

		    if (adultTicketsTemp.isEmpty() || !adultTicketsTemp.matches("\\d+")) {
		        JOptionPane.showMessageDialog(parentFrame, "Số vé người lớn phải là chữ số và không được nhỏ hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        adultTicketsContent.requestFocus();
		        return false;
		    }
		    
		    if (childTicketsTemp.isEmpty() || !childTicketsTemp.matches("\\d+")) {
		        JOptionPane.showMessageDialog(parentFrame, "Số vé trẻ em phải là chữ số và không được nhỏ hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        childTicketsContent.requestFocus();
		        return false;
		    }
		    
		    if (totalAmountTemp.isEmpty() || !totalAmountTemp.matches("\\d+")) {
		        JOptionPane.showMessageDialog(parentFrame, "Tổng giá trị của đơn không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        totalAmountContent.requestFocus();
		        return false;
		    }

		    // Tất cả dữ liệu hợp lệ
		    return true;
		}
    }

}
