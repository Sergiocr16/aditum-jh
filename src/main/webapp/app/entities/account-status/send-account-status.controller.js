(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SendAccountStatusController', SendAccountStatusController);

    SendAccountStatusController.$inject = ['Resident', '$state', 'House', 'ParseLinks', 'AlertService', 'paginationConstants', '$mdDialog', '$rootScope', '$scope', 'AdministrationConfiguration', 'AccountStatus', 'CommonMethods', 'globalCompany', 'Modal'];


    function SendAccountStatusController(Resident, $state, House, ParseLinks, AlertService, paginationConstants, $mdDialog, $rootScope, $scope, AdministrationConfiguration, AccountStatus, CommonMethods, globalCompany, Modal) {
        var vm = this;

        $rootScope.mainTitle = "Enviar estados de cuenta";
        vm.currentDate = new Date();
        vm.month = new Date();
        vm.open = function (houseId) {
            vm.checkedType = 3;
            vm.residents = [];
            vm.houseSelectedId = houseId;
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
                    houseId: houseId
                }, function (tenants) {
                    angular.forEach(tenants, function (tenant, i) {
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
                    if (residentsToSendEmails.indexOf(resident) === -1) {
                        residentsToSendEmails = residentsToSendEmails + resident.id + ",";
                    }
                }
            });
            return residentsToSendEmails;
        }

        vm.close = function () {
            Modal.hideLoadingBar();
            $mdDialog.hide();
        };

        vm.sendByEmail = function () {
            var residentsToSendEmails = obtainEmailToList().slice(0, -1);
            if (residentsToSendEmails == "") {
                Modal.toast("Debes seleccionar al menos un correo electrónico");
            } else {
                Modal.confirmDialog("¿Está seguro que desea enviar el estado de cuenta?", "" +
                    "El estado de cuenta se enviará a los contactos seleccionados.", function () {
                    Modal.showLoadingBar();
                    AccountStatus.sendAccountStatus({
                        houseId: vm.houseSelectedId,
                        companyId: globalCompany.getId(),
                        emailTo: residentsToSendEmails,
                        monthDate: moment(vm.month).format(),
                    }, function (result) {
                        $mdDialog.hide();
                        Modal.hideLoadingBar();
                        Modal.toast("Se envió el estado de cuenta por correo correctamente.");
                    })
                })
            }
        };
        vm.sendByEmailAll = function () {
            Modal.confirmDialog("¿Está seguro que desea enviar los estados de cuenta?", "" +
                "Los estados de cuenta se enviarán a los contactos principales de cada filial.", function () {
                Modal.showLoadingBar();
                AccountStatus.sendAccountStatusToAll({
                    companyId: globalCompany.getId(),
                    monthDate: moment(vm.month).format(),
                }, function (result) {
                    $mdDialog.hide();
                    Modal.hideLoadingBar();
                    Modal.toast("Se envió el estado de cuenta por correo a todos correctamente.");
                })
            })
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
