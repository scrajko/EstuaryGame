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
 * This is the second game.
 * The user must find and select every crab before time runs out
 * in order to win.
 * 
 * @author Matt
 *
 */
public class ModelCountingGame extends Game{
	private static final long serialVersionUID = 1L;
	
	public static final int GAME_LENGTH_SECONDS = 30;
	public static final int CLICK_CRAB_BONUS    = 100;
	public static final int MISCLICK_PENALTY    = 50;
	
	public static final int WIDTH_FULL  = 800;
	public static final int HEIGHT_FULL = 600;
	
	public static final int Width  = 700;
	public static final int Height = 550;
	
	Random rand = new Random();
	
	public List<Barrier>		borders;
	public List<Rock>          rocks;
	public List<HorseshoeCrab> horseshoeCrabs;

	public int secondsCounterMax;
	
	public int horseshoeCrabCount;
	
	/**
	 * Initializes the elements of the game.
	 * Sets up the list of rocks and horseshoe crabs.
	 * Begins the countdown timer.
	 * 
	 * @param difficulty used to determine number of crabs, rocks, and time to complete
	 */
	public ModelCountingGame(int difficulty) {
		
		hasWon = false;
		isPaused = false;
		isPressed = false;
		
		score = 0;

		rocks = new ArrayList<Rock>();
		horseshoeCrabs = new ArrayList<HorseshoeCrab>();
		
		init();
		
		//add borders
		int WallWidth = 10;
		borders = new ArrayList<Barrier>();
		
		borders.add(new Barrier(new Rectangle(new Vector2(-WallWidth,     0), new Vector2(0, Height + WallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(Width ,   0), new Vector2(Width + WallWidth, Height + WallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(  0, Height ), new Vector2(Width ,  Height + WallWidth))));
		borders.add(new Barrier(new Rectangle(new Vector2(0.0,-WallWidth),new Vector2(Width ,0))));
	
	}
	
	/**
	 * Used for testing purposes
	 * Takes in externally-created lists of the game's elements
	 * @param difficulty
	 * @param rocks
	 * @param horseshoeCrabs
	 * @param count horseshoe crab count
	 * @param score
	 * @param border
	 */
	public ModelCountingGame(int difficulty, List<Rock> rocks, List<HorseshoeCrab> horseshoeCrabs, int count, int score, List<Barrier> border) {
	
		this.rocks = rocks;
		this.horseshoeCrabs = horseshoeCrabs;
		this.horseshoeCrabCount = count;
		this.score = score;
		this.borders = border;
		
		//Initializes
		initTimer();	
	}

	/**
	 * Initializes the game
	 */
	@Override
	public void init() {
		horseshoeCrabCount = 0;
		
		//Initializes
		initTimer();
		
		//Spawn HorseshoeCrabs
		generateCrabs();
		
		//Spawn rocks
		generateRocks();
		
	}
	
	/**
	 * Initializes the timer
	 */
	@Override
	public void initTimer(){
		ActionListener decrementSeconds = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e){
				secondsCounter--;
				System.out.println(secondsCounter);
				
				
				for(HorseshoeCrab hc : horseshoeCrabs){
					hc.secondsCounter++;
				}
			}

		};
	
		secondsTimer = new Timer(1000,decrementSeconds);
		secondsTimer.start();
	}

	/**
	 * The game ends if the every horseshoe crab is found,
	 * or when time runs out.
	 */
	@Override
	public boolean isEnd() {
		
		return isPressed;
			 
	}
	
	/**
	 * Determines if the end screen should appear
	 */
	@Override
	public void isEndScreen() {
		
		boolean condition1 = horseshoeCrabs.size() == horseshoeCrabCount;
		boolean condition2 = secondsCounter <= 0;
		
		if(condition1 && !isPaused){
			secondsTimer.stop();
			hasWon = true;
			System.out.println("Counting Game has ended! You win!");
			score = calculateScore();
			isPaused = true;
		}
		else if(condition2 && !isPaused){
			secondsTimer.stop();
			isPaused = true;
		}
	}


