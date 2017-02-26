package estuary.model;

/**
 * This is a version of ShoreGame that is made to teach the player how to play the game
 * 
 * @author Greg Palmer
 * @version 12/6/16
 */
public class ShoreTutorial extends ModelShoreGame {
	private static final long serialVersionUID = 1L;
	
	ShoreTip currentTip;
	int tipStartTime;
	boolean tipVisibility;
	int shorethreatsSpawned = 0;
	
	/**
	 * Creates a shore game made specifically to teach how to play the game
	 * @param diff how hard the game is initially
	 */
	public ShoreTutorial(int diff) {
		
		super(diff);
		tipStartTime = 0;
		currentTip = ShoreTip.MOVEMENT;
		tipVisibility = true;
	}
	
	/**
	 * Returns whether the tip is visible or not
	 * @return tipVisibility
	 */
	public boolean getTipVisibility(){
		return tipVisibility;
	}
	
	/**
	 * Returns the current tip the tutorial is on
	 * @return currentTip
	 */
	public ShoreTip getCurrentTip(){
		return currentTip;
	}
	
	/**
	 * Initializes all of the elements for the game start
	 */
	@Override
	public void init(){
		
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
        
		for(double i = 0.0;i < WIDTH; i = i + 100){
			shoreline.add(new Shore(new Vector2(i,SHORE_LINE_POSITION)));
		}
		
	}
	
	/**
	 * Handles all of the changes that will occur between each tick
	 */
	@Override
	public boolean onTick(){
		
		switch(currentTip){
		
			case MOVEMENT:
				if(tipVisibility == false){
					currentTip = ShoreTip.SEAWALL;
					tipStartTime = secondsCounter;
					tipVisibility = true;
				}
				
				break;
				
			case SEAWALL:
				
				if(seawalls.size() > 0){
					currentTip = ShoreTip.SHORETHREAT;
					tipStartTime = secondsCounter;
					tipVisibility = true;
					spawnShoreThreat();
				}
				break;
				
			case SHORETHREAT:
				
				if(shorethreats.size()<1){
					currentTip = ShoreTip.OYSTERS;
					tipStartTime = secondsCounter;
					tipVisibility = true;
					spawnOyster();
				}
				break;
				
			case OYSTERS:
				
				if(oysterCount > 100){
					currentTip = ShoreTip.GABION;
					tipStartTime = secondsCounter;
					tipVisibility = true;
					spawnShoreThreat();
				}
				break;
				
			case GABION:
				
				if(gabions.size() > 0){
					currentTip = ShoreTip.END;
					tipStartTime = secondsCounter;
					tipVisibility = true;
				}
				break;
				
			case END:
				break;
		
		}
		
		
		if(secondsCounter - 5 > tipStartTime && tipVisibility){
			tipVisibility = false;
			System.out.println("Tip Visibility = false");
		}
		
		
		super.onTick();
		
		return false;
	}
	
	/**
	 * Determines if the endscreen should appear based on the end screen
	 */
	@Override
	public void isEndScreen() {

		if(currentTip == ShoreTip.END && !tipVisibility && !hasWon){
			System.out.println("Shore Tutorial is over! Click anywhere to continue!");
			hasWon = true;
			secondsTimer.stop();
			isPaused = true;
		}
		
	}
	
	
	/**
	 * Creates ShoreThreats
	 */
	@Override
	public void spawnShoreThreat(){
		
		if((currentTip == ShoreTip.SHORETHREAT || currentTip == ShoreTip.GABION) && shorethreatsSpawned < 2 && shorethreats.size() < 1){
			shorethreats.add(new ShoreThreat(new Vector2(0.0,SHORETHREAT_SPAWN_HEIGHT),new Vector2(ShoreThreat.SHORETHREAT_SPEED,0),5));
			shorethreatsSpawned++;
		}
	}

	/**
	 * Spawns Oysters
	 */
	@Override
	public void spawnOyster(){
		
		if(currentTip == ShoreTip.OYSTERS && oysters.size() < 2 && oysterCount < 1){
			oysters.add(new Oyster(new Vector2(640, 420), 50));
			oysters.add(new Oyster(new Vector2(590, 440), 60));
		}
		
	}
	
	/**
	 * Handles logic for when the mouse is pressed
	 * This is only used to exit the game when the endscreen is up
	 */
	@Override
	public void mousePressed(Vector2 mousePosition) {

		if(hasWon){
			isPressed = true;
		}
	}
}
