(function () {
        'use strict';

        angular
            .module('aditumApp')
            .controller('AdministrativeMobileMenuController', AdministrativeMobileMenuController);

        AdministrativeMobileMenuController.$inject = ['$timeout', '$scope', '$stateParams', '$rootScope', 'CommonMethods', 'globalCompany'];

        function AdministrativeMobileMenuController(timeout, $scope, $stateParams, $rootScope, CommonMethods, globalCompany) {
            var vm = this;
            $rootScope.active = "administrative-mobile-menu";
            $rootScope.mainTitle = "Administrativo";
            vm.menu = [
                {
                    title: "Observaciones",
                    icon: "record_voice_over",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "complaint-user",
                    show: true,
                },
                {
                    title: "Solicitudes",
                    icon: "forum",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "individual-release-user",
                    show: $rootScope.adminCompany.id == 1,
                },
                {
                    title: "Revisiones rutinarias",
                    icon: "assignment_turned_in",
                    authoritites: "ROLE_USER,ROLE_OWNER",
                    uisref: "revision",
                    show: true,
                },
            ]
        }
    }

)();
