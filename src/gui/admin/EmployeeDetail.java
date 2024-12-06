package gui.admin;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.*;

import dao.Employee_DAO;
import entity.Customer;
import entity.Employee;

public class EmployeeDetail extends JPanel
{	
	private JPanel functionButton;
	private JPanel formPanel;
	
	private JLabel backButton;
	private JLabel editButton;
	
	private JButton saveButton;
	private JButton cancelButton;
	
	private JLabel employeeIdLabel;
	private JLabel fullNameLabel;
	private JLabel phoneLabel;
	private JLabel emailLabel;
	private JLabel addressLabel;
	private JLabel userNameLabel;
	private JLabel passwordLabel;
	private JLabel permissionsLabel;
	private JLabel hireDateLabel;
	private JLabel statusLabel;
	
	private JTextField employeeIdContent;
	private JTextField fullNameContent;
	private JTextField phoneContent;
	private JTextField emailContent;
	private JTextField addressContent;
	private JTextField userNameContent;
	private JPasswordField passwordContent;
	private JComboBox<String> permissionsContent;
	private JTextField hireDateContent;
	private JComboBox<String> statusContent;
	
	private String employeeId;
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private String userName;
	private String password;
	private String permissions;
	private LocalDate hireDate;
	private String status;
	
	private Employee_DAO employeeDAO = new Employee_DAO();
	private ArrayList<Employee> employees;
	private Employee employee;
	
	public EmployeeDetail(Employee employee)
	{
		this.employee = employee;
		this.employees = employeeDAO.getAll();
		setLayout(new BorderLayout(10,10));
		
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(11,2,10,10));
		
