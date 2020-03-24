(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDeleteController',CommonAreaDeleteController);

    CommonAreaDeleteController.$inject = ['$uibModalInstance', 'entity', 'CommonArea'];

    function CommonAreaDeleteController($uibModalInstance, entity, CommonArea) {
        var vm = this;

        vm.commonArea = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CommonArea.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
