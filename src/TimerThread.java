import javax.swing.JLabel;

public class TimerThread extends Thread{
	JLabel view;
	int time;
	boolean running;
	public TimerThread(JLabel view, int time){
		this.view = view;
		this.time = time;
		running = true;
	}
	public JLabel getLabel(){
		return view;
	}
	public void terminate(){
		running = false;
	}
	public void run(){

			for(int i = time; i >= 0; i--){
				if(running == false){
					break;
				}
				try{
					Thread.sleep(1000);
				}catch(Exception e){
				}
				String tmp = Integer.toString(i);
				view.setText(tmp);
			}
	}
}
