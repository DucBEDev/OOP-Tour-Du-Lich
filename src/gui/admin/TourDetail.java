package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.toedter.calendar.JDateChooser;

import dao.Tour_DAO;
import entity.Tour;

public class TourDetail extends JPanel 
{
    private JPanel formPanel;
    private JPanel formButtonPanel;
    private JPanel functionButton;
    private JPanel imageAndFormWrapperPanel;
    private JPanel wrapperPanel;

    private JLabel backButton;
    private JLabel editButton;
    
    private JButton saveButton;
    private JButton cancelButton;
    private JButton addImageButton;

    private JLabel tourIdLabel;
//    private JLabel imageLabel;
    private JLabel tourNameLabel;
//    private JLabel descriptionLabel;
    private JLabel departureDateLabel;
    private JLabel durationLabel;
    private JLabel departureLocationLabel;
    private JLabel departureTimeLabel;
    private JLabel destinationLabel;
    private JLabel transportInfoLabel;
    private JLabel adultPriceLabel;
    private JLabel childPriceLabel;
    private JLabel maxParticipantsLabel;
    private JLabel currentParticipantsLabel;
    private JLabel statusLabel;

    private JTextField tourIdContent;
    private JLabel imageContent;
    private JTextField tourNameContent;
    private JTextArea descriptionContent;
    private JDateChooser departureDateContent;
    private JTextField durationContent;
    private JTextField departureLocationContent;
    private JTextField departureTimeContent;
    private JTextField destinationContent;
    private JComboBox<String> transportInfoContent;
    private JTextField adultPriceContent;
    private JTextField childPriceContent;
    private JTextField maxParticipantsContent;
    private JTextField currentParticipantsContent;
    private JComboBox<String> statusContent;
    
    private String tourId;
    private Image image;
    private String tourName;
    private String description;
    private int duration;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private String departureLocation;
    private String destination;
    private double adultPrice;
    private double childPrice;
    private int maxParticipants;
    private int currentParticipants;
    private String transportInfo;
    private String status;
    
    private String base64Image;
    
    private Tour_DAO tourDAO = new Tour_DAO();

    public TourDetail(Tour tour) {
        setLayout(new BorderLayout(10, 10));

        formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(1, 2, 10, 10));

        functionButton = new JPanel();
        functionButton.setLayout(new BorderLayout());

        backButton = new JLabel("Thoát");
        backButton.setPreferredSize(new Dimension(100, 50));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBorder(BorderFactory.createLineBorder(Color.black));
        backButton.addMouseListener(new MouseAdapter() 
        {
        	@Override
    		public void mouseClicked(MouseEvent e) 
    		{
        		removeAll();
        		add(new TourManagement());
        		revalidate();
        	    repaint();
    		}
        });

        editButton = new JLabel("Sửa");
        editButton.setPreferredSize(new Dimension(100, 50));
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setBorder(BorderFactory.createLineBorder(Color.black));
        editButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	enableEditing(true);
            }
        });

        functionButton.add(backButton, BorderLayout.WEST);
        functionButton.add(editButton, BorderLayout.EAST);
    
        // Tour ID
        tourIdLabel = new JLabel("Mã tour:");
        tourIdContent = new JTextField();
        tourIdContent.setEnabled(false);
//        tourIdContent.addActionListener(e1 -> {
//            String temp = tourIdContent.getText();
//            if (temp.matches("^[A-Za-z0-9]+$")) {
//                tourId = temp;
//                System.out.println("Tour ID: " + tourId);
//            } else {
//                System.out.println("Invalid input for Tour ID");
//                tourIdContent.setText("");
//            }
//        });

//        imageLabel = new JLabel("Hình ảnh:");
        imageContent = new JLabel(); // Image logic xử lý sau.

        // Tour name
        tourNameLabel = new JLabel("Tên Tour:");
        tourNameContent = new JTextField();
