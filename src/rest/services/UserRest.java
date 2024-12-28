package rest.services;

import static spark.Spark.get;
import static spark.Spark.post;

import model.Uloga;
import model.beans.Korisnik;
import rest.Main;
import rest.RestEntity;
import rest.beans.RestResponse;
import rest.beans.User;

public class UserRest implements RestEntity {

	@Override
	public void init() {
		
		get("/rest/check/super", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.SUPER_ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(new RestResponse("OK"));
			
		});
		
		get("/rest/check/admin", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || !k.getUloga().equals(Uloga.ADMIN)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(new RestResponse("OK"));
			
		});
		
		get("/rest/check/korisnik", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null || k.getUloga().equals(Uloga.KORISNIK)) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(new RestResponse("OK"));

			
		});
		
		get("/rest/user/uloga", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(new RestResponse(k.getUloga() + ""));
			
		});
		
		post("/rest/user/login", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k != null) {
				res.status(403);
				return jsonConvertor.toJson(new RestResponse("Vec ste prijavljeni. Prvo se odlogujte. "));
			}
			
			try {
				
				User u = jsonConvertor.fromJson(req.body(), User.class);
				if (u == null || !u.validData()) {
					res.status(400);
					return RestEntity.badRequest();	
				}
				
				k = Main.korisnici.login(u);
				if (k == null) {
					res.status(400);
					return jsonConvertor.toJson(new RestResponse("Unet korisnik ne postoji. "));
				}
				
				req.session(true).attribute("korisnik", k);
				return jsonConvertor.toJson(new RestResponse("OK"));
				
			}
			
			catch(Exception e) {
				res.status(400);
				return RestEntity.badRequest();
			}
			
		});
		
		get("/rest/user/logout", (req, res) -> {
			
			res.type("application/json");
			req.session(true).invalidate();
			return jsonConvertor.toJson(new RestResponse("OK"));
			
		});
		
		get("/rest/user/profil", (req, res) -> {
			
			res.type("application/json");
			Korisnik k = (Korisnik) req.session(true).attribute("korisnik");
			
			if (k == null) {
				res.status(403);
				return RestEntity.forbidden();
			}
			
			return jsonConvertor.toJson(k);
			
		});
		
	}

}
