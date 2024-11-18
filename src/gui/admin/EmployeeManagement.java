package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.*;

import entity.Employee;
import dao.Employee_DAO;



public class EmployeeManagement extends JPanel
{
	private int totalPages = 1;
	private int currentPage = 1;
	private  int rowPerPage = 10;
	
	private JPanel employeeListPanel;
	private JPanel functionButtonPanel;
	
	private JLabel pageNumber;
	
	private Employee_DAO employeeDAO = new Employee_DAO();
	
	private ArrayList<Employee> employees = new ArrayList<>();
	
	private Employee selectedEmployee;
	
	public EmployeeManagement()
	{
		setLayout(new BorderLayout());
		
		employeeListPanel = new JPanel();
		employeeListPanel.setLayout(new GridLayout(10,1));
		
		
		functionButtonPanel= new JPanel();
		functionButtonPanel.setBackground(Color.PINK);
		functionButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15, 15));
		
		pageNumber = new JLabel("Page: " + currentPage);
		
		JButton addEmployee = new JButton("Thêm");
		AddEmployeeDialog actionListenerAdd = new AddEmployeeDialog();
		addEmployee.addActionListener(actionListenerAdd);
		
		JButton deleteTour = new JButton("Xóa");
		
//		JButton updateTour = new JButton("Sửa");
//		UpdateEmployeeDialog actionListenerUpdate = new UpdateEmployeeDialog();
//		updateTour.addActionListener(actionListenerUpdate);
		
		JButton previousPage = new JButton("<");
		JButton nextPage = new JButton(">");

		functionButtonPanel.add(addEmployee);
		
		functionButtonPanel.add(previousPage);
		functionButtonPanel.add(pageNumber);
		functionButtonPanel.add(nextPage);
