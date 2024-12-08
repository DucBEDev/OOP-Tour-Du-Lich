package gui.admin;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.Customer_DAO;
import entity.Customer;


public class CustomerDetail extends JPanel {
	private Customer_DAO customerDAO = new Customer_DAO();
	private ArrayList<Customer> customers;
	private Customer customer;
	
	private JPanel formPanel;
	private JPanel buttonPanel;
	private JPanel backPanel;
	
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
	
	private JLabel backButton;
	private JButton updateButton;
	private JButton saveButton;
	private JButton cancelButton;
	
	private String customerId;
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private String status;
	private String userName;
	private LocalDate createdAt;
	private String password;
	
	
	public CustomerDetail(Customer customer) {
		this.customer = customer;
		this.customers = customerDAO.getAll();
		setLayout(new BorderLayout(10, 10));
		formPanel = new JPanel(new GridLayout(9, 3, 10, 10));
		buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		backPanel = new JPanel();
		
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
        
        backButton = new JLabel("Thoát");
		backButton.setPreferredSize(new Dimension(100,50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createLineBorder(Color.black));
        backButton.addMouseListener(new MouseAdapter()
        {
        	@Override
    		public void mouseClicked(MouseEvent e) 
    		{
        		removeAll();
        		add(new CustomerManagement());
        		revalidate();
        	    repaint();
    		}        	
        });
        
        // Add components to dialog
        backPanel.add(backButton, BorderLayout.WEST);
        
        formPanel.add(customerIdLabel, BorderLayout.CENTER);
        formPanel.add(customerIdTextField, BorderLayout.EAST);
        
        formPanel.add(fullNameLabel, BorderLayout.CENTER);
        formPanel.add(fullNameTextField, BorderLayout.EAST);
        
        formPanel.add(phoneLabel, BorderLayout.CENTER);
        formPanel.add(phoneTextField, BorderLayout.EAST);
        
        formPanel.add(emailLabel, BorderLayout.CENTER);
        formPanel.add(emailTextField, BorderLayout.EAST);
        
        formPanel.add(addressLabel, BorderLayout.CENTER);
        formPanel.add(addressTextField, BorderLayout.EAST);
        
        formPanel.add(statusLabel, BorderLayout.CENTER);
        formPanel.add(statusBox, BorderLayout.EAST);
        
        formPanel.add(userNameLabel, BorderLayout.CENTER);
        formPanel.add(userNameTextField, BorderLayout.EAST);
        
        formPanel.add(passwordLabel, BorderLayout.CENTER);
        formPanel.add(passwordTextField, BorderLayout.EAST);
	    
        
        updateButton = new JButton("Sửa");
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        saveButton.setEnabled(false);
        
        buttonPanel.add(updateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
	    
	    updateButton.addActionListener(this::updateCustomerInfo);
	    saveButton.addActionListener(this::saveCustomerInfo);
	    cancelButton.addActionListener(this::cancel);
	    
	    add(backPanel, BorderLayout.NORTH);
	    add(formPanel, BorderLayout.CENTER);
	    add(buttonPanel, BorderLayout.SOUTH);
	}
	
	
	private void updateCustomerInfo(ActionEvent e) {
		
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
	    // Lấy dữ liệu từ các trường văn bản
	    customerId = customerIdTextField.getText().trim();
	    fullName = fullNameTextField.getText().trim();
	    phone = phoneTextField.getText().trim();
	    email = emailTextField.getText().trim();
	    address = addressTextField.getText().trim();
	    status = (String) statusBox.getSelectedItem();
	    userName = userNameTextField.getText().trim();
	    password = new String(passwordTextField.getPassword()).trim();

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
	            
	            if (!validateInput()) {
	            	return;
	            }
	        
	            
	            customerIdTextField.setEditable(false);	            
	            fullNameTextField.setEditable(false);
	            phoneTextField.setEditable(false);           
	            emailTextField.setEditable(false);       
	            addressTextField.setEditable(false);
	            statusBox.setEnabled(false);                    
	            userNameTextField.setEditable(false);
	            passwordTextField.setEditable(false);    
	            
	            
	    	    updateButton.setEnabled(true);
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
	
	private boolean validateInput() {
		String fullNameTemp = fullNameTextField.getText().trim();
		String phoneTemp = phoneTextField.getText().trim();
		String emailTemp = emailTextField.getText().trim();
		String addressTemp = addressTextField.getText().trim();			
		String userNameTemp = userNameTextField.getText().trim();
		String passwordTemp = new String(passwordTextField.getPassword()).trim();
		
		
		if (fullNameTemp.trim().isEmpty() || !fullNameTemp.matches("^[A-Za-zÀ-ỹĐđ\\s]+$")) {
	        JOptionPane.showMessageDialog(null, "Họ tên không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        fullNameTextField.requestFocus();
	        return false;
	    }

	    if (phoneTemp.isEmpty() || !phoneTemp.matches("\\d{10}")) {
	        JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ! Định dạng đúng: 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        phoneTextField.requestFocus();
	        return false;
	    }

	    if (emailTemp.isEmpty() || !emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
	        JOptionPane.showMessageDialog(null, "Email không hợp lệ! Vui lòng nhập đúng định dạng email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        emailTextField.requestFocus();
	        return false;
	    }

	    if (addressTemp.isEmpty() || !addressTemp.matches("^[a-zA-ZÀ-ỹ0-9 .,-/]+$")) {
	        JOptionPane.showMessageDialog(null, "Địa chỉ không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        addressTextField.requestFocus();
	        return false;
	    }

	    if (userNameTemp.isEmpty() || !userNameTemp.matches("^[A-Za-z0-9_.-]+$")) {
	        JOptionPane.showMessageDialog(null, "Tên tài khoản có thể gồm các ký tự chữ cái, số và ký tự đặc biệt ( _ / . / - )", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        userNameTextField.requestFocus();
	        return false;
	    }

	    if (passwordTemp.isEmpty() || !passwordTemp.matches("^[A-Za-z\\d@$!%*#?&_]+$")) {
	        JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ! Không để trống và chỉ có thể chứa các ký tự chữ cái, số và ký tự đặc biệt ( @ / $ / ! / % / * / # / ? / & / _)", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        passwordTextField.requestFocus();
	        return false;
	    }

	    // Tất cả dữ liệu hợp lệ
	    return true;
	}
	
	
	private void cancel(ActionEvent e) {
		fullNameTextField.setEditable(false);
	    phoneTextField.setEditable(false);
	    emailTextField.setEditable(false);
	    addressTextField.setEditable(false);
	    statusBox.setEnabled(false);	    
	    userNameTextField.setEditable(false);
	    passwordTextField.setEditable(false);
	    	    
	    updateButton.setEnabled(true);
	    saveButton.setEnabled(false);
	}
}