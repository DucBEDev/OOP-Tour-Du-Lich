package gui.admin;

import java.awt.BorderLayout;


import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
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
import javax.swing.ImageIcon;
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
import entity.Employee;

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
        
        JTextField searching = new JTextField("Nhập Số Điện Thoại");
        
        buttonPanel.add(searching);
        searching.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		searching.setText("");
        	}
        });
        searching.addActionListener(evt -> 
        {
            String customerPhoneTemp = searching.getText();
            if (customerDAO.checkExistByPhone(customerPhoneTemp)) 
            {
                customerPanel.removeAll();
                JPanel row = createCustomerRow(customerDAO.getByPhone(customerPhoneTemp), 0);
                customerPanel.add(row);
                customerPanel.revalidate();
                customerPanel.repaint();
            }
            else if (customerPhoneTemp.equals("")) 
            {
                loadCustomerData();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng " + customerPhoneTemp,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(previousPage);
        buttonPanel.add(pageNumber);
        buttonPanel.add(nextPage);
        buttonPanel.add(addButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Gán sự kiện các button
        addButton.addActionListener(this::addCustomerFrame);
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
            emptyRow.setBackground(Color.white);
            customerPanel.add(emptyRow);
        }

        pageNumber.setText("Page: " + currentPage);
        customerPanel.revalidate();
        customerPanel.repaint();
    }
	
	
	// Tạo dòng hiển thị thông tin trên Page
	private JPanel createCustomerRow(Customer customer, int indexInPage	) {		
		JPanel wrapperPanel = new JPanel(new GridLayout(1, 1));
	    wrapperPanel.setBackground(Color.white);

		JPanel row = new JPanel(new GridLayout(1, 4)); // 4 cột: customerId, fullName, phone, email
		row.setBackground(Color.WHITE);
		row.setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel imageLabel = new JLabel();
	    imageLabel.setIcon(new ImageIcon("C:\\Users\\ADMIN\\OOP-Tour-Du-Lich\\src\\images\\person(1).jpg"));
		JLabel idLabel = new JLabel("ID khách hàng: "+customer.getCustomerId());
		JLabel nameLabel = new JLabel("Họ và tên: "+customer.getFullName());
		JLabel phoneLabel = new JLabel("Số điện thoại: " +customer.getPhone());
		JLabel emailLabel = new JLabel("Email: "+customer.getEmail());
		JLabel statusLabel = new JLabel("Trạng thái: "+customer.getStatus());

		// Căn chỉnh văn bản
		idLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		phoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		

		JPanel column2 = new JPanel(new GridLayout(2, 1));
	    column2.setBackground( Color.white );
	    column2.add(idLabel);
	    column2.add(nameLabel);
	    
	    JPanel column3 = new JPanel(new GridLayout(2, 1));
	    column3.setBackground( Color.white );
	    column3.add(phoneLabel);
	    column3.add(emailLabel);


	    row.add(imageLabel);
	    row.add(column2);
	    row.add(column3); 
	    row.add(statusLabel);
	    
	    wrapperPanel.add(row);
	    
	    CustomerDetailControl mouseListener = new CustomerDetailControl(this);
	    wrapperPanel.addMouseListener(mouseListener);
	    wrapperPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	    wrapperPanel.putClientProperty("customer", customer);

	    return wrapperPanel;
	}
		
		
	// Sự kiện chuyển panel khi bấm vào thông tin khách hàng
	private class CustomerDetailControl extends MouseAdapter
	{
		private CustomerManagement customerManagement;
		
		public CustomerDetailControl(CustomerManagement customerManagement)
		{
			this.customerManagement=customerManagement;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			JPanel sourcePanel = (JPanel)e.getSource();
			Customer customer = (Customer)sourcePanel.getClientProperty("customer");
			
			customerManagement.removeAll();
			CustomerDetail customerDetail = new CustomerDetail(customer);
			customerManagement.add(customerDetail);
			customerManagement.revalidate();
			customerManagement.repaint();
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


	// Thêm khách hàng
	private void addCustomerFrame(ActionEvent e) {
		JFrame addFrame = new JFrame("Thêm thông tin khách hàng");
		addFrame.setLayout(new GridLayout(8, 2, 10, 10));
		addFrame.setSize(600, 600);
        
		
        fullNameTextField = new JTextField();
        phoneTextField = new JTextField();
        emailTextField = new JTextField();
        addressTextField = new JTextField();
        statusBox = new JComboBox<>(new String[]{Customer.STATUS_ACTIVE, Customer.STATUS_INACTIVE});
        userNameTextField = new JTextField();
        passwordTextField = new JPasswordField();
        

        fullNameLabel = new JLabel("Họ tên:");
        phoneLabel = new JLabel("Số điện thoại:");
        emailLabel = new JLabel("Email:");
        addressLabel = new JLabel("Địa chỉ:");
        statusLabel = new JLabel("Trạng thái:");
        userNameLabel = new JLabel("Tên tài khoản:");
        passwordLabel = new JLabel("Mật khẩu:");
       
        
        addFrame.add(fullNameLabel);
        addFrame.add(fullNameTextField);
        
        addFrame.add(phoneLabel);
        addFrame.add(phoneTextField);
        
        addFrame.add(emailLabel);
        addFrame.add(emailTextField);
        
        addFrame.add(addressLabel);
        addFrame.add(addressTextField);
        
        addFrame.add(statusLabel);
        addFrame.add(statusBox);
        
        addFrame.add(userNameLabel);
        addFrame.add(userNameTextField);
        
        addFrame.add(passwordLabel);
        addFrame.add(passwordTextField);
        
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        addFrame.add(saveButton);
        addFrame.add(cancelButton);
        
        saveButton.addActionListener(ev -> {
        	if (!validateInput()) {
        		return;
        	}
        	
        	//customerId = customerIdTextField.getText().trim();
            fullName = fullNameTextField.getText().trim();
            phone = phoneTextField.getText().trim();
            email = emailTextField.getText().trim();
            address = addressTextField.getText().trim();
            status = (String) statusBox.getSelectedItem();
            userName = userNameTextField.getText().trim();
            password = new String(passwordTextField.getPassword()).trim();
        	
        	
        	try {
        		Customer customer = new Customer(customerDAO.generateNextCustomerId(), fullName, phone, email, address, status, userName, password);
        		if(customerDAO.add(customer)) {
        			JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        			loadCustomerData();
        			addFrame.dispose();
        			
        		} else {
        			JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
        		}
        	} catch (Exception ex) {
        		JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        	}
        });
        
        cancelButton.addActionListener(ev -> addFrame.dispose());
        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
	}
	
	
	// Kiểm tra định dạng các thông tin
	private boolean validateInput() {
		String fullNameTemp = fullNameTextField.getText().trim();
		String phoneTemp = phoneTextField.getText().trim();
		String emailTemp = emailTextField.getText().trim();
		String addressTemp = addressTextField.getText().trim();			
		String userNameTemp = userNameTextField.getText().trim();
		String passwordTemp = new String(passwordTextField.getPassword()).trim();
		
		
		if (fullNameTemp.isEmpty() || !fullNameTemp.matches("^\\p{L}+(\\s+\\p{L}+)*$")) {
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
	        JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ! Không để trống và chỉ có thể chứa các ký tự chữ cái, số và ký tự đặc biệt ( @ / $ / ! / % / * / # / ? / & / _ )", "Lỗi", JOptionPane.ERROR_MESSAGE);
	        passwordTextField.requestFocus();
	        return false;
	    }

	    // Tất cả dữ liệu hợp lệ
	    return true;
	}
}