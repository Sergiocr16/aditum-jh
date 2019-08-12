(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitationDeleteController',VisitantInvitationDeleteController);

    VisitantInvitationDeleteController.$inject = ['$uibModalInstance', 'entity', 'VisitantInvitation'];

    function VisitantInvitationDeleteController($uibModalInstance, entity, VisitantInvitation) {
        var vm = this;

        vm.visitantInvitation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VisitantInvitation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
