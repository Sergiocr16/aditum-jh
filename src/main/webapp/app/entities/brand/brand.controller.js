(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BrandController', BrandController);

    BrandController.$inject = ['Brand','Principal','House','Resident','Vehicule','$rootScope','firebase'];

    function BrandController(Brand,Principal,House,Resident,Vehicule, $rootScope,firebase) {

        var vm = this;

        function newWithCallback(childRef,obj,callback){
          var pathName = "/"+childRef+"/"+obj.identificationnumber+"/"
          firebase.database().ref(pathName).set(obj);
          callback(obj);
        }

       function findByIdentificationNumber(childRef,dataId,callback){
            var pathName = "/"+childRef+"/"+dataId
            firebase.database().ref(pathName).once('value', function(snapshot){
                var data = snapshot.val()
                if(data){
                   callback(data)
                }
            })
        }

        vm.createInFirebase = function(){
//        newWithCallback("",{identificationnumber:"116060486",nombre:"Sergio,Castro,Rodriguez"},function(){
//        alert("si")
//        })
findByIdentificationNumber("","116060486",function(person){
console.log(person)
})
        }










        $rootScope.active = "brands";
        vm.brands = [];
        vm.isAuthenticated = Principal.isAuthenticated;

        vm.names = ["Marco","Sofía","Luis","Michael","Bryan","Lorena","Gabriela","Eduardo","Johnny","Violet","Sarah","Cesar","José","Yeimy","Elizabeth","María José","Laura","Ricardo","Adán","Óliver","Rául","Rebeca","Renato","Ingrid","Jesús","Zoe","Helena","Santiago","Simón","Robert"]
        vm.lastNames = ["Carranza","Aguilar","Carrillo","Acosta","Cortés","Calderón","Picado","Ulate","Echandi","Monge","Carazo","Figueres","Trejos","Montealegre","Soto","Jiménez","González","Mora","Tinoco","Durán","Rodríguez","Castro","Castillo","Suarez","Montevideo","Estrada","Salazar","Bonilla","Navarro","Valverde"]
        vm.mails = ["gmail","hotmail","outlook"]
        vm.vehiculeType = ["Automóvil","Motocicleta"]
        vm.selectedBrands = [{

                                brand:"Audi"
                            }, {
                                brand:"Alfa Romeo"
                            }, {
                                brand:"BMW"
                            }, {
                                brand:"BYD"
                            }, {
                                brand:"Chevrolet"
                            }, {
                                brand:"Citroen"
                            }, {
                                brand:"Daewoo"
                            }, {
                                brand:"Daihatsu"
                            }, {
                                brand:"Dodge"
                            }, {
                                brand:"Fiat"
                            }, {
                                brand:"Ford"
                            }, {
                                brand:"Honda"
                            }, {
                                brand:"Hummer"
                            }, {
                                brand:"Hyundai"
                            }, {
                                brand:"Izuzu"
                            }, {
                                brand:"Jaguar"
                            }, {
                                brand:"JAC"
                            }, {
                                brand:"Jeep"
                            }, {
                                brand:"Kia"
                            }, {
                                brand:"Land Rover"
                            }, {
                                brand:"Lexus"
                            }, {
                                brand:"Maserati"
                            }, {
                                brand:"Mazda"
                            }, {
                                brand:"Mercedes Benz"
                            }, {
                                brand:"Mini"
                            }, {
                                brand:"Mitsubishi"
                            }, {
                                brand:"Nissan"
                            }, {
                                brand:"Peugeot"
                            }, {
                                brand:"Porshe"
                            }, {
                                brand:"Renault"
                            }, {
                                brand:"Rolls Royce"
                            }, {
                                brand:"Ssanyong"
                            }, {
                                brand:"Subaru"
                            }, {
                                brand:"Suzuki"
                            }, {
                                brand:"Toyota"
                            }, {
                                brand:"Volkswagen"
                            }, {
                                brand:"Volvo"

                            }, ];

        vm.createHouses = function(){
        var setHouseId = 1;
        for(var i= 1;i<=88;i++){
          House.save({housenumber:i,extension:i+"0"+i,isdesocupated:0,desocupationinitialtime:"2017-07-17T22:44:50.667Z",desocupationfinaltime:"2017-07-17T22:44:50.667Z",securityKey:null,emergencyKey:null,companyId:1},function(){
           toastr['success']("Casa registrada");
            setHouseId = setHouseId+1;
          })
        }
                setTimeout(function(){
                          console.log("AQUI")
                          toastr['warning']("TODAS LAS CASAS REGISTRADAS")
                           vm.createResidents();
                },10000)
        }
        vm.createResidents = function(){
        for(var i=1;i<=(88*5);i++){
        var name = vm.names[Math.floor(Math.random()*vm.names.length) + 0];
        var lastName = vm.lastNames[Math.floor(Math.random()*vm.lastNames.length) + 0];
        var secondLastName = vm.lastNames[Math.floor(Math.random()*vm.lastNames.length) + 0];
        var setEmail = name.charAt(0).toLowerCase()+lastName.toLowerCase()+secondLastName.charAt(0).toLowerCase()+"@"+vm.mails[Math.floor(Math.random()*vm.mails.length) + 0]+".com".toLowerCase();
        Resident.save({        name: name,
                                                      lastname: lastName,
                                                      secondlastname:secondLastName,
                                                      identificationnumber:(Math.floor(Math.random()*900000000) + 0),
                                                      phonenumber:(Math.floor(Math.random()*99999999) + 66666666),
                                                      image:null,
                                                      imageContentType:null,
                                                      email:setEmail,
                                                      isOwner:0,
                                                      enabled:1,
                                                      userId:null,
                                                      userLogin:null,
                                                      companyId:1,
                                                      image_url:'https://res.cloudinary.com/aditum/image/upload/v1504067725/tyvlvgynor3zsnzq6smg.jpg',
                                                      houseId:(Math.floor(Math.random()*88) + 1)},function(){
          toastr['success']("Residente registrado")
        })
        }
       setTimeout(function(){
                  toastr['warning']("TODAS LOS RESIDENTES REGISTRADOS")
                  vm.createCars();
                },30000)
        }
       vm.randomLetters = function(){
           var text = "";
           var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
           for (var i = 0; i < 3; i++)
             text += possible.charAt(Math.floor(Math.random() * possible.length));
           return text+(Math.floor(Math.random()*999) + 100);
       }

             vm.createBrands= function(){
             for(var i=0;i<=vm.selectedBrands.length;i++){
                   Brand.save({brand:vm.selectedBrands[i].brand},function(){
                   toastr['success']("MARCAS REGISTRADA")
                   })

             }
         setTimeout(function(){
                                 toastr['warning']("TODAS LOS MARCAS REGISTRADAS")
                                toastr['warning']("LISTO :)");
                               },3000)
             };
        vm.createCars = function(){

        for(var i = 1;i<=(88*3);i++){
          Vehicule.save({licenseplate:vm.randomLetters(),
          brand:vm.selectedBrands[(Math.floor(Math.random()*vm.selectedBrands.length) + 0)].brand,
          color:'rgb(' + (Math.floor(Math.random() * 256)) + ',' + (Math.floor(Math.random() * 256)) + ',' + (Math.floor(Math.random() * 256)) + ')',
          enabled:1,houseId:(Math.floor(Math.random()*88) + 1),type:vm.vehiculeType[(Math.floor(Math.random()*vm.vehiculeType.length) + 0)], companyId:1},function(){
           toastr['success']("Vehiculo Registrado")
          })
          }
                   setTimeout(function(){
                                    toastr['warning']("TODAS LOS VEHICULOS REGISTRADOS")
                                    vm.createBrands();
                                  },30000)
        }

        vm.fillInfo = function(){
        vm.createHouses();
        }
        loadAll();

        function loadAll() {
            Brand.query(function(result) {
                vm.brands = result;
                vm.searchQuery = null;
            });
        }
    }
})();
