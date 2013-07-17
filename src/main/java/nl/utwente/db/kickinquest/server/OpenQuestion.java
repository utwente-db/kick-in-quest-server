package nl.utwente.db.kickinquest.server;

import org.json.simple.parser.ParseException;

public class OpenQuestion extends Question {

	OpenQuestion(MyJSONRepository json) throws ParseException{
		super(json.getStringPath("type") + "/" + json.getStringPath("question"), json);
	}
}