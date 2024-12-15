package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.MouseAdapter;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import dao.Order_DAO;
import entity.Order;
import entity.Tour;

public class Statistic extends JPanel
{
	private JPanel currentPanel;
	private JPanel functionPanel;
	
	private JLabel inputTimeLabel;
	private JLabel yearLabel;
	private JLabel allTimeLabel;
	
	
	public Statistic()
	{
		setLayout(new BorderLayout());
		
		inputTimeLabel = new JLabel("Thống kê theo 1 khoảng thời gian");
		inputTimeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		inputTimeLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		inputTimeLabel.addMouseListener(new StatisticModeChange());
		
		yearLabel = new JLabel("Thống kê theo năm nhất định");
		yearLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		yearLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		yearLabel.addMouseListener(new StatisticModeChange());
		
		allTimeLabel = new JLabel("Thống kê toàn bộ");
		allTimeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		allTimeLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		allTimeLabel.addMouseListener(new StatisticModeChange());
		
		functionPanel = new JPanel();
		
		functionPanel.add(allTimeLabel, FlowLayout.LEFT);
		functionPanel.add(yearLabel, FlowLayout.LEFT);
		functionPanel.add(inputTimeLabel, FlowLayout.LEFT);
		
		add(functionPanel, BorderLayout.NORTH);

	}
	
	private class StatisticModeChange extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(currentPanel!=null)
			{
				remove(currentPanel);
			}
			
			if(e.getSource() == allTimeLabel)
			{
				currentPanel = new AllTimePanel();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource() == yearLabel)
			{
				currentPanel = new ByYearPanel();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource()==inputTimeLabel)
			{
				currentPanel = new InputTimePanel();
				add(currentPanel, BorderLayout.CENTER);
			}
		  
