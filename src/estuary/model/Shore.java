package estuary.model;

/**
 * A shore is an entity that loses health when a wave collides with it.
 * If any shore loses all of its health, the player loses.
 * @author Greg Palmer
 *
 */
public class Shore extends Entity {
	private static final long serialVersionUID = 1L;
	
	public static final int    SHORE_HEALTH = 5;
	public static final double SHORE_WIDTH 	= 100;
	public static final double SHORE_HEIGHT = 50;
	public static final double SHORE_CENTER = SHORE_WIDTH/2;
	
	public int health;
	
	/**
	 * A shore has a position and health. All shore's have a preset health
	 * 
	 * @param position the top left corner of the shore's hitbox
	 */
	Shore(Vector2 position) {
		super(position, new Rectangle(position,SHORE_WIDTH,SHORE_HEIGHT), new Animation());
		this.health = SHORE_HEALTH;
	}
	
	/**
	 * getHurt() is called when a wave collides with a shore.
	 * The shore loses one health
	 */
	public void getHurt(){
		this.health = this.health - 1;
	}
}
