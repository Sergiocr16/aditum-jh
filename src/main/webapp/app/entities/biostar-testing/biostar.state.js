(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
          .state('biostar', {
            parent: 'entity',
            url: '/biostar-test',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/biostar-testing/biostar.html',
                    controller: 'BioStarController',
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
