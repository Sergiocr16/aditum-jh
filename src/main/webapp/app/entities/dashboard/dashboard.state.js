(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dashboard', {
            parent: 'entity',
            url: '/dashboard',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.brand.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dashboard/dashboard.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
        })
    }

})();
