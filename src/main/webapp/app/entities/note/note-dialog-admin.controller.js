(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NoteDialogAdmin', NoteDialogAdmin);

    NoteDialogAdmin.$inject = ['$timeout', '$scope', '$stateParams', 'Note', 'Principal', 'WSNote', '$rootScope', '$state', 'globalCompany', 'Modal', '$uibModalInstance', 'entity', 'House'];

    function NoteDialogAdmin($timeout, $scope, $stateParams, Note, Principal, WSNote, $rootScope, $state, globalCompany, Modal, $uibModalInstance, entity, House) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        vm.clear = clear;
        vm.note = entity;
        vm.houseSelected = -1;
        House.getAllHousesClean({companyId: globalCompany.getId()}, function (data) {
            vm.houses = data;
        });
        if (vm.note.id != undefined) {
            vm.title = "Editar nota"
            if (vm.note.house != null) {
                vm.houseSelected = vm.note.house.id;
            }
        } else {
            vm.title = "Crear nota"
        }
        function populateValidNote() {
            vm.note.creationdate = moment(new Date()).format();
            vm.note.companyId = globalCompany.getId();
            vm.note.notetype = 1;
            vm.note.deleted = 0;
            vm.note.status = 1;
            vm.note.houseId = globalCompany.getHouseId();
        }
        $rootScope.mainTitle = "Notas";
        $rootScope.active = "admin-notes";
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
            if (vm.houseSelected != -1) {
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
                if (vm.note.id !== null) {
                    populateValidNote()
                    WSNote.sendActivity(vm.note, onSaveSuccess);
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha enviado la nota correctamente");
                    vm.note = undefined;
                    $uibModalInstance.close();
                }
            })
        }

        function onSaveSuccess(result) {
            $uibModalInstance.close(result);
            $scope.$emit('aditumApp:noteUpdate', result);
            vm.isSaving = false;
        }
        function onSaveError() {
            vm.isSaving = false;
        }

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
