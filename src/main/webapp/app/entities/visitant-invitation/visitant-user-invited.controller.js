(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitedUserController', VisitantInvitedUserController);

    VisitantInvitedUserController.$inject = ['$localStorage', 'InvitationSchedule', 'VisitantInvitation', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', '$state', 'CommonMethods', 'WSVisitor', 'WSDeleteEntity', 'companyUser', 'globalCompany', 'Modal'];

    function VisitantInvitedUserController($localStorage, InvitationSchedule, VisitantInvitation, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, $state, CommonMethods, WSVisitor, WSDeleteEntity, companyUser, globalCompany, Modal) {
        var vm = this;
        vm.Principal;
        $rootScope.active = "residentsInvitedVisitors";
        $rootScope.mainTitle = "Visitantes invitados";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        Principal.identity().then(function (account) {
            switch (account.authorities[0]) {
                case "ROLE_USER":
                    vm.userType = 1;
                    break;
                case "ROLE_MANAGER":
                    vm.userType = 2;
                    break;
            }
            if ($localStorage.timeFormat != undefined) {
                loadAll($localStorage.timeFormat);
            } else {
                loadAll(0);
            }
            ;

        });
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;




        function loadAll(timeFormat) {
            vm.timeFormat = timeFormat;
            vm.isReady = false;
            vm.timeFormatTitle = timeFormat == 0 ? 'por intervalo de fechas' : 'por programaciones semanales';
           console.log(vm.userType)
            if (vm.userType == 1) {
                VisitantInvitation.findInvitedByHouse({
                    companyId: globalCompany.getId(),
                    houseId: companyUser.houseId,
                    timeFormat: timeFormat
                }).$promise.then(onSuccess);
            }else{
                VisitantInvitation.findInvitedForAdmins({
                    companyId: globalCompany.getId(),
                    timeFormat: timeFormat
                }).$promise.then(onSuccess);

            }

            function onSuccess(data) {
                if (vm.timeFormat == 0) {
                    angular.forEach(data, function (value, key) {
                        value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
                        if (value.identificationnumber == "") {
                            value.identificationnumber = null;
                        }
                    });

                } else {
                    angular.forEach(data, function (value, key) {
                        value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
                        if (value.identificationnumber == "") {
                            value.identificationnumber = null;
                        }
                        InvitationSchedule.findSchedulesByInvitation({
                            invitationId: value.id
                        }, function (result) {
                            value.lunes = result[0].lunes;
                            value.martes = result[0].martes;
                            value.miercoles = result[0].miercoles;
                            value.jueves = result[0].jueves;
                            value.viernes = result[0].viernes;
                            value.sabado = result[0].sabado;
                            value.domingo = result[0].domingo;

                        });
                    });

                }
                vm.visitants = data;
                vm.page = pagingParams.page;
                vm.isReady = true;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
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

        vm.hasPermission = function (visitor) {
            if (visitor.status == 2) {
                return false;
            }
            return vm.isBetweenDate(visitor)
        };

        vm.isBetweenDate = function (visitor) {
            var currentTime = new Date().getTime();
            var intiTime = new Date(visitor.invitationstartingtime).getTime();
            var finalTime = new Date(visitor.invitationlimittime).getTime();
            if (intiTime <= currentTime && currentTime <= finalTime) {
                return true;
            } else {
                return false
            }
        };
        vm.hasPermissionSchedule = function (visitor) {
            if (visitor.status == 2) {
                return false;
            } else {
                return true;
            }
        }
        vm.renewVisitor = function (visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-user.edit', {
                id: encryptedId
            })
        }
        vm.renewVisitorWithSchedule = function (visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-user.editSchedule', {
                id: encryptedId
            })
        }
        vm.deleteInvitedVisitor = function (visitor) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el registro?", "", function () {
                Modal.showLoadingBar();
                VisitantInvitation.delete({
                    id: visitor.id
                }, successDelete);

            })


            function successDelete() {
                loadAll(vm.timeFormat);
                Modal.toast("Se ha eliminado el registro correctamente");
                Modal.hideLoadingBar();
                WSDeleteEntity.sendActivity({type: 'visitor', id: visitor.id})
            }
        }

        vm.cancelInvitation = function (visitor) {
            Modal.confirmDialog("¿Está seguro que desea revocar el permiso de acceso a " + visitor.name + " " + visitor.lastname + "?", "", function () {
                Modal.showLoadingBar();
                visitor.status = 2;
                VisitantInvitation.update(visitor, success)
            })
        };

        function success(data) {
            WSVisitor.sendActivity(data);
            Modal.hideLoadingBar();
            Modal.toast("Se ha cancelado la invitación correctamente");
        }

        vm.changeTimeFormat = function (timeFormat) {
            vm.isReady = false;
            loadAll(timeFormat);
            $localStorage.timeFormat = timeFormat;
        }

    }
})();
