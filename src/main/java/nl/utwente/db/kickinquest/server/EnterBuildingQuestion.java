package nl.utwente.db.kickinquest.server;

import java.util.LinkedHashMap;

import org.json.simple.parser.ParseException;

public class EnterBuildingQuestion extends Question {

	EnterBuildingQuestion(MyJSONRepository json) throws ParseException{
		super(json.getStringPath("type") + "/" + json.getStringPath("building"), json);
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap get_game_json(Zip zip) throws ParseException  {
		LinkedHashMap res = new LinkedHashMap();
		
		res.put("type", json.getStringPath("type"));
		
		res.put("building", json.getStringPath("building"));
		String image = json.getStringPath("image");
		zip.handle_image(ZipCreationManager.KI_IMAGES, image);
		res.put("image", image);
		res.put("latitude", json.getStringPath("latitude"));
		res.put("longitude", json.getStringPath("longitude"));
		
		res.put("reward", getReward());
		
		return res;
	}
	
}
