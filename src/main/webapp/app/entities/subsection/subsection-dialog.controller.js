(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsectionDialogController', SubsectionDialogController);

    SubsectionDialogController.$inject = ['$rootScope','Modal','$state','$localStorage','$timeout', '$scope', '$stateParams', 'entity', 'Subsection', 'Article'];

    function SubsectionDialogController ($rootScope,Modal,$state,$localStorage,$timeout, $scope, $stateParams,  entity, Subsection, Article) {
        var vm = this;
        vm.isReady = true;
        vm.subsection = entity;
        vm.save = save;
        $rootScope.active = "regulation";
        vm.articles = Article.query();
        vm.chapter = $localStorage.chapterSelected;
        vm.regulation = $localStorage.regulationSelected;
        vm.article = $localStorage.articleSelected;

        if (vm.subsection.id !== null) {
            vm.button = "Editar";
            $rootScope.mainTitle = "Editar inciso - " + vm.article.name+" - " +  vm.chapter.name+" - " + vm.regulation.name;
        } else {
            $rootScope.mainTitle = "Registrar inciso - " + vm.article.name+" - " +  vm.chapter.name +" - " + vm.regulation.name;
            vm.button = "Registrar";
        }


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });



        function save () {
            vm.isSaving = true;
            Modal.showLoadingBar()
            if (vm.subsection.id !== null) {
                Subsection.update(vm.subsection, onSaveSuccess, onSaveError);
            } else {
                vm.subsection.articleId = vm.article.id;
                vm.subsection.deleted = 0;
                Subsection.save(vm.subsection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            Modal.hideLoadingBar();
            $state.go('subsection');
            Modal.toast("Se ha gestionado el inciso correctamente.");
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
