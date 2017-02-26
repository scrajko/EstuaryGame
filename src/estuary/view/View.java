package estuary.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import java.awt.Graphics;

import java.awt.Point;
import java.awt.event.ActionEvent;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.UIManager;

import javax.swing.UnsupportedLookAndFeelException;

import estuary.controller.Controller;
import estuary.controller.GameViewChanger;
import estuary.model.Entity;

import estuary.model.Model;
import estuary.model.ModelCountingGame;

import estuary.model.Rectangle;
import estuary.model.Vector2;

/**
 * Contains the JFrame window that the end user sees
 * Listens for mouse actions to send to the controller
 * 
 * @author Sean
 * @version 1.0 Oct 25, 2016
 */
public class View extends JComponent {

	private static final long serialVersionUID = 1L;
	
	static final int DEFAULT_WINDOW_X = 800;
	static final int DEFAULT_WINDOW_Y = 600;
	
	int windowX = DEFAULT_WINDOW_X;
	int windowY = DEFAULT_WINDOW_Y;
	
	Point light;
	
	static final int LIGHT_RADIUS               = 255;
	static final int ModelCountingGame_DARKNESS = 251;
	
	static int VIEWPORT_BUFFER = 100;
	
	public Model model;
	Controller controller;
	
	public GameView gameView;

	JFrame frame;
	
	boolean upKeyPressed;
	boolean downKeyPressed;
	boolean leftKeyPressed;
	boolean rightKeyPressed;

	JButton startGameButton;
	JPanel controlPanel;
	
	Entity shoreWater;
	
	/**
	 * Initializes the window and adds a MouseListener for the window
	 * 
	 * @param model Reference to the model that contains the logic for each subgame
	 */
	public View(Model model) {
		
		this.model = model;
		
		light = new Point(0, 0);

		GameViewChanger.setGameView(this.model, this);
		
		this.init(this);
		
		// Listen for clicking and entering events:
		this.addMouseListener(new CustomMouseListener());
		
		// Listen for dragging and moving events:
		this.addMouseMotionListener(new CustomMouseListener());
		
		// Listen for anything that causes window to resize:
		this.addComponentListener(new CustomComponentListener());

		// See: http://stackoverflow.com/questions/7071757/keylistener-keypressed-versus-keytyped
		
		bindKeyWith("y.up.press",      KeyStroke.getKeyStroke("UP"),             new UpKeyPressedAction()     );
		bindKeyWith("y.down.press",    KeyStroke.getKeyStroke("DOWN"),           new DownKeyPressedAction()   );
		bindKeyWith("x.left.press",    KeyStroke.getKeyStroke("LEFT"),           new LeftKeyPressedAction()   );
		bindKeyWith("x.right.press",   KeyStroke.getKeyStroke("RIGHT"),          new RightKeyPressedAction()  );
		bindKeyWith("g.press",         KeyStroke.getKeyStroke("G"),              new GKeyPressedAction()      );
		bindKeyWith("w.press",         KeyStroke.getKeyStroke("W"),              new WKeyPressedAction()      );
		bindKeyWith("y.up.release",    KeyStroke.getKeyStroke("released UP"),    new UpKeyReleasedAction()    );
		bindKeyWith("y.down.release",  KeyStroke.getKeyStroke("released DOWN"),  new DownKeyReleasedAction()  );
		bindKeyWith("x.left.release",  KeyStroke.getKeyStroke("released LEFT"),  new LeftKeyReleasedAction()  );
		bindKeyWith("x.right.release", KeyStroke.getKeyStroke("released RIGHT"), new RightKeyReleasedAction() );
		bindKeyWith("g.release",       KeyStroke.getKeyStroke("G"),              new GKeyReleasedAction()     );
		bindKeyWith("w.release",       KeyStroke.getKeyStroke("W"),              new WKeyReleasedAction()     );
		
   		bindKeyWith("s.ctrl",          KeyStroke.getKeyStroke("control S"),      new SaveKeyPressedAction()   );
        bindKeyWith("l.ctrl",          KeyStroke.getKeyStroke("control L"),      new LoadKeyPressedAction()   );
		
		upKeyPressed    = false;
		downKeyPressed  = false;
		leftKeyPressed  = false;
		rightKeyPressed = false;

	}

	/**
	 * Binds a key with the appropriate action
	 * @param name
	 * @param keyStroke
	 * @param action
	 */
	private void bindKeyWith(String name, KeyStroke keyStroke, Action action) {
		
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
		
		im.put(keyStroke, name);
		am.put(name,  action);		
	}

