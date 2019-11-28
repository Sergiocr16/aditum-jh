(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ProtocolDeleteController',ProtocolDeleteController);

    ProtocolDeleteController.$inject = ['$uibModalInstance', 'entity', 'Protocol'];

    function ProtocolDeleteController($uibModalInstance, entity, Protocol) {
        var vm = this;

        vm.protocol = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Protocol.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
