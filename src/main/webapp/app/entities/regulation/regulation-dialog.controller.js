(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RegulationDialogController', RegulationDialogController);

    RegulationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Regulation', 'Company'];

    function RegulationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Regulation, Company) {
        var vm = this;

        vm.regulation = entity;
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
            if (vm.regulation.id !== null) {
                Regulation.update(vm.regulation, onSaveSuccess, onSaveError);
            } else {
                Regulation.save(vm.regulation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:regulationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
