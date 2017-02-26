package estuary.model;

import estuary.view.ImageHandler;

/**
 * A predator is an entity that will cause the mazeCrab to lose health when a collision occurs between them
 * 
 * @author Sean
 *
 */
public class Predator extends MovingEntity {
	private static final long serialVersionUID = 1L;

	public static final int ATTACK_POWER = 1;
	
	public static final double LENGTH = 40.0;
	public static final double HEIGHT = 20.0;
	
	public static final double VERT_HEIGHT = 20.0;
	public static final double VERT_WIDTH = 10.0;
	
	/**
	 * A predator has a position and velocity
	 * It's attack power is initially set to 1
	 * @param position Top left corner of the predator's hitbox
	 * @param velocity The amount the position changes each tick
	 */
	public Predator(Vector2 position, Vector2 velocity) {
		super(position, velocity,
			  new Rectangle(position, LENGTH, HEIGHT),
			  new Animation()
		);
		if(velocity.y != 0){
			this.hitbox = new Rectangle(position,VERT_WIDTH,VERT_HEIGHT);
		}
		update();
	}
	
	/**
	 * Updates the image of the predator in order to make sure it is facing the correct direction
	 */
	public void update() {
		if (velocity.x > 0) {
			this.animation.setImage(ImageHandler.Predator_Right, false);
			
		} else if(velocity.x < 0) {
			this.animation.setImage(ImageHandler.Predator_Left, false);
			
		} else if(velocity.y > 0){
			this.animation.setImage(ImageHandler.Shark_Down, false);
			
		} else{
			this.animation.setImage(ImageHandler.Shark_Up, false);
			
		}
	}

	/**
	 * Getter that returns the attack power of the predator
	 * @return attack power of the predator
	 */
	public int getAttackStrength() {
		
		return ATTACK_POWER;
	}

}
