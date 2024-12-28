package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Disk;
import model.beans.Korisnik;
import model.services.OperationResult.DiskResult;
import rest.Main;
import rest.RestEntity;
import rest.beans.DiskChange;
import rest.beans.RestResponse;

public class DiskRest implements RestEntity {

	@Override
	public void init() {

		get("/rest/diskovi/pregled", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(k.getMojiDiskovi());
			
		});
		
		post("/rest/diskovi/dodavanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				Disk d = jsonConvertor.fromJson(req.body(), Disk.class);
				if (d == null || !d.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				if (!k.getMojeOrganizacije().contains(d.getOrganizacijaRef())) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				if (d.getMasinaRef() != null && !k.getMojeMasine().contains(d.getMasinaRef())) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				DiskResult result = Main.diskovi.dodajDisk(d);
				if (result != DiskResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		post("/rest/diskovi/izmena", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				DiskChange d = jsonConvertor.fromJson(req.body(), DiskChange.class);
				if (d == null || !d.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				if (!k.getMojiDiskovi().contains(new Disk(d.getStaroIme()))) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				if (d.getNoviDisk().getMasinaRef() != null && !k.getMojeMasine().contains(d.getNoviDisk().getMasinaRef())) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				DiskResult result = Main.diskovi.izmeniDisk(d);
				if (result != DiskResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}
			
		});
		
		post("/rest/diskovi/brisanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				Disk d = jsonConvertor.fromJson(req.body(), Disk.class);
				if (d == null || d.getIme().equals("")) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojiDiskovi().contains(d)) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				DiskResult result = Main.diskovi.obrisiDisk(d);
				if (result != DiskResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();					
			}
			
		});
		
	}

}
