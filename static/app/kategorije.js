Vue.component("kategorije", {

    data: function(){
        return{
            kategorije: [], 
            selectedKategorija: {}, 
            selectedKategorijaId: '', 
            selected: false, 
            greskaIme: '', 
            greskaJezgra: '', 
            greskaRam: '', 
            greskaGpu: '', 
            greskaServer: '', 
            greska: false
        }
    }, 

    template: `
    
        <div>

            <div v-if="selected">

                <h1>Izmena kategorije</h1>
                
    			<br>
    			
    			<div class="izmena">
    				
    				<table>		
    				
		                <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="selectedKategorija.ime"></td> <td>{{greskaIme}}</td></tr>
		                <tr><td class="left">Broj jezgara: </td> <td class="right"><input type="text" v-model="selectedKategorija.jezgra"></td> <td>{{greskaJezgra}}</td></tr>
		                <tr><td class="left">RAM: </td> <td class="right"><input type="text" v-model="selectedKategorija.ram"></td> <td>{{greskaRam}}</td></tr>
		                <tr><td class="left">GPU jezgra: </td> <td class="right"><input type="text" v-model="selectedKategorija.gpu"></td> <td>{{greskaGpu}}</td></tr>
		                <tr><td colspan="3"><br><button v-on:click="izmeni()">IZMENI</button><br></td></tr>
		                <tr><td colspan="3"><br><button v-on:click="obrisi()">OBRISI</button><br></td></tr>
		                <tr><td colspan="3">{{greskaServer}}<br></td></tr>
		                
    				</table>
    				
    				<button v-on:click="vratiNaKategorije()">POVRATAK</button>

    			</div>
    			
    		</div>

            <div v-if="!selected">

                <h1>Registrovane kategorije</h1>
                
                <br>
                
	            <div class="main">
		                
	    			<div class="left">
	    			
		    			<table class="data" border="1">
			                <tr><th>Ime</th><th>Broj jezgara</th><th>RAM</th><th>GPU jezgra</th></tr>
			                <tr v-for="k in kategorije" v-on:click="selectKategorija(k)">
			                    <td>{{k.ime}}</td>
			                    <td>{{k.jezgra}}</td>
			                    <td>{{k.ram}}</td>
			                    <td>{{k.gpu}}</td>
			                </tr> 
	                	</table><br><br>
	                	
	                </div>
                
	    			<div class="right">
		    			
		    			<table class="right_menu">
		    			
			    			<tr><td>
			    			
			    				<table>
			    					<tr><td><router-link to="/korisnici">KORISNICI</router-link></td></tr>
			    					<tr><td><router-link to="/organizacije">ORGANIZACIJE</router-link></td></tr>
		    						<tr><td><router-link to="/masine">MASINE</router-link></td></tr>
		    						<tr><td><router-link to="/diskovi">DISKOVI</router-link></td></tr>
		    						<tr><td><router-link to="/profil">PROFIL</router-link></td></tr>
		    						<tr><td><br><button v-on:click="logout()">ODJAVA</button><br><br></td></tr>
			    				</table>
			    		
			   				</td></tr>
    					
					        <tr><td>
					        	<br>
				                <button v-on:click="dodaj()">DODAJ KATEGORIJU</button><br><br>
    						</td></tr>
    						                    	
                    	</table>
    					
			   		</div>
			   		
	            </div>
                
            </div>
            
        </div>
    `, 

    mounted(){
    	
    	axios.get("rest/check/super")
        .catch(error => {
            this.$router.push("masine");
        });

        axios.get("rest/kategorije/pregled")
        .then(response => {
            this.kategorije = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });

    }, 

    methods: {
    	
    	osvezi: function(){
        	this.greskaIme = '';
            this.greskaJezgra = '';
            this.greskaRam = '';
            this.greskaGpu = '';
            this.greskaServer = '';
            this.greska = false;
        },	

        selectKategorija: function(kategorija){
            this.selectedKategorija = kategorija;
            this.selectedKategorijaId = kategorija.ime;
            this.selected = true;
        }, 
        
        dodaj: function(){
            this.$router.push("dodajKategoriju");
        },

        obrisi: function(){
        	
        	let temp = confirm("Da li ste sigurni?");
        	if (!temp) return;

            this.selectedKategorija.ime = this.selectedKategorijaId;
            if (isNaN(parseInt(this.selectedKategorija.jezgra))){
            	this.selectedKategorija.jezgra = 0;
            }
            else{
            	this.selectedKategorija.jezgra = parseInt(this.selectedKategorija.jezgra);
            }
            if (isNaN(parseInt(this.selectedKategorija.ram))){
            	this.selectedKategorija.ram = 0;
            }
            else{
            	this.selectedKategorija.ram = parseInt(this.selectedKategorija.ram);
            }
            if (isNaN(parseInt(this.selectedKategorija.gpu))){
            	this.selectedKategorija.gpu = 0;
            }
            else{
            	this.selectedKategorija.jezgra = parseInt(this.selectedKategorija.gpu);
            }
            
            axios.post("rest/kategorije/brisanje", this.selectedKategorija)
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });

        },

        izmeni: function(){

            this.osvezi();

            if (this.selectedKategorija.ime == ''){
                this.greskaIme = "Ime ne sme biti prazno";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.selectedKategorija.jezgra)) || parseInt(this.selectedKategorija.jegra) <= 0){
                this.greskaJezgra = "Broj jezgara mora biti pozitivan ceo broj. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.selectedKategorija.ram)) || parseInt(this.selectedKategorija.ram) <= 0){
                this.greskaRam= "RAM mora biti pozitivan ceo broj. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.selectedKategorija.gpu)) || parseInt(this.selectedKategorija.gpu) < 0){
                this.greskaGpu = "GPU jezgra moraju biti nenegativan ceo broj. ";
                this.greska = true;
            }
            
            if (this.greska) return;
            
            this.selectedKategorija.jezgra = parseInt(this.selectedKategorija.jezgra);
            this.selectedKategorija.ram = parseInt(this.selectedKategorija.ram);
            this.selectedKategorija.gpu = parseInt(this.selectedKategorija.gpu);

            axios.post("rest/kategorije/izmena", {"staroIme": this.selectedKategorijaId, "novaKategorija": this.selectedKategorija})
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });

        },

        vratiNaKategorije: function() {
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