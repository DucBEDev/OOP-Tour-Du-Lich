package gui.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.Customer_DAO;
import entity.Customer;
import gui.client.Dashboard;

public class Register extends JFrame {
	private JPanel contentPane;
	private JPanel pnlThumbnail;
	private JPanel pnlLogin;
	
	private JLabel lblThumbnail;
	private JLabel lblLogin;
	private JLabel lblUserName;
	private JLabel lblPhone;
	private JLabel lblName;
	private JLabel lblAddress;
	private JLabel lblPassword;
	private JLabel lblCheckPassword;
	private JLabel lblEmail;
	
	private JSeparator sepUserName;
	private JSeparator sepName;
	private JSeparator sepPhone;
	private JSeparator sepAddress;
	private JSeparator sepPassword;
	private JSeparator sepCheckPassword;
	private JSeparator sepEmail;
	
	private JButton btnRegister;
	
	private JTextField txtPhone;
	private JTextField txtAddress;
	private JTextField txtUserName;
	private JTextField txtName;
	private JTextField txtEmail;
	
	private JPasswordField txtPassword;
	private JPasswordField txtCheckPassword;
	

	public Register() {
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
		
		lblLogin = new JLabel("Đăng ký");
		lblLogin.setBounds(144, 0, 144, 81);
		pnlLogin.add(lblLogin);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		lblUserName = new JLabel("Tên đăng nhập");
		lblUserName.setBounds(54, 61, 98, 20);
		pnlLogin.add(lblUserName);
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtUserName = new JTextField();
		txtUserName.setBounds(54, 84, 282, 20);
		txtUserName.setBackground(new Color(133, 216, 255));
		txtUserName.setBorder(null);
		txtUserName.requestFocusInWindow();
		txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtUserName);
		txtUserName.setColumns(10);
		
		sepUserName = new JSeparator();
		sepUserName.setBackground(new Color(0, 0, 0));
		sepUserName.setBounds(54, 104, 282, 2);
		pnlLogin.add(sepUserName);
		
		lblName = new JLabel("Họ và tên");
		lblName.setBounds(54, 112, 98, 20);
		pnlLogin.add(lblName);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtName = new JTextField();
		txtName.setBounds(54, 134, 282, 20);
		txtName.setBackground(new Color(133, 216, 255));
		txtName.setBorder(null);
		txtName.requestFocusInWindow();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtName);
		txtName.setColumns(10);
		
		sepName = new JSeparator();
		sepName.setBackground(new Color(0, 0, 0));
		sepName.setBounds(54, 154, 282, 2);
		pnlLogin.add(sepName);
		
		lblPhone = new JLabel("Số điện thoại");
		lblPhone.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPhone.setBounds(54, 165, 98, 20);
		pnlLogin.add(lblPhone);
		
		sepPhone = new JSeparator();
		sepPhone.setBackground(Color.BLACK);
		sepPhone.setBounds(54, 206, 282, 2);
		pnlLogin.add(sepPhone);
		
		txtPhone = new JTextField();
		txtPhone.setColumns(10);
		txtPhone.setBorder(null);
		txtPhone.setBackground(new Color(133, 216, 255));
		txtPhone.setBounds(54, 186, 282, 20);
		txtPhone.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtPhone);
		
		lblAddress = new JLabel("Địa chỉ");
		lblAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAddress.setBounds(54, 214, 98, 20);
		pnlLogin.add(lblAddress);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBorder(null);
		txtAddress.setBackground(new Color(133, 216, 255));
		txtAddress.setBounds(54, 235, 282, 20);
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtAddress);
		
		sepAddress = new JSeparator();
		sepAddress.setBackground(Color.BLACK);
		sepAddress.setBounds(54, 255, 282, 2);
		pnlLogin.add(sepAddress);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(54, 266, 98, 20);
		pnlLogin.add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBorder(null);
		txtEmail.setBackground(new Color(133, 216, 255));
		txtEmail.setBounds(54, 291, 282, 20);
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlLogin.add(txtEmail);
		
		sepEmail = new JSeparator();
		sepEmail.setBackground(Color.BLACK);
		sepEmail.setBounds(54, 312, 282, 2);
		pnlLogin.add(sepEmail);
		
		lblPassword = new JLabel("Mật khẩu");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPassword.setBounds(54, 326, 98, 20);
		pnlLogin.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setEchoChar('*');
		txtPassword.setColumns(10);
		txtPassword.setBorder(null);
		txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPassword.setBackground(new Color(133, 216, 255));
		txtPassword.setBounds(54, 350, 282, 20);
		pnlLogin.add(txtPassword);
		
		sepPassword = new JSeparator();
		sepPassword.setBackground(Color.BLACK);
		sepPassword.setBounds(54, 370, 282, 2);
		pnlLogin.add(sepPassword);
		
		lblCheckPassword = new JLabel("Xác nhận mật khẩu");
		lblCheckPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCheckPassword.setBounds(54, 381, 184, 20);
		pnlLogin.add(lblCheckPassword);
		
		txtCheckPassword = new JPasswordField();
		txtCheckPassword.setEchoChar('*');
		txtCheckPassword.setColumns(10);
		txtCheckPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtCheckPassword.setBorder(null);
		txtCheckPassword.setBackground(new Color(133, 216, 255));
		txtCheckPassword.setBounds(54, 407, 282, 20);
		pnlLogin.add(txtCheckPassword);
		
		sepCheckPassword = new JSeparator();
		sepCheckPassword.setBackground(Color.BLACK);
		sepCheckPassword.setBounds(54, 428, 282, 2);
		pnlLogin.add(sepCheckPassword);
		
		btnRegister = new JButton("Đăng ký");
		btnRegister.addActionListener(e -> btnRegisterPerformed());
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnRegister.setBackground(new Color(241, 215, 140));
		btnRegister.setBorder(null);
		btnRegister.setBounds(132, 454, 156, 38);
		btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pnlLogin.add(btnRegister);
	}
	
	private void btnRegisterPerformed(){
		String userName = txtUserName.getText();
		String name = txtName.getText();
		String phone = txtPhone.getText();
		String address = txtAddress.getText();
		String email = txtEmail.getText();
		String password = String.valueOf(txtPassword.getPassword());
		String checkPassword = String.valueOf(txtCheckPassword.getPassword());
		
		if (password.equals(checkPassword)) {
			Customer_DAO customer_dao = new Customer_DAO();
			String id = customer_dao.generateNextCustomerId();
			
			Customer customer = new Customer(id, name, phone, email, address, "Đã đăng nhập", userName, password);
			customer_dao.add(customer);
			
			JOptionPane.showMessageDialog(null,"Đăng ký thành công");
			Dashboard dashboard = new Dashboard(customer);
			dashboard.setVisible(true);
			this.dispose();			
		}
		else {
			JOptionPane.showMessageDialog(null, "Mật khẩu không đúng.");
			txtPassword.setText("");
			txtCheckPassword.setText("");
		}
	}
}
