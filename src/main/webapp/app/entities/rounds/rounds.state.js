(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('rounds', {
                parent: 'entity',
                url: '/rondas',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_JD'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rounds/rounds.html',
                        controller: 'RoundsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('soporte');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('round-detail', {
                parent: 'rounds',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_JD'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/rounds/round-detail.html',
                        controller: 'RoundDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('soporte');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Rounds', 'CommonMethods', function ($stateParams, Rounds, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Rounds.getOne({uid: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'soporte',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
    }

})();
