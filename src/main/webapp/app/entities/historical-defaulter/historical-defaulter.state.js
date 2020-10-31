(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('historical-defaulter', {
            parent: 'entity',
            url: '/historical-defaulter',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalDefaulter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulters.html',
                    controller: 'HistoricalDefaulterController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalDefaulter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('historical-defaulter-detail', {
            parent: 'historical-defaulter',
            url: '/historical-defaulter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalDefaulter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulter-detail.html',
                    controller: 'HistoricalDefaulterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalDefaulter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HistoricalDefaulter', function($stateParams, HistoricalDefaulter) {
                    return HistoricalDefaulter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'historical-defaulter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('historical-defaulter-detail.edit', {
            parent: 'historical-defaulter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulter-dialog.html',
                    controller: 'HistoricalDefaulterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalDefaulter', function(HistoricalDefaulter) {
                            return HistoricalDefaulter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-defaulter.new', {
            parent: 'historical-defaulter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulter-dialog.html',
                    controller: 'HistoricalDefaulterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                total: null,
                                date: null,
                                categories: null,
                                housenumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter', null, { reload: 'historical-defaulter' });
                }, function() {
                    $state.go('historical-defaulter');
                });
            }]
        })
        .state('historical-defaulter.edit', {
            parent: 'historical-defaulter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulter-dialog.html',
                    controller: 'HistoricalDefaulterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalDefaulter', function(HistoricalDefaulter) {
                            return HistoricalDefaulter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter', null, { reload: 'historical-defaulter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-defaulter.delete', {
            parent: 'historical-defaulter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter/historical-defaulter-delete-dialog.html',
                    controller: 'HistoricalDefaulterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HistoricalDefaulter', function(HistoricalDefaulter) {
                            return HistoricalDefaulter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter', null, { reload: 'historical-defaulter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
