package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.TipDiska;
import model.Uloga;
import model.beans.Korisnik;
import rest.Main;
import rest.RestEntity;
import rest.beans.RacunZahtev;

public class DataRest implements RestEntity{

	@Override
	public void init() {
		
		get("/rest/uloge/unos/pregled", (req, res) -> {
			
			res.type("application/json");
			return jsonConvertor.toJson(new Uloga[] {Uloga.ADMIN, Uloga.KORISNIK});
			
		});
		
		get("/rest/diskovi/unos/pregled", (req, res) -> {
			
			res.type("application/json");
			return jsonConvertor.toJson(TipDiska.values());
			
		});
		
		get("/rest/kategorije/unos/pregled", (req, res) -> {
			
			res.type("application/json");
			return jsonConvertor.toJson(Main.kategorije.getKategorije());
			
		});

		post("/rest/masine/izracunajRacun", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				RacunZahtev zahtev = jsonConvertor.fromJson(req.body(), RacunZahtev.class);
				if (zahtev == null || !zahtev.validData()) {
					res.status(400);
					return RestEntity.badRequest();
				}
				
				return jsonConvertor.toJson(k.izracunajRacun(zahtev));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
	}
	
}
