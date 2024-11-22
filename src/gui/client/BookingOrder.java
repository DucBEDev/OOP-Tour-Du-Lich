package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.Customer_DAO;
import dao.Order_DAO;
import entity.Customer;
import entity.Order;
import entity.Tour;

public class BookingOrder extends JFrame {
	private JPanel pnlMain;
	private JSplitPane splitPane;
	private JPanel pnlTourList;
	private JPanel pnlForm;
	private JTextField txtFullName;
	private JTextField txtPhone;
	private JTextField txtEmail;
	private JTextField txtAddress;
	private JSpinner adultSpinner;
    private JSpinner childSpinner;
    private JLabel lblTotalPrice;
	
	private Tour selectedTour;
	private Order_DAO order_dao;
	private Customer_DAO customer_dao;
	
	public BookingOrder(ArrayList<Tour> tourList) {
		setTitle("Đặt Tour Du Lịch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    	
    	init(tourList);
	}

	private void init(ArrayList<Tour> tourList) {
		pnlMain = new JPanel(new BorderLayout());
		
		// Split pane into 2 components
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.9);
		
		// Left component
		pnlTourList = createTourList(tourList);
		splitPane.setLeftComponent(pnlTourList);
		
		// Right component
		pnlForm = createForm();
		splitPane.setRightComponent(pnlForm);
		
		pnlMain.add(splitPane, BorderLayout.CENTER);
		add(pnlMain);
	}

	private JPanel createForm() {
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
		pnlFormInput.add(new JLabel("Họ và Tên: "), gbc);
		gbc.gridx = 1;
		txtFullName = new JTextField();
		txtFullName.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtFullName, gbc);
		
