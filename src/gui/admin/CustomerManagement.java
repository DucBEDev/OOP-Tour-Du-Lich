package gui.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.Customer_DAO;
import entity.Customer;

public class CustomerManagement extends JPanel {
	private final Customer_DAO customerDAO;
	
	// Khởi tạo biến biểu hiện số trang
	private int totalPages = 1;
	private int currentPage = 1;
	private static int rowPerPage = 10;

	private ArrayList<Customer> customers;
	
	private Customer customer;
	
	// Tạo các biến components
	private JPanel buttonPanel;
	private JPanel customerPanel;
	
	private JDialog dialog;
	
	private JLabel customerIdLabel;
	private JLabel fullNameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;
	private JLabel addressLabel;
	private JLabel statusLabel;
	private JLabel userNameLabel;
	private JLabel createdAtLabel;
	private JLabel passwordLabel;
	private JLabel pageNumber;
	
	private JTextField customerIdTextField;
	private JTextField fullNameTextField;
	private JTextField phoneTextField;
	private JTextField emailTextField;
	private JTextField addressTextField;
	private JComboBox<String> statusBox;
	private JTextField userNameTextField;
	private JTextField createdAtTextField;
	private JPasswordField passwordTextField;
	
	private JButton addButton;
	private JButton updateButton;
	private JButton saveButton;
	private JButton cancelButton;
	private JButton previousPage;
	private JButton nextPage;
	
	
	// Khởi tạo các biến thành phần 
	private String customerId;
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private String status;
	private String userName;
	private LocalDate createdAt;
	private String password;

	
	public CustomerManagement() {
		customerDAO = new Customer_DAO();
		
		setLayout(new BorderLayout());
        initComponents();
        loadCustomerData();
	}
	
	
	// Tạo panel và gán components
	private void initComponents() {
		customerPanel = new JPanel(new GridLayout(rowPerPage, 1));
        add(new JScrollPane(customerPanel), BorderLayout.CENTER);
        
        // Design Button Panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
                
        previousPage = new JButton("<");
        pageNumber = new JLabel("Page: " + currentPage);
        nextPage = new JButton(">");
        addButton = new JButton("Thêm");
        
 
        buttonPanel.add(previousPage);
        buttonPanel.add(pageNumber);
        buttonPanel.add(nextPage);
        buttonPanel.add(addButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Gán sự kiện các button
        addButton.addActionListener(this::addCustomerDialog);
        
        previousPage.addActionListener(this::previousPagePanel);
        nextPage.addActionListener(this::nextPagePanel);
	}
	
	
	// Hiện thông tin khách hàng
	private void loadCustomerData() {
	    // Lấy danh sách khách hàng
		customers = customerDAO.getAll();
		totalPages = (int) Math.ceil((double) customers.size() / rowPerPage);
		updatePage(); // Chỉ tải dữ liệu của trang đầu tiên
	}
	
	
	// Cập nhật UI ở trang hiện tại
	private void updatePage() {
        customerPanel.removeAll();

        int start = (currentPage - 1) * rowPerPage;					// Vị trí khách hàng đầu tiên trong trang	
        int end = Math.min(start + rowPerPage, customers.size());	// Vị trí khách hàng cuối cùng trong trang	

        for (int i = start; i < end; i++) {
            JPanel row = createCustomerRow(customers.get(i), i - start);
            customerPanel.add(row);
        }

        // Thêm các dòng trống nếu trang không đầy
        for (int i = end - start; i < rowPerPage; i++) {
            JPanel emptyRow = new JPanel();
            emptyRow.setBackground(i % 2 == 0 ? Color.white : Color.gray);
            customerPanel.add(emptyRow);
        }

        pageNumber.setText("Page: " + currentPage);
        customerPanel.revalidate();
        customerPanel.repaint();
    }
	
	
	// Tạo dòng hiển thị thông tin trên Page
	private JPanel createCustomerRow(Customer customer, int indexInPage	) {
		JPanel row = new JPanel(new GridLayout(1, 4)); // 4 cột: customerId, fullName, phone, email
		row.setBackground(indexInPage % 2 == 0 ? Color.white : Color.gray);
		row.setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel idLabel = new JLabel(customer.getCustomerId());
		JLabel nameLabel = new JLabel(customer.getFullName());
		JLabel phoneLabel = new JLabel(customer.getPhone());
		JLabel emailLabel = new JLabel(customer.getEmail());

		// Căn chỉnh văn bản
		idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);

		row.add(idLabel);
		row.add(nameLabel);
		row.add(phoneLabel);
		row.add(emailLabel);
		    
		row.addMouseListener(new MouseAdapter() {
           public void mouseClicked (MouseEvent e) {
        	   showCustomerDetailsDialog(customer);
           }
        });

		return row;
	}
		
		
	// Sự kiện click hiện thông tin chi tiết của khách hàng
	private void showCustomerDetailsDialog(Customer customer) {
		this.customer = customer;
		
		JFrame infoFrame = new JFrame("Chi tiết khách hàng");
		infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		infoFrame.setLayout(new GridLayout(9, 2, 10, 10));
		infoFrame.setSize(600, 600);
        
        customerIdTextField = new JTextField(customer.getCustomerId()); // ID không thay đổi
        customerIdTextField.setEditable(false); // Khóa ID
        
        fullNameTextField = new JTextField(customer.getFullName());
        fullNameTextField.setEditable(false);
        
        phoneTextField = new JTextField(customer.getPhone());
        phoneTextField.setEditable(false);
        
        emailTextField = new JTextField(customer.getEmail());
        emailTextField.setEditable(false);
        
        addressTextField = new JTextField(customer.getAddress());
        addressTextField.setEditable(false);
        
        statusBox = new JComboBox<>(new String[]{Customer.STATUS_ACTIVE, Customer.STATUS_INACTIVE});
        statusBox.setEnabled(false);
        
        userNameTextField = new JTextField(customer.getUserName());
        userNameTextField.setEditable(false);
        
        passwordTextField = new JPasswordField(customer.getPassword());
        passwordTextField.setEditable(false);
        
        customerIdLabel = new JLabel("ID:");
        fullNameLabel = new JLabel("Họ tên:");
        phoneLabel = new JLabel("Số điện thoại:");
        emailLabel = new JLabel("Email:");
        addressLabel = new JLabel("Địa chỉ:");
        statusLabel = new JLabel("Trạng thái:");
        userNameLabel = new JLabel("Tên tài khoản:");
        passwordLabel = new JLabel("Mật khẩu:");
        
        
        // Add components to dialog
        infoFrame.add(customerIdLabel);
        infoFrame.add(customerIdTextField);
        
        infoFrame.add(fullNameLabel);
        infoFrame.add(fullNameTextField);
        
        infoFrame.add(phoneLabel);
        infoFrame.add(phoneTextField);
        
        infoFrame.add(emailLabel);
        infoFrame.add(emailTextField);
        
        infoFrame.add(addressLabel);
        infoFrame.add(addressTextField);
        
        infoFrame.add(statusLabel);
        infoFrame.add(statusBox);
        
        infoFrame.add(userNameLabel);
        infoFrame.add(userNameTextField);
        
        infoFrame.add(passwordLabel);
        infoFrame.add(passwordTextField);
	    
        updateButton = new JButton("Sửa");
        saveButton = new JButton("Lưu");
        saveButton.setEnabled(false);
        
	    infoFrame.add(updateButton);
	    infoFrame.add(saveButton);
	    
	    updateButton.addActionListener(this::updateCustomerInfo);
	    saveButton.addActionListener(this::saveCustomerInfo);
	    
	    infoFrame.setLocationRelativeTo(this);
	    infoFrame.setVisible(true);
}
	
	
	// Tạo event chuyển trang trước
	private void previousPagePanel(ActionEvent e) {
	    if (currentPage > 1) {
	        currentPage--;
	        updatePage();
	    }
	}
	
	
	// Tạo event chuyển trang sau
	private void nextPagePanel(ActionEvent e) {
	    if (currentPage < totalPages) {
	        currentPage++;
	        updatePage();
	    }
	}


