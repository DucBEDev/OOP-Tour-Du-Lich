package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dao.Customer_DAO;
import dao.Order_DAO;
import dao.Tour_DAO;
import entity.Customer;
import entity.Order;
import entity.Tour;

public class TourDetail extends JFrame {
	public static final String STATUS_PENDING = "Chờ thanh toán";
    public static final String STATUS_PAID = "Đã thanh toán";
    public static final String STATUS_CANCELLED = "Hủy";
    public static final String STATUS_COMPLETED = "Hoàn thành";
	
	private JPanel pnlMain;
	private JPanel pnlLeft;
	private JPanel pnlRight;
	private JSplitPane splitPane;
	private JTextField txtFullName;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JTextField txtChosenTour;
	private JSpinner adultSpinner;
    private JSpinner childSpinner;
    private JLabel lblTotalPrice;
    
    private Order_DAO order_dao;
    private Tour_DAO tour_dao;
	private Customer_DAO customer_dao;
	private Tour selectedTour;
	private Customer cus;
	
	public TourDetail(Tour tour, Customer customer) {
		setTitle("Chi tiết tour");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
				
		selectedTour = tour;
		cus = customer;
		
		init();
	}
	
	private void init() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension scrSize = kit.getScreenSize();
		int scrWidth = scrSize.width;
		int scrHeight = scrSize.height;
		
