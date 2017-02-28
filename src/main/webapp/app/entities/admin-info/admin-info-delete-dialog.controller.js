(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDeleteController',AdminInfoDeleteController);

    AdminInfoDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdminInfo'];

    function AdminInfoDeleteController($uibModalInstance, entity, AdminInfo) {
        var vm = this;

        vm.adminInfo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AdminInfo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
