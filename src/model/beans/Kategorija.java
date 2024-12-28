package model.beans;

import model.CSVData;
import model.ValidData;
import rest.Main;

public class Kategorija implements CSVData, ValidData {
	
	private String ime;
	private int jezgra;
	private int ram;
	private int gpu;
	
	public Kategorija() {
		super();
	}
	
	public Kategorija(String ime) {
		this();
		this.ime = ime;
	}

	public Kategorija(String ime, int jezgra, int ram, int gpu) {
		this();
		this.ime = ime;
		this.jezgra = jezgra;
		this.ram = ram;
		this.gpu = gpu;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Kategorija)) return false;
		return ((Kategorija) obj).ime.equals(this.ime);
	}
	
	public static Kategorija parse(String line) {
		String[] array = line.split(";");
		String ime = array[0].trim();
		int brojJezgara = Integer.parseInt(array[1].trim());
		int RAM = Integer.parseInt(array[2].trim());
		int GPUjezgra = Integer.parseInt(array[3].trim());
		return new Kategorija(ime, brojJezgara, RAM, GPUjezgra);
	}
	
	@Override
	public String csvLine() {
		return this.ime + ";" + this.jezgra + ";" + this.ram + ";" + this.gpu;
	}

	@Override
	public boolean validData() {

		if (this.ime == null || this.ime.equals("")) return false;
		if (this.jezgra <= 0) return false;
		if (this.ram <= 0) return false;
		if (this.gpu < 0) return false;
		return true;
		
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
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
	
	public boolean hasMasine() {
		
		for (VirtuelnaMasina m: Main.masine.getMasine()) {
			if (m.getKategorija().equals(this)) return true;
		}
		return false;
		
	}

	public void updateMasine() {
		
		for (VirtuelnaMasina m: Main.masine.getMasine()) {
			if (m.getKategorija().equals(this)) {
				m.setJezgra(this.jezgra);
				m.setRam(this.ram);
				m.setGpu(this.gpu);
			}
		}
		
	}

}
