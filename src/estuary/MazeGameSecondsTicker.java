package estuary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeGameSecondsTicker implements ActionListener {
	
	Model model;
	
	public MazeGameSecondsTicker(Model model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		model.secondsCounter++;
		
	}

}
