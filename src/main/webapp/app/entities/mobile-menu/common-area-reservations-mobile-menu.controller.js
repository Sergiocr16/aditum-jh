(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('CommonAreaReservationMobileMenuController', CommonAreaReservationMobileMenuController);

        CommonAreaReservationMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope'];

        function CommonAreaReservationMobileMenuController(timeout, $scope, $stateParams, $rootScope) {
            var vm = this;
            $rootScope.active = "common-area-administration-mobile-menu";
            $rootScope.mainTitle = "Reservas";

            vm.menu = [
                {
                    title: "Realizar reserva",
                    icon: "event",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "common-area-reservation-resident-view",
                    show: true,
                },
                {
                    title: "Mis reservaciones",
                    icon: "calendar_today",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "common-area-all-reservations-resident-view",
                    show: true,
                },
                {
                    title: "Amenidades",
                    icon: "local_florist",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "common-area-resident-account",
                    show: true,
                },
            ]

        }
    }

)();
