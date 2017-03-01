(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccessDoorDialogController', AccessDoorDialogController);

    AccessDoorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccessDoor', 'Company', 'Watch'];

    function AccessDoorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccessDoor, Company, Watch) {
        var vm = this;

        vm.accessDoor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.watches = Watch.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accessDoor.id !== null) {
                AccessDoor.update(vm.accessDoor, onSaveSuccess, onSaveError);
            } else {
                AccessDoor.save(vm.accessDoor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:accessDoorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
