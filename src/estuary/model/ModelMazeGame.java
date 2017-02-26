package estuary.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;

/**
 * The player completes the game by moving the mazeCrab to the estuary point.
 * The player must navigate past predators and barriers to find their way to the end
 * 
 * @author Sean Rajkowski
 *
 */
public class ModelMazeGame extends Game {
	private static final long serialVersionUID = 1L;
	
	public final static int  Width  = 700;
	public final static int  Height = 500;
	
	public MazeCrab        mazeCrab;

	public int 			   WallWidth;
	public List<KelpPlant> kelpPlants;
	public List<Barrier>   walls;
	public List<Predator>  predators;
	
	public Vector2         estuaryPoint;
	public EstuaryOrientation     estuaryOrientation;

	/**
	 * Constructor that initializes the Maze Game's elements.
	 * A list of barriers, kelp and predators are all made for the user to navigate through
	 * The Maze crab begins far away from the estuary point
	 * 
	 */
	public ModelMazeGame() {
		
		resetPresses();
		
		hasWon = false;
		WallWidth = 10;
		
		walls      = new ArrayList<Barrier>();
		kelpPlants = new ArrayList<KelpPlant>();
		predators  = new ArrayList<Predator>(); 
		
		secondsCounter = 0;
		

		
		// Set up all elements:
		isPressed = false;
		
		init();
		initTimer();
		
	}
	
	/**
	 * Places the estuary point and sets up the estuary orientation
	 */
	public void generateEstuaryPoint() {
		estuaryPoint = new Vector2(0, 120);
		estuaryOrientation = EstuaryOrientation.LEFT;
	}
	
	/**
	 * Creates all the barriers in the game
	 */
	public void generateBarriers() {
		// Borders:
		walls.add(new Barrier(new Rectangle(new Vector2(0,     0), new Vector2(WallWidth, 60))));
		walls.add(new Barrier(new Rectangle(new Vector2(0,     140), new Vector2(WallWidth, Height - WallWidth))));
		walls.add(new Barrier(new Rectangle(new Vector2(Width - WallWidth,   0), new Vector2(Width, Height))));
		walls.add(new Barrier(new Rectangle(new Vector2(  0, Height - WallWidth), new Vector2(Width - WallWidth,  Height))));
		walls.add(new Barrier(new Rectangle(new Vector2(0, 0),new Vector2(Width - WallWidth, WallWidth))));
		
		//Inners
		walls.add(new Barrier(new Rectangle(new Vector2(460,260),new Vector2(470,430))));
		walls.add(new Barrier(new Rectangle(new Vector2(470,330),new Vector2(520,340))));
		walls.add(new Barrier(new Rectangle(new Vector2(520,330),new Vector2(530,430))));
		walls.add(new Barrier(new Rectangle(new Vector2(520,430),new Vector2(600,440))));
		walls.add(new Barrier(new Rectangle(new Vector2(390,430),new Vector2(470,440))));
		walls.add(new Barrier(new Rectangle(new Vector2(600,330),new Vector2(610,440))));
		walls.add(new Barrier(new Rectangle(new Vector2(390,370),new Vector2(400,440))));
		walls.add(new Barrier(new Rectangle(new Vector2(70, 370),new Vector2(390,380))));
		walls.add(new Barrier(new Rectangle(new Vector2(290,440),new Vector2(300,Height))));
		walls.add(new Barrier(new Rectangle(new Vector2(210,440),new Vector2(300,450))));//10
		walls.add(new Barrier(new Rectangle(new Vector2(110,380),new Vector2(120,440))));
		walls.add(new Barrier(new Rectangle(new Vector2(70, 440),new Vector2(120,450))));
		walls.add(new Barrier(new Rectangle(new Vector2(220,330),new Vector2(230,370))));
		walls.add(new Barrier(new Rectangle(new Vector2(90, 260),new Vector2(320,270))));
		walls.add(new Barrier(new Rectangle(new Vector2(260,190),new Vector2(270,260))));
		walls.add(new Barrier(new Rectangle(new Vector2(WallWidth,180),new Vector2(270,190))));
		walls.add(new Barrier(new Rectangle(new Vector2( 70, 50),new Vector2(80,180))));
		walls.add(new Barrier(new Rectangle(new Vector2( 80,120),new Vector2(350,130))));
		walls.add(new Barrier(new Rectangle(new Vector2(350,120),new Vector2(360,210))));
		walls.add(new Barrier(new Rectangle(new Vector2(350,210),new Vector2(380,220))));//20
		walls.add(new Barrier(new Rectangle(new Vector2(170, 40),new Vector2(180,120))));
		walls.add(new Barrier(new Rectangle(new Vector2(310,WallWidth),new Vector2(320,40))));
		walls.add(new Barrier(new Rectangle(new Vector2(460,250),new Vector2(Width-WallWidth,260))));
		walls.add(new Barrier(new Rectangle(new Vector2(180, 70),new Vector2(400,80))));
		walls.add(new Barrier(new Rectangle(new Vector2(400, 50),new Vector2(410,80))));
		walls.add(new Barrier(new Rectangle(new Vector2(410, 50),new Vector2(470,60))));
		walls.add(new Barrier(new Rectangle(new Vector2(470, 50),new Vector2(480,160))));
		walls.add(new Barrier(new Rectangle(new Vector2(530,170),new Vector2(540,250))));
		walls.add(new Barrier(new Rectangle(new Vector2(570,140),new Vector2(Width-WallWidth,150))));
		walls.add(new Barrier(new Rectangle(new Vector2(540, 80),new Vector2(Width - WallWidth,90))));//30
		walls.add(new Barrier(new Rectangle(new Vector2(540, 30),new Vector2(550,80))));
	}
	
