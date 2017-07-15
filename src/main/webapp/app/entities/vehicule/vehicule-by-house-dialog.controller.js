(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeByHouseDialogController', VehiculeByHouseDialogController);

    VehiculeByHouseDialogController.$inject = ['$state','CommonMethods','$rootScope','Principal','$timeout', '$scope', '$stateParams',  'entity', 'Vehicule', 'House', 'Company','WSVehicle','Brand'];

    function VehiculeByHouseDialogController ($state,CommonMethods,$rootScope,Principal,$timeout, $scope, $stateParams, entity, Vehicule, House, Company,WSVehicle,Brand) {
        $rootScope.active = "vehiculesHouses";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
         Brand.query({}, onSuccessBrand);
        vm.save = save;
         vm.myPlate = vm.vehicule.licenseplate;
        vm.houses = House.query();
        CommonMethods.validateSpecialCharacters();
        angular.element(document).ready(function () {
        ColorPicker.init();
         });

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

        House.query({companyId: $rootScope.companyId}).$promise.then(onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;

             setTimeout(function() {
                $("#register_edit_form").fadeIn(600);
             }, 200)
        }

        vm.submitColor = function() {
           vm.vehicule.color = $('#color').css('background-color');
         }
        function save () {
         vm.vehicule.brand = vm.vehicule.brand.brand;
            vm.isSaving = true;
            if (vm.vehicule.id !== null) {
             if(vm.myPlate!==vm.vehicule.licenseplate){
              Vehicule.getByCompanyAndPlate({companyId:$rootScope.companyId,licensePlate:vm.vehicule.licenseplate},alreadyExist,allClear)
                   function alreadyExist(data){
                    toastr["error"]("La placa ingresada ya existe.");
                   }
                function allClear(data){
                 Vehicule.update(vm.vehicule, onSaveSuccess, onSaveError);
                  }
              }else{
               Vehicule.update(vm.vehicule, onSaveSuccess, onSaveError);
              }
            } else {
                vm.vehicule.enabled = 1;
                vm.vehicule.companyId = $rootScope.companyId;
                vm.vehicule.houseId = $rootScope.companyUser.houseId;

                   Vehicule.getByCompanyAndPlate({companyId:$rootScope.companyId,licensePlate:vm.vehicule.licenseplate},alreadyExist,allClear)
                   function alreadyExist(data){
                    toastr["error"]("La placa ingresada ya existe en el condominio.");
                   }

                    function allClear(data){
                      Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
                    }

            }
        }

        function onSaveSuccess (result) {
        WSVehicle.sendActivity(result);
              $state.go('vehiculeByHouse');
                  toastr["success"]("Se ha registrado el vehículo correctamente.");

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