//        tourNameContent.addActionListener(evt -> {
//            String temp = tourNameContent.getText();
//            if (temp.matches("^[a-zA-ZÀ-ỹ ]+$")) {
//                tourName = temp;
//                System.out.println("Tour Name: " + tourName);
//            } else {
//                System.out.println("Invalid input for Tour Name");
//                tourNameContent.setText("");
//            }
//        });

        // Duration
        durationLabel = new JLabel("Số ngày:");
        durationContent = new JTextField();
//        durationContent.addActionListener(evt->
//        {
//            
//                String durationTemp = durationContent.getText();
//                if (durationTemp.matches("\\d+")) 
//                {
//                    duration = Integer.parseInt(durationTemp);
//                    System.out.println("Duration: " + duration);
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    durationContent.setText("");
//                }
//            
//        });

        // Departure Date
        departureDateLabel = new JLabel("Ngày khởi hành:");
        departureDateContent = new JDateChooser();
        departureDateContent.setPreferredSize(new Dimension(20, 35));
        departureDateContent.setDateFormatString("dd/MM/yyyy");
        departureDateContent.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        
        // Departure Time
        departureTimeLabel = new JLabel("Giờ khởi hành:");
        departureTimeContent = new JTextField();
//        departureTimeContent.addActionListener(evt->
//        {
//            
//                String departureTimeTemp = departureTimeContent.getText();
//                if (departureTimeTemp.matches("\\d{2}-\\d{2}"))
//                {
//                    String[] timeParts = departureTimeTemp.split("-");
//                    
//                    int hour = Integer.parseInt(timeParts[0]);
//                    int minute = Integer.parseInt(timeParts[1]);
//                    
//                    departureTime = LocalTime.of(hour, minute);
//                    
//                    System.out.println("Departure Time: " + departureTime);
//
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    departureTimeContent.setText("");
//                }
//            
//        });

        // Departure Location
        departureLocationLabel = new JLabel("Điểm Khởi Hành:");
        departureLocationContent = new JTextField();
//        departureLocationContent.addActionListener(evt -> {
//            String temp = departureLocationContent.getText();
//            if (temp.matches("^[a-zA-ZÀ-ỹ ]+$")) {
//                departureLocation = temp;
//                System.out.println("Departure Location: " + departureLocation);
//            } else {
//                System.out.println("Invalid input for Departure Location");
//                departureLocationContent.setText("");
//            }
//        });

        // Destination
        destinationLabel = new JLabel("Điểm Đến:");
        destinationContent = new JTextField();
//        destinationContent.addActionListener(evt -> 
//        {
//            String temp = destinationContent.getText();
//            if (temp.matches("^[A-Za-z ]+$")) 
//            {
//                destination = temp;
//                System.out.println("Destination: " + destination);
//            } 
//            else 
//            {
//                System.out.println("Invalid input for Destination");
//                destinationContent.setText("");
//            }
//        });

        // Adult Price
        adultPriceLabel = new JLabel("Giá người lớn:");
        adultPriceContent = new JTextField();
//        adultPriceContent.addActionListener(evt->
//        {
//            
//                String adultPriceTemp = adultPriceContent.getText();
//                if (adultPriceTemp.matches("\\d+")) 
//                {
//                    adultPrice = Double.parseDouble(adultPriceTemp);
//                    System.out.println("Adult Price: " + adultPrice);
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    adultPriceContent.setText("");
//                }
//            
//        });

        // Child Price
        childPriceLabel = new JLabel("Giá trẻ em:");
        childPriceContent = new JTextField();
//        childPriceContent.addActionListener(evt->
//        {
//            
//                String childPriceTemp = childPriceContent.getText();
//                if (childPriceTemp.matches("\\d+")) 
//                {
//                    childPrice = Double.parseDouble(childPriceTemp);
//                    System.out.println("Child Price: " + childPrice);
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    childPriceContent.setText("");
//                }
//            
//        });

        // Max Participants
        maxParticipantsLabel = new JLabel("Số lượng tối đa:");
        maxParticipantsContent = new JTextField();
