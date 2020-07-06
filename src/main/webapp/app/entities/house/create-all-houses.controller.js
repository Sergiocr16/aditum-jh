(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseAllDialogController', HouseAllDialogController);

    HouseAllDialogController.$inject = ['CompanyConfiguration', 'CommonMethods', '$state', '$rootScope', 'Principal', '$timeout', '$scope', '$stateParams', 'House', 'WSHouse', 'Balance', 'AdministrationConfiguration', 'Modal', 'globalCompany', 'Company'];

    function HouseAllDialogController(CompanyConfiguration, CommonMethods, $state, $rootScope, Principal, $timeout, $scope, $stateParams, House, WSHouse, Balance, AdministrationConfiguration, Modal, globalCompany, Company) {
        var vm = this;
        vm.config = {sufijo: null, prefijo: null, quantity: 1}
        vm.isReady = true;
        $rootScope.active = "houses-massive";

        vm.defineHouseNumber = function (i) {
            var sufijo = vm.config.sufijo != null ? vm.config.sufijo : "";
            var prefijo = vm.config.prefijo != null ? vm.config.prefijo : "";
            var espacioPrefijo = vm.config.espacioPrefijo ? " " : "";
            var espacioSufijo = vm.config.espacioSufijo ? " " : "";
            i = i + "";
            if (i.length < 2) {
                i = "00" + i
            } else if(i.length<3){
                i = "0" + i
            }
            return prefijo + espacioPrefijo + i + espacioSufijo + sufijo;
        }

        Company.query({
            page: 0,
            size: 100,
        }, onSuccess);
        function onSuccess(data, headers) {
            vm.companies = data;
        }
        vm.save = function () {
            Modal.confirmDialog("¿Está seguro que quiere registrar las casas?","Revise bien, borrarlas después es complicado",function(){
            Modal.showLoadingBar()
            for (var i = 1; i <= vm.config.quantity; i++) {
                var house = {
                    desocupationfinaltime: null,
                    desocupationinitialtime: null,
                    due: 0,
                    emergencyKey: null,
                    extension: null,
                    housenumber: vm.defineHouseNumber(i),
                    id: null,
                    isdesocupated: "0",
                    securityKey: null,
                    squareMeters: 0,
                    subsidiaries: [],
                    subsidiaryTypeId: 1,
                    companyId: vm.companyId
                }
                House.save(house, function (data) {
                    if (data.housenumber == vm.defineHouseNumber(vm.config.quantity)) {
                        Modal.hideLoadingBar();
                        Modal.toast("Listo :)")
                    }
                }, function () {
                    Modal.hideLoadingBar();
                    Modal.toast("Error :(")
                });
            }
            })
        }
    }
})();
