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
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dashboard/dashboard.html',
                    controller: 'DashboardController',
                    controllerAs: 'vm'
                }
            },
        })
       .state('dashboard.selectCompany', {
            parent: 'dashboard',
            url: '/select',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dashboard/dashboard-company-select.html',
                    controller: 'DashboardCompanySelectController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                }).result.then(function() {
                    $state.go('dashboard', {}, { reload: true });
                }, function() {
                    $state.go('dashboard',{}, { reload: true });
                });
            }]
        })
    }

})();
