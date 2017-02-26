package estuary.model;

import java.awt.event.MouseEvent;

import estuary.controller.Tickable;

/**
 * The Model contains all the data and logic for each subgame
 * of our Estuary game.
 *
 * @author Sean
 * @version 1.0 Oct 25, 2016
 */
public class Model implements Tickable, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Game game;
	
	public int gameMode;
	int difficulty;
	public GAME_ENUM currentGame;
	public int overallScore;

	/**
	 * Dynamically initializes the data for the first game
	 */
	public Model () {

		
		difficulty = 1;
		currentGame = GAME_ENUM.MAIN_MENU;
				      //GAME_ENUM.MAZE_TUT;
				      //GAME_ENUM.MAZE;
					  //GAME_ENUM.COUNT_TUT;
				      //GAME_ENUM.COUNT;
					  //GAME_ENUM.SHORE_TUT;
				      //GAME_ENUM.SHORE;
				      //GAME_ENUM.FINAL;
		
		nextGame();
	
	}	
	
	/**
	 * Handles the logic for making the next game
	 */
	public void nextGame() {

		switch (currentGame) {
		
		  case MAIN_MENU:
			 System.out.println("Main Menu");
			 game = new MainMenu();
			 break;
		
		  case MAZE_TUT:
			 System.out.println("Maze Tutorial start");
			 game = new MazeTutorial();
			 break;
			    
		  case MAZE: // if (currentGame == MAZE):
			 System.out.println("ModelMazeGame start");
		     game = new ModelMazeGame();
		     break;
		    
		  case COUNT_TUT:
			 System.out.println("Counting Tutorial start");
			 game = new CountingTutorial();
			 break;
			  
		  case COUNT:
			 System.out.println("ModelCountingGame start");
			 game = new ModelCountingGame(difficulty);
			 break;
			
		  case SHORE:
			 System.out.println("ModelShoreGame start");
			 game = new ModelShoreGame(difficulty);
			 break;
			
		  case SHORE_TUT:
			  System.out.println("ShoreTut Start");
			  game = new ShoreTutorial(difficulty);
			  break;
			
		  case FINAL:
			  System.out.println("Final Screen");
			  game = new FinalScreen();
			  break;
			
		  default:
			System.out.println("Model::nextGame() ERROR!");
		}
		
		
	}
	
	
	
	/**
	 * Updating from events
	 * Each function has an equivalent in the View
	 */
	
	/**
	 * Is called every frame of the game to update
	 * the current state of the game
	 * 
	 * @return boolean whether game has changed
	 */
	@Override
	public boolean onTick() {
		game.onTick();
		
		if (game.isEnd()) {
			switch(currentGame){
				case MAIN_MENU:
					currentGame = GAME_ENUM.MAZE_TUT;
					break;
					
				case MAZE_TUT:
					currentGame = GAME_ENUM.MAZE;
					break;
					
				case MAZE:
					currentGame = GAME_ENUM.COUNT_TUT;
					overallScore = overallScore + game.score;
					System.out.println("Your score is " + overallScore);
					break;
					
				case COUNT_TUT:
					currentGame = GAME_ENUM.COUNT;
					break;
					
				case COUNT:
					currentGame = GAME_ENUM.SHORE_TUT;
					overallScore = overallScore + game.score;
					System.out.println("Your score is " + overallScore);
					break;
					
				case SHORE_TUT:
					currentGame =  GAME_ENUM.SHORE;
					break;
					
				case SHORE:
					currentGame = GAME_ENUM.FINAL;
					overallScore = overallScore + game.score;
					System.out.println("Your score is " + overallScore);
					break;
					
				case FINAL:
					currentGame = GAME_ENUM.MAIN_MENU;
					overallScore = 0;
					break;
					
			}
			nextGame();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Changes the game state based on where the user clicked
	 * 
	 * @param e The mouse event, which contains the position clicked
	 */
	void onMousePress(MouseEvent e) {}
	
	/**
	 * Changes the game state based on where the user released
	 * 
	 * @param e The mouse event, which contains the position released
	 */
	void onMouseRelease(MouseEvent e) {}
	
}

