package estuary;

public class KelpPlant extends Entity {

	public KelpPlant(Vector2 position) {
		super(position,
			    new Rectangle(position, 10.0, 10.0),
				new Animation()
		);

	}

}
