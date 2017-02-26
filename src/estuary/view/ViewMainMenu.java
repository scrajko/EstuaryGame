package estuary.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import estuary.model.MainMenu;
import estuary.model.Vector2;

/**
 * Draws the objects for the main menu
 * @author Greg Palmer
 *
 */
public class ViewMainMenu extends GameView {

	MainMenu menu;
	
	/**
	 * Creates the view for the main menu
	 * @param game
	 * @param view
	 */
	public ViewMainMenu(MainMenu game, View view) {
		super(view);
		
		this.menu = game;
	}

	/**
	 * Paints all the objects for the main menu
	 */
	@Override
	public void onPaint(Graphics g) {

		view.drawImage_NoEntity(g, new Vector2(0, 0), new Vector2(view.windowX, view.windowY),
				ImageHandler.getImageFrame(ImageHandler.SplashScreen,
				ImageHandler.DEFAULT_FRAME));
		
		
		
	}

	/**
	 * Calls the menu's mousePressed logic
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		
		Vector2 mappedMousePosition = new Vector2(e.getX(), e.getY());
		menu.mousePressed(mappedMousePosition);	
	}

	@Override
	public void onResized(Dimension windowSize) {}
	@Override
	public void updateViewport(int windowX, int windowY) {}

}
