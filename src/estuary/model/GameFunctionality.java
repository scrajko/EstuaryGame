package estuary.model;

import java.awt.event.MouseEvent;

/**
 * GameFunctionality is an interface that contains all methods that are necessary for each game
 * This includes all button presses and clicks.
 * This also contains game methods such as init, isEnd and handleCollisions
 * 
 * @author Sean Rajkowski
 *
 */
public interface GameFunctionality {

	/**
	 * Initializes the game
	 */
	void init();
	
	/**
	 * Initializes the timer
	 */
	void initTimer();
	
	/**
	 * Determines if the game has ended based on the game state
	 * @return a boolean of whether the game has ended or not
	 */
	boolean isEnd();
	
	/**
	 * Determine if the end screen should appear based on the game state
	 */
	void isEndScreen();
	
	/**
	 * Restarts the game to its initial state
	 */
	void restart();
	
	/**
	 * Calculates the score at the end of each game
	 */
	int calculateScore();
	
	/**
	 * Goes through each entity and handles all collisions that occur in the current game state
	 */
	void handleCollisions();
	
	/**
	 * Handles the logic for when the left key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onLeftAction(boolean pressed);	
	
	/**
	 * Handles the logic for when the right key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onRightAction(boolean pressed);

	/**
	 * Handles the logic for when the up key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onUpAction(boolean pressed);
	
	/**
	 * Handles the logic for when the down key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onDownAction(boolean pressed);
	
	/**
	 * Handles the logic for when the G key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onGAction(boolean pressed);
	
	/**
	 * Handles the logic for when the W key is pressed
	 * @param pressed boolean of whether the key is pressed or not
	 */
	void onWAction(boolean pressed);

	/**
	 * Handles the logic for when mouse is pressed
	 * @param mousePosition position the mouse is at, in world coords
	 */
	public void mousePressed(Vector2 mousePosition);
	
	/**
	 * Handles the logic for when mouse is released
	 * @param e all info from mouse release
	 */
	public void mouseReleased(MouseEvent e);

}
