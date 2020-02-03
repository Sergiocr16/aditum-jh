(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigTaskDialogController', RevisionConfigTaskDialogController);

    RevisionConfigTaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RevisionConfigTask', 'RevisionTaskCategory', 'RevisionConfig'];

    function RevisionConfigTaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RevisionConfigTask, RevisionTaskCategory, RevisionConfig) {
        var vm = this;

        vm.revisionConfigTask = entity;
        vm.clear = clear;
        vm.save = save;
        vm.revisiontaskcategories = RevisionTaskCategory.query();
        vm.revisionconfigs = RevisionConfig.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.revisionConfigTask.id !== null) {
                RevisionConfigTask.update(vm.revisionConfigTask, onSaveSuccess, onSaveError);
            } else {
                RevisionConfigTask.save(vm.revisionConfigTask, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:revisionConfigTaskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
