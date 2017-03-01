(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDialogController', VehiculeDialogController);

    VehiculeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vehicule', 'House', 'Company'];

    function VehiculeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vehicule, House, Company) {
        var vm = this;

        vm.vehicule = entity;
        vm.clear = clear;
        vm.save = save;
        vm.houses = House.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vehicule.id !== null) {
                Vehicule.update(vm.vehicule, onSaveSuccess, onSaveError);
            } else {
                Vehicule.save(vm.vehicule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:vehiculeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
