package estuary;

public abstract class MovingEntity extends Entity {

	public Vector2 velocity;
	
	MovingEntity(Vector2 position, Vector2 velocity, Rectangle hitbox, Animation animation) {
		super(position, hitbox, animation);
		this.velocity = velocity;
	}
}
