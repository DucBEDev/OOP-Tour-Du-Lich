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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Book;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.Tour_DAO;
import entity.Customer;
import entity.Tour;
import gui.admin.SignIn;
import utils.VNComboBox;

public class Dashboard extends JFrame {
	private JPanel topNav;
	private JPanel content;
	private JPanel content_header;
	private JPanel pnlTourList;
	private JScrollPane scrollPane;
	
	private Tour_DAO tour_dao;
	private Customer cus;
	
	private static JTextArea chatArea;
    private static JTextField messageField;
    private static DataOutputStream dataOutputStream;
	
    public Dashboard(Customer customer) {
    	Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension scrSize = kit.getScreenSize();
		int scrHeight = scrSize.height;
		int scrWidth = scrSize.width;
		setLocationByPlatform(true);
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(new BorderLayout());
    	
    	cus = customer;
    	
    	// Header
    	topNav = createTopNav();
    	add(topNav, BorderLayout.NORTH);
    	
    	// Content
    	content = new JPanel(new BorderLayout());
    	content_header = createContentHeader(scrWidth, scrHeight * 3 / 7);
    	content.add(content_header, BorderLayout.NORTH);
    	add(content, BorderLayout.CENTER);
    	
    	// List of tour
    	tour_dao = new Tour_DAO();
    	ArrayList<Tour> tourList = new ArrayList<Tour>();
    	pnlTourList = createResultsPanel(tourList);
    	scrollPane = new JScrollPane(pnlTourList);
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
    	
    	// Chat button
    	JButton btnMessage = new JButton("Liên hệ hỗ trợ");
    	btnMessage.setFont(new Font("Arial", Font.BOLD, 14));
    	btnMessage.setBackground(new Color(148, 218, 248));
    	btnMessage.setBorder(null);
    	btnMessage.setCursor(new Cursor(Cursor.HAND_CURSOR));
    	btnMessage.setPreferredSize(new Dimension(120, 32));
    	btnMessage.setFocusPainted(false);
    	btnMessage.addActionListener(e -> {
    		if (cus.getCustomerId() == null) {
    			JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập để liên hệ với nhân viên hỗ trợ.");
    			return;
    		}
    		showMessageDialog();
    	});
    	pnlRight.add(btnMessage);
    	
    	
    	if (cus.getCustomerId() == null) {
    		JButton btnRegister = new JButton("Đăng nhập");
    		btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
    		btnRegister.setBackground(new Color(148, 218, 248));
    		btnRegister.setBorder(null);
    		btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
    		btnRegister.setPreferredSize(new Dimension(100, 32));
    		btnRegister.setFocusPainted(false);
    		btnRegister.addActionListener(e -> {
    			SignIn signIn = new SignIn();
    			signIn.setVisible(true);
    			this.dispose();
    		});
    		pnlRight.add(btnRegister);    		
    	} 
    	else {
    		JLabel lblUser = new JLabel("Xin chào, " + cus.getUserName());
    		lblUser.setFont(new Font("Arial", Font.BOLD, 14));
    		lblUser.setBackground(new Color(148, 218, 248));
    		lblUser.setPreferredSize(new Dimension(200, 32));
    		pnlRight.add(lblUser);    
    	}
    	
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
        lblDepartureLocation.setFont(new Font("Arial", Font.BOLD, 16));
        fieldsPanel.add(lblDepartureLocation, gbc);
        JLabel lblDestination = new JLabel("Điểm đến");
        lblDestination.setFont(new Font("Arial", Font.BOLD, 16));
        fieldsPanel.add(lblDestination, gbc);
        JLabel lblDepartureTime = new JLabel("Ngày khởi hành");
        lblDepartureTime.setFont(new Font("Arial", Font.BOLD, 16));
        fieldsPanel.add(lblDepartureTime, gbc);
        JLabel lblTransportInfo = new JLabel("Phương tiện");
        lblTransportInfo.setFont(new Font("Arial", Font.BOLD, 16));
        fieldsPanel.add(lblTransportInfo, gbc);
        
        // DepartureLocation field
        JComboBox<String> txtDepartureLocation = VNComboBox.createProvincesComboBox();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        fieldsPanel.add(txtDepartureLocation, gbc);

        // Destination field
        JComboBox<String> txtDestination = VNComboBox.createProvincesComboBox();
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
        	String departureLocation = (String)txtDepartureLocation.getSelectedItem();
        	String destinationLocation = (String)txtDestination.getSelectedItem();
        	Date departureDate = departDate.getDate();
        	String transport = (String)transportInfo.getSelectedItem();
        	
        	// Validate input
        	boolean isValid = true;
            if (departureLocation == null || departureLocation.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn địa điểm khởi hành.");
                isValid = false;
                return;
            }

            if (destinationLocation == null || destinationLocation.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn địa điểm đến.");
                isValid = false;
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            Date tomorrow = calendar.getTime();

            if (departureDate == null || departureDate.before(tomorrow)) {
                JOptionPane.showMessageDialog(null, "Ngày khởi hành phải lớn hơn ngày hôm nay.");
                isValid = false;
                return;
            }

            if (transport == null || transport.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn phương tiện di chuyển.");
                isValid = false;
                return;
            }

            if (isValid) {
                ArrayList<Tour> tourList = tour_dao.searchTours(departureLocation, destinationLocation, new SimpleDateFormat("dd/MM/yyyy").format(departureDate), transport);
                if (tourList.size() == 0) {
                	JOptionPane.showMessageDialog(null, "Không có Tour thỏa mãn yêu cầu.");
                	return;
                }
                BookingOrder order = new BookingOrder(tourList, cus);
                order.setVisible(true);
    			this.dispose();
            }
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

    private JPanel createResultsPanel(ArrayList<Tour> tourList) {
    	JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel pnlHeader = new JPanel();
        pnlHeader.setLayout(new BoxLayout(pnlHeader, BoxLayout.X_AXIS));
        
        JLabel tourListLabel = new JLabel("Danh sách Tour nổi bật");
        tourListLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pnlHeader.add(tourListLabel);
        pnlHeader.add(Box.createHorizontalGlue());
        
        JButton btnGetAll = new JButton("Xem tất cả");
        btnGetAll.setPreferredSize(new Dimension(100, 30));
        btnGetAll.setMaximumSize(new Dimension(100, 30));
        btnGetAll.setBackground(new Color(0, 0, 139)); 
        btnGetAll.setForeground(Color.WHITE);
        btnGetAll.setFocusPainted(false);
        btnGetAll.setBorderPainted(false);
        btnGetAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGetAll.addActionListener(e -> {
        	ArrayList<Tour> tour_List = tour_dao.getAll();
        	BookingOrder bookingOrder = new BookingOrder(tour_List, cus);
        	bookingOrder.setVisible(true);
        	this.dispose();
        });
        pnlHeader.add(btnGetAll);
        
        mainPanel.add(pnlHeader, BorderLayout.NORTH);

        // Get data form the database
        tourList = tour_dao.getLimitedTours(15);

        JPanel cardsPanel = new JPanel(new GridLayout(tourList.size() / 3, 3, 20, 20));
        cardsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        for (Tour tour : tourList) {
            cardsPanel.add(createResultCard(tour));
        }

        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createResultCard(Tour tour) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Placeholder image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon originalIcon = new ImageIcon(tour.getImage());
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Thêm các thành phần vào InfoPanel
        JLabel lblTourName = new JLabel(tour.getTourName());
        gbc.gridy = 1;
        lblTourName.setFont(new Font("Arial", Font.BOLD, 24));
        lblTourName.setOpaque(true);
        lblTourName.setBackground(Color.WHITE);
        infoPanel.add(lblTourName, gbc);
        
        JLabel lblDuration = new JLabel(String.valueOf(String.format("- Thời gian: %d ngày %d đêm", tour.getDuration(), tour.getDuration() - 1)));
        gbc.gridy = 2;
        lblDuration.setFont(new Font("Arial", Font.BOLD, 16));
        lblDuration.setOpaque(true);
        lblDuration.setBackground(Color.WHITE);
        infoPanel.add(lblDuration, gbc);
        
        JLabel lblTransportInfo = new JLabel("- Phương tiện di chuyển: " + tour.getTransportInfo());
        gbc.gridy = 3;
        lblTransportInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTransportInfo.setOpaque(true);
        lblTransportInfo.setBackground(Color.WHITE);
        infoPanel.add(lblTransportInfo, gbc);
        
        JLabel lblDepartureLocation = new JLabel(String.valueOf("- Địa điểm khởi hành: " + tour.getDepartureLocation()));
        gbc.gridy = 4;
        lblDepartureLocation.setFont(new Font("Arial", Font.BOLD, 16));
        lblDepartureLocation.setOpaque(true);
        lblDepartureLocation.setBackground(Color.WHITE);
        infoPanel.add(lblDepartureLocation, gbc);
        
        JLabel lblDepartureDate = new JLabel(String.valueOf("- Thời gian khởi hành: " + tour.getDepartureDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        gbc.gridy = 5;
        lblDepartureDate.setFont(new Font("Arial", Font.BOLD, 16));
        lblDepartureDate.setOpaque(true);
        lblDepartureDate.setBackground(Color.WHITE);
        infoPanel.add(lblDepartureDate, gbc);
        
        JLabel lblMaxParticipants = new JLabel(String.valueOf("- Số vé: " + tour.getMaxParticipants() + " vé"));
        gbc.gridy = 6;
        lblMaxParticipants.setFont(new Font("Arial", Font.BOLD, 16));
        lblMaxParticipants.setOpaque(true);
        lblMaxParticipants.setBackground(Color.WHITE);
        infoPanel.add(lblMaxParticipants, gbc);
        
        JLabel lblCurrentParticipants = new JLabel(String.valueOf("- Số vé đã bán: " + tour.getCurrentParticipants() + " vé"));
        gbc.gridy = 7;
        lblCurrentParticipants.setFont(new Font("Arial", Font.BOLD, 16));
        lblCurrentParticipants.setOpaque(true);
        lblCurrentParticipants.setBackground(Color.WHITE);
        infoPanel.add(lblCurrentParticipants, gbc);
        

        JPanel pnlPrice = new JPanel(new GridLayout(2, 1, 0, 5)); 
        pnlPrice.setOpaque(false);
		NumberFormat formatVN = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
		
        JLabel lblAdultPrice = new JLabel("Người lớn: " + String.valueOf(formatVN.format(tour.getAdultPrice())) + "/ Vé");
        lblAdultPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblAdultPrice.setForeground(new Color(255, 153, 0)); 
        lblAdultPrice.setHorizontalAlignment(JLabel.RIGHT);
        pnlPrice.add(lblAdultPrice);
        
        JLabel lblChildPrice = new JLabel("Trẻ em: " + String.valueOf(formatVN.format(tour.getChildPrice())) + "/ Vé");
        lblChildPrice.setFont(new Font("Arial", Font.BOLD, 16));
        lblChildPrice.setForeground(new Color(255, 153, 0)); 
        lblChildPrice.setHorizontalAlignment(JLabel.RIGHT);
        pnlPrice.add(lblChildPrice); 
        
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST; 
        infoPanel.add(pnlPrice, gbc);
        
        card.add(infoPanel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		TourDetail tourDetail = new TourDetail(tour, cus);
        		tourDetail.setVisible(true);
        		Window window = SwingUtilities.getWindowAncestor(card);
                window.dispose();
        	}
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
    
    private void showMessageDialog() {
    	JDialog dialog = new JDialog(this, "Liên hệ CSKH", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
		
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(Color.WHITE);
        chatArea.setCaretColor(Color.WHITE);
        chatArea.setForeground(Color.WHITE);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        messageField = new JTextField(30);
        messageField.setFont(new Font("Arial", Font.PLAIN, 16));
        messageField.setPreferredSize(new Dimension(300, 40));
        messageField.setBackground(new Color(240, 240, 240));
        messageField.setForeground(Color.BLACK);
        messageField.setBorder(new EmptyBorder(0, 10, 0, 0)); 
        
        JButton sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(80, 40));
        sendButton.setBackground(new Color(0, 0, 139));
        sendButton.setForeground(Color.WHITE);
        sendButton.setMaximumSize(new Dimension(100, 30));
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(panel, BorderLayout.SOUTH);
        
        try {
            Socket socket = new Socket("localhost", 1234);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            sendButton.addActionListener((ActionEvent e) -> sendMessage());
            messageField.addActionListener((ActionEvent e) -> sendMessage());

            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = dataInputStream.readUTF()) != null) {
                        final String finalMessage = message;
                        SwingUtilities.invokeLater(() -> {
                        	chatArea.setForeground(Color.BLACK);
                            chatArea.append("Admin: " + finalMessage + "\n");
                            if ("exit".equalsIgnoreCase(finalMessage)) {
                                chatArea.append("Admin đã ngắt kết nối.\n");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            receiveThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        dialog.setVisible(true);
	}
    
    private static void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            try {
                chatArea.append("Me: " + message + "\n");
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();

                if ("exit".equalsIgnoreCase(message)) {
                    messageField.setEnabled(false);
                }
                messageField.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}