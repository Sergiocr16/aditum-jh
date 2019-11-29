(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroCondominiumDeleteController',MacroCondominiumDeleteController);

    MacroCondominiumDeleteController.$inject = ['$uibModalInstance', 'entity', 'MacroCondominium'];

    function MacroCondominiumDeleteController($uibModalInstance, entity, MacroCondominium) {
        var vm = this;

        vm.macroCondominium = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MacroCondominium.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
