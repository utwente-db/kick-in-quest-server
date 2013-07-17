package nl.utwente.db.kickinquest.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public abstract class Question {

	public String id;
	protected double lat; // around  6.89 +/- 0.02
	protected double lon; // around 52.22 +/- 0.02
	protected MyJSONRepository json;
	
	public Question(String id, double lat, double lon, MyJSONRepository json) throws ParseException {
		this.id   = id;
		this.lat  = lat;
		this.lon  = lon;
		this.json = json;
	}
	
	public Question(String id, MyJSONRepository json) throws ParseException{
		this.id   = id;
		this.lat  = new Double(json.getStringPath("latitude")).doubleValue();
		this.lon  = new Double(json.getStringPath("longitude")).doubleValue();
		this.json = json;
	}
	
	public static Question createQuestion(MyJSONRepository json) throws ParseException {
		String type = json.getStringPath("type");
		// System.out.println("type = "+type);
		
		if ( type.equals("openQuestion") ) {
			return new OpenQuestion(json);
		} else if ( type.equals("enterBuilding") ) {
			return new EnterBuildingQuestion(json);
		} else if ( type.equals("multipleChoice") ) {
			return new MultipleChoiceQuestion(json);
		} else
			return null;
	}
	
	public static Question[] readQuestions(String dir) throws IOException {
		String[] files = OsUtils.ls(dir, true);

		if (files != null) {
			try {
				Question res[] = new Question[files.length];
				
				for (int i = 0; i < files.length; i++) {
					// do some namecheck ??
					File file = new File(files[i]);

					byte[] arBytes = new byte[(int) file.length()];
					FileInputStream is = new FileInputStream(file);
					is.read(arBytes);

					MyJSONRepository jrep;
					
					try {
						jrep = MyJSONRepository.getRepository(new String(arBytes));
						// System.out.println("#!JSON file read: " + files[i]);
						Question q = createQuestion(jrep);
						res[i] = q;
						System.out.println("#!READ:"+res[i]+"\n"+JSONValue.toJSONString(res[i].json.topMap()));
					} catch (ParseException e) {
						System.err.println("#!ERROR IN FILE: " + files[i]);
						System.err.println(new String(arBytes));
						System.err.println("#!PARSE EXCEPTION=" + e);
						return null;
					}
				}
				return res;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
			try {
				res[i] = new RandomQuestion("ensc_question"+i);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("- Generated: "+res[i]);
		}
		return res;
	}
	
	private int	    order = -1;
	private Question	next_in_line = null;
	
	public void clearOrder() {
		setOrder(-1,null);
	}
	
	boolean hasOrder() {
		return order > -1;
	}
	
	public void setOrder(int n, Question next_in_line) {
		this.order = n;
		this.next_in_line = next_in_line;
	}
	
	public static void clearOrderList(Question l[]) {
		for(int i=0; i<l.length; i++)
			l[i].clearOrder();
	}
	
	public String toString() {
		return "Question(id="+id+",lat="+lat+",lon="+lon+")";
	}
	
}