		    revalidate();
		    repaint();
		} 
	}
	
	
	// Doanh thu tổng
	private class AllTimePanel extends JPanel
	{
		private JPanel centerPanel;
		private JPanel totalByMonthPanel;
		private JPanel destinationsPanel;
		private JPanel totalAmountPanel;
		private JPanel pageControlButtonPanel;
		
		private JLabel pageNumber;
		private JLabel totalAmountLabel;
		
		private JTable destinationTable;
		
		private Order_DAO orderDAO= new Order_DAO();
		
		private HashMap<String, Integer> list;
		
		private HashMap<Integer, Double> totalAmountAllTimeList;
		
		public  AllTimePanel()
		{
			setLayout(new BorderLayout());
			
			list = orderDAO.getMostTravelDestinationAllTime();
			
	        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Điểm đến", "Số lần được đăng ký"}, 0);
	        
	        for (Entry<String, Integer> entry : list.entrySet())
			{
				tableModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
			}
				
			destinationTable = new JTable(tableModel);
   	       
			totalByMonthPanel = new JPanel();
			totalByMonthPanel.setLayout(new GridLayout(12,2,10, 10));
			
			totalAmountAllTimeList = orderDAO.getTotalAmountAllTime();
			for (Entry<Integer, Double> entry : totalAmountAllTimeList.entrySet())
			{
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
			}
			
			double totalAmountAllTime = 0;
			
			for(int i =0; i<12; i++)
			{
				totalByMonthPanel.add(new JLabel("Tháng " + (i+1)));
				if(totalAmountAllTimeList.containsKey(i+1)) 
					{
						DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
						String formattedTotalAmountByMonth = decimalFormat.format(totalAmountAllTimeList.get(i+1));
						totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": " + formattedTotalAmountByMonth));
						totalAmountAllTime +=totalAmountAllTimeList.get(i+1);
					}
				else totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": 0" ));
				
				
			}
			
			DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
            String formattedTotalAmountAllTime = decimalFormat.format(totalAmountAllTime);
		        
		     totalAmountLabel = new JLabel("Tổng doanh thu: " + formattedTotalAmountAllTime);
			
			centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(1,2));
			
			centerPanel.add(totalByMonthPanel);
			centerPanel.add(new JScrollPane(destinationTable));
	        
	        totalAmountPanel= new JPanel();
	        
	        
	        totalAmountPanel.add(totalAmountLabel, FlowLayout.LEFT);
	        
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
	        
	     }
	}
	
		
	// Doanh thu theo năm nhất định	
	private class ByYearPanel extends JPanel
	{
		private JPanel centerPanel;
		private JPanel totalAmountPanel;
		private JPanel totalByMonthPanel;
		private JPanel inputPanel;
		
		private JLabel totalAmountLabel;
		
		private JTable destinationTable;
		
		private JTextField yearInputTf;
		
		private JButton accountingButton;
		
		private Order_DAO orderDAO= new Order_DAO();
		
		private HashMap<String, Integer> list;
		
		private HashMap<Integer, Double> totalAmountByMonthList;
		
		public  ByYearPanel()
		{
			setLayout(new BorderLayout());
			
			inputPanel = new JPanel();
			
			yearInputTf = new JTextField(4);
			
			accountingButton = new JButton("Thống kê");
			accountingButton.addActionListener(e->
			{
				centerPanel.removeAll();
				totalAmountPanel.removeAll();

				list = orderDAO.getMostTravelDestinationByYear(Integer.parseInt(yearInputTf.getText()));
				
		        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Điểm đến", "Số lần được đăng ký"}, 0);
		        
		        for (Entry<String, Integer> entry : list.entrySet())
				{
					tableModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
				}
					
				destinationTable = new JTable(tableModel);
				
								
				totalByMonthPanel = new JPanel();
				totalByMonthPanel.setLayout(new GridLayout(12,2,10, 10));
				
				totalAmountByMonthList = orderDAO.getTotalAmountByYear(Integer.parseInt(yearInputTf.getText()));
				double totalAmountByYear = 0;
				
				for(int i =0; i<12; i++)
				{
					totalByMonthPanel.add(new JLabel("Tháng " + (i+1)));
					if(totalAmountByMonthList.containsKey(i+1)) 
						{
							DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
							String formattedTotalAmountByMonth = decimalFormat.format(totalAmountByMonthList.get(i+1));
							totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": " + formattedTotalAmountByMonth));
							totalAmountByYear += totalAmountByMonthList.get(i+1);
						}
					
					else totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": 0" ));
					
				}
				
				centerPanel.add(totalByMonthPanel);
				centerPanel.add(new JScrollPane(destinationTable));
				
				DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
	            String formattedTotalAmountByYear = decimalFormat.format(totalAmountByYear);
			        
			     totalAmountLabel = new JLabel("Tổng doanh thu: " + formattedTotalAmountByYear);
			        
			     totalAmountPanel.add(totalAmountLabel, FlowLayout.LEFT);
				
				this.revalidate();
			    this.repaint();
			});
					
			inputPanel.add(new JLabel("Tổng doanh thu trong năm:"));
			inputPanel.add(yearInputTf);
			inputPanel.add(accountingButton);									
			
			centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(1,2));
			
			totalAmountPanel= new JPanel();	        
	       
	        add(inputPanel, BorderLayout.NORTH);
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
		}	
	}
	
	
	// Doanh thu theo thời gian nhất định
	private class InputTimePanel extends JPanel
	{
		private JPanel centerPanel;
		private JPanel totalAmountPanel;
		private JPanel tourListPanel;
		private JPanel inputPanel;
		
		private JLabel totalAmountLabel;
		
		private JTable destinationTable;
		
		private JDateChooser beginDate;
		private JDateChooser endDate;
		
		private JButton accountingButton;
		
		private Order_DAO orderDAO = new Order_DAO();
		
		private HashMap<String, Integer> list;
		
		private ArrayList<Tour> tourList;
		
		public InputTimePanel()
		{
			setLayout(new BorderLayout());
			
			inputPanel = new JPanel();
			
			beginDate = new JDateChooser();
			beginDate.setPreferredSize(new Dimension(100, 35));
			beginDate.setDateFormatString("dd/MM/yyyy");
			beginDate.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
	            BorderFactory.createEmptyBorder(5, 5, 5, 5)
	        ));
			
			endDate = new JDateChooser();
			endDate.setPreferredSize(new Dimension(100, 35));
			endDate.setDateFormatString("dd/MM/yyyy");
			endDate.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
	            BorderFactory.createEmptyBorder(5, 5, 5, 5)
	        ));
			
			accountingButton = new JButton("Thống kê");
			accountingButton.addActionListener(e->
			{
				centerPanel.removeAll();
				totalAmountPanel.removeAll();
				
				list = orderDAO.getMostTravelDestinationByInputTime(beginDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				tourList = orderDAO.getMostTravelTourByInputTime(beginDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

				DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Điểm đến", "Số lần được đăng ký"}, 0);
		        
		        if(list!=null)
		        {
		        	for (Entry<String, Integer> entry : list.entrySet())
					{
						tableModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
					}

		        }
		        
				destinationTable = new JTable(tableModel);

				tourListPanel = new JPanel();
				tourListPanel.setLayout(new GridLayout(10,0));
				
				
				for(int i =0; i<10; i++)
				{
					if(i >= 0 && tourList!=null && i < tourList.size())
					{
						JPanel wrapperPanel = new JPanel(new GridLayout(1, 1));
					    wrapperPanel.setBackground(Color.white);
				    	
				        JPanel row = new JPanel(new GridLayout(1, 5));
				        row.setBackground(Color.WHITE);
				        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

				        JLabel idLabel = new JLabel("Mã tour: "+tourList.get(i).getTourId());
				        JLabel nameLabel = new JLabel("Tên tour"+tourList.get(i).getTourName());
				        JLabel statusLabel = new JLabel("Trạng thái: " + tourList.get(i).getStatus());
				        JLabel departureDateLabel = new JLabel("Ngày khởi hành: " + tourList.get(i).getDepartureDate());
				        JLabel durationLabel = new JLabel("Thời gian tour: " + tourList.get(i).getDuration());
				        JLabel departureLocationLabel = new JLabel("Địa điểm khởi hành: " + tourList.get(i).getDepartureLocation());
				        JLabel destinationLabel = new JLabel("Địa điểm đến: " + tourList.get(i).getDestination());
				        JLabel transportInfoLabel = new JLabel("Phương tiện di chuyển: " + tourList.get(i).getTransportInfo());
				        JLabel imageLabel = new JLabel();
				        imageLabel.setIcon(new ImageIcon(tourList.get(i).getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));

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
				        
				        tourListPanel.add(row);
					}									
				}
				
				centerPanel.add(new JScrollPane(tourListPanel));
		        centerPanel.add(new JScrollPane(destinationTable));
		        
		        DecimalFormat decimalFormat = new DecimalFormat("#,###");                 
	            String formattedTotalAmountInputTime = decimalFormat.format(orderDAO.getTotalAmountInputTime(beginDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), 
		        		endDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
		        
		        totalAmountLabel = new JLabel("Tổng doanh thu: " + formattedTotalAmountInputTime);
		        totalAmountPanel.add(totalAmountLabel);
		        
			    this.revalidate();
			    this.repaint();
			});
			
			inputPanel.add(new JLabel("Từ"));
			inputPanel.add(beginDate);
			inputPanel.add(new JLabel("Dến"));
			inputPanel.add(endDate);
			inputPanel.add(accountingButton);
				        			
			totalAmountPanel= new JPanel();	        
	        centerPanel = new JPanel();
	        centerPanel.setLayout(new GridLayout(0,2));	        	        
	        
	        add(inputPanel, BorderLayout.NORTH);
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
		}		
	}
}
