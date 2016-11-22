import java.awt.FileDialog;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.filechooser.FileSystemView;


/*
 * a simple java Recorder
 */
public class JavaRecorder {
	
	public static AudioFormat getAudioFormat() {
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		boolean bigEndian = false;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}
	
	AudioFormat af;
	boolean flg;
	ByteArrayOutputStream baos = null;
	DataLine.Info targetinfo,sourceinfo;
	
	TargetDataLine td;
	SourceDataLine sd;
	
	JavaRecorder(){
		try {
			af = getAudioFormat();
			targetinfo = new DataLine.Info(TargetDataLine.class, af);				
			td = (TargetDataLine) (AudioSystem.getLine(targetinfo));
				
			sourceinfo = new DataLine.Info(SourceDataLine.class, af);	
			sd = (SourceDataLine) (AudioSystem.getLine(sourceinfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void paly(String fileName){
		try{
			File file = new File(fileName);
			AudioInputStream ais = AudioSystem.getAudioInputStream(file);
			
			AudioFormat af = AudioSystem.getAudioFileFormat(file).getFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);			
			SourceDataLine sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			
			sd.open();
			sd.start();
			
			byte buf[] = new byte[0xFF];
			int len;
			while ((len = ais.read(buf, 0, buf.length)) != -1) {
				sd.write(buf, 0, len);
			}		
			sd.drain();
			sd.close();		
			ais.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	class Recorder implements Runnable {
		@Override
		public void run() {
			try {		
System.out.println("Begin record");
	
				td.open(af);
				td.start();
				
				byte bts[] = new byte[10000];
				baos = new ByteArrayOutputStream();
				
				flg = true;
				
				while (flg) {
					int cnt = td.read(bts, 0, bts.length);
					if (cnt > 0) {
						baos.write(bts, 0, cnt);
					}
				}
				
				td.drain();
				td.close();
				baos.close();
							
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	class RecordPlayer implements Runnable {
		public void run() {	
System.out.println("Begin playRecord");
			try {
				byte audioData[] = baos.toByteArray();				
				sd.open(af);
				sd.start();
				sd.write(audioData, 0, audioData.length);
				sd.drain();
				sd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	void start(){
		Thread t = new Thread(new Recorder());
		t.start();
	}
	
	void stop(){		
		flg = false;
	}
	
	void playRecord(){
		Thread t = new Thread(new RecordPlayer());
		t.start();
	}
	
	void saveToDesktop() {
		AudioFormat af = getAudioFormat();
		byte audioData[] = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
		AudioInputStream ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());
		try {
			File filePath = new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());			
			File file = new File(filePath.getPath() + "/" + System.currentTimeMillis() + ".mp3");
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
