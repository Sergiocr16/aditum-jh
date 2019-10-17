(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyWordsDialogController', KeyWordsDialogController);

    KeyWordsDialogController.$inject = ['Modal','$rootScope','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'KeyWords'];

    function KeyWordsDialogController (Modal,$rootScope,$timeout, $scope, $stateParams, $uibModalInstance, entity, KeyWords) {
        var vm = this;
        $rootScope.active = "key-words";
        vm.keyWords = entity;
        vm.clear = clear;
        vm.save = save;
        vm.isReady = true;


        if (vm.keyWords.id !== null) {
            vm.title = "Editar palabra clave de artículos";
            vm.button = "Editar";

        } else {

            vm.title = "Registrar palabra clave de artículos ";
            vm.button = "Registrar";

        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            Modal.showLoadingBar();
            if (vm.keyWords.id !== null) {
                KeyWords.update(vm.keyWords, onSaveSuccess, onSaveError);
            } else {
                vm.keyWords.deleted = 0;
                KeyWords.save(vm.keyWords, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:articleCategoryUpdate', result);
            $uibModalInstance.close(result);
            Modal.hideLoadingBar();
            Modal.toast("Se ha gestionado la palabra clave correctamente.");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
