package nl.utwente.db.kickinquest.server;

import java.io.IOException;
import java.util.Random;

public class ZipCreationManager {

	private static final String ZIP_DESTINATION = "/Users/flokstra/tmp/testzip";
	
	private static final String NL_QUESTIONS = "/Users/flokstra/kick-in/data/questions/nl";
	private static final String EN_QUESTIONS = "/Users/flokstra/kick-in/data/questions/en";

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
			 
			 createOneZip("tz", nl_questions, 3);
			 
		} catch (IOException e) {
			System.err.println("#!Exception: " + e);
			e.printStackTrace();
		}
	}
	
	public void createOneZip(String name, Question qfrom[], int nq) throws IOException {
		// select at random 20 questions and order them for optimal travel
		Question sel20_questions[] = selectRandom(nl_questions, nq);
		Question opt20_questions[] = TravelingSalesman.getOptimalPath(sel20_questions);
		Question.clearOrderList(opt20_questions);
		for(int i=0; i<(opt20_questions.length-1); i++) {
			opt20_questions[i].setOrder(i, opt20_questions[i+1]);
		}
		// TODO: what is the first one
		// TODO: what to do about the order of the last one

		Zip zip = new Zip(ZIP_DESTINATION, name);
		zip.set_info_json(INFO_JSON);
		zip.set_game_json( generate_game_json(opt20_questions, zip) );
		zip.generate();
	}
	
	private String generate_game_json(Question q[], Zip zip) {
		StringBuilder res = new StringBuilder();
		
		zip.add_image("im1.jpg", "xxx".getBytes());
		zip.add_image("im2.jpg", "yyy".getBytes());
		res.append("THE CONTENT OF GAME JSON");
		return res.toString();
	}
	
	protected Question[] selectRandom(Question l[], int n) {
		Random rand = new Random();
		
		if ( n > l.length )
			throw new RuntimeException("selectRandom: n > l.length");
		Question res[] = new Question[n];
		for (int i=0; i<n; i++) {
			while ( res[i] == null) {
				int guess = (int) (rand.nextDouble() * n);
				
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
	}
	
	private static final String INFO_JSON = 
		"{\n" +
		"    \"1\": {\n" +"        \"infoText\": \"Om mee te doen aan dit spel, hebben wij jouw goedkeuring nodig voor het opnemen van het GPS signaal.<br\\/> Deze informatie zal ook gebruikt worden in een onderzoek naar GPS analyse aan de UT.<br\\/> Hierbij zullen geen gegevens worden doorgegeven waaruit te achterhalen is van wie welk signaal is.\",\n" +
		"        \"buttonText\": \"Ik stem toe\"\n" +"    },\n" +"    \"2\": {\n" +"        \"infoText\": \"Je kunt nu beginnen aan het spel.<br/> Eerst krijgen jullie een aantal vragen, waarbij je de stad leert kennen. Elk goed antwoord levert informatie op over het eindpunt.\",\n" +
		"        \"buttonText\": \"Lees verder...\"\n" +"    },\n" +"    \"3\": {\n" +"        \"infoText\": \"Om dit spel te winnen, moet je het hoogste aantal <b>goede antwoorden</b> hebben. Daarnaast moet je <b>geen strafpunten</b> oplopen. Bij een gelijk aantal punten, telt de <b>snelste tijd</b>.\",\n" +
		"        \"buttonText\": \"Naar de eerste vraag\"\n" +
		"    }\n" +
		"}";

	
}
