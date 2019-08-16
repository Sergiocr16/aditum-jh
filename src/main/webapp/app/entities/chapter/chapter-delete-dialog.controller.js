(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChapterDeleteController',ChapterDeleteController);

    ChapterDeleteController.$inject = ['$uibModalInstance', 'entity', 'Chapter'];

    function ChapterDeleteController($uibModalInstance, entity, Chapter) {
        var vm = this;

        vm.chapter = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Chapter.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
