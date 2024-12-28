Vue.component("dodajKategoriju", {

    data: function(){
        return{
            novaKategorija: {
                "ime": '', 
                "jezgra": 0, 
                "ram": 0, 
                "gpu": 0
            }, 
            greskaIme: '', 
            greskaJezgra: '', 
            greskaRam: '', 
            greskaGpu: '', 
            greskaServer: '', 
            greska: false   
        }
    }, 

    template: `

        <div class="dodavanje">
        
		    <h1>Registracija nove kategorije</h1>
		    
		    <br>
		    
            <div>
            
	    		<table>
	
			        <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="novaKategorija.ime"></td> <td>{{greskaIme}}</td></tr>
			        <tr><td class="left">Broj jezgara: </td> <td class="right"><input type="text" v-model="novaKategorija.jezgra"></td> <td>{{greskaJezgra}}</td></tr>
			        <tr><td class="left">RAM: </td> <td class="right"><input type="text" v-model="novaKategorija.ram"></td> <td>{{greskaRam}}</td></tr>
			        <tr><td class="left">GPU jezgra: </td> <td class="right"><input type="text" v-model="novaKategorija.gpu"></td> <td>{{greskaGpu}}</td></tr>
			        <tr><td colspan="3"><button v-on:click="dodaj()">DODAJ</button><br></td></tr>
			        <tr><td colspan="3">{{greskaServer}}<br></td></tr>
			        <tr><td colspan="3"><router-link to="/kategorije">KATEGORIJE</router-link><br></td></tr>
	
	    		</table>

    		</div>

        </div>
    
    `, 
    
    mounted(){

        axios.get("rest/check/super")
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

        dodaj: function(){

            this.osvezi();

            if (this.novaKategorija.ime == ''){
                this.greskaIme = "Ime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.novaKategorija.jezgra)) || parseInt(this.novaKategorija.jezgra) <= 0){
                this.greskaJezgra = "Broj jezgara mora biti pozitivan ceo broj. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.novaKategorija.ram)) || parseInt(this.novaKategorija.ram) <= 0){
                this.greskaRam = "RAM mora biti pozitivan ceo broj. ";
                this.greska = true;
            }
            
            if (isNaN(parseInt(this.novaKategorija.gpu)) || parseInt(this.novaKategorija.gpu) < 0){
                this.greskaGpu = "GPU jezgra moraju biti nenegativan ceo broj. ";
                this.greska = true;
            }
            
            if (this.greska) return;
            
            this.novaKategorija.jezgra = parseInt(this.novaKategorija.jezgra);
            this.novaKategorija.ram = parseInt(this.novaKategorija.ram);
            this.novaKategorija.gpu = parseInt(this.novaKategorija.gpu);

            axios.post("rest/kategorije/dodavanje", this.novaKategorija)
            .then(response => {
                this.$router.push("kategorije");
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
            
        }
    }

});