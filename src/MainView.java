
// MainView.java : Java Chatting Client 의 핵심부분
// read keyboard --> write to network (Thread 로 처리)
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
	private JTextField textField; // 보낼 메세지 쓰는곳

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
	JButton sendBtn; // 전송버튼
	JButton startBtn; // 스타트 버튼
	JButton deathBtn; // 살리는 버튼
	JButton liveBtn; // 죽이는 버튼
	JLabel centerLabel;
	JTextPane textPane; // 수신된 메세지를 나타낼 변수
	StyledDocument doc;
	SimpleAttributeSet attribute = new SimpleAttributeSet();

	private BufferedImage bfImage;

	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	public MainView(String id, String password, String ip, int port)// 생성자
	{
		this.id = id;
		this.password = password;
		this.ip = ip;
		this.port = port;

		init();
		start();

		insertText("매개 변수로 넘어온 값 : " + id + " " + password + " " + ip + " " + port + "\n", Color.YELLOW);
		network();
	}

	public void network() {
		// 서버에 접속
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket이 null값이 아닐때 즉! 연결되었을때
			{
				Connection(); // 연결 메소드를 호출
			}
		} catch (UnknownHostException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "접속 에러", attribute);
			} catch (BadLocationException ee) {
				ee.printStackTrace();
			}
		} catch (IOException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "접속 에러", attribute);
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

	public void Connection() { // 실직 적인 메소드 연결부분
		try { // 스트림 설정
			is = socket.getInputStream();
			dis = new DataInputStream(is);

			os = socket.getOutputStream();
			dos = new DataOutputStream(os);

		} catch (IOException e) {
			attribute.addAttribute(StyleConstants.Foreground, Color.BLUE);
			try {
				doc.insertString(0, "스트림 설정 에러", attribute);
			} catch (BadLocationException ee) {
				ee.printStackTrace();
			}
		}
		String tmp = id + ":" + password + ":";
		send_Message(tmp); // 정상적으로 연결되면 나의 id를 전송

		Thread th = new Thread(new Runnable() { // 스레드를 돌려서 서버로부터 메세지를 수신

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

						// insertText("받은 메시지 : ["+ msg +"]\n", Color.BLUE);

						if (tmp2[0].equals("ERROR")) {
							if (tmp2[1].equals("FULL")) {
								insertText("방이 꽉 찼습니다.\n", Color.YELLOW);
							} else if (tmp2[1].equals("PLAYING")) {
								insertText("게임이 진행중입니다.\n", Color.YELLOW);
							}
						} else if (tmp2[0].equals("USINGID")) {
							insertText("접속중인 아이디 입니다.\n", Color.YELLOW);
						} else if (tmp2[0].equals("WRONGID")) {
							insertText("아이디나 비밀번호가 틀립니다.\n", Color.YELLOW);
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
										insertText(tmp2[2] + "님이 로그아웃 하셨습니다.\n", Color.YELLOW);
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
										insertText(tmp2[2] + "님의 연결이 끊겼습니다..\n", Color.YELLOW);
									}
								}
							}
						} else if (tmp2[0].equals("ULIST")) { // 자신보다 먼저접속한 유저
																// 처리
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
									centerLabel.setText("당신은 방장입니다");
									startBtn.setVisible(true);
									v.get(0).setMaster(true);
									break;
								default:
									break;
								}
							}
						} else if (tmp2[0].equals("LOGIN")) { // 자신보다 뒤에 접속하는 유저
																// 세팅
							if (tmp2[1].equals(id)) {

							} else
								characterSetting(tmp2[1]);
						} else if (tmp2[0].equals("START")) { // 개임시작 + 직업 배정
							insertText("게임이 시작되었습니다.\n지금은 밤입니다.\n", Color.YELLOW);
							if (bgSound.playing == true)
								bgSound.stop();
							bgSound.play("sound/Uka.wav");
							textField.setEnabled(false);
							textField.setBackground(Color.BLACK);
							timer = new TimerThread(timerLabel, 15);
							timer.start();
							timerLabel.setVisible(true);
							timerLabel2.setVisible(true);
							state = 4; // 밤 상태
							if (tmp2[1].equals("MAFIA")) {
								insertText("당신은 마피아 입니다.\n", Color.YELLOW);
								centerLabel.setText("당신은 마피아 입니다");
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
								civilSetting("당신은 의사입니다.");
							} else if (tmp2[1].equals("POLICE")) {
								setBfImage("img/police.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(2);
								civilSetting("당신은 경찰입니다.");
							} else if (tmp2[1].equals("REPORTER")) {
								setBfImage("img/reporter.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(4);
								civilSetting("당신은 기자입니다.");
							} else if (tmp2[1].equals("POLITICIAN")) {
								setBfImage("img/politician.png");
								v.get(0).getButton().setIcon(new ImageIcon(bfImage));
								v.get(0).setJob(7);
								civilSetting("당신은 정치인입니다.");
							} else if (tmp2[1].equals("CIVIL")) {
								v.get(0).setJob(0);
								civilSetting("당신은 시민입니다.");
							} else {
								insertText(msg, Color.BLUE);
							}
							if (v.get(0).getJob() != 0 && v.get(0).getJob() != 4) { // 일반
																					// 시민이
																					// 아닐
																					// 때
								allButtonTrueEnable();
							}
						} else if (tmp2[0].equals("MORNING")) {
							bgSound.stop();
							bgSound.play("sound/L.wav");
							insertText("아침이 밝았습니다.\n", Color.YELLOW);
							if (tmp2[1].equals("MDIE")) { // 살해당하는 사람이 있을때
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										setBfImage("img/death.png");
										v.get(i).getButton().setIcon(new ImageIcon(bfImage));
										v.get(i).setLive(false);
									}
								}
								deathSound.play("sound/scream.wav");
								insertText(tmp2[2] + "님이 살해당했습니다.\n", Color.YELLOW);
							} else if (tmp2[1].equals("HEAL")) { // 의사가 살렸을때
								insertText("의사가 " + tmp2[2] + "님을 살렸습니다\n", Color.YELLOW);
							} else if (tmp2[1].equals("NODIE")) {
								insertText("지난 밤엔 아무도 죽지 않았습니다\n", Color.YELLOW);
							}

							if (tmp2[3].equals("REPORTER")) {
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[4])) {
										insertText("기자가 " + tmp2[4] + "님을" + tmp2[5] + "라고 제보했습니다.\n", Color.YELLOW);
									}
								}
							}
							state = 1; // 낮 상태
							timerRestart(20);
							textField.setEnabled(true);
							textField.setBackground(Color.WHITE);
							centerLabel.setText("아침이 밝았습니다");
							allButtonFalseEnable();
						} else if (tmp2[0].equals("NIGHT")) { // 밤에 직업 활동
							bgSound.stop();
							bgSound.play("sound/Uka.wav");
							if (tmp2[1].equals("VDIE")) { // 재판으로 죽을때
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getId().equals(tmp2[2])) {
										setBfImage("img/death.png");
										v.get(i).getButton().setIcon(new ImageIcon(bfImage));
										v.get(i).setLive(false);
									}
								}
								deathSound.play("sound/scream.wav");
								insertText(tmp2[2] + "님이 사형당했습니다.\n", Color.YELLOW);
							} else if (tmp2[1].equals("NODIE")) {
								insertText("사형을 집행하지 않았습니다.\n", Color.YELLOW);
							} else if (tmp2[1].equals("NOVOTE")) {
								insertText("과반수가 넘는 표를 받은자가 없습니다.\n", Color.YELLOW);
							}
							deathBtn.setVisible(false);
							liveBtn.setVisible(false);
							insertText("밤이 되었습니다.\n", Color.YELLOW);
							state = 4;
							timerRestart(15);
							if (v.get(0).getJob() == 1) { // 마피아일때
								textField.setEnabled(true);
								textField.setBackground(Color.WHITE);
								centerLabel.setText("죽일 사람을 선택하세요");
								for (int i = 0; i < v.size(); i++) {
									if (v.get(i).getJob() != 1) {
										if (v.get(i).getLive() == true) {
											v.get(i).getButton().setEnabled(true);
										}
									}
								}
							} else if (v.get(0).getJob() == 2 && v.get(0).getLive() == true) { // 경찰일때
								centerLabel.setText("조사 할 사람을 선택하세요");
								allButtonTrueEnable();
							} else if (v.get(0).getJob() == 3 && v.get(0).getLive() == true) { // 의사일때
								centerLabel.setText("치료할 사람을 선택하세요");
								allButtonTrueEnable();
							} else if (v.get(0).getJob() == 4 && v.get(0).getLive() == true) { // 기자일때
								allButtonTrueEnable();
								centerLabel.setText("제보할 사람을 선택하세요");
							} else if (v.get(0).getJob() == 7) { // 정치가일때
								centerLabel.setText("밤이 되었습니다");
							} else if (v.get(0).getJob() == 0) { // 일반 시민일때
								centerLabel.setText("밤이 되었습니다");
							}
							// if(v.get(0).getLive() == false){
							// textField.setEnabled(true);
							// textField.setBackground(Color.WHITE);
							// }
						} else if (tmp2[0].equals("VOTE")) { // 낮 투표
							insertText("투표 하세요.\n", Color.YELLOW);
							centerLabel.setText("사형대에 올릴 사람을 뽑아주세요");
							timerRestart(10);
							state = 2; // 죽일사람 투표중인 상태
							if (v.get(0).getLive() == true) { // 살아있는 사람만 투표
								allButtonTrueEnable();
							}
						} else if (tmp2[0].equals("PLEAD")) { // 최후의 변론
							insertText(tmp2[1] + "님이 가장 많은 표를 받았습니다.\n" + tmp2[1] + "님 최후의 변론\n", Color.YELLOW);
							centerLabel.setText(tmp2[1] + "님 최후의 변론");
							timerRestart(10);
							state = 3; // 최후의변론 + 찬반투표시간
							if (v.get(0).getLive() == true) {
								if (tmp2[1].equals(id)) {
								} else {
									textField.setEnabled(false);
									textField.setBackground(Color.BLACK);
								}
							}
						} else if (tmp2[0].equals("VOTE2")) { // 죽일지 살릴지 투표
							insertText("변론시간이 끝났습니다\n사형을 집행하시겠습니까?\n", Color.YELLOW);
							centerLabel.setText("사형을 집행하시겠습니까?\n");
							timerRestart(7);
							if (v.get(0).getLive() == true) { // 살아있는 사람만 투표
								textField.setEnabled(false);
								textField.setBackground(Color.BLACK);
								deathBtn.setVisible(true);
								liveBtn.setVisible(true);
							}
						} else if (tmp2[0].equals("RESULTPOLICE")) { // 경찰 투표 결과
																		// 조회
							if (tmp2[1].equals("ISMAFIA")) {
								insertText("대상은 마피아 입니다\n", Color.YELLOW);
								centerLabel.setText("대상은 마피아입니다");
							} else {
								insertText("대상은 마피아가 아닙니다.\n", Color.YELLOW);
								centerLabel.setText("대상은 마피아가 아닙니다.");
							}
						} else if (tmp2[0].equals("RESULTREPORTER")) {
							insertText("첫날 밤에는 취재를 할 수 없습니다.\n", Color.YELLOW);
						} else if (tmp2[0].equals("NORMALVOTE")) { // 재판 투표 과정
																	// 보여주기
							insertText("[ " + tmp2[2] + " ] 님  " + tmp2[1] + " 표\n", Color.YELLOW);
						} else if (tmp2[0].equals("USERLESS")) { // 인원이 부족해서 시작
																	// 못할때
							startBtn.setVisible(true);
							insertText("인원이 부족합니다\n", Color.YELLOW);
						} else if (tmp2[0].equals("GAMEOVER")) { // 게임 끝
							bgSound.stop();
							if (tmp2[1].equals("MAFIA")) { // 마피아가 이겼을떄
								insertText("마피아 승리!!\n", Color.YELLOW);
								centerLabel.setText("마피아 승리");
								if (v.get(0).getJob() == 1)
									bgSound.play("sound/win.wav");
								else
									bgSound.play("sound/lose.wav");
							} else { // 시민이 이겼을떄
								insertText("시민 승리!!\n", Color.YELLOW);
								centerLabel.setText("시민 승리");
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
						} else if (tmp2[0].equals("MSG")) { // 일반 채팅 메시지 마피아,
															// 유령, 시민 구분
							String[] tmp3 = msg.split(":", 4);
							String tmp = null;
							tmp = String.format("[%s] : %s", tmp3[1], tmp3[2]);
							if (tmp3[1].equals(id)) {
								playChatGraphicThread(0, tmp);
							} else {
								if (v.get(0).getJob() == 1) { // 내가 마피아일 때
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
								} else { // 내가 시민 또는 다른직업일 때
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
						// textArea.append("메세지 수신 에러!!\n");
						// 서버와 소켓 통신에 문제가 생겼을 경우 소켓을 닫는다
						exit();
						break;
					}
				} // while문 끝
			}// run메소드 끝
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

	public void send_Message(String str) { // 서버로 메세지를 보내는 메소드
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
			// textArea.append("메세지 송신 에러!!\n");
		}
		// insertText("보낸 메시지:["+ str +"]\n",Color.MAGENTA);
	}

	public void init() { // 화면구성 메소드
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

		sendBtn = new JButton("전송");
		sendBtn.setBounds(916, 532, 60, 32);
		sendBtn.setForeground(Color.WHITE);
		sendBtn.setBackground(Color.BLACK);
		contentPane.add(sendBtn);
		textPane.setEditable(false); // 사용자가 수정못하게 막는다

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

		timerLabel2 = new JLabel("남은 시간");
		timerLabel2.setBounds(325, 220, 100, 30);
		timerLabel2.setForeground(Color.YELLOW);
		contentPane.add(timerLabel2, 2);
		timerLabel2.setVisible(false);

		centerLabel = new JLabel("환영합니다.", JLabel.CENTER);
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

	public void start() { // 액션이벤트 지정 메소드
		action = new Myaction();
		sendBtn.addActionListener(action); // 내부클래스로 액션 리스너를 상속받은 클래스로
		textField.addActionListener(action);
		startBtn.addActionListener(action);
		deathBtn.addActionListener(action);
		liveBtn.addActionListener(action);
	}

	public void nightAction(User u) {
		switch (v.get(0).getJob()) {
		case 1: // 내가 마피아일때
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 2: // 내가 경찰일때
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 3: // 의사
			send_Message("JOBS:DOCTOR:" + u.getId() + ":");
			insertText(u.getId() + "님을 살렸습니다.\n", Color.YELLOW);
			break;
		case 4: // 기자
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 5: // 영매
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 6: // 군인
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 7: // 정치가
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 8: // 테러
			send_Message("JOBS:" + u.getId() + ":");
			break;
		case 9: // 스파이
			send_Message("JOBS:" + u.getId() + ":");
			break;
		default:
			break;
		}
	}

	public void voteAction(User u) {
		switch (state) {
		case 2: // 사형대에 올릴 사람 투표
			// NORMALVOTE:내아이디:선택한id
			send_Message("NORMALVOTE:" + id + ":" + u.getId() + ":");
			break;
		case 4: // 밤투표
			// JOBS:직업:선택한id
			nightAction(u);
			break;
		default:
			break;
		}
		allButtonFalseEnable();
	}

	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {

			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == sendBtn || e.getSource() == textField) {
				String msg = null;
				String msg2 = null;
				if (textField.getText().equals("")) {
					System.out.println("textField 비어있음");
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

					textField.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
					textField.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
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