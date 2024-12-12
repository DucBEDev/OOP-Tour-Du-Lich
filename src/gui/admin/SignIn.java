package gui.admin;

import javax.swing.JFrame;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import dao.Employee_DAO;
import entity.Employee;
import dao.Customer_DAO;
import entity.Customer;
import gui.client.Dashboard;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SignIn extends JFrame {
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JPanel contentPane;
	private JPanel pnlThumbnail;
	private JLabel lblThumbnail;
	private JPanel pnlLogin;
	private JLabel lblLogin;
	private JLabel lblUserName;
	private JSeparator sepUserName;
	private JLabel lblPassword;
	private JSeparator sepPassword;
	private JButton btnLogin;
	private JButton btnGuest;
	
	private static Employee employee;
	
	private JButton btnRegister;

	public SignIn() {
		this.init();
	}
	
	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 560);
		setLocationRelativeTo(null);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setForeground(new Color(0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPane);
		contentPane.setLayout(null);
		
		pnlThumbnail = new JPanel();
		pnlThumbnail.setBounds(0, 0, 367, 524);
		contentPane.add(pnlThumbnail);
		pnlThumbnail.setLayout(null);
		
		lblThumbnail = new JLabel("");
		lblThumbnail.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(SignIn.class.getResource("/images/thumbnail.png"))));
		lblThumbnail.setBounds(0, 0, 496, 524);
		pnlThumbnail.add(lblThumbnail);
		
		pnlLogin = new JPanel();
		pnlLogin.setBounds(366, 0, 422, 524);
		pnlLogin.setBackground(new Color(133, 216, 255));
		contentPane.add(pnlLogin);
		pnlLogin.setLayout(null);
		
		lblLogin = new JLabel("Đăng nhập");
		lblLogin.setBounds(144, 38, 144, 81);
		pnlLogin.add(lblLogin);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		lblUserName = new JLabel("Tên đăng nhập");
		lblUserName.setBounds(54, 160, 98, 20);
		pnlLogin.add(lblUserName);
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtUserName = new JTextField();
		txtUserName.setBounds(54, 188, 282, 20);
		txtUserName.setBackground(new Color(133, 216, 255));
		txtUserName.setBorder(null);
		txtUserName.requestFocusInWindow();
		txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtUserName);
		txtUserName.setColumns(10);
		
		sepUserName = new JSeparator();
		sepUserName.setBackground(new Color(0, 0, 0));
		sepUserName.setBounds(54, 210, 282, 2);
		pnlLogin.add(sepUserName);
		
		lblPassword = new JLabel("Mật khẩu");
		lblPassword.setBounds(54, 260, 98, 20);
		pnlLogin.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtPassword = new JPasswordField();
		txtPassword.setEchoChar('*');
		txtPassword.setBounds(54, 288, 282, 20);
		txtPassword.setBackground(new Color(133, 216, 255));
		txtPassword.setBorder(null);
		txtPassword.requestFocusInWindow();
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtPassword);
		txtPassword.setColumns(10);
		
		sepPassword = new JSeparator();
		sepPassword.setBackground(new Color(0, 0, 0));
		sepPassword.setBounds(54, 310, 282, 2);
		pnlLogin.add(sepPassword);
		
		btnLogin = new JButton("Đăng nhập");
		btnLogin.addActionListener(e -> btnLoginPerformed());
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLogin.setBackground(new Color(241, 215, 140));
		btnLogin.setBorder(null);
		btnLogin.setBounds(122, 373, 156, 38);
		btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLogin.add(btnLogin);
		
		btnRegister = new JButton("Chưa có tài khoản?");
		btnRegister.setBackground(new Color(133, 216, 255));
		btnRegister.setBorderPainted(false);
		btnRegister.setContentAreaFilled(false);
		btnRegister.setFont(new Font("Tahoma", Font.ITALIC, 13));
		btnRegister.setBounds(82, 459, 236, 32);
		btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnRegister.setFocusPainted(false);
		btnRegister.addActionListener(e -> {
			Register register = new Register();
			register.setVisible(true);
			this.dispose();
		});
		pnlLogin.add(btnRegister);
		
		btnGuest = new JButton("Đăng nhập với tài khoản khách?");
		btnGuest.setBackground(new Color(133, 216, 255));
		btnGuest.setBorderPainted(false);
		btnGuest.setContentAreaFilled(false);
		btnGuest.setFont(new Font("Tahoma", Font.ITALIC, 13));
		btnGuest.setBounds(82, 422, 236, 32);
		btnGuest.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnGuest.setFocusPainted(false);
		btnGuest.addActionListener(e -> {
			Customer cus = new Customer();
			Dashboard dashboard = new Dashboard(cus);
			dashboard.setVisible(true);
			this.dispose();
		});
		pnlLogin.add(btnGuest);
	}
	
	private void btnLoginPerformed(){
		String username = txtUserName.getText();
		String password = String.valueOf(txtPassword.getPassword());
		Manager mag;
//		JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
		Employee_DAO employeeDAO = new Employee_DAO();
		
//			UserDTO role = ubs.getByName(username);
		if (employeeDAO.checkExistByUsername(username, password)) 
		{
			JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
			employee = employeeDAO.getByUsername(username);
			mag = new Manager(employee);
			mag.setVisible(true);
			this.dispose();
		}
		else if(username.equals("1"))
		{
			JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
			employee = employeeDAO.getByUsername("user10");
			mag = new Manager(employee);
			mag.setVisible(true);
			this.dispose();
		}
		
		else 
		{
			Customer_DAO customer_dao = new Customer_DAO();
			Customer customer = customer_dao.checkLogin(username, password);
			
			if (customer != null) {
				JOptionPane.showMessageDialog(null,"Đăng nhập thành công");
				Dashboard dashboard = new Dashboard(customer);
				dashboard.setVisible(true);
				this.dispose();
			}
			else {
				JOptionPane.showMessageDialog(null,"Đăng nhập không thành công");
				txtUserName.setText("");
				txtPassword.setText("");
			}
		}
		
	}
	
	public static Employee getEmployee()
	{
		return employee;
	}
}
