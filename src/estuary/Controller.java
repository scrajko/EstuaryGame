package estuary;

import javax.swing.Timer;

/**
 * Controller runs the Timer that ticks through each frame.
 * It contains a reference to the model and the view.
 * The Timer uses the Ticker's actionPerformed method every frame.
 * 
 * @author Sean
 * @version 1.0 Oct 25, 2016
 *
 */
public class Controller {
	
	Model model;
	View  view;
	
	Timer frameTimer;

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
		
		frameTimer = new Timer(60, new Ticker(model, view, this));	
		frameTimer.start();

	}
	
	/**
	 * Creates the model, view, and controller.
	 * The ctor of the controller starts the main ticker for each frame of the game
	 * 
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Model model = new Model();
		
		// Make view, but not activated yet
		View view   = new View(model);
		
		// Pass both the Model and View into controller
		// controller Ticker and View are activated internally
		
		// Ticker has access to both the Model and View
		Controller controller = new Controller(model, view);
		
	}
}
