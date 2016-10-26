package estuary;

import java.util.List;

import javax.swing.Timer;

public class ModelMazeGame implements Tickable, Game {

	public MazeCrab        mazeCrab;
	
	public List<KelpPlant> kelpPlants;
	public List<Barrier>   walls;
	public List<Predator>  predators;
	
	public Vector2         estuaryPoint;
	public int             secondsCounter;
	public Timer           secondsTimer;
	
	ModelMazeGame(int difficulty) {
		
	}
	
	ModelMazeGame(int difficulty, List<KelpPlant> kelpPlants, List<Barrier> walls, List<Predator> predators,
			Vector2 estuaryPoint) {
		
		this.kelpPlants   = kelpPlants;
		this.walls        = walls;
		this.predators    = predators;
		this.estuaryPoint = estuaryPoint;
	}
	
	@Override
	public void onTick() {
		
		handleCollisions();
		
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnd() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleCollisions() {
		// TODO Auto-generated method stub
		
	}
	
	
	double radiusFromEstuary(MazeCrab mazeCrab) {
		return mazeCrab.getPosition().distanceTo(estuaryPoint);
	}
	
	void removeKelpPlant(KelpPlant kelp) {
		kelpPlants.remove(kelp);
	}
	
	void spawnPredator(Vector2 position, Vector2 velocity) {
		Predator predator = new Predator(position, velocity);
		predators.add(predator);
	}
	
	void spawnKelp(Vector2 position) {
		KelpPlant kelp = new KelpPlant(position);
		kelpPlants.add(kelp);
	}

}
