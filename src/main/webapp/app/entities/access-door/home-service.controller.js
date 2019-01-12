(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('HomeServiceDoorController', HomeServiceDoorController);

        HomeServiceDoorController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal'];

        function HomeServiceDoorController(Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral, Destinies, globalCompany, Modal) {
            var vm = this;
            toastr.options = {
                "closeButton": true,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-bottom-full-width",
                "preventDuplicates": true,
                "onclick": null,
                "showDuration": "300",
                "hideDuration": "100000",
                "timeOut": "5000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "slideDown",
                "hideMethod": "slideUp",
            };

            vm.delete = function(note) {
                vm.note = note;
                Modal.confirmDialog("¿Está seguro que desea eliminar la nota?","",function(){
                    Note.delete({
                        id: note.id
                    }, onSuccessDelete,OnError);
                })

            }

            function onSuccessDelete(result) {
                CommonMethods.deleteFromArray(vm.note,$rootScope.notes);
                toastr["success"]("Se elimino la nota correctamente");
            }
            function OnError(result) {
                toastr["success"]("Ocurrio un error eliminando la nota");
            }
        }
    }
)();
