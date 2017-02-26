package estuary.model;

import estuary.view.ImageHandler;

/**
 * Rocks get in the way of horseshoe crabs and make it harder to find horseshoe crabs
 * @author Matt
 *
 */
public class Rock extends Entity {
	private static final long serialVersionUID = 1L;
	
	public static final double ROCK_WIDTH = 60.0;
	public static final double ROCK_LENGTH = 40.0;
	
	/**
	 * A rock has a location
	 * @param position the top left corner of the rock's hotbox
	 */
	public Rock(Vector2 position) {
		super(position,
				new Rectangle(position, ROCK_WIDTH, ROCK_LENGTH),
				new Animation(ImageHandler.Rock1));
	}

}