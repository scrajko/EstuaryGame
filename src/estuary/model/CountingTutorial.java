package estuary.model;

import java.util.Iterator;

/**
 * A version of counting game that is made to teach the player how to play
 * @author Greg Palmer
 *
 */
public class CountingTutorial extends ModelCountingGame {
	private static final long serialVersionUID = 1L;
	
	public CountingTip currentTip;
	int tipStartTime;
	boolean tipVisibility = true; 
	
	/**
	 * Creates a specific counting game designed to teach the user how to play.
	 * In addition to everything a Counting Game keeps track of, a CountingTutorial keeps track of
	 * what tip it is currently on, how long the tip has been displayed and whether the tip is visible or not
	 */
	public CountingTutorial(){
		super(1);
		currentTip = CountingTip.COUNT;
		tipStartTime = secondsCounter;
		
	}

	/**
	 * Returns a boolean stating whether the tip can be seen or not
	 * @return boolean TipVisibility
	 */
	public boolean getTipVisibility(){
		return tipVisibility;
	}
	
	/**
	 * Returns the current tip the tutorial is on
	 * @return MazeTip currentTip
	 */
	public CountingTip getCurrentTip(){
		return currentTip;
	}
	
	/**
	 * Initializes every rock
	 */
	@Override
	public void generateRocks(){
		rocks.add(new Rock(new Vector2(240,240)));
		rocks.add(new Rock(new Vector2(30,80)));
		rocks.add(new Rock(new Vector2(610,128)));
		rocks.add(new Rock(new Vector2(520,60)));
		rocks.add(new Rock(new Vector2(460,410)));
		rocks.add(new Rock(new Vector2(60,370)));
	}
	
	/**
	 * Initializes all of the horseshoeCrabs
	 */
	@Override
	public void generateCrabs(){
		horseshoeCrabs.add(new HorseshoeCrab(new Vector2(350,250),new Vector2(0.0,0.0)));
		horseshoeCrabs.add(new HorseshoeCrab(new Vector2(50,460),new Vector2(0.0,0.0)));
		horseshoeCrabs.add(new HorseshoeCrab(new Vector2(610,440),new Vector2(0.0,0.0)));
		
		setupCounter();
	}
	
	/**
	 * Initialize the counter to 60
	 */
	@Override
	public void setupCounter(){
		secondsCounterMax = 60;
		secondsCounter = secondsCounterMax;
	}
	
	/**
	 * Updates all of the entities that may change between each tip
	 */
	@Override
	public boolean onTick(){
		
		if(currentTip == CountingTip.COUNT && horseshoeCrabCount > 0){
			currentTip = CountingTip.GLOW;
			tipVisibility = true;
			tipStartTime = secondsCounter;
		}
		else if(currentTip == CountingTip.GLOW && horseshoeCrabCount > 1){
			currentTip = CountingTip.BATTERY;
			tipVisibility = true;
			tipStartTime = secondsCounter;
		}
		
		if(secondsCounter + 6 < tipStartTime){
			tipVisibility = false;
		}
		
		super.onTick();
		
		return false;
	}
	
	/**
	 * Determines if the end screen should appear
	 */
	@Override
	public void isEndScreen() {
		
		boolean condition1 = horseshoeCrabs.size() == horseshoeCrabCount;
		
		if(condition1 && !isPaused){
			secondsTimer.stop();
			hasWon = true;
			System.out.println("Counting Tutorial has ended! You win!");
			score = calculateScore();
			isPaused = true;
		}
	}
	
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
}
