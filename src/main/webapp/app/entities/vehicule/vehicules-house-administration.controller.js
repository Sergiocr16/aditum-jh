(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculesHouseAdministrationController', VehiculesHouseAdministrationController);

    VehiculesHouseAdministrationController.$inject = ['$state','$scope','$localStorage','CommonMethods','$rootScope','Vehicule', 'House','ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','WSVehicle'];

    function VehiculesHouseAdministrationController($state,$scope,$localStorage,CommonMethods,$rootScope,Vehicule, House, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,WSVehicle) {

        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.isReady = false;
        vm.radiostatus = true;

        vm.changesTitles = function() {
            if (enabledOptions) {
                vm.buttonDisabledEnabledVehicules = "Vehículos deshabilitados";
                vm.titleVehiculeIndex = "Vehículos de la filial";

                vm.iconDisabled = "fa fa-user-times";
                vm.color = "red";
            } else {
                vm.buttonDisabledEnabledVehicules = "Vehículos habilitados";
                vm.titleVehiculeIndex = "Vehículos de la filial (deshabilitados)";

                vm.iconDisabled = "fa fa-undo";
                vm.color = "green";
            }
        }
        setTimeout(function(){ loadVehicules();},100)



        function loadVehicules() {
            if(enabledOptions){
                vm.changesTitles();
                Vehicule.findVehiculesEnabledByHouseId({
                    houseId: $localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();
                Vehicule.findVehiculesDisabledByHouseId({
                    houseId: $localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {

                vm.vehicules = data;
                vm.isReady = true;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        vm.swithEnabledDisabledVehicules = function() {
            vm.isReady = false;
            vm.radiostatus = !vm.radiostatus;
            enabledOptions = !enabledOptions;
            loadVehicules();
        };

        $scope.$watch(function() {
            return $rootScope.houseSelected;
        }, function() {
            vm.isReady = false;
            loadVehicules();
            vm.isEditing = false;
        });

    }
})();
