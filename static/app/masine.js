Vue.component("masine", {

    data: function(){
        return {
            masine: [], 
            backup: [],
            selectedMasina: {}, 
            selectedMasinaId: '',
            selectedMasinaStatus: '',
            selected: false, 
            greskaIme: '',
            greskaPocetni: '',
            greskaKrajnji: '',
            greskaServer: '', 
            greska: false, 
            pretragaIme: '',
            pretragaMinJezgra: '', 
            pretragaMaxJezgra: '', 
            pretragaMinRam: '', 
            pretragaMaxRam: '', 
            pretragaMinGpu: '',
            pretragaMaxGpu: '',
            pocetniDatum: '',
            krajnjiDatum: '',
            uloga: '', 
            kategorije: [],
        	diskovi: [],
            kat: '',
            prikaziRacun: false,
            racun: {
            	'racuniMasine' : {},
            	'racuniDiskovi' : {},
            	'ukupniRacun' : ''
            }
        }
    }, 

    template: `

        <div class="masine">

            <div v-if="selected && !prikaziRacun">

	            <h1>Izmena masine</h1>
	                
	            <br>
	                
    			<div class="izmena_masine">
	                
	                <div class="izmena_ui">
	                
	                	<table>
	                	
			                <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="selectedMasina.ime" v-bind:disabled="uloga=='KORISNIK'"></td> <td>{{greskaIme}}</td></tr>
			                <tr><td class="left">Organizacija: </td> <td class="right" colspan="2"><input type="text" v-model="selectedMasina.organizacija" disabled></td></tr>
			                			          
			                <tr><td class="left">Diskovi: </td>
			                <td class="right" colspan="2"><select multiple disabled>
				                <option v-for="d in selectedMasina.diskovi">
				                    {{d}}
				                </option>
			                </select></td></tr>
			                
			                <tr v-if="uloga!='KORISNIK'"><td class="left">Izmena diskova: </td>
			                <td class="right" colspan="2"><select v-model="selectedMasina.diskovi" multiple>
				                <option v-for="d in diskovi" v-if="d.organizacija === selectedMasina.organizacija" v-bind:selected="selectedMasina.diskovi.filter(disk => disk === d.ime).length > 0">
				                    {{d.ime}}
				                </option>
			                </select></td></tr>
			                
			                <tr><td class="left">Kategorija: </td>
			                <td class="right" colspan="2"><select v-model="kat" v-bind:disabled="uloga=='KORISNIK'">
			                    <option v-for="k in kategorije">
			                        {{k.ime}}
			                    </option>
			                </select></td></tr>
			                
			                <tr><td class="left">Broj jezgara: </td> <td class="right" colspan="2"><input type="text" v-model="selectedMasina.jezgra" disabled></td></tr>
			                <tr><td class="left">RAM: </td> <td class="right" colspan="2"><input type="text" v-model="selectedMasina.ram" disabled></td></tr>
			                <tr><td class="left">Broj GPU jezgara: </td> <td class="right" colspan="2"><input type="text" v-model="selectedMasina.gpu" disabled></td></tr>
				            
				            <tr v-if="uloga!='KORISNIK'"><td colspan="3"><br><button v-on:click="izmeni()">IZMENI</button><br></td></tr>
				            <tr v-if="uloga!='KORISNIK'"><td colspan="3"><br><button v-on:click="obrisi()">OBRISI</button><br></td></tr>
			           		<tr><td colspan="3">{{greskaServer}}<br></td></tr>
			           		    
    					</table>
    					
    					<button v-on:click="vratiNaMasine">POVRATAK</button>

	    			</div>
	    			
	    			<div class="aktivnosti">
	    			
	    				<h1> Aktivnosti </h1>
	    				
	    				<br>
	    				
		                <p v-if="selectedMasina.aktivnosti.length==0">NEMA</p>
		                
		                <div>
			                <table v-if="selectedMasina.aktivnosti.length!=0">
			                	<th>Datum paljenja</th><th>Datum Gasenja</th>
			                	<tr v-for="a in selectedMasina.aktivnosti">
			                		<td>{{a.datumPaljenja}}</td> <td>{{a.datumGasenja}}</td>
			                	</tr>
			                </table>
		                </div>
		                
		                <div>
			                Status: {{selectedMasinaStatus}}<br><br>
		    			 	<div v-if="uloga!='KORISNIK'">
			               		<button v-if="selectedMasinaStatus == 'UGASENA'" v-on:click="promeni_status()">UPALI MASINU</button>
			                	<button v-if="selectedMasinaStatus == 'UPALJENA'" v-on:click="promeni_status()">UGASI MASINU</button>
			                </div><br><br>
		                </div>
		                
	    			</div>
    			
    			</div>
    			
            </div>
        
            <div v-if="!selected && !prikaziRacun">
            
    			<h1>Registrovane masine</h1>

	            <br>
	            
	            <div class="main">
		                
	    			<div class="left">
		                
		                <table class="data" border="1">
			                <tr><th>Ime</th><th>Broj jezgara</th><th>RAM</th><th>GPU jezgra</th><th v-if="uloga=='SUPER_ADMIN'">Organizacija</th></tr>
			                <tr v-for="m in masine" v-on:click="selectMasina(m)">
			                    <td>{{m.ime}}</td>
			                    <td>{{m.jezgra}}</td>
			                    <td>{{m.ram}}</td>
			                    <td>{{m.gpu}}</td>
			                    <td v-if="uloga=='SUPER_ADMIN'">{{m.organizacija}}</td>
			                </tr>
		                </table>
		                
	    			</div>
	    			
	    			<div class="right">
		    			
		    			<table class="right_menu">
		    			
			    			<tr><td>
			    				
			    				<table>
			    					<tr v-if="uloga!='KORISNIK'"><td><router-link to="/korisnici">KORISNICI</router-link></td></tr>
			    					<tr v-if="uloga=='SUPER_ADMIN'"><td><router-link to="/kategorije">KATEGORIJE</router-link></td></tr>
			    					<tr v-if="uloga=='SUPER_ADMIN'"><td><router-link to="/organizacije">ORGANIZACIJE</router-link></td></tr>
    								<tr v-if="uloga=='ADMIN'"><td><router-link to="/mojaOrganizacija">MOJA ORGANIZACIJA</router-link></td><tr>
		    						<tr><td><router-link to="/diskovi">DISKOVI</router-link></td></tr>
		    						<tr><td><router-link to="/profil">PROFIL</router-link></td></tr>
		    						<tr><td><br><button v-on:click="logout()">ODJAVA</button><br><br></td></tr>
			    				</table>
					
					        </td></tr>
					        
					        <tr v-if="uloga!='KORISNIK'"><td>
					        	<br>
			                	<button v-on:click="dodaj()">DODAJ MASINU</button>
			                	<br><br>
					        </td></tr>
			                
			                <tr v-if="uloga=='ADMIN'"><td><div>
					    		Pocetni datum: <input type="date" v-model="pocetniDatum"><br>
					    		{{greskaPocetni}} <br> 
					            Krajnji datum: <input type="date" v-model="krajnjiDatum"><br>
					            {{greskaKrajnji}} <br> 
					            <button v-on:click="izracunajRacun">PRIKAZI RACUN</button><br><br>
				    		</div></td></tr>
				    		
			    			<tr><td>
			    			
						        <h1>Pretraga</h1>
						        
						        <table>
							        <tr><td class="left">Ime: </td> <td><input type="text" v-model="pretragaIme"></td></tr>
							        <tr><td class="left">Min. broj jezgara: </td> <td><input type="number" min="1" v-model="pretragaMinJezgra"></td></tr>
							        <tr><td class="left">Max. broj jezgara: </td> <td><input type="number" min="1" v-model="pretragaMaxJezgra"></td></tr>
							        <tr><td class="left">Min. RAM: </td> <td><input type="number" min="1" v-model="pretragaMinRam"></td></tr>
							        <tr><td class="left">Max. RAM: </td> <td><input type="number" min="1" v-model="pretragaMaxRam"></td></tr>
							        <tr><td class="left">Min. GPU jezgra: </td> <td><input type="number" min="0" v-model="pretragaMinGpu"></td></tr>
							        <tr><td class="left">Max. GPU jezgra: </td> <td><input type="number" min="0" v-model="pretragaMaxGpu"></td></tr>
							        <tr><td colspan="2"><br><button v-on:click="pretrazi()">FILTRIRAJ</button><br><br></td></tr>
							    </table>
							   
			    			</td></tr>
		    					
	    				</table>
	    				
					</div>

				</div>

            </div>
            
            <div v-if="!selected && prikaziRacun">

                <h1>Pregled racuna za odabrani period</h1>
                
                <br>

                <div class="mesecni_racun">
	                
	                <div class="top">
		                <table border="1">
			                <tr><th>Tip uredjaja</th><th>Ime</th><th>Racun</th></tr>
			                
			                <tr v-for="m in masine">
			                    <td>Virtuelna masina</td><td>{{m.ime}}</td><td>{{racun.racuniMasine[m.ime]}}</td>
			                </tr>
			                
			                <tr v-for="d in diskovi">
			                    <td>Disk</td><td>{{d.ime}}</td><td>{{racun.racuniDiskovi[d.ime]}}</td>
			                </tr>
		                </table><br><br>
	                </div>
	                
	                <div class="bottom">
	                	Ukupan racun: <input type="text" v-model="racun.ukupniRacun" disabled><br><br>
	                	<button v-on:click="vratiNaMasine">POVRATAK</button><br><br>
	                </div>
	        	</div>
	                
            </div>

        </div>
    `, 

    watch: {
        kat: function(){
            for (let k of this.kategorije){
                if (k.ime == this.kat){
                    this.selectedMasina.kategorija.ime = k.ime;
                    this.selectedMasina.kategorija.jezgra = k.jezgra;
                    this.selectedMasina.kategorija.ram = k.ram;
                    this.selectedMasina.kategorija.gpu = k.gpu;
                    this.selectedMasina.jezgra = k.jezgra;
                    this.selectedMasina.ram = k.ram;
                    this.selectedMasina.gpu = k.gpu;
                }
            }
        }
    },

    mounted(){
    	
    	axios.get("rest/user/uloga")
        .then(response => {
            this.uloga = response.data.result;

        })
        .catch(error => {
            this.$router.push("/");
        });

        axios.get("rest/masine/pregled")
        .then(response => {
            this.masine = response.data;
            this.backup = response.data;
        })
        .catch(error => {
            this.$router.push("/");
        });
        
        axios.get("rest/kategorije/unos/pregled")
        .then(response => {
            this.kategorije = response.data;

        })
        .catch(error => {
            this.$router.push("/");
        });
        
        axios.get("rest/diskovi/pregled")
        .then(response => {
            this.diskovi = response.data;
        })
        .catch(error => {
            this.$router.push("/");
        });

    },

    methods: {
    	
    	osvezi: function(){
        	this.greskaIme = '';
            this.greskaPocetni = '';
            this.greskaKrajnji = '';
            this.greskaServer = '';
            this.greska = false;
        },

        selectMasina: function(masina){
        	
            this.selectedMasina = masina;
            this.selectedMasinaId = masina.ime;
            this.selected = true;
            this.kat = this.selectedMasina.kategorija.ime;
            
            axios.post("rest/masine/status", this.selectedMasinaId)
            .then(response => {
                this.selectedMasinaStatus = response.data;
            })
            .catch(error => {
                this.$router.push("/");
            });
            
        }, 

        dodaj: function(){
            this.$router.push("dodajMasinu");
        },

        obrisi: function(){
        	
        	let temp = confirm("Da li ste sigurni?");
        	if (!temp) return;
        	
            this.selectedMasina.ime = this.selectedMasinaId;

            axios.post("rest/masine/brisanje", this.selectedMasina)
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        },

        izmeni: function(){

        	this.osvezi();
        	
            if (this.selectedMasina.ime == ''){
                this.greskaIme = "Ime ne sme biti prazno.";
                this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/masine/izmena", {"staroIme": this.selectedMasinaId, "novaMasina": this.selectedMasina})
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });

        },
        
        pretrazi: function(){
            this.masine = [];
            
            for (let m of this.backup){
                let imePassed = (this.pretragaIme != '') ? (m.ime.includes(this.pretragaIme)) : true;
                let minJezgraPassed = (this.pretragaMinJezgra != '') ? (m.jezgra >= this.pretragaMinJezgra) : true;
                let maxJezgraPassed = (this.pretragaMaxJezgra != '') ? (m.jezgra <= this.pretragaMaxJezgra) : true;
                let minRamPassed = (this.pretragaMinRam != '') ? (m.ram >= this.pretragaMinRam) : true;
                let maxRamPassed = (this.pretragaMaxRam != '') ? (m.ram <= this.pretragaMaxRam) : true;
                let minGpuPassed = (this.pretragaMinGpu != '') ? (m.gpu >= this.pretragaMinGpu) : true;
                let maxGpuPassed = (this.pretragaMaxGpu != '') ? (m.gpu <= this.pretragaMaxGpu) : true;
                if (imePassed && minJezgraPassed && maxJezgraPassed && minRamPassed && maxRamPassed && minGpuPassed && maxGpuPassed) this.masine.push(m);
            }
        },

        izracunajRacun: function(){

            this.osvezi();

            if (this.pocetniDatum == ''){
                this.greskaPocetni = "Ovo polje ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.krajnjiDatum == ''){
                this.greskaKrajnji = "Ovo polje ne sme biti prazno. ";
                this.greska = true;
            }

            if (this.greska) return;

            var y1 = this.pocetniDatum.substr(0,4),
                m1 = this.pocetniDatum.substr(5,2) - 1,
                d1 = this.pocetniDatum.substr(8,2);
            
            var pocetni = new Date(y1,m1,d1);
            
            var y2 = this.krajnjiDatum.substr(0,4),
            	m2 = this.krajnjiDatum.substr(5,2) - 1,
            	d2 = this.krajnjiDatum.substr(8,2);
        
            var krajnji = new Date(y2,m2,d2);
            
            if (pocetni > krajnji) {
                this.greskaPocetni = "Pocetni datum mora biti pre krajnjeg. ";
                this.greska = true;
            }
            else if (this.pocetniDatum == this.krajnjiDatum) {
                this.greskaPocetni = "Pocetni i krajnji datum moraju biti razliciti. ";
                this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/masine/izracunajRacun", {"pocetniDatum": pocetni.getTime(), "krajnjiDatum": krajnji.getTime()})
            .then(response => {
                this.prikaziRacun = true;
            	this.racun = response.data;
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        },
        
        promeni_status: function() {
        	
            axios.post("rest/masine/promeniStatus", {"staroIme": this.selectedMasinaId, "novaMasina": this.selectedMasina})
            .then(response => {
            	
            	this.selectedMasina = response.data;
            	
            	if (this.selectedMasinaStatus === "UGASENA")
            		this.selectedMasinaStatus = "UPALJENA";
            	else
            		this.selectedMasinaStatus = "UGASENA";
            	
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
    	},
        
        vratiNaMasine: function() {
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