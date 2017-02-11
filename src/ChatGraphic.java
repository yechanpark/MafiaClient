import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChatGraphic extends Thread{
	JPanel dialogPane;
	JLabel chatDialog;
	JLabel background;
	int time = 3;
	String dialog = "";
	private boolean using = false;
	BufferedImage bfImage;
	
	public ChatGraphic(JPanel dialogPane, int index){
		this.dialogPane = dialogPane;
		chatDialog = new JLabel("");
		chatDialog.setVisible(false);
		chatDialog.setForeground(Color.WHITE);
		switch(index){
		case 1:
			try {
				bfImage = ImageIO.read(new File("img/dialog1.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				bfImage = ImageIO.read(new File("img/dialog2.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				bfImage = ImageIO.read(new File("img/dialog3.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				bfImage = ImageIO.read(new File("img/dialog4.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 5:
			try {
				bfImage = ImageIO.read(new File("img/dialog5.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 6:
			try {
				bfImage = ImageIO.read(new File("img/dialog6.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 7:
			try {
				bfImage = ImageIO.read(new File("img/dialog7.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 8:
			try {
				bfImage = ImageIO.read(new File("img/dialog8.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 0:
			try {
				bfImage = ImageIO.read(new File("img/dialog.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
			
		}
		background = new JLabel(new ImageIcon(bfImage));
		background.setVisible(false);
		
		dialogPane.add(chatDialog, 0);
		dialogPane.add(background, 1);
	}
	
	public void setDialog(String dialog){
		this.dialog = dialog;
	}
	
	public void setPosition(int x, int y, int w, int h){
		background.setBounds(x, y, w, h);
		chatDialog.setBounds(x+20, y, w - 20, h-5);
	}
	public void setUsing(boolean using){
		this.using = using;
	}
	public boolean getUsing(){
		return using;
	}
	
	public void terminate(){
		background.setVisible(false);
		chatDialog.setVisible(false);
		using = false;
	}
	public void run(){
		using = true;
		background.setVisible(true);
		chatDialog.setVisible(true);
		chatDialog.setText(dialog);
		try {
			sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		terminate();
	}
}
