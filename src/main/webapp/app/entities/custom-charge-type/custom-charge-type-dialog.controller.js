(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CustomChargeTypeDialogController', CustomChargeTypeDialogController);

    CustomChargeTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomChargeType', 'Company'];

    function CustomChargeTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomChargeType, Company) {
        var vm = this;

        vm.customChargeType = entity;
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
            if (vm.customChargeType.id !== null) {
                CustomChargeType.update(vm.customChargeType, onSaveSuccess, onSaveError);
            } else {
                CustomChargeType.save(vm.customChargeType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:customChargeTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
