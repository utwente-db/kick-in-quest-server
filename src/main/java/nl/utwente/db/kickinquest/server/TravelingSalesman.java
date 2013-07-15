package nl.utwente.db.kickinquest.server;

import java.util.List;


public class TravelingSalesman {
	
	public static final boolean verbose = false;
	
	public static void main(String[] args) {		
		int n = 20;
		
		Question ql[] = Question.randomQuestions(n);
		Question opt[] = getOptimalPath(ql);
		//
		if ( verbose ) {
			System.out.print("OPTIMAL TOUR[");
			for (int i = 0; i < opt.length; i++)
				System.out.print(" " + opt[i].id);
			System.out.println("]");
		}
	}
	
	public static Question[] getOptimalPath(Question ql[]) {
		int i;
		int matrix[][] = new int[ql.length][ql.length];
		
		for(i=0; i<ql.length; i++) {
			for(int j=0; j<ql.length; j++) {
				matrix[i][j]=ql[i].distanceMeters(ql[j]);
				matrix[j][i]=ql[i].distanceMeters(ql[j]);
			}
		}
		CleverTSP tsp = new CleverTSP(matrix);
		if ( verbose )
			tsp.printCostMatrix(matrix);
		tsp.generateSolution();
		if ( true ) {
			System.out.println("OPTIMAL:\n"+tsp.printOptimalTour());
			System.out.println("Tour Length = "+tsp.optimalTourLength);
		}
		
		Question[] res = new Question[ql.length];
		List<Integer> tour = tsp.getOptimalTour();
		for (i = 0; i < (tour.size() - 1); i++)
			res[i] = ql[tour.get(i)];
		return res;
	}

}