		pnlMain = new JPanel(new BorderLayout());
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.9);

		pnlLeft = createLeftComponent(scrWidth, scrHeight);
		splitPane.setLeftComponent(pnlLeft);
		
		pnlRight = createRightComponent();
		splitPane.setRightComponent(pnlRight);
		
		pnlMain.add(splitPane, BorderLayout.CENTER);
		add(pnlMain);
	}

	private JPanel createRightComponent() {
		JPanel pnlMainForm = new JPanel();
		pnlMainForm.setLayout(new BoxLayout(pnlMainForm, BoxLayout.Y_AXIS));
		pnlMainForm.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Header
		JLabel lblHeader = new JLabel("Thông tin đặt tour");
		lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		pnlMainForm.add(lblHeader);
		pnlMainForm.add(Box.createVerticalStrut(20));
		
		// Form
		JPanel pnlFormInput = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		gbc.gridx = 0; gbc.gridy = 0;
		pnlFormInput.add(new JLabel("Tour đã chọn: "), gbc);
		gbc.gridx = 1;
		txtChosenTour = new JTextField();
		txtChosenTour.setEditable(false);
		txtChosenTour.setBorder(null);
		if (selectedTour != null) 
			txtChosenTour.setText(selectedTour.getTourName());
		txtChosenTour.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtChosenTour, gbc);
		
		gbc.gridx = 0; gbc.gridy = 1;
		pnlFormInput.add(new JLabel("Họ và Tên: "), gbc);
		gbc.gridx = 1;
		txtFullName = new JTextField();
		if (cus.getFullName() != null) 
			txtFullName.setText(cus.getFullName());
		txtFullName.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtFullName, gbc);
		
		gbc.gridx = 0; gbc.gridy = 2;
		pnlFormInput.add(new JLabel("Số điện thoại: "), gbc);
		gbc.gridx = 1;
		txtPhone = new JTextField(20);
		if (cus.getPhone() != null) 
			txtPhone.setText(cus.getPhone());
		txtPhone.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtPhone, gbc);
		
		gbc.gridx = 0; gbc.gridy = 3;
		pnlFormInput.add(new JLabel("Email: "), gbc);
		gbc.gridx = 1;
		txtEmail = new JTextField(20);
		if (cus.getEmail() != null) 
			txtEmail.setText(cus.getEmail());
		txtEmail.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtEmail, gbc);
		
		gbc.gridx = 0; gbc.gridy = 4;
		pnlFormInput.add(new JLabel("Địa chỉ: "), gbc);
		gbc.gridx = 1;
		txtAddress = new JTextField(20);
		if (cus.getAddress() != null) 
			txtAddress.setText(cus.getAddress());
		txtAddress.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtAddress, gbc);
		
		gbc.gridx = 0; gbc.gridy = 5;
		pnlFormInput.add(new JLabel("Số người lớn:"), gbc);
		gbc.gridx = 1;
		SpinnerNumberModel adultModel = new SpinnerNumberModel(1, 1, 10, 1);
		adultSpinner = new JSpinner(adultModel);
		adultSpinner.setPreferredSize(new Dimension(200, 30));
		adultSpinner.addChangeListener(e -> updateTotalPrice());
		pnlFormInput.add(adultSpinner, gbc);
		
		gbc.gridx = 0; gbc.gridy = 6;
		pnlFormInput.add(new JLabel("Số trẻ em:"), gbc);
		gbc.gridx = 1;
		SpinnerNumberModel childModel = new SpinnerNumberModel(0, 0, 10, 1);
		childSpinner = new JSpinner(childModel);
		childSpinner.setPreferredSize(new Dimension(200, 30));
		childSpinner.addChangeListener(e -> updateTotalPrice());
		pnlFormInput.add(childSpinner, gbc);
		
		gbc.gridx = 0; gbc.gridy = 7;
		pnlFormInput.add(new JLabel("Tổng tiền: "), gbc);
		gbc.gridx = 1;
		lblTotalPrice = new JLabel("0 VNĐ");
		lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotalPrice.setForeground(new Color(255, 153, 0));
		pnlFormInput.add(lblTotalPrice, gbc);
		
		gbc.gridx = 0; gbc.gridy = 8;
		gbc.gridwidth = 2;
		JButton btnSubmit = new JButton("Xác nhận đặt Tour");
		btnSubmit.setFont(new Font("Arial", Font.BOLD, 16));
		btnSubmit.setForeground(Color.WHITE);
		btnSubmit.setBackground(new Color(0, 0, 139));
		btnSubmit.setBorderPainted(false);
		btnSubmit.setFocusPainted(false);
		btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSubmit.addActionListener(e -> handleBooking());
		pnlFormInput.add(btnSubmit, gbc);
		
		pnlMainForm.add(pnlFormInput);
		
		return pnlMainForm;
	}

	private JPanel createLeftComponent(int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tour image
        JPanel pnlImage = new JPanel();
        pnlImage.setLayout(new BoxLayout(pnlImage, BoxLayout.PAGE_AXIS));
        pnlImage.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton homeButton = new JButton("Trang chủ");
        homeButton.setPreferredSize(new Dimension(100, 30));
        homeButton.setMaximumSize(new Dimension(100, 30));
        homeButton.setBackground(new Color(0, 0, 139)); 
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
        	Dashboard dashboard = new Dashboard(cus);
        	dashboard.setVisible(true);
        	this.dispose();
        });
        pnlBtn.add(homeButton);
        pnlImage.add(pnlBtn);

        JLabel lblTourName = new JLabel(selectedTour.getTourName());
        lblTourName.setFont(new Font("Arial", Font.BOLD, 24));
        lblTourName.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlImage.add(lblTourName);
        pnlImage.add(Box.createRigidArea(new Dimension(0, 20)));

        ImageIcon originalIcon = new ImageIcon(selectedTour.getImage());
        Image scaledImage = originalIcon.getImage().getScaledInstance(width * 70 / 100, height * 40 / 100, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT); 
        pnlImage.add(lblImage);
        
        // Tour detail
        JPanel pnlDetail = new JPanel(new GridLayout(0, 1, 10, 5));
        TitledBorder titleBorder1 = BorderFactory.createTitledBorder("Mô tả tour");
        Font titleFont = new Font("Arial", Font.PLAIN, 16);
        titleBorder1.setTitleFont(titleFont);
        pnlDetail.setBorder(titleBorder1);
        
        String[] inclusions = {"Khách sạn 3-4*", "Bữa ăn", "Vé tham quan", "Hướng dẫn viên", "Bảo hiểm du lịch"};
        for (String inclusion : inclusions) {
            JLabel label = new JLabel("- " + inclusion);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            pnlDetail.add(label);
        }
        
        // Tour description
        JPanel pnlDescription = new JPanel(new GridLayout(0, 2, 10, 5));
        TitledBorder titleBorder = BorderFactory.createTitledBorder("Mô tả Tour");
        titleBorder.setTitleFont(titleFont);
        pnlDescription.setBorder(titleBorder);
        
        JLabel lblDescription = new JLabel("- " + selectedTour.getDescription());
        lblDescription.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblDescription);
        
        JLabel lblDuration = new JLabel(String.valueOf(String.format("- Thời gian: %d ngày %d đêm", selectedTour.getDuration(), selectedTour.getDuration() - 1)));
        lblDuration.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblDuration);
        
        JLabel lblTransportInfo = new JLabel("- Phương tiện di chuyển: " + selectedTour.getTransportInfo());
        lblTransportInfo.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblTransportInfo);
        
        JLabel lblDepartureLocation = new JLabel(String.valueOf("- Địa điểm khởi hành: " + selectedTour.getDepartureLocation()));
        lblDepartureLocation.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblDepartureLocation);
                
        JLabel lblDepartureDate = new JLabel(String.valueOf("- Thời gian khởi hành: " + selectedTour.getDepartureDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        lblDepartureDate.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblDepartureDate);
        
        JLabel lblMaxParticipants = new JLabel(String.valueOf("- Số vé: " + selectedTour.getMaxParticipants() + " vé"));
        lblMaxParticipants.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblMaxParticipants);
        
        JLabel lblCurrentParticipants = new JLabel(String.valueOf("- Số vé đã bán: " + selectedTour.getCurrentParticipants() + " vé"));
        lblCurrentParticipants.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblCurrentParticipants);
        
        NumberFormat formatVN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
        JLabel lblAdultPrice = new JLabel("- Người lớn: " + String.valueOf(formatVN.format(selectedTour.getAdultPrice())) + "/ Vé");
        lblAdultPrice.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblAdultPrice);
        
        JLabel lblChildPrice = new JLabel("- Trẻ em: " + String.valueOf(formatVN.format(selectedTour.getChildPrice())) + "/ Vé");
        lblChildPrice.setFont(new Font("Arial", Font.PLAIN, 16));
        pnlDescription.add(lblChildPrice);
        
        
        panel.add(pnlImage, BorderLayout.NORTH);
        panel.add(pnlDescription, BorderLayout.CENTER);
        panel.add(pnlDetail, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void handleBooking() {
    	// Validate form
    	Customer testInputCus = new Customer();
    	String fullName = txtFullName.getText().trim();
		
		String email;
		try {
			email = txtEmail.getText().trim();
			testInputCus.setEmail(email);
		} catch (Exception e)  {
			JOptionPane.showMessageDialog(this, "Thông tin đăng nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String phone;
		try {
			phone = txtPhone.getText().trim();
			testInputCus.setPhone(phone);
		} catch (Exception e)  {
			JOptionPane.showMessageDialog(this, "Thông tin đăng nhập không hợp lệ", "Thông báo", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String address = txtAddress.getText().trim();
		int adultTickets = (int)adultSpinner.getValue();
		int childTickets = (int)childSpinner.getValue();
		
		int totalTickets = adultTickets + childTickets;
		if (totalTickets > (selectedTour.getMaxParticipants() - selectedTour.getCurrentParticipants())) {
			JOptionPane.showMessageDialog(this, "Không được đặt quá số vé giới hạn", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
		}
        
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Here you would typically save the booking to database
        customer_dao = new Customer_DAO();
        order_dao = new Order_DAO();
        String customer_id;
        if (!customer_dao.checkExistByPhone(phone)) {
        	customer_id = customer_dao.generateNextCustomerId();
	        Customer customer = new Customer(customer_id, fullName, phone, email, address, "Khách", "user_name" + phone, "1");
	        customer_dao.add(customer);	
        }
        else {
        	Customer customer = customer_dao.getByPhone(phone);
        	customer_id = customer.getCustomerId();
        }
        
        double totalPrice = selectedTour.getAdultPrice() * adultTickets + selectedTour.getChildPrice() * childTickets;
        Order order = new Order(order_dao.generateNextOrderId(), customer_id, selectedTour.getTourId(), adultTickets, childTickets, LocalDateTime.now(), totalPrice, STATUS_PENDING, null);
        order_dao.add(order);
        
        // Update current participations
        selectedTour.setCurrentParticipants(selectedTour.getCurrentParticipants() + totalTickets);
        tour_dao = new Tour_DAO();
        tour_dao.update(selectedTour, null);
        
        JOptionPane.showMessageDialog(this, "Đặt tour thành công!\nChúng tôi sẽ liên hệ với bạn sớm nhất.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
        // Back to dashboard
        Dashboard dashboard = new Dashboard(cus);
    	dashboard.setVisible(true);
    	this.dispose();
	}

	private void updateTotalPrice() {
		if (selectedTour != null) {
			int numAdults = (Integer) adultSpinner.getValue();
			int numChilds = (Integer) childSpinner.getValue();
			
			double totalPrice = (numAdults * selectedTour.getAdultPrice()) + (numChilds * selectedTour.getChildPrice());
			NumberFormat formatVN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
			lblTotalPrice.setText(formatVN.format(totalPrice));
		}
	}
}
