(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentsHouseAdministrationController', ResidentsHouseAdministrationController);

    ResidentsHouseAdministrationController.$inject = ['$state','$scope','DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope','WSResident','$localStorage'];

    function ResidentsHouseAdministrationController($state,$scope,DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope,WSResident,$localStorage) {


        var vm = this;
        var enabledOptions = true;
        vm.isReady = false;
        vm.radiostatus = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        setTimeout(function(){ loadResidents();},100)
        function loadResidents() {

            if (enabledOptions) {

                vm.changesTitles();
                Resident.findResidentesEnabledByHouseId({
                    houseId: $localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();

                Resident.findResidentesDisabledByHouseId({
                    houseId:$localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {
                vm.residents = data;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.switchEnabledDisabledResidents = function() {
            vm.isReady = false;
            vm.radiostatus = !vm.radiostatus;
            enabledOptions = !enabledOptions;
            loadResidents();
        }
        vm.changesTitles = function() {
            if (enabledOptions) {
                vm.titleCondominosIndex = "Residentes de la filial ";
                vm.buttonTitle = "Ver residentes deshabilitados";
                vm.actionButtonTitle = "Deshabilitar";
                vm.iconDisabled = "fa fa-user-times";
                vm.color = "red";
            } else {
                vm.titleCondominosIndex = "Residentes de la filial (deshabilitados)";
                vm.buttonTitle = "Ver residentes habilitados";
                vm.actionButtonTitle = "Habilitar";
                vm.iconDisabled = "fa fa-undo";
                vm.color = "green";
            }
        }

        $scope.$watch(function() {
            return $rootScope.houseSelected;
        }, function() {
            vm.isReady = true;
            loadResidents();
            vm.isEditing = false;
        });
    }
})();
