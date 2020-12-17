(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('historical-defaulter-charge', {
            parent: 'entity',
            url: '/historical-defaulter-charge',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalDefaulterCharge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charges.html',
                    controller: 'HistoricalDefaulterChargeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalDefaulterCharge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('historical-defaulter-charge-detail', {
            parent: 'historical-defaulter-charge',
            url: '/historical-defaulter-charge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.historicalDefaulterCharge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charge-detail.html',
                    controller: 'HistoricalDefaulterChargeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('historicalDefaulterCharge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HistoricalDefaulterCharge', function($stateParams, HistoricalDefaulterCharge) {
                    return HistoricalDefaulterCharge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'historical-defaulter-charge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('historical-defaulter-charge-detail.edit', {
            parent: 'historical-defaulter-charge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charge-dialog.html',
                    controller: 'HistoricalDefaulterChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalDefaulterCharge', function(HistoricalDefaulterCharge) {
                            return HistoricalDefaulterCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-defaulter-charge.new', {
            parent: 'historical-defaulter-charge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charge-dialog.html',
                    controller: 'HistoricalDefaulterChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                date: null,
                                concept: null,
                                consecutive: null,
                                ammount: null,
                                leftToPay: null,
                                abonado: null,
                                defaultersDay: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter-charge', null, { reload: 'historical-defaulter-charge' });
                }, function() {
                    $state.go('historical-defaulter-charge');
                });
            }]
        })
        .state('historical-defaulter-charge.edit', {
            parent: 'historical-defaulter-charge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charge-dialog.html',
                    controller: 'HistoricalDefaulterChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HistoricalDefaulterCharge', function(HistoricalDefaulterCharge) {
                            return HistoricalDefaulterCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter-charge', null, { reload: 'historical-defaulter-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('historical-defaulter-charge.delete', {
            parent: 'historical-defaulter-charge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/historical-defaulter-charge/historical-defaulter-charge-delete-dialog.html',
                    controller: 'HistoricalDefaulterChargeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HistoricalDefaulterCharge', function(HistoricalDefaulterCharge) {
                            return HistoricalDefaulterCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('historical-defaulter-charge', null, { reload: 'historical-defaulter-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
