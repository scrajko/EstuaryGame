package estuary.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import estuary.model.Barrier;
import estuary.model.EstuaryOrientation;
import estuary.model.KelpPlant;
import estuary.model.MazeCrab;
import estuary.model.MazeTip;
import estuary.model.MazeTutorial;
import estuary.model.ModelMazeGame;
import estuary.model.Predator;
import estuary.model.Vector2;

/**
 * This is the View that goes hand-in-hand with the ModelMazeGame Model
 * Used polymorphically inside the View class
 * 
 * @author Sean
 *
 */
public class ViewMaze extends GameView {
	
	public final static double SIGHT_WIDTH  = (int)(100 * 1.25);
	public final static double SIGHT_HEIGHT = (int)(100 * 1.25);
	static int VIEWPORT_BUFFER = 100;
	
	ModelMazeGame mazeGame;
	
	TexturePaint paint;
	
	/**
	 * Creates the view to draw the mazeGame
	 * @param mazeGame
	 * @param view
	 */
	public ViewMaze(ModelMazeGame mazeGame, View view) {
		super(view);
		
		this.mazeGame = mazeGame;
		
		BufferedImage img = ImageHandler.getImageFrame(ImageHandler.RockWall, 0);
		paint = new TexturePaint(ImageHandler.getImageFrame(ImageHandler.RockWall, 0),
				new Rectangle(0, 0, img.getWidth(), img.getHeight()));
		
		updateViewport(view.windowX, view.windowY);
	}

