(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('houses-tabs.data-progress', {
            parent: 'houses-tabs',
            url: '/data-progress',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'Aditum'
            },
            templateUrl: 'app/entities/data-control-progress/data-progress.html',
            controller: 'DataProgressController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('destinies');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();
