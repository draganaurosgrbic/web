package model.beans;

import model.CSVData;
import model.GetRacun;
import model.TipDiska;
import model.ValidData;
import rest.Main;
import rest.beans.RacunZahtev;

public class Disk implements CSVData, ValidData, GetRacun {

	private String ime;
	private String organizacija;
	private TipDiska tip;
	private int kapacitet;
	private String masina;

	public Disk() {
		super();
	}

	public Disk(String ime) {
		this();
		this.ime = ime;
	}
	
	public Disk(String ime, String organizacija, TipDiska tip, int kapacitet, String masina) {
		this();
		this.ime = ime;
		this.organizacija = organizacija;
		this.tip = tip;
		this.kapacitet = kapacitet;
		this.masina = masina;
		this.getOrganizacijaRef().dodajResurs(this.ime);
		if (this.getMasinaRef() != null)
			this.getMasinaRef().dodajDisk(this.ime);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Disk)) return false;
		return ((Disk) obj).ime.equals(this.ime);
	}

	public static Disk parse(String line) {
		String[] array = line.split(";");
		String ime = array[0].trim();
		String organizacija = array[1].trim();
		TipDiska tip = TipDiska.values()[Integer.parseInt(array[2].trim())];
		int kapacitet = Integer.parseInt(array[3].trim());
		String masina = array[4].trim();
		if (masina.equals("null")) masina = null;
		return new Disk(ime, organizacija, tip, kapacitet, masina);
	}

	@Override
	public String csvLine() {
		return this.ime + ";" + this.organizacija + ";" + this.tip.ordinal() + ";" + this.kapacitet + ";" + ((this.masina != null && !this.masina.equals("")) ? this.masina : "null");
	}

	@Override
	public boolean validData() {
		
		if (this.ime == null || this.ime.equals("")) return false;
		if (this.organizacija == null || this.organizacija.equals("")) return false;
		if (this.tip == null) return false;
		if (this.kapacitet <= 0) return false;
		return true;
		
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.getOrganizacijaRef().izmeniResurs(this.ime, ime);
		if (this.getMasinaRef() != null)
			this.getMasinaRef().izmeniDisk(this.ime, ime);
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

	public TipDiska getTip() {
		return tip;
	}

	public void setTip(TipDiska tip) {
		this.tip = tip;
	}

	public int getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	public String getMasina() {
		return masina;
	}
	
	public VirtuelnaMasina getMasinaRef() {
		return Main.masine.nadjiMasinu(this.masina);
	}

	public void setMasina(String masina) {
		if (this.getMasinaRef() != null)
			this.getMasinaRef().obrisiDisk(this.ime);
		this.masina = masina;
		if (this.getMasinaRef() != null)
			this.getMasinaRef().dodajDisk(this.ime);
	}
	
	@Override
	public double izracunajRacun(RacunZahtev racunZahtev) {
		
		double racunDiska = 0;
		double pocetni = racunZahtev.getPocetniDatum() / 1000.0 / 60.0 / 60.0 / 24.0 / 30.0;
		double krajnji = racunZahtev.getKrajnjiDatum() / 1000.0 / 60.0 / 60.0 / 24.0 / 30.0;

		if (this.tip == TipDiska.HDD)
			racunDiska = (krajnji - pocetni) * 0.1 * kapacitet;
		
		else if (this.tip == TipDiska.SSD)
			racunDiska = (krajnji - pocetni) * 0.3 * kapacitet;
		
		return racunDiska;
		
	}

}
