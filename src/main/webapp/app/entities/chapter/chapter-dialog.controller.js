(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ChapterDialogController', ChapterDialogController);

    ChapterDialogController.$inject = ['Modal','$state','$rootScope','$localStorage','$timeout', '$scope', '$stateParams', 'entity', 'Chapter', 'Regulation'];

    function ChapterDialogController (Modal,$state,$rootScope,$localStorage,$timeout, $scope, $stateParams, entity, Chapter, Regulation) {
        var vm = this;
        vm.required=1;
        vm.chapter = entity;
        vm.isReady = true;
        vm.save = save;
        $rootScope.active = "regulation";
        vm.regulation = $localStorage.regulationSelected;

        if (vm.chapter.id !== null) {
            vm.button = "Editar";
            $rootScope.mainTitle = "Editar capítulo - " + vm.regulation.name;
        } else {
            $rootScope.mainTitle = "Registrar capítulo - " + vm.regulation.name;
            vm.button = "Registrar";
        }


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save () {
            Modal.showLoadingBar()
            vm.isSaving = true;
            if (vm.chapter.id !== null) {
                Chapter.update(vm.chapter, onSaveSuccess, onSaveError);
            } else {
                vm.chapter.regulationId = vm.regulation.id;
                vm.chapter.deleted = 0;
                Chapter.save(vm.chapter, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go('chapter');
            Modal.toast("Se ha gestionado el capítulo correctamente.");
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
