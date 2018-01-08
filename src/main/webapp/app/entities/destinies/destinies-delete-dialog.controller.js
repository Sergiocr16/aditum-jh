(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DestiniesDeleteController',DestiniesDeleteController);

    DestiniesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Destinies'];

    function DestiniesDeleteController($uibModalInstance, entity, Destinies) {
        var vm = this;

        vm.destinies = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Destinies.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
