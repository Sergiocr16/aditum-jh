(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDeleteController',ComplaintDeleteController);

    ComplaintDeleteController.$inject = ['$uibModalInstance', 'entity', 'Complaint'];

    function ComplaintDeleteController($uibModalInstance, entity, Complaint) {
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
