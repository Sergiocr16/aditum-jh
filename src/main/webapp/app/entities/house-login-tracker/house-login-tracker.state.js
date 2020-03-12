(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('house-login-tracker', {
            parent: 'entity',
            url: '/house-login-tracker',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.houseLoginTracker.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/house-login-tracker/house-login-trackers.html',
                    controller: 'HouseLoginTrackerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('houseLoginTracker');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('house-login-tracker-detail', {
            parent: 'house-login-tracker',
            url: '/house-login-tracker/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.houseLoginTracker.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/house-login-tracker/house-login-tracker-detail.html',
                    controller: 'HouseLoginTrackerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('houseLoginTracker');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'HouseLoginTracker', function($stateParams, HouseLoginTracker) {
                    return HouseLoginTracker.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'house-login-tracker',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('house-login-tracker-detail.edit', {
            parent: 'house-login-tracker-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house-login-tracker/house-login-tracker-dialog.html',
                    controller: 'HouseLoginTrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HouseLoginTracker', function(HouseLoginTracker) {
                            return HouseLoginTracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('house-login-tracker.new', {
            parent: 'house-login-tracker',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house-login-tracker/house-login-tracker-dialog.html',
                    controller: 'HouseLoginTrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                lastTime: null,
                                user: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('house-login-tracker', null, { reload: 'house-login-tracker' });
                }, function() {
                    $state.go('house-login-tracker');
                });
            }]
        })
        .state('house-login-tracker.edit', {
            parent: 'house-login-tracker',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house-login-tracker/house-login-tracker-dialog.html',
                    controller: 'HouseLoginTrackerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['HouseLoginTracker', function(HouseLoginTracker) {
                            return HouseLoginTracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('house-login-tracker', null, { reload: 'house-login-tracker' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('house-login-tracker.delete', {
            parent: 'house-login-tracker',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/house-login-tracker/house-login-tracker-delete-dialog.html',
                    controller: 'HouseLoginTrackerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['HouseLoginTracker', function(HouseLoginTracker) {
                            return HouseLoginTracker.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('house-login-tracker', null, { reload: 'house-login-tracker' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
