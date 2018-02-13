import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class User {
	private String id;	// id
	private int num; // 인덱스
	private JButton button; // 캐릭터 
	private JLabel idLabel;
	private JPanel panel; // 메인뷰 판넬
	private int job; //직업
	private boolean master = false; // 방장 권한
	private ChatGraphic g;
	private ChatGraphic g1;
	private ChatGraphic g2;
	private ChatGraphic g3;
	private ChatGraphic g4;
	private ChatGraphic g5;
	private ChatGraphic g6;
	private ChatGraphic g7;
	private ChatGraphic g8;
	
	private boolean live = true;	//생존여부
	private int index;
	private BufferedImage bfImage;

	public User(JPanel panel){
		this.panel = panel;
		try {
			bfImage = ImageIO.read(new File("img/civil.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		button = new JButton(new ImageIcon(bfImage));
		button.setBackground(Color.BLACK);
		button.setBorderPainted(false);
		button.setEnabled(false);
		panel.add(button, 3);
		
		idLabel = new JLabel("");
		idLabel.setBackground(Color.BLACK);
		idLabel.setForeground(Color.WHITE);
		panel.add(idLabel, 2);
		
		g = new ChatGraphic(panel, 0);
		g1 = new ChatGraphic(panel, 1);
		g2 = new ChatGraphic(panel, 2);
		g3 = new ChatGraphic(panel, 3);
		g4 = new ChatGraphic(panel, 4);
		g5 = new ChatGraphic(panel, 5);
		g6 = new ChatGraphic(panel, 6);
		g7 = new ChatGraphic(panel, 7);
		g8 = new ChatGraphic(panel, 8);
	}
	public void setId(String id){
		this.id = id;
	}
	public void setNum(int num){
		this.num = num;
	}
	public void setJob(int job){
		this.job = job;
	}
	public String getId(){
		return id;
	}
	public JLabel getIdLabel(){
		return idLabel;
	}
	public ChatGraphic getGraphic2(int GraphicIndex){
		switch(GraphicIndex){
			case 0:
				return g1;
			case 1:
				return g2;
			case 2:
				return g3;
			case 3:
				return g4;
			case 4:
				return g5;
			case 5:
				return g6;
			case 6:
				return g7;
			case 7:
				return g8;
			default:
				return g;
		}
	}
//	public ChatGraphic getGraphic(){
//		return g;
//	}
//	
	public void remakeGraphic2(int GraphicIndex){
		switch(GraphicIndex){
			case 0:
				g1 = new ChatGraphic(panel, 1);
				break;
			case 1:
				g2 = new ChatGraphic(panel, 2);
				break;
			case 2:
				g3 = new ChatGraphic(panel, 3);
				break;
			case 3:
				g4 = new ChatGraphic(panel, 4);
				break;
			case 4:
				g5 = new ChatGraphic(panel, 5);
				break;
			case 5:
				g6 = new ChatGraphic(panel, 6);
				break;
			case 6:
				g7 = new ChatGraphic(panel, 7);
				break;
			case 7:
				g8 = new ChatGraphic(panel, 8);
				break;
			default:
				break;
		}
	}
	
//	public void remakeGraphic(){
//		g = new ChatGraphic(panel);
//	}
	
	public int getNum(){
		return num;
	}
	public void setMaster(boolean master){
		this.master = master;
	}
	public boolean getMaster(){
		return master;
	}
	public JButton getButton(){
		return button;
	}
	public void setIndex(int index){
		this.index = index;
	}
	public int getIndex(){
		return index;
	}
	public int getJob(){
		return job;
	}
	public void setLive(boolean live){
		this.live = live;
	}
	public boolean getLive(){
		return live;
	}
}
