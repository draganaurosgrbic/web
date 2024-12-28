package model.beans;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

import model.CSVData;
import model.FileNames;
import model.UpdateReference;
import model.ValidData;
import rest.Main;

public class Organizacija implements CSVData, ValidData, UpdateReference {

	private String ime;
	private String opis;
	private String logo;
	private ArrayList<String> korisnici;
	private ArrayList<String> resursi;
	
	public Organizacija() {
		super();
		this.korisnici = new ArrayList<String>();
		this.resursi = new ArrayList<String>();
	}

	public Organizacija(String ime) {
		this();
		this.ime = ime;
	}
	
	public Organizacija(String ime, String opis, String logo) {
		this();
		this.ime = ime;
		this.opis = opis;
		this.logo = logo;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Organizacija)) return false;
		return ((Organizacija) obj).ime.equals(this.ime);
	}

	public static Organizacija parse(String line) {
		String[] array = line.split(";", 3);
		String ime = array[0].trim();
		String opis = array[1].trim();
		String logo = array[2].trim();
		if (opis.equals("null")) opis = null;
		return new Organizacija(ime, opis, logo);
	}

	@Override
	public String csvLine() {
		return this.ime + ";" + ((this.opis != null && !this.opis.equals("")) ? this.opis : "null") + ";" + this.logo;
	}

	@Override
	public boolean validData() {
		
		if (this.ime == null || this.ime.equals("")) return false;
		if (this.korisnici == null) return false;
		if (this.resursi == null) return false;
		return true;
		
	}

	public void setDefaultLogo() {
		
		try {
			byte[] img = Files.readAllBytes(Paths.get(FileNames.DEFAULT_LOGO));
			String encodedImg = Base64.getEncoder().encodeToString(img);		
			this.logo = "data:image/png;base64," + encodedImg;
		}
		catch(Exception e) {
			;
		}
		
	}
	
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.updateReference(ime);
		this.ime = ime;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public ArrayList<String> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(ArrayList<String> korisnici) {
		this.korisnici = korisnici;
	}

	public ArrayList<String> getResursi() {
		return resursi;
	}

	public void setResursi(ArrayList<String> resursi) {
		this.resursi = resursi;
	}

	public void dodajKorisnika(String ime) {
		if (!this.korisnici.contains(ime))
			this.korisnici.add(ime);
	}
	
	public void obrisiKorisnika(String ime) {
		this.korisnici.remove(ime);
	}
	
	public void izmeniKorisnika(String staroIme, String novoIme) {
		int index = this.korisnici.indexOf(staroIme);
		if (index != -1)
			this.korisnici.set(index, novoIme);
	}
	
	public void dodajResurs(String ime) {
		if (!this.resursi.contains(ime))
			this.resursi.add(ime);
	}
	
	public void obrisiResurs(String ime) {
		this.resursi.remove(ime);
	}
	
	public void izmeniResurs(String staroIme, String novoIme) {
		int index = this.resursi.indexOf(staroIme);
		if (index != -1)
			this.resursi.set(index, novoIme);
	}

	@Override
	public void updateReference(String ime) {

		for (Korisnik k: Main.korisnici.getKorisnici()) {
			if (k.getOrganizacijaRef() != null && k.getOrganizacijaRef().equals(this))
				k.setOrganizacija(ime);
		}
		
		for (VirtuelnaMasina m: Main.masine.getMasine()) {
			if (m.getOrganizacijaRef().equals(this))
				m.setOrganizacija(ime);
		}
		
		for (Disk d: Main.diskovi.getDiskovi()) {
			if (d.getOrganizacijaRef().equals(this))
				d.setOrganizacija(ime);
		}
		
	}
	
}
