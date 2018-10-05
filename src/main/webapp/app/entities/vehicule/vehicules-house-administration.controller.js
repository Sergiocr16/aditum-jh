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
                setTimeout(function() {
                    $("#loadingIcon5").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#vehicules_container").fadeIn('slow');
                },900 )
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        vm.swithEnabledDisabledVehicules = function() {
            $("#vehicules_container").fadeOut(0);
            setTimeout(function() {
                $("#loadingIcon5").fadeIn(100);
            }, 200)
            enabledOptions = !enabledOptions;
            loadVehicules();
        };

        $scope.$watch(function() {
            return $rootScope.houseSelected;
        }, function() {
            $("#vehicules_container").fadeOut(0);
            $("#loadingIcon5").fadeIn("slow");
            loadVehicules();
            vm.isEditing = false;
        });

    }
})();
