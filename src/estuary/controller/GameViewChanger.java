package estuary.controller;

import estuary.model.Model;
import estuary.model.MainMenu;
import estuary.model.ModelMazeGame;
import estuary.model.ModelCountingGame;
import estuary.model.ModelShoreGame;
import estuary.model.FinalScreen;

import estuary.view.View;
import estuary.view.ViewMainMenu;
import estuary.view.ViewMaze;
import estuary.view.ViewCounting;
import estuary.view.ViewShore;
import estuary.view.ViewFinal;

/**
 * This class assists in the setup to start a program initially at a certain game
 * This GameView switching is either done in the Ticker.actionPerformed() method,
 * or in the View's constructor to setup the program initially at a certain game,
 *
 * @author Sean
 *
 */
public class GameViewChanger {

	/**
	 * Changes the View's GameView based on the model's current Game (using an enum)
	 * @param model
	 * @param view
	 */
	public static void setGameView(Model model, View view) {

        switch (model.currentGame) {

		case MAIN_MENU:
			view.gameView = new ViewMainMenu((MainMenu)model.game, view);
			break;

		case MAZE_TUT:
		case MAZE:
			view.gameView = new ViewMaze((ModelMazeGame)model.game, view);
			break;

		case COUNT_TUT:
		case COUNT:
			view.gameView = new ViewCounting((ModelCountingGame)model.game, view);
			break;

		case SHORE_TUT:
		case SHORE:
			view.gameView = new ViewShore((ModelShoreGame)model.game, view);
			break;

		case FINAL:
			view.gameView = new ViewFinal((FinalScreen)model.game, view);
			break;

		default:
			System.err.println("GameViewChanger.setGameView() error!");
			throw new RuntimeException();

		}
    }
	
}
