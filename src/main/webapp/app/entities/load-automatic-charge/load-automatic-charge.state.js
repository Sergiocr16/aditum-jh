(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('load-automatic-charge', {
                parent: 'entity',
                url: '/load-automatic-charge',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/load-automatic-charge/load-automatic-charge.html',
                        controller: 'LoadAutomaticChargeController',
                        controllerAs: 'vm'
                    }
                }
            })
    }
})();
