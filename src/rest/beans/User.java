package rest.beans;

import model.ValidData;

public class User implements ValidData {

	private String korisnickoIme;
	private String lozinka;

	public User() {
		super();
	}

	public User(String korisnickoIme, String lozinka) {
		this();
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
	}
	
	@Override
	public boolean validData() {

		if (this.korisnickoIme == null || this.korisnickoIme.equals("")) return false;
		if (this.lozinka == null || this.lozinka.equals("")) return false;
		return true;
		
	}
	
	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

}
