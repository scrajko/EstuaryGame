package estuary.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.Timer;

import estuary.model.Model;
import estuary.view.View;

/**
 * Controller runs the Timer that ticks through each frame.
 * It contains a reference to the model and the view.
 * The Timer uses the Ticker's actionPerformed method every frame.
 * 
 * @author Sean
 * @version 1.0 12/7/2016
 *
 */
public class Controller {
	
	Model model;
	View  view;
	
	Timer frameTimer;
	
	Ticker ticker;
	
	public final static String SAVE_FILENAME = "game.sav";

	/**
	 * Assigns the references to the model and view,
	 * starts the timer which uses the Ticker class
	 * 
	 * @param model Reference to the game's model, that contains
	 *        all 3 subgames, and the logic for each frame each game
	 * @param view Reference to the game's view, that contains
	 *        the functionality for displaying the game on the screen
	 * 
	 */
	public Controller(Model model, View view) {
		
		this.model = model;
		this.view  = view;
		
		view.setController(this);
		
		ticker = new Ticker(model, view, this);
		
		// 1000 ms / 30 (ticks / s) = 33.3333 millseconds per tick
		frameTimer = new Timer(33, ticker);	
		frameTimer.start();
	}
	


	/**
	 * Loads the FileInputStream for the save file
	 * Gracefully generates a FileNotFoundException
	 */
	public FileInputStream getFileInputStream(String filepath) throws FileNotFoundException
	{
	    File file = new File(filepath);
	    FileInputStream fInputStream = new FileInputStream(file);
	    return fInputStream;
	}
	
	/**
	 * Writes the Estuary Game to a file.
	 * Saving hotkey is Ctrl + S
	 *
	 */
	public void writeToFile(String filepath) {
		
		System.out.println("Saving to " + filepath);
		try {
		    FileOutputStream fos = new FileOutputStream(filepath);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    
		    System.out.println("Saving game at seconds = " + this.model.game.secondsCounter);
		    oos.writeObject(model);	    	    
		    oos.close();
		}
		catch (Exception ex) {
			ex.printStackTrace(System.out);
			System.out.println("Terminating Program...");
			throw new RuntimeException();
		}
	}
	
	/**
	 * Takes in a saved file and loads in the game state
	 * @param filepath saved file
	 */
	public void readFromFile(String filepath) {
	    
		System.out.println("Reading from " + filepath);
        try
        {
            FileInputStream fis = getFileInputStream(filepath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            model.game.secondsTimer.stop();
            
            this.model = (Model) ois.readObject();
            view.model   = this.model;
            ticker.model = this.model;
            view.gameView.view = view;
            GameViewChanger.setGameView(model, view);
            this.model.game.initTimer();

            ois.close();
        }
        catch (Exception ex) //FileNotFoundException ex)
        {
            // save file not found: load new one
            this.model = new Model();
            model.game.resetPresses();
            view.model   = this.model;
            ticker.model = this.model;
            GameViewChanger.setGameView(this.model, this.view);
            this.model.game.initTimer();
        }
	}
		
	/**
	 * Creates the model, view, and controller.
	 * The ctor of the controller starts the main ticker for each frame of the game
	 * 
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		// ImageHandler()'s constant variables are loaded before anything else
		// The images are also loaded at start-up, in ImageHandler, statically
		
		Model model = new Model();
		
		// Make view, but not activated yet
		View view   = new View(model);
		
		// Pass both the Model and View into controller
		// controller Ticker and View are activated internally
		
		// Ticker has access to both the Model and View
		Controller controller = new Controller(model, view);
		
	}
}
