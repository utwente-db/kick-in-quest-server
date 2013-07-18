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
	private static final String KI_ZIPDIR    = KI_BASEDIR + "/zip";

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
			 
			 Question sel20_questions[] = selectRandom(nl_questions, 3);

			 createOneZip("tz", sel20_questions);
			 
		} catch (Exception e) {
			System.err.println("#!Exception: " + e);
			e.printStackTrace();
		}
	}
	
	public void createOneZip(String name, Question sel20_questions[]) throws IOException, ParseException {
		// select at random 20 questions and order them for optimal travel
		// TODO, this should be done one level up
		Question opt20_questions[] = TravelingSalesman.getOptimalPath(sel20_questions);
		Question.clearOrderList(opt20_questions);
		for(int i=0; i<(opt20_questions.length-1); i++) {
			opt20_questions[i].setOrder(i, opt20_questions[i+1]);
		}
		// TODO: what is the first one
		// TODO: what to do about the order of the last one
		opt20_questions[opt20_questions.length-1].setOrder(opt20_questions.length-1, null);

		Zip zip = new Zip(KI_ZIPDIR, name);
		zip.set_info_json(INFO_JSON);
		zip.set_game_json( generate_game_json(opt20_questions, zip) );
		zip.generate();
	}
	
	@SuppressWarnings("unchecked")
	private String generate_game_json(Question q[], Zip zip) throws ParseException {
		
		
//		zip.add_image("im1.jpg", "xxx".getBytes());
//		zip.add_image("im2.jpg", "yyy".getBytes());
		
		LinkedHashMap top=new LinkedHashMap();
		JSONObject ti=new JSONObject();
		ti.put("THE_ANSWER_IS", "Het antwoord is");
		ti.put("TO_ANSWER", "Beantwoorden");
		ti.put("CORRECT", "GOED");
		ti.put("WRONG", "FOUT");ti.put("NEW_INFORMATION", "Je krijgt nieuwe informatie over het eindpunt.");
		ti.put("NEXT", "Volgende");
		ti.put("GO_TO_BUILDING", "Ga nu naar");
		ti.put("PUSH_BUTTON", "Druk op de knop hieronder als je <b>binnen</b> bent.");
		ti.put("PUSH_TOO_EARLY", "Eerder drukken levert strafpunten op!");
		ti.put("WE_ARE_INSIDE", "We zijn er!");
		ti.put("LOCATION_WILL_BE_CHECKED", "De opgegeven locatie zal worden gecontroleerd.<br/>Ga weer naar buiten.");
		ti.put("FOUND_COORDINATES", "We hebben de volgende coordinaten doorgekregen");
		ti.put("DISTANCE_TO_EXPECTED POINT", "De afstand tot het verwachte punt bedraagt");
		ti.put("NO_LOCATION_FOUND", "Er is geen locatie gevonden. Zorg ervoor dat de GPS sensor aanstaat.");
		ti.put("GAME_OVER", "Dit was de laatste vraag, het spel is afgelopen. Hartelijk dank voor het meedoen!");
		ti.put("CLICK_TO_CLOSE", "Klik hier om af te sluiten");
		ti.put("GPS_DISABLED", "Het GPS signaal is uitgeschakeld of nog niet ontvangen. Zet dit aan om verder te gaan, of wacht tot dit signaal weer beschikbaar is.");
		top.put("textItems", ti);
		for(int i=0; i<q.length; i++) {
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
