(function () {
    'use strict';
    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('access-door-macro', {
                parent: 'entity',
                url: '/city-access',
                data: {
                    authorities: ['ROLE_OFFICER_MACRO']
                },
                views: {
                    'access_door_macro@': {
                        templateUrl: 'app/entities/access-door-macro/container-macro.html',
                        controller: 'ContainerMacroController',
                        controllerAs: 'vm'
                    }
                },
            })
            .state('access-door-macro.access', {
                url: '/access',
                data: {
                    authorities: ['ROLE_OFFICER_MACRO']
                },
                templateUrl: 'app/entities/access-door-macro/access-door-macro.html',
                controller: 'AccessDoorMacroController',
                controllerAs: 'vm',
            })
            .state('access-door-macro.register-visitor', {
                url: '/register-visitor',
                data: {
                    authorities: ['ROLE_OFFICER_MACRO']
                },
                templateUrl: 'app/entities/access-door-macro/macro-register-visitor.html',
                controller: 'MacroRegisterVisitorController',
                controllerAs: 'vm',
            })
            .state('access-door-macro.houses', {
                url: '/houses-info',
                data: {
                    authorities: ['ROLE_OFFICER_MACRO']
                },
                templateUrl: 'app/entities/access-door-macro/macro-houses-info.html',
                controller: 'MacroHousesAccessDoorController',
                controllerAs: 'vm',

            })
            .state('access-door-macro.notes', {
                url: '/notes',
                data: {
                    authorities: ['ROLE_OFFICER_MACRO']
                },
                templateUrl: 'app/entities/access-door-macro/macro-notes.html',
                controller: 'HomeServiceDoorController',
                controllerAs: 'vm',
            })
    }

})();
