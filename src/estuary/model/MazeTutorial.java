package estuary.model;

/**
 * Creates a specific maze game designed to teach the user how to play
 * @author Greg Palmer
 *
 */
public class MazeTutorial extends ModelMazeGame{
	private static final long serialVersionUID = 1L;
	
	MazeTip currentTip;
	int tipStartTime;
	boolean tipVisibility = true;
	
	/**
	 * Creates a specific maze game designed to teach the user how to play
	 * In addition to everything a Maze Game keeps track of, a MazeTutorial keeps track of
	 * what tip it is currently on, how long the tip has been displayed and whether the tip is visible or not
	 */
	public MazeTutorial(){
		super();
		currentTip = MazeTip.MOVEMENT;
		tipStartTime = 0;
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
	public MazeTip getCurrentTip(){
		return currentTip;
	}
	
	/**
	 * Establishes where the Estuary Point will be in the game
	 */
	@Override
	public void generateEstuaryPoint(){
		estuaryPoint = new Vector2(490, 0);
		estuaryOrientation = EstuaryOrientation.TOP;
	}
	
	/**
	 * Initializes the MazeCrab into its starting location
	 */
	@Override
	public void setupMazeCrab(){
		this.mazeCrab = new MazeCrab(new Vector2(340,190));
	}
	
	/**
	 * Creates the walls of the maze
	 */
	@Override
	public void generateBarriers(){
		walls.add(new Barrier(new Rectangle(new Vector2(290,170),new Vector2(500,180))));
		walls.add(new Barrier(new Rectangle(new Vector2(290,180),new Vector2(300,340))));
		walls.add(new Barrier(new Rectangle(new Vector2(390,170),new Vector2(400,400))));
		walls.add(new Barrier(new Rectangle(new Vector2(150,400),new Vector2(400,410))));
		walls.add(new Barrier(new Rectangle(new Vector2(150,340),new Vector2(300,350))));//5
		walls.add(new Barrier(new Rectangle(new Vector2(150,400),new Vector2(160,520))));
		walls.add(new Barrier(new Rectangle(new Vector2(80,510),new Vector2(150,520))));
		walls.add(new Barrier(new Rectangle(new Vector2(70,110),new Vector2(80,520))));
		walls.add(new Barrier(new Rectangle(new Vector2(80,110),new Vector2(460,120))));
		walls.add(new Barrier(new Rectangle(new Vector2(450,0),new Vector2(460,120))));//10
		walls.add(new Barrier(new Rectangle(new Vector2(500,0),new Vector2(510,180))));
		walls.add(new Barrier(new Rectangle(new Vector2(80,260),new Vector2(210,270))));
		walls.add(new Barrier(new Rectangle(new Vector2(200,170),new Vector2(210,270))));
	}
	
	/**
	 * Initializes all predators in the maze
	 */
	@Override
	public void generatePredators(){
		predators.add(new Predator(new Vector2(230, 200), new Vector2(2.0, 0)));
	}
	
	/**
	 * Initializes the location of each kelp entity
	 */
	@Override
	public void setupKelp(){
		kelpPlants.add(new KelpPlant(new Vector2( 440, 140)));
		
	}
	
	/**
	 * The the end screen appears if the user reaches the Estuary point or if the crab runs out of health
	 */
	@Override
	public void isEndScreen() {
		boolean condition1 = endingConditionEstuary();
		
		if (condition1 && !isPaused) {
			secondsTimer.stop();
			hasWon = true;
			System.out.println("Maze Game has ended! You win!");
			isPaused = true;
		}
		
	}
	
	/**
	 * Updates every element of the game that changes between each tick
	 */
	@Override
	public boolean onTick(){
		
		switch (currentTip){
		
			case MOVEMENT:
				if(mazeCrab.getPosition().x < 140){
					currentTip = MazeTip.SALINITY;
					tipVisibility = true;
					isPaused = true;
					tipStartTime = secondsCounter;
				}
				break;
				
			case SALINITY:
				if(mazeCrab.getPosition().x < 290 && mazeCrab.getPosition().y < 260){
					currentTip = MazeTip.PREDATOR;
					tipVisibility = true;
					isPaused = true;
					tipStartTime = secondsCounter;
				}
				break;
				
			case PREDATOR:
				if(mazeCrab.getPosition().x > 400 && mazeCrab.getPosition().y < 170){
					currentTip = MazeTip.KELP;
					tipVisibility = true;
					isPaused = true;
					tipStartTime = secondsCounter;
				}
				break;
				
			case KELP:
				if(mazeCrab.getPosition().x > 460 && mazeCrab.getPosition().y < 50){
					currentTip = MazeTip.END;
					tipVisibility = true;
					tipStartTime = secondsCounter;
				}
				break;
				
			case END:
				
				break;
		
		}
		
		
		
		if(secondsCounter - 3 > tipStartTime && isPaused && currentTip!=MazeTip.END){
			isPaused = false;
		}
		
		if(secondsCounter - 6 > tipStartTime && tipVisibility){
			tipVisibility = false;
		}
		
		super.onTick();
		return false;
	}
}
