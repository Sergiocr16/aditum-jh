(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SendAccountStatusController', SendAccountStatusController);

    SendAccountStatusController.$inject = ['Resident', '$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', '$mdDialog', '$rootScope', '$scope', 'AdministrationConfiguration', 'Charge', 'CommonMethods', 'globalCompany', 'Modal'];


    function SendAccountStatusController(Resident, $state, House, ParseLinks, AlertService, paginationConstants, $mdDialog, $rootScope, $scope, AdministrationConfiguration, Charge, CommonMethods, globalCompany, Modal) {
        var vm = this;

        $rootScope.mainTitle = "Enviar estados de cuenta";
        vm.currentDate = new Date();
        vm.month = new Date();
        vm.open = function(houseId) {
            vm.checkedType=3;
            vm.residents = [];
            Resident.getOwners({
                page: 0,
                size: 1000,
                companyId: globalCompany.getId(),
                name: " ",
                houseId: houseId
            }, function (residents) {
                vm.residents = residents;
                Resident.getTenants({
                    page: 0,
                    size: 1000,
                    companyId: globalCompany.getId(),
                    name: " ",
                    houseId:  houseId
                }, function (tenants) {
                    angular.forEach(tenants, function (tenant, i){
                        vm.residents.push(tenant)
                    });
                    $mdDialog.show({
                        templateUrl: 'app/entities/account-status/account-status-send-by-email-list.html',
                        scope: $scope,
                        preserveScope: true
                    });
                }, onError);
            }, onError);
            function onError() {
            }
        };

        function obtainEmailToList() {
            var residentsToSendEmails = "";
            angular.forEach(vm.residents, function (resident, i) {
                if (resident.selected == true) {
                    if(residentsToSendEmails.indexOf(resident) === -1){
                        residentsToSendEmails = residentsToSendEmails + resident.id  + ",";
                    }
                }
            });
            return residentsToSendEmails;
        }
        vm.close = function() {
            $mdDialog.hide();
        };

        vm.sendByEmail = function () {
            Modal.showLoadingBar();
            var residentsToSendEmails = obtainEmailToList().slice(0, -1);
            console.log(residentsToSendEmails)
            $mdDialog.hide();
            Modal.hideLoadingBar();
            // Charge.sendChargeEmail({
            //     companyId: globalCompany.getId(),
            //     houseId: vm.chargeSelected.id,
            //     emailTo: residentsToSendEmails
            // },     function (result) {
            //     $mdDialog.hide();
            //     Modal.hideLoadingBar();
            //     Modal.toast("Se envió la cuota por correo correctamente.");
            // });
        };



        $rootScope.active = "sendAccountStatus";
        vm.changeFormat = function () {
            vm.format = 'MMMM';
            vm.hideDate = true;
            vm.hideDate = false;
        };

        vm.locale = {
            formatDate: function (date) {
                var m = moment(date);
                return m.isValid() ? m.format(vm.format) : '';
            }
        };
        vm.createMonth = function () {
            vm.initial_time = new Date(vm.month.getFullYear(), vm.month.getMonth(), 1);
            vm.final_time = new Date(vm.month.getFullYear(), vm.month.getMonth() + 1, 0);
            vm.changeFormat()
        }
        vm.createMonth()
        moment.locale("es");
        function loadAll() {
            House.query({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.houses = data;
                vm.isReady = true;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        loadAll();

    }
})();
