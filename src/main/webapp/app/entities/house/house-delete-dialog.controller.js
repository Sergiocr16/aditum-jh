(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDeleteController',HouseDeleteController);

    HouseDeleteController.$inject = ['$uibModalInstance', 'entity', 'House','CompanyUser'];

    function HouseDeleteController($uibModalInstance, entity, House,CompanyUser) {
        var vm = this;
       console.log(vm.companyUser);
        vm.house = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            House.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