//        maxParticipantsContent.addActionListener(evt->
//        {
//           
//                String maxParticipantsTemp = maxParticipantsContent.getText();
//                if (maxParticipantsTemp.matches("\\d+")) 
//                {
//                    maxParticipants = Integer.parseInt(maxParticipantsTemp);
//                    System.out.println("Max Participants: " + maxParticipants);
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    maxParticipantsContent.setText("");
//                }
//            
//        });
        
        currentParticipantsLabel = new JLabel("Số người hiện tại:");
        currentParticipantsContent = new JTextField();
//        currentParticipantsContent.addActionListener(evt->
//        {
//           
//                String currentParticipantsTemp = currentParticipantsContent.getText();
//                if (currentParticipantsTemp.matches("\\d+")) 
//                {
//                	currentParticipants = Integer.parseInt(currentParticipantsTemp);
//                    System.out.println("Current Participants: " + currentParticipants);
//                } 
//                else 
//                {
//                    System.out.println("Invalid input");
//                    currentParticipantsContent.setText("");
//                }
//            
//        });

        // Transport Info (ComboBox)
        transportInfoLabel = new JLabel("Phương tiện:");
        transportInfoContent = new JComboBox<>();
        transportInfoContent.addItem("Xe");
        transportInfoContent.addItem("Máy bay");
        transportInfoContent.addItem("Tàu");
//        transportInfoContent.addActionListener(evt->
//        {
//            
//                transportInfo = (String) transportInfoContent.getSelectedItem();
//                System.out.println("Transport Info: " + transportInfo);
//            
//        });

        // Description
//        descriptionLabel = new JLabel("Mô Tả:");
        descriptionContent = new JTextArea(5,30);
        descriptionContent.setBorder(BorderFactory.createTitledBorder("Mô tả Tour"));
