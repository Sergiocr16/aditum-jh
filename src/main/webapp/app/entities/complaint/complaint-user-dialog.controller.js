(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintUserDialogController', ComplaintUserDialogController);

    ComplaintUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', '$state', 'companyUser', 'Complaint', 'globalCompany', 'Modal'];

    function ComplaintUserDialogController($timeout, $scope, $stateParams, $rootScope, $state, companyUser, Complaint, globalCompany, Modal) {
        var vm = this;

        vm.complaint = {complaintType: "Vigilancia"};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.loadResidentsByHouse = loadResidentsByHouse;
        vm.isReady = false;
        $rootScope.mainTitle = "Registrar queja o sugerencia";
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        function clear() {
            history.back();
        }

        vm.isReady = true;

        function loadResidentsByHouse(houseId) {
            vm.residents = [];
            Resident.findResidentesEnabledByHouseId({houseId: houseId},
                function (data) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].fullName = data[i].name + " " + data[i].lastname + " " + data[i].secondlastname;
                        vm.residents.push(data[i]);
                    }
                    console.log(vm.residents)
                }, function () {
                    Modal.toast("Ah ocurrido un error cargando los residentes de la filial.")
                })
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
                angular.forEach(data, function (value, key) {
                    value.housenumber = parseInt(value.housenumber);
                    if (value.housenumber == 9999) {
                        value.housenumber = "Oficina"
                    }
                });
                vm.houses = data;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function save() {
            Modal.confirmDialog("¿Está seguro que desea registrar la queja o sugerencia?","",
                function(){
                        vm.isSaving = true;
                        vm.complaint.creationDate = moment(new Date).format();
                        vm.complaint.companyId = companyUser.companyId;
                        vm.complaint.houseId = companyUser.houseId;
                        vm.complaint.residentId = companyUser.id;
                        vm.complaint.status = 1;
                        vm.complaint.deleted = 0;
                        if (vm.complaint.id !== null) {
                            Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
                        } else {
                            Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
                        }
            });

        }

        function onSaveSuccess(result) {
            Modal.toast("Se registró la queja o sugerencia exitosamente.")
            $state.go('complaint-user');
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.resolutionDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
