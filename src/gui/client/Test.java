package gui.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Test extends JFrame {
	private JPanel contentPane;
	
    public Test() {
    	setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        
        // Top navigation bar
        JPanel topNav = createTopNav();
        add(topNav, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        
        // Header panel với hình nền
        JPanel headerPanel = createHeaderPanel();
        mainContent.add(headerPanel, BorderLayout.NORTH);
        
        // Search form panel
        JPanel searchPanel = createSearchFormPanel();
        mainContent.add(searchPanel, BorderLayout.CENTER);
        
        // Results panel
        JPanel resultsPanel = createResultsPanel();
        mainContent.add(resultsPanel, BorderLayout.SOUTH);
        
        add(mainContent, BorderLayout.CENTER);
        
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createTopNav() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setPreferredSize(new Dimension(getWidth(), 50));
        
        // Left side - Logo
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftNav.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel("Booking");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        leftNav.add(logoLabel);
        
        // Center - Navigation items
        JPanel centerNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        centerNav.setBackground(Color.WHITE);
        String[] navItems = {"Accommodation", "Flights", "Car rentals", "Compilation"};
        
        for (String item : navItems) {
            JLabel navItem = new JLabel(item);
            navItem.setFont(new Font("Arial", Font.PLAIN, 14));
            if (item.equals("Flights")) {
                navItem.setForeground(new Color(0, 102, 204));
                navItem.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 204)));
            }
            centerNav.add(navItem);
        }
        
        // Right side - User menu
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightNav.setBackground(Color.WHITE);
        JLabel searchIcon = new JLabel("🔍");
        JLabel notifIcon = new JLabel("🔔");
        JLabel userIcon = new JLabel("👤");
        rightNav.add(searchIcon);
        rightNav.add(notifIcon);
        rightNav.add(userIcon);
        
        navPanel.add(leftNav, BorderLayout.WEST);
        navPanel.add(centerNav, BorderLayout.CENTER);
        navPanel.add(rightNav, BorderLayout.EAST);
        
        return navPanel;
    }
    
    private JPanel createHeaderPanel() {
        // Main panel using BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(getWidth(), 400));

        // Background panel with gradient
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Create gradient overlay
                GradientPaint gradient = new GradientPaint(
                    0, getHeight(), new Color(0, 0, 0, 180),
                    0, getHeight() - 100, new Color(0, 0, 0, 0)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setBackground(new Color(0, 0, 0, 50));

        // Content panel for text and button using BoxLayout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Add left padding to content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(150, 50, 0, 50));

        // Title text
        JLabel titleLabel = new JLabel("Laghi di Fusine");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Subtitle text
        JLabel subtitleLabel = new JLabel("Italy");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        subtitleLabel.setForeground(Color.BLACK);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add vertical spacing between components
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Button panel for proper alignment
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);

        JButton moreInfoBtn = new JButton("More information →");
        moreInfoBtn.setBackground(new Color(255, 255, 255, 80));
        moreInfoBtn.setForeground(Color.BLACK);
        moreInfoBtn.setBorderPainted(false);
        moreInfoBtn.setFocusPainted(false);
        buttonPanel.add(moreInfoBtn);
        
        // Set button panel alignment
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(buttonPanel);

        // Add panels to main panel
        panel.add(bgPanel, BorderLayout.CENTER);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Trip type options
        JPanel tripTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        String[] tripTypes = {"Return", "One-Way", "Multi-city"};
        ButtonGroup tripTypeGroup = new ButtonGroup();
        
        for (String type : tripTypes) {
            JRadioButton radioBtn = new JRadioButton(type);
            radioBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            if (type.equals("Return")) {
                radioBtn.setSelected(true);
            }
            tripTypeGroup.add(radioBtn);
            tripTypePanel.add(radioBtn);
        }
        
        // Search form
        JPanel searchForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Custom text fields
        JTextField fromField = createCustomTextField("Moscow, Russia", "🔍");
        JTextField toField = createCustomTextField("Norway", "🔍");
        JTextField departField = createCustomTextField("Depart", "📅");
        JTextField returnField = createCustomTextField("Return", "📅");
        JTextField passengersField = createCustomTextField("1 adult, Economy", "👤");
        
        // Add components to search form
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        searchForm.add(fromField, gbc);
        
        gbc.gridx = 1;
        searchForm.add(toField, gbc);
        
        gbc.gridx = 2;
        searchForm.add(departField, gbc);
        
        gbc.gridx = 3;
        searchForm.add(returnField, gbc);
        
        gbc.gridx = 4;
        searchForm.add(passengersField, gbc);
        
        JButton searchButton = new JButton("✈");
        searchButton.setBackground(new Color(0, 102, 204));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(50, 35));
        gbc.gridx = 5;
        gbc.weightx = 0.0;
        searchForm.add(searchButton, gbc);
        
        mainPanel.add(tripTypePanel, BorderLayout.NORTH);
        mainPanel.add(searchForm, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JTextField createCustomTextField(String placeholder, String icon) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.GRAY);
                    g2.setFont(getFont().deriveFont(Font.PLAIN));
                    g2.drawString(placeholder, 35, 23);
                    g2.dispose();
                }
            }
        };
        
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 30, 5, 5)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBounds(8, 7, 20, 20);
        field.setLayout(null);
        field.add(iconLabel);
        
        return field;
    }
    
    private JPanel createResultsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JLabel compilationLabel = new JLabel("Compilation");
        compilationLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(compilationLabel, BorderLayout.NORTH);
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        for (int i = 0; i < 3; i++) {
            cardsPanel.add(createResultCard("€ " + (80 - i*15)));
        }
        
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createResultCard(String price) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Placeholder image panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(200, 200, 200));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        imagePanel.setPreferredSize(new Dimension(0, 150));
        
        // Price label
        JLabel priceLabel = new JLabel(price + " Flight per person");
        priceLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        card.add(imagePanel, BorderLayout.CENTER);
        card.add(priceLabel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }
        });
        
        return card;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Test().setVisible(true);
        });
    }
}