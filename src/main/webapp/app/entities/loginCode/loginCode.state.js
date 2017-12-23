(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('insertCode', {
            parent: 'account',
            url: '/registrarse',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/entities/loginCode/insert-logincode.html',
                    controller: 'InsertLoginCodeController',
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
