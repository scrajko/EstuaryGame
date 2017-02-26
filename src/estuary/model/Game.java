package estuary.model;

import javax.swing.Timer;

import estuary.controller.Tickable;

/**
 * Game is an abstract class that implements Tickable and GameFunctionality
 * All games extend Game to make sure that they contain all necessary methods
 * 
 * @author Sean Rajkowski
 * @author Gregory Palmer
 *
 */
public abstract class Game implements Tickable, GameFunctionality, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public boolean isPaused;
	public boolean isPressed;
	public boolean hasWon;
	public int score;
	public GAME_ENUM nextGame;
	public int secondsCounter;
	public Timer secondsTimer;
	
	
	public int leftPresses;
	public int rightPresses;
	public int upPresses;
	public int downPresses;
	
	/**
	 * Resets the button press counters to prevent buggy movement after
	 * during game transitions and saving/loading
	 */
	public void resetPresses() {
		leftPresses  = 0;
		rightPresses = 0;
		upPresses    = 0;
		downPresses  = 0;
	}
}

