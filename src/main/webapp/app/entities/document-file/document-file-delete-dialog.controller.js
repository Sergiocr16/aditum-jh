(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DocumentFileDeleteController',DocumentFileDeleteController);

    DocumentFileDeleteController.$inject = ['$uibModalInstance', 'entity', 'DocumentFile'];

    function DocumentFileDeleteController($uibModalInstance, entity, DocumentFile) {
        var vm = this;

        vm.documentFile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DocumentFile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
