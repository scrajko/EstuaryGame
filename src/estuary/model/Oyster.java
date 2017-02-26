package estuary.model;

import estuary.view.ImageHandler;

/**
 * An oyster is an entity that the user can collect to create Oyster Gabions
 * @author Greg Palmer
 *
 */
public class Oyster extends Entity {
	private static final long serialVersionUID = 1L;

	int num_of_oysters;
	
	public static int WIDTH  = 30;
	public static int HEIGHT = 20;

	/**
	 * An oyster only has a position and hitbox and can not move
	 * @param position the top left of the hitbox
	 */
	Oyster(Vector2 position , int num_of_oysters) {
		super(position, new Rectangle(position,WIDTH,HEIGHT), new Animation(ImageHandler.Oyster));
		this.num_of_oysters = num_of_oysters;
	}

}
