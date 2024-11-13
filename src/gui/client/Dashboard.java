package gui.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

public class Dashboard extends JFrame {
	private JPanel topNav;
	private JPanel content;
	private JPanel content_header;
	private JPanel tourList;
	private JScrollPane scroll;
	
    public Dashboard() {
    	Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension scrSize = kit.getScreenSize();
		int scrHeight = scrSize.height;
		int scrWidth = scrSize.width;
		setLocationByPlatform(true);
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(new BorderLayout());
    	
    	// Header
    	topNav = createTopNav();
    	add(topNav, BorderLayout.NORTH);
    	
    	// Content
    	content = new JPanel(new BorderLayout());
    	content_header = createContentHeader(scrWidth, scrHeight * 3 / 7);
    	content.add(content_header, BorderLayout.NORTH);
    	add(content, BorderLayout.CENTER);
    	
    	// List of tour
    	tourList = createResultsPanel();
    	JScrollPane scrollPane = new JScrollPane(tourList);
    	scrollPane.setPreferredSize(new Dimension(scrWidth, scrHeight * 4 / 7));
    	scrollPane.getVerticalScrollBar().setUnitIncrement(50); // Tăng tốc độ lướt theo từng đơn vị
    	scrollPane.getVerticalScrollBar().setBlockIncrement(50);
    	add(scrollPane, BorderLayout.SOUTH);
    }
    
    public JPanel createTopNav() {
    	JPanel pnlNav = new JPanel(new BorderLayout());
    	pnlNav.setBackground(Color.WHITE);
    	pnlNav.setPreferredSize(new Dimension(getWidth(), 50));
    	
    	// Logo
    	JPanel pnlLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
    	pnlLeft.setBackground(Color.WHITE);
    	JLabel lblLogo = new JLabel("Booking");
    	lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
    	pnlLeft.add(lblLogo);
    	
    	// User Register
    	JPanel pnlRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
    	pnlRight.setBackground(Color.WHITE);
    	JButton btnRegister = new JButton("Đăng ký");
    	btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
    	btnRegister.setBackground(new Color(148, 218, 248));
    	btnRegister.setBorder(null);
    	btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	btnRegister.setPreferredSize(new Dimension(100, 32));
    	btnRegister.setFocusPainted(false);
    	btnRegister.addActionListener(e -> {
    		// Change to Register 
    		System.out.println("Change to Register");
    	});
    	pnlRight.add(btnRegister);
    	
    	pnlNav.add(pnlLeft, BorderLayout.WEST);
    	pnlNav.add(pnlRight, BorderLayout.EAST);
    	
    	return pnlNav;
    }
    
