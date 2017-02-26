package estuary.model;

import estuary.view.ImageHandler;

/**
 * A KelpPlant is an object for the maze game.
 * When the user collides with a kelp plant, the kelp is removed from the game and the crab's health increases.
 * 
 * @author Sean Rajkowski
 *
 */
public class KelpPlant extends Entity {
	private static final long serialVersionUID = 1L;

	/**
	 * A kelp has a position, hitbox and animation
	 * @param position the top left corner of the hitbox
	 */
	public KelpPlant(Vector2 position) {
		super(position,
			    new Rectangle(position, 10.0, 10.0),
				new Animation(ImageHandler.Kelp)
		);

	}

}
