package gui.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dao.Customer_DAO;
import entity.Customer;

public class CustomerManagement extends JPanel {
	private final Customer_DAO customerDAO;
	private JTable customerTable;
	private DefaultTableModel tableModel;
	
	
	// Tạo các biến components
	private JPanel buttonPanel;
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
	private JButton deleteButton;
	private JButton updateButton;
	private JButton saveButton;
	private JButton cancelButton;
	
	
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
		// Tạo table
		String[] columnNames = {"ID", "Họ tên", "Số điện thoại", "Email", "Địa chỉ", "Trạng thái", "Tên tài khoản", "Ngày tạo"};
		tableModel = new DefaultTableModel(columnNames, 0);
		customerTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        
        addButton = new JButton("Thêm");
        deleteButton = new JButton("Xóa");
        updateButton = new JButton("Sửa");
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Gán sự kiện các button
        addButton.addActionListener(this::addCustomerDialog);
        //deleteButton.addActionListener(this::deleteCustomerDialog);
        updateButton.addActionListener(this::updateCustomerDialog);
	}
	
	private void loadCustomerData() {
		tableModel.setRowCount(0);
		ArrayList<Customer> customers = customerDAO.getAll();
		for (Customer customer : customers) {
			tableModel.addRow(new Object[] {
					customer.getCustomerId(),
	                customer.getFullName(),
	                customer.getPhone(),
	                customer.getEmail(),
	                customer.getAddress(),
	                customer.getStatus(),
	                customer.getUserName(),
	                customer.getCreatedAt()
			});
		}
	}
	
	
	// Event add customer
	private void addCustomerDialog(ActionEvent e) {
		dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Khách Hàng", true);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setSize(400, 400);
        
        customerIdTextField = new JTextField();
        
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
                	System.out.println("Full Name: " + fullName);
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
                	System.out.println("Phone: " + phone);
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
                	System.out.println("Email: " + email);
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
                	System.out.println("Address: " + address);
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
                	System.out.println("Username: " + userName);
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
                	System.out.println("Username: " + password);
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
	private void updateCustomerDialog(ActionEvent e) {
		// Kiểm tra ID khách hàng có tồn tại hay không
    	int selectedRow = customerTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		customerId = (String) tableModel.getValueAt(selectedRow, 0);
		Customer customer = customerDAO.getById(customerId);
		if(customerDAO.getById(customerId) == null) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa Thông Tin Khách Hàng", true);
        dialog.setLayout(new GridLayout(9, 2, 10, 10));
        dialog.setSize(400, 400);
        
        customerIdTextField = new JTextField(customer.getCustomerId()); // ID không thay đổi
        customerIdTextField.setEditable(false); // Khóa ID
        fullNameTextField = new JTextField(customer.getFullName());
        phoneTextField = new JTextField(customer.getPhone());
        emailTextField = new JTextField(customer.getEmail());
        addressTextField = new JTextField(customer.getAddress());
        statusBox = new JComboBox<>(new String[]{Customer.STATUS_ACTIVE, Customer.STATUS_INACTIVE});
        statusBox.setSelectedItem(customer.getStatus());
        userNameTextField = new JTextField(customer.getUserName());
        passwordTextField = new JPasswordField(customer.getPassword());
        
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
        	
        	
        	// Cập nhật thông tin khách hàng
            customer.setFullName(fullName);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setAddress(address);
            customer.setStatus(status);
            customer.setUserName(userName);
            customer.setPassword(password);
        	
        	
        	// Nếu có ID trong DB thì hiện thông tin của khách hàng
        	if(customerDAO.update(customerDAO.getById(customerId))) {
        		JOptionPane.showMessageDialog(this, "Sửa thông tin khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        		loadCustomerData();
        		dialog.dispose();
        	}
        	// Nếu chưa có ID thì add customer
        	else {
        		JOptionPane.showMessageDialog(this, "Không sửa được thông tin khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        cancelButton.addActionListener(ev -> dialog.dispose());
        dialog.setVisible(true);
	}
}