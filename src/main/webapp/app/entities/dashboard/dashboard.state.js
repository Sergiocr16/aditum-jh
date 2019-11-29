(function () {
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
                    authorities: ['ROLE_MANAGER', 'ROLE_JD', 'ROLE_MANAGER_MACRO'],
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
                    authorities: ['ROLE_MANAGER', 'ROLE_JD', 'ROLE_MANAGER_MACRO']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/dashboard/dashboard-company-select.html',
                        controller: 'DashboardCompanySelectController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                    }).result.then(function () {
                        $state.go('dashboard', null, {reload: true, notify: true});
                    }, function () {
                        $state.go('dashboard', null, {reload: true, notify: true});
                    });
                }]
            })
            .state('dashboard.initialConfiguration', {
                parent: 'dashboard',
                url: '/configuracion-inicial',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/dashboard/initialConfiguration.html',
                        controller: 'InitialConfigurationController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            globalCompanyId: ['CommonMethods', '$localStorage', function (CommonMethods, $localStorage) {
                                if ($localStorage.companyId != undefined || $localStorage.companyId != null) {
                                    return CommonMethods.decryptIdUrl($localStorage.companyId)
                                } else {
                                    return null;
                                }
                            }],
                            entity: ['globalCompany', 'AdministrationConfiguration', function(globalCompany, AdministrationConfiguration) {
                                return AdministrationConfiguration.get({companyId : globalCompany.getId()}).$promise;
                            }],
                        }
                    }).result.then(function() {
                        $state.go('dashboard', null, { reload: 'dashboard' });
                    }, function() {
                        $state.go('dashboard');
                    });
                }]
            })
    }

})();
