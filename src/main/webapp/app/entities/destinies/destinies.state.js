(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('destinies', {
            parent: 'entity',
            url: '/destinies',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.destinies.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/destinies/destinies.html',
                    controller: 'DestiniesController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('destinies');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('destinies-detail', {
            parent: 'destinies',
            url: '/destinies/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.destinies.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/destinies/destinies-detail.html',
                    controller: 'DestiniesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('destinies');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Destinies', function($stateParams, Destinies) {
                    return Destinies.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'destinies',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('destinies-detail.edit', {
            parent: 'destinies-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/destinies/destinies-dialog.html',
                    controller: 'DestiniesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Destinies', function(Destinies) {
                            return Destinies.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('destinies.new', {
            parent: 'destinies',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/destinies/destinies-dialog.html',
                    controller: 'DestiniesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('destinies', null, { reload: 'destinies' });
                }, function() {
                    $state.go('destinies');
                });
            }]
        })
        .state('destinies.edit', {
            parent: 'destinies',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/destinies/destinies-dialog.html',
                    controller: 'DestiniesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Destinies', function(Destinies) {
                            return Destinies.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('destinies', null, { reload: 'destinies' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('destinies.delete', {
            parent: 'destinies',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/destinies/destinies-delete-dialog.html',
                    controller: 'DestiniesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Destinies', function(Destinies) {
                            return Destinies.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('destinies', null, { reload: 'destinies' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
