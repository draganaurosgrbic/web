Vue.component("korisnici", {

    data: function(){
        return{
            korisnici: [], 
            selectedKorisnik: {}, 
            selectedKorisnikId: '', 
            selected: false,
            greskaIme: '', 
            greskaPrezime: '', 
            greskaUloga: '',
            greskaServer: '',
            greska: false, 
            uloga: '',
            uloge: []
        }
    }, 

    template: `
    
        <div>

            <div v-if="selected">

                <h1>Izmena korisnika</h1>
                
    			<br>
    			
    			<div class="izmena">   
    			         
    				<table>		
    				
		                <tr><td class="left">Email: </td> <td class="right" colspan="2"><input type="text" v-model="selectedKorisnik.email" disabled></td></tr>
		                <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="selectedKorisnik.ime"></td> <td>{{greskaIme}}</td></tr>
		                <tr><td class="left">Prezime: </td> <td class="right"><input type="text" v-model="selectedKorisnik.prezime"></td> <td>{{greskaPrezime}}</td></tr>
		                
		                <tr><td class="left">Uloga: </td> 
		                <td class="right"><input type="text" v-model="selectedKorisnik.uloga" v-bind:hidden="selectedKorisnik.uloga!='SUPER_ADMIN'" disabled>
		                <select v-model="selectedKorisnik.uloga" v-bind:hidden="selectedKorisnik.uloga=='SUPER_ADMIN'">
		                    <option v-for="u in uloge">
		                        {{u}}
		                    </option>
		                </select> {{greskaUloga}}
		                </td></tr>
		                
		                <tr><td class="left">Organizacija: </td> <td class="right" colspan="2"><input type="text" v-model="selectedKorisnik.organizacija" disabled></td></tr>
		                <tr><td colspan="3"><br><button v-on:click="izmeni()">IZMENI</button><br></td></tr>
		                <tr><td colspan="3"><br><button v-on:click="obrisi()">OBRISI</button><br></td></tr>
		                <tr><td colspan="3">{{greskaServer}}<br></td></tr>
		                
		            </table>
		            
    				<button v-on:click="vratiNaKorisnike()">POVRATAK</button>
		            
    			</div>
    			
            </div>

            <div v-if="!selected">

                <h1>Registrovani korisnici</h1>
                
                <br>
                
	            <div class="main">
		                
	    			<div class="left">
	    			
	    				<table class="data" border="1">
	    				
			                <tr><th>Email</th><th>Ime</th><th>Prezime</th><th v-if="uloga=='SUPER_ADMIN'">Organizacija</th></tr>
			                <tr v-for="k in korisnici" v-on:click="selectKorisnik(k)">
			                    <td>{{k.email}}</td>
			                    <td>{{k.ime}}</td>
			                    <td>{{k.prezime}}</td>
			                    <td v-if="uloga=='SUPER_ADMIN'">{{k.organizacija}}</td>
			                </tr>
			                
		                </table><br><br>
		                
	                </div>
                
	    			<div class="right">
		    			
		    			<table class="right_menu">
		    			
			    			<tr><td>
			    			
			    				<table>		
			    					    					
			    					<tr v-if="uloga=='SUPER_ADMIN'"><td><router-link to="/kategorije">KATEGORIJE</router-link></td></tr>
			    					<tr v-if="uloga=='SUPER_ADMIN'"><td><router-link to="/organizacije">ORGANIZACIJE</router-link></td></tr>
		    						<tr v-if="uloga=='ADMIN'"><td><router-link to="/mojaOrganizacija">MOJA ORGANIZACIJA</router-link></td><tr>
		    						<tr><td><router-link to="/masine">MASINE</router-link></td></tr>
		    						<tr><td><router-link to="/diskovi">DISKOVI</router-link></td></tr>
		    						<tr><td><router-link to="/profil">PROFIL</router-link></td></tr>
		    						<tr><td><br><button v-on:click="logout()">ODJAVA</button><br><br></td></tr>
		    						
			    				</table>
			    		
			   				</td></tr>
			   				
					        <tr v-if="uloga!='KORISNIK'"><td>
					        	<br>
				                <button v-on:click="dodaj()">DODAJ KORISNIKA</button><br><br>
    						</td></tr>
    						                    	
                    	</table>
    					
			   		</div>
			   		
	            </div>
	            
            </div>
            
        </div>
    `, 

    mounted(){
    	
    	axios.get("rest/check/korisnik")
        .catch(error => {
            this.$router.push("masine");
        });
    	
    	axios.get("rest/user/uloga")
        .then(response => {
            this.uloga = response.data.result;
        })
        .catch(error => {
            this.$router.push("masine");
        });
    	
        axios.get("rest/korisnici/pregled")
        .then(response => {
            this.korisnici = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });

        axios.get("rest/uloge/unos/pregled")
        .then(response => {
            this.uloge = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });
        
        
    }, 

    methods: {
        
        osvezi: function(){
        	this.greskaIme = '';
            this.greskaPrezime = '';
            this.greskaUloga = '';
            this.greskaServer = '';
            this.greska = false;
        },
        
        selectKorisnik: function(korisnik){
            this.selectedKorisnik = korisnik;
            this.selectedKorisnikId = korisnik.email;
            this.selected = true;
        },

        dodaj: function(){
            this.$router.push("dodajKorisnika");
        }, 

        obrisi: function(){
        	
        	let temp = confirm("Da li ste sigurni?");
        	if (!temp) return;

            this.selectedKorisnik.email = this.selectedKorisnikId;

            axios.post("rest/korisnici/brisanje", this.selectedKorisnik)
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });

        }, 

        izmeni: function(){
        	
        	this.osvezi();

            if (this.selectedKorisnik.ime == ''){
                this.greskaIme = "Ime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.selectedKorisnik.prezime == ''){
                this.greskaPrezime = "Prezime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.selectedKorisnik.uloga == ''){
            	this.greskaUloga = "Uloga ne sme biti prazna. ";
            	this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/korisnici/izmena", {"staroIme": this.selectedKorisnikId, "noviKorisnik": this.selectedKorisnik})
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });

        },
        
        vratiNaKorisnike: function() {
            location.reload();
        },

        logout: function(){
            axios.get("rest/user/logout")
            .then(response => {
                this.$router.push("/");
            })
            .catch(error => {
                this.$router.push("/");
            });
        }
    }

});