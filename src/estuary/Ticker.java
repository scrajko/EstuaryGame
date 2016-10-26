package estuary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListner responsible for running the logic
 * that happens due to the Timer event that happens every frame
 * 
 * @author Sean
 * @version 1.0 Oct 25, 2016
 */
public class Ticker implements ActionListener {

	Model      model;
	View       view;
	Controller controller;
	
	/**
	 * 
	 * @param model Reference to model, which contains all data and logic for each subgame
	 * @param view Reference to view, which contains all drawing functionality
	 * @param controller Reference to controller, which drives frame
	 */
	public Ticker(Model model, View view, Controller controller) {
		this.model      = model;
		this.view       = view;
		this.controller = controller;
	}
	
	/**
	 * Logic for every sequential time-step in the game!
	 * 
	 * @param arg0 ActionEvent for the timer
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		model.onTick();
		view.onTick();
		
		// Refreshing screen every frame
		view.repaint();
	}

}