(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryCategoryDialogController', SubsidiaryCategoryDialogController);

    SubsidiaryCategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubsidiaryCategory', 'Company'];

    function SubsidiaryCategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SubsidiaryCategory, Company) {
        var vm = this;

        vm.subsidiaryCategory = entity;
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
            if (vm.subsidiaryCategory.id !== null) {
                SubsidiaryCategory.update(vm.subsidiaryCategory, onSaveSuccess, onSaveError);
            } else {
                SubsidiaryCategory.save(vm.subsidiaryCategory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:subsidiaryCategoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
