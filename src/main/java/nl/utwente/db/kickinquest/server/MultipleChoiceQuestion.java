package nl.utwente.db.kickinquest.server;

import org.json.simple.parser.ParseException;

public class MultipleChoiceQuestion extends Question {

	MultipleChoiceQuestion(MyJSONRepository json) throws ParseException{
		super(json.getStringPath("type") + "/" + json.getStringPath("question"), json);
	}
}
