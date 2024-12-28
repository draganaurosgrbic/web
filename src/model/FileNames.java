package model;

import java.io.File;

public class FileNames {
	
	private static final String FILES = "files" + File.separatorChar;
	private static final String PICTURES = "pictures" + File.separatorChar;

	public static final String KATEGORIJE_FILE = FILES + "kategorije.txt";
	public static final String ORGANIZACIJE_FILE = FILES + "organizacije.txt";
	public static final String KORISNICI_FILE = FILES + "korisnici.txt";
	public static final String MASINE_FILE = FILES + "masine.txt";
	public static final String DISKOVI_FILE = FILES + "diskovi.txt";
	public static final String AKTIVNOSTI_FILE = FILES + "aktivnosti.txt";
	public static final String DEFAULT_LOGO = PICTURES + "default.png";

}
