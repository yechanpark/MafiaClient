
// MainView.java : Java Chatting Client �� �ٽɺκ�
// read keyboard --> write to network (Thread �� ó��)
// read network --> write to textArea

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MainView extends JFrame {

	private JPanel contentPane;
	private JTextField textField; // ���� �޼��� ���°�

	int[] login = { 0, 0, 0, 0, 0, 0, 0, 0 };
	TimerThread timer;
	int state;

	Myaction action;

	JLabel label1;
	JLabel label2;

	Vector<User> v = new Vector<User>();

	private JScrollPane scrollPane;

	private String id;
	private String password;
	private String ip;
	private int port;

	SoundMaker bgSound;
	SoundMaker clickSound;
	SoundMaker deathSound;

	JLabel timerLabel;
	JLabel timerLabel2;
	JButton sendBtn; // ���۹�ư
	JButton startBtn; // ��ŸƮ ��ư
	JButton deathBtn; // �츮�� ��ư
	JButton liveBtn; // ���̴� ��ư
	JLabel centerLabel;
	JTextPane textPane; // ���ŵ� �޼����� ��Ÿ�� ����
	StyledDocument doc;
	SimpleAttributeSet attribute = new SimpleAttributeSet();

	private BufferedImage bfImage;

	private Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public MainView(String id, String password, String ip, int port)// ������
	{
		this.id = id;
		this.password = password;
		this.ip = ip;
		this.port = port;

		init();
		start();

		insertText("�Ű� ������ �Ѿ�� �� : " + id + " " + password + " " + ip + " " + port + "\n", Color.YELLOW);
		network();
	}

	public void network() {
		// ������ ����
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket�� null���� �ƴҶ� ��! ����Ǿ�����
			{
				Connection(); // ���� �޼ҵ带 ȣ��
			}
		} catch (UnknownHostException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "���� ����", attribute);
			} catch (BadLocationException ee) {
				ee.printStackTrace();
			}
		} catch (IOException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "���� ����", attribute);
			} catch (BadLocationException ee) {
				ee.printStackTrace();
			}
		}

	}

	public void characterSetting(String s) {
		User u = new User(contentPane);
		u.getButton().setText(s);
		u.getButton().addActionListener(action);
		for (int i = 0; i < login.length; i++) {
			if (login[i] == 0) {
				switch (i) {
				case 0:
					u.getButton().setBounds(50, 50, 100, 100);
					u.getIdLabel().setBounds(87, 30, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(0);
					login[i] = 1;
					break;
				case 1:
					u.getButton().setBounds(300, 50, 100, 100);
					u.getIdLabel().setBounds(337, 30, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(1);
					login[i] = 1;
					break;
				case 2:
					u.getButton().setBounds(550, 50, 100, 100);
					u.getIdLabel().setBounds(587, 30, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(2);
					login[i] = 1;
					break;
				case 3:
					u.getButton().setBounds(50, 250, 100, 100);
					u.getIdLabel().setBounds(87, 230, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(3);
					login[i] = 1;
					break;
				case 4:
					u.getButton().setBounds(550, 250, 100, 100);
					u.getIdLabel().setBounds(587, 230, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(4);
					login[i] = 1;
					break;
				case 5:
					u.getButton().setBounds(50, 450, 100, 100);
					u.getIdLabel().setBounds(87, 430, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(5);
					login[i] = 1;
					break;
				case 6:
					u.getButton().setBounds(300, 450, 100, 100);
					u.getIdLabel().setBounds(337, 430, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(6);
					login[i] = 1;
					break;
				case 7:
					u.getButton().setBounds(550, 450, 100, 100);
					u.getIdLabel().setBounds(587, 430, 60, 30);
					u.getIdLabel().setText(s);
					u.setIndex(7);
					login[i] = 1;
					break;
				default:
					break;
				}
				break;
			}
		}
		u.setId(s);
		v.add(u);
		repaint();
	}

	public void allButtonTrueEnable() {
		for (int i = 0; i < v.size(); i++) {
			if (v.get(i).getLive() == true)
				v.get(i).getButton().setEnabled(true);
		}
	}

	public void allButtonFalseEnable() {
		for (int i = 0; i < v.size(); i++) {
			v.get(i).getButton().setEnabled(false);
		}
	}

	public void setBfImage(String s) {
		try {
			bfImage = ImageIO.read(new File(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertText(String s, Color c) {
		attribute.addAttribute(StyleConstants.Foreground, c);
		try {
			doc.insertString(doc.getLength(), s, attribute);
		} catch (BadLocationException ee) {
			ee.printStackTrace();
		}
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}

	public void playChatGraphicThread(int index, String s) {
		int x = 0, y = 0;
		switch (index) {
		case 0:
			x = 40;
			y = 150;
			break;
		case 1:
			x = 260;
			y = 150;
			break;
		case 2:
			x = 470;
			y = 150;
			break;
		case 3:
			x = 155;
			y = 275;
			break;
		case 4:
			x = 360;
			y = 270;
			break;
		case 5:
			x = 40;
			y = 370;
			break;
		case 6:
			x = 260;
			y = 370;
			break;
		case 7:
			x = 470;
			y = 370;
			break;
		default:
			break;
		}
		if (v.get(0).getGraphic2(index).getUsing()) {
			v.get(0).getGraphic2(index).background.setVisible(false);
			v.get(0).getGraphic2(index).chatDialog.setText("");
			v.get(0).remakeGraphic2(index);
			v.get(0).getGraphic2(index).setDialog(s);
			if (index == 3 || index == 4) {
				v.get(0).getGraphic2(index).setPosition(x, y, 189, 70);
			} else {
				v.get(0).getGraphic2(index).setPosition(x, y, 180, 83);
			}
			v.get(0).getGraphic2(index).start();
		} else {
			v.get(0).remakeGraphic2(index);
			v.get(0).getGraphic2(index).setDialog(s);
			if (index == 3 || index == 4) {
				System.out.println("4,5");
				v.get(0).getGraphic2(index).setPosition(x, y, 189, 70);
			} else {
				v.get(0).getGraphic2(index).setPosition(x, y, 180, 83);
			}
			v.get(0).getGraphic2(index).start();
		}
	}

	public void civilSetting(String s) {
		centerLabel.setText(s);
		insertText(s + "\n", Color.YELLOW);
		for (int i = 1; i < v.size(); i++) {
			v.get(i).getButton().setIcon(new ImageIcon("img/civil.png"));
		}
		repaint();
	}

	public void timerRestart(int t) {
		timer.getLabel().setVisible(false);
		timer.terminate();
		timer = new TimerThread(timerLabel, t);
		timer.start();
		timer.getLabel().setVisible(true);
	}

	public void Connection() { // ���� ���� �޼ҵ� ����κ�
		try { // ��Ʈ�� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);

			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

		} catch (IOException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "��Ʈ�� ���� ����", attribute);
			} catch (BadLocationException ee) {
				ee.printStackTrace();
			}
		}
		String tmp = id + ":" + password + ":";
		send_Message(tmp); // ���������� ����Ǹ� ���� id�� ����

		Thread th = new Thread(new Runnable() { // �����带 ������ �����κ��� �޼����� ����

			@SuppressWarnings("null")
			@Override
			public void run() {
				while (true) {
					try {
						byte[] b = new byte[128];
						dis.read(b, 0, 128);
						String msg = new String(b);
						msg = msg.trim();
						String[] tmp2 = msg.split(":");

						// insertText("���� �޽��� : ["+ msg +"]\n", Color.BLUE);

						if (tmp2[0].equals("ERROR")) {
							if (tmp2[1].equals("FULL")) {
								insertText("���� �� á���ϴ�.\n", Color.YELLOW);
							} else if (tmp2[1].equals("PLAYING")) {
								insertText("������ �������Դϴ�.\n", Color.YELLOW);
							}
						} else if (tmp2[0].equals("USINGID")) {
							insertText("�������� ���̵� �Դϴ�.\n", Color.YELLOW);
						} else if (tmp2[0].equals("WRONGID")) {
							insertText("���̵� ��й�ȣ�� Ʋ���ϴ�.\n", Color.YELLOW);
						} else if (tmp2[0].equals("LOGOUT")) {
							if (tmp2[1].equals("BEFORE")) {
								for (int i = 1; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										login[v.get(i).getIndex()] = 0;
										v.get(i).getButton().setVisible(false);
										v.get(i).getIdLabel().setVisible(false);
										if (tmp2[3].equals("NEWMASTER")) {
											for (int j = 0; j < v.size(); j++) {
												if (v.get(j).getId().equals(tmp2[4]))
													v.get(j).setMaster(true);
											}
										}
										if (v.get(0).getMaster()) {
											startBtn.setVisible(true);
										}
										v.removeElement(v.get(i));
										insertText(tmp2[2] + "���� �α׾ƿ� �ϼ̽��ϴ�.\n", Color.YELLOW);
									}
								}
							} else if (tmp2[1].equals("AFTER")) {
								for (int i = 1; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										v.get(i).getButton().setVisible(false);
										v.get(i).getIdLabel().setVisible(false);
										login[v.get(i).getIndex()] = 0;
										if (tmp2[3].equals("NEWMASTER")) {
											for (int j = 0; j < v.size(); j++) {
												if (v.get(j).getId().equals(tmp2[4]))
													v.get(j).setMaster(true);
											}
										}
										v.removeElement(v.get(i));
										insertText(tmp2[2] + "���� ������ ������ϴ�..\n", Color.YELLOW);
									}
								}
							}
						} else if (tmp2[0].equals("ULIST")) { // �ڽź��� ���������� ����
																// ó��
							int userNum = Integer.parseInt(tmp2[1]);
							characterSetting(id);
							send_Message("LOGIN:" + id + ":");
							if (userNum >= 0) {
								switch (userNum) {
								case 7:
									characterSetting(tmp2[8]);
								case 6:
									characterSetting(tmp2[7]);
								case 5:
									characterSetting(tmp2[6]);
								case 4:
									characterSetting(tmp2[5]);
								case 3:
									characterSetting(tmp2[4]);
								case 2:
									characterSetting(tmp2[3]);
								case 1:
									characterSetting(tmp2[2]);
									break;
								case 0:
									centerLabel.setText("����� �����Դϴ�");
									startBtn.setVisible(true);
									v.get(0).setMaster(true);
									break;
								default:
									break;
								}
							}
						} else if (tmp2[0].equals("LOGIN")) { // �ڽź��� �ڿ� �����ϴ� ����
																// ����
							if (tmp2[1].equals(id)) {

							} else
								characterSetting(tmp2[1]);
						} else if (tmp2[0].equals("START")) { // ���ӽ��� + ���� ����
							insertText("������ ���۵Ǿ����ϴ�.\n������ ���Դϴ�.\n", Color.YELLOW);
							if (bgSound.playing == true)
								bgSound.stop();
							bgSound.play("sound/Uka.wav");
							textField.setEnabled(false);
							textField.setBackground(Color.BLACK);
							timer = new TimerThread(timerLabel, 15);
							timer.start();
							timerLabel.setVisible(true);
							timerLabel2.setVisible(true);
							state = 4; // �� ����
							if (tmp2[1].equals("MAFIA")) {
								insertText("����� ���Ǿ� �Դϴ�.\n", Color.YELLOW);
								centerLabel.setText("����� ���Ǿ� �Դϴ�");
								setBfImage("img/mafia.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).getButton().setForeground(Color.RED);
								v.get(0).setJob(1);
								int mNum = Integer.parseInt(tmp2[2]);
								if (mNum == 2) {
									for (int i = 1; i < v.size(); i++) {
										if (v.get(i).getId().equals(tmp2[3])) {
											setBfImage("img/mafia.png");
											v.get(i).getButton().setIcon(new ImageIcon(bfImage));
											v.get(i).setJob(1);
										} else {
											setBfImage("img/civil.png");
											v.get(i).getButton().setIcon(new ImageIcon(bfImage));
										}
									}
								}
								if (mNum == 1) {
									for (int i = 1; i < v.size(); i++) {
										setBfImage("img/civil.png");
										v.get(i).getButton().setIcon(new ImageIcon(bfImage));
									}
								}
								repaint();
							} else if (tmp2[1].equals("DOCTOR")) {
								setBfImage("img/doctor.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(3);
								civilSetting("����� �ǻ��Դϴ�.");
							} else if (tmp2[1].equals("POLICE")) {
								setBfImage("img/police.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(2);
								civilSetting("����� �����Դϴ�.");
							} else if (tmp2[1].equals("REPORTER")) {
								setBfImage("img/reporter.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(4);
								civilSetting("����� �����Դϴ�.");
							} else if (tmp2[1].equals("POLITICIAN")) {
								setBfImage("img/politician.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(7);
								civilSetting("����� ��ġ���Դϴ�.");
							} else if (tmp2[1].equals("CIVIL")) {
								v.get(0).setJob(0);
								civilSetting("����� �ù��Դϴ�.");
							} else {
								insertText(msg, Color.BLUE);
							}
							if (v.get(0).getJob() != 0 && v.get(0).getJob() != 4) { // �Ϲ�
																					// �ù���
																					// �ƴ�
																					// ��
								allButtonTrueEnable();
							}
						} else if (tmp2[0].equals("MORNING")) {
							bgSound.stop();
							bgSound.play("sound/L.wav");
							insertText("��ħ�� ��ҽ��ϴ�.\n", Color.YELLOW);
							if (tmp2[1].equals("MDIE")) { // ���ش��ϴ� ����� ������
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										setBfImage("img/death.png");
										v.get(i).getButton().setIcon(new ImageIcon(bfImage));
										v.get(i).setLive(false);
									}
								}
								deathSound.play("sound/scream.wav");
								insertText(tmp2[2] + "���� ���ش��߽��ϴ�.\n", Color.YELLOW);
							} else if (tmp2[1].equals("HEAL")) { // �ǻ簡 �������
								insertText("�ǻ簡 " + tmp2[2] + "���� ��Ƚ��ϴ�\n", Color.YELLOW);
							} else if (tmp2[1].equals("NODIE")) {
								insertText("���� �㿣 �ƹ��� ���� �ʾҽ��ϴ�\n", Color.YELLOW);
							}

							if (tmp2[3].equals("REPORTER")) {
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[4])) {
										insertText("���ڰ� " + tmp2[4] + "����" + tmp2[5] + "��� �����߽��ϴ�.\n", Color.YELLOW);
									}
								}
							}
							state = 1; // �� ����
							timerRestart(20);
							textField.setEnabled(true);
							textField.setBackground(Color.WHITE);
							centerLabel.setText("��ħ�� ��ҽ��ϴ�");
							allButtonFalseEnable();
						} else if (tmp2[0].equals("NIGHT")) { // �㿡 ���� Ȱ��
							bgSound.stop();
							bgSound.play("sound/Uka.wav");
							if (tmp2[1].equals("VDIE")) { // �������� ������
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										setBfImage("img/death.png");
										v.get(i).getButton().setIcon(new ImageIcon(bfImage));
										v.get(i).setLive(false);
									}
								}
								deathSound.play("sound/scream.wav");
								insertText(tmp2[2] + "���� �������߽��ϴ�.\n", Color.YELLOW);
							} else if (tmp2[1].equals("NODIE")) {
								insertText("������ �������� �ʾҽ��ϴ�.\n", Color.YELLOW);
							} else if (tmp2[1].equals("NOVOTE")) {
								insertText("���ݼ��� �Ѵ� ǥ�� �����ڰ� �����ϴ�.\n", Color.YELLOW);
							}
							deathBtn.setVisible(false);
							liveBtn.setVisible(false);
							insertText("���� �Ǿ����ϴ�.\n", Color.YELLOW);
							state = 4;
							timerRestart(15);
							if (v.get(0).getJob() == 1) { // ���Ǿ��϶�
								textField.setEnabled(true);
								textField.setBackground(Color.WHITE);
								centerLabel.setText("���� ����� �����ϼ���");
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getJob() != 1) {
										if (v.get(i).getLive() == true) {
											v.get(i).getButton().setEnabled(true);
										}
									}
								}
							} else if (v.get(0).getJob() == 2 && v.get(0).getLive() == true) { // �����϶�
								centerLabel.setText("���� �� ����� �����ϼ���");
								allButtonTrueEnable();
							} else if (v.get(0).getJob() == 3 && v.get(0).getLive() == true) { // �ǻ��϶�
								centerLabel.setText("ġ���� ����� �����ϼ���");
								allButtonTrueEnable();
							} else if (v.get(0).getJob() == 4 && v.get(0).getLive() == true) { // �����϶�
								allButtonTrueEnable();
								centerLabel.setText("������ ����� �����ϼ���");
							} else if (v.get(0).getJob() == 7) { // ��ġ���϶�
								centerLabel.setText("���� �Ǿ����ϴ�");
							} else if (v.get(0).getJob() == 0) { // �Ϲ� �ù��϶�
								centerLabel.setText("���� �Ǿ����ϴ�");
							}
							// if(v.get(0).getLive() == false){
							// textField.setEnabled(true);
							// textField.setBackground(Color.WHITE);
							// }
						} else if (tmp2[0].equals("VOTE")) { // �� ��ǥ
							insertText("��ǥ �ϼ���.\n", Color.YELLOW);
							centerLabel.setText("�����뿡 �ø� ����� �̾��ּ���");
							timerRestart(10);
							state = 2; // ���ϻ�� ��ǥ���� ����
							if (v.get(0).getLive() == true) { // ����ִ� ����� ��ǥ
								allButtonTrueEnable();
							}
						} else if (tmp2[0].equals("PLEAD")) { // ������ ����
							insertText(tmp2[1] + "���� ���� ���� ǥ�� �޾ҽ��ϴ�.\n" + tmp2[1] + "�� ������ ����\n", Color.YELLOW);
							centerLabel.setText(tmp2[1] + "�� ������ ����");
							timerRestart(10);
							state = 3; // �����Ǻ��� + ������ǥ�ð�
							if (v.get(0).getLive() == true) {
								if (tmp2[1].equals(id)) {
								} else {
									textField.setEnabled(false);
									textField.setBackground(Color.BLACK);
								}
							}
						} else if (tmp2[0].equals("VOTE2")) { // ������ �츱�� ��ǥ
							insertText("���нð��� �������ϴ�\n������ �����Ͻðڽ��ϱ�?\n", Color.YELLOW);
							centerLabel.setText("������ �����Ͻðڽ��ϱ�?\n");
							timerRestart(7);
							if (v.get(0).getLive() == true) { // ����ִ� ����� ��ǥ
								textField.setEnabled(false);
								textField.setBackground(Color.BLACK);
								deathBtn.setVisible(true);
								liveBtn.setVisible(true);
							}
						} else if (tmp2[0].equals("RESULTPOLICE")) { // ���� ��ǥ ���
																		// ��ȸ
							if (tmp2[1].equals("ISMAFIA")) {
								insertText("����� ���Ǿ� �Դϴ�\n", Color.YELLOW);
								centerLabel.setText("����� ���Ǿ��Դϴ�");
							} else {
								insertText("����� ���Ǿư� �ƴմϴ�.\n", Color.YELLOW);
								centerLabel.setText("����� ���Ǿư� �ƴմϴ�.");
							}
						} else if (tmp2[0].equals("RESULTREPORTER")) {
							insertText("ù�� �㿡�� ���縦 �� �� �����ϴ�.\n", Color.YELLOW);
						} else if (tmp2[0].equals("NORMALVOTE")) { // ���� ��ǥ ����
																	// �����ֱ�
							insertText("[ " + tmp2[2] + " ] ��  " + tmp2[1] + " ǥ\n", Color.YELLOW);
						} else if (tmp2[0].equals("USERLESS")) { // �ο��� �����ؼ� ����
																	// ���Ҷ�
							startBtn.setVisible(true);
							insertText("�ο��� �����մϴ�\n", Color.YELLOW);
						} else if (tmp2[0].equals("GAMEOVER")) { // ���� ��
							bgSound.stop();
							if (tmp2[1].equals("MAFIA")) { // ���Ǿư� �̰�����
								insertText("���Ǿ� �¸�!!\n", Color.YELLOW);
								centerLabel.setText("���Ǿ� �¸�");
								if (v.get(0).getJob() == 1)
									bgSound.play("sound/win.wav");
								else
									bgSound.play("sound/lose.wav");
							} else { // �ù��� �̰�����
								insertText("�ù� �¸�!!\n", Color.YELLOW);
								centerLabel.setText("�ù� �¸�");
								if (v.get(0).getJob() == 1)
									bgSound.play("sound/lose.wav");
								else
									bgSound.play("sound/win.wav");
							}
							if (v.get(0).getMaster()) {
								startBtn.setVisible(true);
							}
							state = 0;
							timerLabel.setVisible(false);
							timerLabel2.setVisible(false);
							textField.setEnabled(true);
							textField.setBackground(Color.WHITE);
							for (int i = 0; i < v.size(); i++) {
								v.get(i).setLive(true);
							}
						} else if (tmp2[0].equals("MSG")) { // �Ϲ� ä�� �޽��� ���Ǿ�,
															// ����, �ù� ����
							String[] tmp3 = msg.split(":", 4);
							String tmp = null;
							tmp = String.format("[%s] : %s", tmp3[1], tmp3[2]);
							if (tmp3[1].equals(id)) {
								playChatGraphicThread(0, tmp);
							} else {
								if (v.get(0).getJob() == 1) { // ���� ���Ǿ��� ��
									for (int i = 1; i < v.size(); i++) {
										if (v.get(i).getId().equals(tmp3[1])) {
											if (v.get(i).getJob() == 1 && v.get(i).getLive() == true) {
												playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
												insertText(tmp, Color.RED);
											} else if (v.get(i).getJob() == 0 && v.get(i).getLive() == true) {
												playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
												insertText(tmp, Color.WHITE);
											} else if (v.get(0).getLive() == false && v.get(i).getLive() == false) {
												playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
												insertText(tmp, Color.GRAY);
											}
										}
									}
								} else { // ���� �ù� �Ǵ� �ٸ������� ��
									if (state != 4) {
										if (v.get(0).getLive() == true && state == 0) {
											for (int i = 1; i < v.size(); i++) {
												if (v.get(i).getId().equals(tmp3[1]) && v.get(i).getLive() == true) {
													playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
													insertText(tmp, Color.WHITE);
												}
											}
										} else {
											for (int i = 0; i < v.size(); i++) {
												if (v.get(i).getId().equals(tmp3[1])) {
													if (v.get(i).getLive() == true) {
														playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
														insertText(tmp, Color.WHITE);
													} else if (v.get(0).getLive() == false) {
														playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
														insertText(tmp, Color.GRAY);
													}
												}
											}
										}

									} else {
										if (v.get(0).getLive() == false) {
											for (int i = 0; i < v.size(); i++) {
												if (v.get(i).getId().equals(tmp3[1])) {
													if (v.get(i).getLive() == false) {
														playChatGraphicThread(v.get(i).getIndex(), tmp3[2]);
														insertText(tmp, Color.GRAY);
													}
												}
											}
										}
									}
								}
							}
						}

					} catch (IOException e) {
						// textArea.append("�޼��� ���� ����!!\n");
						// ������ ���� ��ſ� ������ ������ ��� ������ �ݴ´�
						exit();
						break;
					}
				} // while�� ��
			}// run�޼ҵ� ��
		});

		th.start();
	}

	public void exit() {
		try {
			os.close();
			is.close();
			dos.close();
			dis.close();
			socket.close();
		} catch (IOException e1) {
		}
	}

	public void send_Message(String str) { // ������ �޼����� ������ �޼ҵ�
		try {
			byte[] bb;
			byte[] bb2 = new byte[128];
			bb = str.getBytes();
			for (int i = 0; i < 128; i++) {
				bb2[i] = 'X';
			}
			for (int i = 0; i < bb.length; i++) {
				bb2[i] = bb[i];
			}
			dos.write(bb2, 0, 128);
		} catch (IOException e) {
			// textArea.append("�޼��� �۽� ����!!\n");
		}
		// insertText("���� �޽���:["+ str +"]\n",Color.MAGENTA);
	}

	public void init() { // ȭ�鱸�� �޼ҵ�
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 100, 1000, 620);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.BLACK);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(696, 30, 280, 490);
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane);
		textPane = new JTextPane();
		doc = textPane.getStyledDocument();

		scrollPane.setViewportView(textPane);
		textPane.setBackground(Color.BLACK);

		textField = new JTextField();
		textField.setBounds(696, 532, 210, 32);
		textField.setDocument(new JTextFieldLimit(20));
		contentPane.add(textField);

		sendBtn = new JButton("����");
		sendBtn.setBounds(916, 532, 60, 32);
		sendBtn.setForeground(Color.WHITE);
		sendBtn.setBackground(Color.BLACK);
		contentPane.add(sendBtn);
		textPane.setEditable(false); // ����ڰ� �������ϰ� ���´�

		startBtn = new JButton("start");
		startBtn.setBounds(310, 350, 80, 30);
		startBtn.setBackground(Color.BLACK);
		startBtn.setForeground(Color.WHITE);
		startBtn.setVisible(false);
		contentPane.add(startBtn, 2);

		timerLabel = new JLabel("0");
		timerLabel.setBounds(345, 240, 100, 30);
		timerLabel.setForeground(Color.YELLOW);
		contentPane.add(timerLabel, 2);
		timerLabel.setVisible(false);

		timerLabel2 = new JLabel("���� �ð�");
		timerLabel2.setBounds(325, 220, 100, 30);
		timerLabel2.setForeground(Color.YELLOW);
		contentPane.add(timerLabel2, 2);
		timerLabel2.setVisible(false);

		centerLabel = new JLabel("ȯ���մϴ�.", JLabel.CENTER);
		centerLabel.setBounds(200, 200, 300, 200);
		centerLabel.setBackground(Color.red);
		centerLabel.setForeground(Color.white);
		contentPane.add(centerLabel, 2);

		deathBtn = new JButton("Kill");
		deathBtn.setBounds(280, 320, 70, 30);
		deathBtn.setBackground(Color.BLACK);
		deathBtn.setForeground(Color.WHITE);
		deathBtn.setVisible(false);
		contentPane.add(deathBtn, 2);

		liveBtn = new JButton("Save");
		liveBtn.setBounds(360, 320, 70, 30);
		liveBtn.setBackground(Color.BLACK);
		liveBtn.setForeground(Color.WHITE);
		liveBtn.setVisible(false);
		contentPane.add(liveBtn, 2);

		state = 0;

		bgSound = new SoundMaker();
		deathSound = new SoundMaker();
		clickSound = new SoundMaker();

		setVisible(true);
	}

	public void start() { // �׼��̺�Ʈ ���� �޼ҵ�
		action = new Myaction();
		sendBtn.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		textField.addActionListener(action);
		startBtn.addActionListener(action);
		deathBtn.addActionListener(action);
		liveBtn.addActionListener(action);
	}

	public void nightAction(User u) {
		switch (v.get(0).getJob()) {
		case 1: // ���� ���Ǿ��϶�
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 2: // ���� �����϶�
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 3: // �ǻ�
			send_Message("JOBS:DOCTOR:" + u.getId() + ":");
			insertText(u.getId() + "���� ��Ƚ��ϴ�.\n", Color.YELLOW);
			break;
		case 4: // ����
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 5: // ����
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 6: // ����
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 7: // ��ġ��
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 8: // �׷�
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 9: // ������
			send_Message("JOBS:" + u.getId() + ":");
			break;
		default:
			break;
		}
	}

	public void voteAction(User u) {
		switch (state) {
		case 2: // �����뿡 �ø� ��� ��ǥ
			// NORMALVOTE:�����̵�:������id
			send_Message("NORMALVOTE:" + id + ":" + u.getId() + ":");
			break;
		case 4: // ����ǥ
			// JOBS:����:������id
			nightAction(u);
			break;
		default:
			break;
		}
		allButtonFalseEnable();
	}

	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		@Override
		public void actionPerformed(ActionEvent e) {

			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				String msg2 = null;
				if (textField.getText().equals("")) {
					System.out.println("textField �������");
				} else {
					msg = String.format("MSG:%s:%s\n", id, textField.getText());
					msg2 = String.format("[%s] : %s\n", id, textField.getText());
					send_Message(msg + ":");
					if (v.get(0).getJob() == 1 && v.get(0).getLive() == true)
						insertText(msg2, Color.RED);
					else if (v.get(0).getJob() == 0 && v.get(0).getLive() == true)
						insertText(msg2, Color.WHITE);
					else if (v.get(0).getLive() == false)
						insertText(msg2, Color.GRAY);
					else
						insertText(msg2, Color.WHITE);

					textField.setText(""); // �޼����� ������ ���� �޼��� ����â�� ����.
					textField.requestFocus(); // �޼����� ������ Ŀ���� �ٽ� �ؽ�Ʈ �ʵ�� ��ġ��Ų��
				}
			}
			if (e.getSource() == startBtn) {
				send_Message("GAMESTART:");
				startBtn.setVisible(false);
				clickSound.play("sound/click.wav");
			}
			if (e.getSource() == deathBtn) {
				send_Message("VOTE2:AGREE:");
				deathBtn.setVisible(false);
				liveBtn.setVisible(false);
				clickSound.play("sound/click.wav");
			} else if (e.getSource() == liveBtn) {
				send_Message("VOTE2:DISAGREE:");
				deathBtn.setVisible(false);
				liveBtn.setVisible(false);
				clickSound.play("sound/click.wav");
			}
			switch (v.size()) {
			case 1:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				}
				break;
			case 2:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				}
				break;
			case 3:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				}
				break;
			case 4:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(3).getButton()) {
					voteAction(v.get(3));
					clickSound.play("sound/click.wav");
				}
				break;
			case 5:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(3).getButton()) {
					voteAction(v.get(3));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(4).getButton()) {
					voteAction(v.get(4));
					clickSound.play("sound/click.wav");
				}
				break;
			case 6:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(3).getButton()) {
					voteAction(v.get(3));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(4).getButton()) {
					voteAction(v.get(4));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(5).getButton()) {
					voteAction(v.get(5));
					clickSound.play("sound/click.wav");
				}
				break;
			case 7:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(3).getButton()) {
					voteAction(v.get(3));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(4).getButton()) {
					voteAction(v.get(4));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(5).getButton()) {
					voteAction(v.get(5));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(6).getButton()) {
					voteAction(v.get(6));
					clickSound.play("sound/click.wav");
				}
				break;
			case 8:
				if (e.getSource() == v.get(0).getButton()) {
					voteAction(v.get(0));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(1).getButton()) {
					voteAction(v.get(1));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(2).getButton()) {
					voteAction(v.get(2));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(3).getButton()) {
					voteAction(v.get(3));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(4).getButton()) {
					voteAction(v.get(4));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(5).getButton()) {
					voteAction(v.get(5));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(6).getButton()) {
					voteAction(v.get(6));
					clickSound.play("sound/click.wav");
				} else if (e.getSource() == v.get(7).getButton()) {
					voteAction(v.get(7));
					clickSound.play("sound/click.wav");
				}
				break;
			default:
				break;
			}
		}
	}

}