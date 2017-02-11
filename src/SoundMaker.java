import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundMaker {
	Clip clip;
	boolean playing = false;
    
	public void play(String fileName){
		try{
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			playing = true;
		}
		catch (Exception ex){
		    System.out.println("Exception : " + ex);
		}
	}
	public void stop(){
		clip.stop();
		clip.close();
		playing = false;
	}
}
