(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VehiculeDeleteController',VehiculeDeleteController);

    VehiculeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Vehicule'];

    function VehiculeDeleteController($uibModalInstance, entity, Vehicule) {
        var vm = this;

        vm.vehicule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Vehicule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
