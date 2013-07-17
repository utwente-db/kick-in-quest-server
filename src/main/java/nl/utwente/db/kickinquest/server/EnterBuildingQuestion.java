package nl.utwente.db.kickinquest.server;

import org.json.simple.parser.ParseException;

public class EnterBuildingQuestion extends Question {

	EnterBuildingQuestion(MyJSONRepository json) throws ParseException{
		super(json.getStringPath("type") + "/" + json.getStringPath("building"), json);
	}
}
