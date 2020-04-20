(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseSecurityDirectionDeleteController',HouseSecurityDirectionDeleteController);

    HouseSecurityDirectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'HouseSecurityDirection'];

    function HouseSecurityDirectionDeleteController($uibModalInstance, entity, HouseSecurityDirection) {
        var vm = this;

        vm.houseSecurityDirection = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HouseSecurityDirection.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
