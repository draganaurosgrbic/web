package model.services;

public class OperationResult {
	
	public enum KategorijaResult {
		
		OK, AL_EXISTS, DOESNT_EXIST, CANT_DELETE;
		
		@Override
		public String toString() {

			switch(this.ordinal()) {
			case 0: return "OK";
			case 1: return "Uneto ime kategorije vec postoji. ";
			case 2: return "Uneta kategorija ne postoji. ";
			default: return "Kategorija poseduje masine. Nije moguce brisanje. ";
			}
		}
	}
	
	public enum OrganizacijaResponse {
		
		OK, AL_EXISTS, DOESNT_EXIST;
		
		@Override
		public String toString() {

			switch(this.ordinal()) {
			case 0: return "OK";
			case 1: return "Uneto ime organizacije vec postoji. ";
			default: return "Uneta organizacija ne postoji. ";
			}
		}
	}
	
	public enum KorisnikResult{
		
		OK, AL_EXISTS, DOESNT_EXIST, ORG_DOESNT_EXIST, CANT_DEL_SELF;
		
		@Override
		public String toString() {

			switch(this.ordinal()) {
			case 0: return "OK";
			case 1: return "Unet email korisnika vec postoji. ";
			case 2: return "Unet korisnik ne postoji. ";
			case 3: return "Uneta organizacija ne postoji. ";
			default: return "Nije moguce brisanje samog sebe. ";
			}
		}
		
	}
	
	public enum MasinaResult {
		
		OK, AL_EXISTS, DOESNT_EXIST, ORG_DOESNT_EXIST, INVALID_NAME, DISK_NOT_EXISTS;
		
		@Override
		public String toString() {

			switch(this.ordinal()) {
			case 0:	return "OK";
			case 1: return "Uneto ime masine vec postoji. ";
			case 2: return "Uneta masina ne postoji. ";
			case 3: return "Uneta organizacija ne postoji. ";
			case 4: return "Nevalidno ime za odabranu organizaciju. ";
			default: return "Ne postoje svi uneti diskovi. ";
			}
		}
		
	}
	
	public enum DiskResult {
		
		OK, AL_EXISTS, DOESNT_EXIST, ORG_DOESNT_EXIST, INVALID_NAME;
		
		@Override
		public String toString() {

			switch(this.ordinal()) {
			case 0: return "OK";
			case 1: return "Uneto ime diska vec postoji. ";
			case 2: return "Unet disk ne postoji. ";
			case 3: return "Uneta organizacija ne postoji. ";
			default: return "Nevalidno ime za odabranu organizaciju. ";
			}
		}
	}

}
