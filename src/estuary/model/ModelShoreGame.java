package estuary.model;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

/**
 * Contains all of the game logic for the Shore Game
 * 
 * @author Greg Palmer
 * @version 12/6/16
 */
public class ModelShoreGame extends Game {
	private static final long serialVersionUID = 1L;
	
	public static final double SHORE_LINE_POSITION = 350.0;
	public static final double SHORETHREAT_SPAWN_HEIGHT = 40.0;
	public static final int WIDTH  = 700;
	public static final int HEIGHT = 500;
	
	public static final int GABION_COST = 100;
	public static final int minOysterSpawnTime = 3;
	
	public List<ShoreThreat> 	shorethreats;
	public List<Oyster> 		oysters;
	public List<Shore>  		shoreline;
	public List<OysterGabion> 	gabions;
	public List<Wave> 			waves;
	public List<SeaWall>		seawalls;
	public List<Barrier>		borders;
	
	public ShoreCrab shoreCrab;
	public int oysterCount;

	Random rand = new Random();

	public int oysterSpawnTime;
	public int shorethreatSpawnTime;
	public int buildingWallTime;
	public int difficulty;

	
	/**
	 * A ModelShoreGame has lists of Oysters, ShoreThreats, Shores, OysterGabions, Waves, Seawalls and Barriers.
	 * It also has a set WIDTH and HEIGHT.
	 * It has one ShoreCrab at all times.
	 * The Oystercount starts at 0 and increases as the player collects Oysters.
	 * A shoregame also has a timer to keep track of when events should occur.
	 * @param diff used to determine how often shore threats and waves appear.
	 */
	public ModelShoreGame(int diff) {
		
		super.resetPresses();
		
		int wallWidth = 10;

		shorethreats = new ArrayList<ShoreThreat>();
		oysters 	 = new ArrayList<Oyster>();
		shoreline 	 = new ArrayList<Shore>();
		gabions 	 = new ArrayList<OysterGabion>();
		waves 		 = new ArrayList<Wave>();
		seawalls 	 = new ArrayList<SeaWall>();
		borders 	 = new ArrayList<Barrier>();
		
		init();
		initTimer();

		// Borders:
		borders.add(new Barrier(new Rectangle(new Vector2(-wallWidth,     0), new Vector2(0, HEIGHT + wallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(WIDTH ,   0), new Vector2(WIDTH + wallWidth, HEIGHT + wallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(  0, HEIGHT ), new Vector2(WIDTH ,  HEIGHT + wallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(0.0,SHORE_LINE_POSITION-wallWidth),new Vector2(WIDTH ,SHORE_LINE_POSITION))));
	}
	
	/**
	 * Takes in a list of all relevant game elements, to be called for testing purposes
	 * @param shoreCrab
	 * @param waves
	 * @param shoreThreats
	 * @param oysters
	 * @param shoreLine
	 * @param seaWalls
	 * @param oysterGabions
	 * @param borders
	 * @param oysterCount
	 */
	public ModelShoreGame(ShoreCrab shoreCrab, List<Wave> waves, List<ShoreThreat> shoreThreats,
			List<Oyster> oysters, List<Shore> shoreLine, List<SeaWall> seaWalls, List<OysterGabion> oysterGabions,
			List<Barrier> borders, int oysterCount) {
		
		this.shoreCrab = shoreCrab;
		this.waves = waves;
		this.shorethreats = shoreThreats;
		this.oysters = oysters;
		this.shoreline = shoreLine;
		this.seawalls = seaWalls;
		this.gabions = oysterGabions;
		this.borders = borders;
		
		this.oysterCount = oysterCount;
		
		resetPresses();
		initTimer();
		
	}

	/**
	 * Initializes the timer
	 */
	@Override
	public void initTimer(){
		ActionListener incrementSeconds = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				secondsCounter++;
				
				oysterSpawnTime++;
				shorethreatSpawnTime++;
				if(shoreCrab.isBuildingWall){
					buildingWallTime++;
				}
				
				
				if((secondsCounter % 10 == 0) && (difficulty < 20)){
					difficulty++;
					System.out.println("Difficulty increased to " + difficulty + "!");
				}
			}

		};
		secondsTimer = new Timer(1000,incrementSeconds);
		
		secondsTimer.start();
	}

	/**
	 * onTick() handles all of the logic of the game from frame to frame and updates the game elements accordingly.
	 */
	@Override
	public boolean onTick() {
		
		isEndScreen();
		
		if(!isPaused){
			//if a crab has been building a wall for more than 3 seconds, he is done and can move again
			if((buildingWallTime > 3) && shoreCrab.isBuildingWall){
				shoreCrab.isBuildingWall = false;
				buildingWallTime = 0;	
			}

			spawnOyster();			
			spawnShoreThreat();
			
			//If the shoreCrab is not building a wall, he should be able to move.
			if(!shoreCrab.isBuildingWall){
				shoreCrab.animation.incrementFrame();
				shoreCrab.move();
			}
			
			//A shorethreat should move every tick. 
			//If it lines up with a shore, is should use RNG to decide to generate a wave or not.
			for(ShoreThreat shorethreat : shorethreats){
				shorethreat.move();
				
				//If the shore threat is lined up with the center of a shore, potentially generate a wave
				if((shorethreat.position.x + (shorethreat.hitbox.getWidth()/2) + Shore.SHORE_CENTER) % Shore.SHORE_WIDTH < 2){
					int r = rand.nextInt(5) + 1;
					if(shorethreat.power >= r){
						System.out.println("Wave incoming!");
						waves.add(shorethreat.generateWave());
					}
					
				}
			}
			
			//Move all waves
			for(Wave wave : waves){
				wave.move();
			}
			
			//Call handleCollisions
			handleCollisions();
		}
		return false;
	}

	/**
	 * Initializes the game
	 */
	@Override
	public void init() {
		
		hasWon = false;
		isPaused = false;
		isPressed = false;
		
		difficulty = 1;
		
		shoreCrab = new ShoreCrab(new Vector2(400.0,400.0));
		
		oysterCount = 0;
		
		oysterSpawnTime = 0;
		shorethreatSpawnTime = 0;
		buildingWallTime = 0;
		
		
		secondsCounter = 0;
		
		oysters.add(new Oyster(new Vector2(450.0,450.0),10));
        
		for(double i = 0.0;i < WIDTH;i = i + 100){
			shoreline.add(new Shore(new Vector2(i,SHORE_LINE_POSITION)));
		}
		shorethreats.add(new ShoreThreat(new Vector2(0.0,SHORETHREAT_SPAWN_HEIGHT)));	
	}
	
	/**
	 * Determines if the game has ended or not.
	 * If any shore entity loses all of its health, the player loses and game is over.
	 * If a gabion is placed at every shore, the player wins and the game is over.
	 */
	@Override
	public boolean isEnd() {
		
		return isPressed;
	}
	
	/**
	 * Goes through each shore entity to see if any are at 0 health
	 * @return true if any shore has 0 health, otherwise false
	 */
	public boolean isShoreDead(){
		for(Shore shore : shoreline){
			if(shore.health == 0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if the game has reached its end state,
	 * and the end screen needs to be displayed
	 */
	@Override
	public void isEndScreen() {
		
		if(isShoreDead() && !isPaused){
			System.out.println("ModelShoreGame is over! You lose!");
			secondsTimer.stop();
			isPaused = true;
		}
		
		if(shoreline.size() == gabions.size() && !isPaused){
			System.out.println("ModelShoreGame is over! You win!");
			hasWon = true;
			secondsTimer.stop();
			score = calculateScore();
			isPaused = true;
		}
		
	}

	/**
	 * handleCollsions() checks for all collisions that may have occurred and handles the logic of what happens.
	 */
	@Override
	public void handleCollisions() {
		
		//handles collisions of ShoreCrab and Oyster
		//if this collision occurs, the Oyster should be removed and the oysterCount should increase by one.
		Iterator<Oyster> oyster_iter = oysters.iterator();
		while(oyster_iter.hasNext()){
			Oyster oyster = oyster_iter.next();
			if(shoreCrab.hitbox.intersects(oyster.hitbox)){
				oysterCount = oysterCount + oyster.num_of_oysters;
				oyster_iter.remove();
				System.out.println("Oyster collected! You have "+ oysterCount + " oyster(s)!");
				
			}
		}
		
		//handles wave collisions
		//If a wave collides with a shore, the shore should lose one health and the wave should be removed.
		Iterator<Wave> wave_iter = waves.iterator();
		while(wave_iter.hasNext()){
			Wave wave = wave_iter.next();
			for(Shore shore : shoreline){
				if(wave.hitbox.intersects(shore.hitbox)){
					wave_iter.remove();
					shore.getHurt();
					System.out.println("Shore damaged! " + shore.health + " health remains!");
					break;
				}
			}
		}
		
		//If a wave collides with a gabion, it should be removed
		Iterator<Wave> wave_iter2 = waves.iterator();
		while(wave_iter2.hasNext()){
			Wave wave = wave_iter2.next();
			for(OysterGabion gabion : gabions){
				if(wave.hitbox.intersects(gabion.hitbox)){
					wave_iter2.remove();
					System.out.println("Wave stopped by Oyster Gabion!");
					break;
					
				}
			}
		}
		
		//If a wave collids with a seawall, it should be removed.
		//If a seawall has 0 health, it should be removed.
		Iterator<Wave> wave_iter3 = waves.iterator();
		while(wave_iter3.hasNext()){
			Wave wave = wave_iter3.next();
			Iterator<SeaWall> seawall_iter = seawalls.iterator();
			while(seawall_iter.hasNext()){
				SeaWall seawall = seawall_iter.next();
				if(wave.hitbox.intersects(seawall.hitbox)){
					wave_iter3.remove();
					seawall.getHurt();
					System.out.println("Seawall hit! it has " + seawall.health + " health remaining!");
					if(seawall.health == 0){
						seawall_iter.remove();
						System.out.println("Seawall was destroyed!");
					}
				}
			}
		}
		
		//handles collisions of ShoreThreat and Borders
		//If a shore threat collides with a border, the shorethreat should be removed.
		Iterator<ShoreThreat> shorethreat_iter = shorethreats.iterator();
		while(shorethreat_iter.hasNext()){
			ShoreThreat shorethreat = shorethreat_iter.next();
			for(Barrier border : borders){
				if(shorethreat.hitbox.intersects(border.hitbox)){
					shorethreat_iter.remove();
					
				}
			}
		}
		
		// Handle collisions between Crab and Borders
		//If a shoreCrab collides with a Border, it should stay in the same position
		for (Barrier wall : borders) {
			if (shoreCrab.hitbox.intersects(wall.hitbox)) {

				// "unmove" the crab (that was just moved)
				shoreCrab.setPosition(Vector2.subtract(
						shoreCrab.position, shoreCrab.velocity
				));
			}
		}
		
		
	}

	/**
	 * If it has been more than 20 seconds - difficulty since the last shorethreat was spawned, spawn a shorethreat.
	 * The shore threat is randomly chosen to start on the right or left
	 */
	public void spawnShoreThreat(){
		
		
		if(shorethreatSpawnTime >= 20 - difficulty ){
			
			Random r = new Random();
			if(r.nextInt(1000)>980){
				
				addNewShoreThreat();
			}
		}
		
		
	}
	
	/**
	 * Adds a new shore threat to the game
	 */
	public void addNewShoreThreat(){

		ShoreThreat threat;
		//Picks a power
		int power = rand.nextInt(5) + 1;
		
		//Picks a side
		boolean side = (rand.nextBoolean()?false:true);
		
		if(side){
			System.out.println("Shore threat on left with power " + power + "!");
			threat = new ShoreThreat(new Vector2(0.0,SHORETHREAT_SPAWN_HEIGHT),
								   new Vector2(ShoreThreat.SHORETHREAT_SPEED,0.0),
								   power);
		}
		else{
			System.out.println("Shore threat on right with power " + power + "!");
			threat = new ShoreThreat(new Vector2(WIDTH - ShoreThreat.SHORETHREAT_WIDTH,SHORETHREAT_SPAWN_HEIGHT),
								   new Vector2(-ShoreThreat.SHORETHREAT_SPEED,0.0),
								   power);
			
		}
		
		shorethreats.add(threat);
		shorethreatSpawnTime = 0;
	}
	
	/**
	 * If it has been more than 6 seconds since the last oyster was spawned, spawn an oyster.
	 * Its location is randomly chosen within the borders of the beach area.
	 */
	public void spawnOyster(){
		if(oysterSpawnTime >= minOysterSpawnTime ){
			Random r = new Random();
			if(r.nextInt(1000)>980){
				
				addNewOyster();
			}
		}
		
	}
	
	/**
	 * Creates an oyster
	 */
	public void addNewOyster(){
		
		//Randomly generate oyster position
		double randX = rand.nextInt(WIDTH - Oyster.WIDTH);
		double randY = rand.nextInt(HEIGHT - Oyster.HEIGHT + 1 - (int)SHORE_LINE_POSITION) + SHORE_LINE_POSITION;
		
		//Randomly generate how many oysters are contained from 10 to 30
		int num_of_oysters = (rand.nextInt(4)+ 1) * 10;
		
		System.out.println(num_of_oysters + " oysters at " + randX + ", " + randY);
		Oyster o = new Oyster(new Vector2(randX,randY),num_of_oysters);
		
		oysters.add(o);
		oysterSpawnTime = 0;
	}
	
	/**
	 * Creates an Oyster Gabion based on the location of a shore
	 * @param shore the shore the Gabion is being placed in front of
	 * @return The new oyster gabion
	 */
	public OysterGabion spawnGabion(Shore shore){
		return new OysterGabion(new Vector2(shore.position.x + Shore.SHORE_CENTER - OysterGabion.GABION_WIDTH/2,
				shore.position.y-OysterGabion.GABION_HEIGHT));
	}

	
	/**
	 * Handles the logic for when the left key is pressed
	 * 
	 * @param pressed returns true if the key is pressed
	 */
	@Override
	public void onLeftAction(boolean pressed) {
		
		
		if (pressed) {
			leftPresses++;
			  shoreCrab.velocity.x -= ShoreCrab.SPEED;
		} else if(leftPresses > 0) {
			  shoreCrab.velocity.x += ShoreCrab.SPEED;
		}
		if(!isPaused){
			shoreCrab.onMovementChanged();
		}
	}
	/**
	 * Handles the logic for when the right key is pressed
	 * 
	 * @param pressed returns true if the key is pressed
	 */
	@Override
	public void onRightAction(boolean pressed) {
		
		
		if (pressed) {
			rightPresses++;
			  shoreCrab.velocity.x += ShoreCrab.SPEED;
		} else if(rightPresses > 0) {
			  shoreCrab.velocity.x -= ShoreCrab.SPEED;
		}
		if(!isPaused){
			shoreCrab.onMovementChanged();
		}
	}

	/**
	 * Handles the logic for when the up key is pressed
	 * 
	 * @param pressed returns true if the key is pressed
	 */
	@Override
	public void onUpAction(boolean pressed) {
		
		
		if (pressed) {
			upPresses++;
			shoreCrab.velocity.y -= ShoreCrab.SPEED;
		} else if(upPresses > 0){
			shoreCrab.velocity.y += ShoreCrab.SPEED;
		}
		if(!isPaused){
			shoreCrab.onMovementChanged();
		}
	}

	/**
	 * Handles the logic for when the down key is pressed
	 * 
	 * @param pressed returns true if the key is pressed
	 */
	@Override
	public void onDownAction(boolean pressed) {
			
		if (pressed) {
			downPresses++;
			shoreCrab.velocity.y += ShoreCrab.SPEED;	
		} else if(downPresses > 0){
			shoreCrab.velocity.y -= ShoreCrab.SPEED;
		}
		if(!isPaused){
			shoreCrab.onMovementChanged();
		}
	}
	
	/**
	 * Handles the logic that occurs if the user presses the G key
	 * This key is used to create a gabion and place the gabion
	 * 
	 * @param pressed boolean that is true if the button is pressed and false if it is released
	 */
	@Override
	public void onGAction(boolean pressed) {
		
		System.out.println("G was pressed");
		//If the player has at least 5 oysters and is not already holding a gabion, set the shoreCrab in holdingGabion mode
		//Also, take away 5 oysters
		if ((oysterCount >= GABION_COST) && (!shoreCrab.isHoldingGabion) && (!shoreCrab.isBuildingWall) && (!isPaused)){
			shoreCrab.isHoldingGabion = true;
			oysterCount = oysterCount - GABION_COST;
			System.out.println("Gabion made! You have " + oysterCount + " oysters remaining!");
		}
		
		//If the shoreCrab is already holding a gabion and is on a shore, check if there is a place open for the gabion
		//If a wall is there, remove the wall and place the gabion
		//If a gabion is there, do nothing
		else if(shoreCrab.isHoldingGabion && (!isPaused)){
			
			Shore shore = findClosestShore();
			
			if(shoreCrab.hitbox.intersects(shore.hitbox)){
				boolean gabCollision  = false;
				boolean wallCollision = false;
				//Check if a gabion is already there
				for(OysterGabion gabion : gabions){
					if(gabion.position.equals(new Vector2(shore.position.x + Shore.SHORE_CENTER - OysterGabion.GABION_WIDTH/2,
							shore.position.y-OysterGabion.GABION_HEIGHT))){
						gabCollision = true;
						System.out.println("Gabion can't be placed on another Gabion!");
					}
				}
				
				//If there isn't a gabion, check if there is a seawall
				if(!gabCollision){
					Iterator<SeaWall> wall_iter = seawalls.iterator();
					//Check all walls
					while(wall_iter.hasNext()){
						SeaWall seawall = wall_iter.next();
						//If a seawall is there, remove the seawall and place the gabion
						if(seawall.position.equals(new Vector2(shore.position.x,shore.position.y-SeaWall.SEAWALL_HEIGHT))){
							System.out.println("Wall replaced by Gabion!");
							wallCollision = true;
							wall_iter.remove();
							gabions.add(spawnGabion(shore));
							shoreCrab.isHoldingGabion = false;
							System.out.println("You now have " + gabions.size() +" gabions!");
							return; // return early to prevent building two gabions
						}
					}
					//If there wasn't a wall collision, place the gabion
					if(!wallCollision){
						System.out.println("Gabion placed!");
						gabions.add(spawnGabion(shore));
						shoreCrab.isHoldingGabion = false;
						System.out.println("You now have " + gabions.size() +" gabions!");
						return; // return early to prevent building two gabions
					}
				}
				
				
			}
		
		}
		
	}
	
	/**
	 * Handles the logic that occurs if the user presses the W key
	 * This key is used to create a seawall
	 * @param pressed boolean that is true if the button is pressed and false if it is released
	 */
	@Override
	public void onWAction(boolean pressed) {
		System.out.println("W was pressed!");
		
		//If the shoreCrab is not building a wall or holding a gabion, see if the crab intersects with a shore.
		
		// Check if the shore Crab is currently holding a gabion or building a wall or if is the end screen
		if((!shoreCrab.isHoldingGabion) && (!shoreCrab.isBuildingWall) && (!isPaused)){
			
			Shore shore = findClosestShore();	
			
			//If the crab intersects with a shore, check if there is a gabion or wall there
			if(shoreCrab.hitbox.intersects(shore.hitbox)) {
				
				boolean gabCollision = false;
				boolean wallCollision = false;
				
				//Check each gabion to see if it is in the position
				for(OysterGabion gabion : gabions){
					if(gabion.position.equals(new Vector2(shore.position.x + Shore.SHORE_CENTER - OysterGabion.GABION_WIDTH/2,
							shore.position.y-OysterGabion.GABION_HEIGHT))){
						System.out.println("You can't build a wall on a gabion!");
						gabCollision = true;
					}
				}
				
				//If there isn't a gabion, check for seawalls
				if(!gabCollision){
					
					//If there are no seawalls, create the gabion
					if(seawalls.size()<1){
						System.out.println("Sea Wall created!");
						seawalls.add(new SeaWall(new Vector2(shore.position.x,shore.position.y-SeaWall.SEAWALL_HEIGHT)));
						shoreCrab.isBuildingWall = true;
						buildingWallTime = 0;
						wallCollision = true;
						return; // return early to prevent building two walls
					}
					//If there are seawalls, check if they are in the position
					else{
						for(SeaWall seawall : seawalls){
							//If there is a sea wall, set wallCollision to true
							if((seawall.position.equals(new Vector2(shore.position.x,shore.position.y-SeaWall.SEAWALL_HEIGHT)))){
								System.out.println("You can't build a wall on a wall!");
								wallCollision = true;
							}
						}
					}
					//If there isn't a sea wall in the way, make a seawall
					if(!wallCollision){
						System.out.println("Sea Wall created!");
						seawalls.add(new SeaWall(new Vector2(shore.position.x,shore.position.y-SeaWall.SEAWALL_HEIGHT)));
						shoreCrab.isBuildingWall = true;
						buildingWallTime = 0;
						return; // return early to prevent building two walls
					}
				}
				
					
			}
			
		}
		
	}


	/**
	 * Handles logic for when the mouse is pressed
	 * This is only used to exit the game when the endscreen is up
	 */
	@Override
	public void mousePressed(Vector2 mousePosition) {

		if(isShoreDead() || shoreline.size() == gabions.size()){
			
			if(!hasWon){
				restart();
			}
			else{
				isPressed = true;
			}
		}
	}

	/**
	 * Handles logic for when the mouse is released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	/**
	 * Restarts the game to its initial state
	 */
	public void restart(){
		
		// Clear all Lists
		shorethreats.clear();
		oysters.clear();
		shoreline.clear();
		gabions.clear();
		waves.clear();
		seawalls.clear();
		
		resetPresses();
		init();
		initTimer();
		
	}

	/**
	 * Determines the final score based on the health of the shore entities
	 */
	@Override
	public int calculateScore() {
		
		int scoreTemp = 0;
		for(Shore shore : shoreline){
			scoreTemp = scoreTemp + shore.health * 100;
		}
		return scoreTemp;
	}
	
	/**
	 * Finds the shore element that is closest to the shore crab
	 * @return the shore that is closest
	 */
	public Shore findClosestShore(){
		
		Shore shore = shoreline.get(1);
		double crabMiddle = shoreCrab.getPosition().x + ShoreCrab.WIDTH/2;
		
		for(Shore shore1 : shoreline){
		
			if(Math.abs(crabMiddle - (shore1.getPosition().x + Shore.SHORE_CENTER))< Math.abs(crabMiddle - (shore.getPosition().x + Shore.SHORE_CENTER))){
				shore = shore1;
			}
			
		
		}
		
		return shore;
	}


}