	/**
	 * Sets up all the predators in tha maze
	 */
	public void generatePredators() {
		//Predators
		predators.add(new Predator(new Vector2(220.0, 400.0), new Vector2(0.0, 2.0)));
		predators.add(new Predator(new Vector2(650.0, 340.0), new Vector2(3.0, 0.0)));
		predators.add(new Predator(new Vector2(190.0, 300.0), new Vector2(6.0, 0.0)));
		predators.add(new Predator(new Vector2(490.0, 180.0), new Vector2(3.0, 0.0)));
		predators.add(new Predator(new Vector2(190.0, 160.0), new Vector2(0.0, 2.0)));
		predators.add(new Predator(new Vector2(220.0,  50.0), new Vector2(5.0, 0.0)));
	}
	
	/**
	 * Sets up all the kelp in the maze
	 */
	public void setupKelp() {
		//Kelp
		kelpPlants.add(new KelpPlant(new Vector2( 90.0, 405.0)));
		kelpPlants.add(new KelpPlant(new Vector2(100.0, 150.0)));
		kelpPlants.add(new KelpPlant(new Vector2(660.0, 105.0)));
		kelpPlants.add(new KelpPlant(new Vector2(340.0,  20.0)));	
	}
	
	/**
	 * Sets up the maze crab in its starting location
	 */
	public void setupMazeCrab() {
		this.mazeCrab = new MazeCrab(new Vector2(490.0, 350.0));
	}
	
	/**
	 * Creates a custom game
	 * @param difficulty determines how hard the maze is
	 * @param kelpPlants list of all KelpPlants in the game
	 * @param walls list of all barriers in the maze
	 * @param predators list of all predators in the maze
	 * @param estuaryPoint location of the point the user must reach to win the game
	 */
	public ModelMazeGame(int difficulty, MazeCrab mazeCrab, List<KelpPlant> kelpPlants,
			List<Barrier> walls, List<Predator> predators,
			Vector2 estuaryPoint, EstuaryOrientation estuaryOrientation) {
		
		hasWon = false;
		
		
		this.mazeCrab     = mazeCrab;
		this.kelpPlants   = kelpPlants;
		this.walls        = walls;
		this.predators    = predators;
		this.estuaryPoint = estuaryPoint;
		this.estuaryOrientation = estuaryOrientation;

		initTimer();
	}
	
