package estuary.model;

/**
 * Entity is super class for all objects within the game.
 * Every entity has a position along with getters and setters for their position.
 * Every entity also has a hitbox to handle collisions.
 * Every entity has an animation
 * 
 * @author Sean Rajkowski
 *
 */
public class Entity implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public Vector2   position;
	public Rectangle hitbox;
	public Animation animation;
	
	/**
	 * Constructor that creates a new entity with a position, hitbox and animation.
	 * @param position position is the top left corner of the entity's hitbox
	 * @param hitbox A rectangle that makes up the area in which the entity can collide with another entity
	 * @param animation The current animation that the view will use to display the entity
	 */
	public Entity(Vector2 position, Rectangle hitbox, Animation animation) {
		this.position  = position;
		this.hitbox    = hitbox;
		this.animation = animation;
	}
	
	/**
	 * Constructor that creates a new entity with a hitbox and animation
	 * @param hitbox A rectangle that makes up the area in which the entity can collide with another entity
	 * @param animation The current animation that the view will use to display the entity
	 */
	public Entity(Rectangle hitbox, Animation animation) {
		this.position  = new Vector2(hitbox.topLeft);
		this.hitbox    = hitbox;
		this.animation = animation;
	}
	
	/**
	 * Gets the position of the entity
	 * @return position, which is the top left corner of the entity's hitbox
	 */
	public Vector2 getPosition() {
		return this.position;
	}
	
	/**
	 * Sets the position of the entity
	 * @param position The top left corner of the entity's hitbox
	 */
	public void setPosition(Vector2 position) {
		this.position = new Vector2(position);
		double width  = this.hitbox.getWidth();
		double height = this.hitbox.getHeight();
		this.hitbox.topLeft = new Vector2(position);
		this.hitbox.bottomRight = new Vector2(position.x + width,
				                              position.y + height);
	}
	
	/**
	 * Sets the entity's position to a new location
	 * @param x x-coordinate of new position
	 * @param y y-coordinate of new position
	 */
	public void setPosition(double x, double y) {
		setPosition(new Vector2(x, y));
	}
}
