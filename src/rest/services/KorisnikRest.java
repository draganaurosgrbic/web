package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Korisnik;
import model.services.OperationResult.KorisnikResult;
import rest.Main;
import rest.RestEntity;
import rest.beans.KorisnikChange;
import rest.beans.RestResponse;

public class KorisnikRest implements RestEntity{

	@Override
	public void init() {
		
		get("/rest/korisnici/pregled", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(k.getMojiKorisnici());
			
		});
		
		post("/rest/korisnici/dodavanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				Korisnik korisnik = jsonConvertor.fromJson(req.body(), Korisnik.class);
				if (korisnik == null || !korisnik.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				if (!k.getMojeOrganizacije().contains(korisnik.getOrganizacijaRef())) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				KorisnikResult result = Main.korisnici.dodajKorisnika(korisnik);
				if (result != KorisnikResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		post("/rest/korisnici/izmena", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				KorisnikChange korisnik = jsonConvertor.fromJson(req.body(), KorisnikChange.class);
				if (korisnik == null || !korisnik.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				if (!k.getMojiKorisnici().contains(new Korisnik(korisnik.getStaroIme()))) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				KorisnikResult result = Main.korisnici.izmeniKorisnika(korisnik, k);
				if (result != KorisnikResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		post("/rest/korisnici/brisanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				Korisnik korisnik = jsonConvertor.fromJson(req.body(), Korisnik.class);
				if (korisnik == null || korisnik.getEmail().equals("")) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				if (!k.getMojiKorisnici().contains(korisnik)) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				KorisnikResult result = Main.korisnici.obrisiKorisnika(korisnik, k);
				if (result != KorisnikResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
	
	}

}
