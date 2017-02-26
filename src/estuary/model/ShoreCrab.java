package estuary.model;

import estuary.view.ImageHandler;

/**
 * A shoreCrab is the entity that the player controls. 
 * The user moves the shoreCrab around to collect oysters, build walls, and make/place gabions
 * 
 * @author Greg Palmer
 *
 */
public class ShoreCrab extends MovingEntity {
	private static final long serialVersionUID = 1L;

	public static double SPEED = 5.0; 
	
	public static double WIDTH  = (int)(30 * 2);
	public static double HEIGHT = (int)(20 * 2);
	
	public boolean isHoldingGabion;
	public boolean isBuildingWall;
	
	/**
	 * Constructor creates a shore crab. The shore crab by default is not building a wall or holding a gabion
	 * @param position position the top left corner of the entity's hitbox
	 * @param velocity vector that determines how far the crab should move when .move() is called
	 */
	ShoreCrab(Vector2 position, Vector2 velocity) {
		super(position, velocity, 
				new Rectangle(position,WIDTH, HEIGHT),
				new Animation());

		isHoldingGabion = false;
		isBuildingWall  = false;
		this.animation.setImage(ImageHandler.MazeCrab_Stationary, true);
	}
	
	/**
	 * default constructor sets velocity to 0
	 * @param position the top left corner of the entity's hitbox
	 */
	ShoreCrab(Vector2 position) {
		this(position, new Vector2(0.0, 0.0));
	}
	
	/**
	 * Changes the shore crabs animation based on whether he is stationary or moving
	 */
	public void onMovementChanged() {
		if (this.velocity.magnitudeSquared() == 0 || isBuildingWall) { // Not Moving
			this.animation.setImage(ImageHandler.MazeCrab_Stationary, true);
		} else {
			this.animation.setImage(ImageHandler.MazeCrab_Moving, false);
		}
	}

	
}
