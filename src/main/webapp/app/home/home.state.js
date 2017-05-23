(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    return $translate.refresh();
                }]
            },
//            onEnter: ['Principal', '$state', function(Principal, $state) {
//            if(Principal.isAuthenticated){
//            Principal.identity().then(function(account){
//            if(account !== null){
//            if(account.authorities[0] === 'ROLE_USER'){
//            console.log('hola')
//                $state.go('residentByHouse');
//             }
//             }
//            })
//            }
//            }]
        });
    }
})();
