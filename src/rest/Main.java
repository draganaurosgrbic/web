package rest;

import model.FileNames;
import model.services.Diskovi;
import model.services.Kategorije;
import model.services.Korisnici;
import model.services.Masine;
import model.services.Organizacije;
import rest.services.DataRest;
import rest.services.DiskRest;
import rest.services.KategorijaRest;
import rest.services.KorisnikRest;
import rest.services.MasinaRest;
import rest.services.OrganizacijaRest;
import rest.services.UserRest;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

public class Main {

	public static Kategorije kategorije = new Kategorije();
	public static Organizacije organizacije = new Organizacije();
	public static Korisnici korisnici = new Korisnici();
	public static Masine masine = new Masine();
	public static Diskovi diskovi = new Diskovi();

	public static void createFiles() throws IOException {
		
		File file = new File("files");
		if (!file.exists())
			file.mkdir();

		file = new File(FileNames.KATEGORIJE_FILE);
		if (!file.exists())
			file.createNewFile();
		
		file = new File(FileNames.ORGANIZACIJE_FILE);
		if (!file.exists())
			file.createNewFile();
		
		file = new File(FileNames.KORISNICI_FILE);
		if (!file.exists())
			file.createNewFile();

		file = new File(FileNames.MASINE_FILE);
		if (!file.exists())
			file.createNewFile();
		
		file = new File(FileNames.DISKOVI_FILE);
		if (!file.exists())
			file.createNewFile();

		file = new File(FileNames.AKTIVNOSTI_FILE);
		if (!file.exists())
			file.createNewFile();

	}

	public static void loadData() throws Exception {
		
		kategorije.load();
		organizacije.load();
		korisnici.load();
		masine.load();
		diskovi.load();
		
	}

	public static void main(String[] args) throws Exception {

		createFiles();
		loadData();
		korisnici.addSuperAdmin();
		port(8080);
		staticFiles.externalLocation(new File("static").getCanonicalPath());

		new KategorijaRest().init();
		new OrganizacijaRest().init();
		new KorisnikRest().init();
		new MasinaRest().init();
		new DiskRest().init();
		new UserRest().init();
		new DataRest().init();

	}

}
