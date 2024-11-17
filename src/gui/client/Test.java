package gui.client;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;

import dao.Tour_DAO;
import entity.Tour;

public class Test extends JFrame {
    private JPanel mainPanel;
    private JPanel tourListPanel;
    private JPanel bookingFormPanel;
    private Tour_DAO tour_dao;
    private Tour selectedTour;
    
    // Form components
    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JSpinner adultSpinner;
    private JSpinner childSpinner;
    private JLabel lblTotalPrice;
    private ArrayList<Tour> tourList;
    
    public Test() {
        setTitle("Đặt Tour Du Lịch");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Initialize components
        tour_dao = new Tour_DAO();
        tourList = tour_dao.getLimitedTours(15);
        initializeUI();
        
        setVisible(true);
    }
    
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Create split pane with proper ratio
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.9); // Set the divider to maintain 2/3 ratio
        
        // Left panel - Tour List (2/3 width)
        tourListPanel = createTourListPanel();
//        JScrollPane tourScrollPane = new JScrollPane(tourListPanel);
        splitPane.setLeftComponent(tourListPanel);
        
        // Right panel - Booking Form (1/3 width)
        bookingFormPanel = createBookingFormPanel();
//        JScrollPane formScrollPane = new JScrollPane(bookingFormPanel);
        splitPane.setRightComponent(bookingFormPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createTourListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Danh sách Tour Du lịch");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(headerLabel);
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
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        
        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        
        // Tour image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/background.jpg"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(imageLabel);
        
        // Basic tour info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel durationLabel = new JLabel(String.format("Thời gian: %d ngày %d đêm", 
            tour.getDuration(), tour.getDuration() - 1));
        durationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel transportLabel = new JLabel("Phương tiện: " + tour.getTransportInfo());
        transportLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Price panel
        JPanel pricePanel = new JPanel(new GridLayout(2, 1));
        pricePanel.setBackground(Color.WHITE);
        
        NumberFormat formatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        JLabel adultPriceLabel = new JLabel("Người lớn: " + formatVN.format(tour.getAdultPrice()));
        JLabel childPriceLabel = new JLabel("Trẻ em: " + formatVN.format(tour.getChildPrice()));
        
        pricePanel.add(adultPriceLabel);
        pricePanel.add(childPriceLabel);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(durationLabel);
        infoPanel.add(transportLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(pricePanel);
        leftPanel.add(infoPanel);
        
        // Right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        // Tour name as header
        JLabel nameLabel = new JLabel(tour.getTourName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tour description in a scrollable text area
        JTextArea descriptionArea = new JTextArea(tour.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        scrollPane.setBorder(null);
        
        rightPanel.add(nameLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(scrollPane);
        
        // Book button at bottom of right panel
        JButton bookButton = new JButton("Đặt Tour");
        bookButton.setBackground(new Color(0, 102, 204));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> {
            selectedTour = tour;
            updateBookingForm();
        });
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(bookButton);
        
        // Add panels to card
        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                    new EmptyBorder(10, 10, 10, 10)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedTour != tour) {
                    card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        new EmptyBorder(10, 10, 10, 10)
                    ));
                }
            }
        });
        
        return card;
    }
    
    private JPanel createBookingFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Thông tin đặt Tour");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Full name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Họ tên:"), gbc);
        
        gbc.gridx = 1;
        txtFullName = new JTextField(20);
        formPanel.add(txtFullName, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);
        
        // Phone
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);
        
        // Address
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        
        gbc.gridx = 1;
        txtAddress = new JTextField(20);
        formPanel.add(txtAddress, gbc);
        
        // Number of adults
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Số người lớn:"), gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel adultModel = new SpinnerNumberModel(1, 1, 10, 1);
        adultSpinner = new JSpinner(adultModel);
        adultSpinner.addChangeListener(e -> updateTotalPrice());
        formPanel.add(adultSpinner, gbc);
        
        // Number of children
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Số trẻ em:"), gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel childModel = new SpinnerNumberModel(0, 0, 10, 1);
        childSpinner = new JSpinner(childModel);
        childSpinner.addChangeListener(e -> updateTotalPrice());
        formPanel.add(childSpinner, gbc);
        
        // Total price
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Tổng tiền:"), gbc);
        
        gbc.gridx = 1;
        lblTotalPrice = new JLabel("0 VND");
        lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalPrice.setForeground(new Color(255, 153, 0));
        formPanel.add(lblTotalPrice, gbc);
        
        // Submit button
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton submitButton = new JButton("Xác nhận đặt Tour");
        submitButton.setBackground(new Color(0, 102, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> handleBooking());
        formPanel.add(submitButton, gbc);
        
        panel.add(formPanel);
        
        return panel;
    }
    
    private void updateBookingForm() {
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
    
    private void updateTotalPrice() {
        if (selectedTour != null) {
            int numAdults = (Integer) adultSpinner.getValue();
            int numChildren = (Integer) childSpinner.getValue();
            
            double totalPrice = (numAdults * selectedTour.getAdultPrice().intValue()) + 
                              (numChildren * selectedTour.getChildPrice().intValue());
            
            NumberFormat formatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            lblTotalPrice.setText(formatVN.format(totalPrice));
        }
    }
    
    private void handleBooking() {
        if (selectedTour == null) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn tour trước khi đặt",
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate form
        if (txtFullName.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty() ||
            txtAddress.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Vui lòng điền đầy đủ thông tin",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Here you would typically save the booking to database
        JOptionPane.showMessageDialog(this,
            "Đặt tour thành công!\nChúng tôi sẽ liên hệ với bạn sớm nhất.",
            "Thông báo",
            JOptionPane.INFORMATION_MESSAGE);
            
        // Reset form
        selectedTour = null;
        updateBookingForm();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Test();
        });
    }
}