(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('AccessDoorNotesController', AccessDoorNotesController);

        AccessDoorNotesController.$inject = ['Auth', '$state', '$scope', '$rootScope', 'CommonMethods', 'AccessDoor', 'Resident', 'House', 'Vehicule', 'Visitant', 'Note', 'AlertService', 'Emergency', 'Principal', '$filter', 'companyUser', 'WSDeleteEntity', 'WSEmergency', 'WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'PadronElectoral', 'Destinies', 'globalCompany', 'Modal', 'ParseLinks'];

        function AccessDoorNotesController(Auth, $state, $scope, $rootScope, CommonMethods, AccessDoor, Resident, House, Vehicule, Visitant, Note, AlertService, Emergency, Principal, $filter, companyUser, WSDeleteEntity, WSEmergency, WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, PadronElectoral, Destinies, globalCompany, Modal, ParseLinks) {
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


            $scope.$on("$destroy", function () {
                $rootScope.houseNoteNotification = undefined;
            });
            $rootScope.mainTitle = "Notas";
            moment.locale('es');
            vm.isReady = false;
            $rootScope.deletedStatusNote = 0;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.itemsPerPage = 12;
            vm.loadNotes = function () {
                vm.isReady = false;
                vm.page = 0;
                vm.links = {
                    last: 0
                };
                $rootScope.notes = [];
                if ($rootScope.houseSelectedNote == -1) {
                    loadNotesByCompany();
                } else {
                    loadNotesByHouse();
                }
            }
            vm.showArchivedNotes = function () {
                $rootScope.deletedStatusNote = 1;
                $rootScope.mainTitle = "Notas Archivadas";
                vm.loadNotes();
            }
            vm.showActualNotes = function () {
                $rootScope.deletedStatusNote = 0;
                $rootScope.mainTitle = "Notas";
                vm.loadNotes();
            }

            function loadNotesByCompany() {
                Note.findAllByCompanyAndDeleted({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sortNotes(),
                    companyId: globalCompany.getId(),
                    deleted: $rootScope.deletedStatusNote,
                    status: $rootScope.noteCreatedBy
                }, onSuccessNotes, onError);
            }


            function loadNotesByHouse() {
                Note.findAllByHouseAndDeleted({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sortNotes(),
                    houseId: $rootScope.houseSelectedNote,
                    deleted: $rootScope.deletedStatusNote,
                    status: $rootScope.noteCreatedBy
                }, onSuccessNotes, onError);
            }

            function sortNotes() {
                var result = [];
                if (vm.predicate !== 'creationdate') {
                    result.push('creationdate,desc');
                }
                return result;
            }

            function onSuccessNotes(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    data[i].sinceDate = moment(data[i].creationdate).fromNow();
                    $rootScope.notes.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError() {

            }

            vm.loadPageNotes = function (page) {
                vm.page = page;
                if ($rootScope.houseSelectedNote === -1) {
                    loadNotesByCompany();
                } else {
                    loadNotesByHouse();
                }
            };

            vm.delete = function (note) {
                vm.note = note;
                Modal.confirmDialog("¿Está seguro que desea archivar la nota?", "", function () {
                    Note.delete({
                        id: note.id
                    }, onSuccessDelete, OnError);
                })

            }

            function onSuccessDelete(result) {
                CommonMethods.deleteFromArray(vm.note, $rootScope.notes);
                Modal.actionToastGiant("Se archivo la nota correctamente", "Deshacer", function () {
                    Note.restore({id: vm.note.id}, function () {
                        vm.note.deleted = 0;
                        $rootScope.notes.push(vm.note);
                    })
                })
            }

            vm.restore = function (note) {
                Modal.confirmDialog("¿Está seguro que desea restaurar  la nota?", "", function () {
                    Note.restore({id: note.id}, function () {
                        CommonMethods.deleteFromArray(note, $rootScope.notes);
                        Modal.actionToastGiant("Se restauro la nota correctamente", "Deshacer", function () {
                            Note.delete({
                                id: note.id
                            }, function(){
                                $rootScope.notes.push(note);
                            }, OnError);
                        })
                    });
                })
            };
            function OnError(result) {
                Modal.toastGiant("Ocurrio un error eliminando la nota");
            }
            $scope.$watch(function () {
                return $rootScope.houseNoteNotification;
            }, function () {
                if ($rootScope.houseNoteNotification != undefined) {
                    vm.checkNoteNotification();
                }
            }, true);

            vm.checkNoteNotification = function(){
                if($rootScope.houseNoteNotification==undefined){
                    $rootScope.houseSelectedNote = -1;
                    $rootScope.noteCreatedBy = -1;
                }else{
                    $rootScope.houseSelectedNote = $rootScope.houseNoteNotification;
                    $rootScope.noteCreatedBy = 1;
                }
                $rootScope.deletedStatusNote = 0;
                vm.loadNotes();
            };

            vm.checkNoteNotification();
        }
    }

)();
