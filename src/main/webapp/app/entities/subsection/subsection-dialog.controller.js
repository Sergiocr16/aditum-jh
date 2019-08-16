(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsectionDialogController', SubsectionDialogController);

    SubsectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subsection', 'Article'];

    function SubsectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Subsection, Article) {
        var vm = this;

        vm.subsection = entity;
        vm.clear = clear;
        vm.save = save;
        vm.articles = Article.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subsection.id !== null) {
                Subsection.update(vm.subsection, onSaveSuccess, onSaveError);
            } else {
                Subsection.save(vm.subsection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:subsectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