	/**
	 * Handles the functionality when the up key is pressed
	 *
	 */
	public class UpKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (!upKeyPressed) {
				model.game.onUpAction(true);
				upKeyPressed = true;
			}
		}	
	}
	
	/**
	 * Handles the functionality when the right key is pressed
	 *
	 */
	public class RightKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (!rightKeyPressed) {
				model.game.onRightAction(true);
				rightKeyPressed = true;
			}
		}
		
	}
	
	/**
	 * Handles the functionality when the left key is pressed
	 *
	 */
	public class LeftKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (!leftKeyPressed) {
				model.game.onLeftAction(true);
				leftKeyPressed = true;
			}
		}
		
	}
	
	/**
	 * Handles the functionality when the down key is pressed
	 *
	 */
	public class DownKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (!downKeyPressed) {
				model.game.onDownAction(true);
				downKeyPressed = true;
			}
		}
		
	}
	
	/**
	 * Handles the functionality when the G key is pressed
	 *
	 */
	public class GKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			model.game.onGAction(true);	
		}
		
	}
	
	/**
	 * Handles the functionality when the W key is pressed
	 *
	 */
	public class WKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			model.game.onWAction(true);	
		}
		
	}
	
	/**
	 * Handles the functionality when the up key is released
	 *
	 */
	public class UpKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			upKeyPressed = false;
			model.game.onUpAction(false);	
		}
		
	}
	
	/**
	 * Handles the functionality when the right key is released
	 *
	 */
	public class RightKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			rightKeyPressed = false;
			model.game.onRightAction(false);	
		}
	}
	
	/**
	 * Handles the functionality when the left key is released
	 *
	 */
	public class LeftKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			leftKeyPressed = false;
			model.game.onLeftAction(false);	
		}
	}
	
	/**
	 * Handles the functionality when the down key is released
	 *
	 */
	public class DownKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			downKeyPressed = false;
			model.game.onDownAction(false);	
		}	
	}
	
	/**
	 * Handles the functionality when the G key is released
	 *
	 */
	public class GKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			model.game.onGAction(false);	
		}
	}
	
	/**
	 * Handles the functionality when the W key is released
	 *
	 */
	public class WKeyReleasedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			model.game.onWAction(false);	
		}
	}
	
	/**
	 * Used when CTRL + S is pressed to save the game
	 *
	 */
	public class SaveKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			controller.writeToFile(Controller.SAVE_FILENAME);
		}
		
	}
	
	/**
	 * Used when CTRL + L is pressed to load the game
	 *
	 */
	public class LoadKeyPressedAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			controller.readFromFile(Controller.SAVE_FILENAME);
			model.game.onWAction(false);	
		}
		
	}  
	
	
	/**
	 * Updating from events
	 * Each function has an equivalent in the View
	 */

	/**
	 * Is called every frame of the game to update
	 * what is drawn fort the current state of the game
	 */
	public void onTick() {	
		repaint();
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
		
		// Paint whole screen white:
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		gameView.onPaint(g);
	}
	

	/**
	 * Draws an image of an entity's animation scaled to the current viewport
	 * @param g Graphics object
	 * @param viewport
	 * @param e Entity to be drawn
	 */
	public void drawImage_usingViewport(Graphics g, Viewport viewport, Entity e) {
		
		Rectangle rect = e.hitbox;
		Vector2 tl = viewport.mapCoordToPixel(rect.topLeft);
		Vector2 br = viewport.mapCoordToPixel(rect.bottomRight);

		BufferedImage img = ImageHandler.getImageFrame(e.animation.imageIndex, e.animation.frameIndex);

		g.drawImage(img,
			    (int)tl.x, (int)tl.y,
			    (int)br.x, (int)br.y,
		 	    0, 0, img.getWidth(), img.getHeight(), this);
	}
	
	/**
	 * Draws an image using given endpoints and then scales it to the current window size 
	 * @param g Graphics element
	 * @param viewport
	 * @param topLeft Location of TopLeft of image
	 * @param bottomRight Location of BottomRight of image
	 * @param img Image to be drawn
	 */
	public void drawImage_usingViewport_NoEntity(Graphics g, Viewport viewport, Vector2 topLeft, Vector2 bottomRight, BufferedImage img) {

		Vector2 tl = viewport.mapCoordToPixel(topLeft);
		Vector2 br = viewport.mapCoordToPixel(bottomRight);

		g.drawImage(img,
			    (int)tl.x, (int)tl.y,
			    (int)br.x, (int)br.y,
		 	    0, 0, img.getWidth(), img.getHeight(), this);
	}
	
	/**
	 * Draws an image using given endpoints
	 * @param g Graphics element
	 * @param topLeft Location of TopLeft of image
	 * @param bottomRight Location of BottomRight of image
	 * @param img Image to be drawn
	 */
	public void drawImage_NoEntity(Graphics g, Vector2 topLeft, Vector2 bottomRight, BufferedImage img) {
		g.drawImage(img,
			    (int)topLeft.x,     (int)topLeft.y,
			    (int)bottomRight.x, (int)bottomRight.y,
		 	    0, 0, img.getWidth(), img.getHeight(), this);
	}
	
	/**
	 * Draws an entity at its position
	 * @param g Graphics Element
	 * @param e Entity to be drawn
	 */
	public void drawImage(Graphics g, Entity e) {
		
		BufferedImage img = ImageHandler.getImageFrame(e.animation.imageIndex, e.animation.frameIndex);
		Rectangle rect = e.hitbox;
		g.drawImage(img,
			    (int)rect.topLeft.x, (int)rect.topLeft.y,
			    (int)rect.bottomRight.x, (int)rect.bottomRight.y,
		 	    0, 0, img.getWidth(), img.getHeight(), this);
	}
	
	
	
	/**
	 * Draws a Rectangle that represents an Entity with a certain color onto the screen
	 * 
	 * @param g Graphics object that is responsible for drawing commands
	 * @param e Entity object that contains a hitbox
	 * @param color Color the Rectangle is drawn
	 */
	public void draw(Graphics g, Entity e, Color color){
		
		Rectangle rect = e.hitbox;
		g.setColor(color);
		g.fillRect((int)rect.topLeft.x,
				   (int)rect.topLeft.y,
				   (int)rect.getWidth(),
				   (int)rect.getHeight());
	}
	
	/**
	 * Fills a rectangle of color and scales it to the viewport
	 * @param g
	 * @param viewport
	 * @param x Top Left x coordinate
	 * @param y Top Left y coordinate
	 * @param width
	 * @param height
	 */
	public void fillRect_usingViewport(Graphics g, Viewport viewport,
			int x, int y, int width, int height) {
		
		Vector2 tL = viewport.mapCoordToPixel(new Vector2(x, y));
		Vector2 bR = viewport.mapCoordToPixel(new Vector2(x + width, y + height));
		int viewportWidth  = (int)(bR.x - tL.x);
		int viewportHeight = (int)(bR.y - tL.y);
		g.fillRect(x, y, viewportWidth, viewportHeight);
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
				frame.setResizable(true);
				
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

	class CustomComponentListener implements ComponentListener {

		@Override
		public void componentHidden(ComponentEvent arg0) {
			return;
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			return;
		}

		@Override
		public void componentResized(ComponentEvent e) {
		
			// http://stackoverflow.com/questions/5097301/jframe-get-size-without-borders
			Dimension dim = frame.getContentPane().getSize();
			
			windowX = (int)dim.getWidth();
			windowY = (int)dim.getHeight();

			gameView.onResized(dim);
			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			return;
		}
		
	}
	
	/**
	 * Listener for mouse clicking and mouse movement
	 *
	 */
	class CustomMouseListener implements MouseMotionListener, MouseListener {
		  
		@Override
		public void mouseClicked(MouseEvent e) {}
	    
		@Override
	    public void mouseEntered(MouseEvent e) {}

		@Override
	    public void mouseExited(MouseEvent e) {}
	    
		/**
		 * Calls gameView's mouse pressed logic
		 */
		@Override
	    public void mousePressed(MouseEvent e) {
			
			// Convert From Screen Coordinates to World Coordinates
			gameView.mousePressed(e);
		}
	    
		/**
		 * Calls game's mouseReleased logic
		 */
	    @Override
	    public void mouseReleased(MouseEvent e) {	  
	        model.game.mouseReleased(e);
	    }

		@Override
		public void mouseDragged(MouseEvent arg0) {}

		/**
		 * Updates Light's position to match the mouse's
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			
			if (model.game instanceof ModelCountingGame) {
				light.x = e.getPoint().x;
				light.y = e.getPoint().y;
			}
		}
	}

	/**
	 * Setter for controller
	 * @param controller
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

}