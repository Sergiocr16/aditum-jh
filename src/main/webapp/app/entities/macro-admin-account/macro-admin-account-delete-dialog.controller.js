(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroAdminAccountDeleteController',MacroAdminAccountDeleteController);

    MacroAdminAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'MacroAdminAccount'];

    function MacroAdminAccountDeleteController($uibModalInstance, entity, MacroAdminAccount) {
        var vm = this;

        vm.macroAdminAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MacroAdminAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
