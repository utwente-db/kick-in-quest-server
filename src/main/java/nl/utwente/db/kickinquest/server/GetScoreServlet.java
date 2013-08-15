package nl.utwente.db.kickinquest.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.utwente.db.neogeo.utils.FileUtils;

public class GetScoreServlet extends HttpServlet {
	
    public final static String ROOT_FOLDER = "/data/tomcat/";
	private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	PrintWriter writer = response.getWriter();
    	String resultsFolderPath = ROOT_FOLDER + "results/";
    	
    	File resultsFolder = new File(resultsFolderPath);
    	String delimiter = "";
    	
    	for (String teamId : resultsFolder.list()) {
    		int teamScore = 0;
    		
    		String teamFolderPath = resultsFolderPath + teamId;
    		File teamFolder = new File(teamFolderPath);
    		
    		for (String deviceId : teamFolder.list()) {
    			File gpsScoreFile = new File(teamFolderPath + "/" + deviceId + "/gpsdatascore.txt");
    			
    			if (gpsScoreFile.exists()) {
	    			String score = FileUtils.getFileAsString(gpsScoreFile);
	    			teamScore += Integer.valueOf(score);
    			}
    			
    			File questionScoreFile = new File(teamFolderPath + "/" + deviceId + "/questionscore.txt");
    			
    			if (questionScoreFile.exists()) {
	    			String score = FileUtils.getFileAsString(questionScoreFile);
	    			teamScore += Integer.valueOf(score);
    			}
    		}
    		
        	writer.write(delimiter + teamId + "=" + teamScore);
        	delimiter = ";";
    	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	doGet(request, response);
    }
}