	/**
	 * Paints the objects of the mazeGame
	 */
	@Override
	public void onPaint(Graphics g) {
		
		//
		// draw stuff for MazeGame
		//

		// Set viewport's center
		// (Dimensions of viewport already set in updateViewport() )
		viewport.move(crabCenter());
		
		//Draw beach and MazeFloor
		if (mazeGame.estuaryOrientation == EstuaryOrientation.LEFT) {
			view.drawImage_usingViewport_NoEntity(g, viewport,
					new Vector2(-VIEWPORT_BUFFER, -VIEWPORT_BUFFER),
					new Vector2(mazeGame.estuaryPoint.x ,ModelMazeGame.Height + VIEWPORT_BUFFER),
					ImageHandler.getImageFrame(ImageHandler.SandFloor, 0));
			
			view.drawImage_usingViewport_NoEntity(g, viewport,
					new Vector2(mazeGame.estuaryPoint.x,-VIEWPORT_BUFFER),
					new Vector2(ModelMazeGame.Width + VIEWPORT_BUFFER, ModelMazeGame.Height + VIEWPORT_BUFFER),
					ImageHandler.getImageFrame(ImageHandler.MazeFloor, 0));
		} else {
			view.drawImage_usingViewport_NoEntity(g, viewport,
					new Vector2(-VIEWPORT_BUFFER, -VIEWPORT_BUFFER),
					new Vector2(ModelMazeGame.Width + VIEWPORT_BUFFER,mazeGame.estuaryPoint.y ),
					ImageHandler.getImageFrame(ImageHandler.SandFloor, 0));
			
			view.drawImage_usingViewport_NoEntity(g, viewport,
					new Vector2(-VIEWPORT_BUFFER,mazeGame.estuaryPoint.y),
					new Vector2(ModelMazeGame.Width + VIEWPORT_BUFFER, ModelMazeGame.Height + VIEWPORT_BUFFER),
					ImageHandler.getImageFrame(ImageHandler.MazeFloor, 0));
		}

		// draw MazeCrab
		drawMazeCrab(g, mazeGame.mazeCrab);
		
		// draw Maze Barriers
		for (Barrier barrier : mazeGame.walls) {
			
			drawBarrier(g, barrier);
		}
		
		// draw Kelp Plants
		for (KelpPlant kelp : mazeGame.kelpPlants) {
			view.drawImage_usingViewport(g, viewport, kelp);
		}
		
		// draw Predators
		for (Predator predator : mazeGame.predators) {
			view.drawImage_usingViewport(g, viewport, predator);
		}
		
		//draw button
		
		if(mazeGame instanceof MazeTutorial){
			if(mazeGame.endingConditionEstuary()){
				drawEndScreen(g);
			}
		}
		else{
			if(mazeGame.endingConditionEstuary() || mazeGame.mazeCrab.getHealth() == 0){
				drawEndScreen(g);
			}
		}
		
		//
		// Draw Overlay GUI
		//
		
		int windowX = view.windowX;
		int windowY = view.windowY;

		int meterX_start = (int)(9.2 * windowX / 10);
		int meterX_end   = (int)(9.5 * windowX / 10);
		int meterY_start = windowY/14;
		int meterY_end   = 3 * windowY/ 14;
		int meterOffsetX = (meterX_end - meterX_start) / 10;
		int meterOffsetY = (meterY_end - meterY_start) / 20;

		int meterY_fillLine = (int)
				( meterY_end - (meterY_end - meterY_start) *
				(mazeGame.radiusFromEstuary() /
						Math.sqrt(ModelMazeGame.Height*ModelMazeGame.Height + ModelMazeGame.Width*ModelMazeGame.Width)));
		
		
		int background_factor = 16;
		// draw background block for salinity meter
		g.setColor(new Color(0, 0, 30, 120));
		g.fillRect(meterX_start - (background_factor * meterOffsetX), meterY_start - background_factor * meterOffsetY,
				  meterX_end - meterX_start + 2 * background_factor * meterOffsetX,
				  meterY_end - meterY_start + 2 * background_factor * meterOffsetY);
		
		// Label salinity meter
		
		double t = 0.1; // interpolation t
		int salinityTextSize = windowX / 35;
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", 10, salinityTextSize));
		g.drawString("Salinity",
				(int)(((1 - t)*(meterX_start - background_factor * meterOffsetX)  + t * (meterX_end + background_factor * 2 *meterOffsetX) )),
				meterY_end + (background_factor/2) * meterOffsetY);
		
		//if on the salinity tutorial and the tip is visible, light up the salinity meter
		if(mazeGame instanceof MazeTutorial 
				&& ((MazeTutorial) mazeGame).getCurrentTip() == MazeTip.SALINITY
				&& ((MazeTutorial)mazeGame).getTipVisibility()){
			
				g.setColor(Color.YELLOW);
				g.fillRect((int)(meterX_start - 2 *meterOffsetX),(int) (meterY_start - 2* meterOffsetY),
						   meterX_end - meterX_start + 4 * meterOffsetX,
						   meterY_end - meterY_start + 4 * meterOffsetY);
			
		}
				
		//Draw Salinity Meter
		g.setColor(Color.BLACK);
		g.fillRect(meterX_start - meterOffsetX, meterY_start - meterOffsetY,
				   meterX_end - meterX_start + 2 * meterOffsetX,
				   meterY_end - meterY_start + 2 * meterOffsetY);
		g.setColor(Color.WHITE);
		g.fillRect(meterX_start, meterY_fillLine, meterX_end - meterX_start, meterY_end - meterY_fillLine);
		
		//draw health
		
		int healthBoxOffset = 5;
		int healthBoxWidth = 100;
		int healthBoxHeight = 40;
		int healthBoxX = view.windowX / 2;
		int healthBoxY = view.windowY - healthBoxHeight - healthBoxOffset;

		//if on the predator tutorial and the tip is visible, light up the health bar
		if(mazeGame instanceof MazeTutorial 
				&& (((MazeTutorial) mazeGame).getCurrentTip() == MazeTip.PREDATOR
				|| ((MazeTutorial) mazeGame).getCurrentTip() == MazeTip.KELP)
				&& ((MazeTutorial)mazeGame).getTipVisibility()){
			
				g.setColor(Color.YELLOW);
				g.fillRect(healthBoxX-5, healthBoxY-5, healthBoxWidth+10, healthBoxHeight+10);
			
		}
		g.setColor(new Color(128, 128, 128, 180));
		g.fillRect(healthBoxX, healthBoxY, healthBoxWidth, healthBoxHeight);
		
		
		for (int i = 0; i < MazeCrab.MAX_HEALTH; i++) {
			
			Vector2 topLeft = new Vector2(healthBoxX + i * 20, healthBoxY);
			Vector2 bottomRight = new Vector2(topLeft.x + 20, topLeft.y + 20);
			
			int imgIndex;
			if (i < mazeGame.mazeCrab.getHealth())
				imgIndex = ImageHandler.Heart_Full;
			else
				imgIndex = ImageHandler.Heart_Empty;
			
			view.drawImage_NoEntity(g, topLeft, bottomRight, ImageHandler.getImageFrame(imgIndex, 0));
		}
		
		//draw tips for tutorial
			if(mazeGame instanceof MazeTutorial){
				
				if(((MazeTutorial) mazeGame).getTipVisibility() && !mazeGame.hasWon){
					
					//Tip moves with mazecrab
					view.drawImage_usingViewport_NoEntity(g,viewport,
							new Vector2(mazeGame.mazeCrab.position.x - 60,mazeGame.mazeCrab.position.y),
							new Vector2(mazeGame.mazeCrab.position.x - 35,mazeGame.mazeCrab.position.y + 100),
							ImageHandler.getImageFrame(ImageHandler.Denrec, 0));
					
					switch(((MazeTutorial) mazeGame).getCurrentTip()){
						case MOVEMENT:
							view.drawImage_usingViewport_NoEntity(g,viewport,
									new Vector2(mazeGame.mazeCrab.position.x - 40,mazeGame.mazeCrab.position.y - 40),
									new Vector2(mazeGame.mazeCrab.position.x,mazeGame.mazeCrab.position.y + 5),
									ImageHandler.getImageFrame(ImageHandler.TextMazeMovement, 0));
							break;
						case SALINITY:
							view.drawImage_usingViewport_NoEntity(g,viewport,
									new Vector2(mazeGame.mazeCrab.position.x - 40,mazeGame.mazeCrab.position.y - 40),
									new Vector2(mazeGame.mazeCrab.position.x,mazeGame.mazeCrab.position.y + 5),
									ImageHandler.getImageFrame(ImageHandler.TextMazeSalinity, 0));
							break;
						case PREDATOR:
							view.drawImage_usingViewport_NoEntity(g,viewport,
									new Vector2(mazeGame.mazeCrab.position.x - 40,mazeGame.mazeCrab.position.y - 40),
									new Vector2(mazeGame.mazeCrab.position.x,mazeGame.mazeCrab.position.y + 5),
									ImageHandler.getImageFrame(ImageHandler.TextMazePredator, 0));
							break;
						case KELP:
							view.drawImage_usingViewport_NoEntity(g,viewport,
									new Vector2(mazeGame.mazeCrab.position.x - 40,mazeGame.mazeCrab.position.y - 40),
									new Vector2(mazeGame.mazeCrab.position.x,mazeGame.mazeCrab.position.y + 5),
									ImageHandler.getImageFrame(ImageHandler.TextMazeKelp, 0));
							break;
						case END:
							view.drawImage_usingViewport_NoEntity(g,viewport,
									new Vector2(mazeGame.mazeCrab.position.x - 40,mazeGame.mazeCrab.position.y - 40),
									new Vector2(mazeGame.mazeCrab.position.x,mazeGame.mazeCrab.position.y + 5),
									ImageHandler.getImageFrame(ImageHandler.TextMazeEnd, 0));
							break;
													
					
					}
			}
		}	
	}
	
