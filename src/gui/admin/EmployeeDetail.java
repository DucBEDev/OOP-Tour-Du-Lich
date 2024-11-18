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

import javax.swing.*;

import dao.Employee_DAO;
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
	private JTextField passwordContent;
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
	
	public EmployeeDetail(Employee employee)
	{
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
        		employeeIdContent.setEnabled(true);
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
		employeeIdContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String employeeIdTemp = employeeIdContent.getText();
                if(employeeDAO.checkExistById(employeeIdTemp))
                {
                	employeeId = employeeIdTemp;
              
                	System.out.println("Employee ID: " + employeeId);
                }
                else
                	{
                		System.out.println("Employee Not Found");
                		fullNameContent.setText("");
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


		phoneLabel = new JLabel("Số điện thoại:");
		if(employee != null)
		{
			phoneContent = new JTextField(employee.getPhone());
		}
		else
		{
			phoneContent = new JTextField();
		}
		phoneContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String phoneTemp = phoneContent.getText();
                if(phoneTemp.matches("\\d{10}")) 
                {
                	phone = phoneTemp;
                	System.out.println("Phone: " + phone);
                }
                else
                	{
                		System.out.println("Invalid input");
                		phoneContent.setText("");
                	}
            }
        });

		emailLabel = new JLabel("Email:");
		if(employee != null)
		{
			emailContent = new JTextField(employee.getEmail());
		}
		else
		{
			emailContent = new JTextField();
		}
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
		if(employee != null)
		{
			addressContent = new JTextField(employee.getAddress());
		}
		else
		{
			addressContent = new JTextField();
		}
		addressContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String adressTemp = addressContent.getText();
                if(adressTemp.matches("^[A-Za-z ]+$")) 
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

		
		userNameLabel = new JLabel("Tên tài khoản:");
		if(employee != null)
		{
			userNameContent = new JTextField(employee.getUserName());
		}
		else
		{
			userNameContent = new JTextField();
		}
		userNameContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String userNameContentTemp = userNameContent.getText();
                if (userNameContentTemp.matches("^[A-Za-z0-9_.-]+$"))
                {
                	userName = userNameContentTemp;
                	System.out.println("Username: " + userName);
                }
                else
                	{
                		System.out.println("Invalid input");
                		userNameContent.setText("");
                	}
            }
        });

		passwordLabel = new JLabel("Mật khẩu:");
		if(employee != null)
		{
			passwordContent = new JTextField(employee.getPassWord());
		}
		else
		{
			passwordContent = new JTextField();
		}
		passwordContent.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                String passwordContentTemp = passwordContent.getText();
                if(passwordContentTemp.matches("^[A-Za-z\\d@$!%*#?&]+$")) 
                {
                	password = passwordContentTemp;
                	System.out.println("Username: " + password);
                }
                else
                	{
                		System.out.println("Invalid input");
                		passwordContent.setText("");
                	}
            }
        });

		permissionsLabel = new JLabel("Quyền hạn:");
		permissionsContent =  new JComboBox<>();
		permissionsContent.addItem("Nhân viên");
		permissionsContent.addItem("Admin");
		
		
		permissionsContent.addActionListener(new ActionListener() 
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        if (permissionsContent.getSelectedItem().equals("Admin")) 
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
		if(employee != null)
		{
			hireDateContent = new JTextField(employee.getDateHired().toString());
		}
		else
		{
			hireDateContent = new JTextField();
		}
		
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
            	employee.setFullName(fullName);
            	employee.setPhone(phone);
            	employee.setEmail(email);
            	employee.setAddress(address);
            	employee.setUserName(userName);
            	employee.setPassword(password);
            	employee.setPermissions(permissions);
            	employee.setHireDate(hireDate);
            	employee.setStatus(status);
            	
            	if(employeeDAO.update(employee))
            	{
            		System.out.println("Update Completed");
            		JDialog updateCompleteDialog = new JDialog();
            		JPanel updateCompletePanel = new JPanel();
            		JPanel okButtonPanel = new JPanel();
            		JLabel updateCompleteLabel = new JLabel("Cập nhật thành công");
            		JButton okButton = new JButton("OK");
            		okButton.addActionListener(evt->updateCompleteDialog.dispose());
            		updateCompletePanel.add(updateCompleteLabel);
            		okButtonPanel.add(okButton);
            		updateCompleteDialog.add(updateCompletePanel, BorderLayout.CENTER);
            		updateCompleteDialog.add(okButtonPanel, BorderLayout.SOUTH);
            		updateCompleteDialog.setLocationRelativeTo(null);
            		updateCompleteDialog.pack();
            		updateCompleteDialog.setVisible(true);
            	}
            	else System.out.println("Update Failed");

            		                
            	
            	
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

        // Action for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	fullName= null;
        		phone =null;
        		email= null;
        		address=null;
        		userName=null;
        		password=null;
        		permissions=null;
        		hireDate = null;
        		status=null;
        		
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
}