//		functionButtonPanel.add(updateTour);
		functionButtonPanel.add(deleteTour);
	
        JScrollPane scrollBar = new JScrollPane(employeeListPanel);
       
        add(scrollBar, BorderLayout.CENTER);
        add(functionButtonPanel, BorderLayout.SOUTH);
        
        LoadEmployeeData();
	}
	
	// Hiện thông tin khách hàng
		private void LoadEmployeeData() 
		{
		    // Lấy danh sách khách hàng
			employees = employeeDAO.getAll();
			totalPages = (int) Math.ceil((double) employees.size() / rowPerPage);
			UpdatePage(); // Chỉ tải dữ liệu của trang đầu tiên
		}
		
		
		// Cập nhật UI ở trang hiện tại
		private void UpdatePage() 
		{
	        employeeListPanel.removeAll();

	        int start = (currentPage - 1) * rowPerPage;					// Vị trí khách hàng đầu tiên trong trang
	        int end = Math.min(start + rowPerPage, employees.size());	// Vị trí khách hàng cuối cùng trong trang

	        for (int i = start; i < end; i++) 
	        {
	            JPanel row = CreateEmployeeRow(employees.get(i), i - start);
	            
	            employeeListPanel.add(row);
	            
	        }

	        // Thêm các dòng trống nếu trang không đầy
	        for (int i = end - start; i < rowPerPage; i++) 
	        {
	            JPanel emptyRow = new JPanel();
	            emptyRow.setBackground( Color.white);
	            employeeListPanel.add(emptyRow);
	        }

	        pageNumber.setText("Page: " + currentPage);
	        employeeListPanel.revalidate();
	        employeeListPanel.repaint();
	    }
		
		
		// Tạo dòng hiển thị thông tin trên Page
			private JPanel CreateEmployeeRow(Employee employee, int indexInPage	) 
			{
			    JPanel row = new JPanel(new GridLayout(2, 3)); // 4 cột: customerId, fullName, phone, email
			    row.setBackground( Color.white );
			    row.setBorder(BorderFactory.createLineBorder(Color.black));

			    JLabel idLabel = new JLabel(employee.getEmployeeId());
			    JLabel nameLabel = new JLabel(employee.getFullName());
			    JLabel phoneLabel = new JLabel(employee.getPhone());
			    JLabel emailLabel = new JLabel(employee.getEmail());
			    JLabel permissionLabel = new JLabel(employee.getPermissions());

			    // Căn chỉnh văn bản
			    idLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    permissionLabel.setHorizontalAlignment(SwingConstants.CENTER);


			    row.add(idLabel);
			    row.add(phoneLabel);
			    row.add(permissionLabel);
			    row.add(nameLabel); 
			    row.add(emailLabel);
			    
			    EmployeeDetailControl mouseListener = new EmployeeDetailControl(this);
			    row.addMouseListener(mouseListener);
		        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
		        row.putClientProperty("employee", employee);

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
		
		private class EmployeeDetailControl extends MouseAdapter
		{
			private EmployeeManagement employeeManagement;
			
			public EmployeeDetailControl(EmployeeManagement employeeManagement)
			{
				this.employeeManagement=employeeManagement;
			}
			
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				JPanel sourcePanel = (JPanel)e.getSource();
				Employee employee = (Employee)sourcePanel.getClientProperty("employee");
				
				employeeManagement.removeAll();
				EmployeeDetail employeeDetail = new EmployeeDetail(employee);
				employeeManagement.add(employeeDetail);
				employeeManagement.revalidate();
                employeeManagement.repaint();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				JPanel tempPanel = (JPanel)e.getSource();
				tempPanel.setBackground(Color.LIGHT_GRAY);
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				JPanel tempPanel = (JPanel)e.getSource();
				tempPanel.setBackground(Color.WHITE);			
			}
		}
	
	
	private class AddEmployeeDialog implements ActionListener
	{
		
		private JLabel fullNameLabel;
		private JLabel phoneLabel;
		private JLabel emailLabel;
		private JLabel addressLabel;
		private JLabel userNameLabel;
		private JLabel passwordLabel;
		private JLabel permissionsLabel;
		
		private JTextField fullNameContent;
		private JTextField phoneContent;
		private JTextField emailContent;
		private JTextField addressContent;
		private JTextField userNameContent;
		private JTextField passwordContent;
		private JComboBox<String> permissionsContent;
		
		private String fullName;
		private String phone;
		private String email;
		private String address;
		private String userName;
		private String password;
		private String permissions;

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			JDialog addDialog = new JDialog();
			addDialog.setAlwaysOnTop(true);
			addDialog.setLayout(new GridLayout(8,2,10,10));
			
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
			phoneContent = new JTextField();
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
			userNameContent = new JTextField();
			userNameContent.addActionListener(new ActionListener() 
			{
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	                String userNameContentTemp = userNameContent.getText();
	                if(userNameContentTemp.matches("^[A-Za-z0-9_.-]+$")) 
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
			passwordContent = new JPasswordField();
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
			        } else 
			        {
			            permissions = Employee.PERMISSION_STAFF;
			        }
			    }
			});

			
			addDialog.add(fullNameLabel);
			addDialog.add(fullNameContent);

			addDialog.add(phoneLabel);
			addDialog.add(phoneContent);

			addDialog.add(emailLabel);
			addDialog.add(emailContent);

			addDialog.add(addressLabel);
			addDialog.add(addressContent);

			addDialog.add(userNameLabel);
			addDialog.add(userNameContent);

			addDialog.add(passwordLabel);
			addDialog.add(passwordContent);

			addDialog.add(permissionsLabel);
			addDialog.add(permissionsContent);
			
			JButton saveButton = new JButton("Lưu");
	        JButton cancelButton = new JButton("Hủy");

	        // Action for the Save button
	        saveButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	            	Employee_DAO employeeDAO = new Employee_DAO();
	            	Employee temp = new Employee( fullName, phone, email, address, userName, password, permissions);
	            	System.out.println(temp);
	            	employeeDAO.add(temp);
	                
	            	JOptionPane addComplete = new JOptionPane("Thêm thành công.\nBạn có muốn thêm tiếp không?", JOptionPane.YES_NO_OPTION);
	            	addDialog.add(addComplete);
	                addDialog.dispose(); // Close the dialog after saving
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
	                addDialog.dispose(); 
	            }
	        });

	        // Add buttons to the dialog
	        addDialog.add(saveButton);
	        addDialog.add(cancelButton);

	        // Set dialog properties
	        addDialog.setSize(400, 500); // Set a reasonable size for the dialog
	        addDialog.setLocationRelativeTo(null); // Center the dialog on the screen
	        addDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        addDialog.setVisible(true); // Show the dialog
			
			
			
		}
		
				
	
	}
	
