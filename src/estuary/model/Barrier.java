package estuary.model;

import estuary.view.ImageHandler;

/**
 * A barrier is an entity that prevents other entities from moving through a location
 * 
 * @author Sean Rajkowski
 *
 */
public class Barrier extends Entity {
	private static final long serialVersionUID = 1L;

	/**
	 * A barrier is just a hitbox
	 * @param box the size of the rectangle
	 */
	public Barrier(Rectangle box) {
		super(box.topLeft, box,
				new Animation(ImageHandler.RockWall));
	}
}