	// Event add customer
	private void addCustomerDialog(ActionEvent e) {
		dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Khách Hàng", true);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setSize(400, 400);
        
        customerIdTextField = new JTextField();
        customerIdTextField.addActionListener(new ActionListener() 
        {
        	@Override
        	public void actionPerformed(ActionEvent e) 
        	{
        		String customerIdTemp = customerIdTextField.getText();
        		if (customerIdTemp.matches("CUS%03d")) 
        		{
        			customerId = customerIdTemp;
        		}
        		
        		else 
        			{	
        				JOptionPane.showInputDialog(this, "ID không hợp lệ!");
        				customerIdTextField.setText("");
        			}
        	}
        });
        
        
        fullNameTextField = new JTextField();
        fullNameTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String fullNameTemp = fullNameTextField.getText();
                if(fullNameTemp.matches("^[A-Za-z ]+$")) 
                {
                	fullName = fullNameTemp;
                	//System.out.println("Full Name: " + fullName);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Họ tên không hợp lệ!");
                		fullNameTextField.setText("");
                	}
            }
        });
        
        
        phoneTextField = new JTextField();
        phoneTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String phoneTemp = phoneTextField.getText();
                if(phoneTemp.matches("\\d{10}")) 
                {
                	phone = phoneTemp;
                	//System.out.println("Phone: " + phone);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Số điện thoại không hợp lệ!");
                		phoneTextField.setText("");
                	}
            }
        });
        
        
        emailTextField = new JTextField();
        emailTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String emailTemp = emailTextField.getText();
                if(emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) 
                {
                	email = emailTemp;
                	//System.out.println("Email: " + email);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Email không hợp lệ!");
                		emailTextField.setText("");
                	}
            }
        });
        
        
        addressTextField = new JTextField();
        addressTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String adressTemp = addressTextField.getText();
                if(adressTemp.matches("^[A-Za-z ]+$")) 
                {
                	address = adressTemp;
                	//System.out.println("Address: " + address);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Địa chỉ không hợp lệ!");
                		addressTextField.setText("");
                	}
            }
        });
        
        
        statusBox = new JComboBox<>(new String[]{Customer.STATUS_ACTIVE, Customer.STATUS_INACTIVE});
        
        
        userNameTextField = new JTextField();
        userNameTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String userNameContentTemp = userNameTextField.getText();
                if(userNameContentTemp.matches("^[A-Za-z0-9_.-]+$")) 
                {
                	userName = userNameContentTemp;
                	//System.out.println("Username: " + userName);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Tên tài khoản không hợp lệ!");
                		userNameTextField.setText("");
                	}
            }
        });
        
        
        passwordTextField = new JPasswordField();
        passwordTextField.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String passwordContentTemp = passwordTextField.getText();
                if(passwordContentTemp.matches("^[A-Za-z\\d@$!%*#?&]+$")) 
                {
                	password = passwordContentTemp;
                	//System.out.println("Password: " + password);
                }
                else
                	{
                		JOptionPane.showInputDialog(this, "Mật khẩu không hợp lệ!");
                		passwordTextField.setText("");
                	}
            }
        });
        
        
        customerIdLabel = new JLabel("ID:");
        fullNameLabel = new JLabel("Họ tên:");
        phoneLabel = new JLabel("Số điện thoại:");
        emailLabel = new JLabel("Email:");
        addressLabel = new JLabel("Địa chỉ:");
        statusLabel = new JLabel("Trạng thái:");
        userNameLabel = new JLabel("Tên tài khoản:");
        passwordLabel = new JLabel("Mật khẩu:");
        
        
        // Add components to dialog
        dialog.add(customerIdLabel);
        dialog.add(customerIdTextField);
        
        dialog.add(fullNameLabel);
        dialog.add(fullNameTextField);
        
        dialog.add(phoneLabel);
        dialog.add(phoneTextField);
        
        dialog.add(emailLabel);
        dialog.add(emailTextField);
        
        dialog.add(addressLabel);
        dialog.add(addressTextField);
        
        dialog.add(statusLabel);
        dialog.add(statusBox);
        
        dialog.add(userNameLabel);
        dialog.add(userNameTextField);
        
        dialog.add(passwordLabel);
        dialog.add(passwordTextField);
        
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        saveButton.addActionListener(ev -> {
        	customerId = customerIdTextField.getText();
        	fullName = fullNameTextField.getText();
        	phone = phoneTextField.getText();
        	email = emailTextField.getText();
        	address = addressTextField.getText();
        	status = (String) statusBox.getSelectedItem();
        	userName = userNameTextField.getText();
        	password= new String(passwordTextField.getPassword());
        	
        	
        	// Kiểm tra ID của khách hàng có trong DB hay chưa
        	if(customerDAO.checkExistById(customerId)) {
        		JOptionPane.showMessageDialog(this, "ID đã tồn tại, vui lòng nhập ID khác!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        		return;
        	}
        	// Nếu chưa có ID thì add customer
        	try {
        		Customer customer = new Customer(customerId, fullName, phone, email, address, status, userName, password);
        		if(customerDAO.add(customer)) {
        			JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        			loadCustomerData();
            		dialog.dispose();
        			
        		} else {
        			JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        		}
        	} catch (Exception ex) {
        		JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
	}
	
	
	// Event update customer
	private void updateCustomerInfo(ActionEvent e) {
		if (this.customer == null) {
	        JOptionPane.showMessageDialog(this, "Không thể sửa. Không tìm thấy thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
		
	    // Khi nhấn nút "Cập nhật", cho phép chỉnh sửa các trường thông tin
	    fullNameTextField.setEditable(true);
	    phoneTextField.setEditable(true);
	    emailTextField.setEditable(true);
	    addressTextField.setEditable(true);
	    statusBox.setEnabled(true);	    
	    userNameTextField.setEditable(true);
	    passwordTextField.setEditable(true);
	    	    
	    updateButton.setEnabled(false);
	    saveButton.setEnabled(true);
	}

	
	// Lưu và cập nhật thông tin mới của khách hàng, hiện trên Page và cập nhật trong DB
	private void saveCustomerInfo(ActionEvent e) {
	    if (this.customer == null) {
	        JOptionPane.showMessageDialog(this, "Không thể lưu do không tìm thấy thông tin khách hàng", "Thông báo", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    // Lấy dữ liệu từ các trường văn bản
	    customerId = customerIdTextField.getText();
	    fullName = fullNameTextField.getText();
	    phone = phoneTextField.getText();
	    email = emailTextField.getText();
	    address = addressTextField.getText();
	    status = (String) statusBox.getSelectedItem();
	    userName = userNameTextField.getText();
	    password = new String(passwordTextField.getPassword());

	    try {
	        // Tạo đối tượng Customer mới với thông tin đã cập nhật
	        Customer updatedCustomer = new Customer(customerId, fullName, phone, email, address, status, userName, password);
	        // Gọi phương thức update từ DAO
	        if (customerDAO.update(updatedCustomer)) {
	            // Cập nhật dữ liệu trên giao diện
	            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            

	            // Thay đổi thông tin trong danh sách khách hàng
	            for (int i = 0; i < customers.size(); i++) {
	                if (customers.get(i).getCustomerId().equals(this.customer.getCustomerId())) {
	                    customers.set(i, updatedCustomer);
	                    break;
	                }
	            }
	        
	            // Tải lại giao diện hiển thị
	            updatePage();
	            
	            customerIdTextField.setEditable(false);
	            
	            fullNameTextField.setEditable(false);
	            fullNameTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String fullNameTemp = fullNameTextField.getText();
	                    if(fullNameTemp.matches("^[A-Za-z ]+$")) 
	                    {
	                    	fullName = fullNameTemp;
	                    	//System.out.println("Full Name: " + fullName);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Họ tên không hợp lệ!");
	                    		fullNameTextField.setText("");
	                    	}
	                }
	            });
	            
	            
	            phoneTextField.setEditable(false);
	            phoneTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String phoneTemp = phoneTextField.getText();
	                    if(phoneTemp.matches("\\d{10}")) 
	                    {
	                    	phone = phoneTemp;
	                    	//System.out.println("Phone: " + phone);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Số điện thoại không hợp lệ!");
	                    		phoneTextField.setText("");
	                    	}
	                }
	            });
	            
	            
	            emailTextField.setEditable(false);
	            emailTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String emailTemp = emailTextField.getText();
	                    if(emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) 
	                    {
	                    	email = emailTemp;
	                    	//System.out.println("Email: " + email);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Email không hợp lệ!");
	                    		emailTextField.setText("");
	                    	}
	                }
	            });
	            
	            
	            addressTextField.setEditable(false);
	            addressTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String adressTemp = addressTextField.getText();
	                    if(adressTemp.matches("^[A-Za-z ]+$")) 
	                    {
	                    	address = adressTemp;
	                    	//System.out.println("Address: " + address);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Địa chỉ không hợp lệ!");
	                    		addressTextField.setText("");
	                    	}
	                }
	            });
	            
	            
	            statusBox.setEnabled(false);        
	            
	            userNameTextField.setEditable(false);
	            userNameTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String userNameContentTemp = userNameTextField.getText();
	                    if(userNameContentTemp.matches("^[A-Za-z0-9_.-]+$")) 
	                    {
	                    	userName = userNameContentTemp;
	                    	//System.out.println("Username: " + userName);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Tên tài khoản không hợp lệ!");
	                    		userNameTextField.setText("");
	                    	}
	                }
	            });
	            
	            
	            passwordTextField.setEditable(false);
	            passwordTextField.addActionListener(new ActionListener() 
	    		{
	                @Override
	                public void actionPerformed(ActionEvent e) 
	                {
	                    String passwordContentTemp = passwordTextField.getText();
	                    if(passwordContentTemp.matches("^[A-Za-z\\d@$!%*#?&]+$")) 
	                    {
	                    	password = passwordContentTemp;
	                    	//System.out.println("Password: " + password);
	                    }
	                    else
	                    	{
	                    		JOptionPane.showInputDialog(this, "Mật khẩu không hợp lệ!");
	                    		passwordTextField.setText("");
	                    	}
	                }
	            });
	            
	    	    
	    	    updateButton.setEnabled(false);
	    	    saveButton.setEnabled(false);
	    	    
	            
	        } else {
	            JOptionPane.showMessageDialog(this, "Không thể cập nhật thông tin khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
	    }
	    
	    // Reset trạng thái các nút
	    updateButton.setEnabled(true);
	    saveButton.setEnabled(false);
	}
}