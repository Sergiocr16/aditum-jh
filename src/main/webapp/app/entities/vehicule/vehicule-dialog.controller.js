(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDialogController', VehiculeDialogController);

    VehiculeDialogController.$inject = ['$state','CommonMethods','$rootScope','Principal','$timeout', '$scope', '$stateParams',  'entity', 'Vehicule', 'House', 'Company','WSVehicle','Brand'];

    function VehiculeDialogController ($state,CommonMethods,$rootScope,Principal,$timeout, $scope, $stateParams, entity, Vehicule, House, Company,WSVehicle,Brand) {
         $rootScope.active = "vehicules";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
        vm.myPlate = vm.vehicule.licenseplate;
        vm.save = save;
        vm.required = 1;
        vm.required2 = 1;
        CommonMethods.validateSpecialCharacters();
         CommonMethods.validateSpecialCharactersAndVocals();

        angular.element(document).ready(function () {
        ColorPicker.init();
         });
        Brand.query({}, onSuccessBrand);
        function onSuccessBrand(brands){
           vm.brands = brands;

            if(vm.vehicule.id !== null){
                vm.title = "Editar vehículo";
                vm.button = "Editar";
                angular.forEach(vm.brands,function(brand,i){
                   if(brand.brand===vm.vehicule.brand){
                        vm.vehicule.brand = brand;
                    }
                })
            } else{
                vm.title = "Registrar vehículo";
                vm.button = "Registrar";
            }
        }
        setTimeout(function(){
          House.query({companyId: $rootScope.companyId}).$promise.then(onSuccessHouses);
                function onSuccessHouses(data, headers) {
                   angular.forEach(data,function(value,key){
                      if(value.housenumber==9999){
                      value.housenumber="Oficina"
                      }
                  })
                    vm.houses = data;
                    setTimeout(function() {
                        $("#register_edit_form").fadeIn(600);
                    }, 200)
                }
        },500)


        vm.submitColor = function() {
           vm.vehicule.color = $('#color').css('background-color');
         }

                 vm.validate = function(){
                   var invalido = 0;
                  function hasWhiteSpace(s) {
                   function tiene(s) {
                         return /\s/g.test(s);
                      }
                      if(tiene(s)||s==undefined){
                       return true
                      }
                     return false;
                   }

                   function hasCaracterEspecial(s){
                   var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿","{","}"]
                   var invalido = 0;
                    angular.forEach(caracteres,function(val,index){
                    if (s!=undefined){
                     for(var i=0;i<s.length;i++){
                     if(s.charAt(i)==val){
                     invalido++;
                     }
                     }
                     }
                    })
                    if(invalido==0){
                    return false;
                    }else{
                    return true;
                    }
                   }

                   if(vm.vehicule.licenseplate == undefined || hasWhiteSpace(vm.vehicule.licenseplate)){
                      toastr["error"]("No puede ingresar la placa con espacios en blanco.");
                      invalido++;
                   }else if(hasCaracterEspecial(vm.vehicule.licenseplate)){
                      invalido++;
                        toastr["error"]("No puede ingresar la placa con guiones o cualquier otro carácter especial");
                   }
                    if(invalido==0){
                    return true;
                    }else{
                    return false;
                    }
                  }
        function save () {
   if(  vm.validate()){
             if(vm.vehicule.color==undefined){
              vm.vehicule.color = "rgb(255, 255, 255)";
              }
             vm.vehicule.brand = vm.vehicule.brand.brand;
             vm.vehicule.enabled = 1;
             vm.vehicule.companyId = $rootScope.companyId;
             vm.vehicule.licenseplate = vm.vehicule.licenseplate.toUpperCase();
             vm.isSaving = true;
             if (vm.vehicule.id !== null) {
                 if(vm.myPlate!==vm.vehicule.licenseplate){
                  Vehicule.getByCompanyAndPlate({companyId:$rootScope.companyId,licensePlate:vm.vehicule.licenseplate},alreadyExist,allClearUpdate)
                       function alreadyExist(data){
                        toastr["error"]("La placa ingresada ya existe.");
                       }
                  }else{
                       Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
                  }
             } else {
                   Vehicule.getByCompanyAndPlate({companyId:$rootScope.companyId,licensePlate:vm.vehicule.licenseplate},alreadyExist,allClearInsert)
                   function alreadyExist(data){
                    toastr["error"]("La placa ingresada ya existe.");
                   }
                   function allClearInsert(){
                         CommonMethods.waitingMessage();
                         insertVehicule();
                     }
                   function insertVehicule(){
                   console.log(vm.vehicule.type);
                   Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
              }
            }
        }

         function allClearUpdate(data){
            CommonMethods.waitingMessage();
            updateVehicule();

          }

        function updateVehicule(){
            Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
         }
        function onSaveSuccess (result) {
              WSVehicle.sendActivity(result);
              $state.go('vehicule');
                bootbox.hideAll();
              toastr["success"]("Se ha registrado el vehículo correctamente.");

            vm.isSaving = false;
        }

           function onEditSuccess (result) {
                WSVehicle.sendActivity(result);
                      $state.go('vehicule');
                        bootbox.hideAll();

                          toastr["success"]("Se ha editado el vehículo correctamente.");

                    vm.isSaving = false;
                }

        function onSaveError () {
            vm.isSaving = false;
        }

  }
    }
})();
