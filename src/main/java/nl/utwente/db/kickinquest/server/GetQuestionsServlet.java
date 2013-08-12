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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        
        String teamId = request.getParameter("teamId");
        String language = request.getParameter("language");
        
        if (teamId == null || "".equals(teamId)) {
        	throw new RuntimeException("Invalid request, teamId is required.");
        }
        
        if (!("nl".equals(language) || "en".equals(language))) {
        	throw new RuntimeException("Invalid request, language is invalid.");
        }
        
        File file = new File(classLoader.getResource(language + "/" + teamId + ".zip").getFile());

        response.setContentType("application/pdf");
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

