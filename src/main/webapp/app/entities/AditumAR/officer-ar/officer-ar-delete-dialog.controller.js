(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerARDeleteController',OfficerARDeleteController);

    OfficerARDeleteController.$inject = ['$uibModalInstance', 'entity', 'OfficerAR'];

    function OfficerARDeleteController($uibModalInstance, entity, OfficerAR) {
        var vm = this;

        vm.officerAR = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            OfficerAR.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
