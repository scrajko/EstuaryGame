package estuary.model;

import estuary.view.ImageHandler;

/**
 * Horseshoe Crabs are moving entities that the user must click on to count them.
 * The user wins if they find every horseshoe crab before the time runs out.
 * 
 * @author Matt Blaschak
 *
 */
public class HorseshoeCrab extends MovingEntity {
	private static final long serialVersionUID = 1L;
	
	public static final double HORSESHOECRAB_WIDTH  = 60;
	public static final double HORSESHOECRAB_HEIGHT = 40;
	
	int secondsCounter;
	public boolean isMoving;
	public boolean isFound;

	/**
	 * Horseshoe crabs have positions and velocities to determine their location and whether or not they are moving
	 * Their secondsCounter is initially 3 so that they begin moving when the game starts
	 * They initially have isMoving and isFound set to false.
	 * 
	 * @param position position is the top left corner of the entity's hitbox
	 * @param velocity vector that determines whether the entity should move or not
	 */
	public HorseshoeCrab(Vector2 position, Vector2 velocity) {
		super(position, velocity,
				new Rectangle(position, HORSESHOECRAB_WIDTH, HORSESHOECRAB_HEIGHT),
				new Animation(ImageHandler.HorseshoeCrab_East));
		secondsCounter = 3;
		isMoving = velocity.magnitudeSquared() > 0;
		isFound  = false;
	}
	
	/**
	 * Constructor that places a horseshoeCrab at a given position
	 * @param position the position the crab should be placed at
	 */
	public HorseshoeCrab(Vector2 position) {
		this(position, new Vector2(0.0, 0.0));
	}

	/**
	 * Updates the image of the horseshoecrab based on whether it is facing left or right
	 */
	public void update() {
		
		if (velocity.x > 0) {
			animation.setImage(ImageHandler.HorseshoeCrab_East, false);
		} else if (velocity.x < 0) {
			animation.setImage(ImageHandler.HorseshoeCrab_West, false);	
		}
	}

}

