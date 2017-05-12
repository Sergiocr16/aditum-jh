(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoController', AdminInfoController);

    AdminInfoController.$inject = ['User','Company','DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope'];

    function AdminInfoController(User,Company,DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,$rootScope) {

        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        var admins = [];
        loadAll();

        function loadAll () {
            AdminInfo.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
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
                vm.queryCount = vm.totalItems;
                vm.page = pagingParams.page;
                Company.query(onSuccessCompany, onError);
                admins = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
            function onSuccessCompany(data) {
                vm.companies = data;
                vm.adminInfos = formatAdminInfo();
            }

        }
        function formatAdminInfo() {
            for (var i = 0; i < admins.length; i++) {

                for (var e = 0; e < vm.companies.length; e++) {
                    if (admins[i].companyId == vm.companies[e].id) {
                        admins[i].companyId = vm.companies[e].name;
                        admins[i].name = admins[i].name + " " + admins[i].lastname + " " + admins[i].secondlastname ;
                    }
                }
            }

            return admins;
        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        vm.deleteAdmin = function(admin) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar al residente " + admin.name + "?",
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {
                    if (result) {
                        vm.login = admin.userLogin;
                        AdminInfo.delete({
                            id: admin.id
                        }, onSuccessDelete);
                    }
                }
            });


        };

        function onSuccessDelete () {
            User.delete({login: vm.login},
                function () {
                    toastr["success"]("Se ha eliminado el administrador correctamente.");
                    loadAll();
                });
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
