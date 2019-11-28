(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('protocol', {
            parent: 'entity',
            url: '/protocol?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.protocol.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/protocol/protocols.html',
                    controller: 'ProtocolController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('protocol');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('protocol-detail', {
            parent: 'protocol',
            url: '/protocol/{id}',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.protocol.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/protocol/protocol-detail.html',
                    controller: 'ProtocolDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('protocol');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Protocol', function($stateParams, Protocol) {
                    return Protocol.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'protocol',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('protocol-detail.edit', {
            parent: 'protocol-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/protocol/protocol-dialog.html',
                    controller: 'ProtocolDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Protocol', function(Protocol) {
                            return Protocol.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('protocol.new', {
            parent: 'protocol',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },    views: {
                'content@': {
                    templateUrl: 'app/entities/protocol/protocol-dialog.html',
                    controller: 'ProtocolDialogController',
                    controllerAs: 'vm',
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: null,
                        description: null,
                        deleted: null,
                        image: null,
                        imageContentType: null,
                        id: null
                    };
                }
            }

        })
        .state('protocol.edit', {
            parent: 'protocol',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/protocol/protocol-dialog.html',
                    controller: 'ProtocolDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Protocol', function(Protocol) {
                            return Protocol.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('protocol', null, { reload: 'protocol' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('protocol.delete', {
            parent: 'protocol',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/protocol/protocol-delete-dialog.html',
                    controller: 'ProtocolDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Protocol', function(Protocol) {
                            return Protocol.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('protocol', null, { reload: 'protocol' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
