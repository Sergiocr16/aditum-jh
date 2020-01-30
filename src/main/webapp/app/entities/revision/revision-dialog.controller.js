(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDialogController', RevisionDialogController);

    RevisionDialogController.$inject = ['RevisionTaskCategory', 'Modal', '$rootScope', 'ParseLinks', 'globalCompany', 'RevisionConfig', '$timeout', '$scope', '$stateParams', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function RevisionDialogController(RevisionTaskCategory, Modal, $rootScope, ParseLinks, globalCompany, RevisionConfig, $timeout, $scope, $stateParams, entity, Revision, RevisionTask, Company) {
        var vm = this;
        vm.revision = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.saveTask = saveTask;

        vm.revisionConfigSelected = null;
        vm.revisiontasks = RevisionTask.query();
        vm.companies = Company.query();
        vm.revisionTaskCategories = [];
        vm.ready = false;
        $rootScope.mainTitle = "Revisión rutinaria";
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.options = {
            height: 150,
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['fontname', ['fontname']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link']],
                ['view', ['fullscreen', 'help']],
            ]
        };

        function formatCategories(array) {
            for (var i = 0; i < vm.revisionTaskCategories.length; i++) {
                vm.revisionTaskCategories[i].tasks = [];
                for (var j = 0; j < array.length; j++) {
                    if (array[j].revisionTaskCategoryId == vm.revisionTaskCategories[i].id) {
                        if (array[j].observations) {
                            array[j].hasObservations = true;
                        } else {
                            array[j].hasObservations = false;
                        }
                        vm.revisionTaskCategories[i].tasks.push(array[j])
                    }
                }
            }
            vm.revision.revisionTasks = vm.revisionTaskCategories;
            vm.isReady = true;
        }

        loadAllCategories()

        function loadAllCategories() {
            RevisionTaskCategory.findByCompany({
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    vm.revisionTaskCategories.push(data[i]);
                }
                formatCategories(vm.revision.revisionTasks);
            }

            function onError(error) {
                Modal.toast("Ha ocurrido un error inesperado")
            }
        }

        function save() {
            vm.isSaving = true;
            if (vm.revision.id !== null) {
                Revision.update(vm.revision, onSaveSuccess, onSaveError);
            } else {
                Revision.save(vm.revision, onSaveSuccess, onSaveError);
            }
        }
        function saveTask(task) {
            vm.isSaving = true;
            if (task.id !== null) {
                RevisionTask.update(task, onSaveSuccess, onSaveError);
            } else {
                RevisionTask.save(task, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.executionDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
