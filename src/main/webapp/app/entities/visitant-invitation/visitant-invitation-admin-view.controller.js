(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitedAdminViewController', VisitantInvitedAdminViewController);

    VisitantInvitedAdminViewController.$inject = ['$localStorage', 'InvitationSchedule', 'VisitantInvitation', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', '$state', 'CommonMethods', 'WSVisitorInvitation', 'WSDeleteEntity', 'globalCompany', 'Modal','House'];

    function VisitantInvitedAdminViewController($localStorage, InvitationSchedule, VisitantInvitation, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, $state, CommonMethods, WSVisitorInvitation, WSDeleteEntity, globalCompany, Modal,House) {
        $rootScope.active = "visitantsAdminView";
        var vm = this;
        vm.changeHouse = function (house, i) {
            vm.isReady = false;
            vm.page = 0;
            vm.links = {
                last: 0
            };
            vm.residents = [];
            $localStorage.infoHouseNumber = house;
            vm.infoHouseResident = house;
            vm.selectedIndex = i ;
            $rootScope.mainTitle = "Invitaciones de la filial " + house.housenumber;
            loadAll(0);
        };

        House.query({companyId: globalCompany.getId()}).$promise.then(onSuccessHouses);

        function onSuccessHouses(data, headers) {
            vm.houses = data;
            if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                vm.changeHouse($localStorage.infoHouseNumber, 1);
            } else {
                vm.changeHouse(data[0], 1);
                vm.infoHouseResident = data[0];
                loadAll(0);
            }
        }

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;


        vm.cancelInvitation = function (visitor) {
            Modal.confirmDialog("¿Está seguro que desea revocar el permiso de acceso a " + visitor.name + " " + visitor.lastname + "?", "", function () {
                Modal.showLoadingBar();
                visitor.status = 2;
                VisitantInvitation.update(visitor, success)
            })
        };
        function success(data) {
            WSVisitorInvitation.sendActivity(data);
            Modal.hideLoadingBar();
            Modal.toast("Se ha cancelado la invitación correctamente");
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
        vm.renewVisitorWithSchedule = function (visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-admin-view.editSchedule', {
                id: encryptedId
            })
        }
        vm.renewVisitor = function (visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-admin-view.edit', {
                id: encryptedId
            })
        }

        function loadAll(timeFormat) {
            vm.timeFormat = timeFormat;
            vm.isReady = false;
            vm.timeFormatTitle = timeFormat == 0 ? 'por intervalo de fechas' : 'por programaciones semanales';
            VisitantInvitation.findInvitedByHouse({
                companyId: globalCompany.getId(),
                houseId: vm.infoHouseResident.id,
                timeFormat: timeFormat
            }).$promise.then(onSuccess);

            function onSuccess(data) {
                angular.forEach(vm.houses, function (value, key) {
                    if ($localStorage.infoHouseNumber != null || $localStorage.infoHouseNumber != undefined) {
                        if (value.id == $localStorage.infoHouseNumber.id) {
                            vm.selectedIndex = key ;
                        }
                    }
                });
                if (vm.timeFormat == 0) {
                    angular.forEach(data, function (value, key) {
                        var secondlN = value.secondlastname!=null?value.secondlastname:"";
                        value.fullName = value.name + " " + value.lastname + " " + secondlN;
                        if (value.identificationnumber == "") {
                            value.identificationnumber = null;
                        }
                    });

                } else {
                    angular.forEach(data, function (value, key) {
                        var secondlN = value.secondlastname!=null?value.secondlastname:"";
                        value.fullName = value.name + " " + value.lastname + " " + secondlN;
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
                console.log(vm.visitants)
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
        vm.changeTimeFormat = function (timeFormat) {
            vm.isReady = false;
            loadAll(timeFormat);
            $localStorage.timeFormat = timeFormat;
        }
        vm.visitorProveedor = function(visitor){
            if(visitor.proveedor == null || visitor.proveedor == undefined || visitor.proveedor == "" ){
                return false;
            }
            return true;
        }
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
        vm.hasPermission = function (visitor) {
            if (visitor.status == 2) {
                return false;
            }
            return vm.isBetweenDate(visitor)
        };
        vm.hasPermissionSchedule = function (visitor) {
            if (visitor.status == 2) {
                return false;
            } else {
                return true;
            }
        }
    }
})();
