package gui.admin;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;

import entity.Order;
import entity.Tour;
import dao.Tour_DAO;

public class TourManagement extends JPanel {
    private int totalPages = 1;
    private int currentPage = 1;
    private int rowPerPage = 10;

    private JPanel tourListPanel;
    private JPanel functionButtonPanel;
    private JPanel pageControlButtonPanel;

    private JTextField searchTextField;

    private JLabel pageNumber;

    private Tour_DAO tourDAO = new Tour_DAO();

    private ArrayList<Tour> tours = new ArrayList<>();

    public TourManagement() {
        setLayout(new BorderLayout(10, 10));

        tourListPanel = new JPanel();
        tourListPanel.setLayout(new GridLayout(10, 1));

        pageControlButtonPanel = new JPanel();
        pageControlButtonPanel.setBackground(Color.PINK);
        pageControlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        functionButtonPanel = new JPanel();
        functionButtonPanel.setBackground(Color.CYAN);
        functionButtonPanel.setLayout(new BorderLayout());

        pageNumber = new JLabel("Page: " + currentPage);

        JButton addTourButton = new JButton("Thêm");
        addTourButton.addActionListener(new AddTourDialog());

        JButton previousPage = new JButton("<");
        previousPage.addActionListener(evt -> previousPagePanel(evt));

        JButton nextPage = new JButton(">");
        nextPage.addActionListener(evt -> nextPagePanel(evt));

        searchTextField = new JTextField("Nhập mã tour để tìm");
        searchTextField.addMouseListener(new MouseAdapter() 
        {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		searchTextField.setText("");
        	}
        });
        searchTextField.addActionListener(evt -> {
            String tempTourId = searchTextField.getText();
            if (tourDAO.checkExistById(tempTourId)) 
            {
                tourListPanel.removeAll();
                JPanel row = createTourRow(tourDAO.getByTourId(tempTourId), 0);
                tourListPanel.add(row);
                tourListPanel.revalidate();
                tourListPanel.repaint();
            } 
            else if (tempTourId.equals("")) 
            {
                loadTourData();
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Không tìm thấy tour nào có mã " + tempTourId,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        functionButtonPanel.add(addTourButton, BorderLayout.EAST);
        functionButtonPanel.add(searchTextField, BorderLayout.WEST);

        pageControlButtonPanel.add(previousPage);
        pageControlButtonPanel.add(pageNumber);
        pageControlButtonPanel.add(nextPage);

        JScrollPane scrollBar = new JScrollPane(tourListPanel);

        add(functionButtonPanel, BorderLayout.NORTH);
        add(scrollBar, BorderLayout.CENTER);
        add(pageControlButtonPanel, BorderLayout.SOUTH);

        loadTourData();
    }

    private void loadTourData() {
        tours = tourDAO.getAll();
        totalPages = (int) Math.ceil((double) tours.size() / rowPerPage);
        updatePage();
    }

    private void updatePage() {
        tourListPanel.removeAll();

        int start = (currentPage - 1) * rowPerPage;
        int end = Math.min(start + rowPerPage, tours.size());

        for (int i = start; i < end; i++) {
            JPanel row = createTourRow(tours.get(i), i - start);
            tourListPanel.add(row);
        }

        for (int i = end - start; i < rowPerPage; i++) {
            JPanel emptyRow = new JPanel();
            emptyRow.setBackground(Color.WHITE);
            tourListPanel.add(emptyRow);
        }

        pageNumber.setText("Page: " + currentPage);
        tourListPanel.revalidate();
        tourListPanel.repaint();
    }

    private JPanel createTourRow(Tour tour, int indexInPage) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel idLabel = new JLabel(tour.getTourId());
        JLabel nameLabel = new JLabel(tour.getTourName());
        JLabel statusLabel = new JLabel(tour.getStatus());
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(tour.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));

        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        row.add(imageLabel);
        row.add(idLabel);
        row.add(nameLabel);
        row.add(statusLabel);

        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.putClientProperty("tour", tour);
        
        row.addMouseListener(new TourDetailControl(this));

        return row;
    }

    private void previousPagePanel(ActionEvent e) {
        if (currentPage > 1) {
            currentPage--;
            updatePage();
        }
    }

    private void nextPagePanel(ActionEvent e) {
        if (currentPage < totalPages) {
            currentPage++;
            updatePage();
        }
    }
    
    private class TourDetailControl extends MouseAdapter 
    {
        private TourManagement tourManagement;

        public TourDetailControl(TourManagement tourManagement) 
        {
            this.tourManagement = tourManagement;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JPanel sourcePanel = (JPanel) e.getSource();
            Tour tour = (Tour) sourcePanel.getClientProperty("tour");

            tourManagement.removeAll();
            TourDetail tourDetail = new TourDetail(tour);
            tourManagement.add(tourDetail);
            tourManagement.revalidate();
            tourManagement.repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) 
        {
            JPanel tempPanel = (JPanel) e.getSource();
            tempPanel.setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) 
        {
            JPanel tempPanel = (JPanel) e.getSource();
            tempPanel.setBackground(Color.WHITE);
        }
    }

    private class AddTourDialog implements ActionListener 
    {
    	private JPanel formInfoPanel;
    	private JPanel formPanel;
    	private JPanel descriptionPanel;
    	private JPanel functionButtonPanel;
    	
