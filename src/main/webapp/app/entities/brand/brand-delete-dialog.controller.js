(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BrandDeleteController',BrandDeleteController);

    BrandDeleteController.$inject = ['$uibModalInstance', 'entity', 'Brand','Principal'];

    function BrandDeleteController($uibModalInstance, entity, Brand,Principal) {
        var vm = this;

        vm.brand = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
         vm.isAuthenticated = Principal.isAuthenticated;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Brand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