//        descriptionContent.addKeyListener(new KeyAdapter() 
//        {
//        	  @Override
//              public void keyPressed(KeyEvent e) 
//        	  {
//                  if (e.getKeyCode() == KeyEvent.VK_ENTER) 
//                  {
//                      // Save text to a variable
//                	  String descriptionTemp = descriptionContent.getText();
//                      if (descriptionTemp.matches("^[a-zA-ZÀ-ỹ .,\\n]+$"))
//                      {
//                          description = descriptionTemp;
//                          System.out.println("Description: " + description);
//                      } 
//                      else 
//                      {
//                          System.out.println("Invalid input for Description");
//                          descriptionContent.setText("");
//                      }
//                  }
//              }
//        
//        });

        

        statusLabel = new JLabel("Trạng thái:");
        statusContent = new JComboBox<>();
        statusContent.addItem("Hết vé");
        statusContent.addItem("Còn vé");
        statusContent.addActionListener(evt->
        {
            
                status = (String) statusContent.getSelectedItem();
                System.out.println("Status Info: " + status);
            
        });
        
        
        if (tour != null) 
        {
        	tourId = tour.getTourId();
        	image = tour.getImage();
            tourName = tour.getTourName();
            description = tour.getDescription();
            departureDate = tour.getDepartureDate();
            duration = tour.getDuration();
            departureLocation = tour.getDepartureLocation();
            departureTime = tour.getDepartureTime();
            destination = tour.getDestination();
            transportInfo = tour.getTransportInfo();
            adultPrice = tour.getAdultPrice();
            childPrice = tour.getChildPrice();
            maxParticipants = tour.getMaxParticipants();
            currentParticipants = tour.getCurrentParticipants();
            status = tour.getStatus();
            
            
            tourIdContent.setText(tourId);
            imageContent.setIcon(new ImageIcon(tour.getImage().getScaledInstance(700, 250, Image.SCALE_SMOOTH)));
            imageContent.setHorizontalAlignment(SwingConstants.CENTER);
            tourNameContent.setText(tourName);
            descriptionContent.setText(description);
            departureDateContent.setDate(Date.from(tour.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            durationContent.setText(String.valueOf(duration));
            departureLocationContent.setText(departureLocation);
            departureTimeContent.setText(departureTime.toString());
            destinationContent.setText(destination);
            transportInfoContent.setSelectedItem(transportInfo);
            adultPriceContent.setText(String.format("%.2f", adultPrice));
            childPriceContent.setText(String.format("%.2f", childPrice));
            maxParticipantsContent.setText(String.valueOf(maxParticipants));
            currentParticipantsContent.setText(String.valueOf(currentParticipants));
            statusContent.setSelectedItem(status);

       }
        
        wrapperPanel = new JPanel(new BorderLayout());
        
        JPanel leftPanel = new JPanel(new GridLayout(7,2,10,10));
        JPanel rightPanel = new JPanel(new GridLayout(7,2,10,10));
       

        // Add tất cả các fields vào formPanel.
        leftPanel.add(tourIdLabel);
        leftPanel.add(tourIdContent);
        
//        formPanel.add(imageLabel);
//        formPanel.add(imageContent);
        
        leftPanel.add(tourNameLabel);
        leftPanel.add(tourNameContent);                
        
        leftPanel.add(departureDateLabel);
        leftPanel.add(departureDateContent);
        
        leftPanel.add(durationLabel);
        leftPanel.add(durationContent);
        
        leftPanel.add(departureLocationLabel);
        leftPanel.add(departureLocationContent);
        
        leftPanel.add(departureTimeLabel);
        leftPanel.add(departureTimeContent);
        
        leftPanel.add(destinationLabel);
        leftPanel.add(destinationContent);
        
        rightPanel.add(transportInfoLabel);
        rightPanel.add(transportInfoContent);
        
        rightPanel.add(adultPriceLabel);
        rightPanel.add(adultPriceContent);
        
        rightPanel.add(childPriceLabel);
        rightPanel.add(childPriceContent);
        
        rightPanel.add(maxParticipantsLabel);
        rightPanel.add(maxParticipantsContent);
        
        rightPanel.add(currentParticipantsLabel);
        rightPanel.add(currentParticipantsContent);
        
        rightPanel.add(statusLabel);
        rightPanel.add(statusContent);
        
//        formPanel.add(descriptionLabel);
//        descriptionPanel.add(new JScrollPane(descriptionContent));
        
        formPanel.add(leftPanel);
        formPanel.add(rightPanel);
        
        
        TitledBorder titleBorderForm = BorderFactory.createTitledBorder("Thông tin Tour");       
        formPanel.setBorder(titleBorderForm);
        
        wrapperPanel.add(formPanel, BorderLayout.NORTH);
        wrapperPanel.add(new JScrollPane(descriptionContent), BorderLayout.CENTER);
       
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        addImageButton = new JButton("Thêm hình");

        // Action for the Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	if(!validateInput()) return;
            	else
            	{
            		tourName= tourNameContent.getText().trim();
            		description= descriptionContent.getText().trim();
            		departureDate= departureDateContent.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            		duration= Integer.parseInt(durationContent.getText().trim());
            		departureLocation= departureLocationContent.getText().trim();
            		
            		String[] timeParts = departureTimeContent.getText().trim().split(":");
                   
            		int hour = Integer.parseInt(timeParts[0]);
            		int minute = Integer.parseInt(timeParts[1]);
                   
                   	departureTime = LocalTime.of(hour, minute);
                   	
                   	destination= destinationContent.getText().trim();
                   	transportInfo= transportInfoContent.getSelectedItem().toString().trim();
                   	adultPrice= Integer.parseInt(adultPriceContent.getText().trim());
                   	childPrice= Integer.parseInt(childPriceContent.getText().trim());
                   	maxParticipants= Integer.parseInt(maxParticipantsContent.getText().trim());
                   	currentParticipants= Integer.parseInt(currentParticipantsContent.getText().trim());
                   	status= statusContent.getSelectedItem().toString().trim();

            	}
//            	tour.setTourId(tourId);
            	tour.setImage(image);
            	tour.setTourName(tourName);
            	tour.setDescription(description);
            	tour.setDepartureDate(departureDate);
            	tour.setDuration(duration);
            	tour.setDepartureLocation(departureLocation);
            	tour.setDepartureTime(departureTime);
            	tour.setDestination(destination);
            	tour.setTransportInfo(transportInfo);
            	tour.setAdultPrice(adultPrice);
            	tour.setChildPrice(childPrice);
            	tour.setMaxParticipants(maxParticipants);
            	tour.setCurrentParticipants(currentParticipants);
            	tour.setStatus(status);

            	
            	
            	if(tourDAO.update(tour, base64Image))
            	{
                    JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            	}
            	else JOptionPane.showMessageDialog(null, "Cập nhật đơn hàng không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
         	
            	
            	enableEditing(false);
            	formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving
            }
        });

        // Action for the Cancel button
        cancelButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	tourId = tour.getTourId();
                tourName = tour.getTourName();
                description = tour.getDescription();
                departureDate = tour.getDepartureDate();
                duration = tour.getDuration();
                departureLocation = tour.getDepartureLocation();
                departureTime = tour.getDepartureTime();
                destination = tour.getDestination();
                transportInfo = tour.getTransportInfo();
                adultPrice = tour.getAdultPrice();
                childPrice = tour.getChildPrice();
                maxParticipants = tour.getMaxParticipants();
                currentParticipants = tour.getCurrentParticipants();
                status = tour.getStatus();
        		
                tourIdContent.setText(tourId);
                tourNameContent.setText(tourName);
                descriptionContent.setText(description);
                departureDateContent.setDate(Date.from(tour.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                durationContent.setText(String.valueOf(duration));
                departureLocationContent.setText(departureLocation);
                departureTimeContent.setText(departureTime.toString());
                destinationContent.setText(destination);
                transportInfoContent.setSelectedItem(transportInfo);
                adultPriceContent.setText(String.format("%.2f", adultPrice));
                childPriceContent.setText(String.format("%.2f", childPrice));
                maxParticipantsContent.setText(String.valueOf(maxParticipants));
                currentParticipantsContent.setText(String.valueOf(currentParticipants));
                statusContent.setSelectedItem(status);
                
                base64Image = null;
		        
                enableEditing(false);
        		
        		formPanel.revalidate();
     		    formPanel.repaint(); // Close the dialog after saving
            }
        });
        
        
        addImageButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                JFileChooser filechooser = new JFileChooser();
                filechooser.setDialogTitle("Choose an Image");
                filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = filechooser.showOpenDialog(formPanel);
                if (result == JFileChooser.APPROVE_OPTION) 
                {
                    File selectedFile = filechooser.getSelectedFile();
                    ImageIcon imageIcon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image imageTemp = imageIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);

                    // Display the selected image in the GUI
                    imageContent.setIcon(new ImageIcon(imageTemp));
                    imageContent.setText(""); 
                    
                    image = imageIcon.getImage();
                    
                                            
                    try 
                    {
                    	File file = new File(selectedFile.getAbsolutePath());
						Image imageIo = ImageIO.read(file);
						
						 ByteArrayOutputStream baos = new ByteArrayOutputStream();
					     ImageIO.write((BufferedImage) imageIo, "jpg", baos); // Assuming JPG format
					     byte[] imageBytes = baos.toByteArray();
					     
					     base64Image = Base64.getEncoder().encodeToString(imageBytes);
					} 
                    catch (IOException e1) 
                    {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    
                }
            }
        });
        
        enableEditing(false);


        // Add buttons to the dialog
        formButtonPanel = new JPanel();
        formButtonPanel.add(saveButton);
        formButtonPanel.add(cancelButton);
        formButtonPanel.add(addImageButton);
        wrapperPanel.add(formButtonPanel, BorderLayout.SOUTH);

        imageAndFormWrapperPanel = new JPanel(new BorderLayout());
        imageAndFormWrapperPanel.add(imageContent, BorderLayout.CENTER);
        imageAndFormWrapperPanel.add(wrapperPanel, BorderLayout.SOUTH);
        
        add(new JScrollPane(imageAndFormWrapperPanel), BorderLayout.CENTER);
        add(functionButton, BorderLayout.NORTH);
