package estuary.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import estuary.model.CountingTutorial;
import estuary.model.HorseshoeCrab;
import estuary.model.ModelCountingGame;
import estuary.model.Rock;
import estuary.model.Vector2;

/**
 * Handles the drawing and coordinate mapping for the Counting Game
 * @author Sean Rajkowski
 *
 */
public class ViewCounting extends GameView {
	
	static final int LIGHT_RADIUS               = 255;
	static final int ModelCountingGame_DARKNESS = 251;
	
	ModelCountingGame countingGame;
	
	/**
	 * Constructor that sets the countingGame and sets the view reference
	 * @param game
	 * @param view
	 */
	public ViewCounting(ModelCountingGame game, View view) {
		super(view);
		
		countingGame = game;

		updateViewport(view.windowX, view.windowY);
	}

	/**
	 * Paints all objects in the Counting Game
	 */
	@Override
	public void onPaint(Graphics g) {

		// Draw beach/shore
		view.drawImage_usingViewport_NoEntity(g, viewport,
				new Vector2(0, 0), new Vector2(ModelCountingGame.Width,
						                       ModelCountingGame.Height),
				ImageHandler.getImageFrame(ImageHandler.SandFloor, 0));
	
		//Draw horseshoe crabs
		for (HorseshoeCrab hcrab : countingGame.horseshoeCrabs) {
			drawHorseshoeCrab(g, hcrab);
		}
		
		//Draw rocks
		for (Rock rock : countingGame.rocks) {
			//draw(g, rock, Color.GRAY);
			view.drawImage_usingViewport(g, viewport,  rock);
		}

		//
		// Draw Light 
		//
		g.setColor(new Color(ImageHandler.CountingGame_Darkness_Red, ImageHandler.CountingGame_Darkness_Green,
				ImageHandler.CountingGame_Darkness_Blue, ModelCountingGame_DARKNESS));

		Vector2 mapped_border_vec = viewport.mapCoordToPixel(
				new Vector2(ModelCountingGame.Width, ModelCountingGame.Height));
		int borderx = view.windowX;
		int bordery = view.windowY;

		Point light = view.light;
		
		int lightRadius = LIGHT_RADIUS;
		
		g.fillRect(
				0,  0, light.x - lightRadius, bordery);
		g.fillRect(
				light.x + lightRadius, 0, borderx - (light.x + lightRadius), bordery);
		g.fillRect(
				light.x - lightRadius, 0, 2 * lightRadius, light.y - lightRadius);
		g.fillRect(
				light.x - lightRadius, light.y + lightRadius, 2 * lightRadius, bordery - (light.y + lightRadius));
		

		g.drawImage(ImageHandler.getImageFrame(0, 0),
				   light.x - lightRadius, light.y - lightRadius,
				   light.x + lightRadius, light.y + lightRadius,
				   0, 0, 256, 256, view);

		
		/////////////////////////////
		/// Draw Overlay Elements ///
		/////////////////////////////

		int windowX = view.windowX;
		int windowY = view.windowY;
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, (int)mapped_border_vec.y, windowX, 10000);
		g.fillRect((int)mapped_border_vec.x, 0, 10000, windowY);
		
		int meterX_start = (int)((3 * mapped_border_vec.x +     windowX) / 4);
		int meterX_end   = (int)((    mapped_border_vec.x + 3 * windowX) / 4);
		int meterY_start = windowY/10;
		int meterY_end   = 9 * windowY/ 10;
		int meterOffsetX = (meterX_end - meterX_start) / 10;
		int meterOffsetY = (meterY_end - meterY_start) / 100;
		
		int meterY_fillLine = (int)
				( meterY_end - (meterY_end - meterY_start) *
				((double)countingGame.secondsCounter / countingGame.secondsCounterMax));
		
		//Draw tips
		if(countingGame instanceof CountingTutorial && ((CountingTutorial)countingGame).getTipVisibility() && !countingGame.hasWon){
			view.drawImage_NoEntity(g,
					new Vector2(view.windowX/40,view.windowY/3),
					new Vector2(view.windowX/8,view.windowY/1.4),
					ImageHandler.getImageFrame(ImageHandler.Denrec, 0));
			switch(((CountingTutorial)countingGame).getCurrentTip()){
			case COUNT:
				view.drawImage_NoEntity(g,
						new Vector2(view.windowX/10, view.windowY/20),
						new Vector2(view.windowX/2, view.windowY/2.5),
						ImageHandler.getImageFrame(ImageHandler.TextCountingCount, 0));
				break;
			case GLOW:
				view.drawImage_NoEntity(g,
						new Vector2(view.windowX/10, view.windowY/20),
						new Vector2(view.windowX/2, view.windowY/2.5),
						ImageHandler.getImageFrame(ImageHandler.TextCountingGlow, 0));
				break;
			case BATTERY:
				view.drawImage_NoEntity(g,
						new Vector2(view.windowX/10, view.windowY/20),
						new Vector2(view.windowX/2, view.windowY/2.5),
						ImageHandler.getImageFrame(ImageHandler.TextCountingBattery, 0));
						g.setColor(Color.YELLOW);
						g.fillRect(meterX_start - 2*meterOffsetX, meterY_start - 2*meterOffsetY,
								   meterX_end - meterX_start + 4 * meterOffsetX,
								   meterY_end - meterY_start + 4 * meterOffsetY);
				break;
			}
		
		}

