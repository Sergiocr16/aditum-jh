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
