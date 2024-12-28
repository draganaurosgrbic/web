package rest.beans;

import model.ValidData;
import model.beans.Organizacija;

public class OrganizacijaChange implements ValidData {

	private String staroIme;
	private Organizacija novaOrganizacija;
	
	public OrganizacijaChange() {
		super();
	}
	
	@Override
	public boolean validData() {

		if (this.staroIme == null || this.staroIme.equals("")) return false;
		if (this.novaOrganizacija == null) return false;
		return this.novaOrganizacija.validData();
		
	}
	
	public String getStaroIme() {
		return staroIme;
	}
	
	public void setStaroIme(String staroIme) {
		this.staroIme = staroIme;
	}
	
	public Organizacija getNovaOrganizacija() {
		return novaOrganizacija;
	}
	
	public void setNovaOrganizacija(Organizacija novaOrganizacija) {
		this.novaOrganizacija = novaOrganizacija;
	}
	
}
