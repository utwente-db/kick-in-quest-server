package nl.utwente.db.kickinquest.server;

import java.io.IOException;
import java.util.Vector;

public class Zip {
	
	private static final String TMPDIR = "/tmp";
	
	class ZipImage {
		public String name;
		public byte   content[];
		
		public ZipImage(String name, byte content[]) {
			this.name = name;
			this.content = content;
		}
	}
	
	//
	//
	//
		
	private String dir;	// directory to store
	private String name; // without .zip
	
	private String info_json = null;
	private String game_json = null;
	private Vector<ZipImage> images = new Vector<ZipImage>();
	
	public Zip(String dir, String name) {
		this.dir  = dir;
		this.name = name;
	}
	
	public void set_info_json(String s) {
		this.info_json = s;
	}
	
	public void set_game_json(String s) {
		this.game_json = s;
	}
	
	public void add_image(String name, byte content[]) {
		images.add( new ZipImage(name, content));
	}
	
	public void generate() throws IOException {
		OsUtils.mkdir(TMPDIR, name);
		String zipbase = TMPDIR + "/" + name;
		OsUtils.mkdir(zipbase,"images");
		String imagesbase = zipbase + "/images";
		OsUtils.mkdir(zipbase,"json");
		String jsonbase = zipbase + "/json";
		OsUtils.createFile(jsonbase, "info.json", info_json.getBytes());
		OsUtils.createFile(jsonbase, "game.json", game_json.getBytes());
		for(int i=0; i<images.size(); i++)
			OsUtils.createFile(imagesbase, images.get(i).name, images.get(i).content);
		// zip -r tz.zip tz/images tz/json
		String zipfile = dir + "/" + name + ".zip";
		OsUtils.removeIfPossible(zipfile);
		OsUtils.runCommand("zip -r "+zipfile+" images/ json/",null,null,zipbase,false);
		OsUtils.removeall(TMPDIR, name);
	}
	
}
