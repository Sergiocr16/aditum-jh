(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyWordsDeleteController',KeyWordsDeleteController);

    KeyWordsDeleteController.$inject = ['$uibModalInstance', 'entity', 'KeyWords'];

    function KeyWordsDeleteController($uibModalInstance, entity, KeyWords) {
        var vm = this;

        vm.keyWords = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            KeyWords.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
