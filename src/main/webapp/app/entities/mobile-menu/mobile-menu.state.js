(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('common-area-administration-mobile-menu', {
                parent: 'entity',
                url: '/common-area-reservations-mobile-menu',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
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
                    authorities: ['ROLE_USER','ROLE_OWNER'],
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
    }
})();
