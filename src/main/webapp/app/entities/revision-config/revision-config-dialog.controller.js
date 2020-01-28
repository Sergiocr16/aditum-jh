(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionConfigDialogController', RevisionConfigDialogController);

    RevisionConfigDialogController.$inject = ['$state', 'Modal', 'globalCompany', 'ParseLinks', '$rootScope', '$timeout', '$scope', '$stateParams', 'entity', 'RevisionConfig', 'Company', 'RevisionTaskCategory'];

    function RevisionConfigDialogController($state, Modal, globalCompany, ParseLinks, $rootScope, $timeout, $scope, $stateParams, entity, RevisionConfig, Company, RevisionTaskCategory) {
        var vm = this;

        vm.revisionConfig = entity;
        vm.clear = clear;
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        if (vm.revisionConfig.id == undefined) {
            $rootScope.mainTitle = 'Crear configuración de revisión';
        } else {
            $rootScope.mainTitle = 'Editar configuración de revisión';
        }

        loadAll();

        function loadAll() {
            RevisionTaskCategory.findByCompany({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.revisionTaskCategories = data
            }

            function onError(error) {
            }
        }

        function clear() {
        }

        vm.addTask = function () {
            vm.revisionConfig.configTasks.push({deleted: 0});
        }

        vm.delete = function ($index) {
            Modal.confirmDialog("¿Está seguro que desea quitar esta tarea?", "", function () {
                vm.revisionConfig.configTasks[$index].deleted = 1;
            })
        }
        vm.countConfigTasks = function () {
            var count = 0;
            for (var i = 0; i < vm.revisionConfig.configTasks.length; i++) {
                if (vm.revisionConfig.configTasks[i].deleted == 0) {
                    count++;
                }
            }
            return count;
        }

        function save() {
            var dialog = "";
            if (vm.revisionConfig.id == undefined) {
                dialog = '¿Está seguro que desea crear la configuración de la revisión?';
            } else {
                dialog = '¿Está seguro que desea editar la configuración de la revisión?';
            }
            if (vm.countConfigTasks() == 0) {
                Modal.toast("Debe de agregar al menos una tarea a la configuración de revisión.");
            } else {
                Modal.confirmDialog(dialog, "", function () {
                    Modal.showLoadingBar();
                    vm.isSaving = true;
                    vm.revisionConfig.companyId = globalCompany.getId();
                    vm.revisionConfig.deleted = 0;
                    if (vm.revisionConfig.id !== null) {
                        RevisionConfig.update(vm.revisionConfig, onSaveSuccess, onSaveError);
                    } else {
                        RevisionConfig.save(vm.revisionConfig, onSaveSuccess, onSaveError);
                    }
                })
            }
        }

        function onSaveSuccess(result) {
            Modal.hideLoadingBar();
            $state.go('revision-config');
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
