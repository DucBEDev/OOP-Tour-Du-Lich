package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.event.MouseAdapter;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
		inputTimeLabel.addMouseListener(new StatisticModeChange());
		
		yearLabel = new JLabel("Thống kê theo năm nhất định");
		yearLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		yearLabel.addMouseListener(new StatisticModeChange());
		
		allTimeLabel = new JLabel("Thống kê toàn bộ");
		allTimeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
			
			for(int i =0; i<12; i++)
			{
				totalByMonthPanel.add(new JLabel("Tháng " + (i+1)));
				if(totalAmountAllTimeList.containsKey(i+1)) totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": " + totalAmountAllTimeList.get(i+1)));
				else totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": 0" ));
			}
			
			
			centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(1,2));
			
			centerPanel.add(totalByMonthPanel);
			centerPanel.add(new JScrollPane(destinationTable));
	        
	        totalAmountPanel= new JPanel();
	        
	        totalAmountLabel = new JLabel("Tổng doanh thu: " + orderDAO.getTotalAmountAllTime());
	        
	        totalAmountPanel.add(totalAmountLabel, FlowLayout.LEFT);
	        
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
	        
	     }
	}
		
		 // Hiện thông tin đơn hàng
	  
	
	private class ByYearPanel extends JPanel
	{
		private JPanel centerPanel;
		private JPanel totalAmountPanel;
		private JPanel totalByMonthPanel;
		
		private JLabel totalAmountLabel;
		
		private JTable destinationTable;
		
		private Order_DAO orderDAO= new Order_DAO();
		
		private HashMap<String, Integer> list;
		
		private HashMap<Integer, Double> totalAmountByMonthList;
		
		public  ByYearPanel()
		{
			setLayout(new BorderLayout());
					
			
			list = orderDAO.getMostTravelDestinationByYear(2024);
			
	        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Điểm đến", "Số lần được đăng ký"}, 0);
	        
	        for (Entry<String, Integer> entry : list.entrySet())
			{
				tableModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
			}
				
			destinationTable = new JTable(tableModel);
			
			
			
			totalByMonthPanel = new JPanel();
			totalByMonthPanel.setLayout(new GridLayout(12,2,10, 10));
			
			totalAmountByMonthList = orderDAO.getTotalAmountByYear(2024);
			
			for(int i =0; i<12; i++)
			{
				totalByMonthPanel.add(new JLabel("Tháng " + (i+1)));
				if(totalAmountByMonthList.containsKey(i+1)) totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": " + totalAmountByMonthList.get(i+1)));
				else totalByMonthPanel.add(new JLabel("Doanh thu tháng " +(i+1) +": 0" ));
			}
			
			
			centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(1,2));
			
			centerPanel.add(totalByMonthPanel);
			centerPanel.add(new JScrollPane(destinationTable));
	        
	        totalAmountPanel= new JPanel();
	        
	        totalAmountLabel = new JLabel("Tổng doanh thu: " + orderDAO.getTotalAmountAllTime());
	        
	        totalAmountPanel.add(totalAmountLabel, FlowLayout.LEFT);
	        
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
		}
		
		
	}
	
	private class InputTimePanel extends JPanel
	{
		private JPanel centerPanel;
		private JPanel totalAmountPanel;
		private JPanel tourListPanel;

		private JLabel totalAmountLabel;
		
		private JTable destinationTable;
		private JTable tourTable;
		
		private Order_DAO orderDAO = new Order_DAO();
		
		private HashMap<String, Integer> list;
		
		private ArrayList<Tour> tourList;
		
		public InputTimePanel()
		{
			setLayout(new BorderLayout());
					
			
			list = orderDAO.getMostTravelDestinationByYear(2024);
			
	        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Điểm đến", "Số lần được đăng ký"}, 0);
	        
	        for (Entry<String, Integer> entry : list.entrySet())
			{
				tableModel.addRow(new Object[] {entry.getKey(), entry.getValue()});
			}
				
			destinationTable = new JTable(tableModel);
			
			tourListPanel = new JPanel();
			tourListPanel.setLayout(new GridLayout(10,0));
			
			tourList = orderDAO.getMostTravelTourByInputTime(LocalDate.of(2024, 01, 01), LocalDate.of(2024, 12,31));
			
			for(int i =0; i<10; i++)
			{
				if(i >= 0 && i < tourList.size())
				{
					JPanel row = new JPanel(new GridLayout(1, 5));
			        row.setBackground(Color.WHITE);
			        row.setBorder(BorderFactory.createLineBorder(Color.BLACK));

			        JLabel idLabel = new JLabel(tourList.get(i).getTourId());
			        JLabel nameLabel = new JLabel(tourList.get(i).getTourName());
			        JLabel statusLabel = new JLabel(tourList.get(i).getStatus());
			        JLabel imageLabel = new JLabel();
			        imageLabel.setIcon(new ImageIcon(tourList.get(i).getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH)));

			        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
			        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
			        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

			        row.add(imageLabel);
			        row.add(idLabel);
			        row.add(nameLabel);
			        row.add(statusLabel);
			        
			        tourListPanel.add(row);
				}				
				
			}
			
			totalAmountPanel= new JPanel();
	        
	        totalAmountLabel = new JLabel("Tổng doanh thu: " + orderDAO.getTotalAmountAllTime());
	        
	        centerPanel = new JPanel();
	        centerPanel.setLayout(new GridLayout(0,2));
	        
	        centerPanel.add(new JScrollPane(tourListPanel));
	        centerPanel.add(new JScrollPane(destinationTable));
	        
	        add(centerPanel, BorderLayout.CENTER);
	        add(totalAmountPanel, BorderLayout.SOUTH);
		}
	}
}
