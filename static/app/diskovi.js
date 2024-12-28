Vue.component("diskovi", {

    data: function(){
        return {
            diskovi: [],
            backup: [],
            selectedDisk: {}, 
            selectedDiskId: '',
            selected: false, 
            greskaIme: '', 
            greskaTip: '', 
            greskaKapacitet: '',
            greskaServer: '', 
            greska: false,
            pretragaIme: '',
            pretragaTipDiska: '',
            pretragaMinKapacitet: '',
            pretragaMaxKapacitet: '',
            pretragaVMIme: '',
            uloga: '', 
            tipovi: [],
            masine: [],
            novaMasina: ''
        }
    }, 

    template: `

        <div>
        
            <div v-if="selected">

		        <h1>Izmena diska</h1>

    			<br>
    			
    			<div class="izmena">
    				
    				<table>		 
    				               
		                <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="selectedDisk.ime" v-bind:disabled="uloga=='KORISNIK'"></td> <td>{{greskaIme}}</td></tr>    
		          		<tr><td class="left">Organizacija: </td> <td class="right" colspan="2"><input type="text" v-model="selectedDisk.organizacija" disabled></td></tr>

		                <tr><td class="left">Tip: </td> <td class="right"><select v-model="selectedDisk.tip" v-bind:disabled="uloga=='KORISNIK'"> 
			                <option v-for="t in tipovi">
			                    {{t}}
			                </option>
		                </select> </td> <td>
		                {{greskaTip}}</td></tr>
		                
		                <tr><td class="left">Kapacitet: </td> <td class="right"><input type="text" v-model="selectedDisk.kapacitet" v-bind:disabled="uloga=='KORISNIK'"> </td> <td>{{greskaKapacitet}}</td></tr>
		                <tr><td class="left">Virtuelna masina: </td> <td class="right" colspan="2"><input type="text" v-model="selectedDisk.masina" disabled> </td></tr>
		                <tr v-if="uloga!='KORISNIK'"><td class="left">Nova virtuelna masina: </td> <td class="right" colspan="2"><select v-model="novaMasina">
    						<option v-for="m in masine" v-if="m.organizacija === selectedDisk.organizacija">
    							{{m.ime}}
    						</option>
		                </select></td></tr>
		                
				        <tr v-if="uloga!='KORISNIK'"><td colspan="3"><br><button v-on:click="izmeni()">IZMENI</button><br></td></tr>
				        <tr v-if="uloga!='KORISNIK'"><td colspan="3"><br><button v-on:click="obrisi()">OBRISI</button><br></td></tr>
			         	<tr><td colspan="3">{{greskaServer}}<br></td></tr>
			         				         	
    				</table>
    				
    				<button v-on:click="vratiNaDiskove()">POVRATAK</button>
    				
    			</div>
    			
            </div>

            <div v-if="!selected">

                <h1>Registrovani diskovi</h1>
                
                <br>
                
	            <div class="main">
		                
	    			<div class="left">
	    			
		                <table class="data" border="1">
			                <tr><th>Ime</th><th>Kapacitet</th><th>Virtuelna masina</th><th v-if="uloga=='SUPER_ADMIN'">Organizacija</th></tr>
			                <tr v-for="d in diskovi" v-on:click="selectDisk(d)">
			                    <td>{{d.ime}}</td>
			                    <td>{{d.kapacitet}}</td>
			                    <td>{{d.masina}}</td>
			                    <td v-if="uloga=='SUPER_ADMIN'">{{d.organizacija}}</td>
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
		    						<tr><td><router-link to="/masine">MASINE</router-link></td></tr>
		    						<tr><td><router-link to="/profil">PROFIL</router-link></td></tr>
		    						<tr><td><br><button v-on:click="logout()">ODJAVA</button><br><br></td></tr>
		    						
			    				</table>
			    		
			   				</td></tr>
			    			
					        <tr v-if="uloga!='KORISNIK'"><td>
					        
					        	<br>
				                <button v-on:click="dodaj()">DODAJ DISK</button><br><br>
				                
    						</td></tr>
    						
			    			<tr><td>
			    			
				                <h1>Pretraga</h1>
				
								<table>
								
					                <tr><td class="left">Ime: </td> <td><input type="text" v-model="pretragaIme"></td></tr>
					                <tr><td class="left">Tip diska: </td> <td><select v-model="pretragaTipDiska" class="pretraga"> 
						                			<option v-for="t in tipovi">
						                    			{{t}}
						                			</option>
					                		   </select> </td></tr>
					                <tr><td class="left">Min. kapacitet: </td> <td><input type="number" min="1" v-model="pretragaMinKapacitet"></td></tr>
					                <tr><td class="left">Max. kapacitet: </td> <td><input type="number" min="1" v-model="pretragaMaxKapacitet"></td></tr>
					                <tr><td class="left">Ime virtuelne masine: </td> <td><input type="text" v-model="pretragaVMIme"></td></tr>
					                <tr><td colspan="2"><br><button v-on:click="pretrazi()">FILTRIRAJ</button><br><br></td></tr>
					                
						        </table>
						        
			    			</td></tr>
	    		
	    				</table>
	    				
					</div>
		
				</div>

            </div>
        
        </div>
    `,

    mounted(){
    	
    	axios.get("rest/user/uloga")
        .then(response => {
            this.uloga = response.data.result;
        })
        .catch(error => {
            this.$router.push("masine");
        });
    	
        axios.get("rest/diskovi/pregled")
        .then(response => {
            this.diskovi = response.data;
            this.backup = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });
        
        axios.get("rest/diskovi/unos/pregled")
        .then(response => {
            this.tipovi = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });
        
        axios.get("rest/masine/pregled")
        .then(response => {
            this.masine = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });

    },

    methods: {

    	osvezi: function(){
        	this.greskaIme = ''; 
            this.greskaTip = ''; 
            this.greskaKapacitet = '';
            this.greskaServer = ''; 
            this.greska = false;
        },
        
        selectDisk: function(disk){
            this.selectedDisk = disk;
            this.selectedDiskId = disk.ime;
            this.selected = true;
        }, 
        
        dodaj: function(){
            this.$router.push("dodajDisk");
        }, 

        obrisi: function(){
        	
        	let temp = confirm("Da li ste sigurni?");
        	if (!temp) return;

            this.selectedDisk.ime = this.selectedDiskId;
            if (isNaN(parseInt(this.selectedDisk.kapacitet))){
                this.selectedDisk.kapacitet = 0;            	
            }
            else{
                this.selectedDisk.kapacitet = parseInt(this.selectedDisk.kapacitet);	
            }

            axios.post("rest/diskovi/brisanje", this.selectedDisk)
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        },

        izmeni: function(){
        	
        	this.osvezi();

            if (this.selectedDisk.ime == ''){
                this.greskaIme = "Ime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.selectedDisk.tip == ''){
                this.greskaTip = "Tip ne sme biti prazan. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.selectedDisk.kapacitet)) || parseInt(this.selectedDisk.greskaKapacitet) <= 0){
                this.greskaKapacitet = "Kapacitet mora biti pozitivan ceo broj. ";
                this.greska = true;
            }
            
            if (this.greska) return;
            
            this.selectedDisk.masina = this.novaMasina;
            this.selectedDisk.kapacitet = parseInt(this.selectedDisk.kapacitet);
                        
            axios.post("rest/diskovi/izmena", {"staroIme": this.selectedDiskId, "noviDisk": this.selectedDisk})
            .then(response => {
            	location.reload();
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        },
        
        pretrazi: function(){
        	
            this.diskovi = [];
            
            for (let d of this.backup){
            	
                let imePassed = (this.pretragaIme != '') ? (d.ime.includes(this.pretragaIme)) : true;
                let imeVMPassed = (this.pretragaVMIme != '') ? (d.masina.includes(this.pretragaVMIme)) : true;
                let tipDiskaPassed = (this.pretragaTipDiska != '') ? (d.tip.includes(this.pretragaTipDiska)) : true;
                let minKapacitetPassed = (this.pretragaMinKapacitet != '') ? (d.kapacitet >= this.pretragaMinKapacitet) : true;
                let maxKapacitetPassed = (this.pretragaMaxKapacitet != '') ? (d.kapacitet <= this.pretragaMaxKapacitet) : true;
                if (imePassed && imeVMPassed && tipDiskaPassed && minKapacitetPassed && maxKapacitetPassed) this.diskovi.push(d);
                
            }
        },

        vratiNaDiskove: function() {
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