package nl.utwente.db.kickinquest.server;

import java.util.Random;

public class  Question {

	public String id;
	protected double lat; // around  6.89 +/- 0.02
	protected double lon; // around 52.22 +/- 0.02
	
	public Question(String id, double lat, double lon) {
		this.id  = id;
		this.lat = lat;
		this.lon = lon;
	}
	
	// Question enschede[] = Question.randomQuestions(pnumcities);
	// this.matrix[i][j]=enschede[i].distanceMeters(enschede[j]);
	
	private static final double radius = 0.02;
	private static final double double_radius = 2 * radius;
	
	private static final double lat_min = 6.89 - radius;
	private static final double lon_min = 52.22 - radius;
	
	private static Question randomQuestion(String id) {
		Random generator = new Random();

		return new Question(id, 
				lat_min + (generator.nextDouble() * double_radius), 
				lon_min + (generator.nextDouble() * double_radius));

	}
	
	public int distanceMeters(Question q) {
		double d1 = lat - q.lat;
		double d2 = lon - q.lon;
		d1 *= d1;
		d2 *= d2;
		double dsq = Math.sqrt(d1+d2);
		int res = (int)(dsq*150000.0);
		// System.out.println("DIST="+res);
		return res;
	}
	
	public static Question[] randomQuestions(int n) {
		Question res[] = new Question[n];
		
		for(int i=0; i<n; i++) {
			res[i] = randomQuestion("ensc_question"+i);
			System.out.println("- Generated: "+res[i]);
		}
		return res;
	}
	
	public String toString() {
		return "Question(id="+id+",lat="+lat+",lon="+lon+")";
	}
	
	
	
}
