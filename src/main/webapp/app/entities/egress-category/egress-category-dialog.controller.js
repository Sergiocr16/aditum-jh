(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryDialogController', EgressCategoryDialogController);

    EgressCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EgressCategory', 'Company'];

    function EgressCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EgressCategory, Company) {
        var vm = this;

        vm.egressCategory = entity;
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
            if (vm.egressCategory.id !== null) {
                EgressCategory.update(vm.egressCategory, onSaveSuccess, onSaveError);
            } else {
                EgressCategory.save(vm.egressCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:egressCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
