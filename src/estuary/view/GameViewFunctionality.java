package estuary.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Methods used to assist with handling view drawing and dynamic window sizing.
 * 
 * @author Sean Rajkowski
 *
 */
public interface GameViewFunctionality {

	public void onPaint(Graphics g);
	
	/**
	 * Handles the logic for when the window is resized
	 */
	public void onResized(Dimension windowSize);
	
	/**
	 * Handles for the logic for when the mouse is pressed or simulated
	 */
	public void mousePressed(MouseEvent e);
	
	/**
	 * Dynamically updates the viewport to correspond to the new window size
	 */
	public void updateViewport(int windowX, int windowY);
}
