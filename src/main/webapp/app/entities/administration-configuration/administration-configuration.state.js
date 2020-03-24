(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('administration-configuration-detail', {
            parent: 'entity',
            url: '/administration-config',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.administrationConfiguration.detail.title'
            },
            views: {
                'content@': {
                  templateUrl: 'app/entities/administration-configuration/administration-configuration-detail.html',
                    //   templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                    controller: 'AdministrationConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('administrationConfiguration');
                    return $translate.refresh();
                }],
                entity: ['globalCompany', 'AdministrationConfiguration', function(globalCompany, AdministrationConfiguration) {
                    return AdministrationConfiguration.get({companyId : globalCompany.getId()}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'administration-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }

})();