		gbc.gridx = 0; gbc.gridy = 1;
		pnlFormInput.add(new JLabel("Số điện thoại: "), gbc);
		gbc.gridx = 1;
		txtPhone = new JTextField(20);
		txtPhone.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtPhone, gbc);
		
		gbc.gridx = 0; gbc.gridy = 2;
		pnlFormInput.add(new JLabel("Email: "), gbc);
		gbc.gridx = 1;
		txtEmail = new JTextField(20);
		txtEmail.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtEmail, gbc);
		
		gbc.gridx = 0; gbc.gridy = 3;
		pnlFormInput.add(new JLabel("Địa chỉ: "), gbc);
		gbc.gridx = 1;
		txtAddress = new JTextField(20);
		txtAddress.setPreferredSize(new Dimension(200, 30));
		pnlFormInput.add(txtAddress, gbc);
		
		gbc.gridx = 0; gbc.gridy = 4;
		pnlFormInput.add(new JLabel("Số người lớn:"), gbc);
		gbc.gridx = 1;
		SpinnerNumberModel adultModel = new SpinnerNumberModel(1, 1, 10, 1);
		adultSpinner = new JSpinner(adultModel);
		adultSpinner.setPreferredSize(new Dimension(200, 30));
		adultSpinner.addChangeListener(e -> updateTotalPrice());
		pnlFormInput.add(adultSpinner, gbc);
		
		gbc.gridx = 0; gbc.gridy = 5;
		pnlFormInput.add(new JLabel("Số trẻ em:"), gbc);
		gbc.gridx = 1;
		SpinnerNumberModel childModel = new SpinnerNumberModel(1, 1, 10, 1);
		childSpinner = new JSpinner(childModel);
		childSpinner.setPreferredSize(new Dimension(200, 30));
		childSpinner.addChangeListener(e -> updateTotalPrice());
		pnlFormInput.add(childSpinner, gbc);
		
		gbc.gridx = 0; gbc.gridy = 6;
		pnlFormInput.add(new JLabel("Tổng tiền: "), gbc);
		gbc.gridx = 1;
		lblTotalPrice = new JLabel("0 VNĐ");
		lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotalPrice.setForeground(new Color(255, 153, 0));
		pnlFormInput.add(lblTotalPrice, gbc);
		
		gbc.gridx = 0; gbc.gridy = 7;
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

	private JPanel createTourList(ArrayList<Tour> tourList) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panel chứa nút home và tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

        // Tạo nút home
        JButton homeButton = new JButton("Trang chủ");
        homeButton.setPreferredSize(new Dimension(100, 30));
        homeButton.setMaximumSize(new Dimension(100, 30));
        homeButton.setBackground(new Color(0, 0, 139)); 
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
        	Dashboard dashboard = new Dashboard();
        	dashboard.setVisible(true);
        	this.dispose();
        });

        // Header label
        JLabel headerLabel = new JLabel("Danh sách Tour Du lịch");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm các components vào headerPanel
        headerPanel.add(homeButton);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Tạo khoảng cách
        headerPanel.add(headerLabel);
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Thêm headerPanel vào panel chính
        panel.add(headerPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Container for tour cards with GridBagLayout
        JPanel cardsContainer = new JPanel(new GridBagLayout());
        cardsContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Configure constraints for the cards
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Get screen dimensions
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension scrSize = kit.getScreenSize();
        int scrWidth = scrSize.width;
        int scrHeight = scrSize.height;
        // Calculate card width based on screen size
        int cardWidth = (scrWidth * 69 / 100) / 2;
        
        // Add cards to container
        int column = 0;
        for (Tour tour : tourList) {
            // Create card wrapper
        	JPanel cardWrapper = new JPanel();
            cardWrapper.setPreferredSize(new Dimension(cardWidth - 20, 300));
            cardWrapper.setMaximumSize(new Dimension(cardWidth - 20, 300));
            cardWrapper.setLayout(new BorderLayout());
            cardWrapper.add(createTourCard(tour), BorderLayout.CENTER);
            
            // Set grid position
            gbc.gridx = column;
            cardsContainer.add(cardWrapper, gbc);
            
            // Move to next position
            column++;
            if (column > 1) {  // After 2 columns
                column = 0;    // Reset to first column
                gbc.gridy++;   // Move to next row
            }
        }
        
        // Add filler to maintain alignment
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        cardsContainer.add(Box.createGlue(), gbc);
        
        // Scroll pane for cards container
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(scrWidth * 80 / 100, scrHeight));
        scrollPane.getVerticalScrollBar().setUnitIncrement(26);
        
        // Add scroll pane and vertical glue
        panel.add(scrollPane);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }

	private JPanel createTourCard(Tour tour) {
		JPanel pnlCard = new JPanel(new BorderLayout(10, 0));
		pnlCard.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.LIGHT_GRAY),
			new EmptyBorder(10, 10, 10, 10)
		));
		pnlCard.setBackground(Color.WHITE);
		
		// Left panel
		JPanel pnlLeft = new JPanel();
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBackground(Color.WHITE);
		
		// Images panel
		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/background.jpg"));
		Image scaledImage = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
		JLabel lblImage = new JLabel(new ImageIcon(scaledImage));
		lblImage.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlLeft.add(lblImage);
		
		// Tour info
		JPanel pnlInfo = new JPanel();
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.setBackground(Color.WHITE);
		
		JLabel lblDuration = new JLabel(String.format("- Thời gian: %d ngày %d đêm", tour.getDuration(), tour.getDuration() - 1));
		lblDuration.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblTransport = new JLabel("- Phương tiện: " + tour.getTransportInfo());
		lblTransport.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel pnlPrice = new JPanel(new GridLayout(2, 1));
		pnlPrice.setBackground(Color.WHITE);
		
		NumberFormat formatVN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
		JLabel lblAdultPrice = new JLabel("- Người lớn: " + String.valueOf(formatVN.format(tour.getAdultPrice().intValue())) + "/ Vé");
		JLabel lblChildPrice = new JLabel("- Trẻ em: " + String.valueOf(formatVN.format(tour.getChildPrice().intValue())) + "/ Vé");
		
		pnlPrice.add(lblAdultPrice);
		pnlPrice.add(lblChildPrice);
		pnlPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		pnlInfo.add(Box.createVerticalStrut(10));
		pnlInfo.add(lblDuration);
		pnlInfo.add(lblTransport);
		pnlInfo.add(Box.createVerticalStrut(10));
		pnlInfo.add(pnlPrice);
		pnlLeft.add(pnlInfo);
		
		// Right panel
		JPanel pnlRight = new JPanel();
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBackground(Color.WHITE);
		pnlRight.setBorder(new EmptyBorder(0, 10, 0, 0));
		
		JLabel lblTourName = new JLabel(tour.getTourName());
		lblTourName.setFont(new Font("Arial", Font.BOLD, 18));
		lblTourName.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JTextArea txtaDescription = new JTextArea(tour.getDescription());
		txtaDescription.setWrapStyleWord(true);
		txtaDescription.setLineWrap(true);
		txtaDescription.setEditable(false);
		txtaDescription.setBackground(Color.WHITE);
		txtaDescription.setFont(new Font("Arial", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(txtaDescription);
		scrollPane.setPreferredSize(new Dimension(300, 150));
		scrollPane.setBorder(null);
		
		pnlRight.add(lblTourName);
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(scrollPane);
		
		// Booking button
		JButton btnBook = new JButton("Đặt tour");
		btnBook.setFont(new Font("Arial", Font.BOLD, 16));
		btnBook.setForeground(Color.WHITE);
		btnBook.setBackground(new Color(0, 0, 139));
		btnBook.setBorderPainted(false);
        btnBook.setFocusPainted(false);
        btnBook.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBook.addActionListener(e -> {
			selectedTour = tour;
			updateForm();
		});
		btnBook.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		pnlRight.add(Box.createVerticalStrut(10));
		pnlRight.add(btnBook);
		
		pnlCard.add(pnlLeft, BorderLayout.WEST);
		pnlCard.add(pnlRight, BorderLayout.CENTER);
		
		// Add hover effect
		pnlCard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	pnlCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                    new EmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedTour != tour) {
                	pnlCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }
            }
        });
		
		return pnlCard;
	}
	
	private void handleBooking() {
		String fullName = txtFullName.getText().trim();
		String email = txtEmail.getText().trim();
		String phone = txtPhone.getText().trim();
		String address = txtAddress.getText().trim();
		int adultTickets = (int)adultSpinner.getValue();
		int childTickets = (int)childSpinner.getValue();
		
		if (selectedTour == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tour trước khi đặt", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate form
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
	        Customer customer = new Customer(customer_id, fullName, phone, email, address, "Khách", "user_name", "1");
	        customer_dao.add(customer);	
        }
        else {
        	Customer customer = customer_dao.getByPhone(phone);
        	customer_id = customer.getCustomerId();
        }
        BigDecimal totalPrice = selectedTour.getAdultPrice().multiply(BigDecimal.valueOf(adultTickets)).add(selectedTour.getChildPrice().multiply(BigDecimal.valueOf(childTickets)));
        Order order = new Order(order_dao.generateNextOrderId(), customer_id, selectedTour.getTourId(), adultTickets, childTickets, LocalDateTime.now(), totalPrice, "Chờ thanh toán", null);
        order_dao.add(order);
        JOptionPane.showMessageDialog(this, "Đặt tour thành công!\nChúng tôi sẽ liên hệ với bạn sớm nhất.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
        // Reset form
        selectedTour = null;
        updateForm();
	}

	private void updateTotalPrice() {
		if (selectedTour != null) {
			int numAdults = (Integer) adultSpinner.getValue();
			int numChilds = (Integer) childSpinner.getValue();
			
			double totalPrice = (numAdults * selectedTour.getAdultPrice().intValue()) + (numChilds * selectedTour.getChildPrice().intValue());
			NumberFormat formatVN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
			lblTotalPrice.setText(formatVN.format(totalPrice));
		}
	}

	private void updateForm() {
		if (selectedTour != null) {
            updateTotalPrice();
            // Reset form fields
            txtFullName.setText("");
            txtEmail.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            adultSpinner.setValue(1);
            childSpinner.setValue(0);
        }
	}
}

