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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;

import dao.Tour_DAO;
import entity.Tour;

public class TourDetail extends JPanel 
{
    private JPanel formPanel;
    private JPanel formButtonPanel;
    private JPanel functionButton;

    private JLabel backButton;
    private JLabel editButton;
    
    private JButton saveButton;
    private JButton cancelButton;
    private JButton addImageButton;

    private JLabel tourIdLabel;
    private JLabel imageLabel;
    private JLabel tourNameLabel;
    private JLabel descriptionLabel;
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
    private JTextField departureDateContent;
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
        formPanel.setLayout(new GridLayout(16, 2, 10, 10));

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
        tourIdContent.addActionListener(e1 -> {
            String temp = tourIdContent.getText();
            if (temp.matches("^[A-Za-z0-9]+$")) {
                tourId = temp;
                System.out.println("Tour ID: " + tourId);
            } else {
                System.out.println("Invalid input for Tour ID");
                tourIdContent.setText("");
            }
        });

        imageLabel = new JLabel("Hình ảnh:");
        imageContent = new JLabel(); // Image logic xử lý sau.

        // Tour name
        tourNameLabel = new JLabel("Tên Tour:");
        tourNameContent = new JTextField();
        tourNameContent.addActionListener(evt -> {
            String temp = tourNameContent.getText();
            if (temp.matches("^[a-zA-ZÀ-ỹ ]+$")) {
                tourName = temp;
                System.out.println("Tour Name: " + tourName);
            } else {
                System.out.println("Invalid input for Tour Name");
                tourNameContent.setText("");
            }
        });

        // Duration
        durationLabel = new JLabel("Thời gian:");
        durationContent = new JTextField();
        durationContent.addActionListener(evt->
        {
            
                String durationTemp = durationContent.getText();
                if (durationTemp.matches("\\d+")) 
                {
                    duration = Integer.parseInt(durationTemp);
                    System.out.println("Duration: " + duration);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    durationContent.setText("");
                }
            
        });

        // Departure Date
        departureDateLabel = new JLabel("Ngày khởi hành:");
        departureDateContent = new JTextField();
        departureDateContent.addActionListener(evt->
        {
            
                String departureDateTemp = departureDateContent.getText();
                if (departureDateTemp.matches("\\d{4}-\\d{2}-\\d{2}"))
                {
                    String[] dateParts = departureDateTemp.split("-");
                    
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int day = Integer.parseInt(dateParts[2]);
                    
                    departureDate = LocalDate.of(year, month, day);
                    
                    System.out.println("Departure Date: " + departureDate);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    departureDateContent.setText("");
                }
            
        });
        
