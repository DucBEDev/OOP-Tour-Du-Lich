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
	private JPasswordField passwordContent;
	private JComboBox<String> permissionsContent;
	
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private String userName;
	private String password;
	private String permissions;
	
	private int totalPages = 1;
	private int currentPage = 1;
	private  int rowPerPage = 10;
	
	private JPanel employeeListPanel;
	private JPanel functionButtonPanel;
	private JPanel pageControlButtonPanel;
	
	private JTextField searchTextField;
	
	private JLabel pageNumber;
	
	private Employee_DAO employeeDAO = new Employee_DAO();
	
	private ArrayList<Employee> employees = new ArrayList<>();
	
	
	public EmployeeManagement()
	{
		setLayout(new BorderLayout());
		
		employeeListPanel = new JPanel();
		employeeListPanel.setLayout(new GridLayout(10,1));
		
		
		pageControlButtonPanel= new JPanel();
		pageControlButtonPanel.setBackground(Color.WHITE);
		pageControlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,15, 15));
		
		functionButtonPanel = new JPanel();
		functionButtonPanel.setBackground(Color.WHITE);
		functionButtonPanel.setLayout(new BorderLayout());
		
		
		pageNumber = new JLabel("Page: " + currentPage);
		
		JButton addEmployee = new JButton("Thêm");
		AddEmployeeFrame actionListenerAdd = new AddEmployeeFrame();
		addEmployee.addActionListener(actionListenerAdd);
		
		
		JButton previousPage = new JButton("<");
		previousPage.addActionListener(evt->previousPagePanel(evt));
		
		JButton nextPage = new JButton(">");
		nextPage.addActionListener(evt->NextPagePanel(evt));
		
		
		searchTextField = new JTextField("Nhập mã nhân viên để tìm");
		searchTextField.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		searchTextField.setText("");
        	}
        });
		searchTextField.addActionListener(evt->
		{
			String tempEmployeeId = searchTextField.getText();
			if(employeeDAO.checkExistById(tempEmployeeId))
			{
				employeeListPanel.removeAll();
	            JPanel row = CreateEmployeeRow(employeeDAO.getById(tempEmployeeId),0);
	            employeeListPanel.add(row);		
	            employeeListPanel.revalidate();
	            employeeListPanel.repaint();
			}
			else if(tempEmployeeId.equals(""))
			{
				loadEmployeeData();
			}
			else 
			{
				JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào có mã " + tempEmployeeId,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		functionButtonPanel.add(addEmployee, BorderLayout.EAST);
		functionButtonPanel.add(searchTextField, BorderLayout.WEST);

//		pageControlButtonPanel.add(addEmployee);
		
		pageControlButtonPanel.add(previousPage);
		pageControlButtonPanel.add(pageNumber);
		pageControlButtonPanel.add(nextPage);
	
        JScrollPane scrollBar = new JScrollPane(employeeListPanel);
       
        add(functionButtonPanel, BorderLayout.NORTH);
        add(scrollBar, BorderLayout.CENTER);
        add(pageControlButtonPanel, BorderLayout.SOUTH);
        
        
        loadEmployeeData();
	}
	
	// Hiện thông tin khách hàng
		private void loadEmployeeData() 
		{
		    // Lấy danh sách khách hàng
			employees = employeeDAO.getAll();
			totalPages = (int) Math.ceil((double) employees.size() / rowPerPage);
			updatePage(); // Chỉ tải dữ liệu của trang đầu tiên
		}
		
		
		// Cập nhật UI ở trang hiện tại
		private void updatePage() 
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
			    JPanel wrapperPanel = new JPanel(new GridLayout(1, 1));
			    wrapperPanel.setBackground(Color.white);


			    JPanel row = new JPanel(new GridLayout(1, 4)); // 4 cột: customerId, fullName, phone, email
			    row.setBackground( Color.white );
			    row.setBorder(BorderFactory.createLineBorder(Color.black));
			    
			    JLabel imageLabel = new JLabel();
			    imageLabel.setIcon(new ImageIcon("C:\\Users\\longh\\eclipse-workspace\\OOP-Test\\src\\images\\person(1).jpg"));
			    JLabel idLabel = new JLabel("ID nhân viên: "+employee.getEmployeeId());
			    JLabel nameLabel = new JLabel("Họ và tên: "+employee.getFullName());
			    JLabel phoneLabel = new JLabel("Số điện thoại: "+employee.getPhone());
			    JLabel emailLabel = new JLabel("Email: "+employee.getEmail());
			    JLabel permissionLabel = new JLabel("Quyền hạn: "+employee.getPermissions());

			    // Căn chỉnh văn bản
			    idLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    permissionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    
			    JPanel column2 = new JPanel(new GridLayout(2, 1));
			    column2.setBackground( Color.white );
			    column2.add(idLabel);
			    column2.add(nameLabel);
			    
			    JPanel column3 = new JPanel(new GridLayout(2, 1));
			    column3.setBackground( Color.white );
			    column3.add(phoneLabel);
			    column3.add(emailLabel);
			    
//			    JPanel column4 = new JPanel(new GridLayout(2, 1));

			    row.add(imageLabel);
			    row.add(column2);
			    row.add(column3);
//			    row.add(permissionLabel);
//			    row.add(nameLabel); 
			    row.add(permissionLabel);
			    
			    wrapperPanel.add(row);
			    
			    EmployeeDetailControl mouseListener = new EmployeeDetailControl(this);
			    wrapperPanel.addMouseListener(mouseListener);


			    wrapperPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			    wrapperPanel.putClientProperty("employee", employee);

			    return wrapperPanel;
			}
		
		
		// Tạo event chuyển trang trước
		private void previousPagePanel(ActionEvent e) 
		{
		    if (currentPage > 1) 
		    {
		        currentPage--;
		        updatePage();
		    }
		}
		
		
		// Tạo event chuyển trang sau
		private void NextPagePanel(ActionEvent e) 
		{
		    if (currentPage < totalPages) 
		    {
		        currentPage++;
		        updatePage();
		    }
		}
		
		
		// Sự kiện chọn và xem thông tin chi tiết
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
				JPanel row = (JPanel) tempPanel.getComponent(0);
				JPanel column2=(JPanel)row.getComponent(1);
				JPanel column3=(JPanel)row.getComponent(2);
				row.setBackground(Color.LIGHT_GRAY);
				column2.setBackground(Color.LIGHT_GRAY);
				column3.setBackground(Color.LIGHT_GRAY);

			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				JPanel tempPanel = (JPanel)e.getSource();
				JPanel row = (JPanel) tempPanel.getComponent(0);
				JPanel column2=(JPanel)row.getComponent(1);
				JPanel column3=(JPanel)row.getComponent(2);
				row.setBackground(Color.WHITE);
				column2.setBackground(Color.WHITE);
				column3.setBackground(Color.WHITE);		
			}
		}
	
	
	// UI cửa sổ thêm nhân viên
	private class AddEmployeeFrame implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			JFrame addFrame = new JFrame("Thêm thông tin nhân viên");
			addFrame.setAlwaysOnTop(true);
			addFrame.setLayout(new GridLayout(8,2,10,10));
			
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
			        } else 
			        {
			            permissions = Employee.PERMISSION_STAFF;
			        }
			    }
			});

			
			addFrame.add(fullNameLabel);
			addFrame.add(fullNameContent);

			addFrame.add(phoneLabel);
			addFrame.add(phoneContent);

			addFrame.add(emailLabel);
			addFrame.add(emailContent);

			addFrame.add(addressLabel);
			addFrame.add(addressContent);

			addFrame.add(userNameLabel);
			addFrame.add(userNameContent);

			addFrame.add(passwordLabel);
			addFrame.add(passwordContent);

			addFrame.add(permissionsLabel);
			addFrame.add(permissionsContent);
			
			JButton saveButton = new JButton("Lưu");
	        JButton cancelButton = new JButton("Hủy");

	        // Action for the Save button
	        saveButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) 
	            {
	            	if (!validateInput(addFrame)) {
	            		return;
	            	}
	            	
	            	//customerId = customerIdTextField.getText().trim();
	                fullName = fullNameContent.getText().trim();
	                phone = phoneContent.getText().trim();
	                email = emailContent.getText().trim();
	                address = addressContent.getText().trim();
	                permissions = (String) permissionsContent.getSelectedItem();
	                userName = userNameContent.getText().trim();
	                password = new String(passwordContent.getPassword()).trim();
	                
	                
	            	try {
	            		//Employee_DAO employeeDAO = new Employee_DAO();
		            	Employee temp = new Employee(employeeDAO.generateNextEmployeeId(), fullName, phone, email, address, userName, password, permissions);
		            	System.out.println(temp);
		            	if(employeeDAO.add(temp)) {
		        			JOptionPane.showMessageDialog(addFrame, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		        			loadEmployeeData();
		        			addFrame.dispose();
		        			
		        		} else {
		        			JOptionPane.showMessageDialog(addFrame, "Không thể thêm khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
		        		}
	            	} catch (Exception ex) {
	            		JOptionPane.showMessageDialog(addFrame, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	            	}
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
	        		addFrame.dispose(); 
	            }
	        });

	        // Add buttons to the dialog
	        addFrame.add(saveButton);
	        addFrame.add(cancelButton);

	        // Set dialog properties
	        addFrame.setSize(600, 600); // Set a reasonable size for the dialog
	        addFrame.setLocationRelativeTo(null); // Center the dialog on the screen
	        addFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        addFrame.setVisible(true); // Show the dialog	
		}
	}		

	
	// Kiểm tra định dạng các thông tin
		private boolean validateInput(JFrame parentFrame) 
		{
			String fullNameTemp = fullNameContent.getText().trim();
			String phoneTemp = phoneContent.getText().trim();
			String emailTemp = emailContent.getText().trim();
			String addressTemp = addressContent.getText().trim();			
			String userNameTemp = userNameContent.getText().trim();
			String passwordTemp = new String(passwordContent.getPassword()).trim();
			
			
			if (fullNameTemp.isEmpty() || !fullNameTemp.matches("^\\p{L}+(\\s+\\p{L}+)*$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Họ tên không hợp lệ! Vui lòng nhập chữ cái và không để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        fullNameContent.requestFocus();
		        return false;
		    }

		    if (phoneTemp.isEmpty() || !phoneTemp.matches("\\d{10}")) {
		        JOptionPane.showMessageDialog(parentFrame, "Số điện thoại không hợp lệ! Định dạng đúng: 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        phoneContent.requestFocus();
		        return false;
		    }

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

		    if (userNameTemp.isEmpty() || !userNameTemp.matches("^[A-Za-z0-9_.-]+$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Tên tài khoản có thể gồm các ký tự chữ cái, số và ký tự đặc biệt ( _ / . / - )", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        userNameContent.requestFocus();
		        return false;
		    }

		    if (passwordTemp.isEmpty() || !passwordTemp.matches("^[A-Za-z\\d@$!%*#?&_]+$")) {
		        JOptionPane.showMessageDialog(parentFrame, "Mật khẩu không hợp lệ! Không để trống và chỉ có thể chứa các ký tự chữ cái, số và ký tự đặc biệt ( @ / $ / ! / % / * / # / ? / & / _ )", "Lỗi", JOptionPane.ERROR_MESSAGE);
		        passwordContent.requestFocus();
		        return false;
		    }

		    // Tất cả dữ liệu hợp lệ
		    return true;
		}	

}
