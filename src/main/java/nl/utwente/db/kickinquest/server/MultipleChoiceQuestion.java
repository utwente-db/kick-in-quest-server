package nl.utwente.db.kickinquest.server;

import java.util.LinkedHashMap;

import org.json.simple.parser.ParseException;

public class MultipleChoiceQuestion extends Question {

	MultipleChoiceQuestion(MyJSONRepository json) throws ParseException{
		super(json.getStringPath("type") + "/" + json.getStringPath("question"), json);
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap get_game_json(Zip zip) throws ParseException  {
		LinkedHashMap res = new LinkedHashMap();
		
		res.put("type", json.getStringPath("type"));
		res.put("question", json.getStringPath("question"));
		
		String image = json.getStringPath("image");
		res.put("image", image);
		zip.handle_image(ZipCreationManager.KI_IMAGES, image);
		res.put("answers", json.getObjectPath("answers"));

		String correct = json.getStringPath("correctAnswer");
		res.put("correctAnswer", getMD5(order_str() + "*" + correct));		
		
		res.put("explanation", json.getStringPath("explanation"));

		res.put("latitude", json.getStringPath("latitude"));
		res.put("longitude", json.getStringPath("longitude"));

		res.put("reward", getReward());
		
		return res;
	}
}
