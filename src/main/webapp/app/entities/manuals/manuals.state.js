(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('manualResidente', {
            parent: 'account',
            url: '/manual-residente',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/entities/manuals/manual-residente.html',
                    controller: 'ManualResidenteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }]
            },
           onEnter: ['$rootScope', function($rootScope) {
              $rootScope.showLogin= false;

           }],
           onExit: ['$rootScope', function($rootScope) {
             $rootScope.showLogin= true;
          }]
        });
    }
})();
