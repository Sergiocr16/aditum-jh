(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('SecurityMobileMenuController', SecurityMobileMenuController);

        SecurityMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope'];

        function SecurityMobileMenuController(timeout, $scope, $stateParams, $rootScope) {
            var vm = this;
            $rootScope.active = "security-mobile-menu";
            $rootScope.mainTitle = "Seguridad";

            vm.menu = [
                {
                    title: "Reportar invitado",
                    icon: "person_add_alt",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "visitant-invited-user.new",
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
                    title: "Reportar reunión",
                    icon: "group_add",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "visitant-invited-user.new-list",
                    show: true,
                },
                {
                    title: "Reportar emergencia",
                    icon: "local_fire_department",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "emergency.new",
                    show: true,
                },
                {
                    title: "Mis invitados",
                    icon: "hail",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "visitant-invited-user",
                    show: true,
                },
                {
                    title: "Bitácora de ingresos",
                    icon: "follow_the_signs",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "visitant",
                    show: true,
                },
            ]

        }
    }

)();
