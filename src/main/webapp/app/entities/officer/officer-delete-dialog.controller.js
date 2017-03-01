(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDeleteController',OfficerDeleteController);

    OfficerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Officer'];

    function OfficerDeleteController($uibModalInstance, entity, Officer) {
        var vm = this;

        vm.officer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Officer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
