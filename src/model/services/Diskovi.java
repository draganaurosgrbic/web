package model.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.FileNames;
import model.LoadStoreData;
import model.beans.Disk;
import model.services.OperationResult.DiskResult;
import rest.beans.DiskChange;

public class Diskovi implements LoadStoreData {

	private ArrayList<Disk> diskovi;

	public Diskovi() {
		super();
		this.diskovi = new ArrayList<Disk>();
	}

	public Disk nadjiDisk(String ime) {
		
		int index = this.diskovi.indexOf(new Disk(ime));
		if (index == -1) return null;
		return this.diskovi.get(index);
		
	}
	
	public DiskResult dodajDisk(Disk d) throws Exception {
		
		if (this.nadjiDisk(d.getIme()) != null) 
			return DiskResult.AL_EXISTS;
		
		if (d.getOrganizacijaRef() == null)
			return DiskResult.ORG_DOESNT_EXIST;

		if (d.getOrganizacijaRef().getResursi().contains(d.getIme()))
			return DiskResult.INVALID_NAME;
				
		d.getOrganizacijaRef().dodajResurs(d.getIme());
		if (d.getMasinaRef() != null)
			d.getMasinaRef().dodajDisk(d.getIme());
		
		this.diskovi.add(d);
		this.store();
		return DiskResult.OK;
		
	}
	
	public DiskResult obrisiDisk(Disk d) throws Exception {
		
		Disk disk = this.nadjiDisk(d.getIme());
		if (disk == null) 
			return DiskResult.DOESNT_EXIST;

		disk.getOrganizacijaRef().obrisiResurs(disk.getIme());
		if (disk.getMasinaRef() != null)
			disk.getMasinaRef().obrisiDisk(disk.getIme());
		
		this.diskovi.remove(disk);
		this.store();
		return DiskResult.OK;
		
	}

	public DiskResult izmeniDisk(DiskChange d) throws Exception {
		
		Disk disk = this.nadjiDisk(d.getStaroIme());
		if (disk == null) 
			return DiskResult.DOESNT_EXIST;
		
		if (this.nadjiDisk(d.getNoviDisk().getIme()) != null && 
				(!(d.getStaroIme().equals(d.getNoviDisk().getIme())))) 
			return DiskResult.AL_EXISTS;
		
		if (d.getNoviDisk().getOrganizacijaRef().getResursi().contains(d.getNoviDisk().getIme()) && !d.getStaroIme().equals(d.getNoviDisk().getIme()))
			return DiskResult.INVALID_NAME;
		
		disk.setIme(d.getNoviDisk().getIme());
		disk.setTip(d.getNoviDisk().getTip());
		disk.setKapacitet(d.getNoviDisk().getKapacitet());
		disk.setMasina(d.getNoviDisk().getMasina());
		
		this.store();
		return DiskResult.OK;
		
	}
	
	@Override
	public void load() throws Exception {

		BufferedReader in = new BufferedReader(new FileReader(FileNames.DISKOVI_FILE));
		String line;
		while ((line = in.readLine()) != null) {
			line = line.trim();
			if (line.equals(""))
				continue;
			this.diskovi.add(Disk.parse(line));
		}
		in.close();
	}

	@Override
	public void store() throws Exception {

		PrintWriter out = new PrintWriter(new FileWriter(FileNames.DISKOVI_FILE));
		for (Disk d : this.diskovi) {
			out.println(d.csvLine());
			out.flush();
		}
		out.close();
	}
	
	public ArrayList<Disk> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(ArrayList<Disk> diskovi) {
		this.diskovi = diskovi;
	}
	
}
