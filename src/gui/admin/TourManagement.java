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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import com.toedter.calendar.JDateChooser;


import javax.imageio.ImageIO;
import javax.swing.*;

import com.toedter.calendar.JDateChooser;

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
        setLayout(new BorderLayout());

        tourListPanel = new JPanel();
        tourListPanel.setLayout(new GridLayout(10, 1));

        pageControlButtonPanel = new JPanel();
        pageControlButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        functionButtonPanel = new JPanel();
        functionButtonPanel.setLayout(new BorderLayout());

        pageNumber = new JLabel("Page: " + currentPage);

        JButton addTourButton = new JButton("Thêm");
        addTourButton.addActionListener(e->{
        	new AddTourFrame();
        });

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

    // Tải danh sách thông tin tour
    void loadTourData() {
        tours = tourDAO.getAll();
        totalPages = (int) Math.ceil((double) tours.size() / rowPerPage);
        updatePage();
    }

    // Cập nhật các thông tin trên UI
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

    // Hiển thị các thông tin chủ yếu ở từng row
    private JPanel createTourRow(Tour tour, int indexInPage) 
    {
    	JPanel wrapperPanel = new JPanel(new GridLayout(1, 1));
	    wrapperPanel.setBackground(Color.white);
    	
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel idLabel = new JLabel("Mã tour: "+tour.getTourId());
        JLabel nameLabel = new JLabel("Tên tour: "+tour.getTourName());
        JLabel statusLabel = new JLabel("Trạng thái: " + tour.getStatus());
        JLabel departureDateLabel = new JLabel("Ngày khởi hành: " + tour.getDepartureDate());
        JLabel durationLabel = new JLabel("Thời gian tour: " + tour.getDuration());
        JLabel departureLocationLabel = new JLabel("Địa điểm khởi hành: " + tour.getDepartureLocation());
        JLabel destinationLabel = new JLabel("Địa điểm đến: " + tour.getDestination());
        JLabel transportInfoLabel = new JLabel("Phương tiện di chuyển: " + tour.getTransportInfo());
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(tour.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));

        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        departureDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        durationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        departureLocationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        destinationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        transportInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel column2 = new JPanel(new GridLayout(2, 1));
	    column2.setBackground( Color.white );
	    column2.add(idLabel);
	    column2.add(nameLabel);
	    
	    JPanel column3 = new JPanel(new GridLayout(2, 1));
	    column3.setBackground( Color.white );
	    column3.add(departureDateLabel);
	    column3.add(durationLabel);
	    
	    JPanel column4 = new JPanel(new GridLayout(2, 1));
	    column4.setBackground( Color.white );
	    column4.add(departureLocationLabel);
	    column4.add(destinationLabel);
	    
	    JPanel column5 = new JPanel(new GridLayout(2, 1));
	    column5.setBackground( Color.white );
	    column5.add(transportInfoLabel);
	    column5.add(statusLabel);
	    

        row.add(imageLabel);
        row.add(column2);
        row.add(column3);
        row.add(column4);
        row.add(column5);
        
        wrapperPanel.add(row);

        wrapperPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wrapperPanel.putClientProperty("tour", tour);
        
        wrapperPanel.addMouseListener(new TourDetailControl(this));

        return wrapperPanel;
    }
    
    // Chuyển trang trước
    private void previousPagePanel(ActionEvent e) {
        if (currentPage > 1) {
            currentPage--;
            updatePage();
        }
    }

    // Chuyển trang sau
    private void nextPagePanel(ActionEvent e) {
        if (currentPage < totalPages) {
            currentPage++;
            updatePage();
        }
    }
    
    // Chuyển panel khi bấm tour ở row
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
			JPanel tempPanel = (JPanel)e.getSource();
			JPanel row = (JPanel) tempPanel.getComponent(0);
			JPanel column2=(JPanel)row.getComponent(1);
			JPanel column3=(JPanel)row.getComponent(2);
			JPanel column4=(JPanel)row.getComponent(3);
			JPanel column5=(JPanel)row.getComponent(4);
			row.setBackground(Color.LIGHT_GRAY);
			column2.setBackground(Color.LIGHT_GRAY);
			column3.setBackground(Color.LIGHT_GRAY);
			column4.setBackground(Color.LIGHT_GRAY);
			column5.setBackground(Color.LIGHT_GRAY);

		}

		@Override
		public void mouseExited(MouseEvent e) 
		{
			JPanel tempPanel = (JPanel)e.getSource();
			JPanel row = (JPanel) tempPanel.getComponent(0);
			JPanel column2=(JPanel)row.getComponent(1);
			JPanel column3=(JPanel)row.getComponent(2);
			JPanel column4=(JPanel)row.getComponent(3);
			JPanel column5=(JPanel)row.getComponent(4);
			row.setBackground(Color.WHITE);
			column2.setBackground(Color.WHITE);
			column3.setBackground(Color.WHITE);	
			column4.setBackground(Color.WHITE);
			column5.setBackground(Color.WHITE);
		}
    }
}


