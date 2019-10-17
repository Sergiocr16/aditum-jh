(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionDeleteController',ResolutionDeleteController);

    ResolutionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resolution'];

    function ResolutionDeleteController($uibModalInstance, entity, Resolution) {
        var vm = this;

        vm.resolution = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resolution.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
