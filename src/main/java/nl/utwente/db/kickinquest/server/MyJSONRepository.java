package nl.utwente.db.kickinquest.server;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyJSONRepository {
	
	private static boolean debug = false; 

	@SuppressWarnings("rawtypes")
	public Map topJSONMap = null;

	public Map topMap() {
		return (Map)topJSONMap;
	}
	
	@SuppressWarnings("rawtypes")
	public MyJSONRepository(Map topJSONMap) {
		this.topJSONMap = topJSONMap;
	}
	
	public Object getPath(String tag1) {
		Object res = topJSONMap.get(tag1);

		if (res != null)
			return res;
		return null;
	}

	public String getStringPath(String tag1) throws ParseException {
		Object res = topJSONMap.get(tag1);

		if (res != null)
			return res.toString();
		else {
			System.err.println("#!ERROR: JSON field \""+tag1+"\" missing");
			throw new ParseException(2);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Object getPath(String tag1, String tag2) {
		Object res = topJSONMap.get(tag1);

		if (res != null) {
			if (res instanceof Map) {
				res = ((Map) res).get(tag2);
				if (res != null)
					return res;
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Object getPath(String tag1, String tag2, String tag3) {
		Object res = topJSONMap.get(tag1);

		if (res != null) {
			if (res instanceof Map) {
				res = ((Map) res).get(tag2);
				if (res != null) {
					if (res instanceof Map) {
						res = ((Map) res).get(tag3);
						if (res != null)
							return res;
					}
				}
			}
		}
		return null;
	}
	
	public static final double obj2double(Object o) {
		if ( o instanceof Double )
			return ((Double)o).doubleValue();
		else if (o instanceof Long)
			return ((Long)o).doubleValue();
		else if (o instanceof Integer)
			return ((Integer)o).doubleValue();
		else
			throw new ClassCastException("cannot cast "+o+" to double");
		
	}
	
	private static JSONParser parser = new JSONParser();
	
	private static ContainerFactory containerFactory = new ContainerFactory() {
		@SuppressWarnings("rawtypes")
		public List creatArrayContainer() {
			return new LinkedList();
		}

		@SuppressWarnings("rawtypes")
		public Map createObjectContainer() {
			return new LinkedHashMap();
		}
	};

	public static synchronized MyJSONRepository getRepository(
			String json_encoded) throws ParseException {

		@SuppressWarnings("rawtypes")
		Map json = (Map) parser.parse(json_encoded, containerFactory);
		if (debug) {
			System.out.println("JSON=" + json_encoded);
			@SuppressWarnings("rawtypes")
			Iterator iter = json.entrySet().iterator();
			System.out.println("==JSON iterate result==");
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				System.out.println(entry.getKey() + "=>" + entry.getValue());
			}

			System.out.println("==toJSONString()==");
			System.out.println(JSONValue.toJSONString(json));
		}
		return new MyJSONRepository(json);
	}

}
