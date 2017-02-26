package estuary.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import estuary.model.Animation;
import estuary.model.Entity;
import estuary.model.ModelShoreGame;
import estuary.model.Oyster;
import estuary.model.OysterGabion;
import estuary.model.Rectangle;
import estuary.model.SeaWall;
import estuary.model.Shore;
import estuary.model.ShoreCrab;
import estuary.model.ShoreThreat;
import estuary.model.ShoreTutorial;
import estuary.model.Vector2;
import estuary.model.Wave;

/**
 * View used to draw shore game
 * @author Sean Rajkowski
 *
 */
public class ViewShore extends GameView {

	ModelShoreGame shoreGame;
	Entity shoreWater;
	Animation hammerAnimation;

	/**
	 * Creates the view to be used to draw the shore game
	 * @param game
	 * @param view
	 */
	public ViewShore(ModelShoreGame game, View view) {
		super(view);

		this.shoreGame = game;
		
		updateViewport(view.windowX, view.windowY);
		
		shoreWater = new Entity( new Rectangle(
		    new Vector2(0,  -200), new Vector2(ModelShoreGame.WIDTH, (int)(ModelShoreGame.SHORE_LINE_POSITION))),
			new Animation(ImageHandler.ShoreWater));
		
		hammerAnimation = new Animation(ImageHandler.Hammer);
	}

