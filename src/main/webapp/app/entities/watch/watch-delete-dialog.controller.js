(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('WatchDeleteController',WatchDeleteController);

    WatchDeleteController.$inject = ['$uibModalInstance', 'entity', 'Watch'];

    function WatchDeleteController($uibModalInstance, entity, Watch) {
        var vm = this;

        vm.watch = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Watch.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
