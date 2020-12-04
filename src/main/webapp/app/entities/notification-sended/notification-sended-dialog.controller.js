(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NotificationSendedDialogController', NotificationSendedDialogController);

    NotificationSendedDialogController.$inject = ['$rootScope', 'Modal', 'Resident', 'globalCompany', 'House', '$timeout', '$scope', '$stateParams', 'entity', 'NotificationSended', 'Company', '$state'];

    function NotificationSendedDialogController($rootScope, Modal, Resident, globalCompany, House, $timeout, $scope, $stateParams, entity, NotificationSended, Company, $state) {
        var vm = this;
        vm.notificationSended = entity;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.companies = Company.query();
        vm.isReady = false;
        vm.notShowSendTo = false;
        vm.housesSended = [];
        vm.toAll = 0;
        vm.sendToResident = null;
        $rootScope.mainTitle = "Enviar QuickMessage";
        loadAll();
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.loadResidentsByHouse = function (housesSended) {
            if (housesSended.length == 1 && vm.toAll == 0) {
                vm.notShowSendTo = false;
                vm.residents = [];
                Resident.findAllResidentesWithNotificationsEnabledByHouseId({houseId: housesSended[0].id},
                    function (data) {
                        for (var i = 0; i < data.length; i++) {
                            data[i].fullName = data[i].name + " " + data[i].lastname;
                            vm.residents.push(data[i]);
                        }
                    }, function () {
                        Modal.toast("Ah ocurrido un error cargando los residentes de la filial.")
                    })
            } else {
                vm.notShowSendTo = true;
            }

        }

        function loadAll() {
            House.query({
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.houses = data;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.defineResidentType = function (type) {
            switch (type) {
                case 1:
                    return "Propietario"
                    break;
                case 2:
                    return "Propietario arrendador"
                    break;
                case 3:
                    return "Residente autorizado"
                    break;
                case 4:
                    return "Inquilino"
                    break;
            }
        }

        function save() {
            Modal.confirmDialog("¿Está seguro que desea enviar la notificación?", "Una vez enviada NO PODRÁ CANCELAR EL ENVÍO", function () {
                vm.isSaving = true;
                vm.notificationSended.toAll = vm.toAll;
                if (vm.toAll == 0) {
                    var toSend = "";
                    for (var i = 0; i < vm.housesSended.length; i++) {
                        var h = vm.housesSended[i];
                        toSend = toSend + h.id + "," + h.housenumber + ";"
                    }
                    vm.notificationSended.sendedTo = toSend;
                    if (vm.sendToResident) {
                        vm.notificationSended.sendToResident = vm.sendToResident.id + ";" + vm.sendToResident.name + " " + vm.sendToResident.lastname;
                    }
                }
                vm.notificationSended.companyId = globalCompany.getId();
                if (vm.notificationSended.id !== null) {
                    NotificationSended.update(vm.notificationSended, onSaveSuccess, onSaveError);
                } else {
                    NotificationSended.save(vm.notificationSended, onSaveSuccess, onSaveError);
                }
            })
        }

        function onSaveSuccess(result) {
            Modal.toast("Se envió correctamente")
            $state.go("notification-sended");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
