const login = {template: '<login></login>'}
const profil = {template: '<profil></profil>'}
const kategorije = {template: '<kategorije></kategorije>'}
const dodajKategoriju = {template: '<dodajKategoriju></dodajKategoriju>'}
const organizacije = {template: '<organizacije></organizacije>'}
const mojaOrganizacija = {template: '<mojaOrganizacija></mojaOrganizacija>'}
const dodajOrganizaciju = {template: '<dodajOrganizaciju></dodajOrganizaciju>'}
const korisnici = {template: '<korisnici></korisnici>'}
const dodajKorisnika = {template: '<dodajKorisnika></dodajKorisnika>'}
const masine = {template: '<masine></masine>'}
const dodajMasinu = {template: '<dodajMasinu></dodajMasinu>'}
const diskovi = {template: '<diskovi></diskovi>'}
const dodajDisk = {template: '<dodajDisk></dodajDisk>'}

const router = new VueRouter({
	
    mode: 'hash', 
    routes: [
        {path: '/', component: login}, 
        {path: '/profil', component: profil}, 
        {path: '/kategorije', component: kategorije}, 
        {path: '/dodajKategoriju', component: dodajKategoriju}, 
        {path: '/organizacije', component: organizacije},
        {path: '/mojaOrganizacija', component: mojaOrganizacija},
        {path: '/dodajOrganizaciju', component: dodajOrganizaciju},
        {path: '/korisnici', component: korisnici}, 
        {path: '/dodajKorisnika', component: dodajKorisnika},
        {path: '/masine', component: masine}, 
        {path: '/dodajMasinu', component: dodajMasinu}, 
        {path: '/diskovi', component: diskovi},
        {path: '/dodajDisk', component: dodajDisk}

    ]

});

var app = new Vue({
    router: router, 
    el: '#mainDiv'
});