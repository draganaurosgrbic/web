package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Kategorija;
import model.beans.Korisnik;
import model.services.OperationResult.KategorijaResult;
import rest.Main;
import rest.RestEntity;
import rest.beans.KategorijaChange;
import rest.beans.RestResponse;

public class KategorijaRest implements RestEntity{
	
	@Override
	public void init() {

		get("/rest/kategorije/pregled", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(Main.kategorije.getKategorije());
			
		});
		
		post("/rest/kategorije/dodavanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			};
			
			try {
				
				Kategorija kategorija = jsonConvertor.fromJson(req.body(), Kategorija.class);
				if (kategorija == null || !kategorija.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				KategorijaResult result = Main.kategorije.dodajKategoriju(kategorija);
				if (result != KategorijaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		post("/rest/kategorije/izmena", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			};
			
			try {
				
				KategorijaChange kategorija = jsonConvertor.fromJson(req.body(), KategorijaChange.class);
				if (kategorija == null || !kategorija.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				KategorijaResult result = Main.kategorije.izmeniKategoriju(kategorija);
				if (result != KategorijaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		post("/rest/kategorije/brisanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			};
			
			try {
				
				Kategorija kategorija = jsonConvertor.fromJson(req.body(), Kategorija.class);
				if (kategorija == null || kategorija.getIme().equals("")) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				KategorijaResult result = Main.kategorije.obrisiKategoriju(kategorija);
				if (result != KategorijaResult.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
	}

}
