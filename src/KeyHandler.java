import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	public boolean upPressed, downPressed, leftPressed, rightPressed, restart;
	public String lastKeyPressed;
	//GamePanel gp = new GamePanel();
	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	// checking key presses
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//if(gp.death == true) {
			if(code == KeyEvent.VK_ENTER){
				restart = true;
			}
		//}
		
		if(code == KeyEvent.VK_W){
			if(lastKeyPressed != "DOWN" ) {
			upPressed = true;
			rightPressed = false;
			leftPressed = false;
			downPressed = false;
			lastKeyPressed = "UP";
	      //  System.out.println("UP1");
			}
	    }
		if(code == KeyEvent.VK_A){
			if(lastKeyPressed != "RIGHT" ) {
			leftPressed = true;
			upPressed = false;
			rightPressed = false;
			downPressed = false;
			lastKeyPressed = "LEFT";
	        //System.out.println("LEFT1");
			}
	    }
		if(code == KeyEvent.VK_S){
			if(lastKeyPressed != "UP" ) {
			downPressed = true;
			upPressed = false;
			rightPressed = false;
			leftPressed = false;
			lastKeyPressed = "DOWN";
	       // System.out.println("DOWN");
			}
	        
	    }
		if(code == KeyEvent.VK_D){
			if(lastKeyPressed != "LEFT" ) {
			rightPressed = true;
			
			upPressed = false;
			leftPressed = false;
			downPressed = false;
			
			lastKeyPressed = "RIGHT";
			}
	       // System.out.println("RIGHT");
	       
	    }
		
	}

	@Override
	//checking key releases
	public void keyReleased(KeyEvent e) {
int code = e.getKeyCode();
		
//if(gp.death == true) {
	if(code == KeyEvent.VK_ENTER){
		restart = false;
	}
//}
		if(code == KeyEvent.VK_W){
	        
			//upPressed = false;
	        if(lastKeyPressed != "DOWN" ) {
	        	
	        	 lastKeyPressed = "UP";
	        	
	        }
	    }
		if(code == KeyEvent.VK_A){
			//leftPressed = false;
	        if(lastKeyPressed != "RIGHT" ) {
	        	 lastKeyPressed = "LEFT";
	        }
	        
	    }
		if(code == KeyEvent.VK_S){
			
			//downPressed = false;
	        if(lastKeyPressed != "UP" ) {
	        	 lastKeyPressed = "DOWN";
	        }
	        
	    }
		if(code == KeyEvent.VK_D){
			//rightPressed = false;
	        if(lastKeyPressed != "LEFT" ) {
	        	 lastKeyPressed = "RIGHT";
	        }
	       
	        
	    }
	}
	
}
