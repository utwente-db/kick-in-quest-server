package nl.utwente.db.kickinquest.server;

import java.util.Random;

import org.json.simple.parser.ParseException;

class RandomQuestion extends Question {

	protected static final double radius = 0.02;
	protected static final double double_radius = 2 * radius;
	
	protected static final double lat_min = 6.89 - radius;
	protected static final double lon_min = 52.22 - radius;
	
	RandomQuestion(String id) throws ParseException {
		super(id, lat_min + (new Random().nextDouble() * double_radius),
				lon_min + (new Random().nextDouble() * double_radius), null);

	}
}
