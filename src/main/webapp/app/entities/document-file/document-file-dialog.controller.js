(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DocumentFileDialogController', DocumentFileDialogController);

    DocumentFileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DocumentFile', 'Company'];

    function DocumentFileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DocumentFile, Company) {
        var vm = this;

        vm.documentFile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.documentFile.id !== null) {
                DocumentFile.update(vm.documentFile, onSaveSuccess, onSaveError);
            } else {
                DocumentFile.save(vm.documentFile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:documentFileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
