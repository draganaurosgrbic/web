package model.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.CSVData;
import model.StatusMasine;
import rest.Main;

public class Aktivnost implements CSVData {

	private Date datumPaljenja;
	private Date datumGasenja;
	private StatusMasine status;

	public Aktivnost() {
		super();
	}

	public Aktivnost(Date datumPaljenja, Date datumGasenja, StatusMasine status) {
		this();
		this.datumPaljenja = datumPaljenja;
		this.datumGasenja = datumGasenja;
		this.status = status;
	}

	public static void loadAktivnost(String line) throws ParseException {

		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy. hh:mm:ss");
		String[] array = line.split(";");
		VirtuelnaMasina masina = Main.masine.nadjiMasinu(array[0].trim());
		
		Date datumPaljenja = f.parse(array[1].trim());
		Date datumGasenja = null;
		if (!array[2].trim().equals("/"))
			datumGasenja = f.parse(array[2].trim());
		
		StatusMasine status = StatusMasine.valueOf(array[3].trim());
		masina.dodajAktivnost(new Aktivnost(datumPaljenja, datumGasenja, status));

	}

	@Override
	public String csvLine() {
		
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy. hh:mm:ss");

		if (this.datumGasenja == null)
			return f.format(this.datumPaljenja) + ";" + "/" + ";" + this.status;
		
		else
			return f.format(this.datumPaljenja) + ";" + f.format(this.datumGasenja) + ";" + this.status;
		
	}

	public Date getDatumPaljenja() {
		return datumPaljenja;
	}

	public void setDatumPaljenja(Date datumPaljenja) {
		this.datumPaljenja = datumPaljenja;
	}

	public Date getDatumGasenja() {
		return datumGasenja;
	}

	public void setDatumGasenja(Date datumGasenja) {
		this.datumGasenja = datumGasenja;
	}

	public StatusMasine getStatus() {
		return status;
	}

	public void setStatus(StatusMasine status) {
		this.status = status;
	}

}
