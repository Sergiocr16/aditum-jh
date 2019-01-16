(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('main-access-door', {
                parent: 'entity',
                url: '/puertaAcceso',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                views: {
                    'access_door@': {
                        templateUrl: 'app/entities/access-door/access-door-container.html',
                        controller: 'AccessDoorController',
                        controllerAs: 'vm'
                    }
                },
            })
            .state('main-access-door.register-visitor', {
                url: '/registrarVisitante',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/register-visitor.html',
                controller: 'RegisterVisitorController',
                controllerAs: 'vm',
            })
            .state('main-access-door.filiales-info', {
                url: '/infoFiliales',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/houses-info-access-door.html',
                controller: 'HousesInfoAccessDoorController',
                controllerAs: 'vm',
            })
            .state('main-access-door.home-service', {
                url: '/homeService',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/home-service.html',
                controller: 'HomeServiceDoorController',
                controllerAs: 'vm',
            })
            .state('main-access-door.change-watch', {
                url: '/watchChange',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl:'app/entities/access-door/change-watch.html',
                controller: 'ChangeWatchController',
                controllerAs: 'vm',
            })
    }

})();
