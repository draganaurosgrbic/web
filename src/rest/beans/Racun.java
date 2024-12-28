package rest.beans;

import java.util.HashMap;

public class Racun {

	private HashMap<String, Double> racuniMasine;
	private HashMap<String, Double> racuniDiskovi;
	private double ukupniRacun;

	public Racun() {
		super();
	}

	public Racun(HashMap<String, Double> racuniMasine, HashMap<String, Double> racuniDiskovi, double ukupniRacun) {
		this();
		this.racuniMasine = racuniMasine;
		this.racuniDiskovi = racuniDiskovi;
		this.ukupniRacun = ukupniRacun;
	}

	public HashMap<String, Double> getRacuniMasine() {
		return racuniMasine;
	}

	public void setRacuniMasine(HashMap<String, Double> racuniMasine) {
		this.racuniMasine = racuniMasine;
	}

	public HashMap<String, Double> getRacuniDiskovi() {
		return racuniDiskovi;
	}

	public void setRacuniDiskovi(HashMap<String, Double> racuniDiskovi) {
		this.racuniDiskovi = racuniDiskovi;
	}

	public double getUkupniRacun() {
		return ukupniRacun;
	}

	public void setUkupniRacun(double ukupniRacun) {
		this.ukupniRacun = ukupniRacun;
	}

}
