(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintUserDialogController', ComplaintUserDialogController);

    ComplaintUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', '$state', 'companyUser', 'Complaint', 'globalCompany'];

    function ComplaintUserDialogController($timeout, $scope, $stateParams, $rootScope, $state, companyUser, Complaint, globalCompany) {
        var vm = this;

        vm.complaint = {complaintType: "Vigilancia"};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.loadResidentsByHouse = loadResidentsByHouse;

        $timeout(function () {
            setTimeout(function () {
                $("#loadingIcon").fadeOut(300);
            }, 400)
            setTimeout(function () {
                $("#tableData").fadeIn('slow');
            }, 700)
        }, 1000);

        function clear() {
            history.back();
        }

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
                    toastr["error"]("Ah ocurrido un error cargando los residentes de la filial.")
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
            bootbox.confirm({
                message: "¿Está seguro que desea registrar la queja o sugerencia?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    if (result) {
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
                    }
                }
            });

        }

        function onSaveSuccess(result) {
            toastr["success"]("Se registró la queja o sugerencia exitosamente.")
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
