(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessNoteDialogController', AccessNoteDialogController);

    AccessNoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'Note', 'Principal', 'WSNote', '$rootScope', '$state', 'globalCompany', 'Modal', '$uibModalInstance', 'entity'];

    function AccessNoteDialogController($timeout, $scope, $stateParams, Note, Principal, WSNote, $rootScope, $state, globalCompany, Modal, $uibModalInstance, entity) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        vm.clear = clear;
        vm.note = entity;
        vm.houseSelected = -1;

        if (vm.note.id != undefined) {
            vm.title = "Editar nota"
            if(vm.note.house!=null){
                vm.houseSelected = vm.note.house.id;
            }
        } else {
            vm.title = "Crear nota"
        }

        $rootScope.mainTitle = "Notas";
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });

        function populateValidNote() {
            vm.note.creationdate = moment(new Date()).format();
            vm.note.companyId = globalCompany.getId();
            vm.note.notetype = 1;
            vm.note.deleted = 0;
            vm.note.status = 2;
            if(vm.houseSelected!=-1){
                vm.note.houseId = vm.houseSelected;
            }
        }


        function save() {
            var title = "";
            var success = "";
            if (vm.note.id == undefined) {
                title = "¿Está seguro que desea registrar esta nota?"
                success = "Se ha registrado la nota correctamente";
            } else {
                title = "¿Está seguro que desea editar esta nota?"
                success = "Se ha editado la nota correctamente";
            }
            Modal.confirmDialog(title, "", function () {
                vm.isSaving = true;
                Modal.showLoadingBar();
                populateValidNote()
                if (vm.note.id !== null) {
                    Note.update(vm.note,function (result) {
                        Modal.hideLoadingBar();
                        vm.note = undefined;
                        $uibModalInstance.close(result);
                        vm.isSaving = false;
                        Modal.toastGiant(success);
                    })
                }else{
                    Note.save(vm.note,function (result) {
                        Modal.hideLoadingBar();
                        vm.note = undefined;
                        $uibModalInstance.close(result);
                        vm.isSaving = false;
                        Modal.toastGiant(success);
                    })
                }
            })
        }


        function onSaveError() {
            vm.isSaving = false;
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