	/**
	 * Allocates the timer and sets the period
	 */
	public void initTimer() {
		
		
		ActionListener incrementSeconds = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e){
				secondsCounter++;
				System.out.println(secondsCounter);
			}

		};
		secondsTimer = new Timer(1000, incrementSeconds);
		secondsTimer.start();
	}
	
	/**
	 * Handles all of the logic that occurs from frame-to-frame
	 * Handles moving objects and collisions that may occur
	 */
	@Override
	public boolean onTick() {
		
		
		if(!isPaused){

			// 1. Handle individual cooldowns
			if (mazeCrab.invincibilityCooldown > 0)
				mazeCrab.invincibilityCooldown--;
			
			
			// 2. Move all Movable Entities
			for (Predator predator : predators)
				predator.move();
			
			mazeCrab.move();
			
			
			// 3. Check for collisions with all Appropriate Entity interactions	
			handleCollisions();
			
			
			// 4. update frame data
            mazeCrab.update();
            
            isEndScreen();
		}
		
		return false;
	}
	
	/**
	 * Initializes the game
	 */
	@Override
	public void init() {
		generateEstuaryPoint();
		setupMazeCrab();
		generateBarriers();
		generatePredators();
		setupKelp();
	}

	/**
	 * The game ends if the user won and clicks to continue
	 */
	@Override
	public boolean isEnd() {

		return isPressed && hasWon;
	}
	
	/**
	 * Calculates the score based on the cabs score and how long it took the player to finish
	 */
	@Override
	public int calculateScore() {
		return Math.max((2000 - secondsCounter) + (mazeCrab.getHealth() * 100),0);
	}
	
	/**
	 * Returns if the winning ending condition has been met
	 * @return whether the crab has reached the estuary point or not
	 */
	public boolean endingConditionEstuary() {
		
		switch (estuaryOrientation) {
		case TOP:
			return mazeCrab.getPosition().y < estuaryPoint.y;
		case LEFT:
			return mazeCrab.getPosition().x < estuaryPoint.x;
		
		}
		return false;	
	}
	
	/**
	 * The the end screen appears if the user reaches the Estuary point or if the crab runs out of health
	 */
	@Override
	public void isEndScreen() {
		boolean condition1 = endingConditionEstuary();
		boolean condition2 = mazeCrab.getHealth() <= 0;
		
		if (condition1 && !isPaused) {
			secondsTimer.stop();
			hasWon = true;
			System.out.println("Maze Game has ended! You win!");
			score = calculateScore();
			isPaused = true;
		}
		else if (condition2 && !isPaused) {
			secondsTimer.stop();
			System.out.println("Maze Game has ended! You lose!");
			isPaused = true;
		}
		
	}

	/**
	 * Handles all of the logic for every possible collision in the game
	 */
	@Override
	public void handleCollisions() {

		//
		// Handle collisions between Crab and Walls
		//
		for (Barrier wall : walls) {
			if (mazeCrab.hitbox.intersects(wall.hitbox)) {

				// "unmove" the crab (that was just moved)
				mazeCrab.setPosition(Vector2.subtract(
						mazeCrab.position, mazeCrab.velocity
				));
			}
		}
		
		//
		// Handle collisions between Predators and Walls
		//
		for (Predator predator : predators) {
			for (Barrier wall : walls) {
				if (predator.hitbox.intersects(wall.hitbox)) {
					
					// Flip the predator to move in opposite direction
					predator.velocity.x = -predator.velocity.x;
					predator.velocity.y = -predator.velocity.y;
					predator.update();
				}
			}
		}
			
		//
		// Handle collisions between Crab and Predators
		//
		for (Predator predator : predators) {
			if (mazeCrab.hitbox.intersects(predator.hitbox)) {
				mazeCrab.getHurt(predator);
			}
		}
		
		//
		// Handle collisions between Crab and Kelp
		//
		Iterator<KelpPlant> kelp_iter = kelpPlants.iterator();
		while (kelp_iter.hasNext()) {
			KelpPlant kelp = kelp_iter.next();
			
			if (mazeCrab.hitbox.intersects(kelp.hitbox)) {
				mazeCrab.eatKelp();
				kelp_iter.remove();
				System.out.println("Kelp removed");
			}			
		}

		
	}
	
	/**
	 * Determines how far away the crab is from the estuary point
	 * @return double signifying distance
	 */
	public double radiusFromEstuary() {
		return mazeCrab.getPosition().distanceTo(estuaryPoint);
	}
	
	/**
	 * removes a kelp from the list of kelpplants
	 * @param kelp kelp to be removed
	 */
	void removeKelpPlant(KelpPlant kelp) {
		kelpPlants.remove(kelp);
	}
	
	/**
	 * Creates a predator at a given position with a given velocity
	 * @param position Top left corner of the predator's hitbox
	 * @param velocity The change in position the predator should have each frame
	 */
	void spawnPredator(Vector2 position, Vector2 velocity) {
		Predator predator = new Predator(position, velocity);
		predators.add(predator);
	}
	
	/**
	 * Creates a Kelp at a given position with a given velocity
	 * @param position Top left corner of the kelp's hitbox
	 * */
	void spawnKelp(Vector2 position) {
		KelpPlant kelp = new KelpPlant(position);
		kelpPlants.add(kelp);
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
			  mazeCrab.velocity.x -= MazeCrab.SPEED;
		} else if(leftPresses > 0) {
			  mazeCrab.velocity.x += MazeCrab.SPEED;
		}
		if(!isPaused){
			mazeCrab.onMovementChanged();
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
			  mazeCrab.velocity.x += MazeCrab.SPEED;
		} else if(rightPresses > 0) {
			  mazeCrab.velocity.x -= MazeCrab.SPEED;
		}
		if(!isPaused){
			mazeCrab.onMovementChanged();
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
			mazeCrab.velocity.y -= MazeCrab.SPEED;
		} else if(upPresses > 0){
			mazeCrab.velocity.y += MazeCrab.SPEED;
		}
		if(!isPaused){
			mazeCrab.onMovementChanged();
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
			mazeCrab.velocity.y += MazeCrab.SPEED;	
		} else if(downPresses > 0){
			mazeCrab.velocity.y -= MazeCrab.SPEED;
		}
		if(!isPaused){
			mazeCrab.onMovementChanged();
		}
	}


	/**
	 * Handles the logic for when the mouse pressed
	 */
	@Override
	public void mousePressed(Vector2 mousePosition) {

		if(endingConditionEstuary() || mazeCrab.getHealth() <= 0){
			if(hasWon){
				isPressed = true;
			}
			else{
				restart();
			}
		}
		
	}

	/**
	 * Handles the logic for when the mouse is released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		return;
	}

	/**
	 * Handles the logic for when the G key is pressed
	 */
	@Override
	public void onGAction(boolean pressed) {
		return;
	}

	/**
	 * Handles the logic for when the W key is pressed
	 */
	@Override
	public void onWAction(boolean pressed) {
		return;
	}
	
	/**
	 * Restarts the game to its initial setup
	 */
	public void restart(){
		hasWon = false;
		isPaused = false;
		
		kelpPlants.clear();
		predators.clear();
		walls.clear();
		
		resetPresses();
		init();
		initTimer();
		
		secondsCounter = 0;
	}
	


}
