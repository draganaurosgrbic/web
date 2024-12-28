package rest;

import com.google.gson.Gson;

import rest.beans.RestResponse;

public interface RestEntity {
	
	public static Gson jsonConvertor = new Gson();
	public void init();
	
	public static String forbidden() {
		return jsonConvertor.toJson(new RestResponse("PRISTUP NEDOZVOLJENOM RESURSU"));
	}
	
	public static String badRequest() {
		return jsonConvertor.toJson(new RestResponse("NEVALIDNI PODACI"));
	}

}
