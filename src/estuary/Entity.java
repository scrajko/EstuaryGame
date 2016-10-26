package estuary;

public abstract class Entity {

	Vector2   position;
	Rectangle hitbox;
	Animation animation;
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	Entity(Vector2 position, Rectangle hitbox, Animation animation) {
		this.position  = position;
		this.hitbox    = hitbox;
		this.animation = animation;
	}
}
