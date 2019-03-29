(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoController', AdminInfoController);

    AdminInfoController.$inject = ['$state', 'CommonMethods','User','Company','DataUtils', 'AdminInfo', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope'];

    function AdminInfoController($state, CommonMethods,User,Company,DataUtils, AdminInfo, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,$rootScope) {
        $rootScope.active = "admins";
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
vm.isReady = false;
      vm.viewDetail = function(adminId){
           var adminInfoId = CommonMethods.encryptIdUrl(adminId);

            $state.go('admin-info-detail', {id: adminInfoId});

        }
        loadAll();
          vm.showCondominios = function(adminInfo) {
               AdminInfo.get({id:adminInfo.id},function(data){
                                   bootbox.dialog({
                                       message: '<div class="text-center gray-font font-20"> <h4 class="font-30">Condominios que administra <span class="font-30" id="key_id_house"></span></h4></div> <div class="text-center gray-font" id="condos"></div>',
                                       closeButton: false,
                                       buttons: {
                                           confirm: {
                                               label: 'Ocultar',
                                               className: 'btn-success'
                                           }
                                       },
                                   })
                                   var formattedCompanies = "";

                                   angular.forEach(data.companies,function(value,index){
                                    formattedCompanies += "<h5 class='text-center font-20'>"+value.name+"<h5>"
                                   })
                                   console.log(formattedCompanies)
                                   document.getElementById("key_id_house").innerHTML = "" + data.name;
                                    document.getElementById("condos").innerHTML = formattedCompanies;
               //                    document.getElementById("security_key").innerHTML = "" + securityKey;
               //                    document.getElementById("emergency_key").innerHTML = "" + emergencyKey;

               })

            }
        function loadAll (option) {
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
                vm.isReady = true;
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
                if(option!==1){
                    vm.adminInfos = formatAdminInfo(admins);
                } else {
                    var adminsByCondo = [];

                    for (var i = 0; i < admins.length; i++) {

                        if (vm.condo.id === admins[i].companyId) {
                            adminsByCondo.push(admins[i])
                        }
                    }

                    vm.adminInfos = formatAdminInfo(adminsByCondo);
                }
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                },100 )
            }

        }
        function formatAdminInfo(adminstrators) {

            for (var i = 0; i < adminstrators.length; i++) {
                        adminstrators[i].name = adminstrators[i].name + " " + adminstrators[i].lastname + " " + adminstrators[i].secondlastname ;

            }
            return adminstrators;
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

        vm.disableEnabledAdmin= function(adminInfo) {

            var correctMessage;
            if (adminInfo.enabled==1) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + adminInfo.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + adminInfo.name + "?";
            }
            bootbox.confirm({

                message: correctMessage,

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
                        CommonMethods.waitingMessage();
                        AdminInfo.get({id: adminInfo.id}).$promise.then(onSuccessGetAdmin);
                    }
                }
            });
        };

        function onSuccessGetAdmin (result) {
            enabledDisabledAdmin(result);
        }

        function enabledDisabledAdmin(adminInfo){
            if(adminInfo.enabled==1){
                adminInfo.enabled = 0;
            } else {
                adminInfo.enabled = 1;
            }
            AdminInfo.update(adminInfo, onSuccessDisabledAdmin);
        }

        function onSuccessDisabledAdmin(data, headers) {

            User.getUserById({
                id: data.userId
            }, onSuccessGetDisabledUser);

        }
        function onSuccessGetDisabledUser(data, headers) {
            if(data.activated==1){
                data.activated = 0;
            } else {
                data.activated = 1;
            }

            User.update(data, onSuccessDisabledUser);

            function onSuccessDisabledUser(data, headers) {
                toastr["success"]("Se ha modificado el estado el admin correctamente.");
                bootbox.hideAll();
                loadAll();
            }
        }
        vm.findAdminsByCondo = function(condo) {
            $("#tableData").fadeOut(0);
            vm.condo = condo;
            if (condo == undefined) {
                loadAll();
            } else {
                loadAll(1);
            }
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
