package rest.beans;

import model.ValidData;
import model.beans.Disk;

public class DiskChange implements ValidData {

	private String staroIme;
	private Disk noviDisk;

	public DiskChange() {
		super();
	}

	@Override
	public boolean validData() {
		if (this.staroIme == null || this.staroIme.equals("")) return false;
		if (this.noviDisk == null) return false;
		return this.noviDisk.validData();
	}
	
	public String getStaroIme() {
		return staroIme;
	}

	public void setStaroIme(String staroIme) {
		this.staroIme = staroIme;
	}

	public Disk getNoviDisk() {
		return noviDisk;
	}

	public void setNoviDisk(Disk noviDisk) {
		this.noviDisk = noviDisk;
	}
	
}
