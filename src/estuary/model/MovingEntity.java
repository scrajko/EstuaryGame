package estuary.model;

/**
 * Abstract class that contains all methods that movable entities need
 * @author Sean
 *
 */
public abstract class MovingEntity extends Entity implements Movable {
	private static final long serialVersionUID = 1L;
	
	public Vector2 velocity;
	
	/**
	 * Constructor that creates a movable entity
	 * @param position Top left corner of the hitbox
	 * @param velocity Rate at which the position should change each tick
	 * @param hitbox The rectangle in which collisions can occur
	 * @param animation Animation the view uses to display the entity
	 */
	MovingEntity(Vector2 position, Vector2 velocity, Rectangle hitbox, Animation animation) {
		super(new Vector2(position), hitbox, animation); // this fixed the memory pointing problem
		this.velocity = new Vector2(velocity);
	}
	
	/**
	 * Function used to change the entity's position based on its velocity
	 * The velocity is added to the position each time this is called
	 */
	@Override
	public void move() {	
		
		this.position.add(velocity);
		this.hitbox.topLeft.add(velocity);
		this.hitbox.bottomRight.add(velocity);
		
	}
}
