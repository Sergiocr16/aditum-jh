(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigDialogController', RevisionConfigDialogController);

    RevisionConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RevisionConfig', 'Company'];

    function RevisionConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RevisionConfig, Company) {
        var vm = this;

        vm.revisionConfig = entity;
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
            if (vm.revisionConfig.id !== null) {
                RevisionConfig.update(vm.revisionConfig, onSaveSuccess, onSaveError);
            } else {
                RevisionConfig.save(vm.revisionConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:revisionConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
