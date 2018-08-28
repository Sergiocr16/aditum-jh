(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintCommentDeleteController',ComplaintCommentDeleteController);

    ComplaintCommentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ComplaintComment'];

    function ComplaintCommentDeleteController($uibModalInstance, entity, ComplaintComment) {
        var vm = this;

        vm.complaintComment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ComplaintComment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