// Tạo lớp thêm tour
class AddTourFrame  
{
	private JPanel formInfoPanel;
	private JPanel formPanel;
	private JPanel descriptionPanel;
	private JPanel functionButtonPanel;
	
	private JFrame addFrame;
	
	JButton saveButton = new JButton("Lưu");
    JButton cancelButton = new JButton("Hủy");
    JButton addImageButton = new JButton("Thêm hình");
	
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
    private JDateChooser departureDateContent;
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
    
    private Tour_DAO tourDAO = new Tour_DAO();
    
    public AddTourFrame()
    {

        addFrame = new JFrame();
        addFrame.setAlwaysOnTop(true);
        addFrame.setLayout(new BorderLayout());       

        // Image 
        imageLabel = new JLabel("Hình ảnh:");
        imageContent = new JLabel("Chưa có hình");
        
        // Tour name
        tourNameLabel = new JLabel("Tên Tour:");
        tourNameContent = new JTextField();

        // Duration
        durationLabel = new JLabel("Thời gian:");
        durationContent = new JTextField();

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
        
        // Departure Location
        departureLocationLabel = new JLabel("Điểm Khởi Hành:");
        departureLocationContent = new JTextField();

        // Destination
        destinationLabel = new JLabel("Điểm Đến:");
        destinationContent = new JTextField();

        // Adult Price
        adultPriceLabel = new JLabel("Giá người lớn:");
        adultPriceContent = new JTextField();

        // Child Price
        childPriceLabel = new JLabel("Giá trẻ em:");
        childPriceContent = new JTextField();

        // Max Participants
        maxParticipantsLabel = new JLabel("Số lượng tối đa:");
        maxParticipantsContent = new JTextField();

        // Transport Info (ComboBox)
        transportInfoLabel = new JLabel("Phương tiện:");
        transportInfoContent = new JComboBox<>();
        transportInfoContent.addItem("Xe");
        transportInfoContent.addItem("Máy bay");
        transportInfoContent.addItem("Tàu");

        // Description
        descriptionLabel = new JLabel("Mô Tả:");
        descriptionContent = new JTextArea(5,30);
        descriptionContent.setBorder(BorderFactory.createTitledBorder("Description"));
        
        formInfoPanel = new JPanel();
        formInfoPanel.setLayout(new GridLayout(11,2,10,10));

        // Add components to the dialog
        formInfoPanel.add(imageLabel);
        formInfoPanel.add(imageContent); 

        formInfoPanel.add(tourNameLabel);
        formInfoPanel.add(tourNameContent);

        formInfoPanel.add(durationLabel);
        formInfoPanel.add(durationContent);

        formInfoPanel.add(departureDateLabel);
        formInfoPanel.add(departureDateContent);  

        formInfoPanel.add(departureTimeLabel);
        formInfoPanel.add(departureTimeContent);  

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

        
        // Action for the Save button
        saveButtonFunction actionListener = new saveButtonFunction();
        saveButton.addActionListener(actionListener);

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
                
                addFrame.dispose();  
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

                int result = filechooser.showOpenDialog(addFrame);
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
					     System.out.println(base64Image);
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
        
        addFrame.add(new JScrollPane(formPanel), BorderLayout.NORTH);
        addFrame.add(new JScrollPane(descriptionContent), BorderLayout.CENTER);
        addFrame.add(functionButtonPanel, BorderLayout.SOUTH);          
        
        addFrame.setSize(400, 600); 
        addFrame.setLocationRelativeTo(null);
        addFrame.setVisible(true);
    }
    
    
    private boolean validateInput() {
      String tourNameTemp = tourNameContent.getText();    	
      String descriptionTemp = descriptionContent.getText();
      String durationTemp = durationContent.getText();
      String departureLocationTemp = departureLocationContent.getText();    
      String departureTimeTemp = departureTimeContent.getText();
      String destinationTemp = destinationContent.getText();
      String adultPriceTemp = adultPriceContent.getText();
      String childPriceTemp = childPriceContent.getText();
      String maxParticipantsTemp = maxParticipantsContent.getText();
    
      if (tourNameTemp.isEmpty() || !tourNameTemp.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Tên tour không hợp lệ! Vui lòng nhập tên hợp lệ.", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          tourNameContent.requestFocus();
          return false;
      }

      if (durationTemp.isEmpty() || !durationTemp.matches("\\d+")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Thời gian tour không hợp lệ! Vui lòng nhập số ngày.", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          durationContent.requestFocus();
          return false;
      }

      if (departureLocationTemp.isEmpty() || !departureLocationTemp.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Địa điểm khởi hành không hợp lệ!", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          departureLocationContent.requestFocus();
          return false;
      }

      if (departureTimeTemp.isEmpty() || !departureTimeTemp.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Giờ khởi hành không hợp lệ! Định dạng: HH:mm", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          departureTimeContent.requestFocus();
          return false;
      }

      if (destinationTemp.isEmpty() || !destinationTemp.matches("^[A-Za-zÀ-ỹĐđ0-9\\s-_.,]+$")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Điểm đến không hợp lệ!", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          destinationContent.requestFocus();
          return false;
      }


      if (adultPriceTemp.isEmpty() || !adultPriceTemp.matches("\\d+")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Giá vé người lớn không hợp lệ! Vui lòng nhập số tiền hợp lệ.", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          adultPriceContent.requestFocus();
          return false;
      }

      if (childPriceTemp.isEmpty() || !childPriceTemp.matches("\\d+")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Giá vé trẻ em không hợp lệ! Vui lòng nhập số tiền hợp lệ.", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          childPriceContent.requestFocus();
          return false;
      }

      if (maxParticipantsTemp.isEmpty() || !maxParticipantsTemp.matches("\\d+")) {
          JOptionPane.showMessageDialog(addFrame, 
              "Số người tối đa không hợp lệ! Vui lòng nhập số dương.", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          maxParticipantsContent.requestFocus();
          return false;
      }
      
      if (base64Image == null) {
    	  JOptionPane.showMessageDialog(addFrame, "Loi");
    	  return false;
      }
      
      if (descriptionTemp.isEmpty()) {
          JOptionPane.showMessageDialog(addFrame, 
              "Mô tả không được để trống!", 
              "Lỗi", 
              JOptionPane.ERROR_MESSAGE);
          descriptionContent.requestFocus();
          return false;
      }

      // Tất cả dữ liệu hợp lệ
      return true;
  }
    
    
    // Kiểm tra và lưu thông tin mới
    private class saveButtonFunction implements ActionListener
    {
    	public void actionPerformed(ActionEvent e) 
        {   
            // Validate input
            if (!validateInput()) 
            {
                System.out.println("Validation failed, returning...");
                return; // Stops execution here; user must click the button again
            }
            
            tourName= tourNameContent.getText().trim();
    		description= descriptionContent.getText().trim();
    		departureDate= departureDateContent.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    		duration= Integer.parseInt(durationContent.getText().trim());
    		departureLocation= departureLocationContent.getText().trim();
    		System.out.println(tourName);
        		
        		String[] timeParts = departureTimeContent.getText().trim().split(":");
               
        		int hour = Integer.parseInt(timeParts[0]);
        		int minute = Integer.parseInt(timeParts[1]);
               
               	departureTime = LocalTime.of(hour, minute);
               	
               	destination= destinationContent.getText().trim();
               	transportInfo= transportInfoContent.getSelectedItem().toString().trim();
               	adultPrice= Integer.parseInt(adultPriceContent.getText().trim());
               	childPrice= Integer.parseInt(childPriceContent.getText().trim());
               	maxParticipants= Integer.parseInt(maxParticipantsContent.getText().trim());

           if(base64Image!=null)
           {
        	   // Create the tour object
        	   Tour tour = new Tour(tourDAO.generateNextTourId(),  image,  tourName,  description,  departureDate,  duration,  departureLocation, 
                       departureTime,  destination,  transportInfo,  adultPrice,  childPrice, 
                       maxParticipants,  0,  Tour.STATUS_AVAILABLE);
              
              // Save or process the tour object here
              tourDAO.add(tour, base64Image);
              System.out.println("Tour saved: " + tour);
              
              addFrame.dispose();  
              
              TourManagement tourManagement = new TourManagement();
              tourManagement.loadTourData();
           }
           else
           {
               JOptionPane.showMessageDialog(addFrame, "Chưa thêm hình!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
           }
            
        }
    
    }
}

