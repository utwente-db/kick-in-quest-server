package nl.utwente.db.kickinquest.server;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class ZipCreationManager {

	// private static final String ZIP_DESTINATION = "/Users/flokstra/tmp/testzip";
	
	private static final String KI_BASEDIR    = "/Users/flokstra/kick-in";

	private static final String NL_QUESTIONS = KI_BASEDIR + "/data/questions/nl";
	private static final String EN_QUESTIONS = KI_BASEDIR + "/data/questions/en";
	public static final  String KI_IMAGES    = KI_BASEDIR + "/data";
	// private static final String KI_ZIPDIR    = KI_BASEDIR + "/zip";
	private static final String KI_ZIPDIR    = KI_BASEDIR + "/tomcat";
	private static final String KI_PROPDIR   = KI_BASEDIR + "/tomcat/properties";

	private Question nl_questions[];
	private Question en_questions[];
	
	ZipCreationManager() {
		try {
			 nl_questions =
			 Question.readQuestions(NL_QUESTIONS);
			 en_questions =
			 Question.readQuestions(EN_QUESTIONS);
			 if ( nl_questions == null || en_questions == null )
				 throw new RuntimeException("FAIL TO READ QUESTIONS");
			 
			if ( false ) {
				startTeams();
				while ( moreTeams() ) {
					System.out.println(nextTeam());
				}
			}
			if ( false ) {
				Question sel15_questions[] = selectRandom(nl_questions, 15);
				createTeamZips("AAA15", sel15_questions, -1);
			}
			if ( true ) {
				int trail15[] = {18,16,2,10,17,5,4,1,3,22,13,14,15,21,11,12,24,6,7,9};
				createTeamZips("AAA15", trail15, 20);
				// createTeamZips("AAA16", reverse_array(trail15));
			}
			if ( false ) {
				int trails[][] = {
						{18,16,2,10,17,5,4,1,3,22,13,14,15,21,11,12,24,6,7,9},		// 1
						{8,23,20,11,19,15,21,14,13,12,24,22,2,1,6,17,4,5,10,18},	// 2	
						{7,4,17,10,20,21,19,16,23,6,1,2,22,12,24,11,15,14,13,9},	// 3
						{23,11,14,15,19,16,20,10,6,24,13,12,22,5,4,17,3,2,7,8},		// 4
						{ 7,17,5,4,1,3,6,10,20,24,12,13,14,11,16,19,21,15,23,8},	// 5
						{18,5,4,1,3,22,6,17,20,7,24,12,13,14,15,19,16,11,23,8}, 	// 6
						{9,14,15,21,19,16,11,13,12,24,20,10,6,22,1,3,17,5,4,8},		// 7
						{18,19,21,14,15,11,13,8,24,12,22,6,10,17,3,2,4,5,20,23},	// 8
						{23,24,22,4,5,17,1,3,6,10,20,19,21,15,14,13,12,11,9,7},		// 9
						{9,14,22,13,12,2,3,4,17,10,6,24,7,20,16,19,21,15,11,23},	// 10
						{18,20,16,19,15,14,11,13,22,12,24,8,6,1,3,4,5,17,10,7},		// 11
						{8,18,24,6,1,2,3,4,5,22,12,13,14,15,21,19,16,11,23,9},		// 12
						{8,23,19,21,16,20,10,17,5,22,13,14,15,12,4,2,3,6,7,18},		// 13
						{18,8,11,19,16,20,6,24,12,13,14,15,21,10,17,5,4,1,2,7},		// 14
						{8,18,16,19,21,17,4,22,13,14,15,20,10,6,3,1,12,24,9,23},	// 15
						{23,18,10,17,4,1,2,3,6,20,16,19,14,15,21,11,12,22,24,8},	// 16
						{8,18,20,10,6,24,9,16,19,21,17,4,2,3,22,13,14,15,11,23},	// 17
						{9,14,15,13,22,12,24,8,19,16,21,20,10,17,4,1,3,6,7,23},		// 18
						{7,6,2,1,4,5,17,22,12,24,10,20,16,21,15,14,13,11,9,23},		// 19
						{23,11,15,14,13,12,24,22,6,18,19,21,16,2,3,4,17,20,7,8}		// 20
				};
				startTeams();
				while ( moreTeams() ) {
					for (int i = 0; (i < trails.length) && moreTeams(); i++) {
						int trail[] = trails[i];

						createTeamZips(nextTeam(), trail, 20);
						if ( moreTeams() )
							createTeamZips(nextTeam(), reverse_array(trail), 20);
					}
				}
			}
			 
		} catch (Exception e) {
			System.err.println("#!Exception: " + e);
			e.printStackTrace();
		}
	}
	
	public static int[] reverse_array(int[] data_src) {
		int data[] = data_src.clone();
	    for (int left = 0, right = data.length - 1; left < right; left++, right--) {
	        // swap the values at the left and right indices
	        int temp = data[left];
	        data[left]  = data[right];
	        data[right] = temp;
	    }
	    return data;
	}
	
	private static Question[] get_questions(int indices[], Question src[]) {
		Question res[] = new Question[indices.length];
		
		for(int i=0; i<indices.length; i++) {
			int number  = indices[i];
			for(int j=0; j<src.length; j++) {
				if ( src[j].number == number ) {
					res[i] = src[j];
					break;
				}
			}
		}
		return res;
	}
	
	private static final String prefix = "AA";
	private static final char cfrom = 'A';
	private static final char cto = 'K'; // must be 'K'
	private static final int ifrom = 1;
	private static final int ito = 17;
	
	private char cc;
	private int ci;
	private boolean moreTeams;
	
	void startTeams() {
		this.moreTeams = true;
		this.cc = cfrom;
		this.ci = ifrom;
	}
	
	boolean moreTeams() {
		return moreTeams;
	}
	
	String nextTeam() {
		String res = prefix + cc + (ci<10?"0":"")+ ci;
		if ( ++ci >= ito ) {
			ci = ifrom;
			if ( ++cc >= cto )
				moreTeams = false;
		}
		return res;
	}
	public void createTeamZips(String name, Question sel20_questions[], int N) throws IOException, ParseException {
		// select at random 20 questions and order them for optimal travel
		// TODO, this should be done one level up
		Question opt20_questions[] = TravelingSalesman.getOptimalPath(sel20_questions);
		int indices[] = new int[sel20_questions.length];
		for(int i=0; i<indices.length; i++)
			indices[i] = opt20_questions[i].number;
		createTeamZips(name,indices, -1);
	}
	
	public void createTeamZips(String name, int indices[], int N) throws IOException, ParseException {
		// select at random 20 questions and order them for optimal travel
		// TODO, this should be done one level up
		Question questions[] = get_questions(indices, nl_questions);
		Question.clearOrderList(questions);
		StringBuilder propBuff = new StringBuilder();
		for(int i=0; i<questions.length; i++) {
			if ( N > 0 ) {
				if (indices.length != N)
					throw new RuntimeException("BAD N: "+indices.length);
				for(int j=0; j<indices.length; j++) {
					if ( (i!=j) && indices[i]==indices[j] )
						throw new RuntimeException("DUPLICATE INDEX: "+indices[i]);
					
				}
			}
			if ( i < (questions.length-1) )
				questions[i].setOrder(i, questions[i+1]);
			propBuff.append(""+(i+1)+"="+"q"+questions[i].number+".json"+"\n");
		}
		OsUtils.createFile(KI_PROPDIR, name+".properties", propBuff.toString().getBytes());
		questions[questions.length-1].setOrder(questions.length-1, null);

		Zip zip = new Zip(KI_ZIPDIR, name);
		String language;
		
		language = "nl";
		zip.set_info_json(INFO_JSON_NL);
		zip.set_game_json( generate_game_json(questions, zip, language) );
		zip.generate(language);
		//
		language = "en";
		zip.set_info_json(INFO_JSON_EN);
		questions = get_questions(indices, en_questions);
		for(int i=0; i<(questions.length-1); i++)
			questions[i].setOrder(i, questions[i+1]);
		questions[questions.length-1].setOrder(questions.length-1, null);
		zip.set_game_json( generate_game_json(questions, zip, language) );
		zip.generate(language);
	}
	
	@SuppressWarnings("unchecked")
	private String generate_game_json(Question q[], Zip zip, String language) throws ParseException {
		LinkedHashMap top=new LinkedHashMap();
		JSONObject ti=new JSONObject();
		if (language.equals("nl")) {
			ti.put("THE_ANSWER_IS", "Het antwoord is");
			ti.put("TO_ANSWER", "Beantwoorden");
			ti.put("CORRECT", "GOED");
			ti.put("WRONG", "FOUT");
			ti.put("NEW_INFORMATION",
					"Je krijgt nieuwe informatie over het eindpunt:");
			ti.put("NO_NEW_INFORMATION",
					"Je krijgt geen nieuwe informatie over het eindpunt:");
			ti.put("NEXT", "Volgende");
			ti.put("GO_TO_BUILDING", "Ga nu naar");
			ti.put("PUSH_BUTTON",
					"Druk op de knop hieronder als je <b>binnen</b> bent. Denk eraan: hoe dichter bij je bent, hoe meer punten je krijgt.");
			ti.put("WE_ARE_INSIDE", "We zijn er!");
			ti.put("LOCATION_WILL_BE_CHECKED",
					"De opgegeven locatie zal worden gecontroleerd.<br/>Ga weer naar buiten.");
			ti.put("FOUND_COORDINATES",
					"We hebben de volgende coordinaten doorgekregen");
			ti.put("DISTANCE_TO_EXPECTED POINT",
					"De afstand tot het verwachte punt bedraagt");
			ti.put("GAME_OVER",
					"Dit was de laatste vraag, het spel is afgelopen. Hartelijk dank voor het meedoen!");
			ti.put("CLICK_TO_CLOSE",
					"Klik hier om de antwoorden te uploaden en af te sluiten");
			ti.put("GPS_DISABLED",
					"Er is geen locatie gevonden. Zorg ervoor dat de GPS sensor aanstaat, en ga eventueel even naar buiten.");
			ti.put("NO_LOCATION_FOR_ONE_MINUTE",
					"Er is al een minuut geen nieuwe GPS locatie gevonden. Zorg dat de GPS sensor aanstaat, dit is makkelijk punten verdienen.");
			ti.put("UNABLE_TO_CLOSE_APP", "De app kan nog niet gesloten worden");
			ti.put("NO_INTERNET_AVAILABLE",
					"Er is geen internetverbinding beschikbaar");
			ti.put("QUESTION", "Vraag");
		} else {
			ti.put("THE_ANSWER_IS", "The answer is");
			ti.put("TO_ANSWER", "Answer");
			ti.put("CORRECT", "CORRECT");
			ti.put("WRONG", "WRONG");
			ti.put("NEW_INFORMATION",
					"You get new information on the end point:");
			ti.put("NO_NEW_INFORMATION",
					"You do not get new information on the end point:");
			ti.put("NEXT", "Next");
			ti.put("GO_TO_BUILDING", "Go to");
			ti.put("PUSH_BUTTON",
					"Push the button below when you are <b>inside</b>. Remember: the closer you are, the more points you get.");
			ti.put("WE_ARE_INSIDE", "We are there!");
			ti.put("LOCATION_WILL_BE_CHECKED",
					"The given location will be checked.<br/>Go outside again.");
			ti.put("FOUND_COORDINATES",
					"We have received the following coordinates");
			ti.put("DISTANCE_TO_EXPECTED POINT",
					"The distance to the expected point is");
			ti.put("GAME_OVER",
					"This was the last question, the game is over. Thanks for playing!");
			ti.put("CLICK_TO_CLOSE",
					"Click here to upload the answers and close the app");
			ti.put("GPS_DISABLED",
					"No location has been found. Make sure the GPS sensor is on, and make sure you have been outside.");
			ti.put("NO_LOCATION_FOR_ONE_MINUTE",
					"No location has been found in the past minute. Make sure the GPS sensor is on, this is an easy way to earn points.");
			ti.put("UNABLE_TO_CLOSE_APP", "The app cannot be closed yet");
			ti.put("NO_INTERNET_AVAILABLE",
					"There is no internet connection available");
			ti.put("QUESTION", "Question");
		}
		top.put("textItems", ti);
		for(int i=0; i<q.length; i++) {
			Question sq = q[i];
			top.put(q[i].order_str(), q[i].get_game_json(zip));
		}
		if ( true )
			System.out.println(JSONValue.toJSONString(top));
		return JSONValue.toJSONString(top);
	}
	
	protected Question[] selectRandom(Question l[], int n) {
		Random rand = new Random();
		
		if ( n > l.length )
			throw new RuntimeException("selectRandom: n > l.length");
		Question res[] = new Question[n];
		for (int i=0; i<n; i++) {
			while ( res[i] == null) {
				int guess = (int) (rand.nextDouble() * (l.length));
				
				if ( !l[guess].hasOrder()  ) {
					res[i] = l[guess];
					l[guess].setOrder(i,null);
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) {
		new ZipCreationManager();
//		FileUtils.getFileAsString(file)
	}
	
	private static final String INFO_JSON_NL = "{\n \"1\": {\n \"infoText\": \"Om mee te doen aan dit spel, hebben wij jouw goedkeuring nodig voor het opnemen van het GPS signaal.<br\\/> Deze informatie zal ook gebruikt worden in wetenschappelijk onderzoek naar GPS analyse.<br\\/> Hierbij zullen geen gegevens worden doorgegeven waaruit te achterhalen is van wie welk signaal is.\",\n \"buttonText\": \"Ik stem toe\"\n },\n \"2\": {\n \"infoText\": \"Je kunt nu beginnen aan het spel.<br/> Eerst krijgen jullie een aantal vragen, waarbij je de stad leert kennen. Elk goed antwoord levert informatie op over het eindpunt.\",\n \"buttonText\": \"Lees verder...\"\n },\n \"3\": {\n \"infoText\": \"Om dit spel te winnen, moet je het hoogste aantal punten behalen. Bij elke vraag hoort een locatie. Punten kunnen behaald worden door vragen <b>dicht mogelijk</b> bij die locatie <b>goed</b> te beantwoorden. Een vraag over de Oude Markt moet dus beantwoord worden <b>op</b> de Oude Markt, ook als je het antwoord al weet.\",\n \"buttonText\": \"Lees verder...\"\n },\n \"4\": {\n \"infoText\": \"Verder krijg je &eacute;&eacute;n punt per doorgegeven GPS co&ouml;rdinaat.\",\n \"buttonText\": \"Lees verder...\"\n },\n \"5\": {\n \"infoText\": \"Let op: bij het gebruik van <b>meerdere telefoons</b> worden de punten <b>opgeteld</b>. Laat dus zo veel mogelijk teamleden de app gebruiken.\",\n \"buttonText\": \"Lees verder...\"\n },\n \"6\": {\n \"infoText\": \"Bij een gelijk aantal punten, telt de <b>snelste tijd</b>.\",\n \"buttonText\": \"Naar de eerste vraag\"\n }\n }\n";
	private static final String INFO_JSON_EN = "{\n \"1\": {\n \"infoText\": \"To participate in this game, we need your approval to record your GPS signal.<br\\/> This information will also be used in scientific research on trajectory analysis.<br\\/>No data will be shared from which can be detected which signal belongs to whom.\",\n \"buttonText\": \"I accept\"\n },\n \"2\": {\n \"infoText\": \"You can now start the game.<br/>First you will get some questions, which you will let you get to know the town. Every correct answer will be rewarded with information on the end point.\",\n \"buttonText\": \"More...\"\n },\n \"3\": {\n \"infoText\": \"To win this game, you need to have the highest number of points. Each question belongs to a location. Points can be obtained by answering questions <b>right</b>, and <b>as close as possible</b> to that location. A question about the Oude Markt should therefore be answered <b>at</b> the Oude Markt, even if you know the answer without going there.\",\n \"buttonText\": \"More...\"\n },\n \"4\": {\n \"infoText\": \"Furthermore, you will be awarded one point per GPS coordinate.\",\n \"buttonText\": \"More...\"\n },\n \"5\": {\n \"infoText\": \"Mind you: when a team uses multiple devices, the number of points will be <b>added up</b>. We therefore recommend you to use as many devices as possible.\",\n \"buttonText\": \"More...\"\n },\n \"6\": {\n \"infoText\": \"In case of a tie, the <b>fastest team</b> will be appointed as the winner.\",\n \"buttonText\": \"To the first question\"\n }\n }\n";
	

	
}
