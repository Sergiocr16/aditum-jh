(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerController', OfficerController);

    OfficerController.$inject = ['User','$state','CommonMethods','DataUtils', 'Officer', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','Principal','$rootScope'];

    function OfficerController(User,$state,CommonMethods,DataUtils, Officer, ParseLinks, AlertService, paginationConstants, pagingParams,Principal,$rootScope) {
        var enabledOptions = true;
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    vm.radiostatus=true;

        setTimeout(function(){loadAll();

        $rootScope.active = "officers";
        },500);

        vm.editOfficer = function(id){
         var encryptedId = CommonMethods.encryptIdUrl(id)
            $state.go('officer.edit', {
                id: encryptedId
          })
        }
        vm.switchEnabledResidents = function() {
            enabledOptions = true;
             vm.radiostatus=true;
             $("#radio18").prop("checked", "checked")
              $("#tableData").fadeOut(0);
                         setTimeout(function() {
                             $("#loadingIcon").fadeIn(100);
                         }, 200)
                         loadAll();
        }
        vm.switchDisabledResidents = function() {
            enabledOptions = false;
             vm.radiostatus=false;
             $("#radio19").prop("checked", "checked")
              $("#tableData").fadeOut(0);
                         setTimeout(function() {
                             $("#loadingIcon").fadeIn(100);
                         }, 200)
                         loadAll();
        }

        function loadAll () {
         vm.canEditOfficers = $rootScope.companyUser.administradaOficiales;
            if (enabledOptions) {
                changesTitles();
                Officer.officersEnabled({
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            } else {
                changesTitles();
                Officer.officersDisabled({
                    companyId: $rootScope.companyId,
                }).$promise.then(onSuccess, onError);
            }

            function onSuccess(data) {
               vm.officers = data;
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableData").fadeIn('slow');
                },900 )


            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

            vm.detailOfficer= function(id){
                var encryptedId = CommonMethods.encryptIdUrl(id)
                $state.go('officer.details', {
                    id: encryptedId
                })
            }
            function changesTitles () {
            if(vm.canEditOfficers==1){

            if (enabledOptions) {
                                vm.title = "Oficiales habilitados";
                                vm.buttonTitle = "Ver oficiales deshabilitados";
                                vm.actionButtonTitle = "Deshabilitar";

                            } else {
                                vm.title = "Oficiales deshabilitados";
                                vm.buttonTitle = "Ver oficiales habilitados";
                                vm.actionButtonTitle = "Habilitar";

                            }
            }else{
            vm.title = "Oficiales asignados al condominio";
            }
            }
        }



        vm.deleteOfficer = function(officer) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar al oficial " + officer.name + " " + officer.lastname +"?",
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
                        vm.login = officer.userLogin;
                        Officer.delete({
                            id: officer.id
                        }, onSuccessDelete);


                    }
                }
            });

            function onSuccessDelete () {

                    toastr["success"]("Se ha eliminado el oficial correctamente.");
                    loadAll();


            }

        };

        vm.disableEnabledOfficer = function(officerInfo) {

            var correctMessage;
            if (enabledOptions) {
                correctMessage = "¿Está seguro que desea deshabilitar al residente " + officerInfo.name + "?";
            } else {
                correctMessage = "¿Está seguro que desea habilitar al residente " + officerInfo.name + "?";
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
                        Officer.get({id: officerInfo.id}).$promise.then(onSuccessGetOfficer);

                    }
                }
            });
        };
        function onSuccessGetOfficer (result) {
            enabledDisabledOfficer(result);
        }

        function enabledDisabledOfficer(officer){
            if (enabledOptions) {
                officer.enable = false;
                Officer.update(officer, onSuccessDisabledOfficer);
            } else {
                officer.enable = true;
                Officer.update(officer, onSuccessEnabledOfficer);

            }
        }

        function onSuccessDisabledOfficer(data, headers) {
                   toastr["success"]("Se ha deshabilitado el oficial correctamente.");
                   bootbox.hideAll();
                   loadAll();

        }

    function onSuccessEnabledOfficer(onSuccessEnabledOfficer, headers) {
                   toastr["success"]("Se ha habilitado el oficial correctamente.");
                   bootbox.hideAll();
                   loadAll();

        }
        function loadPage(page) {
            vm.page = page;
            vm.transition();
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
