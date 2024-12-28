Vue.component("dodajOrganizaciju", {

    data: function(){
        return{
            novaOrganizacija: {
                "ime": '', 
                "opis": null, 
                "logo": null,
                "korisnici": [], 
                "resursi": []
            },
            greskaIme: '', 
            greskaServer: '', 
            greska: false
        }
    }, 

    template: `

        <div class="dodavanje">

            <h1>Registracija nove organizacije</h1>
            
            <br>
            
            <div>
            
            	<table>

		            <tr><td class="left">Ime: </td> <td class="right"><input type="text" v-model="novaOrganizacija.ime"></td> <td>{{greskaIme}}</td></tr>
		            <tr><td class="left">Opis: </td> <td class="right"><textarea v-model="novaOrganizacija.opis"></textarea></td></tr>
		            <tr><td class="left">Logo: </td> <td class="right"><input type="file" accept="image/*" v-on:change="updateLogo($event)"></td></tr>
		            <tr><td colspan="3"><br><button v-on:click="dodaj()">DODAJ</button><br></td></tr>
		            <tr><td colspan="3">{{greskaServer}}<br></td></tr>
		            <tr><td colspan="3"><router-link to="/organizacije">ORGANIZACIJE</router-link><br></td></tr>

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
            this.greskaServer = '';
            this.greska = false;
        },
    	
        dodaj: function(){

        	this.osvezi();

            if (this.novaOrganizacija.ime == '' || this.novaOrganizacija.ime === 'null'){
                this.greskaIme = "Ime ne sme biti prazno. ";
                this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/organizacije/dodavanje", this.novaOrganizacija)
            .then(response => {
            	this.$router.push("organizacije");
            })
            .catch(error => {
                this.greskaServer = error.response.data.result;
            });
           
        }, 
        
        updateLogo: function(event) {
        	
	  		var reader = new FileReader();
	  		var instance = this;
	  		
	  		reader.onloadend = function() {
				instance.novaOrganizacija.logo = reader.result;
			}
			 
			reader.readAsDataURL(event.target.files[0]);
			
        },
        
    }, 

});