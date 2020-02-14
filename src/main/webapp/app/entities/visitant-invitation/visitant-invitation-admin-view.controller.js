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
            console.log(house)
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
            console.log($localStorage.infoHouseNumber)
            if ($localStorage.infoHouseNumber !== undefined || $localStorage.infoHouseNumber !== null) {
                vm.changeHouse($localStorage.infoHouseNumber, 1);
            } else {
                vm.infoHouseResident = data[0];
                loadAll(0);
            }
        }

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;




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
        vm.changeTimeFormat = function (timeFormat) {
            vm.isReady = false;
            loadAll(timeFormat);
            $localStorage.timeFormat = timeFormat;
        }
    }
})();
