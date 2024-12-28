package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Korisnik;
import model.beans.Organizacija;
import model.services.OperationResult.OrganizacijaResponse;
import rest.Main;
import rest.RestEntity;
import rest.beans.RestResponse;
import rest.beans.OrganizacijaChange;

public class OrganizacijaRest implements RestEntity{

	@Override
	public void init() {

		get("/rest/organizacije/pregled", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(k.getMojeOrganizacije());
			
		});
		
		post("/rest/organizacije/dodavanje", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				Organizacija o = jsonConvertor.fromJson(req.body(), Organizacija.class);
				if (o == null || !o.validData()) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				OrganizacijaResponse result = Main.organizacije.dodajOrganizaciju(o);
				if (result != OrganizacijaResponse.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}			
			
		});

		post("/rest/organizacije/izmena", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			try {
				
				OrganizacijaChange o = jsonConvertor.fromJson(req.body(), OrganizacijaChange.class);
				if (o == null || !o.validData()) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				if (!k.getMojeOrganizacije().contains(new Organizacija(o.getStaroIme()))) {
					res.status(403);
					return RestEntity.forbidden();
				}
				
				OrganizacijaResponse result = Main.organizacije.izmeniOrganizaciju(o);
				if (result != OrganizacijaResponse.OK) res.status(400);
				return jsonConvertor.toJson(new RestResponse(result + ""));	
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();	
			}			
			
		});
		
	}

}
