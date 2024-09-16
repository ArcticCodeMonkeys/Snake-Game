import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
//declaration
KeyHandler keyH = new KeyHandler();
Thread gameThread;
BufferedImage image = null;
String lastKey;
Random rand = new Random();
int deathTimer = 0;
boolean death = false;
int foodX = 30*rand.nextInt(0,44);
int foodY = 30*rand.nextInt(2,23);
int powerUpX = 30*rand.nextInt(0,44);
int powerUpY = 30*rand.nextInt(2,23);
int increaseTimer = 0;
int length = 3;
int px[] = new int[100];
int py[] = new int[100];
int tempX[] = new int[100];
int tempY[] = new int[100];
int pw = 30;
int ph = 30;
int FPS = 7;
int timeBonus = 0;
int timeElapsed = 0;
int score = 0;
int powerUpType = rand.nextInt(0,3);
boolean timeStop = false;
boolean invincible = false;
String storeTime;
String name;
String highScore;
public GamePanel() {
	//setting up jpanel
	this.setPreferredSize(new Dimension(1400, 700));
	this.setBackground(Color.WHITE);
	this.setDoubleBuffered(true);
	this.addKeyListener(keyH);
	this.setFocusable(true);
	
}
public void startGameThread() {
	//start game thread
	gameThread = new Thread(this);
	gameThread.start();
}
public String getTimeElapsed() {
	//calculate time and format
	int secondsLeft = 60 - timeElapsed;
	int minutesLeft = secondsLeft/60;
	secondsLeft = secondsLeft - (minutesLeft*60);
	String time = minutesLeft + ":";
	if(secondsLeft < 10) {
		time = time +"0";
	}
	time = time + secondsLeft;
	//return time
	return time;
}
public void posSaver() {
	//stop walking offscreen
	if(px[0] < 0) {
		px[0] = 0;
	} else if(py[0] < 60) {
		py[0] = 60;
	} else if(px[0] > 1330) {
		px[0] = 1330;
	
	} else if(py[0] > 700) {
		px[0] = 700;
	}
		
}

