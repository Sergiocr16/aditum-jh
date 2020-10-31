(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('historical-positive', {
            parent: 'entity',
            url: '/historical-positive',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalPositive.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-positive/historical-positives.html',
                    controller: 'HistoricalPositiveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalPositive');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('historical-positive-detail', {
            parent: 'historical-positive',
            url: '/historical-positive/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalPositive.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-positive/historical-positive-detail.html',
                    controller: 'HistoricalPositiveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalPositive');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HistoricalPositive', function($stateParams, HistoricalPositive) {
                    return HistoricalPositive.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'historical-positive',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('historical-positive-detail.edit', {
            parent: 'historical-positive-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-positive/historical-positive-dialog.html',
                    controller: 'HistoricalPositiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalPositive', function(HistoricalPositive) {
                            return HistoricalPositive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-positive.new', {
            parent: 'historical-positive',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-positive/historical-positive-dialog.html',
                    controller: 'HistoricalPositiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                total: null,
                                housenumber: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('historical-positive', null, { reload: 'historical-positive' });
                }, function() {
                    $state.go('historical-positive');
                });
            }]
        })
        .state('historical-positive.edit', {
            parent: 'historical-positive',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-positive/historical-positive-dialog.html',
                    controller: 'HistoricalPositiveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalPositive', function(HistoricalPositive) {
                            return HistoricalPositive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-positive', null, { reload: 'historical-positive' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-positive.delete', {
            parent: 'historical-positive',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-positive/historical-positive-delete-dialog.html',
                    controller: 'HistoricalPositiveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HistoricalPositive', function(HistoricalPositive) {
                            return HistoricalPositive.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-positive', null, { reload: 'historical-positive' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
