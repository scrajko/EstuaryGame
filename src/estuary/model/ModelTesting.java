package estuary.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import estuary.view.ImageHandler;

/**
 * Calls to all tests to make sure game runs properly
 * 
 * @author Sean
 *
 */
public class ModelTesting {

  	final static double EPSILON = 0.000001;
  	
  	@Test
  	public void test_Rectangle001() {
  		Rectangle rect = new Rectangle(
  			new Vector2(100.0, 200.0),
  			new Vector2(450.0, 460.0)
  		);
  		assertEquals(rect.topLeft.x + rect.getWidth(), rect.getRight(), EPSILON);

  	}
	
	ModelMazeGame createCrabKelpCollisionMazeGame() {
		
		List<Barrier>   walls      = new ArrayList<Barrier>();
		List<KelpPlant> kelpPlants = new ArrayList<KelpPlant>();
		List<Predator>  predators  = new ArrayList<Predator>();
		
		
		// Add estuaryPoint at top of map
		Vector2 estuaryPoint           = new Vector2(500, 5);
		EstuaryOrientation orientation = EstuaryOrientation.TOP;
		
		kelpPlants.add(new KelpPlant(new Vector2(100.0, 100.0)));
		kelpPlants.add(new KelpPlant(new Vector2(200.0, 200.0)));
		kelpPlants.add(new KelpPlant(new Vector2(300.0, 300.0)));
		
		MazeCrab mazeCrab = new MazeCrab(new Vector2(89.0, 100.0));
		
		return new ModelMazeGame(0, mazeCrab, kelpPlants, walls, predators, estuaryPoint, orientation);	
	}
	
	ModelMazeGame createCrabPredatorCollisionMazeGame() {
		
		List<Barrier>   walls      = new ArrayList<Barrier>();
		List<KelpPlant> kelpPlants = new ArrayList<KelpPlant>();
		List<Predator>  predators  = new ArrayList<Predator>();
		Vector2 estuaryPoint       = new Vector2(500, 5);
		EstuaryOrientation orientation = EstuaryOrientation.TOP;

		// Crab and Predator are both initially to the left of the wall,
		// and they are travelling to the right (towards the wall)
		
		double pos_y = 700;
		
		double crab_x = 500 - MazeCrab.SIZE/2 - 1;
		double velo_x = 2.0;
		MazeCrab mazeCrab = new MazeCrab(new Vector2(crab_x, pos_y),
                                         new Vector2(velo_x,   0.0));
		
		predators.add(new Predator(new Vector2(489.0, pos_y), new Vector2(2.0, 0.0)));

		return new ModelMazeGame(0, mazeCrab, kelpPlants, walls, predators, estuaryPoint, orientation);
		
	}
	
	/**
	 * Setup to test both Crabs and Predators colliding with walls.
	 * @return the new ModelMazeGame to be tested
	 */
	ModelMazeGame createWallCollisionMazeGame() {
		
		List<Barrier>   walls      = new ArrayList<Barrier>();
		List<KelpPlant> kelpPlants = new ArrayList<KelpPlant>();
		List<Predator>  predators  = new ArrayList<Predator>();
		Vector2 estuaryPoint = new Vector2(500, 5);
		EstuaryOrientation orientation = EstuaryOrientation.TOP;
		
		walls.add(new Barrier(new Rectangle(new Vector2(500, 0), new Vector2(600, 1000))));
		
		double entity_pos = 100.0;
		
		MazeCrab mazeCrab = new MazeCrab(new Vector2(entity_pos, entity_pos),
                                         new Vector2(0.0, 0.0));
		
		double pred_x = mazeCrab.position.x + mazeCrab.hitbox.getWidth() + 1.0;
		predators.add(new Predator(new Vector2(pred_x, entity_pos), new Vector2(-2.0, 0.0)));

		return new ModelMazeGame(0, mazeCrab, kelpPlants, walls, predators, estuaryPoint, orientation);
		
	}
	
	// assertEquals(msg, expected, actual)
	
	
	/*************************
	 * Maze Game Model Tests *
	 *************************/
	
	@Test
	public void test_CrabMove001() {
		
		MazeCrab mazeCrab = new MazeCrab(new Vector2(0,0), new Vector2(3, 3));
		
		mazeCrab.move();
		
		assertEquals("Crab moves to correct position given velocity",
				     new Vector2(3, 3), mazeCrab.position);
	}
	
	
	@Test
	public void test_CrabPredator001() {
		
		ModelMazeGame mazeGame = createCrabPredatorCollisionMazeGame();
		
		// possible initial adjustments
		mazeGame.mazeCrab.setHealth(MazeCrab.MAX_HEALTH);
		
		mazeGame.onTick();
		
		assertEquals("Crab Health decreases after getting hit by a predator",
					 MazeCrab.MAX_HEALTH - 1, mazeGame.mazeCrab.getHealth());
	}
	
	@Test
	public void test_CrabPredator002() {
		// When a predator hits a crab, the crab should be hurt,
		// but we don't want the crab to immidiately get hurt again
		// the next frame, when the predator will probably still be colliding with him
		// there has to be a cooldown on getting hurt
		
		ModelMazeGame mazeGame = createCrabPredatorCollisionMazeGame();
		
		// possible initial adjustments
		mazeGame.mazeCrab.setHealth(MazeCrab.MAX_HEALTH);
		
		mazeGame.onTick();
		mazeGame.onTick();
		
		assertEquals("Crab Health doesn't immediately lose more than 1 health for colliding with Predator",
					 MazeCrab.MAX_HEALTH - 1, mazeGame.mazeCrab.getHealth());
	}
	
	
	
	@Test
	public void test_CrabKelp001() {
		
		ModelMazeGame mazeGame = createCrabKelpCollisionMazeGame();

		int size = mazeGame.kelpPlants.size();
		
		mazeGame.onTick();
		
		assertEquals("Kelp goes away after being eaten",
					 size - 1, mazeGame.kelpPlants.size());
	}
	
	@Test
	public void test_CrabKelp002() {
		
		ModelMazeGame mazeGame = createCrabKelpCollisionMazeGame();
		
		// possible initial adjustments
		mazeGame.mazeCrab.setHealth(MazeCrab.MAX_HEALTH);
		
		mazeGame.onTick();
		
		assertEquals("When MazeCrab with full health eats Kelp, health doesn't increase",
					 MazeCrab.MAX_HEALTH, mazeGame.mazeCrab.getHealth());
	}
	
	@Test
	public void test_CrabKelp003() {
		
		ModelMazeGame mazeGame = createCrabKelpCollisionMazeGame();
		
		// possible initial adjustments
		mazeGame.mazeCrab.setHealth(MazeCrab.MAX_HEALTH - 1);
		
		mazeGame.onTick();
		
		assertEquals("MazeCrab eats Kelp, gains 1 health",
					 MazeCrab.MAX_HEALTH, mazeGame.mazeCrab.getHealth());
	}
	
	
	
