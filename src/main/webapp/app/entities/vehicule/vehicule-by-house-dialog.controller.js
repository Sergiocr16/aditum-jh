(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeByHouseDialogController', VehiculeByHouseDialogController);

    VehiculeByHouseDialogController.$inject = ['$state','CommonMethods','$rootScope','Principal','$timeout', '$scope', '$stateParams',  'entity', 'Vehicule', 'House', 'Company','WSVehicle','Brand','globalCompany','Modal'];

    function VehiculeByHouseDialogController ($state,CommonMethods,$rootScope,Principal,$timeout, $scope, $stateParams, entity, Vehicule, House, Company,WSVehicle,Brand, globalCompany,Modal) {
        $rootScope.active = "vehiculesHouses";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;

        vm.required = 1;
        vm.isReady = false;
         Brand.query({}, onSuccessBrand);
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
         vm.myPlate = vm.vehicule.licenseplate;
        vm.houses = House.query();
        CommonMethods.validateSpecialCharacters();
         CommonMethods.validateSpecialCharactersAndVocals();
        angular.element(document).ready(function () {
        ColorPicker.init();
         });

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
          var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿"]
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
              Modal.toast("No puede ingresar la placa con espacios en blanco");
             invalido++;
          }else if(hasCaracterEspecial(vm.vehicule.licenseplate)){
             invalido++;
              Modal.toast("No puede ingresar la placa con guiones o cualquier otro carácter especial");
          }
           if(invalido==0){
           return true;
           }else{
           return false;
           }
         }





           function onSuccessBrand(brands){
           vm.brands = brands;

        if(vm.vehicule.id !== null){
            vm.title = "Editar vehículo";
            vm.button = "Editar";
            $rootScope.mainTitle =  vm.title;
            vm.mainTitle =  vm.title;
            angular.forEach(vm.brands,function(brand,i){
               if(brand.brand===vm.vehicule.brand){
                    vm.vehicule.brand = brand;
                }
            })
        } else{
            vm.title = "Registrar vehículo";
            $rootScope.mainTitle =  vm.title;
            vm.mainTitle =  vm.title;
            vm.button = "Registrar";
        }
 }

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;
            vm.isReady = true;

        }

        vm.submitColor = function() {
           vm.vehicule.color = $('#color').css('background-color');
         }



        function save () {
        if(  vm.validate()){
        if(vm.vehicule.color==undefined){
          vm.vehicule.color = "rgb(255, 255, 255)";
          }
           vm.vehicule.enabled = 1;
           vm.vehicule.companyId = globalCompany.getId();
            vm.vehicule.houseId = $rootScope.companyUser.houseId;
          vm.vehicule.licenseplate = vm.vehicule.licenseplate.toUpperCase();
            vm.isSaving = true;
            if (vm.vehicule.id !== null ) {
             if(vm.myPlate!==vm.vehicule.licenseplate){
              Vehicule.getByCompanyAndPlate({companyId:globalCompany.getId(),licensePlate:vm.vehicule.licenseplate},alreadyExist,allClearUpdate)
                   function alreadyExist(data){
                       Modal.toast("La placa ingresada ya existe");
                   }

          }else{
               vm.vehicule.brand = vm.vehicule.brand.brand;
               Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
              }

      } else {
                Vehicule.getByCompanyAndPlate({companyId:globalCompany.getId(),licensePlate:vm.vehicule.licenseplate},alreadyExist,allClearInsert)
               function alreadyExist(data){
                   Modal.toast("La placa ingresada ya existe");
               }
               function allClearInsert(){
                  CommonMethods.waitingMessage();
                     insertVehicule();
                 }

                function insertVehicule(){
                   vm.vehicule.brand = vm.vehicule.brand.brand;
               Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
              }
            }
        }

        function allClearUpdate(){
           CommonMethods.waitingMessage();
             updateVehicule();
          }

          function updateVehicule(){
               vm.vehicule.brand = vm.vehicule.brand.brand;
               Vehicule.update(vm.vehicule, onEditSuccess, onSaveError);
          }
        function onSaveSuccess (result) {
             WSVehicle.sendActivity(result);
             $state.go('vehiculeByHouse');
            bootbox.hideAll();
            Modal.toast("Se ha registrado el vehículo correctamente");
            vm.isSaving = false;
        }

            function onEditSuccess (result) {
                WSVehicle.sendActivity(result);
                      $state.go('vehiculeByHouse');
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
