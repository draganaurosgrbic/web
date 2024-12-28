package model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.FileNames;
import model.LoadStoreData;
import model.StatusMasine;
import model.beans.Aktivnost;
import model.beans.VirtuelnaMasina;
import model.services.OperationResult.MasinaResult;
import rest.Main;
import rest.beans.MasinaChange;

public class Masine implements LoadStoreData {

	private ArrayList<VirtuelnaMasina> masine;

	public Masine() {
		super();
		this.masine = new ArrayList<VirtuelnaMasina>();
	}

	public VirtuelnaMasina nadjiMasinu(String ime) {
		int index = this.masine.indexOf(new VirtuelnaMasina(ime));
		if (index == -1) return null;
		return this.masine.get(index);
	}

	public MasinaResult dodajMasinu(VirtuelnaMasina m) throws Exception {

		if (this.nadjiMasinu(m.getIme()) != null)
			return MasinaResult.AL_EXISTS;

		if (m.getOrganizacijaRef() == null)
			return MasinaResult.ORG_DOESNT_EXIST;

		if (m.getOrganizacijaRef().getResursi().contains(m.getIme()))
			return MasinaResult.INVALID_NAME;

		for (String d: m.getDiskovi()) {
			if (Main.diskovi.nadjiDisk(d) == null)
				return MasinaResult.DISK_NOT_EXISTS;
		}
		
		m.getOrganizacijaRef().dodajResurs(m.getIme());		
		m.setDiskovi(m.getDiskovi());
		
		this.masine.add(m);
		this.store();
		Main.diskovi.store();
		return MasinaResult.OK;

	}

	public MasinaResult obrisiMasinu(VirtuelnaMasina m) throws Exception {

		VirtuelnaMasina masina = this.nadjiMasinu(m.getIme());
		if (masina == null) 
			return MasinaResult.DOESNT_EXIST;

		masina.getOrganizacijaRef().obrisiResurs(masina.getIme());
		masina.setDiskovi(new ArrayList<String>());
		
		this.masine.remove(masina);
		this.store();
		Main.diskovi.store();
		return MasinaResult.OK;

	}

	public MasinaResult izmeniMasinu(MasinaChange m) throws Exception {

		VirtuelnaMasina masina = this.nadjiMasinu(m.getStaroIme());
		if (masina == null) 
			return MasinaResult.DOESNT_EXIST;
		
		if (this.nadjiMasinu(m.getNovaMasina().getIme()) != null && 
				(!(m.getStaroIme().equals(m.getNovaMasina().getIme()))))
			return MasinaResult.AL_EXISTS;
		
		if (m.getNovaMasina().getOrganizacijaRef().getResursi().contains(m.getNovaMasina().getIme()) && !m.getStaroIme().equals(m.getNovaMasina().getIme()))
			return MasinaResult.INVALID_NAME;
		
		masina.setIme(m.getNovaMasina().getIme());
		masina.setKategorija(m.getNovaMasina().getKategorija());
		masina.setJezgra(m.getNovaMasina().getJezgra());
		masina.setRam(m.getNovaMasina().getRam());
		masina.setGpu(m.getNovaMasina().getGpu());
		masina.setAktivnosti(m.getNovaMasina().getAktivnosti());
		masina.setDiskovi(m.getNovaMasina().getDiskovi());
		
		this.store();
		Main.diskovi.store();
		return MasinaResult.OK;
		
	}
	
	@Override
	public void load() throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(FileNames.MASINE_FILE));
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.equals(""))
				continue;
			this.masine.add(VirtuelnaMasina.parse(line));
		}
		in.close();
		this.loadAktivnosti();
	}

	private void loadAktivnosti() throws IOException, ParseException {

		BufferedReader in = new BufferedReader(new FileReader(FileNames.AKTIVNOSTI_FILE));
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.equals(""))
				continue;
			Aktivnost.loadAktivnost(line);
		}
		in.close();

	}

	@Override
	public void store() throws Exception {
		
		PrintWriter out = new PrintWriter(new FileWriter(FileNames.MASINE_FILE));
		PrintWriter aktivnostiOut = new PrintWriter(new FileWriter(FileNames.AKTIVNOSTI_FILE));
		
		for (VirtuelnaMasina m : this.masine) {
			out.println(m.csvLine());
			out.flush();
			for (Aktivnost a : m.getAktivnosti()) {
				aktivnostiOut.println(m.getIme() + ";" + a.csvLine());
				aktivnostiOut.flush();
			}
		}
		
		out.close();
		aktivnostiOut.close();
	}
	
	public MasinaResult promeniStatusMasine(MasinaChange m) throws Exception {
		
		VirtuelnaMasina masina = this.nadjiMasinu(m.getStaroIme());
		if (masina == null) return MasinaResult.DOESNT_EXIST;
				
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		
		if (!masina.getAktivnosti().isEmpty()) {
			
			Aktivnost trenutnaAktivnost = masina.getAktivnosti().get(masina.getAktivnosti().size() - 1);
			
			if (trenutnaAktivnost.getStatus() == StatusMasine.UPALJENA) {
				trenutnaAktivnost.setDatumGasenja(f.parse(f.format(date)));
				trenutnaAktivnost.setStatus(StatusMasine.UGASENA);
			}
			
			else {
				Aktivnost novaAktivnost = new Aktivnost(f.parse(f.format(date)), null, StatusMasine.UPALJENA);
				masina.getAktivnosti().add(novaAktivnost);
			}
			
		}
		
		else {
			Aktivnost novaAktivnost = new Aktivnost(f.parse(f.format(date)), null, StatusMasine.UPALJENA);
			masina.getAktivnosti().add(novaAktivnost);
		}
		
		this.store();
		return MasinaResult.OK;
		
	}
	
	public ArrayList<VirtuelnaMasina> getMasine() {
		return masine;
	}

	public void setMasine(ArrayList<VirtuelnaMasina> masine) {
		this.masine = masine;
	}
	
}
