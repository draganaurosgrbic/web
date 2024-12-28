Vue.component("login", {

    data: function(){
        return {
            user: {
                "korisnickoIme": '', 
                "lozinka": ''
            },
            greskaKorisnickoIme: '',  
            greskaLozinka: '', 
            greskaLogin: '', 
            greska: false
        }
    }, 

    template: `

        <div class="login">
            <h1>Prijava</h1><br><br>
            
            <table>
            
	            <tr><td class="left">Email: </td><td><input type="text" v-model="user.korisnickoIme"></td> <td>{{greskaKorisnickoIme}}</td></tr>
	            <tr><td class="left">Lozinka: </td><td><input type="password" v-model="user.lozinka"></td> <td>{{greskaLozinka}}</td></tr>
	            <tr><td colspan="3"><br><button v-on:click="login()">PRIJAVA</button><br></td></tr>
	            <tr><td colspan="3">{{greskaLogin}}</td></tr>
	            
            </table>
        </div>
    `, 

    methods: {

    	osvezi: function(){
    		this.greskaKorisnickoIme = '';
            this.greskaLozinka = '';
            this.greskaLogin = '';
            this.greska = false;
    	},
    	
        login: function(){
            
            this.osvezi();

            if (this.user.korisnickoIme == ''){
                this.greskaKorisnickoIme = "Niste uneli korisnicko ime. ";
                this.greska = true;
            }
            
            if (this.user.lozinka == ''){
                this.greskaLozinka = "Niste uneli lozinku. ";
                this.greska = true;
            }
            
            if (this.greska) return;

            axios.post("rest/user/login", this.user)
            .then(response => {
                this.$router.push("masine");
            })
            .catch(error => {
                this.greskaLogin = error.response.data.result;
            });

        }
    }
});