		//Draw Battery Meter
		g.setColor(Color.BLACK);
		g.fillRect(meterX_start - meterOffsetX, meterY_start - meterOffsetY,
				   meterX_end - meterX_start + 2 * meterOffsetX,
				   meterY_end - meterY_start + 2 * meterOffsetY);
		g.setColor(Color.GREEN);
		g.fillRect(meterX_start, meterY_fillLine, meterX_end - meterX_start, meterY_end - meterY_fillLine);
		
		//Draw battery icon
		view.drawImage_NoEntity(g, 
				new Vector2(meterX_start, meterY_start - windowY/20 - windowY/100),
				new Vector2(meterX_end, meterY_start - windowY/100), 
				ImageHandler.getImageFrame(ImageHandler.Battery, 0));
		
		//Set text position
		int textX = borderx;
		int textY = bordery - 20; 

		// Draw Score
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 10, 30));
		g.drawString("Score: " + countingGame.score, (int)(textX/1.5), textY);
		
		// Draw Horseshoe Crab count
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 10, 30));
		g.drawString("Horseshoe Crabs found: " + countingGame.horseshoeCrabCount, textX/10, textY);

		//Draw Endscreen
		if(countingGame instanceof CountingTutorial){
			if(countingGame.horseshoeCrabs.size() == countingGame.horseshoeCrabCount){
				drawEndScreen(g);
			}
		}
		else{
			if(countingGame.horseshoeCrabs.size() == countingGame.horseshoeCrabCount || countingGame.secondsCounter <= 0){
				drawEndScreen(g);
			}
		}
	}
	
	/**
	 * Draws the overlay for the end screen
	 * @param g Graphics Element
	 */
	public void drawEndScreen(Graphics g){
		g.setColor( new Color(255,255,255,200));
		g.fillRect(0,0,view.windowX,view.windowY);
		g.setFont(new Font("Arial", 10, 20));
		g.setColor(Color.BLACK);
		if(countingGame.hasWon){
			g.drawString("You Win! Click to continue!", view.windowX/3, view.windowY/2);
		}
		else{
			g.drawString("You Lose! Click to restart!", view.windowX/3, view.windowY/2);
		}
	}
	
	/**
	 * Draws a horseshoecrab and its highlight if found
	 * @param g
	 * @param hCrab Crab to be drawn
	 */
	public void drawHorseshoeCrab(Graphics g, HorseshoeCrab hCrab) {
		
		// Draw Horseshoe Crab Highlight
		if (hCrab.isFound) {

			if (hCrab.animation.imageIndex == ImageHandler.HorseshoeCrab_East) {
				
				view.drawImage_usingViewport_NoEntity(g, viewport,
				    hCrab.hitbox.topLeft, hCrab.hitbox.bottomRight,
				    ImageHandler.getImageFrame(ImageHandler.HorseshoeCrab_East_Found, 0));

			} else { // West
				view.drawImage_usingViewport_NoEntity(g, viewport,
					hCrab.hitbox.topLeft, hCrab.hitbox.bottomRight,
				    ImageHandler.getImageFrame(ImageHandler.HorseshoeCrab_West_Found, 0));

			}
		}
		
		// Draw Horseshoe Crab body
		view.drawImage_usingViewport(g, viewport, hCrab); // original Color(102, 51, 0)
	}

	/**
	 * Updates the viewport to match the new window size
	 */
	@Override
	public void onResized(Dimension windowSize) {
		
		updateViewport( (int)windowSize.getWidth(), (int)windowSize.getHeight() );
	}

	/**
	 * Converts mouse's screen coordinate to a game coordinate and calls the models mousePressed logic
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Vector2 mappedMousePosition = viewport.mapPixelToCoord(new Vector2(e.getX(), e.getY()));
		countingGame.mousePressed(mappedMousePosition);
	}

	/**
	 * Updates the viewport to match the current window size
	 */
	@Override
	public void updateViewport(int windowX, int windowY) {
		viewport = new Viewport(0, 0, windowX, windowY,
				ModelCountingGame.WIDTH_FULL, ModelCountingGame.HEIGHT_FULL, 
				new Vector2(ModelCountingGame.WIDTH_FULL / 2, ModelCountingGame.HEIGHT_FULL / 2) );
		
	}

}