    public JPanel createContentHeader(int scrWidth, int scrHeight) {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setPreferredSize(new Dimension(scrWidth, scrHeight));

        // Background panel
        JPanel pnlBackground = new JPanel(new BorderLayout()) {
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        
        // Load and scale image
        ImageIcon originalIcon = new ImageIcon(Dashboard.class.getResource("/images/background.jpg"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(scrWidth, scrHeight, Image.SCALE_SMOOTH);
        JLabel lblBackground = new JLabel(new ImageIcon(scaledImage));
        pnlBackground.add(lblBackground, BorderLayout.CENTER);

        // Text panel
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        pnlContent.setOpaque(false);
        pnlContent.setBorder(BorderFactory.createEmptyBorder(50, 50, 100, 50));
        
        // Title
        JLabel lblTitle = new JLabel("Thế giới tour trong tay bạn");
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setFont(new Font("Godwit", Font.BOLD, 48));
        lblTitle.setForeground(Color.WHITE);
        pnlContent.add(lblTitle, BorderLayout.NORTH);

        // Create search form
        JPanel searchFormPanel = createSearchForm(scrWidth);
        pnlContent.add(searchFormPanel);
        
        // Using JLayeredPane to control layer between panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(scrWidth, scrHeight));
        
        // Set bounds for panels in LayeredPane
        pnlBackground.setBounds(0, 0, scrWidth, scrHeight);
        pnlContent.setBounds(0, 0, scrWidth, scrHeight);

        layeredPane.add(pnlBackground, Integer.valueOf(0));
        layeredPane.add(pnlContent, Integer.valueOf(1));

        pnlMain.add(layeredPane, BorderLayout.CENTER);

        return pnlMain;
    }

    private JPanel createSearchForm(int screenWidth) {
        // Main search panel with semi-transparent white background
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBackground(new Color(255, 255, 255, 230));
        
        // Limit size of background
        int panelWidth = (int)(screenWidth * 0.6); 
        searchPanel.setPreferredSize(new Dimension(panelWidth, 100));
        searchPanel.setMaximumSize(new Dimension(panelWidth, 100));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Search fields panel
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        int totalFields = 5;
        int buttonWidth = 50;
        int spacing = 10;
        int availableWidth = panelWidth - (2 * 20) - (buttonWidth + spacing * totalFields);
        int fieldWidth = availableWidth / totalFields;
        
        JLabel lblDepartureLocation = new JLabel("Điểm khởi hành");
        fieldsPanel.add(lblDepartureLocation, gbc);
        JLabel lblDestination = new JLabel("Điểm đến");
        fieldsPanel.add(lblDestination, gbc);
        JLabel lblDepartureTime = new JLabel("Ngày khởi hành");
        fieldsPanel.add(lblDepartureTime, gbc);
        JLabel lblTransportInfo = new JLabel("Phương tiện");
        fieldsPanel.add(lblTransportInfo, gbc);
        
        // DepartureLocation field
        JTextField txtDepartureLocation = createSearchField(fieldWidth);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        fieldsPanel.add(txtDepartureLocation, gbc);

        // Destination field
        JTextField txtDestination = createSearchField(fieldWidth);
        gbc.gridx = 1;
        fieldsPanel.add(txtDestination, gbc);

        // Depart date
        JDateChooser departDate = new JDateChooser();
        departDate.setPreferredSize(new Dimension(fieldWidth, 35));
        departDate.setDateFormatString("dd/MM/yyyy");
        departDate.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 2;
        fieldsPanel.add(departDate, gbc);

        // Return date
        String[] transportOptions = {"Xe", "Máy bay", "Tàu hỏa"};
        JComboBox<String> transportInfo = new JComboBox<>(transportOptions);
        gbc.gridx = 3;
        fieldsPanel.add(transportInfo, gbc);

        // Search button
        JButton searchButton = new JButton("Tìm");
        searchButton.setPreferredSize(new Dimension(buttonWidth, 35));
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(0, 0, 139));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> {
        	System.out.println(txtDepartureLocation.getText());
        	System.out.println(txtDestination.getText());
        	System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(departDate.getDate()));
        	System.out.println(transportInfo.getSelectedItem());
        });
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        fieldsPanel.add(searchButton, gbc);

        // Canh giữa fieldsPanel
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.add(fieldsPanel);

        JPanel centeringPanel = new JPanel();
        centeringPanel.setOpaque(false);
        centeringPanel.setLayout(new BoxLayout(centeringPanel, BoxLayout.X_AXIS));
        centeringPanel.add(Box.createHorizontalGlue());
        centeringPanel.add(searchPanel);
        centeringPanel.add(Box.createHorizontalGlue());

        return centeringPanel;
    }

    private JTextField createSearchField(int width) {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(width, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setForeground(Color.GRAY);
        field.setBackground(new Color(255, 255, 255, 200));
        return field;
    }

    private JPanel createResultsPanel() {
    	JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel tourListLabel = new JLabel("Danh sách Tour nổi bật");
        tourListLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(tourListLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(5, 3, 20, 20));
        cardsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        for (int i = 0; i < 15; i++) {
            cardsPanel.add(createResultCard("€ " + (80 - i*5)));
        }

        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createResultCard(String price) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Placeholder image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon originalIcon = new ImageIcon(Dashboard.class.getResource("/images/background.jpg"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(600, 250, Image.SCALE_SMOOTH);
        JLabel lblBackground = new JLabel(new ImageIcon(scaledImage));
        imagePanel.setPreferredSize(new Dimension(100, 250));
        imagePanel.add(lblBackground, BorderLayout.CENTER);
        card.add(imagePanel, BorderLayout.CENTER);
        
        // Price label
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Thêm các thành phần vào InfoPanel
        JLabel label = new JLabel("Tour Miền Bắc 5N4Đ: HCM - Hà Nội ");
        gbc.gridy = 1;
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        infoPanel.add(label, gbc);
        
        JLabel label1 = new JLabel("Tour Miền Bắc 5N4Đ: HCM - Hà Nội ");
        gbc.gridy = 2;
        label1.setFont(new Font("Arial", Font.BOLD, 16));
        label1.setOpaque(true);
        label1.setBackground(Color.WHITE);
        infoPanel.add(label1, gbc);
        
        JLabel label2 = new JLabel("Tour Miền Bắc 5N4Đ: HCM - Hà Nội ");
        gbc.gridy = 3;
        label2.setFont(new Font("Arial", Font.BOLD, 16));
        label2.setOpaque(true);
        label2.setBackground(Color.WHITE);
        infoPanel.add(label2, gbc);
        
        JLabel label3 = new JLabel("Tour Miền Bắc 5N4Đ: HCM - Hà Nội ");
        gbc.gridy = 4;
        label3.setFont(new Font("Arial", Font.BOLD, 16));
        label3.setOpaque(true);
        label3.setBackground(Color.WHITE);
        infoPanel.add(label3, gbc);

        JLabel priceLabel = new JLabel("8.590.000 VND");
        gbc.gridy = 5;
        priceLabel.setForeground(new Color(255, 153, 0));
        infoPanel.add(priceLabel, gbc);
        
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });
        
        return card;
    }
}