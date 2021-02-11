(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('HomeMobileMenuController', HomeMobileMenuController);

        HomeMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope'];

        function HomeMobileMenuController(timeout, $scope, $stateParams, $rootScope) {
            var vm = this;
            $rootScope.mainTitle = "";
            $rootScope.activeOn = "";


            vm.banners = ["/content/images/banner-morning.png", "/content/images/banner-afternoon.png", "/content/images/banner-night.png"];

            vm.defineBanner = function () {
                var now = new Date().getHours();
                console.log(now)
                if (now >= 5 && now <= 13) {
                    vm.currentBanner = vm.banners[0];
                    vm.getting = "Buenos días";
                }
                if (now > 13 && now <= 18) {
                    vm.currentBanner = vm.banners[1];
                    vm.getting = "Buenas tardes";
                }
                if (now > 18 && now < 5) {
                    vm.currentBanner = vm.banners[2];
                    vm.getting = "Buenas noches";
                }
            }
            vm.defineBanner();

            vm.menu = [
                {
                    title: "Reservar amenidad",
                    icon: "event",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "common-area-reservation-resident-view",
                    show: true,
                },
                {
                    title: "Reportar invitado",
                    icon: "person_add_alt",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "visitant-invited-user.new-list",
                    show: true,
                },
                {
                    title: "Informar a oficial",
                    icon: "forward_to_inbox",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "noteNew",
                    show: true,
                },
                {
                    title: "Ver mi estado de cuenta",
                    icon: "account_balance_wallet",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "accountStatus-residentAccount",
                    show: true,
                },
            ]
            vm.menu2 = [
                {
                    title: "Reportar emergencia",
                    icon: "local_fire_department",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "emergency.new",
                    show: true,
                },
                {
                    title: "Enviar observación del condominio",
                    icon: "record_voice_over",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "emergency.new",
                    show: true,
                },
                {
                    title: "Enviar solicitud al administrador",
                    icon: "forum",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "individual-release-user",
                    show: $rootScope.adminCompany.id == 1,
                },
            ]
        }
    }

)();