public void run() {
	//main game loop
	// TODO Auto-generated method stub
	int startingTime = (int) (System.nanoTime()/1000000000);
	 String line = "";
	 int infoCount = 0;
	 //read highscore
	try (FileReader reader = new FileReader("info.txt");
            BufferedReader br = new BufferedReader(reader)) {
         
           while ((line = br.readLine()) != null) {
           		highScore = line;
           }
	} catch(Exception e) {
		
	}
	
	while(gameThread != null) {
		
		//loop
		//get drawing intervals and timer
		timeElapsed = -1*((int) (startingTime -(System.nanoTime()/1000000000))) -timeBonus;
		double drawInterval = 1000000000/FPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		long currentTime = System.nanoTime();
		
	//	System.out.println("Running");
		//System.out.println(timeElapsed);
		if(death != true) {
			//update player pos and info
		update();
		} 
		//System.out.println(powerUpType);
		//paint new pos and colors
		repaint();
		if(death != true) {
			//check if dead
		deathCheck();
		//stop walking off screen
		posSaver();
		//eating logic
		eatCheck();
		if(timeElapsed == 60) {
			//die when timer reaches 0
			death = true ;
		}
		} else {
			if(score > Integer.parseInt(highScore)) {
			try (FileWriter writer = new FileWriter("info.txt");
					
					 BufferedWriter bw = new BufferedWriter(writer)) {
				//write highscore
				bw.write(""+score);
					bw.close();
					
				} catch (Exception e) {
					System.err.format("IOException: %s%n", e);
				}
		}
		}
		
		
		
		
		if(keyH.restart == true && death == true) {
			//reset values to base when restarting
			px[0] = 300;
			py[0] = 300;
			px[1] = 270;
			py[1] = 300;
			px[2] = 240;
			py[2] = 300;
			tempX[0] = 300;
			tempY[0] = 300;
			tempX[1] = 270;
			tempY[1] = 300;
			tempX[2] = 240;
			tempY[2] = 300;
			startingTime = (int) (System.nanoTime()/1000000000);
			timeBonus = 0;
			length = 3;
			FPS = 7;
			foodX = 30*rand.nextInt(0,44);
			foodY = 30*rand.nextInt(0,23);
			powerUpX = 30*rand.nextInt(0,44);
			powerUpY = 30*rand.nextInt(0,23);
			keyH.downPressed = false;
			keyH.leftPressed = false;
			keyH.rightPressed = true;
			keyH.upPressed = false;
			keyH.restart = false;
			death = false;
			
		}
		
 		try {
 			//time drawing intervals
			double remainingTime = nextDrawTime - System.nanoTime();
			
			remainingTime = remainingTime/1000000;
			
			if(remainingTime < 0) {
				remainingTime =0;
				
			}
			Thread.sleep((long) remainingTime);
			nextDrawTime += drawInterval;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
public void posCalc() {
	//calculate trailing piece positons
	for(int i = 0; i<length; i++) {
		int pieceNum = length;
		pieceNum = pieceNum - i;
		tempX[pieceNum] = tempX[pieceNum-1];
		tempY[pieceNum] = tempY[pieceNum-1];
	}
}

public void posSet() {
	//set trailing positions
	for(int i = 0; i<length; i++) {
		px[i] = tempX[i];
		py[i] = tempY[i];
	}
}
public void deathCheck() {
	//check if run into walls or self, then kill
		for(int k = 1; k<length-1; k++) {
			if(px[k] == px[0] && py[k] == py[0] && length > 3) {
				if(invincible != true) {
				death = true;
				}
				
				//System.out.println("Death time:" + deathTimer);
			} else {
				
			}
		}
	
	if(px[0] < 0 || py[0] < 60 || px[0] > 1330 || py[0] > 700) {
		
		death = true;
		
		//System.exit(1);
	}
}
public void eatCheck() {
	//checks if a food or power up was eaten
	if(px[0] == foodX && py[0] == foodY) {
		length++;
		timeBonus += 2;
		px[length-1] = px[length-2];
		py[length-1] = py[length-2];
		//makes a new food
		 foodX = 30*rand.nextInt(0,44);
		 
		 foodY = 30*rand.nextInt(2,23);
		
		 score++;
		 
	}
	if(px[0] == powerUpX && py[0] == powerUpY) {
		//different powerup logic
		switch(powerUpType) {
		case 0: 
			//invincibility
			invincible = true;
			Thread t1 = new Thread(new Runnable() {
			    @Override
			    //terminate
			    public void run() {
			    	try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	invincible = false;
			    	
			    	
			    }
			}); 
			t1.start();
			
			break;
		case 1:
			//Speed Boost
			FPS = FPS +4;
			
			Thread t2 = new Thread(new Runnable() {
			    @Override
			    //terminate
			    public void run() {
			    	try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	 FPS = FPS -4;
			    	
			    	
			    }
			}); 
			t2.start();
			
			break;
		case 2:
			//Time Stop
			timeStop = true;
			storeTime = getTimeElapsed();
			Thread t3 = new Thread(new Runnable() {
			    @Override
			    //terminate
			    public void run() {
			    	try {
						TimeUnit.SECONDS.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	timeBonus+= 10;
			    	timeStop = false;
			    	
			    	
			    }
			}); 
			
				t3.start();
			
			
			break;
		}
		//randomize powerup type
		powerUpType = rand.nextInt(0,3);
		//randomize pos
		 powerUpX = 30*rand.nextInt(0,44);
		 powerUpY = 30*rand.nextInt(2,23);
	}
}
	public void update() {
		//move players depending on where they pressed. Disallow walking back on self.
	if(keyH.upPressed == true && lastKey != "DOWN") {
		//save current position of all feet.
		//remove last foot
		//calculate nextFootPosition
		//draw foot there
		posCalc();
		tempY[0] = tempY[0]-30;
		posSet();
		
		
		lastKey = "UP";
		//System.out.println(keyH.lastKeyPressed);
		
	} else if(keyH.downPressed == true && lastKey != "UP") {
		posCalc();
		tempY[0] = tempY[0]+30;
		posSet();
		
		lastKey = "DOWN";
		//System.out.println(keyH.lastKeyPr essed);
		
	} else if(keyH.leftPressed == true && lastKey != "RIGHT") {
		posCalc();
		tempX[0] = tempX[0]-30;
		posSet();
		
		lastKey = "LEFT";
		//System.out.println(keyH.lastKeyPressed);
		
	} else if(keyH.rightPressed == true && lastKey != "LEFT") {
		posCalc();
		tempX[0] = tempX[0]+30;
		posSet();
		
		lastKey = "RIGHT";
		//System.out.println(keyH.lastKeyPressed);
	
}
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//drawing food and powerups
		
		g2.drawRoundRect(0, 0, 1365, 60, 15,15);
		g2.setColor(Color.PINK);
		g2.fillRoundRect(foodX, foodY, pw, ph, 15, 15);
		g2.setColor(Color.RED);
		g2.drawRoundRect(foodX, foodY, pw, ph, 15, 15);
		g2.setColor(Color.YELLOW);
		g2.fillRect(powerUpX, powerUpY, pw, ph);
		g2.setColor(Color.ORANGE);
		g2.drawRect(powerUpX, powerUpY, pw, ph);
		g2.setColor(Color.LIGHT_GRAY);
		//setup fonts
		g2.fillRoundRect(0, 0, 1365, 60, 15,15);
		Font font = new Font("Arial", Font.BOLD, 48);
		g2.fillRoundRect(0, 0, 1365, 60, 15,15);
		Font font2 = new Font("Arial", Font.BOLD, 30);
		g2.setFont(font); 
		//draw player pieces
		for(int i = 0; i <length; i++) {
			//rainbow when invincible
			int color = rand.nextInt(0,7);
			//System.out.println(color);
			if(invincible == true) {
				
				switch(color) {
				case 0:
					g2.setColor(Color.RED);
					break;
				case 1:	
					g2.setColor(Color.ORANGE);
					break;
				case 2:
					g2.setColor(Color.YELLOW);
					break;
				case 3:
					g2.setColor(Color.GREEN);
					break;
				case 4:
					g2.setColor(Color.BLUE);
					break;
				case 5:
					g2.setColor(Color.MAGENTA);
					break;
				case 6:
					g2.setColor(Color.CYAN);
					break;
				}
			} else {
			g2.setColor(Color.GREEN);
			}
			g2.fillRect(px[i], py[i], pw, ph);
			g2.setColor(Color.black);
			g2.drawRect(px[i], py[i], pw, ph);
			
		}
		if(death == true) {
			//death screen
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRoundRect(325, 200, 700, 300, 35,35);
			
			if(score <= Integer.parseInt(highScore)) {
				g2.setColor(Color.RED);
			
			g2.drawString("GAME OVER!", 525, 350 );
			} else {
				//highscore screen
				g2.setColor(Color.GREEN);
				g2.drawString("NEW HIGHSCORE!", 500, 350 );
				
			}
			g2.setFont(font2); 
			g2.drawString("PRESS ENTER TO TRY AGAIN", 450, 400 );
		}
		g2.setFont(font); 
		//pause timer when time is stopped
		if(timeStop != true) {
		g2.drawString("" + getTimeElapsed(), 100, 50 );
		} else {
			
			g2.drawString("" + storeTime, 100, 50 );
		}
		//score draw
		g2.drawString("Score: " +score, 400, 50 );
		g2.dispose();
		
	}
	
	
}

