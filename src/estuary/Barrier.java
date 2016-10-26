package estuary;

public class Barrier extends Entity {

	Barrier(Rectangle box) {
		super(box.topLeft, box,
			  new Animation());
		
	}
}
