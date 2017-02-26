package estuary.model;

import estuary.view.ImageHandler;

/**
 * The MazeCrab is the entity that the user controls.
 * The user moves the MazeCrab using the arrow keys.
 * When the MazeCrab reaches the Estuary Point, the player wins.
 * 
 * @author Sean Rajkowski
 *
 */
public class MazeCrab extends MovingEntity {
	private static final long serialVersionUID = 1L;
	
	public static final int MAX_HEALTH = 5;
	public static final int INVINCIBILITY_FRAMES = 60;
	public static final double SPEED   = 5;
	
	public static final double SIZE = 20.0;
	
	private int health;
	
	public int invincibilityCooldown;
	
	/**
	 * Creates MazeCrab with a given position and velocity
	 * The MazeCrab's hitbox is created
	 * Its health is set to MAX_HEALTH.
	 * Its invicibilityCooldown is set at 0
	 * @param position The top left corner of the entity's hitbox
	 * @param velocity The rate at which the Crab's position changes
	 */
	public MazeCrab(Vector2 position, Vector2 velocity) {
		super(position, velocity,
			  new Rectangle(position, SIZE, SIZE),
			  new Animation(ImageHandler.MazeCrab_Stationary)
		);
		this.health = MAX_HEALTH;
		this.invincibilityCooldown = 0;
	}
	
	/**
	 * Constructor that calls the main constructor, but with an input velocity of 0.0
	 * @param position The top left corner of the entity's hitbox
	 */
	public MazeCrab(Vector2 position) {
		this(position, new Vector2(0.0, 0.0));
	}
	
	/**
	 * Update the animation of the maze crab
	 */
	public void update() {
		animation.incrementFrame();
		
		onMovementChanged();
	}

	/**
	 * Updates the animation based on whether the crab is moving or stationary and if the crab is
	 * hurt or not hurt
	 */
	public void onMovementChanged() {
		
		// Offset based on whether or not the Maze Crab is suffereing from being hurt
		int indexOffset = (this.invincibilityCooldown > 0) ? ImageHandler.HurtOffset : 0;
		
		if (velocity.magnitudeSquared() == 0) {
			animation.setImage(ImageHandler.MazeCrab_Stationary + indexOffset, true);
		} else {
			animation.setImage(ImageHandler.MazeCrab_Moving     + indexOffset, false);
		}
	}
	
	/**
	 * Accessors
	 * @return Crab's health
	 */
	public int getHealth() {
		return this.health;
	}
	
	/**
	 * Sets the crabs health
	 * @param health
	 */
	public void setHealth(int health) {
		this.health = health;	
	}	
	
	/**
	 * This method adds one health to the crab's health total
	 * This is called when the crab collides with a KelpPlant
	 */
	void eatKelp() {
		
		if (this.health < MAX_HEALTH) {
			this.health++;
		}
		System.out.println("Yummy! (health = "+this.health+")");
	}
	
	/**
	 * This method subtracts the crabs health by the predator's strength if the crab's invincibilityCooldown isn't 0
	 * This method is called when a crab collides with a Predator
	 * @param predator the Predator the crab is currently colliding with
	 */
	void getHurt(Predator predator) {
		
		if (invincibilityCooldown == 0) {
	
			this.health -= predator.getAttackStrength();
			invincibilityCooldown = INVINCIBILITY_FRAMES;
			System.out.println("Ouch! (health = "+this.health+")");
		}

	}

}
