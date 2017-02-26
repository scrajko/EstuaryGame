package estuary.model;

import java.util.Random;

import estuary.view.ImageHandler;

/**
 * 
 * A shorethreat moves across the top of the screen and generates waves when lined up with shore entities
 * Whether a wave is generated or not is based on the threat's power
 * 
 * @author Greg Palmer
 *
 */
public class ShoreThreat extends MovingEntity {
	private static final long serialVersionUID = 1L;
	
	public static final double SHORETHREAT_SPEED  = 2.0;
	public static final double SHORETHREAT_HEIGHT = 40;
	public static final double SHORETHREAT_WIDTH  = 80;
	public static final int    SHORETHREAT_POWER  = 3;
	
	public static final double STORM_WIDTH = 60;
	public static final double STORM_HEIGHT = 60;
	
	public int power;
	
	static Random r = new Random();
	
		
	/**
	 * Constructor creates a shore threat. The shore threat has a power that determines whether it makes a wave or not.
	 * The velocity is set to SHORETHREAT_SPEED
	 * @param position the top left corner of the entity's hitbox
	 */
	ShoreThreat(Vector2 position) {
		
		super(position, 
				new Vector2(SHORETHREAT_SPEED,0.0), 
				new Rectangle(position,SHORETHREAT_WIDTH,SHORETHREAT_HEIGHT),
				new Animation(ImageHandler.Boat_Right));
		power = SHORETHREAT_POWER;
	}
	
	/**
	 * Constructor creates a shore threat. The shore threat has a power that determines whether it makes a wave or not.
	 * The velocity is set to SHORETHREAT_SPEED
	 * @param position the top left corner of the entity's hitbox
	 * @param power determines how often the shorethreat genereates waves
	 */
	ShoreThreat(Vector2 position, Vector2 velocity, int power) {
		
		super(position, velocity, 
				new Rectangle(position, SHORETHREAT_WIDTH, SHORETHREAT_HEIGHT),
				new Animation());
		this.power = power;
		
		if(power > 4){
			this.animation.setImage(ImageHandler.Storm, false);
			this.setPosition(new Vector2(position.x,position.y - 40));
			this.hitbox.bottomRight.y = hitbox.bottomRight.y + 30;
		}
		else if (power > 2) {
			if(velocity.x > 0){
				this.animation.setImage(ImageHandler.Boat_Right, false);
			}
			else{
				this.animation.setImage(ImageHandler.Boat_Left,false);
			}
			
		} else {
			this.setPosition(new Vector2(position.x,position.y - 30));
			this.hitbox.bottomRight.y = hitbox.bottomRight.y + 30;
			if(velocity.x > 0){
				this.animation.setImage(ImageHandler.Sailboat_Right, false);
			}
			else{
				this.animation.setImage(ImageHandler.Sailboat_Left,false);
			}
		}
	}
	
	
	public ShoreThreat(Vector2 position, int power) {
		
		this(position,new Vector2(SHORETHREAT_SPEED,0),power);
	}

	/**
	 * This method creates a new wave centered with the shore threat
	 * @return A new wave centered with the shore threat
	 */
	public Wave generateWave(){
		
		return new Wave(new Vector2(position.x - (Wave.WAVE_WIDTH/2 - hitbox.getWidth()/2),hitbox.getBottom()));
		
	}
	

}
