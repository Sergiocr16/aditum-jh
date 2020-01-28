(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDialogController', RevisionDialogController);

    RevisionDialogController.$inject = ['RevisionTaskCategory', 'Modal', '$rootScope', 'ParseLinks', 'globalCompany', 'RevisionConfig', '$timeout', '$scope', '$stateParams', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function RevisionDialogController(RevisionTaskCategory, Modal, $rootScope, ParseLinks, globalCompany, RevisionConfig, $timeout, $scope, $stateParams, entity, Revision, RevisionTask, Company) {
        var vm = this;
        vm.revision = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.revisionConfigSelected = null;
        vm.revisiontasks = RevisionTask.query();
        vm.companies = Company.query();
        vm.revisionTaskCategories = [];
        vm.ready = true;
        if (vm.revision.id == null) {
            $rootScope.mainTitle = "Nueva revisión";
            vm.revisionCreated = false;
        } else {
            $rootScope.mainTitle = "Editar revisión";
            vm.createRevision()
        }
        vm.revisionConfigs = [];
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        loadAll();

        vm.createRevision = function () {
            Modal.confirmDialog("¿Está seguro que desea crear la revisión?", "", function () {
                vm.ready = false;
                vm.revisionCreated = true;
                RevisionConfig.get({id: vm.revisionConfigSelected}, function (data) {
                    vm.revision = data;
                    vm.revision.revisionTasks = data.configTasks;
                    $rootScope.mainTitle = "Nueva "+vm.revision.name;
                    loadAllCategories();
                })
            })
        }

        function formatCategories(array) {
            console.log(array)
            for (var i = 0; i < vm.revisionTaskCategories.length; i++) {
                vm.revisionTaskCategories[i].tasks = [];
                for (var j = 0; j < array.length; j++) {
                    if (array[j].revisionTaskCategoryId == vm.revisionTaskCategories[i].id) {
                        vm.revisionTaskCategories[i].tasks.push(array[j])
                    }
                }
            }
            vm.revision.revisionTasks = vm.revisionTaskCategories;
            vm.isReady = true;
        }

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
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            RevisionConfig.findByCompany({
                page: 0,
                size: 500,
                companyId: globalCompany.getId(),
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.revisionConfigs.push(data[i]);
                }
                vm.isReady = true;
            }

            function onError(error) {
            }
        }

        function clear() {
        }

        function save() {
            vm.isSaving = true;
            if (vm.revision.id !== null) {
                Revision.update(vm.revision, onSaveSuccess, onSaveError);
            } else {
                Revision.save(vm.revision, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:revisionUpdate', result);
            $uibModalInstance.close(result);
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