        private JLabel imageLabel;
        private JLabel tourNameLabel;
        private JLabel durationLabel;
        private JLabel departureDateLabel;
        private JLabel departureTimeLabel;
        private JLabel departureLocationLabel;
        private JLabel destinationLabel;
        private JLabel adultPriceLabel;
        private JLabel childPriceLabel;
        private JLabel maxParticipantsLabel;
        private JLabel transportInfoLabel;
        private JLabel descriptionLabel;
        
        private JLabel imageContent;
        private JTextField tourNameContent;
        private JTextField durationContent;
        private JTextField departureDateContent;
        private JTextField departureTimeContent;
        private JTextField departureLocationContent;
        private JTextField destinationContent;
        private JTextField adultPriceContent;
        private JTextField childPriceContent;
        private JTextField maxParticipantsContent;
        
        private JTextArea descriptionContent;
        
        private JComboBox<String> transportInfoContent;
        
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
        private String transportInfo;
        
        private String base64Image;
        
        public void actionPerformed(ActionEvent e) 
        {

            JDialog addDialog = new JDialog();
            addDialog.setAlwaysOnTop(true);
            addDialog.setLayout(new BorderLayout());
            

            // Image (for illustration, no functionality added here)
            imageLabel = new JLabel("Hình ảnh:");
            imageContent = new JLabel("Chưa có hình");
            
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
                          } else {
                              System.out.println("Invalid input for Description");
                              descriptionContent.setText("");
                          }
                      }
                  }
            });
            
            formInfoPanel = new JPanel();
            formInfoPanel.setLayout(new GridLayout(11,2,10,10));

            // Add components to the dialog
            formInfoPanel.add(imageLabel);
            formInfoPanel.add(imageContent);  // Placeholder for image selection (you can add image picker here)

            formInfoPanel.add(tourNameLabel);
            formInfoPanel.add(tourNameContent);

            formInfoPanel.add(durationLabel);
            formInfoPanel.add(durationContent);

            formInfoPanel.add(departureDateLabel);
            formInfoPanel.add(departureDateContent);  // You can replace with a date picker

            formInfoPanel.add(departureTimeLabel);
            formInfoPanel.add(departureTimeContent);  // You can replace with a time picker

            formInfoPanel.add(departureLocationLabel);
            formInfoPanel.add(departureLocationContent);

            formInfoPanel.add(destinationLabel);
            formInfoPanel.add(destinationContent);

            formInfoPanel.add(adultPriceLabel);
            formInfoPanel.add(adultPriceContent);

            formInfoPanel.add(childPriceLabel);
            formInfoPanel.add(childPriceContent);

            formInfoPanel.add(maxParticipantsLabel);
            formInfoPanel.add(maxParticipantsContent);

            formInfoPanel.add(transportInfoLabel);
            formInfoPanel.add(transportInfoContent);

            
            descriptionPanel = new JPanel();
            descriptionPanel.setLayout(new BorderLayout());
            descriptionPanel.add(new JScrollPane(descriptionContent));
            
            formPanel= new JPanel();
            formPanel.setLayout(new BorderLayout(10,10));
            formPanel.add(formInfoPanel, BorderLayout.CENTER);
            formPanel.add(descriptionPanel, BorderLayout.SOUTH);

         
            JButton saveButton = new JButton("Lưu");
            JButton cancelButton = new JButton("Hủy");
            JButton addImageButton = new JButton("Thêm hình");

            // Action for the Save button
            saveButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                   if(base64Image!=null)
                   {
                	   // Create the tour object
                	   Tour tour = new Tour(tourDAO.generateNextTourId(),  image,  tourName,  description,  departureDate,  duration,  departureLocation, 
                               departureTime,  destination,  transportInfo,  adultPrice,  childPrice, 
                               maxParticipants,  0,  Tour.STATUS_AVAILABLE);
                      
                      
                      // Save or process the tour object here
                      tourDAO.add(tour, base64Image);
                      System.out.println("Tour saved: " + tour);
                      
                      addDialog.dispose();  // Close the dialog after saving

                   }
                   else
                   {
                       JOptionPane.showMessageDialog(addDialog, "Chưa thêm hình!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                   }
                    
                }
            });

            // Action for the Cancel button
            cancelButton.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent e) 
                {
                	image=null;
                    tourName=null;
                    description=null;
                    duration=0;
                    departureDate=null;
                    departureTime=null;
                    departureLocation=null;
                    destination=null;
                    adultPrice=0;
                    childPrice=0;
                    maxParticipants=0;
                    transportInfo=null;
                    
                    base64Image=null;
                    
                    addDialog.dispose();  
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

                    int result = filechooser.showOpenDialog(addDialog);
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


            functionButtonPanel = new JPanel();           
            functionButtonPanel.add(saveButton);
            functionButtonPanel.add(cancelButton);
            functionButtonPanel.add(addImageButton);
            
            addDialog.add(new JScrollPane(formPanel), BorderLayout.NORTH);
            addDialog.add(new JScrollPane(descriptionContent), BorderLayout.CENTER);
            addDialog.add(functionButtonPanel, BorderLayout.SOUTH);
            
          
            
            addDialog.setSize(400, 600);  // Set a suitable size for the dialog
            addDialog.setLocationRelativeTo(null);
            addDialog.setVisible(true);
        }
    }


}
