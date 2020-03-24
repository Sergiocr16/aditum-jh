(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TasksDeleteController',TasksDeleteController);

    TasksDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tasks'];

    function TasksDeleteController($uibModalInstance, entity, Tasks) {
        var vm = this;

        vm.tasks = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tasks.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
