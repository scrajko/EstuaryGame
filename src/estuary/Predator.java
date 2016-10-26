package estuary;

public class Predator extends MovingEntity {

	public static final int ATTACK_POWER = 1;
	
	public static final double LENGTH = 1.0;
	public static final double HEIGHT = 1.0;
	
	Predator(Vector2 position, Vector2 velocity) {
		super(position, velocity,
			  new Rectangle(position, LENGTH, HEIGHT),
			  new Animation()
		);
	}
}
