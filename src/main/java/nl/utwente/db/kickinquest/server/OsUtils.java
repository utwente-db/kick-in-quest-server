package nl.utwente.db.kickinquest.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class OsUtils {

	  public static String runCommand( String c ) throws IOException {
	    return runCommand( c, null, null, null, false );
	  }

	public static String runCommand(String c, String input,
			Vector<String> outv, String execDir, boolean verbose) throws IOException {
		String out = "";

		String s = null;
		String ss = null;
		if (verbose)
			System.out.println("!runCommand(\"" + c + "\")");
		File workdir = null;
		
		if (execDir != null) {
			workdir = new File(execDir);
			if ( verbose )
				System.out.println("!runCommand:workdir="+workdir);
		}
		Process p = Runtime.getRuntime().exec(c, null, workdir);

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));

		if (input != null) {
			if (verbose)
				System.out.println("!runCommand:input=" + input);
			BufferedWriter stdOut = new BufferedWriter(new OutputStreamWriter(
					p.getOutputStream()));
			stdOut.write(input, 0, input.length());
			stdOut.close();
		}

		// read the output from the command
		while ((s = stdInput.readLine()) != null) {
			if (outv != null)
				outv.add(s);
			out += s + "\n";
		}
		if (verbose && out.length() > 0) {
			System.out.println("!STDOUT:");
			System.out.println(out);
		}

		// read any errors from the attempted command
		ss = "";
		while ((s = stdError.readLine()) != null) {
			if (s.startsWith("NOTICE"))
				s = ""; // Just a warning
			if (s.startsWith("Document availability check not implemented"))
				s = ""; // Just a warning
			ss += s;
		}
		if (ss.length() > 0) {
			System.err.println("!ERROR:neography.Utils.runCommand(\"" + c
					+ "\")");
			if (input != null) {
				System.err.println("!ERROR:input=" + input);
			}
			System.err.println(ss);
			return null;
		}

		try {
			// be sure process really terminated before getting exit value
			p.waitFor();
		} catch (java.lang.InterruptedException e) {
		}

		int exitValue = p.exitValue();
		if (exitValue != 0) {
			System.err.println("!ERROR:neography.Utils.runCommand(\"" + c
					+ "\") = " + exitValue + "");
			if (input != null) {
				System.err.println("!ERROR:input=" + input);
			}
			return null;
		}
		return out;
	}
	  
	  public static String[] ls(String dir, boolean fullName) throws IOException {
		  Vector<String> outv = new Vector<String>();
		  
		  String out = runCommand("ls " + dir ,null,outv,null,false);
		  if ( out != null ) {
			  String res[] = new String[outv.size()];
			  
			  if ( fullName && !dir.endsWith("/"))
				  dir = dir + "/";
			  
			  for (int i=0; i<res.length; i++) {
				  if ( fullName )
					  res[i] = dir + outv.elementAt(i);
				  else
					  res[i] = outv.elementAt(i);
				  // System.out.println("OUT: "+ res[i]);
			  }
			  return res;
		  } else 
			  return null;
	  }
	  
	  public static void mkdir(String dir, String name) throws IOException {
		  runCommand("mkdir " + dir + "/" + name ,null,null,null,false);
	  }
	  
	  public static void removeall(String dir, String name) throws IOException {
		  runCommand("rm -rf " + dir + "/" + name ,null,null,null,false);
	  }
	  
	  public static void removeIfPossible(String filename) {
		  try {
			runCommand("rm -f " + filename ,null,null,null,false);
		} catch (IOException e) {
			// ignoreS
		}
	  }
	  
	public static void createFile(String dir, String name, byte content[]) throws IOException {
		FileOutputStream fileOuputStream = new FileOutputStream(dir + "/"
				+ name);
		fileOuputStream.write(content);
		fileOuputStream.close();
	}
	  
	  
	  public static void main(String[] args) {	
		  try {
			System.out.println( runCommand("ls .",null,null,"/Users/flokstra",true) );
		  } catch (IOException e) {
			  System.err.println("#!CAUGHT: "+ e);
			  e.printStackTrace();
		  }
	  }
		

	}