	/**
	 * Paints all the different elements of the game
	 */
	@Override
	public void onPaint(Graphics g) {

		// draw Beach
		view.drawImage_usingViewport_NoEntity(g, viewport,
				new Vector2(0, ModelShoreGame.SHORE_LINE_POSITION),
				new Vector2(ModelShoreGame.WIDTH, ModelShoreGame.HEIGHT),
				ImageHandler.getImageFrame(ImageHandler.SandFloor, 0));
		
		// draw Sky
		g.setColor(new Color(135, 206, 250));
		view.fillRect_usingViewport(g, viewport,
				0, 0, ModelShoreGame.WIDTH, (int)(ModelShoreGame.SHORETHREAT_SPAWN_HEIGHT + 10));
		
		//draw shorWater
		view.drawImage_usingViewport(g, viewport, shoreWater);
		
		// draw shore segments
		for (Shore shore : shoreGame.shoreline) {
			
			int imgIndex;
			
			switch(shore.health) {
			
			case 0: //Shore has 0 health
				imgIndex = ImageHandler.Crack1;
				break;		
			case 1: //Shore has 1 health
				imgIndex = ImageHandler.Crack1;
				break;	
			case 2: //Shore has 2 health
				imgIndex = ImageHandler.Crack2;
				break;		
			case 3: //Shore has 3 health
				imgIndex = ImageHandler.Crack3;
				break;		
			case 4: //Shore has 4 health
				imgIndex = ImageHandler.Crack4;
				break;	
			case 5: //Shore has 5 health
				imgIndex = ImageHandler.Crack5;
				break;
			default:
				imgIndex = ImageHandler.Crack1;
				break;
			}
			
			view.drawImage_usingViewport_NoEntity(g, viewport,
					shore.hitbox.topLeft, shore.hitbox.bottomRight,
					 ImageHandler.getImageFrame(imgIndex, 0));
			
		}
		
		// draw Oysters
		for (Oyster oyster : shoreGame.oysters) {
			view.drawImage_usingViewport_NoEntity(g, viewport,
					oyster.hitbox.topLeft, oyster.hitbox.bottomRight,
					ImageHandler.getImageFrame(ImageHandler.Oyster,0));
		}

		// draw shoreThreats
		for (ShoreThreat shoreThreat : shoreGame.shorethreats) {	
			view.drawImage_usingViewport(g, viewport, shoreThreat);
		}
		
		// draw waves
		for (Wave wave : shoreGame.waves) {
			
			view.drawImage_usingViewport_NoEntity(g, viewport,
					wave.hitbox.topLeft, wave.hitbox.bottomRight,
					ImageHandler.getImageFrame(ImageHandler.Wave, 0));
		}
		
		//draw seawalls
		for (SeaWall seawall : shoreGame.seawalls) {
			view.drawImage_usingViewport_NoEntity(g, viewport,
				seawall.hitbox.topLeft, seawall.hitbox.bottomRight,
				ImageHandler.getImageFrame(ImageHandler.SeaWall, 0));
		}
		
		// draw OysterGabions
		for (OysterGabion gabion : shoreGame.gabions) {
			view.drawImage_usingViewport_NoEntity(g, viewport,
				gabion.hitbox.topLeft, gabion.hitbox.bottomRight,
				ImageHandler.getImageFrame(ImageHandler.Gabion, 0));
		}
		
		//update Hammer Animation
		if(!shoreGame.isPaused){
			hammerAnimation.incrementFrame();
		}
		
		// draw shoreCrab
		if (shoreGame.shoreCrab.isBuildingWall) {
			// Hammer isn't an Entity,
			// but it has to keep a state of the Hammer animation
			view.drawImage_usingViewport_NoEntity(g, viewport,
					new Vector2(shoreGame.shoreCrab.getPosition().x - ShoreCrab.WIDTH/3,shoreGame.shoreCrab.getPosition().y - ShoreCrab.HEIGHT/1.5),
					new Vector2(shoreGame.shoreCrab.getPosition().x + ShoreCrab.WIDTH/2,shoreGame.shoreCrab.getPosition().y + ShoreCrab.HEIGHT/3),
					ImageHandler.getImageFrame(hammerAnimation.imageIndex, hammerAnimation.frameIndex));
		}
			
		if(shoreGame.shoreCrab.isHoldingGabion) {
			
			view.drawImage_usingViewport_NoEntity(g, viewport,
				new Vector2(shoreGame.shoreCrab.hitbox.topLeft.x,
				shoreGame.shoreCrab.hitbox.topLeft.y - ShoreCrab.HEIGHT),
				new Vector2(shoreGame.shoreCrab.hitbox.bottomRight.x - 5,
						shoreGame.shoreCrab.hitbox.topLeft.y + 10),
				ImageHandler.getImageFrame(ImageHandler.Gabion, 0));
		}
		
		//draw ShoreCrab
		view.drawImage_usingViewport(g, viewport, shoreGame.shoreCrab);
		
		// draw tip guy
		if(shoreGame instanceof ShoreTutorial && ((ShoreTutorial)shoreGame).getTipVisibility()){
			view.drawImage_NoEntity(g,
					new Vector2(view.windowX/40,view.windowY/3),
					new Vector2(view.windowX/8,view.windowY/1.4),
					ImageHandler.getImageFrame(ImageHandler.Denrec, 0));
			switch(((ShoreTutorial)shoreGame).getCurrentTip()){
				case MOVEMENT:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreMovement, 0));
					break;
				case SEAWALL:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreSeawall, 0));
					break;
				case SHORETHREAT:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreShorethreat, 0));
					break;
				case OYSTERS:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreOysters, 0));
					break;
				case GABION:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreGabion, 0));
					break;
				case END:
					view.drawImage_NoEntity(g,
							new Vector2(view.windowX/10, view.windowY/20),
							new Vector2(view.windowX/2, view.windowY/2.5),
							ImageHandler.getImageFrame(ImageHandler.TextShoreEnd, 0));
					break;
			
			}
		}
		
		//draw oyster count
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 10, 30));
		g.drawString("Oysters: " + shoreGame.oysterCount, 25, view.windowY - 20);
		
		//Draw Endscreen
		if(shoreGame instanceof ShoreTutorial){
			if(shoreGame.hasWon){
				drawEndScreen(g);
			}
		}
		else{
			if(shoreGame.isShoreDead() || shoreGame.shoreline.size() == shoreGame.gabions.size()){
				drawEndScreen(g);
			}
		}
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
		if(shoreGame.hasWon){
			g.drawString("You Win! Click to continue!", view.windowX/3, view.windowY/2);
		}
		else{
			g.drawString("You Lose! Click to restart!", view.windowX/3, view.windowY/2);
		}
	}
	
	/**
	 * Handles for the logic for when the mouse is pressed
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Vector2 mappedMousePosition = viewport.mapPixelToCoord(new Vector2(e.getX(), e.getY()));
		shoreGame.mousePressed(mappedMousePosition);
	}
	
	/**
	 * Handles the logic for when the window is resized
	 */
	@Override
	public void onResized(Dimension windowSize) {
		
		updateViewport((int)windowSize.getWidth(), (int)windowSize.getHeight());
	}

	/**
	 * Dynamically updates the viewport to correspond to the new window size
	 */
	@Override
	public void updateViewport(int windowX, int windowY) {
		
		viewport = new Viewport(0, 0, windowX, windowY,
	               ModelShoreGame.WIDTH, ModelShoreGame.HEIGHT,
	               new Vector2(ModelShoreGame.WIDTH/2, ModelShoreGame.HEIGHT/2)); 
		
	}

}
