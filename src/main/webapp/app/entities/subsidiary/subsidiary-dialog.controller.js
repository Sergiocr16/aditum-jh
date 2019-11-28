(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsidiaryDialogController', SubsidiaryDialogController);

    SubsidiaryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subsidiary', 'SubsidiaryType', 'House'];

    function SubsidiaryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Subsidiary, SubsidiaryType, House) {
        var vm = this;

        vm.subsidiary = entity;
        vm.clear = clear;
        vm.save = save;
        vm.subsidiarytypes = SubsidiaryType.query();
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subsidiary.id !== null) {
                Subsidiary.update(vm.subsidiary, onSaveSuccess, onSaveError);
            } else {
                Subsidiary.save(vm.subsidiary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:subsidiaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
