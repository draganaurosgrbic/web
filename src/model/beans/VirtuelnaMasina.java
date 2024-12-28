package model.beans;

import java.util.ArrayList;

import model.CSVData;
import model.GetRacun;
import model.StatusMasine;
import model.UpdateReference;
import model.ValidData;
import rest.Main;
import rest.beans.RacunZahtev;

public class VirtuelnaMasina implements CSVData, ValidData, UpdateReference, GetRacun {

	private String ime;
	private String organizacija;
	private Kategorija kategorija;
	private int jezgra;
	private int ram;
	private int gpu;
	private ArrayList<Aktivnost> aktivnosti;
	private ArrayList<String> diskovi;

	public VirtuelnaMasina() {
		super();
		this.aktivnosti = new ArrayList<Aktivnost>();
		this.diskovi = new ArrayList<String>();
	}
	
	public VirtuelnaMasina(String ime) {
		this();
		this.ime = ime;
	}

	public VirtuelnaMasina(String ime, String organizacija, Kategorija kategorija) {
		this();
		this.ime = ime;
		this.organizacija = organizacija;
		this.kategorija = kategorija;
		this.jezgra = this.kategorija.getJezgra();
		this.ram = this.kategorija.getRam();
		this.gpu = this.kategorija.getGpu();
		this.getOrganizacijaRef().dodajResurs(this.ime);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VirtuelnaMasina)) return false;
		return ((VirtuelnaMasina) obj).ime.equals(this.ime);
	}

	public static VirtuelnaMasina parse(String line) {
		String[] array = line.split(";");
		String ime = array[0].trim();
		String organizacija = array[1].trim();
		Kategorija kategorija = Main.kategorije.nadjiKategoriju(array[2].trim());
		return new VirtuelnaMasina(ime, organizacija, kategorija);
	}

	@Override
	public String csvLine() {
		return this.ime + ";" + this.organizacija + ";" + this.kategorija.getIme();
	}

	@Override
	public boolean validData() {

		if (this.ime == null || this.ime.equals("")) return false;
		if (this.organizacija == null || this.organizacija.equals("")) return false;
		if (this.kategorija == null) return false;
		if (this.jezgra <= 0) return false;
		if (this.ram <= 0) return false;
		if (this.gpu < 0) return false;
		if (this.aktivnosti == null) return false;
		if (this.diskovi == null) return false;
		return this.kategorija.validData();

	}
	
	public StatusMasine status() {
		if (this.aktivnosti.isEmpty()) return StatusMasine.UGASENA;
		return this.aktivnosti.get(this.aktivnosti.size() - 1).getStatus();
	}
	
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.getOrganizacijaRef().izmeniResurs(this.ime, ime);
		this.updateReference(ime);
		this.ime = ime;
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

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public int getJezgra() {
		return jezgra;
	}

	public void setJezgra(int jezgra) {
		this.jezgra = jezgra;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public int getGpu() {
		return gpu;
	}

	public void setGpu(int gpu) {
		this.gpu = gpu;
	}

	public ArrayList<Aktivnost> getAktivnosti() {
		return aktivnosti;
	}

	public void setAktivnosti(ArrayList<Aktivnost> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	public ArrayList<String> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(ArrayList<String> diskovi) {
		ArrayList<String> temp = new ArrayList<String>();
		for (String d: this.diskovi)
			temp.add(d);
		for (String d: temp)
			Main.diskovi.nadjiDisk(d).setMasina(null);
		for (String d: diskovi)
			Main.diskovi.nadjiDisk(d).setMasina(this.ime);
	}
	
	public void dodajDisk(String ime) {
		if (!this.diskovi.contains(ime))
			this.diskovi.add(ime);
	}

	public void obrisiDisk(String ime) {
		this.diskovi.remove(ime);
	}
	
	public void izmeniDisk(String staroIme, String novoIme) {
		int index = this.diskovi.indexOf(staroIme);
		if (index != -1)
			this.diskovi.set(index, novoIme);
	}
	
	public void dodajAktivnost(Aktivnost a) {
		this.aktivnosti.add(a);
	}

	@Override
	public void updateReference(String ime) {

		for (Disk d: Main.diskovi.getDiskovi()) {
			if (d.getMasinaRef() != null && d.getMasinaRef().equals(this))
				d.setMasina(ime);
		}
	}
	
	@Override
	public double izracunajRacun(RacunZahtev racunZahtev) {
		
		double racunMasine = 0;
		double jedinicnaCena = this.izracunajJedinicnuCenu();
		double pocetni = racunZahtev.getPocetniDatum() / 1000.0 / 60.0;
		double krajnji = racunZahtev.getKrajnjiDatum() / 1000.0 / 60.0;

		for (Aktivnost a : aktivnosti) {
			if (a.getDatumGasenja() != null) {
				
				double datumPaljenja = a.getDatumPaljenja().getTime() / 1000.0 / 60.0;
				double datumGasenja = a.getDatumGasenja().getTime() / 1000.0 / 60.0;

				if (datumGasenja - datumPaljenja < 60)
					continue;

				if (datumGasenja <= pocetni || datumPaljenja >= krajnji)
					continue;

				if (datumGasenja <= krajnji && datumPaljenja >= pocetni)
					racunMasine += jedinicnaCena * ((datumGasenja - datumPaljenja) / 60.0 / 24.0 / 30.0);
				
				else if (datumGasenja > krajnji && datumPaljenja > pocetni)
					racunMasine += jedinicnaCena * ((krajnji - datumPaljenja) / 60.0 / 24.0 / 30.0);
				
				else if (datumGasenja < krajnji && datumPaljenja < pocetni)
					racunMasine += jedinicnaCena * ((datumGasenja - pocetni) / 60.0 / 24.0 / 30.0);

			} 
			else {
				
				double datumPaljenja = a.getDatumPaljenja().getTime() / 1000.0 / 60.0;

				if (krajnji - datumPaljenja < 60)
					continue;

				if (datumPaljenja >= krajnji)
					continue;

				if (datumPaljenja > pocetni)
					racunMasine += jedinicnaCena * ((krajnji - datumPaljenja) / 60.0 / 24.0 / 30.0);
				
				else
					racunMasine += jedinicnaCena * ((krajnji - pocetni) / 60.0 / 24.0 / 30.0);

			}
		}

		return racunMasine;
	}

	private double izracunajJedinicnuCenu() {
		return 25 * jezgra + 15 * ram + 1 * gpu;
	}

}
