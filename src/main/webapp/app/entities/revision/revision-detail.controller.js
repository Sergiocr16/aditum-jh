(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDetailController', RevisionDetailController);

    RevisionDetailController.$inject = ['globalCompany', 'Modal', 'RevisionTaskCategory', '$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function RevisionDetailController(globalCompany, Modal, RevisionTaskCategory, $scope, $rootScope, $stateParams, previousState, entity, Revision, RevisionTask, Company) {
        var vm = this;
        vm.expanding = false;
        vm.revision = entity;
        vm.previousState = previousState.name;
        vm.isReady = false;
        vm.revisionTaskCategories = [];
        vm.month = moment(vm.revision.executionDate).format("MMMM");
        var unsubscribe = $rootScope.$on('aditumApp:revisionUpdate', function (event, result) {
            vm.revision = result;
        });
        $rootScope.active = "revisionSemanal";
        $rootScope.mainTitle = vm.revision.name;
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        loadAllCategories();

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
                Modal.toast("Ocurrio un error inesperado")
            }
        }

        vm.weekOfMonth = function (d) {
            var m = moment(d);
            return m.week();
        };

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

        vm.expand = function () {
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.expanding = !vm.expanding;
                });
            }, 200);
        };
        $scope.$on('$destroy', unsubscribe);
    }
})();
