package model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.FileNames;
import model.LoadStoreData;
import model.beans.Kategorija;
import model.services.OperationResult.KategorijaResult;
import rest.Main;
import rest.beans.KategorijaChange;

public class Kategorije implements LoadStoreData{
	
	private ArrayList<Kategorija> kategorije;

	public Kategorije() {
		super();
		this.kategorije = new ArrayList<Kategorija>();
	}
	
	public Kategorija nadjiKategoriju(String ime) {
		
		int index = this.kategorije.indexOf(new Kategorija(ime));
		if (index == -1) return null;
		return this.kategorije.get(index);
		
	}
	
	public KategorijaResult dodajKategoriju(Kategorija k) throws Exception {
		
		if (this.nadjiKategoriju(k.getIme()) != null) 
			return KategorijaResult.AL_EXISTS;
		
		this.kategorije.add(k);
		this.store();
		return KategorijaResult.OK;
		
	}
	
	public KategorijaResult obrisiKategoriju(Kategorija k) throws Exception {
		
		Kategorija kategorija = this.nadjiKategoriju(k.getIme());
		if (kategorija == null) 
			return KategorijaResult.DOESNT_EXIST;
		
		if (kategorija.hasMasine()) 
			return KategorijaResult.CANT_DELETE;
		
		this.kategorije.remove(kategorija);
		this.store();
		return KategorijaResult.OK;
		
	}

	public KategorijaResult izmeniKategoriju(KategorijaChange k) throws Exception {

		Kategorija kategorija = this.nadjiKategoriju(k.getStaroIme());
		if (kategorija == null) 
			return KategorijaResult.DOESNT_EXIST;
		
		if (this.nadjiKategoriju(k.getNovaKategorija().getIme()) != null && 
				(!(k.getStaroIme().equals(k.getNovaKategorija().getIme())))) 
			return KategorijaResult.AL_EXISTS;

		kategorija.setIme(k.getNovaKategorija().getIme());
		kategorija.setJezgra(k.getNovaKategorija().getJezgra());
		kategorija.setRam(k.getNovaKategorija().getRam());
		kategorija.setGpu(k.getNovaKategorija().getGpu());
		kategorija.updateMasine();
		
		this.store();
		Main.masine.store();
		return KategorijaResult.OK;
		
	}
	
	@Override
	public void load() throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(FileNames.KATEGORIJE_FILE));
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.equals(""))
				continue;
			this.kategorije.add(Kategorija.parse(line));
		}
		in.close();
	}
	
	@Override
	public void store() throws Exception {

		PrintWriter out = new PrintWriter(new FileWriter(FileNames.KATEGORIJE_FILE));
		for (Kategorija k: this.kategorije) {
			out.println(k.csvLine());
			out.flush();
		}
		out.close();
	}
	
	public ArrayList<Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(ArrayList<Kategorija> kategorije) {
		this.kategorije = kategorije;
	}

}
