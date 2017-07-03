// Client.Java Java Chatting Client 의 Nicknam, IP, Port 번호 입력하고 접속하는 부분

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField tf_ID; // ID를 입력받을곳
	private JTextField tf_PSWD; // IP를 입력받을곳
	private JTextField tf_IP; // IP를 입력받을곳

	private BufferedImage bfImage;
	public Client() // 생성자
	{
		init();
		setVisible(true);
	}

	public void init() // 화면 구성
	{
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 380, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try {
			bfImage = ImageIO.read(new File("img/title.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel title = new JLabel(new ImageIcon(bfImage));
		title.setFont(new Font("", Font.PLAIN, 30));
		title.setBounds(84, 26, 200, 94);
		title.setForeground(Color.RED);
		contentPane.add(title);

		// id 입력창
		try {
			bfImage = ImageIO.read(new File("img/id.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel lblNewLabel = new JLabel(new ImageIcon(bfImage));
		lblNewLabel.setBounds(64, 140, 90, 43);
		lblNewLabel.setForeground(Color.WHITE);
		contentPane.add(lblNewLabel);

		tf_ID = new JTextField();
		tf_ID.setBounds(140, 154, 130, 21);
		tf_ID.setBackground(Color.BLACK);
		tf_ID.setForeground(Color.WHITE);
		tf_ID.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
		contentPane.add(tf_ID);
		tf_ID.setColumns(10);

		// pw 입력창
		try {
			bfImage = ImageIO.read(new File("img/password.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel lblPassword = new JLabel(new ImageIcon(bfImage));
		lblPassword.setBounds(18, 190, 177, 43);
		lblPassword.setForeground(Color.WHITE);
		contentPane.add(lblPassword);

		tf_PSWD = new JPasswordField();
		tf_PSWD.setColumns(10);
		tf_PSWD.setBounds(140, 204, 130, 21);
		tf_PSWD.setBackground(Color.BLACK);
		tf_PSWD.setForeground(Color.WHITE);
		tf_PSWD.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
		contentPane.add(tf_PSWD);

		// ip 입력창
		try {
			bfImage = ImageIO.read(new File("img/ip.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel lblIP = new JLabel(new ImageIcon(bfImage));
		lblIP.setBounds(18, 240, 177, 43);
		lblIP.setForeground(Color.WHITE);
		contentPane.add(lblIP);
		
		tf_IP = new JTextField();
		tf_IP.setColumns(10);
		tf_IP.setBounds(140, 254, 130, 21);
		tf_IP.setBackground(Color.BLACK);
		tf_IP.setForeground(Color.WHITE);
		tf_IP.setBorder(new MatteBorder(1, 1, 1, 1, Color.RED));
		contentPane.add(tf_IP);
		
		
		
		// 로그인 버튼
		try {
			bfImage = ImageIO.read(new File("img/login.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JButton btnLogin  = new JButton(new ImageIcon(bfImage));
		btnLogin.setBounds(100, 280, 174, 40);
		btnLogin.setBackground(Color.BLACK);
		btnLogin.setForeground(Color.RED);
		btnLogin.setBorderPainted(false);
		contentPane.add(btnLogin);
		
		ConnectAction action = new ConnectAction();
		btnLogin.addActionListener(action);
		tf_PSWD.addActionListener(action);
		tf_IP.addActionListener(action);
	}
	class ConnectAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String id = tf_ID.getText().trim(); // 공백이 있지 모르니 공백 제거 trim() 사용
			String password = tf_PSWD.getText().trim();
			
			//String ip = "127.0.0.1";
			String ip= tf_IP.getText().trim(); // 공백이 있을지 모르므로 공백제거
			
			int port = 30000;
			//int port=Integer.parseInt(tf_PORT.getText().trim()); // 공백을 제거한 후 int형으로 변환 
			
			MainView view = new MainView(id, password, ip, port);
			setVisible(false);						
		}
	}
	
}
