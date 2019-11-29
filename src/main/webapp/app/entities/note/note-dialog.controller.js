(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NoteDialogController', NoteDialogController);

    NoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'Note', 'Principal', 'WSNote', '$rootScope', '$state', 'globalCompany', 'Modal'];

    function NoteDialogController($timeout, $scope, $stateParams, Note, Principal, WSNote, $rootScope, $state, globalCompany, Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.save = save;
        $rootScope.active = "reportHomeService";
        $rootScope.mainTitle = "Enviar nota a oficial";
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        function populateValidNote() {
            vm.note.creationdate = moment(new Date()).format();
            vm.note.companyId = globalCompany.getId();
            vm.note.notetype = 1;
            vm.note.deleted = 0;
            vm.note.status = 1;
            vm.note.houseId = $rootScope.companyUser.houseId;
        }

        function save() {
            Modal.confirmDialog("¿Está seguro que desea enviar esta nota a los oficiales?","",function(){
                vm.isSaving = true;
                Modal.showLoadingBar();
                if (vm.note.id !== null) {
                    populateValidNote()
                    WSNote.sendActivity(vm.note, onSaveSuccess);
                    Modal.hideLoadingBar();
                    Modal.toast("Se ha enviado la nota correctamente");
                    vm.note = undefined;
                    $state.go('residentByHouse');
                }
            })
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:noteUpdate', result);

            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