	/**
	 * Handles the logic for the collions between all game entities.
	 * Horseshoe crabs cannot go through rocks or the border of the game.
	 */
	@Override
	public void handleCollisions() {
	
		//mb - Handle collisions between horseshoecrabs and rocks
		for (HorseshoeCrab hcrab : horseshoeCrabs) {
			for (Rock rock : rocks) {
				if (hcrab.hitbox.intersects(rock.hitbox)) {					
					hcrab.velocity.x = 0; //-hcrab.velocity.x;
					hcrab.velocity.y = 0; //-hcrab.velocity.y;
				}
			}		
		}
		
		// Handle collision between horseshoecrab and game border
		for (HorseshoeCrab hcrab : horseshoeCrabs){
			for(Barrier barrier : borders){
				if(hcrab.hitbox.intersects(barrier.hitbox)){
					//Flip the horseshoecrab to move in opposite direction
					
					hcrab.velocity.x = -hcrab.velocity.x;
					hcrab.velocity.y = -hcrab.velocity.y;
				}
			}
		}
		

		//mb - handle collision between horseshoecrab and horseshoecrab
		for (int i = 0; i < horseshoeCrabs.size(); i++){
			for(int j = i+1; j < horseshoeCrabs.size(); j++){
				if(horseshoeCrabs.get(i).hitbox.intersects(horseshoeCrabs.get(j).hitbox)){
					

					//Set both crabs velocity to 0
					horseshoeCrabs.get(i).velocity.x = 0;
					horseshoeCrabs.get(i).velocity.y = 0;
					horseshoeCrabs.get(j).velocity.x = 0;
					horseshoeCrabs.get(j).velocity.y = 0; 
				}
			}
		}		
		
	}

	/**
	 * Handles all the logic for the frame-to-frame logic
	 * This includes moving entites and handling collisions.
	 */
	@Override
	public boolean onTick() {

		isEndScreen();
		
		if(!isPaused) {
			
			// 1. Move all Movable Entities

			for (HorseshoeCrab hcrab : horseshoeCrabs){
				
				if((hcrab.secondsCounter>3)&&(!hcrab.isMoving)){
					
					if(rand.nextInt(100)>98){
						double r1 = Math.random()/2 * (rand.nextBoolean()?-1:1);
						double r2 = Math.random()/2 * (rand.nextBoolean()?-1:1);
						Vector2 randomv1 = new Vector2(r1,r2);
						hcrab.velocity = randomv1;
						hcrab.secondsCounter = 0;
						hcrab.isMoving = true;
					}
				}
				
				else if((hcrab.secondsCounter>1)&&(hcrab.isMoving)){
					hcrab.velocity = new Vector2(0.0,0.0);
					hcrab.secondsCounter = 0;
					hcrab.isMoving = false;
				}
				
				hcrab.update();
				hcrab.move();
			}
			
			// 2. Check for collisions
			
			handleCollisions();
		}
		
		return false; 
	}
	
	
	/**
	 * Constructs horseshoeCrabs randomly
	 */
	void generateCrabs() {
		int hsCrabSpawn = rand.nextInt(7) + 13;
		while(horseshoeCrabs.size()<hsCrabSpawn){
			
			//Randomly generate position
			double randX = rand.nextInt(Width - (int)HorseshoeCrab.HORSESHOECRAB_WIDTH);
			double randY = rand.nextInt(Height - (int)HorseshoeCrab.HORSESHOECRAB_HEIGHT);
			
			//Create hsCrab and check for collisions
			HorseshoeCrab hs = new HorseshoeCrab(new Vector2(randX,randY),new Vector2(0.0,0.0));
			boolean hasCollision = false;
			for(HorseshoeCrab hs2 : horseshoeCrabs) {
				if(hs.hitbox.intersects(hs2.hitbox)) {
					hasCollision = true;
					break;
				}
			}
			for(Rock rock : rocks){
				if(hs.hitbox.intersects(rock.hitbox)){
					hasCollision = true;
					break;
				}
			}
			
			//if there are no collisions, add to the list
			if(!hasCollision){
				horseshoeCrabs.add(hs);
			}
			
		}
		
		setupCounter();
		
	}
	
