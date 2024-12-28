Vue.component("dodajMasinu", {

    data: function(){
        return{
            novaMasina:{
                "ime": '', 
                "organizacija": '', 
                "kategorija": {
                    "ime": '', 
                    "jezgra": 0, 
                    "ram": 0, 
                    "gpu": 0
                },
                "jezgra": 0, 
                "ram": 0, 
                "gpu": 0,
                "aktivnosti": [], 
                "diskovi": []
            }, 
            greskaIme: '',
            greskaOrganizacija: '',
            greskaKategorija: '', 
            greskaServer: '',
            greska: false,
            kategorije: [], 
            organizacije: [], 
            organizacija: '',
            diskovi: [],
            diskoviBackup: [],
            kat: ''
        }
    }, 

    template:`

        <div class="dodavanje">

            <h1>Registracija nove masine</h1>
            
            <br>
            
            <div>
            
            	<table>
            	
	                <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="novaMasina.ime"></td> <td>{{greskaIme}}</td></tr>
	                <tr><td class="left">Organizacija: </td>
	                
	                <td class="right"><input type="text" v-model="organizacija" v-bind:hidden="organizacije.length>1" disabled>
	                <select v-model="organizacija" v-bind:hidden="organizacije.length<=1">
		                <option v-for="o in organizacije">
		                    {{o.ime}}
		                </option>
	                </select></td> 
	                <td class="right">{{greskaOrganizacija}}</td></tr>
	                
	                <tr><td class="left">Diskovi: </td>
	                <td class="right"><select v-model="novaMasina.diskovi" multiple>
		                <option v-for="d in diskovi">
		                    {{d.ime}}
		                </option>
	                </select></td></tr>	 
	                
	                <tr><td class="left">Kategorija: </td>
	                <td class="right"><select v-model="kat">
	                	<option v-for="k in kategorije">
	                    	{{k.ime}}
	                    </option>
	                </select> </td>
	                <td class="right">{{greskaKategorija}}</td></tr>
	                
	                <tr><td class="left">Broj jezgara: </td> <td class="right" colspan="2"><input type="text" v-model="novaMasina.jezgra" disabled></td></tr>
	                <tr><td class="left">RAM: </td> <td class="right" colspan="2"><input type="text" v-model="novaMasina.ram" disabled></td></tr>
	                <tr><td class="left">GPU jezgra: </td> <td class="right" colspan="2"><input type="text" v-model="novaMasina.gpu" disabled></td></tr>
	                
	                <tr><td colspan="3"><br><button v-on:click="dodaj()">DODAJ</button><br></td></tr>
	                <tr><td colspan="3"><br>{{greskaServer}}<br></td></tr>
	                <tr><td colspan="3"><br><router-link to="/masine">MASINE</router-link><br></td></tr>
	                	                
                </table>
        	</div>
        	
        </div>
    
    `, 

    watch: {
        kat: function() {
          for (let k of this.kategorije){
              if (k.ime == this.kat){
                  this.novaMasina.kategorija.ime = k.ime;
                  this.novaMasina.kategorija.jezgra = k.jezgra;
                  this.novaMasina.kategorija.ram = k.ram;
                  this.novaMasina.kategorija.gpu = k.gpu;
                  this.novaMasina.jezgra = k.jezgra;
                  this.novaMasina.ram = k.ram;
                  this.novaMasina.gpu = k.gpu;
              }
          }
        },
        
	    organizacija: function() {
	    	
	    	let org = this.organizacija;
	    	this.diskovi = this.diskoviBackup;
	    	
	        this.diskovi = this.diskovi.filter(function(disk) {
	        	return disk.organizacija === org;
	        });
	        
	    }
    },

    mounted(){
    	
    	axios.get("rest/check/korisnik")
        .catch(error => {
            this.$router.push("masine");
        });

        axios.get("rest/kategorije/unos/pregled")
        .then(response => {
            this.kategorije = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });

        axios.get("rest/organizacije/pregled")
        .then(response => {
            this.organizacije = response.data;
            this.organizacija = this.organizacije.length >= 1 ? this.organizacije[0].ime : '';
        })
        .catch(error => {
            this.$router.push("masine");
        });
        
        axios.get("rest/diskovi/pregled")
        .then(response => {
            this.diskoviBackup = response.data;
        })
        .catch(error => {
            this.$router.push("masine");
        });

    },

    methods: {
    	
    	osvezi: function(){
    		this.greskaIme = '';
            this.greskaOrganizacija = '';
            this.greskaKategorija = '';
            this.greskaServer = '';
            this.greska = false;
    	},

        dodaj: function(){

        	this.osvezi();
        	
        	this.novaMasina.organizacija = this.organizacija;
        	
            if (this.novaMasina.ime == '' || this.novaMasina.ime === 'null'){
                this.greskaIme = "Ime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.novaMasina.organizacija == ''){
                this.greskaOrganizacija = "Organizacija ne sme biti prazna. ";
                this.greska = true;
            }
            
            if (this.novaMasina.kategorija == '' || this.kat == ''){
                this.greskaKategorija = "Kategorija ne sme biti prazna. ";
                this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/masine/dodavanje", this.novaMasina)
            .then(response => {
                this.$router.push("masine");
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        }, 
       
    }, 

});