//        add(wrapperPanel, BorderLayout.SOUTH);
    }

    private void enableEditing(boolean enable) 
    {
//        tourIdContent.setEnabled(enable);
        tourNameContent.setEnabled(enable);
        descriptionContent.setEnabled(enable);
        departureDateContent.setEnabled(enable);
        durationContent.setEnabled(enable);
        departureLocationContent.setEnabled(enable);
        departureTimeContent.setEnabled(enable);
        destinationContent.setEnabled(enable);
        transportInfoContent.setEnabled(enable);
        adultPriceContent.setEnabled(enable);
        childPriceContent.setEnabled(enable);
        maxParticipantsContent.setEnabled(enable);
        currentParticipantsContent.setEnabled(enable);
        statusContent.setEnabled(enable);
        saveButton.setEnabled(enable);
        cancelButton.setEnabled(enable);
        addImageButton.setEnabled(enable);
    }

    

    private boolean validateInput() {
//        String tourId = tourIdContent.getText().trim();
        String tourName = tourNameContent.getText().trim();
        String description = descriptionContent.getText().trim();
//        Date departureDate = departureDateContent.getDate();
        String duration = durationContent.getText().trim();
        String departureLocation = departureLocationContent.getText().trim();
        String departureTime = departureTimeContent.getText().trim();
        String destination = destinationContent.getText().trim();
//        String transportInfo = (String) transportInfoContent.getSelectedItem();
        String adultPrice = adultPriceContent.getText().trim();
        String childPrice = childPriceContent.getText().trim();
        String maxParticipants = maxParticipantsContent.getText().trim();
//        String currentParticipants = currentParticipantsContent.getText().trim();
//        String status = (String) statusContent.getSelectedItem();

      
        if (tourName.isEmpty() || !tourName.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
            JOptionPane.showMessageDialog(null, 
                "Tên tour không hợp lệ! Vui lòng nhập tên hợp lệ.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            tourNameContent.requestFocus();
            return false;
        }

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Mô tả không được để trống!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            descriptionContent.requestFocus();
            return false;
        }


        if (duration.isEmpty() || !duration.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Thời gian tour không hợp lệ! Vui lòng nhập số ngày.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            durationContent.requestFocus();
            return false;
        }

        if (departureLocation.isEmpty() || !departureLocation.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
            JOptionPane.showMessageDialog(null, 
                "Địa điểm khởi hành không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            departureLocationContent.requestFocus();
            return false;
        }

        if (departureTime.isEmpty() || !departureTime.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
            JOptionPane.showMessageDialog(null, 
                "Giờ khởi hành không hợp lệ! Định dạng: HH:mm", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            departureTimeContent.requestFocus();
            return false;
        }

        if (destination.isEmpty() || !destination.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
            JOptionPane.showMessageDialog(null, 
                "Điểm đến không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            destinationContent.requestFocus();
            return false;
        }


        if (adultPrice.isEmpty() || !adultPrice.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Giá vé người lớn không hợp lệ! Vui lòng nhập số tiền hợp lệ.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            adultPriceContent.requestFocus();
            return false;
        }

        if (childPrice.isEmpty() || !childPrice.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Giá vé trẻ em không hợp lệ! Vui lòng nhập số tiền hợp lệ.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            childPriceContent.requestFocus();
            return false;
        }

        if (maxParticipants.isEmpty() || !maxParticipants.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, 
                "Số người tối đa không hợp lệ! Vui lòng nhập số dương.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            maxParticipantsContent.requestFocus();
            return false;
        }

        // Tất cả dữ liệu hợp lệ
        return true;
    }
    
    

}
