package rest.beans;

public class RestResponse {
	
	private String result;

	public RestResponse() {
		super();
	}

	public RestResponse(String result) {
		super();
		this.result = result;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