	/**
	 * Scales the seconds timer to match the number of crabs
	 */
	void setupCounter() {
		
		//Scale the seconds timer to match the number of crabs
		secondsCounterMax = horseshoeCrabs.size() * 3;
		secondsCounter = secondsCounterMax;
	}
	
	/**
	 * Constructs all rocks at random positions
	 */
	void generateRocks() {
		
		int rockSpawn = rand.nextInt(6) + 16;
		while(rocks.size()<rockSpawn){
			//Randomly generate position
			double randX = rand.nextInt(Width - (int)Rock.ROCK_WIDTH);
			double randY = rand.nextInt(Height - (int)Rock.ROCK_LENGTH);
			Rock rock = new Rock(new Vector2(randX,randY));
			boolean hasCollision = false;
			for(HorseshoeCrab hs : horseshoeCrabs){
				if(rock.hitbox.intersects(hs.hitbox)){
					hasCollision = true;
					break;
				}
			}
			for(Rock rock2 : rocks){
				if(rock.hitbox.intersects(rock2.hitbox)){
					hasCollision = true;
					break;
				}
			}
			if(!hasCollision){
				rocks.add(rock);
			}
			
		}
	}

	
	/**
	 * Arrow keys do not do anything in this game.
	 */
	
	@Override
	public void onLeftAction(boolean pressed) {}

	@Override
	public void onRightAction(boolean pressed) {}

	@Override
	public void onUpAction(boolean pressed) {}

	@Override
	public void onDownAction(boolean pressed) {}
	

	/**
	 * Handles logic for mouse press on screen
	 */
	@Override
	public void mousePressed(Vector2 mousePosition) {
		System.out.println("Left mouse was pressed at : (" + mousePosition.x + ", " + mousePosition.y + ")" );
		
		boolean condition1 = horseshoeCrabs.size() == horseshoeCrabCount;
		if(condition1){
			isPressed = true;
		}
		else if(secondsCounter <= 0){
			restart();
		}
		
		updateScore(mousePosition);
		// mb - Handle collision between mouse click and horseshoecrab
		Iterator<HorseshoeCrab> horseshoeCrab_iter = horseshoeCrabs.iterator();
		while(horseshoeCrab_iter.hasNext()){
			HorseshoeCrab hcrab = horseshoeCrab_iter.next();
			
			if(hcrab.hitbox.contains(mousePosition)&&(!hcrab.isFound)){
				hcrab.isFound = true;
				horseshoeCrabCount++;
				System.out.println("HorseshoeCrab found");
				
			}
		}
		
		
		
	}
	
	/**
	 * Updates the score of the game based on where the user clicked
	 * @param position where the mouse clicked
	 */
	public void updateScore(Vector2 position) {
		
		
		boolean isContained  = false;
		boolean alreadyFound = false;
		
		for (HorseshoeCrab hcrab : horseshoeCrabs) {
			if (hcrab.hitbox.contains(position)) {
				if (hcrab.isFound) {
					System.out.println("alreadyfound");
					alreadyFound = true;
					break;
				}
				else {
					
					isContained = true;
					break;
				}
			}
			else {
				isContained = false;
			}
		}
		
		if (isContained) {
			score = score + CLICK_CRAB_BONUS;
		}
		else if (!alreadyFound) {
			if (score - MISCLICK_PENALTY > 0){
				score = score - MISCLICK_PENALTY;
				
			}
		}
	}
	

	/**
	 * handles the logic for the mouse being released
	 */
	@Override
	public void mouseReleased(MouseEvent e) {}

	/**
	 * Handles the logic for the G key being pressed
	 */
	@Override
	public void onGAction(boolean pressed) {}

	/**
	 * Handles the logic for the W key being pressed
	 */
	@Override
	public void onWAction(boolean pressed) {}
	
	/**
	 * Restarts the game to its initial state
	 */
	public void restart(){
		
		hasWon = false;
		isPaused = false;
		
		score = 0;

		rocks.clear();
		horseshoeCrabs.clear();
		
		init();

		
	}

	/**
	 * Calculates the score based on the horseshoecrabs found and extra time left
	 */
	@Override
	public int calculateScore() {
		// Points from selecting the Hcrabs + timer bonus
		return score + secondsCounter*50;
	}

	
}
