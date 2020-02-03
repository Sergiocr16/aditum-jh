(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CondominiumRecordDeleteController',CondominiumRecordDeleteController);

    CondominiumRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'CondominiumRecord'];

    function CondominiumRecordDeleteController($uibModalInstance, entity, CondominiumRecord) {
        var vm = this;

        vm.condominiumRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CondominiumRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
