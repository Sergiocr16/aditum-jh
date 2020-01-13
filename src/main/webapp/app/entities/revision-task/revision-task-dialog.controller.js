(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskDialogController', RevisionTaskDialogController);

    RevisionTaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RevisionTask', 'Revision', 'RevisionTaskCategory'];

    function RevisionTaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RevisionTask, Revision, RevisionTaskCategory) {
        var vm = this;

        vm.revisionTask = entity;
        vm.clear = clear;
        vm.save = save;
        vm.revisions = Revision.query();
        vm.revisiontaskcategories = RevisionTaskCategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.revisionTask.id !== null) {
                RevisionTask.update(vm.revisionTask, onSaveSuccess, onSaveError);
            } else {
                RevisionTask.save(vm.revisionTask, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:revisionTaskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