		functionButton = new JPanel();
		functionButton.setLayout(new BorderLayout());
		
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
        		add(new EmployeeManagement());
        		revalidate();
        	    repaint();
    		}
    		
        	
        });

		editButton = new JLabel("Sửa");
		editButton.setPreferredSize(new Dimension(100,50));
		editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		editButton.setBorder(BorderFactory.createLineBorder(Color.black));
		editButton.addMouseListener(new MouseAdapter()
        {
        	@Override
    		public void mouseClicked(MouseEvent e) 
    		{
        		employeeIdContent.setEnabled(false);
        		fullNameContent.setEnabled(true);
        		phoneContent.setEnabled(true);
        		emailContent.setEnabled(true);
        		addressContent.setEnabled(true);
        		userNameContent.setEnabled(true);
        		passwordContent.setEnabled(true);
        		permissionsContent.setEnabled(true);
        		hireDateContent.setEnabled(true);
        		statusContent.setEnabled(true);
        		saveButton.setEnabled(true);
        		cancelButton.setEnabled(true);
    		}
    		
        	
        });


		
		functionButton.add(backButton, BorderLayout.WEST);
		functionButton.add(editButton, BorderLayout.EAST);
		
		
		
		
		employeeIdLabel = new JLabel("Mã nhân viên:");
		employeeIdContent = new JTextField();
		
		fullNameLabel = new JLabel("Họ tên:");
		fullNameContent = new JTextField();		

		phoneLabel = new JLabel("Số điện thoại:");
		phoneContent = new JTextField();

		emailLabel = new JLabel("Email:");
		emailContent = new JTextField();

		addressLabel = new JLabel("Địa chỉ:");
		addressContent = new JTextField();
		
		userNameLabel = new JLabel("Tên tài khoản:");
		userNameContent = new JTextField();

		passwordLabel = new JLabel("Mật khẩu:");
		passwordContent = new JPasswordField();
		
		permissionsLabel = new JLabel("Quyền hạn:");
		permissionsContent =  new JComboBox<>();
		permissionsContent.addItem("staff");
		permissionsContent.addItem("admin");
		
		
		permissionsContent.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        if (permissionsContent.getSelectedItem().equals("admin")) 
		        {
		            permissions = Employee.PERMISSION_ADMIN;
		        }
		        else 
		        {
		            permissions = Employee.PERMISSION_STAFF;
		        }
		    }
		});
		
		hireDateLabel = new JLabel("Ngày bắt đầu làm việc:");
		hireDateContent = new JTextField();
		
		
		hireDateContent.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) 
		    {
		    	String hireDateString = hireDateContent.getText();
		        if (hireDateString.matches("^\\d{4}-\\d{2}-\\d{2}$")) 
		        {
		        	String[] dateParts = hireDateString.split("-");
		        	
		        	int year = Integer.parseInt(dateParts[0]);
		        	int month = Integer.parseInt(dateParts[1]);
		        	int day = Integer.parseInt(dateParts[2]);

		        	
		            LocalDate dateHire = LocalDate.of(year, month, day);
		            
		            System.out.println("Hire Date: " + dateHire);
		       
		        }
		    }
		});
		
		statusLabel = new JLabel("Trạng thái:");
		statusContent =  new JComboBox<>();
		statusContent.addItem("Đang làm việc");
		statusContent.addItem("Đã nghỉ");	
		
		statusContent.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e) 
		    {
		        if (statusContent.getSelectedItem().equals("Đang làm việc")) 
		        {
		            status = Employee.STATUS_WORKING;
		        } else 
		        {
		            status = Employee.STATUS_RESIGN;
		        }
		    }
		});
		
		 if (employee != null) {
		        employeeId = employee.getEmployeeId();
		        fullName = employee.getFullName();
		        phone = employee.getPhone();
		        email = employee.getEmail();
		        address = employee.getAddress();
		        userName = employee.getUserName();
		        password = employee.getPassWord();
		        permissions = employee.getPermissions();
		        hireDate = employee.getDateHired();
		        status = employee.getStatus();
		        

		        employeeIdContent.setText(employeeId);
		        fullNameContent.setText(fullName);
		        phoneContent.setText(phone);
		        emailContent.setText(email);
		        addressContent.setText(address);
		        userNameContent.setText(userName);
		        passwordContent.setText(password);
		        permissionsContent.setSelectedItem(permissions.equals(Employee.PERMISSION_ADMIN) ? "Admin" : "Nhân viên");
		        hireDateContent.setText(hireDate.toString());
		        statusContent.setSelectedItem(status.equals(Employee.STATUS_WORKING) ? "Đang làm việc" : "Đã nghỉ");
		    }

		formPanel.add(employeeIdLabel);
		formPanel.add(employeeIdContent);
		employeeIdContent.setEnabled(false);
		
		formPanel.add(fullNameLabel);
		formPanel.add(fullNameContent);
		fullNameContent.setEnabled(false);
		
		formPanel.add(phoneLabel);
		formPanel.add(phoneContent);
		phoneContent.setEnabled(false);

		formPanel.add(emailLabel);
		formPanel.add(emailContent);
		emailContent.setEnabled(false);

		formPanel.add(addressLabel);
		formPanel.add(addressContent);
		addressContent.setEnabled(false);

		formPanel.add(userNameLabel);
		formPanel.add(userNameContent);
		userNameContent.setEnabled(false);

		formPanel.add(passwordLabel);
		formPanel.add(passwordContent);
		passwordContent.setEnabled(false);

		formPanel.add(permissionsLabel);
		formPanel.add(permissionsContent);
		permissionsContent.setEnabled(false);
		
		formPanel.add(hireDateLabel);
		formPanel.add(hireDateContent);
		hireDateContent.setEnabled(false);
		
		formPanel.add(statusLabel);
		formPanel.add(statusContent);
		statusContent.setEnabled(false);
		
		
		
		saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        // Action for the Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	employeeId = employeeIdContent.getText().trim();
            	fullName = fullNameContent.getText().trim();
            	phone = phoneContent.getText().trim();
            	email = emailContent.getText().trim();
            	address= addressContent.getText().trim();
            	userName = userNameContent.getText().trim();
            	password = new String(passwordContent.getPassword()).trim();
            	permissions = permissionsContent.getSelectedItem().toString().trim();
            	try {
            	    hireDate = LocalDate.parse(hireDateContent.getText().trim());
            	} catch (DateTimeParseException ex) {
            	    JOptionPane.showMessageDialog(formPanel, "Ngày không hợp lệ! Định dạng đúng: yyyy-MM-dd", "Lỗi", JOptionPane.ERROR_MESSAGE);
            	    hireDateContent.requestFocus();
            	    return;
            	}

            	status = statusContent.getSelectedItem().toString().trim();

            	
            	try {
            		Employee updatedEmployee  = new Employee(employeeId, fullName, phone, email, address, userName, password, permissions);
            		if (employeeDAO.update(updatedEmployee)) {
            			// Cập nhật dữ liệu trên giao diện
        	            JOptionPane.showMessageDialog(formPanel, "Cập nhật thông tin khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        	            // Thay đổi thông tin trong danh sách khách hàng
        	            for (int i = 0; i < employees.size(); i++) {
        	                if (employees.get(i).getEmployeeId().equals(updatedEmployee.getEmployeeId())) {
        	                	employees.set(i, updatedEmployee);
        	                    break;
        	                }
        	            }
        	            
        	            if (!validateInput()) {
        	            	return;
        	            }
        	        
        	            
        	            employeeIdContent.setEnabled(false);
                 		fullNameContent.setEnabled(false);
                 		phoneContent.setEnabled(false);
                 		emailContent.setEnabled(false);
                 		addressContent.setEnabled(false);
                 		userNameContent.setEnabled(false);
                 		passwordContent.setEnabled(false);
                 		permissionsContent.setEnabled(false);
                 		hireDateContent.setEnabled(false);
                 		statusContent.setEnabled(false);
                 		saveButton.setEnabled(false);
                 		cancelButton.setEnabled(false);    
        	            
        	            
        	    	    editButton.setEnabled(true);
        	    	    saveButton.setEnabled(false);
        	    	    
        	            
        	        } else {
        	            JOptionPane.showMessageDialog(formPanel, "Không thể cập nhật thông tin khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            		}
            	} catch (Exception ex) {
            		JOptionPane.showMessageDialog(formPanel, "Lỗi: " + ex.getMessage(), "Thông báo", JOptionPane.ERROR_MESSAGE);
            	}

            	formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving
            }
        });

        // Action for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	employeeId = employee.getEmployeeId();
		        fullName = employee.getFullName();
		        phone = employee.getPhone();
		        email = employee.getEmail();
		        address = employee.getAddress();
		        userName = employee.getUserName();
		        password = employee.getPassWord();
		        permissions = employee.getPermissions();
		        hireDate = employee.getDateHired();
		        status = employee.getStatus();
        		
        		employeeIdContent.setText(employee.getEmployeeId());
		        fullNameContent.setText(employee.getFullName());
		        phoneContent.setText(employee.getPhone());
		        emailContent.setText(employee.getEmail());
		        addressContent.setText(employee.getAddress());
		        userNameContent.setText(employee.getUserName());
		        passwordContent.setText(employee.getPassWord());
		        permissionsContent.setSelectedItem(employee.getPermissions().equals(Employee.PERMISSION_ADMIN) ? "Admin" : "Nhân viên");
		        hireDateContent.setText(employee.getDateHired().toString());
		        statusContent.setSelectedItem(employee.getStatus().equals(Employee.STATUS_WORKING) ? "Đang làm việc" : "Đã nghỉ");
		        
		        employeeIdContent.setEnabled(false);
        		fullNameContent.setEnabled(false);
        		phoneContent.setEnabled(false);
        		emailContent.setEnabled(false);
        		addressContent.setEnabled(false);
        		userNameContent.setEnabled(false);
        		passwordContent.setEnabled(false);
        		permissionsContent.setEnabled(false);
        		hireDateContent.setEnabled(false);
        		statusContent.setEnabled(false);
        		saveButton.setEnabled(false);
        		cancelButton.setEnabled(false);
        		
        		formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving
            }
        });

        // Add buttons to the dialog
        formPanel.add(saveButton);
        formPanel.add(cancelButton);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        
        add(functionButton, BorderLayout.NORTH);

        add(formPanel, BorderLayout.CENTER);
	}
	
	
	// Kiểm tra định dạng các thông tin
			private boolean validateInput() 
			{
				String fullNameTemp = fullNameContent.getText().trim();
				String phoneTemp = phoneContent.getText().trim();
				String emailTemp = emailContent.getText().trim();
				String addressTemp = addressContent.getText().trim();			
				String userNameTemp = userNameContent.getText().trim();
				String passwordTemp = new String(passwordContent.getPassword()).trim();
				
				
				if (fullNameTemp.isEmpty() || !fullNameTemp.matches("^\\p{L}+(\\s+\\p{L}+)*$")) {
			        JOptionPane.showMessageDialog(null, "Họ tên không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        fullNameContent.requestFocus();
			        return false;
			    }

			    if (phoneTemp.isEmpty() || !phoneTemp.matches("\\d{10}")) {
			        JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ! Định dạng đúng: 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        phoneContent.requestFocus();
			        return false;
			    }

			    if (emailTemp.isEmpty() || !emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			        JOptionPane.showMessageDialog(null, "Email không hợp lệ! Vui lòng nhập đúng định dạng email.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        emailContent.requestFocus();
			        return false;
			    }

			    if (addressTemp.isEmpty() || !addressTemp.matches("^[a-zA-ZÀ-ỹ0-9 .,-/]+$")) {
			        JOptionPane.showMessageDialog(null, "Địa chỉ không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        addressContent.requestFocus();
			        return false;
			    }

			    if (userNameTemp.isEmpty() || !userNameTemp.matches("^[A-Za-z0-9_.-]+$")) {
			        JOptionPane.showMessageDialog(null, "Tên tài khoản có thể gồm các ký tự chữ cái, số và ký tự đặc biệt ( _ / . / - )", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        userNameContent.requestFocus();
			        return false;
			    }

			    if (passwordTemp.isEmpty() || !passwordTemp.matches("^[A-Za-z\\d@$!%*#?&]+$")) {
			        JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ! Không để trống và chỉ có thể chứa các ký tự chữ cái, số và ký tự đặc biệt ( @ / $ / ! / % / * / # / ? / & )", "Lỗi", JOptionPane.ERROR_MESSAGE);
			        passwordContent.requestFocus();
			        return false;
			    }

			    // Tất cả dữ liệu hợp lệ
			    return true;
			}
}
