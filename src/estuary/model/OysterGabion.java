package estuary.model;

import estuary.view.ImageHandler;

/**
 * 
 * An Oyster Gabion is created by the user once 5 oysters are collected.
 * The user can place a gabion in front of a shore.
 * If a gabion is placed in front of every shore, the player wins.
 * 
 * @author Greg Palmer
 *
 */
public class OysterGabion extends Entity{
	private static final long serialVersionUID = 1L;
	
	public static final double GABION_HEIGHT = 50;
	public static final double GABION_WIDTH = 70;
	
	/**
	 * A gabion has a position and cannot move
	 * @param position The top left corner of the gabion's hitbox
	 */
	OysterGabion(Vector2 position) {
		super(position, new Rectangle(position,GABION_WIDTH,GABION_HEIGHT), new Animation(ImageHandler.Gabion));
	}

}
