(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('MacroVisitDeleteController',MacroVisitDeleteController);

    MacroVisitDeleteController.$inject = ['$uibModalInstance', 'entity', 'MacroVisit'];

    function MacroVisitDeleteController($uibModalInstance, entity, MacroVisit) {
        var vm = this;

        vm.macroVisit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MacroVisit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
