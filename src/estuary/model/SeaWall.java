package estuary.model;

import estuary.view.ImageHandler;

/**
 * A seawall is made by the shoreCrab and prevents waves from hitting the shore.
 * A seawall can only stop 2 waves before being removed
 * 
 * @author Greg
 *
 */
public class SeaWall extends Entity {
	private static final long serialVersionUID = 1L;
	
	public static final int SEAWALL_HEALTH = 2; 
	public static final double SEAWALL_HEIGHT = 20;
	public static final double SEAWALL_WIDTH = 100;
	
	int health;
	
	/**
	 * Creates a seawall of size SEAWALL_WIDTH by SEAWALL_HEIGHT
	 * The Seawall's health is set to SEAWALL_HEALTH
	 * 
	 * @param position the top left corner of the entity's hitbox
	 */
	SeaWall(Vector2 position) {
		super(position, new Rectangle(position,SEAWALL_WIDTH,SEAWALL_HEIGHT), new Animation(ImageHandler.SeaWall));
		this.health = SEAWALL_HEALTH;
	}

	/**
	 * This method decreases the seawall's health by one
	 */
	public void getHurt(){
		this.health = this.health - 1;
	}
}
