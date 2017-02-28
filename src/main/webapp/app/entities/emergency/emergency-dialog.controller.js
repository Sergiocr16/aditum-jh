(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EmergencyDialogController', EmergencyDialogController);

    EmergencyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Emergency', 'Company', 'House'];

    function EmergencyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Emergency, Company, House) {
        var vm = this;

        vm.emergency = entity;
        vm.clear = clear;
        vm.save = save;
        vm.companies = Company.query();
        vm.houses = House.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.emergency.id !== null) {
                Emergency.update(vm.emergency, onSaveSuccess, onSaveError);
            } else {
                Emergency.save(vm.emergency, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:emergencyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
