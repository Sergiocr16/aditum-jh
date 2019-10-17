(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResolutionCommentsDeleteController',ResolutionCommentsDeleteController);

    ResolutionCommentsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ResolutionComments'];

    function ResolutionCommentsDeleteController($uibModalInstance, entity, ResolutionComments) {
        var vm = this;

        vm.resolutionComments = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ResolutionComments.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
