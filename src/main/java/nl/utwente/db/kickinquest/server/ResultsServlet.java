package nl.utwente.db.kickinquest.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.utwente.db.neogeo.utils.WebUtils;

public class ResultsServlet extends HttpServlet {
	
    public final static String ROOT_FOLDER = "/data/tomcat/";
	private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	Map<String, Integer> resultMap = new HashMap<String, Integer>();
    	PrintWriter responseWriter = response.getWriter();
    	response.setContentType("text/html");
    	
    	for (int i = 1; i <= 16; i++) {
    		String serverNumber = (i < 10 ? "0" : "") + i;
    		String getScoreURL = "http://farm" + serverNumber + ".ewi.utwente.nl:8080/kick-in-quest-server/GetScore";
    		String scores = "";
    		
    		try {
    			scores = WebUtils.getContent(getScoreURL);
    		} catch (Exception e) {
    			responseWriter.write("Unable to reach server: " + getScoreURL + "<br/>");
    			continue;
    		}
    		
    		for (String score : scores.split(";")) {
    			if ("".equals(score)) {
    				continue;
    			}
    			
    			String[] scorePair = score.split("=");
    			resultMap.put(scorePair[0], Integer.valueOf(scorePair[1]));
    		}
    	}
    	
    	List<Entry<String, Integer>> resultList = new ArrayList<Entry<String, Integer>>(resultMap.entrySet());
    	Collections.sort(resultList, new ResultComparator());
    	
    	int i = 1;
    	
    	responseWriter.write("<html><head><title>Kick-In Quest Results</title>" +
    			"<link type=\"text/css\" href=\"css/stylesheet.css\" rel=\"stylesheet\"/></head>" +
    			"<body><table id=\"resultsTable\" cellspacing=\"0\"><thead><tr><td>#</td><td>Team ID</td><td>Score</td></tr></thead><tbody>");
    	
    	for (Entry<String, Integer> resultItem : resultList) {
    		responseWriter.write("<tr><td>" + i++ + ".</td><td>" + resultItem.getKey() + "</td><td>" + resultItem.getValue() + "</td></tr>");
    	}
    	
    	responseWriter.write("</tbody></table></body></html>");
    }
    
    public class ResultComparator implements Comparator<Entry<String, Integer>> {

		public int compare(Entry<String, Integer> arg0, Entry<String, Integer> arg1) {
			if (arg0.getValue() > arg1.getValue()) {
				return -1;
			} else if (arg0.getValue() < arg1.getValue()) {
				return 1;
			} else {
				return 0;
			}
		}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	doGet(request, response);
    }
}