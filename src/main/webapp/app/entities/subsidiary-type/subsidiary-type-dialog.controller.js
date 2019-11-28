(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryTypeDialogController', SubsidiaryTypeDialogController);

    SubsidiaryTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubsidiaryType', 'SubsidiaryCategory', 'Company'];

    function SubsidiaryTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SubsidiaryType, SubsidiaryCategory, Company) {
        var vm = this;

        vm.subsidiaryType = entity;
        vm.clear = clear;
        vm.save = save;
        vm.subsidiarycategories = SubsidiaryCategory.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subsidiaryType.id !== null) {
                SubsidiaryType.update(vm.subsidiaryType, onSaveSuccess, onSaveError);
            } else {
                SubsidiaryType.save(vm.subsidiaryType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:subsidiaryTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
