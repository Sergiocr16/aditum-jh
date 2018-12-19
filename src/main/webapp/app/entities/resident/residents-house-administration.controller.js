(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentsHouseAdministrationController', ResidentsHouseAdministrationController);

    ResidentsHouseAdministrationController.$inject = ['$state', '$scope', 'DataUtils', 'Resident', 'User', 'CommonMethods', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', 'Company', 'MultiCompany', '$rootScope', 'WSResident', '$localStorage'];

    function ResidentsHouseAdministrationController($state, $scope, DataUtils, Resident, User, CommonMethods, House, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, Company, MultiCompany, $rootScope, WSResident, $localStorage) {


        var vm = this;
        var enabledOptions = true;
        vm.isReady = false;
        vm.radiostatus = true;
        vm.isAuthenticated = Principal.isAuthenticated;
        setTimeout(function () {
            loadResidents();
        }, 100)

        function loadResidents() {

            if (enabledOptions) {

                vm.changesTitles();
                Resident.findResidentesEnabledByHouseId({
                    houseId: $localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            } else {
                vm.changesTitles();

                Resident.findResidentesDisabledByHouseId({
                    houseId: $localStorage.houseSelected.id
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {

                vm.residents = data;
                vm.residents = formatResidents(data);
                vm.isReady = true;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        vm.detailResident = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('resident-detail', {
                id: encryptedId
            })
        }
        vm.switchEnabledDisabledResidents = function () {
            vm.isReady = false;
            vm.radiostatus = !vm.radiostatus;
            enabledOptions = !enabledOptions;
            loadResidents();
        }
        vm.changesTitles = function () {
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
        };

        function formatResidents(residents) {
            for (var i = 0; i < residents.length; i++) {

                residents[i].name = residents[i].name + " " + residents[i].lastname;
                if (residents[i].phonenumber == null) {
                    residents[i].phonenumber = "No registrado"
                }
                ;

            }
            return residents;
        }

        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {

            loadResidents();
            vm.isEditing = false;
        });
    }
})();
