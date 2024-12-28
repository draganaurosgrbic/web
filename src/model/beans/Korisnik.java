package model.beans;

import java.util.ArrayList;
import java.util.HashMap;

import model.CSVData;
import model.Uloga;
import model.ValidData;
import rest.Main;
import rest.beans.Racun;
import rest.beans.RacunZahtev;

public class Korisnik implements CSVData, ValidData {

	private String email;
	private String lozinka;
	private String ime;
	private String prezime;
	private Uloga uloga;
	private String organizacija;

	public Korisnik() {
		super();
	}
	
	public Korisnik(String email) {
		this();
		this.email = email;
	}

	public Korisnik(String email, String lozinka, String ime, String prezime, Uloga uloga, String organizacija) {
		this();
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = uloga;
		this.organizacija = organizacija;
		if (this.getOrganizacijaRef() != null)
			this.getOrganizacijaRef().dodajKorisnika(this.email);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Korisnik)) return false;
		return ((Korisnik) obj).email.equals(this.email);
	}

	public static Korisnik parse(String line) {
		String[] array = line.split(";");
		String email = array[0].trim();
		String lozinka = array[1].trim();
		String ime = array[2].trim();
		String prezime = array[3].trim();
		Uloga uloga = Uloga.values()[(Integer.parseInt(array[4].trim()))];
		String organizacija = array[5].trim();
		if (organizacija.equals("null")) organizacija = null;
		return new Korisnik(email, lozinka, ime, prezime, uloga, organizacija);
	}

	@Override
	public String csvLine() {
		return this.email + ";" + this.lozinka + ";" + this.ime + ";" + this.prezime + ";" + this.uloga.ordinal() + ";" + ((this.organizacija != null && !this.organizacija.equals("")) ? this.organizacija : "null");
	}
	
	@Override
	public boolean validData() {

		if (this.email == null || this.email.equals("")) return false;
		if (this.lozinka == null || this.lozinka.equals("")) return false;
		if (this.ime == null || this.ime.equals("")) return false;
		if (this.prezime == null || this.prezime.equals("")) return false;
		if (this.uloga == null) return false;
		if (this.uloga != Uloga.SUPER_ADMIN && (this.organizacija == null || this.organizacija.equals(""))) return false;
		return true;
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (this.getOrganizacijaRef() != null)
			this.getOrganizacijaRef().izmeniKorisnika(this.email, email);
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	public String getOrganizacija() {
		return organizacija;
	}

	public Organizacija getOrganizacijaRef() {
		return Main.organizacije.nadjiOrganizaciju(this.organizacija);
	}

	public void setOrganizacija(String organizacija) {
		this.organizacija = organizacija;
	}

	public ArrayList<Organizacija> getMojeOrganizacije(){
		
		if (this.uloga.equals(Uloga.SUPER_ADMIN)) return Main.organizacije.getOrganizacije();
		ArrayList<Organizacija> organizacije = new ArrayList<Organizacija>();
		organizacije.add(this.getOrganizacijaRef());
		return organizacije;
		
	}

	public ArrayList<VirtuelnaMasina> getMojeMasine(){
		
		if (this.uloga.equals(Uloga.SUPER_ADMIN)) return Main.masine.getMasine();
		ArrayList<VirtuelnaMasina> masine = new ArrayList<VirtuelnaMasina>();
		for (VirtuelnaMasina m: Main.masine.getMasine()) {
			if (m.getOrganizacijaRef().equals(this.getOrganizacijaRef()))
				masine.add(m);
		}
		return masine;
		
	}
	
	public ArrayList<Disk> getMojiDiskovi(){
		
		if (this.uloga.equals(Uloga.SUPER_ADMIN)) return Main.diskovi.getDiskovi();
		ArrayList<Disk> diskovi = new ArrayList<Disk>();
		for (Disk d: Main.diskovi.getDiskovi()) {
			if (d.getOrganizacijaRef().equals(this.getOrganizacijaRef()))
				diskovi.add(d);
		}
		return diskovi;
		
	}
	
	public ArrayList<Korisnik> getMojiKorisnici(){
		
		if (this.uloga.equals(Uloga.SUPER_ADMIN)) return Main.korisnici.getKorisnici();
		else if (this.uloga.equals(Uloga.ADMIN)) {
			ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();
			for (Korisnik k: Main.korisnici.getKorisnici()) {
				if (k.getOrganizacijaRef() != null && k.getOrganizacijaRef().equals(this.getOrganizacijaRef()))
					korisnici.add(k);
			}
			return korisnici;
		}
		ArrayList<Korisnik> lista = new ArrayList<Korisnik>();
		lista.add(this);
		return lista;
		
	}
	
	public Racun izracunajRacun(RacunZahtev racunZahtev) {	
		
		if (!this.uloga.equals(Uloga.ADMIN)) return null;
		
		HashMap<String, Double> racuniMasine = new HashMap<String, Double>();
		HashMap<String, Double> racuniDiskovi = new HashMap<String, Double>();
		double ukupniRacun = 0;
		
		for (VirtuelnaMasina m: getMojeMasine()) {
			double racunMasine = m.izracunajRacun(racunZahtev);
			racuniMasine.put(m.getIme(), Math.round(racunMasine * 100.0) / 100.0);
			ukupniRacun += racunMasine;
		}
		
		for (Disk d: getMojiDiskovi()) {
			double racunDiska = d.izracunajRacun(racunZahtev);
			racuniDiskovi.put(d.getIme(), Math.round(racunDiska * 100.0) / 100.0);
			ukupniRacun += racunDiska;
		}
		
		return new Racun(racuniMasine, racuniDiskovi, Math.round(ukupniRacun * 100.0) / 100.0);
		
	}
	
}
