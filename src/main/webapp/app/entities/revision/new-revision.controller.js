(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NewRevisionDialogController', NewRevisionDialogController);

    NewRevisionDialogController.$inject = ['$state', 'CommonMethods', 'RevisionTaskCategory', 'Modal', '$rootScope', 'ParseLinks', 'globalCompany', 'RevisionConfig', '$timeout', '$scope', '$stateParams', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function NewRevisionDialogController($state, CommonMethods, RevisionTaskCategory, Modal, $rootScope, ParseLinks, globalCompany, RevisionConfig, $timeout, $scope, $stateParams, entity, Revision, RevisionTask, Company) {
        var vm = this;
        vm.revision = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.revisionConfigSelected = null;
        vm.revisiontasks = RevisionTask.query();
        vm.companies = Company.query();
        vm.revisionTaskCategories = [];
        vm.ready = false;
        $rootScope.mainTitle = "Crear revisión rutinaria";
        vm.revisionConfigs = [];
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        loadAll();

        vm.createRevision = function () {
            Modal.confirmDialog("¿Está seguro que desea crear la revisión rutinaria?", "", function () {
                vm.ready = false;
                vm.isSaving = true;
                Modal.showLoadingBar();
                Revision.createFromConfig({
                    revisionConfigId: vm.revisionConfigSelected,
                    revisionName: vm.revisionName
                }, function (data) {
                    vm.revision = data;
                    Modal.hideLoadingBar();
                    Modal.toast("Se creo la revisión rutinaria correctamente.")
                    var encryptedId = CommonMethods.encryptIdUrl(data.id);
                    $state.go('revision.edit', {
                        id: encryptedId
                    })
                    vm.isSaving = false;
                })
            })
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


        vm.datePickerOpenStatus.executionDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
