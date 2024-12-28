package model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.FileNames;
import model.LoadStoreData;
import model.Uloga;
import model.beans.Korisnik;
import model.services.OperationResult.KorisnikResult;
import rest.beans.KorisnikChange;
import rest.beans.User;

public class Korisnici implements LoadStoreData{
		
	private ArrayList<Korisnik> korisnici;

	public Korisnici() {
		super();
		this.korisnici = new ArrayList<Korisnik>();
	}
	
	public Korisnik nadjiKorisnika(String email) {
		int index = this.korisnici.indexOf(new Korisnik(email));
		if (index == -1) return null;
		return this.korisnici.get(index);
	}

	public KorisnikResult dodajKorisnika(Korisnik k) throws Exception {
		
		if (this.nadjiKorisnika(k.getEmail()) != null) 
			return KorisnikResult.AL_EXISTS;
				
		if (k.getOrganizacijaRef() == null)
			return KorisnikResult.ORG_DOESNT_EXIST;

		k.getOrganizacijaRef().dodajKorisnika(k.getEmail());
		this.korisnici.add(k);
		this.store();
		return KorisnikResult.OK;
		
	}
	
	public KorisnikResult obrisiKorisnika(Korisnik k, Korisnik user) throws Exception {
		
		Korisnik korisnik = this.nadjiKorisnika(k.getEmail());
		if (korisnik == null) 
			return KorisnikResult.DOESNT_EXIST;
		if (korisnik.equals(user)) 
			return KorisnikResult.CANT_DEL_SELF;

		korisnik.getOrganizacijaRef().obrisiKorisnika(korisnik.getEmail());
		this.korisnici.remove(korisnik);
		this.store();
		return KorisnikResult.OK;
		
	}
	
	public KorisnikResult izmeniKorisnika(KorisnikChange k, Korisnik user) throws Exception {
		
		Korisnik korisnik = this.nadjiKorisnika(k.getStaroIme());
		if (korisnik == null) 
			return KorisnikResult.DOESNT_EXIST;
		
		if (this.nadjiKorisnika(k.getNoviKorisnik().getEmail()) != null && 
				(!(k.getStaroIme().equals(k.getNoviKorisnik().getEmail()))))
			return KorisnikResult.AL_EXISTS;
				
		if (korisnik.getEmail().equals(user.getEmail())) {
			user.setEmail(k.getNoviKorisnik().getEmail());
			user.setLozinka(k.getNoviKorisnik().getLozinka());
			user.setIme(k.getNoviKorisnik().getIme());
			user.setPrezime(k.getNoviKorisnik().getPrezime());
			user.setUloga(k.getNoviKorisnik().getUloga());
		}
		
		korisnik.setEmail(k.getNoviKorisnik().getEmail());
		korisnik.setLozinka(k.getNoviKorisnik().getLozinka());
		korisnik.setIme(k.getNoviKorisnik().getIme());
		korisnik.setPrezime(k.getNoviKorisnik().getPrezime());
		korisnik.setUloga(k.getNoviKorisnik().getUloga());
		
		this.store();
		return KorisnikResult.OK;
		
	}
	
	public Korisnik login(User u) {
		for (Korisnik k: this.korisnici) {
			if (k.getEmail().equals(u.getKorisnickoIme()) && 
					k.getLozinka().equals(u.getLozinka()))
				return k;
		}
		return null;
	}
	
	private boolean hasSuperAdmin() {
		
		for (Korisnik k: this.korisnici) {
			if (k.getUloga().equals(Uloga.SUPER_ADMIN)) return true;
		}
		return false;
		
	}
	
	public void addSuperAdmin() throws Exception {
		
		if (!this.hasSuperAdmin()) {
			this.korisnici.add(new Korisnik("super@gmail.com", "super", "super", "super", Uloga.SUPER_ADMIN, null));
			this.store();
		}
	}
	
	@Override
	public void load() throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(FileNames.KORISNICI_FILE));
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.equals(""))
				continue;
			this.korisnici.add(Korisnik.parse(line));
		}
		in.close();
	}
	
	@Override
	public void store() throws Exception {

		PrintWriter out = new PrintWriter(FileNames.KORISNICI_FILE);
		for (Korisnik k: this.korisnici) {
			out.println(k.csvLine());
			out.flush();
		}
		out.close();
	}
	
	public ArrayList<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(ArrayList<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
	
}
