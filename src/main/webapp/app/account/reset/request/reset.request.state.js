(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('requestReset', {
            parent: 'account',
            url: '/reset/request',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/account/reset/request/reset.request.html',
                    controller: 'RequestResetController',
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
                setTimeout(function(){
                    $rootScope.$apply(function(){
                        $rootScope.showLogin = false;
                    })
                },0)
            }],
            onExit: ['$rootScope', function($rootScope) {
                setTimeout(function(){
                    $rootScope.$apply(function(){
                        $rootScope.showLogin = true;
                    })
                },0)
            }]
        });
    }
})();
