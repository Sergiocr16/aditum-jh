(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyWordsDialogController', KeyWordsDialogController);

    KeyWordsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'KeyWords'];

    function KeyWordsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, KeyWords) {
        var vm = this;

        vm.keyWords = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.keyWords.id !== null) {
                KeyWords.update(vm.keyWords, onSaveSuccess, onSaveError);
            } else {
                KeyWords.save(vm.keyWords, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:keyWordsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
