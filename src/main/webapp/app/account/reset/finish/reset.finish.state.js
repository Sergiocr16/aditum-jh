(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('finishReset', {
            parent: 'account',
            url: '/reset/finish?key',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/account/reset/finish/reset.finish.html',
                    controller: 'ResetFinishController',
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
                  $rootScope.showLogin = false;
             }],
             onExit: ['$rootScope', function($rootScope) {
                $rootScope.showLogin = true;
             }]
       });
    }
})();
