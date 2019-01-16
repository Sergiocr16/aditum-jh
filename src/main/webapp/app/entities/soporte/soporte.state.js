(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
          .state('soporte', {
            parent: 'entity',
            url: '/soporte',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_USER','ROLE_RH','ROLE_JD'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/soporte/soporte.html',
                    controller: 'SoporteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resident');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })

    }

})();