//	private class UpdateEmployeeDialog implements ActionListener
//	{
//		private JLabel employeeIdLabel;
//		private JLabel fullNameLabel;
//		private JLabel phoneLabel;
//		private JLabel emailLabel;
//		private JLabel addressLabel;
//		private JLabel userNameLabel;
//		private JLabel passwordLabel;
//		private JLabel permissionsLabel;
//		private JLabel hireDateLabel;
//		private JLabel statusLabel;
//		
//		private JTextField employeeIdContent;
//		private JTextField fullNameContent;
//		private JTextField phoneContent;
//		private JTextField emailContent;
//		private JTextField addressContent;
//		private JTextField userNameContent;
//		private JTextField passwordContent;
//		private JComboBox<String> permissionsContent;
//		private JTextField hireDateContent;
//		private JComboBox<String> statusContent;
//		
//		private String employeeId;
//		private String fullName;
//		private String phone;
//		private String email;
//		private String address;
//		private String userName;
//		private String password;
//		private String permissions;
//		private LocalDate hireDate;
//		private String status;
//		
//		private Employee_DAO employeeDAO = new Employee_DAO();
//		
//		private Employee tempEmployee;
//
//		@Override
//		public void actionPerformed(ActionEvent e) 
//		{
//			
//			JDialog updateDialog = new JDialog();
//			updateDialog.setAlwaysOnTop(true);
//
//			updateDialog.setLayout(new GridLayout(11,2,10,10));
//			
//			employeeIdLabel = new JLabel("Mã nhân viên:");
//			employeeIdContent = new JTextField();
//			employeeIdContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String employeeIdTemp = employeeIdContent.getText();
//	                if(employeeDAO.checkExistById(employeeIdTemp))
//	                {
//	                	employeeId = employeeIdTemp;
//	                	tempEmployee = employeeDAO.getById(employeeId);
//	                	
//	                	fullName = tempEmployee.getFullName();
//	                	phone = tempEmployee.getPhone();
//	                	email = tempEmployee.getEmail();
//	                	address = tempEmployee.getAddress();
//	                	userName = tempEmployee.getUserName();
//	                	password = tempEmployee.getPassWord();
//	                	permissions = tempEmployee.getPermissions();
//	                	hireDate = tempEmployee.getDateHired();
//	                	status = tempEmployee.getStatus();
//	                	
//	                	fullNameContent.setText(tempEmployee.getFullName());
//	                    phoneContent.setText(tempEmployee.getPhone());
//	                    emailContent.setText(tempEmployee.getEmail());
//	                    addressContent.setText(tempEmployee.getAddress());
//	                    userNameContent.setText(tempEmployee.getUserName());
//	                    passwordContent.setText(tempEmployee.getPassWord());
//	                    if(tempEmployee.getPermissions().equals(Employee.PERMISSION_ADMIN)) permissionsContent.setSelectedItem("Admin");
//	    				else if(tempEmployee.getPermissions().equals(Employee.PERMISSION_STAFF)) permissionsContent.setSelectedItem("Nhân viên");
//	                    hireDateContent.setText(tempEmployee.getDateHired().toString());
//	                    if(tempEmployee.getStatus().equals(Employee.STATUS_WORKING)) statusContent.setSelectedItem("Đang làm việc");
//	    				else if(tempEmployee.getStatus().equals(Employee.STATUS_RESIGN)) statusContent.setSelectedItem("Đã nghỉ");
//	                    
//	                    
//	                    
//	                	System.out.println("Employee ID: " + employeeId);
//	                }
//	                else
//	                	{
//	                		System.out.println("Employee Not Found");
//	                		fullNameContent.setText("");
//	                	}
//	            }
//	        });
//			
//			fullNameLabel = new JLabel("Họ tên:");
//			if(tempEmployee != null)
//			{
//				fullNameContent = new JTextField(tempEmployee.getFullName());
//			}
//			else
//			{
//				fullNameContent = new JTextField();
//			}
//			
//			fullNameContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String fullNameTemp = fullNameContent.getText();
//	                if(fullName.matches("^[A-Za-z ]+$")) 
//	                {
//	                	fullName = fullNameTemp;
//	                	System.out.println("Full Name: " + fullName);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		fullNameContent.setText("");
//	                	}
//	            }
//	        });
//
//
//			phoneLabel = new JLabel("Số điện thoại:");
//			if(tempEmployee != null)
//			{
//				phoneContent = new JTextField(tempEmployee.getPhone());
//			}
//			else
//			{
//				phoneContent = new JTextField();
//			}
//			phoneContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String phoneTemp = phoneContent.getText();
//	                if(phoneTemp.matches("\\d{10}")) 
//	                {
//	                	phone = phoneTemp;
//	                	System.out.println("Phone: " + phone);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		phoneContent.setText("");
//	                	}
//	            }
//	        });
//
//			emailLabel = new JLabel("Email:");
//			if(tempEmployee != null)
//			{
//				emailContent = new JTextField(tempEmployee.getEmail());
//			}
//			else
//			{
//				emailContent = new JTextField();
//			}
//			emailContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String emailTemp = emailContent.getText();
//	                if(emailTemp.matches("^[A-Za-z0-9+_.-]+@(.+)$")) 
//	                {
//	                	email = emailTemp;
//	                	System.out.println("Email: " + email);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		emailContent.setText("");
//	                	}
//	            }
//	        });
//
//
//			addressLabel = new JLabel("Địa chỉ:");
//			if(tempEmployee != null)
//			{
//				addressContent = new JTextField(tempEmployee.getAddress());
//			}
//			else
//			{
//				addressContent = new JTextField();
//			}
//			addressContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String adressTemp = addressContent.getText();
//	                if(adressTemp.matches("^[A-Za-z ]+$")) 
//	                {
//	                	address = adressTemp;
//	                	System.out.println("Address: " + address);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		addressContent.setText("");
//	                	}
//	            }
//	        });
//
//			
//			userNameLabel = new JLabel("Tên tài khoản:");
//			if(tempEmployee != null)
//			{
//				userNameContent = new JTextField(tempEmployee.getUserName());
//			}
//			else
//			{
//				userNameContent = new JTextField();
//			}
//			userNameContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String userNameContentTemp = userNameContent.getText();
//	                if (userNameContentTemp.matches("^[A-Za-z0-9_.-]+$"))
//	                {
//	                	userName = userNameContentTemp;
//	                	System.out.println("Username: " + userName);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		userNameContent.setText("");
//	                	}
//	            }
//	        });
//
//			passwordLabel = new JLabel("Mật khẩu:");
//			if(tempEmployee != null)
//			{
//				passwordContent = new JTextField(tempEmployee.getPassWord());
//			}
//			else
//			{
//				passwordContent = new JTextField();
//			}
//			passwordContent.addActionListener(new ActionListener() 
//			{
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	                String passwordContentTemp = passwordContent.getText();
//	                if(passwordContentTemp.matches("^[A-Za-z\\d@$!%*#?&]+$")) 
//	                {
//	                	password = passwordContentTemp;
//	                	System.out.println("Username: " + password);
//	                }
//	                else
//	                	{
//	                		System.out.println("Invalid input");
//	                		passwordContent.setText("");
//	                	}
//	            }
//	        });
//
//			permissionsLabel = new JLabel("Quyền hạn:");
//			permissionsContent =  new JComboBox<>();
//			permissionsContent.addItem("Nhân viên");
//			permissionsContent.addItem("Admin");
//			if(tempEmployee != null)
//			{
//				if(tempEmployee.getPermissions().equals(Employee.PERMISSION_ADMIN)) permissionsContent.setSelectedItem("Admin");
//				else if(tempEmployee.getPermissions().equals(Employee.PERMISSION_STAFF)) permissionsContent.setSelectedItem("Nhân viên");
//			}
//			
//			permissionsContent.addActionListener(new ActionListener() 
//			{
//			    @Override
//			    public void actionPerformed(ActionEvent e) {
//			        if (permissionsContent.getSelectedItem().equals("Admin")) {
//			            permissions = Employee.PERMISSION_ADMIN;
//			        } else {
//			            permissions = Employee.PERMISSION_STAFF;
//			        }
//			    }
//			});
//			
//			hireDateLabel = new JLabel("Ngày bắt đầu làm việc:");
//			if(tempEmployee != null)
//			{
//				hireDateContent = new JTextField(tempEmployee.getDateHired().toString());
//			}
//			else
//			{
//				hireDateContent = new JTextField();
//			}
//			
//			hireDateContent.addActionListener(new ActionListener() 
//			{
//			    @Override
//			    public void actionPerformed(ActionEvent e) 
//			    {
//			    	String hireDateString = hireDateContent.getText();
//			        if (hireDateString.matches("^\\d{4}-\\d{2}-\\d{2}$")) 
//			        {
//			        	String[] dateParts = hireDateString.split("-");
//			        	
//			        	int year = Integer.parseInt(dateParts[0]);
//			        	int month = Integer.parseInt(dateParts[1]);
//			        	int day = Integer.parseInt(dateParts[2]);
//
//			        	
//			            LocalDate dateHire = LocalDate.of(year, month, day);
//			            
//			            System.out.println("Hire Date: " + dateHire);
//			       
//			        }
//			    }
//			});
//			
//			statusLabel = new JLabel("Quyền hạn:");
//			statusContent =  new JComboBox<>();
//			statusContent.addItem("Đang làm việc");
//			statusContent.addItem("Đã nghỉ");
//			if(tempEmployee != null)
//			{
//				if(tempEmployee.getStatus().equals(Employee.STATUS_WORKING)) statusContent.setSelectedItem("Đang làm việc");
//				else if(tempEmployee.getStatus().equals(Employee.STATUS_RESIGN)) statusContent.setSelectedItem("Đã nghỉ");
//			}
//			
//			statusContent.addActionListener(new ActionListener() 
//			{
//			    @Override
//			    public void actionPerformed(ActionEvent e) {
//			        if (statusContent.getSelectedItem().equals("Đang làm việc")) {
//			            status = Employee.STATUS_WORKING;
//			        } else {
//			            status = Employee.STATUS_RESIGN;
//			        }
//			    }
//			});
//
//			updateDialog.add(employeeIdLabel);
//			updateDialog.add(employeeIdContent);
//			
//			updateDialog.add(fullNameLabel);
//			updateDialog.add(fullNameContent);
//
//			updateDialog.add(phoneLabel);
//			updateDialog.add(phoneContent);
//
//			updateDialog.add(emailLabel);
//			updateDialog.add(emailContent);
//
//			updateDialog.add(addressLabel);
//			updateDialog.add(addressContent);
//
//			updateDialog.add(userNameLabel);
//			updateDialog.add(userNameContent);
//
//			updateDialog.add(passwordLabel);
//			updateDialog.add(passwordContent);
//
//			updateDialog.add(permissionsLabel);
//			updateDialog.add(permissionsContent);
//			
//			updateDialog.add(hireDateLabel);
//			updateDialog.add(hireDateContent);
//			
//			updateDialog.add(statusLabel);
//			updateDialog.add(statusContent);
//			
//			
//			
//			JButton saveButton = new JButton("Lưu");
//	        JButton cancelButton = new JButton("Hủy");
//
//	        // Action for the Save button
//	        saveButton.addActionListener(new ActionListener() {
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	            	
//	            	
//	            	employeeDAO.update(tempEmployee);
//	            		                
//	            	System.out.println("Update Completed");
//	            	updateDialog.dispose(); // Close the dialog after saving
//	            }
//	        });
//
//	        // Action for the Cancel button
//	        cancelButton.addActionListener(new ActionListener() {
//	            @Override
//	            public void actionPerformed(ActionEvent e) 
//	            {
//	            	fullName= null;
//	        		phone =null;
//	        		email= null;
//	        		address=null;
//	        		userName=null;
//	        		password=null;
//	        		permissions=null;
//	        		hireDate = null;
//	        		status=null;
//	        		
//	        		updateDialog.dispose(); 
//	            }
//	        });
//
//	        // Add buttons to the dialog
//	        updateDialog.add(saveButton);
//	        updateDialog.add(cancelButton);
//
//	        // Set dialog properties
//	        updateDialog.setSize(400, 500); // Set a reasonable size for the dialog
//	        updateDialog.setLocationRelativeTo(null); // Center the dialog on the screen
//	        updateDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	        updateDialog.setVisible(true); // Show the dialog
//			
//			
//			
//		}
//		
//				
//	
//	}
	


}
