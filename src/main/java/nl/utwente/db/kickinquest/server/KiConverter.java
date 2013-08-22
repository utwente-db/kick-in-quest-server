package nl.utwente.db.kickinquest.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class KiConverter {
	
	public static void convert(String filename, String basedir) throws IOException {

		String l;
		BufferedReader in = new BufferedReader(new FileReader(filename));

		/*
		 * INCOMPLETE:
		 * - correct answer A,B,C,D sugg prefix answer
		 * - vuurwerkramp coord
		 * - voorwerkramp english
		 * - images
		 * - strange chars
		 * - gedicht willem wilmink newlines
		 */
		while ((l = in.readLine()) != null) {
			int cp = 0;
			
			while ( Character.isDigit(l.charAt(cp)) )
					cp++;
			int qnumber = Integer.parseInt(l.substring(0,cp));
			System.out.println("@@ NUMBER="+qnumber);
			while ( l.charAt(cp) != '(' )
				cp++;
			l = l.substring(cp);
			char lang = 0;
			if ( l.startsWith("(NE)"))
				lang = 'd';
			else if ( l.startsWith("(EN)"))
				lang = 'e';
			else
				throw new RuntimeException("Unexpected!"+l);
			System.out.println("@@ LANGUAGE="+lang);
			l = l.substring(l.indexOf("\t")+1);
			cp = l.indexOf('\t');
			String adres = l.substring(0,cp);
			System.out.println("@@ ADRES="+adres);
			l = l.substring(cp+1);
			cp = l.indexOf(',');
			String coord1 = l.substring(0,cp);
			while( !Character.isDigit(l.charAt(cp))) cp++;
			l = l.substring(cp);
			cp = l.indexOf('\t');
			String coord2 = l.substring(0,cp);
			System.out.println("@@ COORD["+coord1+","+coord2+"]");
			l = l.substring(cp+1);
			cp = l.indexOf('\t');
			String question = l.substring(0,cp);
			System.out.println("@@ QUESTION="+question);
			l = l.substring(cp+1);
			
			if ( lang != 'e') {
				System.out.println("QQ "+qnumber+" "+adres+" "+question+"\n");
			}
			/*
			 * 
			 */
			JSONObject json=new JSONObject();
			String explanation = null, correct = null;
			if ( l.startsWith("A.") ) {
				json.put("type", "multipleChoice");
				String a1 = l.substring(3,l.length());
				System.out.println("@@ A1="+a1);
				l = in.readLine();
				String a2 = l.substring(3,l.length());
				System.out.println("@@ A2="+a2);
				l = in.readLine();
				String a3 = l.substring(3,l.length());
				System.out.println("@@ A3="+a3);
				l = in.readLine();
				cp = l.indexOf("\t");
				String a4 = l.substring(3,cp);
				System.out.println("@@ A4="+a4);
				l = l.substring(cp+1);
				if ( l.charAt(1) != '.' )
					throw new RuntimeException("NO CORRECT ANSWER PREFIX");
				correct = l.substring(0,1);
				explanation = l.substring(2);
				System.out.println("@@ CORRECT="+correct);
				System.out.println("@@ EXPL="+explanation);
				// incomplete, this is the text, find out the number
				JSONObject answers=new JSONObject();
				answers.put("A",a1);
				answers.put("B",a2);
				answers.put("C",a3);
				answers.put("D",a4);
				json.put("answers", answers);
			} else {
				json.put("type", "openQuestion");
				cp = l.indexOf("\t");
				correct = l.substring(0,cp);
				explanation = l.substring(cp+1);
				System.out.println("@@ CORRECT="+correct);
				System.out.println("@@ EXPL="+explanation);
			}
			json.put("number",""+qnumber);
			json.put("language",(lang=='e'?"english":"nederlands"));
			json.put("question", question);
			json.put("correctAnswer", correct);
			json.put("explanation",explanation);
			json.put("image", "images/q"+qnumber+".jpg");
			json.put("latitude", coord1);
			json.put("longitude", coord2);
			//
			String str_json = json.toJSONString();
			System.out.println(str_json);
			
			//
			String outFile = basedir + (lang=='e'?"en/":"nl/") + "q"+qnumber+".json";
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
            out.write(str_json);
            out.close();
		} // end while

	}
		
	public static void main(String[] args) {
		try {
		convert("/Users/flokstra/kick-in/kinput.txt","/Users/flokstra/kick-in/data/questions/");
		} catch (Exception e) {
			System.err.println("Error converting file: " + e);
			e.printStackTrace();
		}
	}
	
}
