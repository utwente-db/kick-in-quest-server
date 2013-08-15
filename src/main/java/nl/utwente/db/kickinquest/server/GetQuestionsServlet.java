package nl.utwente.db.kickinquest.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class GetQuestionsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public final static String ROOT_FOLDER = "/data/tomcat/";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String teamId = request.getParameter("teamId");
        String language = request.getParameter("language");
        
        if (teamId == null || "".equals(teamId)) {
        	throw new RuntimeException("Invalid request, teamId is required.");
        }
        
        teamId = teamId.toUpperCase();
        
        if (!("nl".equals(language) || "en".equals(language))) {
        	throw new RuntimeException("Invalid request, language is invalid.");
        }
        
        File file = new File(ROOT_FOLDER + language + "/" + teamId + ".zip");
        
        if (!file.exists()) {
        	throw new RuntimeException("Invalid team ID, no such file found");
        }

        response.setContentType("application/zip");
        response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", "attachment;filename=\"quest.zip\"");

        try {
            byte[] arBytes = new byte[(int) file.length()];
            
            FileInputStream is = new FileInputStream(file);
            is.read(arBytes);
            
            ServletOutputStream op = response.getOutputStream();
            op.write(arBytes);
            op.flush();
        } catch (IOException e) {
            throw new RuntimeException("Unable to download file", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }
}

