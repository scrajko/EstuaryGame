package estuary;

import java.awt.event.MouseEvent;

import javax.swing.Timer;

/**
 * The Model contains all the data and logic for each subgame
 * of our Estuary game.
 *
 * @author Sean
 * @version 1.0 Oct 25, 2016
 */
public class Model {
	
	public int x;
	public int y;
	
	
	int    secondsCounter;
	Timer  secondsTimer;

	/**
	 * Initializes the data for all three subgames
	 */
	Model () {
		x = 0;
		y = 0;
	
		secondsTimer = new Timer(1000, new MazeGameSecondsTicker(this));
		secondsTimer.start();
	}	
	
	
	
	/**
	 * Updating from events
	 * Each function has an equivalent in the View
	 */
	
	/**
	 * Is called every frame of the game to update
	 * the current state of the game
	 */
	void onTick() {
		System.out.println(secondsCounter);
	}
	
	/**
	 * Changes the game state based on where the user clicked
	 * 
	 * @param e The mouse event, which contains the position clicked
	 */
	void onMousePress(MouseEvent e) {
		
	}
	
	/**
	 * Changes the game state based on where the user released
	 * 
	 * @param e The mouse event, which contains the position released
	 */
	void onMouseRelease(MouseEvent e) {

	}
	
}