        // Departure Time
        departureTimeLabel = new JLabel("Giờ khởi hành:");
        departureTimeContent = new JTextField();
        departureTimeContent.addActionListener(evt->
        {
            
                String departureTimeTemp = departureTimeContent.getText();
                if (departureTimeTemp.matches("\\d{2}-\\d{2}"))
                {
                    String[] timeParts = departureTimeTemp.split("-");
                    
                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);
                    
                    departureTime = LocalTime.of(hour, minute);
                    
                    System.out.println("Departure Time: " + departureTime);

                } 
                else 
                {
                    System.out.println("Invalid input");
                    departureTimeContent.setText("");
                }
            
        });

        // Departure Location
        departureLocationLabel = new JLabel("Điểm Khởi Hành:");
        departureLocationContent = new JTextField();
        departureLocationContent.addActionListener(evt -> {
            String temp = departureLocationContent.getText();
            if (temp.matches("^[a-zA-ZÀ-ỹ ]+$")) {
                departureLocation = temp;
                System.out.println("Departure Location: " + departureLocation);
            } else {
                System.out.println("Invalid input for Departure Location");
                departureLocationContent.setText("");
            }
        });

        // Destination
        destinationLabel = new JLabel("Điểm Đến:");
        destinationContent = new JTextField();
        destinationContent.addActionListener(evt -> 
        {
            String temp = destinationContent.getText();
            if (temp.matches("^[A-Za-z ]+$")) 
            {
                destination = temp;
                System.out.println("Destination: " + destination);
            } 
            else 
            {
                System.out.println("Invalid input for Destination");
                destinationContent.setText("");
            }
        });

        // Adult Price
        adultPriceLabel = new JLabel("Giá người lớn:");
        adultPriceContent = new JTextField();
        adultPriceContent.addActionListener(evt->
        {
            
                String adultPriceTemp = adultPriceContent.getText();
                if (adultPriceTemp.matches("\\d+")) 
                {
                    adultPrice = Double.parseDouble(adultPriceTemp);
                    System.out.println("Adult Price: " + adultPrice);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    adultPriceContent.setText("");
                }
            
        });

        // Child Price
        childPriceLabel = new JLabel("Giá trẻ em:");
        childPriceContent = new JTextField();
        childPriceContent.addActionListener(evt->
        {
            
                String childPriceTemp = childPriceContent.getText();
                if (childPriceTemp.matches("\\d+")) 
                {
                    childPrice = Double.parseDouble(childPriceTemp);
                    System.out.println("Child Price: " + childPrice);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    childPriceContent.setText("");
                }
            
        });

        // Max Participants
        maxParticipantsLabel = new JLabel("Số lượng tối đa:");
        maxParticipantsContent = new JTextField();
        maxParticipantsContent.addActionListener(evt->
        {
           
                String maxParticipantsTemp = maxParticipantsContent.getText();
                if (maxParticipantsTemp.matches("\\d+")) 
                {
                    maxParticipants = Integer.parseInt(maxParticipantsTemp);
                    System.out.println("Max Participants: " + maxParticipants);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    maxParticipantsContent.setText("");
                }
            
        });
        
        currentParticipantsLabel = new JLabel("Số người hiện tại:");
        currentParticipantsContent = new JTextField();
        currentParticipantsContent.addActionListener(evt->
        {
           
                String currentParticipantsTemp = currentParticipantsContent.getText();
                if (currentParticipantsTemp.matches("\\d+")) 
                {
                	currentParticipants = Integer.parseInt(currentParticipantsTemp);
                    System.out.println("Current Participants: " + currentParticipants);
                } 
                else 
                {
                    System.out.println("Invalid input");
                    currentParticipantsContent.setText("");
                }
            
        });

        // Transport Info (ComboBox)
        transportInfoLabel = new JLabel("Phương tiện:");
        transportInfoContent = new JComboBox<>();
        transportInfoContent.addItem("Xe");
        transportInfoContent.addItem("Máy bay");
        transportInfoContent.addItem("Tàu");
        transportInfoContent.addActionListener(evt->
        {
            
                transportInfo = (String) transportInfoContent.getSelectedItem();
                System.out.println("Transport Info: " + transportInfo);
            
        });

        // Description
        descriptionLabel = new JLabel("Mô Tả:");
        descriptionContent = new JTextArea(5,30);
        descriptionContent.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionContent.addKeyListener(new KeyAdapter() 
        {
        	  @Override
              public void keyPressed(KeyEvent e) 
        	  {
                  if (e.getKeyCode() == KeyEvent.VK_ENTER) 
                  {
                      // Save text to a variable
                	  String descriptionTemp = descriptionContent.getText();
                      if (descriptionTemp.matches("^[a-zA-ZÀ-ỹ .,\\n]+$"))
                      {
                          description = descriptionTemp;
                          System.out.println("Description: " + description);
                      } 
                      else 
                      {
                          System.out.println("Invalid input for Description");
                          descriptionContent.setText("");
                      }
                  }
              }
        
        });

        

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
            imageContent.setIcon(new ImageIcon(tour.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));
            tourNameContent.setText(tourName);
            descriptionContent.setText(description);
            departureDateContent.setText(departureDate.toString());
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
        

        // Add tất cả các fields vào formPanel.
        formPanel.add(tourIdLabel);
        formPanel.add(tourIdContent);
        
        formPanel.add(imageLabel);
        formPanel.add(imageContent);
        
        formPanel.add(tourNameLabel);
        formPanel.add(tourNameContent);                
        
        formPanel.add(departureDateLabel);
        formPanel.add(departureDateContent);
        
        formPanel.add(durationLabel);
        formPanel.add(durationContent);
        
        formPanel.add(departureLocationLabel);
        formPanel.add(departureLocationContent);
        
        formPanel.add(departureTimeLabel);
        formPanel.add(departureTimeContent);
        
        formPanel.add(destinationLabel);
        formPanel.add(destinationContent);
        
        formPanel.add(transportInfoLabel);
        formPanel.add(transportInfoContent);
        
        formPanel.add(adultPriceLabel);
        formPanel.add(adultPriceContent);
        
        formPanel.add(childPriceLabel);
        formPanel.add(childPriceContent);
        
        formPanel.add(maxParticipantsLabel);
        formPanel.add(maxParticipantsContent);
        
        formPanel.add(currentParticipantsLabel);
        formPanel.add(currentParticipantsContent);
        
        formPanel.add(statusLabel);
        formPanel.add(statusContent);
        
        formPanel.add(descriptionLabel);
        formPanel.add(new JScrollPane(descriptionContent));
       
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");
        addImageButton = new JButton("Thêm hình");

        // Action for the Save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	tour.setTourId(tourId);
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
                departureDateContent.setText(departureDate.toString());
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
                
        
        add(formPanel, BorderLayout.CENTER);
        add(functionButton, BorderLayout.NORTH);
        add(formButtonPanel, BorderLayout.SOUTH);
    }

    private void enableEditing(boolean enable) 
    {
        tourIdContent.setEnabled(enable);
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


    private void showInvalidInputMessage(JTextField field) {
        JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        field.setText("");
    }
}
