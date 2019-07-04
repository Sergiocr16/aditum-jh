(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroOfficerAccountDeleteController',MacroOfficerAccountDeleteController);

    MacroOfficerAccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'MacroOfficerAccount'];

    function MacroOfficerAccountDeleteController($uibModalInstance, entity, MacroOfficerAccount) {
        var vm = this;

        vm.macroOfficerAccount = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MacroOfficerAccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
