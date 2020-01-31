(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionTaskCategoryDialogController', RevisionTaskCategoryDialogController);

    RevisionTaskCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RevisionTaskCategory', 'Company'];

    function RevisionTaskCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RevisionTaskCategory, Company) {
        var vm = this;

        vm.revisionTaskCategory = entity;
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
            if (vm.revisionTaskCategory.id !== null) {
                RevisionTaskCategory.update(vm.revisionTaskCategory, onSaveSuccess, onSaveError);
            } else {
                RevisionTaskCategory.save(vm.revisionTaskCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:revisionTaskCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
