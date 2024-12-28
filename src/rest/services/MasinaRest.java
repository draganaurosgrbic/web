package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Korisnik;
import model.beans.VirtuelnaMasina;
import model.services.OperationResult.MasinaResult;
import rest.Main;
import rest.RestEntity;
import rest.beans.MasinaChange;
import rest.beans.RestResponse;

public class MasinaRest implements RestEntity{

	@Override
	public void init() {

		get("/rest/masine/pregled", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(k.getMojeMasine());
			
		});
		
		post("/rest/masine/dodavanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				VirtuelnaMasina m = jsonConvertor.fromJson(req.body(), VirtuelnaMasina.class);
				if (m == null || !m.validData()) {
					res.status(400);
					return RestEntity.badRequest();	
				}				
				
				if (!k.getMojeOrganizacije().contains(m.getOrganizacijaRef())) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				for (String d: m.getDiskovi()) {
					if (!k.getMojeOrganizacije().contains(Main.diskovi.nadjiDisk(d).getOrganizacijaRef())) {
						res.status(403);
						return RestEntity.forbidden();						
					}
				}
								
				m.setKategorija(Main.kategorije.nadjiKategoriju(m.getKategorija().getIme()));
				if (m.getKategorija() == null) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				MasinaResult result = Main.masine.dodajMasinu(m);
				if (result != MasinaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}
		});
		
		post("/rest/masine/izmena", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				MasinaChange m = jsonConvertor.fromJson(req.body(), MasinaChange.class);
				if (m == null || !m.validData()) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojeMasine().contains(new VirtuelnaMasina(m.getStaroIme()))) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				for (String d: m.getNovaMasina().getDiskovi()) {
					if (!k.getMojeOrganizacije().contains(Main.diskovi.nadjiDisk(d).getOrganizacijaRef())) {
						res.status(403);
						return RestEntity.forbidden();						
					}
				}
				
				m.getNovaMasina().setKategorija(Main.kategorije.nadjiKategoriju(m.getNovaMasina().getKategorija().getIme()));
				if (m.getNovaMasina().getKategorija() == null) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				MasinaResult result = Main.masine.izmeniMasinu(m);
				if (result != MasinaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}
			
		});
		
		post("/rest/masine/brisanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				VirtuelnaMasina m = jsonConvertor.fromJson(req.body(), VirtuelnaMasina.class);
				if (m == null || m.getIme().equals("")) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojeMasine().contains(m)) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				MasinaResult result = Main.masine.obrisiMasinu(m);
				if (result != MasinaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}
		});
		

		post("rest/masine/status", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				VirtuelnaMasina m = Main.masine.nadjiMasinu(jsonConvertor.fromJson(req.body(), String.class));
				if (m == null) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojeMasine().contains(m)) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				return jsonConvertor.toJson(m.status());
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}
			
		});
		
		post("rest/masine/promeniStatus", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
			
				MasinaChange m = jsonConvertor.fromJson(req.body(), MasinaChange.class);
				if (m == null) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojeMasine().contains(new VirtuelnaMasina(m.getStaroIme()))) {
					res.status(403);
					return RestEntity.forbidden();
				}
								
				MasinaResult result = Main.masine.promeniStatusMasine(m);
				if (result != MasinaResult.OK) res.status(400);
				return jsonConvertor.toJson(Main.masine.nadjiMasinu(m.getStaroIme()));

			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}

		});
		
	}

}
