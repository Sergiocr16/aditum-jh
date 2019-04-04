(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressDeleteController',EgressDeleteController);

    EgressDeleteController.$inject = ['$uibModalInstance', 'entity', 'Egress'];

    function EgressDeleteController($uibModalInstance, entity, Egress) {
        var vm = this;

        vm.egress = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Egress.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
