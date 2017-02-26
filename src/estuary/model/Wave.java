package estuary.model;

import estuary.view.ImageHandler;

/**
 * A wave is generated by a shore threat.
 * When a wave collides with anything, it is removed.
 * When a wave collides with a shore or seawall, is damages that entity.
 * @author Greg Palmer
 *
 */
public class Wave extends MovingEntity {
	private static final long serialVersionUID = 1L;
	
	public static final double WAVE_SPEED = 2.0;
	public static final double WAVE_HEIGHT = 10;
	public static final double WAVE_WIDTH = 80;
	
	/**
	 * Creates a wave with a velocity of WAVE_SPEED
	 * 
	 * @param position the top left corner of the entity's hitbox
	 */
	Wave(Vector2 position) {
		super(position, new Vector2(0.0, WAVE_SPEED), 
				new Rectangle(position, WAVE_WIDTH, WAVE_HEIGHT),
				new Animation(ImageHandler.Wave));
	}

}
