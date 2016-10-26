package estuary;

public class MazeCrab extends MovingEntity {
	
	public static final int MAX_HEALTH = 4;
	
	private int health;
	

	MazeCrab(Vector2 position, Vector2 velocity) {
		super(position, velocity,
			  new Rectangle(position, 10.0, 10.0),
			  new Animation()
		);
		this.health = MAX_HEALTH;
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Accessors
	 * @return
	 */
	int getHealth() {
		return this.health;
	}
	
	public void setHealth(int health) {
		this.health = health;
		
	}
	
	
	void eatKelp() {
		
	}
	
	void getHurt(Predator predator) {
		
	}


}
