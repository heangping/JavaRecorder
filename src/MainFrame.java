import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
public class MainFrame extends JFrame{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -1082166342481848841L;
	JButton openBtn = new JButton("�����ļ�");
	JButton beginBtn = new JButton("��ʼ¼��");
	JButton stopBtn = new JButton("ֹͣ¼��");
	JButton playBtn = new JButton("����¼��");
	JButton saveBtn = new JButton("����¼��������");
	JavaRecorder jrecorder = new JavaRecorder();
	
	MainFrame(){																		
		setLayout(new GridLayout(1,5));
		Frame tt = this;
		add(openBtn);add(beginBtn);add(stopBtn);add(playBtn);add(saveBtn);
		openBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				FileDialog fLoader = new FileDialog(tt,"ѡ��򿪵��ļ�",FileDialog.LOAD);				
				fLoader.setVisible(true);
				jrecorder.paly(fLoader.getDirectory() + fLoader.getFile());
			}
		});
		
		beginBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jrecorder.start();
			}
		});
		
		stopBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jrecorder.stop();
			}
		});
		
		playBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jrecorder.playRecord();
			}
		});
		
		saveBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				jrecorder.saveToDesktop();
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	} 
	public static void main(String args[]) throws Exception {
		new MainFrame();
	}
}