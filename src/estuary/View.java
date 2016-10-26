package estuary;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Contains the JFrame window that the end user sees
 * Listens for mouse actions to send to the controller
 * 
 * @author Sean
 * @version 1.0 Oct 25, 2016
 */
public class View extends JComponent {

	private static final long serialVersionUID = 1L;
	
	int windowX = 800;
	int windowY = 600;
	
	Model model;
	
	JFrame frame;

	/**
	 * Initializes the window and adds a MouseListener for the window
	 * 
	 * @param model Reference to the model that contains the logic for each subgame
	 */
	public View(Model model) {
		
		this.model = model;
		
		this.init(this);
		
		this.addMouseListener(new CustomMouseListener());
	}
	
	/**
	 * Updating from events
	 * Each function has an equivalent in the View
	 */

	/**
	 * Is called every frame of the game to update
	 * what is drawn fort the current state of the game
	 */
	void onTick() {
		
	}
	
	/**
	 * Sends the controller the coordinates of the click
	 * 
	 * @param e The MouseEvent that contains the position of the click
	 */
	void onMousePress(MouseEvent e) {
		
	}
	
	/**
	 * Sends the controller the coordinates of the release
	 * 
	 * @param e The MouseEvent that contains the position of the release
	 */
	void onMouseRelease(MouseEvent e) {

	}
	
	/**
	 * Main method called each frame to draw the images and shapes
	 * 
	 * @param g The Graphics object that performs the drawing of each object
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
			
		this.onPaint(g);
	}
	
	/**
	 * Draws the different shapes and colors in the view
	 * 
	 * @param g THe Graphics objects that performs the drawing
	 */
	public void onPaint(Graphics g) {
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//g.setColor(Color.RED);
		//g.fillRect(model.x,  model.y,  50,  50);
		
		//System.out.println(model.x);
	}
	
	/**
	 * Makes a Runnable object and sets up the UI and window
	 * 
	 * @param thisview Reference to our own view so that the ContentPane can add itself
	 */
	public void init(View thisview) {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				frame = new JFrame("Mainframe");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(windowX, windowY);
				frame.setFocusable(true);
				frame.getContentPane().setBackground(Color.WHITE);
				// needed to pass itself to avoid (this)
				// referring to a Runnable object
				frame.getContentPane().add(thisview);
				frame.setVisible(true);
			}
		});	
	}
	
	/**
	 * The MouseListener responsible for capturing mouse input
	 * for the game. Calls the corresponding methods in actual View class
	 * 
	 * @author Sean
	 *
	 */
	class CustomMouseListener implements MouseListener {
		   
		public void mouseClicked(MouseEvent e) {
			// You can see if we double-clicked using this
	    }

		public void mousePressed(MouseEvent e) {
			//System.out.println("("+e.getX()+", "+e.getY() +")");
			//model.x = e.getX();
			//model.y = e.getY();
			  
			model.onMousePress(e);
			onMousePress(e); // View's onMousePress()
		}

	    public void mouseReleased(MouseEvent e) {
	    	  
	        model.onMouseRelease(e);
	    	onMouseRelease(e); // View's onMouseRelease()
	    }

	    public void mouseEntered(MouseEvent e) {
	    }

	    public void mouseExited(MouseEvent e) {
	    }
	}

}
