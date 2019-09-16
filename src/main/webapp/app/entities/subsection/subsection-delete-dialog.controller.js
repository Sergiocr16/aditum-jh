(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsectionDeleteController',SubsectionDeleteController);

    SubsectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Subsection'];

    function SubsectionDeleteController($uibModalInstance, entity, Subsection) {
        var vm = this;

        vm.subsection = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Subsection.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
