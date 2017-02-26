package estuary.model;

import java.awt.event.MouseEvent;

/**
 * Game type for displaying the user's final score
 * @author Greg Palmer
 *
 */
public class FinalScreen extends Game {
	private static final long serialVersionUID = 1L;
	
	/**
	 * FinalScreen only has a button that the user can click to go back to the Main Menu
	 */
	public FinalScreen(){
		isPressed = false;
	}
	
	/**
	 * Handles the logic for everything that changes each tick
	 */
	@Override
	public boolean onTick() {
		return false;
		
	}

	/**
	 * Initializes the game state
	 */
	@Override
	public void init() {}

	/**
	 * Returns whether the button has been pressed indicating the FinalScreen is over
	 */
	@Override
	public boolean isEnd() {
		return isPressed;
	}

	/**
	 * The final screen does not need an EndScreen
	 */
	@Override
	public void isEndScreen() {}

	/**
	 * Restarts the FinalScreen
	 */
	@Override
	public void restart() {}
	
	/**
	 * Calculates the score
	 */
	@Override
	public int calculateScore() {
		return 0;
	}

	/**
	 * Handles the logic for when entities collide
	 */
	@Override
	public void handleCollisions() {}

	/**
	 * Handles the logic for when the left button is pressed
	 */
	@Override
	public void onLeftAction(boolean pressed) {}

	/**
	 * Handles the logic for when the right button is pressed
	 */
	@Override
	public void onRightAction(boolean pressed) {}

	/**
	 * Handles the logic for when the up button is pressed
	 */
	@Override
	public void onUpAction(boolean pressed) {}

	/**
	 * Handles the logic for when the down button is pressed
	 */
	@Override
	public void onDownAction(boolean pressed) {}

	/**
	 * Handles the logic for when the G button is pressed
	 */
	@Override
	public void onGAction(boolean pressed) {}

	/**
	 * Handles the logic for when the W button is pressed
	 */
	@Override
	public void onWAction(boolean pressed) {}


	/**
	 * Handles the logic for when the mouse pressed
	 */
	@Override
	public void mousePressed(Vector2 mousePosition) {

		isPressed = true;
		
	}

	/**
	 * Handles the logic for when the mouse is released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void initTimer() {}



}
