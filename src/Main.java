import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import main.GamePanel;

public class Main {
	Thread gameThread;
	
	
	public static void main(String[] args) {
		//setup Jframe
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setResizable(false);
		window.setTitle("Footprints");
		window.setSize(1920,1080);
		window.setResizable(false);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		JLabel timer = new JLabel();
		//startinf player pos
		GamePanel gamePanel = new GamePanel();
		gamePanel.px[0] = 300;
		gamePanel.py[0] = 300;
		gamePanel.px[1] = 270;
		gamePanel.py[1] = 300;
		gamePanel.px[2] = 240;
		gamePanel.py[2] = 300;
		gamePanel.tempX[0] = 300;
		gamePanel.tempY[0] = 300;
		gamePanel.tempX[1] = 270;
		gamePanel.tempY[1] = 300;
		gamePanel.tempX[2] = 240;
		gamePanel.tempY[2] = 300;
	//add jpanel
		timer.setText(""+gamePanel.getTimeElapsed());
		timer.setBounds(300,10,100,300);
		window.add(timer);
		window.add(gamePanel);
		//window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		// TODO Auto-generated method stub
		gamePanel.startGameThread();
		//new FootTest();
		
	}

	

}
