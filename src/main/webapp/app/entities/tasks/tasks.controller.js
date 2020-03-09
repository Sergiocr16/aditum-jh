(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TasksController', TasksController);

    TasksController.$inject = ['Tasks', 'ParseLinks', 'AlertService', 'paginationConstants', 'globalCompany', 'Modal','CommonMethods','$state','$rootScope'];

    function TasksController(Tasks, ParseLinks, AlertService, paginationConstants, globalCompany, Modal,CommonMethods,$state,$rootScope) {

        var vm = this;
        $rootScope.active = "tasks";
        vm.tasks = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.isReady = false;
        vm.status = 1;
        $rootScope.mainTitle = "Lista de tareas";
        loadAll();

        function loadAll() {
            Tasks.allByCompany({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId(),
                status: vm.status

            }, onSuccess, onError);



        }
        function sort() {
            var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
            if (vm.predicate !== 'id') {
                result.push('id');
            }
            return result;
        }
        function reset() {
            vm.page = 0;
            vm.tasks = [];
            loadAll();
        }

        vm.switchTaskStatus = function (status) {
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.status = status;
            vm.isReady = false;
            vm.tasks = [];
            Tasks.allByCompany({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: globalCompany.getId(),
                status: status

            }, onSuccess, onError);
        };
        vm.changeTaskStatus = function (task,status) {
            task.status = status;
            Tasks.update(task, function () {
                vm.switchTaskStatus(vm.status);
            }, function () {
                Modal.toast("Un error inesperado ocurrió");
            });
        };


        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        vm.editTask = function (id) {
            var encryptedId = CommonMethods.encryptIdUrl(id);
            $state.go('tasks.edit', {
                id: encryptedId
            })
        };
        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            var date = new Date();
            console.log(date)
            for (var i = 0; i < data.length; i++) {
                var expirationDate = new Date((data[i].expirationDate));
                if (data[i].expirationDate != null) {
                    if (expirationDate.getTime()< date.getTime()) {
                        data[i].vigencia = 2;
                    }else{
                        data[i].vigencia = 1;
                    }
                }else{
                    data[i].vigencia = 1;
                }
                vm.tasks.push(data[i]);
            }
            vm.isReady = true;
        }


        vm.deleteTask = function (task) {
            Modal.confirmDialog("¿Está seguro que desea eliminar esta tarea?","",
                function(){
                    task.deleted = 1;
                    Tasks.update(task, function () {
                        vm.switchTaskStatus(vm.status);
                    }, function () {
                        Modal.toast("Un error inesperado ocurrió");
                    });

                });

        };
        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
