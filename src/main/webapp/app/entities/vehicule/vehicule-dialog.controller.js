(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDialogController', VehiculeDialogController);

    VehiculeDialogController.$inject = ['$state','CommonMethods','$rootScope','Principal','$timeout', '$scope', '$stateParams',  'entity', 'Vehicule', 'House', 'Company','WSVehicle'];

    function VehiculeDialogController ($state,CommonMethods,$rootScope,Principal,$timeout, $scope, $stateParams, entity, Vehicule, House, Company,WSVehicle) {
         $rootScope.active = "vehicules";
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicule = entity;
         vm.brands = CommonMethods.getCarBrands();
        vm.save = save;
        vm.houses = House.query();
        CommonMethods.validateSpecialCharacters();
        angular.element(document).ready(function () {
        ColorPicker.init();
         });
        if(vm.vehicule.id !== null){
            vm.title = "Editar vehículos";
            vm.button = "Editar";

                for(var i=0;i<vm.brands.data.length;i++){
                    if(vm.brands.data[i].name==vm.vehicule.brand){
                        vm.vehicule.brand = vm.brands.data[i];
                    }
                }


        } else{
            vm.title = "Registrar vehículo";
            vm.button = "Registrar";
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
         vm.vehicule.brand = vm.vehicule.brand.name;
            vm.isSaving = true;
            if (vm.vehicule.id !== null) {
                Vehicule.update(vm.vehicule, onSaveSuccess, onSaveError);
            } else {
                vm.vehicule.enabled = 1;
                vm.vehicule.companyId = $rootScope.companyId;

                Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
        WSVehicle.sendActivity(result);
              $state.go('vehicule');
                  toastr["success"]("Se ha registrado el vehículo correctamente.");

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
