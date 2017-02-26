package estuary.controller;

/**
 * Interface for all elements that need to handle tick logic
 * @author Sean
 *
 */
public interface Tickable {

	/**
	 * Handles the frame-to-frame changes of the game
	 */
	public boolean onTick();
}