	public void setupPredatorForBarrierCollision(ModelMazeGame mazeGame) {
		// Predator is going left (-2.0 units/tick in the x-dir)
		Predator pred = mazeGame.predators.get(0);
		Barrier barr = mazeGame.walls.get(0);
		pred.setPosition(barr.getPosition().x + barr.hitbox.getWidth() + 1.0, 400);
		
	}
	
	@Test
	public void test_PredatorBarrierCollision001() {
		
		ModelMazeGame mazeGame = createWallCollisionMazeGame();
		setupPredatorForBarrierCollision(mazeGame);
		Predator predator = mazeGame.predators.get(0);
		
		// Copy velocity for later reference
		Vector2 oldVelocity = new Vector2(predator.velocity);
		
		mazeGame.onTick();

		assertEquals("When Predator runs into wall, its direction should be reversed",
				-oldVelocity.x, predator.velocity.x,
				EPSILON);
	}
	
	@Test
	public void test_PredatorBarrierCollision002() {
		
		ModelMazeGame mazeGame = createWallCollisionMazeGame();
		setupPredatorForBarrierCollision(mazeGame);
		Predator pred = mazeGame.predators.get(0);

		// Copy position and velocity.x for later reference
		Vector2 oldPosition = new Vector2(pred.position);
		double oldVelocity_x = pred.velocity.x;
		
		mazeGame.onTick();
		
		assertEquals("When Predator runs into wall, its position isnt immediately fixed",
				oldPosition.x + oldVelocity_x, pred.position.x,
				EPSILON);
	}
	
	@Test
	public void test_PredatorBarrierCollision003() {
		
		ModelMazeGame mazeGame = createWallCollisionMazeGame();
		
		Vector2 position = new Vector2(mazeGame.mazeCrab.getPosition());

		mazeGame.onTick();
		
		
		assertEquals("When Crab runs into wall from left, its position should not be changed",
				position, mazeGame.mazeCrab.getPosition());
		/*
		Barrier barrier = mazeGame.walls.get(0);
		assertEquals("When Crab runs into wall from left, its position should be against wall but not into it",
				barrier.position.x - mazeGame.mazeCrab.hitbox.getWidth(), mazeGame.mazeCrab.position.x,
				EPSILON);
		*/
	}
	
	@Test
	public void test_PredatorBarrierCollision004() {
		
		ModelMazeGame mazeGame = createWallCollisionMazeGame();
		
		// Move Maze Crab to be almost against wall;
		mazeGame.mazeCrab.setPosition(601.0, mazeGame.mazeCrab.getPosition().x);
		mazeGame.mazeCrab.velocity.x =  -2.0;
		
		Vector2 position = new Vector2(mazeGame.mazeCrab.position);

		mazeGame.onTick();

		assertEquals("When Crab runs into wall from right, its position should not change",
				position, mazeGame.mazeCrab.getPosition());
				//barrier.position.x + barrier.hitbox.getWidth(), mazeGame.mazeCrab.position.x,
				//EPSILON);
	}
	
	
	
	@Test
	public void test_intersects001() {
		
		MazeCrab  mazeCrab  = new MazeCrab(new Vector2(10.0, 10.0));
		KelpPlant kelpPlant = new KelpPlant(new Vector2(15.0, 15.0));
		
		boolean intersects = mazeCrab.hitbox.intersects(kelpPlant.hitbox);
		
		assertEquals("Collision between Crab from NW and KelpPlant detected",
				true, intersects);
	}
	
	@Test
	public void test_intersects002() {
		
		MazeCrab  mazeCrab  = new MazeCrab(new Vector2(10.0, 10.0));
		KelpPlant kelpPlant = new KelpPlant(new Vector2(5.0, 15.0));
		
		boolean intersects = mazeCrab.hitbox.intersects(kelpPlant.hitbox);
		
		assertEquals("Collision between Crab from NE and KelpPlant detected",
				true, intersects);
	}
	
	@Test
	public void test_intersects003() {
		
		MazeCrab  mazeCrab  = new MazeCrab(new Vector2(10.0, 10.0));
		KelpPlant kelpPlant = new KelpPlant(new Vector2(15.0, 5.0));
		
		boolean intersects = mazeCrab.hitbox.intersects(kelpPlant.hitbox);
		
		assertEquals("Collision between Crab from SW and KelpPlant detected",
				true, intersects);
	}
	
	@Test
	public void test_intersects004() {
		
		MazeCrab  mazeCrab  = new MazeCrab(new Vector2(10.0, 10.0));
		KelpPlant kelpPlant = new KelpPlant(new Vector2(5.0, 5.0));
		
		boolean intersects = mazeCrab.hitbox.intersects(kelpPlant.hitbox);
		
		assertEquals("Collision between Crab from SE and KelpPlant detected",
				true, intersects);
	}
	
	
	@Test
	public void test_radiusFromEstuary() {
		
		ModelMazeGame mazeGame = new ModelMazeGame();
		mazeGame.estuaryPoint = new Vector2(500, 50);
		mazeGame.mazeCrab = new MazeCrab(new Vector2(500, 500), new Vector2(0, -100));
		
		double radiusBefore = mazeGame.radiusFromEstuary();
		mazeGame.mazeCrab.move();
		
		double radiusAfter = mazeGame.radiusFromEstuary();
		boolean condition = radiusAfter < radiusBefore;

		assertTrue("Radius decreases as you move toward estuary", condition);
	}
	
	@Test
	public void test_MazeGame_isEnd001() {
		
		ModelMazeGame mazeGame = new ModelMazeGame();
		mazeGame.estuaryPoint = new Vector2(500, 50);
		mazeGame.mazeCrab = new MazeCrab(new Vector2(500, 100), new Vector2(0, -100));
		mazeGame.estuaryOrientation = EstuaryOrientation.TOP;

		mazeGame.walls.clear();
		mazeGame.onTick();

		assertTrue("Game ends (pauses) once you cross the estuary finish line", mazeGame.isPaused);
	}
	
	@Test
	public void test_MazeMouse(){
		ModelMazeGame mazeGame = new ModelMazeGame();
		

		mazeGame.mousePressed(new Vector2());
		
		assertFalse("If the crab is not past the estuary point and the user clicks, isPressed should be false.",mazeGame.isPressed);
		
		mazeGame.mazeCrab.setHealth(0);
		mazeGame.onTick();
		mazeGame.mousePressed(new Vector2());
		assertFalse("If the crab has zero health and the user clicks, isPressed should be false.",mazeGame.isPressed);
		
		
		mazeGame.mazeCrab.setPosition(new Vector2(-5,200));
		mazeGame.onTick();
		mazeGame.mousePressed(new Vector2());
		
		assertTrue("If the crab is past the estuary point and the user clicks, isPressed should be true.",mazeGame.isPressed);
	}
	
