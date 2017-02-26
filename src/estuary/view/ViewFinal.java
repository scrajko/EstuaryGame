package estuary.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import estuary.model.FinalScreen;
import estuary.model.Vector2;

/**
 * Draws the objects of the final screen
 * @author Greg Palmer
 *
 */
public class ViewFinal extends GameView {
	
	FinalScreen finalScreen;

	/**
	 * Creates the view for drawing the final screen
	 * @param game
	 * @param view
	 */
	public ViewFinal(FinalScreen game, View view) {
		super(view);
		
		this.finalScreen = game;
	}

	/**
	 * Paints all objects for the final screen
	 */
	@Override
	public void onPaint(Graphics g) {
		//Draws EndScreen Splash Image
		view.drawImage_NoEntity(g, new Vector2(0, 0), new Vector2(view.windowX, view.windowY),
				ImageHandler.getImageFrame(ImageHandler.EndScreen,
				ImageHandler.DEFAULT_FRAME));
		
		//Draw overall score
		g.setFont(new Font("Arial", 10, view.windowY/15));
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(view.model.overallScore), (int)(view.windowX/2.25), (int)(view.windowY/2.1));
	}

	@Override
	public void onResized(Dimension windowSize) {}

	/**
	 * Calls logic for mousePressed in finalScreens
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Vector2 mappedMousePosition = new Vector2(e.getX(), e.getY());
		finalScreen.mousePressed(mappedMousePosition);
	}
	

	@Override
	public void updateViewport(int windowX, int windowY) {}

}