	/**
	 * Draws mazeCrab
	 * @param g
	 * @param mazeCrab
	 */
	private void drawMazeCrab(Graphics g, MazeCrab mazeCrab) {
		view.drawImage_usingViewport(g, viewport, mazeGame.mazeCrab);
	}

	/**
	 * Barriers have repeated tiling, making them tricky to draw.
	 * @param g Graphics object for painting
	 * @param brr Barrier to be drawn
	 */
	private void drawBarrier(Graphics g, Barrier barr) {
		
		view.drawImage_usingViewport(g, viewport, barr);

	}


	/**
	 * Updates the viewport to stretch the screen according to the new
	 * window width and height. Also sets the center of the viewport to
	 * the center of the crab.
	 * 
	 * @param windowSize_Width
	 * @param windowSize_Height
	 */
	public void updateViewport(int windowSize_Width, int windowSize_Height) {

		viewport = new Viewport(0, 0,windowSize_Width, windowSize_Height,
	               SIGHT_WIDTH, SIGHT_HEIGHT,
	               crabCenter()); 
	}
	

	/**
	 * Finds the crab's center
	 * @return position of crab's position
	 */
	Vector2 crabCenter() {
		return new Vector2(mazeGame.mazeCrab.getPosition().x + MazeCrab.SIZE/2 ,
 		                   mazeGame.mazeCrab.getPosition().y + MazeCrab.SIZE/2);
	}
	
	/**
	 * Draws the overlay for the endscreen
	 * @param g
	 */
	public void drawEndScreen(Graphics g){
		g.setColor( new Color(255,255,255,200));
		g.fillRect(0,0,view.windowX,view.windowY);
		g.setFont(new Font("Arial", 10, 20));
		g.setColor(Color.BLACK);
		if(mazeGame.hasWon){
			g.drawString("You Win! Click to continue!", view.windowX/3, view.windowY/2);
		}
		else{
			g.drawString("You Lose! Click to restart!", view.windowX/3, view.windowY/2);
		}
	}
	
	/**
	 * Parameter is not necessary because you can access the view independently.
	 */
	@Override
	public void onResized(Dimension windowSize) {
		updateViewport((int)windowSize.getWidth(), (int)windowSize.getHeight());
	}

	/**
	 * Calls the mousePressed() to update the Maze Game's Model
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Vector2 mappedMousePosition = viewport.mapPixelToCoord(new Vector2(e.getX(), e.getY()));
		mazeGame.mousePressed(mappedMousePosition);
	}

}