	@Test
	public void test_mazeButtons(){
		ModelMazeGame mazeGame = new ModelMazeGame();
		
		mazeGame.onDownAction(true);
		
		assertEquals("The maze crabs velocity should change by MazeCrab.SPEED when down is pressed.",
				MazeCrab.SPEED,mazeGame.mazeCrab.velocity.y,EPSILON);
		
		mazeGame.onDownAction(false);
		assertEquals("The maze crabs velocity should change back to 0 when down is released.",
				0,mazeGame.mazeCrab.velocity.y,EPSILON);
		
		mazeGame.onUpAction(true);
		assertEquals("The maze crabs velocity should change by -MazeCrab.SPEED when down is pressed.",
				-MazeCrab.SPEED,mazeGame.mazeCrab.velocity.y,EPSILON);
		
		mazeGame.onUpAction(false);
		assertEquals("The maze crabs velocity should change back to 0 when down is released.",
				0,mazeGame.mazeCrab.velocity.y,EPSILON);
		
		mazeGame.onLeftAction(true);
		assertEquals("The maze crabs velocity should change by -MazeCrab.SPEED when left is pressed.",
				-MazeCrab.SPEED,mazeGame.mazeCrab.velocity.x,EPSILON);
		
		mazeGame.onLeftAction(false);
		assertEquals("The maze crabs velocity should change back to 0 when left is released.",
				0,mazeGame.mazeCrab.velocity.x,EPSILON);
		
		mazeGame.onRightAction(true);	
		assertEquals("The maze crabs velocity should change by MazeCrab.SPEED when right is pressed.",
				MazeCrab.SPEED,mazeGame.mazeCrab.velocity.x,EPSILON);
		
		mazeGame.onRightAction(false);
		assertEquals("The maze crabs velocity should change back to 0 when right is released.",
				0,mazeGame.mazeCrab.velocity.x,EPSILON);
	}
	
	
	
	/*****************************
	 * Counting Game Model Tests *
	 *****************************/
	
