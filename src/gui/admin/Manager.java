package gui.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import entity.Employee;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Manager extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel currentPanel;
	private Recive chatBox;

    private JLabel tourManagement;
    private JLabel customerManagement;
    private JLabel employeeManagement;
    private JLabel orderManagement;
    private JLabel statistic;
    private JLabel customerService;
    private JLabel logOut;
    
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Manager frame = new Manager(SignIn.getEmployee());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Manager(Employee employee) {
		ManagerControl mouselistener = new ManagerControl();

		// Initialize the content pane before adding components
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        // Title panel
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(33, 150, 243));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));

        
        JLabel lblHeaderTitle = new JLabel("Quản Lí Tour Du Lịch");
        lblHeaderTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
        
        titlePanel.add(lblHeaderTitle);
        
        JPanel functionPanel = new JPanel();
        functionPanel.setBackground(new Color(66, 165, 243));
        functionPanel.setLayout(new GridLayout(7,1));
        
        
        tourManagement = new JLabel("Quản Lý Tour");
        tourManagement.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tourManagement.setBackground(new Color(66, 165, 243));
        tourManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tourManagement.setOpaque(true); 
        tourManagement.addMouseListener(mouselistener);
        tourManagement.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/travel.png"))));
                
        
        customerManagement = new JLabel("Quản Lý Khách Hàng");
        customerManagement.setFont(new Font("Tahoma", Font.PLAIN, 14));
        customerManagement.setOpaque(true); 
        customerManagement.setBackground(new Color(66, 165, 243));
        customerManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerManagement.addMouseListener(mouselistener);
        customerManagement.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/customer.png"))));
        
       
        employeeManagement = new JLabel("Quản Lý Nhân Viên");
        employeeManagement.setFont(new Font("Tahoma", Font.PLAIN, 14));
        employeeManagement.setBackground(new Color(66, 165, 243));
        employeeManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeManagement.setOpaque(true); 
        employeeManagement.addMouseListener(mouselistener);

        employeeManagement.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/staff.png"))));

        

        orderManagement = new JLabel("Quản Lý Đơn");
        orderManagement.setFont(new Font("Tahoma", Font.PLAIN, 14));
        orderManagement.setBackground(new Color(66, 165, 243));
        orderManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        orderManagement.setOpaque(true); 
        orderManagement.addMouseListener(mouselistener);
        orderManagement.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/ticket.png"))));
        
        
        statistic = new JLabel("Thống Kê");
        statistic.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statistic.setBackground(new Color(66, 165, 243));
        statistic.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statistic.setOpaque(true); 
        statistic.addMouseListener(mouselistener);
        statistic.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/statistical.png"))));

        
        customerService = new JLabel("Dịch Vụ Khách Hàng");
        customerService.setFont(new Font("Tahoma", Font.PLAIN, 14));
        customerService.setBackground(new Color(66, 165, 243));
        customerService.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerService.setOpaque(true); 
        customerService.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/service.png"))));
        customerService.addMouseListener(mouselistener);

        
        logOut = new JLabel("Đăng Xuất");
        logOut.setFont(new Font("Tahoma", Font.PLAIN, 14));
        logOut.setBackground(new Color(66, 165, 243));
        logOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOut.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(Manager.class.getResource("/images/log-out.png"))));
        logOut.setOpaque(true); 
        logOut.addMouseListener(mouselistener);

   
        functionPanel.add(tourManagement);
        functionPanel.add(customerManagement);
        if(employee.getPermissions().equals(Employee.PERMISSION_ADMIN)) functionPanel.add(employeeManagement);
        functionPanel.add(orderManagement);
        functionPanel.add(statistic);
        functionPanel.add(customerService);
        functionPanel.add(logOut);

        
        JPanel functionDetailPanel = new JPanel();
        functionDetailPanel.setBackground(Color.WHITE);
        
        // Add the title panel to the content pane
        add(titlePanel, BorderLayout.NORTH);
        add(functionPanel, BorderLayout.WEST);
        //contentPane.add(functionDetailPanel, BorderLayout.CENTER);

        
        // Set the size of the frame
	}
	
	private class ManagerControl implements MouseListener
	{
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(currentPanel!=null)
			{
				remove(currentPanel);
			}
			
			if(e.getSource() == tourManagement)
			{
				currentPanel = new TourManagement();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource() == employeeManagement)
			{
				currentPanel = new EmployeeManagement();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource()==orderManagement)
			{
				currentPanel = new OrderManagement();
				add(currentPanel, BorderLayout.CENTER);
			}
			else if(e.getSource()== statistic)
			{
				currentPanel = new Statistic();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource() == customerManagement) 
			{
				currentPanel = new CustomerManagement();
				add(currentPanel, BorderLayout.CENTER);
			}
			
			else if(e.getSource() == customerService) 
			{
				if (chatBox == null) {
					chatBox = new Recive();
				}
				chatBox.setVisible(true);
			}
			
			else if(e.getSource() == logOut) 
			{
				int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất", "Xác nhận", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_NO_OPTION) 
				{
					dispose();
					SignIn si = new SignIn();
					si.setVisible(true);
				}
			}

		  
		    revalidate();
		    repaint();
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == tourManagement) {
				tourManagement.setBackground(new Color(77, 182, 245)); // Lighter color on hover
                // Add action for Tour Management
            } else if (e.getSource() == customerManagement) {
            	customerManagement.setBackground(new Color(77, 182, 245));
                // Add action for Customer Management
            } else if (e.getSource() == employeeManagement) {
            	employeeManagement.setBackground(new Color(77, 182, 245));
                // Add action for Account Management
            } 
            else if (e.getSource() == orderManagement) {
            	orderManagement.setBackground(new Color(77, 182, 245));
            }else if (e.getSource() == statistic) {
            	statistic.setBackground(new Color(77, 182, 245));
                // Add action for Statistic
            } else if (e.getSource() == customerService) {
            	customerService.setBackground(new Color(77, 182, 245));
                // Add action for Customer Service
            } else if (e.getSource() == logOut) {
            	logOut.setBackground(new Color(77, 182, 245));
                // Add action for Log Out
            }
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == tourManagement) {
				tourManagement.setBackground(new Color(66, 165, 243)); // Lighter color on hover
                // Add action for Tour Management
            } else if (e.getSource() == customerManagement) {
            	customerManagement.setBackground(new Color(66, 165, 243));
                // Add action for Customer Management
            } else if (e.getSource() == employeeManagement) {
            	employeeManagement.setBackground(new Color(66, 165, 243));
                // Add action for Account Management
            } 
            else if (e.getSource() == orderManagement) {
            	orderManagement.setBackground(new Color(66, 165, 243));
            }else if (e.getSource() == statistic) {
            	statistic.setBackground(new Color(66, 165, 243));
                // Add action for Statistic
            } else if (e.getSource() == customerService) {
            	customerService.setBackground(new Color(66, 165, 243));
                // Add action for Customer Service
            } else if (e.getSource() == logOut) {
            	logOut.setBackground(new Color(66, 165, 243));
                // Add action for Log Out
            }
			
		}
		
	}

}
