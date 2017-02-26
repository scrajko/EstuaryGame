package estuary.model;

import java.awt.event.MouseEvent;

/**
 * Game type for the initial splash screen
 * @author Greg Palmer
 *
 */
public class MainMenu extends Game {
	private static final long serialVersionUID = 1L;
	
	public int Width;
	public int Height;
	public Vector2 mousePosition;
	
	/**
	 * MainMenu has a button for the user to press to start the game
	 */
	public MainMenu(){
		
		isPressed = false;
		Width  = 700;
		Height = 500;
		
		nextGame = GAME_ENUM.MAZE;
	}
	
	/**
	 * Handles the logic of everything that changes between each tick
	 */
	@Override
	public boolean onTick() {
		return false;
	}

	/**
	 * Initializes MainMenu
	 */
	@Override
	public void init() {}

	/**
	 * The Menu is over if the button gets pressed
	 */
	@Override
	public boolean isEnd() {
		
		return isPressed;
	}

	/**
	 * Handles the logic for when a collision occurs
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
	 * Handles the logic for when the mouse is pressed
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

	/**
	 * The mainmenu will never have an end screen
	 */
	@Override
	public void isEndScreen() {}

	/**
	 * Restarts the main menu
	 */
	@Override
	public void restart() {}

	/**
	 * Updates the users overall score
	 */
	@Override
	public int calculateScore() {
		return 0;
	}

	@Override
	public void initTimer() {}

}