	ModelCountingGame createhorseshoecrabrockcollision(){
		
		List<Rock> rocks      = new ArrayList<Rock>();
		List<HorseshoeCrab> horseshoeCrabs = new ArrayList<HorseshoeCrab>();
		List<Barrier> border = new ArrayList<Barrier>();
		int count = 0;
		int score = 0;
		
		horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0, 10.0),new Vector2(3.0,0)));
		rocks.add(new Rock(new Vector2(
				HorseshoeCrab.HORSESHOECRAB_WIDTH + 1.0, 10.0)));

		border.add(new Barrier(new Rectangle(new Vector2(-100.0, 0.0),new Vector2(0.0, 300.0))));
		border.add(new Barrier(new Rectangle(new Vector2(0.0, -100.0),new Vector2(400.0,0.0))));
		border.add(new Barrier(new Rectangle(new Vector2(400.0, 0.0),new Vector2(500.0,300.0))));
		border.add(new Barrier(new Rectangle(new Vector2(0.0, 300.0),new Vector2(400.0,400.0))));
		return new ModelCountingGame(0, rocks, horseshoeCrabs, count, score, border);
	}
	@Test
	public void test_horseshoeCrabmove() {
		
		HorseshoeCrab horseshoeCrab = new HorseshoeCrab(new Vector2(0,0), new Vector2(3, 3));
		
		horseshoeCrab.move();
		
		assertEquals("HorseshoeCrab moves to correct position given velocity",
					 new Vector2(3, 3), horseshoeCrab.position);
	}
	
	@Test
	public void test_horseshoeCrabRockCollision001() {
		
		ModelCountingGame countingGame = createhorseshoecrabrockcollision();
		countingGame.isPaused = false;
		countingGame.secondsCounter = 30;
		countingGame.onTick();
		
		assertEquals("When horseshoecrab collides with rock, it stops moving",
				0, countingGame.horseshoeCrabs.get(0).velocity.x,
				EPSILON);
	}
	
	@Test
	public void test_horseshoeCrabRockCollision002() {
		
		ModelCountingGame countingGame = createhorseshoecrabrockcollision();
		Vector2 oldPosition = new Vector2(countingGame.horseshoeCrabs.get(0).position);
		countingGame.onTick();
		
		assertEquals("When horseshoecrab collides with rock, its position should not change",
				oldPosition.x, countingGame.horseshoeCrabs.get(0).position.x,
				EPSILON);
	}
	
	@Test
	public void test_horseshoeCrabBorderCollision002(){
		
		ModelCountingGame countingGame = createhorseshoecrabrockcollision();
		
		countingGame.horseshoeCrabs.get(0).position = new Vector2(10.0, 399.0);
		
		countingGame.onTick();
		
		assertEquals("When horseshoecrab collides with border, its position should not change",
				new Vector2(10.0,399.0), countingGame.horseshoeCrabs.get(0).position);
	}
	
	@Test
	public void test_horseshoeCrabBorderCollision003(){
		
		ModelCountingGame countingGame = createhorseshoecrabrockcollision();
		
		countingGame.rocks.clear();
		countingGame.borders.clear();
		countingGame.horseshoeCrabs.clear();
		
		countingGame.borders.add(new Barrier(new Rectangle(new Vector2(0,0),new Vector2(10,10))));
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10,5), new Vector2(-2,0)));
		double oldVelocityX = -2;
		countingGame.horseshoeCrabs.get(0).move();
		countingGame.handleCollisions();
		
		assertEquals("When horseshoecrab collides with border, its velocity should invert",
				-oldVelocityX, countingGame.horseshoeCrabs.get(0).velocity.x,
				EPSILON);
	}
		
	
	@Test
	public void test_CountingGame_isEnd001() {
		
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.secondsCounter = 0;
		countingGame.onTick();
		
		assertTrue("When time runs out, the game should pause", countingGame.isPaused);
	}
	
	@Test
	public void test_CountingGame_isEnd002() {
		
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.horseshoeCrabCount = countingGame.horseshoeCrabs.size();
		
		countingGame.onTick();
		
		assertTrue("When we count all the crabs, the game should pause", countingGame.isPaused);
	}
	
	
	@Test
	public void test_CountingGame_Score001() {
		
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0, 10.0),new Vector2(0.0,0.0)));
		//countingGame.horseshoeCrabs.get(1).isFound = true;
		countingGame.updateScore(new Vector2(11.0,11.0));
		
		assertEquals("When horseshoe crab is click on, score increases by 100",
				     ModelCountingGame.CLICK_CRAB_BONUS, countingGame.score);
	}
	
	@Test
	public void test_CountingGame_Score002() {
		
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0, 10.0)));
		
		countingGame.score = 100;
		int oldScore = countingGame.score;
		//countingGame.onMouseClick(new Vector2(9.0, 9.0));
		countingGame.updateScore(new Vector2(9.0, 9.0));
		
		assertEquals("When the user is clicks on something other than a horseshoe, score decreases by 50",
				     oldScore - ModelCountingGame.MISCLICK_PENALTY, countingGame.score);
	}
	
	@Test
	public void test_CountingGame_Score003() {
		
		ModelCountingGame countingGame = new ModelCountingGame(1);
		countingGame.horseshoeCrabs.clear();
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0, 10.0)));
		
		countingGame.score = 0;
		countingGame.updateScore(new Vector2(9.0, 9.0));
		
		assertEquals("When the user clicks on something other than a horseshoe crab, score decreases to no less than 0",
				     0, countingGame.score);
	}
	
	@Test
	public void test_CountingMousePresses(){
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.mousePressed(new Vector2());
		assertFalse("If the size of horseshoecrabs != horseshoecrabcount and the mouse is pressed, isPressed should be false",countingGame.isPressed);
		
		countingGame.secondsCounter = 0;
		countingGame.mousePressed(new Vector2());
		assertFalse("If the secondsCounter is 0 and the mouse is pressed, isPressed should be false",countingGame.isPressed);
		
		countingGame.horseshoeCrabs.clear();
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10,10)));
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(100,100)));
		countingGame.mousePressed(new Vector2(11,11));
		assertTrue("If a horseshoe crab is clicked on, isFound should be true.",countingGame.horseshoeCrabs.get(0).isFound);
		
		int oldScore = countingGame.score;
		countingGame.mousePressed(new Vector2(11,11));
		assertEquals("If a horseshoe crab is clicked on and is already found, the score should not change.",oldScore,countingGame.score);
		
		countingGame.restart();
		countingGame.horseshoeCrabs.clear();
		countingGame.mousePressed(new Vector2());
		assertTrue("If the size of horseshoecrabs == horseshoecrabcount and the mouse is pressed, isPressed should be true",countingGame.isPressed);
		
		
		
	}
	
	@Test
	public void test_CountingButtons(){
		ModelCountingGame countingGame = new ModelCountingGame(1);
		
		countingGame.onDownAction(true);
		assertFalse("Pressing down should keep isPressed false",countingGame.isPressed);
		
		countingGame.onUpAction(true);
		assertFalse("Pressing up should keep isPressed false",countingGame.isPressed);
		
		countingGame.onLeftAction(true);
		assertFalse("Pressing left should keep isPressed false",countingGame.isPressed);
		
		countingGame.onRightAction(true);
		assertFalse("Pressing right should keep isPressed false",countingGame.isPressed);
		
		countingGame.onWAction(true);
		assertFalse("Pressing W should keep isPressed false",countingGame.isPressed);
		
		countingGame.onGAction(true);
		assertFalse("Pressing G should keep isPressed false",countingGame.isPressed);
		
		countingGame.mouseReleased(null);
		assertFalse("Mouse Release should keep isPressed false",countingGame.isPressed);
	}
	
	@Test
	public void test_HorseshoeCrabHorseshoeCrabCollision(){
		ModelCountingGame countingGame = new ModelCountingGame(1);
		countingGame.horseshoeCrabs.clear();
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0, 10.0),new Vector2(3,0)));
		countingGame.horseshoeCrabs.add(new HorseshoeCrab(new Vector2(10.0 + HorseshoeCrab.HORSESHOECRAB_WIDTH, 10.0),new Vector2(0,0)));
		countingGame.onTick();
		
		assertEquals("When a horshoecrab collides with a horseshoecrab, its velocity should be set to 0",
				new Vector2(),countingGame.horseshoeCrabs.get(0).velocity);
	}
	
	@Test
	public void test_HorseshoeCrabAnimationUpdate(){
		HorseshoeCrab hcrab = new HorseshoeCrab(new Vector2(),new Vector2(0,0));
		
		hcrab.velocity.x = 1;
		hcrab.update();
		
		assertEquals("If the crab x velocity is positive, its image should be to the right.",
				ImageHandler.HorseshoeCrab_East,hcrab.animation.imageIndex);
		
		hcrab.velocity.x = -1;
		hcrab.update();
		
		assertEquals("If the crab x velocity is negative, its image should be to the left.",
				ImageHandler.HorseshoeCrab_West,hcrab.animation.imageIndex);
		
	}
	
	
	
	/*************************
	 * Shore Game Model Test *
	 *************************/
	
	ModelShoreGame createCollisionShore(){
		
		List<Wave> waves = new ArrayList<Wave>();
		List<ShoreThreat> shoreThreats = new ArrayList<ShoreThreat>();
		List<Oyster> oysters = new ArrayList<Oyster>();
		List<Shore> shoreLine = new ArrayList<Shore>();
		List<SeaWall> seaWalls = new ArrayList<SeaWall>();
		List<OysterGabion> oysterGabions = new ArrayList<OysterGabion>();
		List<Barrier> borders = new ArrayList<Barrier>();
		
		int oysterCounter = 0;
		
		ShoreCrab shoreCrab = new ShoreCrab(new Vector2(10.0,200.0), new Vector2(2.0,0.0));
		
		oysters.add(new Oyster(new Vector2(ShoreCrab.WIDTH + 11,200.0),20));
		
		waves.add(new Wave(new Vector2(10.0,196.0)));
		
		shoreLine.add(new Shore(new Vector2(0.0,200.0)));
		shoreLine.add(new Shore(new Vector2(Shore.SHORE_WIDTH,200.0)));
		shoreLine.add(new Shore(new Vector2(Shore.SHORE_WIDTH * 2 ,200.0)));
		
		seaWalls.add(new SeaWall(new Vector2(0.0,190.0)));
		
		oysterGabions.add(new OysterGabion(new Vector2(Shore.SHORE_WIDTH * 2,190.0)));
		

		borders.add(new Barrier(new Rectangle(new Vector2(-100.0, 0.0),new Vector2(0.0, 300.0))));
		borders.add(new Barrier(new Rectangle(new Vector2(0.0, 100.0),new Vector2(400.0,200.0))));
		borders.add(new Barrier(new Rectangle(new Vector2(400.0, 0.0),new Vector2(500.0,300.0))));
		borders.add(new Barrier(new Rectangle(new Vector2(0.0, 300.0),new Vector2(400.0,400.0))));
		
		return new ModelShoreGame(shoreCrab, waves, shoreThreats, oysters, shoreLine, seaWalls, oysterGabions, borders, oysterCounter);
		
	}
	
	@Test
	public void test_shoreRestart(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		ModelShoreGame shoreGame2 = new ModelShoreGame(1);
		shoreGame.restart();
		
		assertEquals("The position of the shore crab should be the same after restarting as when initialized.",
				shoreGame2.shoreCrab.position,shoreGame.shoreCrab.position);
		assertEquals("The number of shore entities should be the same after restarting as when initialized.",
				shoreGame2.shoreline.size(),shoreGame.shoreline.size());
		assertEquals("The number of the shorethreats should be the same after restarting as when initialized.",
				shoreGame2.shorethreats.size(),shoreGame.shorethreats.size());
		
		
	}
	
	@Test
	public void test_shoreMouse2(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		shoreGame.mousePressed(new Vector2());
		
		assertFalse("If hasWon is false, isPressed should stay false.",shoreGame.isPressed);
		
		shoreGame.shoreline.get(0).health = 0;
		shoreGame.mousePressed(new Vector2());
		shoreGame.onTick();
		assertFalse("If a shore dies and the mouse is pressed, isPressed should stay false.",shoreGame.isPressed);
		
		for(int i = 0; i < 7; i++){
			shoreGame.gabions.add(new OysterGabion(new Vector2()));
		}
		shoreGame.onTick();
		shoreGame.mousePressed(new Vector2());
		assertTrue("If gabion size = shoreline size, isPressed should become true.", shoreGame.isPressed);
		
		
	}
	
	@Test
	public void test_shoreCrabMove001() {
		
		ShoreCrab shoreCrab = new ShoreCrab(new Vector2(10.0,10.0),new Vector2(5.0,0.0));
		shoreCrab.move();
		
		assertEquals("ShoreCrab moves to correct position given velocity",
				new Vector2(15.0,10.0), shoreCrab.position);
	}
	
	
	
	@Test
	public void test_shoreCollisions001(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		int oldOysterCount = shoreGame.oysterCount;
		shoreGame.onTick();
		
		assertTrue("Oyster total should increase when crab collides with oyster.",
				oldOysterCount < shoreGame.oysterCount);
	}
	
	@Test
	public void test_shoreCollisions002(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		int oysterTotal = shoreGame.oysters.size();
		shoreGame.onTick();
		
		assertEquals("Collected oyster should be removed from oysters.",
				oysterTotal - 1, shoreGame.oysters.size());
	}
	
	@Test
	public void test_shoreCollisions003(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		int oldWaveTotal = shoreGame.waves.size();
		shoreGame.onTick();
		
		assertEquals("Wave should be removed from list when it collides with shore.",
				oldWaveTotal - 1, shoreGame.waves.size());
	}
	
	@Test
	public void test_shoreCollisions004(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		int oldShoreHealth = shoreGame.shoreline.get(0).health;
		shoreGame.onTick();
		
		assertEquals("Wave should damage shore when it collides with shore.",
				oldShoreHealth - 1, shoreGame.shoreline.get(0).health);
	}
	
	@Test
	public void test_shoreCollisions005(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		shoreGame.waves.get(0).position = new Vector2(20.0,186.0);
		int oldWaveTotal = shoreGame.waves.size();
		shoreGame.onTick();
		
		assertEquals("Wave should be removed from list when it collides with sea wall.",
				oldWaveTotal - 1, shoreGame.waves.size());
	}
	
	@Test
	public void test_shoreCollisions006(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.waves.add(new Wave((new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION - SeaWall.SEAWALL_HEIGHT - Wave.WAVE_SPEED + 1))));
		shoreGame.seawalls.add(new SeaWall(new Vector2(0.0,ModelShoreGame.SHORE_LINE_POSITION - SeaWall.SEAWALL_HEIGHT)));
		
		int oldSeaWallHealth = shoreGame.seawalls.get(0).health;
		shoreGame.onTick();
		
		assertEquals("The seawall should lose health when a wave collides with it.",
				oldSeaWallHealth - 1, shoreGame.seawalls.get(0).health);
	}
	
	@Test
	public void test_shoreCollisions007(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		shoreGame.waves.get(0).position = new Vector2(20.0,186.0);
		
		int oldShoreHealth = shoreGame.shoreline.get(1).health;
		shoreGame.onTick();
		
		assertEquals("The shore should not lose health when a wave collides with the sea wall.",
				oldShoreHealth, shoreGame.shoreline.get(1).health);
	}
	
	@Test
	public void test_shoreCollisions008(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.waves.add(new Wave((new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION - SeaWall.SEAWALL_HEIGHT - Wave.WAVE_SPEED + 1))));
		shoreGame.seawalls.add(new SeaWall(new Vector2(0.0,ModelShoreGame.SHORE_LINE_POSITION - SeaWall.SEAWALL_HEIGHT)));
		shoreGame.seawalls.get(0).health = 1;
		
		int seawallSize =  shoreGame.seawalls.size();
		shoreGame.onTick();
		
		
		assertEquals("A sea wall should be removed from the list when it collides with a wave and its health is 0.",
				seawallSize-1, shoreGame.seawalls.size());
	}
	
	@Test
	public void test_shoreCollisions009(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		shoreGame.waves.get(0).position = new Vector2(30.0,186.0);
		
		int oldWaveTotal = shoreGame.waves.size();
		shoreGame.onTick();
		
		assertEquals("A wave should be removed from the list when it collides with a gabion.",
				oldWaveTotal-1, shoreGame.waves.size());
	}
	
	@Test
	public void test_shoreCollisions010(){
		
		ModelShoreGame shoreGame = createCollisionShore();
		
		shoreGame.waves.get(0).position = new Vector2(30.0,186.0);
		
		int oldShoreHealth = shoreGame.shoreline.get(2).health;
		shoreGame.onTick();
		
		assertEquals("A shore should not lose health when a wave collides with a gabion.",
				oldShoreHealth, shoreGame.shoreline.get(2).health);
	}
	
	@Test
	public void test_shoreCollisions011(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		Vector2 oldPosition = new Vector2(ModelShoreGame.WIDTH - ShoreCrab.WIDTH,250.0);
		shoreGame.shoreCrab.position = oldPosition;
		shoreGame.shoreCrab.velocity.x = 2.0;
		
		shoreGame.onTick();
		
		assertEquals("A crab position should not chnage when colliding with the border",
				oldPosition.x, shoreGame.shoreCrab.position.x , EPSILON);
	}
	
	@Test
	public void test_shoreCollisions012(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		Vector2 oldPosition = new Vector2(1,250.0);
		shoreGame.shoreCrab.position = oldPosition;
		shoreGame.shoreCrab.velocity.x = -2.0;
		
		shoreGame.onTick();
		
		assertEquals("A crab position should not move when it collides with a border.",
				oldPosition.x, shoreGame.shoreCrab.position.x,
				EPSILON);
	}
	
	@Test
	public void test_shoreCollisions013(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		Vector2 oldPosition = new Vector2(1,ModelShoreGame.HEIGHT - ShoreCrab.HEIGHT);
		shoreGame.shoreCrab.position = oldPosition;
		shoreGame.shoreCrab.velocity.y = 2.0;
		
		shoreGame.onTick();
		
		assertEquals("A crab position should not move when it collides with a border.",
				oldPosition.y, shoreGame.shoreCrab.position.y,
				EPSILON);
	}
	
	@Test
	public void test_shoreCollisions014(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		Vector2 oldPosition = new Vector2(1,ModelShoreGame.SHORE_LINE_POSITION + ShoreCrab.HEIGHT);
		shoreGame.shoreCrab.position = oldPosition;
		shoreGame.shoreCrab.velocity.y = -2.0;
		
		shoreGame.onTick();
		
		assertEquals("A crab position should not move when it collides with the top border.",
				oldPosition.y, shoreGame.shoreCrab.position.y,
				EPSILON);
	}
	
	@Test
	public void test_shoreCollisions015(){
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.seawalls.add(new SeaWall(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION - SeaWall.SEAWALL_HEIGHT)));
		int oldSeaWallSize = shoreGame.seawalls.size();
		shoreGame.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION+1));
		shoreGame.shoreCrab.isHoldingGabion = true;
		
		shoreGame.onGAction(true);
		shoreGame.onTick();
		
		assertEquals("A sea wall should be destroyed if it collides with a gabion.",
				oldSeaWallSize - 1, shoreGame.seawalls.size());
	}
	
	@Test
	public void test_shoreButtons(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.onDownAction(true);
		
		assertEquals("The shore crabs velocity should change by ShoreCrab.SPEED when down is pressed.",
				ShoreCrab.SPEED,shoreGame.shoreCrab.velocity.y,EPSILON);
		
		shoreGame.onDownAction(false);
		assertEquals("The shore crabs velocity should change back to 0 when down is released.",
				0,shoreGame.shoreCrab.velocity.y,EPSILON);
		
		shoreGame.onUpAction(true);
		assertEquals("The shore crabs velocity should change by -ShoreCrab.SPEED when down is pressed.",
				-ShoreCrab.SPEED,shoreGame.shoreCrab.velocity.y,EPSILON);
		
		shoreGame.onUpAction(false);
		assertEquals("The shore crabs velocity should change back to 0 when down is released.",
				0,shoreGame.shoreCrab.velocity.y,EPSILON);
		
		shoreGame.onLeftAction(true);
		assertEquals("The shore crabs velocity should change by -ShoreCrab.SPEED when left is pressed.",
				-ShoreCrab.SPEED,shoreGame.shoreCrab.velocity.x,EPSILON);
		
		shoreGame.onLeftAction(false);
		assertEquals("The shore crabs velocity should change back to 0 when left is released.",
				0,shoreGame.shoreCrab.velocity.x,EPSILON);
		
		shoreGame.onRightAction(true);	
		assertEquals("The shore crabs velocity should change by ShoreCrab.SPEED when right is pressed.",
				ShoreCrab.SPEED,shoreGame.shoreCrab.velocity.x,EPSILON);
		
		shoreGame.onRightAction(false);
		assertEquals("The shore crabs velocity should change back to 0 when right is released.",
				0,shoreGame.shoreCrab.velocity.x,EPSILON);
	}
	
	@Test
	public void test_shoreMouse(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		boolean oldIsPressed = shoreGame.isPressed;
		
		shoreGame.mouseReleased(null);
		assertEquals("Releasing the mouse should not affect isPressed.",oldIsPressed,shoreGame.isPressed);
	}
	
	@Test
	public void test_shorePlaceWallonGabion(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION));
		shoreGame.shoreCrab.isHoldingGabion = true;
		shoreGame.onGAction(true);
		shoreGame.onWAction(true);
		
		assertEquals("A wall should not be able to be placed on top of a gabion.",0,shoreGame.seawalls.size());
		
	}
	
	@Test 
	public void test_BuildWall(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION));
		shoreGame.seawalls.add(new SeaWall(new Vector2()));
		shoreGame.onWAction(true);
		
		assertEquals("Pressing W while intersecting a shore should create a wall.",2,shoreGame.seawalls.size());
		
		shoreGame.onWAction(true);
		assertEquals("You can't place a wall on a wall.",2,shoreGame.seawalls.size());
		
	}
	
	@Test
	public void test_ShoreThreatSpawn(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		int oldShoreThreatCount = shoreGame.shorethreats.size();
		shoreGame.addNewShoreThreat();
		
		assertEquals("addNewShoreThreat should create a new shorethreat.",oldShoreThreatCount+1,shoreGame.shorethreats.size());
		
	}
	
	@Test 
	public void test_OysterSpawn(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		int oldOysterCount = shoreGame.oysters.size();
		shoreGame.addNewOyster();
		
		assertEquals("addNewOyster should create a new oyster.",oldOysterCount+1,shoreGame.oysters.size());
		
		
	}
	
	
	@Test
	public void test_moveBusy001() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.velocity = new Vector2(2.0, 0.0);
		shoreGame.buildingWallTime = 0;
		shoreGame.shoreCrab.isBuildingWall = true;
		
		double oldPositionX = shoreGame.shoreCrab.position.x;
		
		shoreGame.onTick();
		
		assertEquals("ShoreCrab should not be able to move if it is on a Busy Frame",
				     oldPositionX, shoreGame.shoreCrab.position.x,
				     EPSILON);
	}
	
	@Test
	public void test_carryingGabion001() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.oysterCount = 100;
		shoreGame.shoreCrab.isHoldingGabion = true;
		shoreGame.onGAction(true);
		
		assertEquals("Can't make another oysterGabion if already carrying one", 100, shoreGame.oysterCount);
	}
	
	@Test
	public void test_carryingGabion002() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.isHoldingGabion = false;
		shoreGame.oysterCount = 100;
		shoreGame.onGAction(true);
				
		assertEquals("Can make an oysterGabion if not already carrying one", 0, shoreGame.oysterCount);
		assertTrue("Once you make a gabion, the shore Crab is holding it", shoreGame.shoreCrab.isHoldingGabion);
	}
	
	
	@Test
	public void test_crabBuildingReset(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.isBuildingWall = true;
		shoreGame.buildingWallTime = 4;
		shoreGame.onTick();
		assertFalse("After building a wall for more than 3 seconds, isBuildingWall should be false.", shoreGame.shoreCrab.isBuildingWall);
	}
	
	@Test
	public void test_placeGabion(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION));
		shoreGame.shoreCrab.isHoldingGabion = true;
		shoreGame.onGAction(true);
		assertEquals("Pressing G should place a gabion",1,shoreGame.gabions.size());
		
		shoreGame.shoreCrab.isHoldingGabion = true;
		shoreGame.onGAction(true);
		assertEquals("Pressing G again should not place another gabion",1,shoreGame.gabions.size());
		
	}
	
	
	@Test
	public void test_ShoreGame_isEnd001() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		for (int i = 0; i < shoreGame.shoreline.size(); i++) {
			shoreGame.gabions.add(new OysterGabion(new Vector2(100.0, 100.0)));
		}
		
		shoreGame.onTick();
		
		assertTrue("Game ends (pauses) when # of oyster gabions == # of shores", shoreGame.isPaused);
		
	}
		
	@Test
	public void test_isEndScreen002() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreline.add(new Shore(new Vector2(100.0, 100.0)));
		shoreGame.shoreline.get(0).health = 0;
		
		shoreGame.onTick();
		
		assertTrue("Game ends (pauses) when a shore's health is 0", shoreGame.isPaused);
	}
	
	@Test
	public void test_shoreThreat001() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shorethreats.add(new ShoreThreat(new Vector2(10.0, 10.0),5));
		
		// List of <ShoreThreat>
		
		int oldNumberWaves = shoreGame.waves.size();
		shoreGame.waves.add(shoreGame.shorethreats.get(0).generateWave());
		
		assertEquals("generateWave() adds a wave to the list of waves", oldNumberWaves + 1, shoreGame.waves.size());
		
	}
	
	@Test
	public void test_shoreThreat002() {
		
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shorethreats.add(new ShoreThreat(new Vector2(ModelShoreGame.WIDTH - ShoreThreat.SHORETHREAT_WIDTH,
				ModelShoreGame.SHORETHREAT_SPAWN_HEIGHT)));
		int oldShoreThreatNum = shoreGame.shorethreats.size();
		shoreGame.onTick();
		
		assertEquals("shoreThreat has been deleted after leaving map", oldShoreThreatNum - 1, shoreGame.shorethreats.size());
		
	}
	
	@Test
	public void test_shoreWaveGabionCollision(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.gabions.add(new OysterGabion(new Vector2(0,100)));
		shoreGame.waves.add(new Wave(new Vector2(0,100-Wave.WAVE_HEIGHT)));
		shoreGame.onTick();
		
		assertEquals("A wave should be removed if it collides with a gabion.",0,shoreGame.waves.size());
		
		
	}
	
	@Test
	public void test_shoreCrabBarrierCollision(){
		ModelShoreGame shoreGame = new ModelShoreGame(1);
		
		shoreGame.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION));
		shoreGame.shoreCrab.velocity.x = -1;
		shoreGame.onTick();
		
		assertEquals("The shoreCrabs position should not change if it collides with a barrier",0,shoreGame.shoreCrab.getPosition().x,EPSILON);
		
	}
	
	@Test
	public void test_ShoreThreatAnimation(){
		ShoreThreat shoreThreat = new ShoreThreat(new Vector2(0,0),2);
		
		assertEquals("If the shorethreats power is 2 or less, its image should be of a sailboat",
				ImageHandler.getImageFrame(ImageHandler.Sailboat_Right, 0),
				ImageHandler.getImageFrame(shoreThreat.animation.imageIndex,
						                   shoreThreat.animation.frameIndex));
		
		shoreThreat = new ShoreThreat(new Vector2(0,0),3);
		
		assertEquals("If the shorethreats power is 3 or 4, its image should be of a boat",
				ImageHandler.getImageFrame(ImageHandler.Boat_Right, 0),
				ImageHandler.getImageFrame(shoreThreat.animation.imageIndex,
						                   shoreThreat.animation.frameIndex));
	}
	
	@Test
	public void testVector2(){
		Vector2 vector = new Vector2();
		
		assertEquals("Default vector constructor makes zero length vector",
				0.0,vector.magnitudeSquared(),EPSILON);
		
		
		assertTrue("Vector should equal itself",vector.equals(vector));
		assertFalse("Vector should not be equal to a different object",vector.equals(new String()));
		
		vector = new Vector2(3,2);
		Vector2 vector_other = new Vector2(3,2);
		
		assertTrue("Vectors are equal if their x and y components are equal",vector.equals(vector_other));
		
	}
	
	@Test
	public void test_levelChange001() {
		
		Model model = new Model();
		
		assertTrue("First game loaded is the Main Menu", model.game instanceof MainMenu);
		
	}
	
	@Test
	public void test_levelChange002() {
		
		Model model = new Model();
		model.game.isPressed = true;
		model.game.hasWon = true;
		model.onTick();
		
		assertTrue("Second game loaded is the Maze Tutorial", model.game instanceof MazeTutorial);		
	}
	
	@Test
	public void test_levelChange003() {
		
		Model model = new Model();
		for(int i = 0; i < 2; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Third game loaded is the Maze Game", model.game instanceof ModelMazeGame);	
	}
	
	@Test
	public void test_levelChange004() {
		
		Model model = new Model();
		for(int i = 0; i < 3; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Fourth game loaded is the Counting Tutorial", model.game instanceof CountingTutorial);	
	}
	
	@Test
	public void test_levelChange005() {
		
		Model model = new Model();
		for(int i = 0; i < 4; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Fifth game loaded is the Counting Game", model.game instanceof ModelCountingGame);	
	}
	
	@Test
	public void test_levelChange006() {
		
		Model model = new Model();
		for(int i = 0; i < 5; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Sixth game loaded is the Shore Tutorial", model.game instanceof ShoreTutorial);	
	}
	
	@Test
	public void test_levelChange007() {
		
		Model model = new Model();
		for(int i = 0; i < 6; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Seventh game loaded is the Shore Game", model.game instanceof ModelShoreGame);	
	}
	
	@Test
	public void test_levelChange008() {
		
		Model model = new Model();
		for(int i = 0; i < 7; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("Eighth game loaded is the Final Screen", model.game instanceof FinalScreen);	
	}
	
	@Test
	public void test_levelChange009() {
		
		Model model = new Model();
		for(int i = 0; i < 8; i++){
			model.game.isPressed = true;
			model.game.hasWon = true;
			model.onTick();
		}
		
		assertTrue("After reaching the final screen, the next game loaded should be the main menu",
				model.game instanceof MainMenu);	
	}
	

	@Test
	public void test_MazeTutorial001(){
		MazeTutorial maze_tut = new MazeTutorial();
		
		maze_tut.secondsCounter = 7;
		maze_tut.onTick();
		
		assertFalse("When the seconds counter reaches 7, the tip visiblity should become false.", maze_tut.getTipVisibility());
		
		maze_tut.mazeCrab.position.x = 139; 
		maze_tut.onTick();
		
		assertEquals("Once the crab goes past x-coordinate 140, the current tip should change to SALINITY",
				maze_tut.getCurrentTip(),MazeTip.SALINITY);
		assertTrue("Once the crab goes past x-coordinate 140, the game should pause",
				maze_tut.isPaused);
		
		maze_tut.mazeCrab.position.y = 270;
		maze_tut.tipStartTime = 6;
		maze_tut.secondsCounter = 10;
		maze_tut.onTick();
		
		assertFalse("After 3 seconds have passed, the game should not be paused",
				maze_tut.isPaused);
		
		maze_tut.mazeCrab.position.y = 250;
		maze_tut.onTick();
		
		assertEquals("When the crab's position is less than [290,260], the currentTip should change to PREDATOR",
				maze_tut.getCurrentTip(),MazeTip.PREDATOR);
		
		maze_tut.mazeCrab.setPosition(new Vector2(410,160));
		maze_tut.onTick();
		
		assertEquals("When the crab's position.x is greater than 400 and position.y is less than 170, the currentTip should change to KELP",
				maze_tut.getCurrentTip(),MazeTip.KELP);
		
		maze_tut.mazeCrab.setPosition(new Vector2(470,40));
		maze_tut.onTick();
		assertEquals("When the crab's position.x is greater than 460 and position.y is less than 50, the currentTip should change to END",
				maze_tut.getCurrentTip(),MazeTip.END);
		
		maze_tut.mazeCrab.setPosition(new Vector2(470,-1));
		maze_tut.isPaused = false;
		maze_tut.isEndScreen();
		
		assertTrue("The game's hasWon should be true when the crab passes y-coordinate 0",
				maze_tut.hasWon);
		
		
	}
	
	@Test
	public void testCountingTutorial(){
		CountingTutorial count_tut = new CountingTutorial();
		
		assertEquals("The tutorial should begin on tip COUNT",count_tut.getCurrentTip(),CountingTip.COUNT);
		assertTrue("The tutorial should start with the tip visible.",count_tut.getTipVisibility());
		
		count_tut.secondsCounter = 7;
		count_tut.onTick();
		
		assertFalse("When the seconds counter reaches 7, the tip visiblity should become false.", count_tut.getTipVisibility());
		
		
		count_tut.mousePressed(new Vector2(351,251));
		count_tut.onTick();
		
		assertEquals("When the first horseshoecrab is found, the current tip should change to GLOW",
				CountingTip.GLOW,count_tut.getCurrentTip());
		
		count_tut.mousePressed(new Vector2(51,461));
		count_tut.onTick();
		
		assertEquals("When the second horseshoecrab is found, the current tip should change to BATTERY",
				CountingTip.BATTERY,count_tut.getCurrentTip());
		
		count_tut.mousePressed(new Vector2(611,441));
		count_tut.onTick();
		
		assertTrue("When the third horseshoecrab is found, the endscreen should appear, pausing the game",
				count_tut.isPaused);
	}
	
	@Test
	public void test_ShoreTutorial(){
		
		ShoreTutorial shore_tut = new ShoreTutorial(1);
		
		assertEquals("The shore tutorial should begin with the tip MOVEMENT.",
				ShoreTip.MOVEMENT, shore_tut.getCurrentTip());
		assertTrue("The game should begin with the tip visible.", shore_tut.getTipVisibility());
		
		shore_tut.secondsCounter = 7;
		shore_tut.onTick();
		
		assertFalse("The tip should not be visible after 6 seconds.",shore_tut.getTipVisibility());
		
		shore_tut.onTick();
		assertEquals("The tip should change to SEAWALL once the first tip is no longer visible"
				,ShoreTip.SEAWALL,shore_tut.getCurrentTip());
		
		shore_tut.shoreCrab.setPosition(new Vector2(0,ModelShoreGame.SHORE_LINE_POSITION));
		shore_tut.onWAction(true);
		
		shore_tut.onTick();
		
		assertEquals("The tip should change to SHORETHREAT when a wall is built.",
				ShoreTip.SHORETHREAT,shore_tut.getCurrentTip());
		
		shore_tut.shorethreats.remove(0);
		shore_tut.onTick();
		
		assertEquals("The tip should change to OYSTERS when the shorethreat leaves the screen.",
				ShoreTip.OYSTERS,shore_tut.getCurrentTip());
		
		shore_tut.oysterCount = 110;
		shore_tut.onTick();
		
		assertEquals("The tip should change to GABION when the oyster count is greater than 100",
				ShoreTip.GABION,shore_tut.getCurrentTip());
		
		shore_tut.gabions.add(new OysterGabion(new Vector2(0,0)));
		shore_tut.onTick();
		
		assertEquals("The tip should change to END when a gbaion is made.",
				ShoreTip.END,shore_tut.getCurrentTip());
		
		shore_tut.secondsCounter = 20;
		shore_tut.tipStartTime = 10;
		shore_tut.onTick();
		
		assertTrue("The game should pause when the final has been up for more than 6 seconds.",shore_tut.isPaused);
		
		shore_tut.hasWon = true;
		shore_tut.mousePressed(new Vector2());
		
		assertTrue("When the player has won and clicks, isPressed should become true.",shore_tut.isPressed);
	
		
	}
	
	@Test
	public void test_Entity(){
		Entity entity = new Entity(new Rectangle(new Vector2(),new Vector2(3,2)),new Animation(ImageHandler.Battery));
		
		assertEquals("Entity constructor sets rectangle dimension.",new Vector2(3,2),entity.hitbox.bottomRight);
		assertEquals("Entity constructor sets animation image index.",ImageHandler.Battery,entity.animation.imageIndex);
	}
	
	@Test
	public void test_FinalScreen(){
		FinalScreen finalScreen = new FinalScreen();
		
		boolean oldIsPressed = finalScreen.isPressed;
		finalScreen.init();
		finalScreen.isEndScreen();
		finalScreen.restart();
		finalScreen.handleCollisions();
		finalScreen.onLeftAction(true);
		finalScreen.onRightAction(true);
		finalScreen.onDownAction(true);
		finalScreen.onUpAction(true);
		finalScreen.onWAction(true);
		finalScreen.onGAction(true);
		finalScreen.initTimer();
		finalScreen.onTick();
		finalScreen.mouseReleased(null);
		assertEquals("All functions but mousePressed should not affect isPressed",oldIsPressed,finalScreen.isPressed);
		assertEquals("Calculate scrore should always return 0",0,finalScreen.calculateScore());
		
		finalScreen.mousePressed(new Vector2());
		assertTrue("A mouse press should set isPressed to true",finalScreen.isPressed);
	}
	
	@Test
	public void test_MainMenu(){
		MainMenu mainMenu = new MainMenu();
		
		boolean oldIsPressed = mainMenu.isPressed;
		mainMenu.init();
		mainMenu.isEndScreen();
		mainMenu.restart();
		mainMenu.handleCollisions();
		mainMenu.onLeftAction(true);
		mainMenu.onRightAction(true);
		mainMenu.onDownAction(true);
		mainMenu.onUpAction(true);
		mainMenu.onWAction(true);
		mainMenu.onGAction(true);
		mainMenu.initTimer();
		mainMenu.onTick();
		mainMenu.mouseReleased(null);
		assertEquals("All functions but mousePressed should not affect isPressed",oldIsPressed,mainMenu.isPressed);
		assertEquals("Calculate scrore should always return 0",0,mainMenu.calculateScore());
		
		mainMenu.mousePressed(new Vector2());
		assertTrue("A mouse press should set isPressed to true",mainMenu.isPressed);
	}
	
}
