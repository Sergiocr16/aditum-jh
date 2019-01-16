(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('JuntaDirectivaAccountDeleteController',JuntaDirectivaAccountDeleteController);

    JuntaDirectivaAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'JuntaDirectivaAccount'];

    function JuntaDirectivaAccountDeleteController($uibModalInstance, entity, JuntaDirectivaAccount) {
        var vm = this;

        vm.juntaDirectivaAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            JuntaDirectivaAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
