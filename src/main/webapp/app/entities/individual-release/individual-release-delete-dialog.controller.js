(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('IndividualReleaseDeleteController',IndividualReleaseDeleteController);

    IndividualReleaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Complaint'];

    function IndividualReleaseDeleteController($uibModalInstance, entity, Complaint) {
        var vm = this;

        vm.complaint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Complaint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
