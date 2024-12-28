package rest.beans;

import model.ValidData;

public class RacunZahtev implements ValidData{

	private long pocetniDatum;
	private long krajnjiDatum;

	public RacunZahtev() {
		super();
	}
	
	public RacunZahtev(long pocetniDatum, long krajnjiDatum) {
		super();
		this.pocetniDatum = pocetniDatum;
		this.krajnjiDatum = krajnjiDatum;
	}
	
	public long getPocetniDatum() {
		return pocetniDatum;
	}

	public void setPocetniDatum(long pocetniDatum) {
		this.pocetniDatum = pocetniDatum;
	}

	public long getKrajnjiDatum() {
		return krajnjiDatum;
	}

	public void setKrajnjiDatum(long krajnjiDatum) {
		this.krajnjiDatum = krajnjiDatum;
	}

	@Override
	public boolean validData() {
		// TODO Auto-generated method stub
		if (this.pocetniDatum <= 0) return false;
		if (this.krajnjiDatum <= 0) return false;
		if (this.pocetniDatum >= this.krajnjiDatum) return false;
		return true;
	}

}
