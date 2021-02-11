(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {

        $stateProvider
            .state('home-mobile-menu', {
                parent: 'entity',
                url: '/home-mobile-menu',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mobile-menu/home-mobile-menu.html',
                        controller: 'HomeMobileMenuController',
                        controllerAs: 'vm',
                    }
                },
            })
            .state('common-area-administration-mobile-menu', {
                parent: 'entity',
                url: '/common-area-reservations-mobile-menu',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mobile-menu/items-mobile-menu.html',
                        controller: 'CommonAreaReservationMobileMenuController',
                        controllerAs: 'vm',
                    }
                },
            })
            .state('security-mobile-menu', {
                parent: 'entity',
                url: '/security-mobile-menu',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mobile-menu/items-mobile-menu.html',
                        controller: 'SecurityMobileMenuController',
                        controllerAs: 'vm',
                    }
                },
            })
            .state('finances-mobile-menu', {
                parent: 'entity',
                url: '/finances-mobile-menu',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mobile-menu/items-mobile-menu.html',
                        controller: 'FinancesMobileMenuController',
                        controllerAs: 'vm',
                    }
                },
            })
            .state('administrative-mobile-menu', {
                parent: 'entity',
                url: '/administrative-mobile-menu',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mobile-menu/items-mobile-menu.html',
                        controller: 'AdministrativeMobileMenuController',
                        controllerAs: 'vm',
                    }
                },
            })
    }
})();
