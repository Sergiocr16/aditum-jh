(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subsection', {
            parent: 'entity',
            url: '/subsection?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.subsection.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsection/subsections.html',
                    controller: 'SubsectionController',
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
                    $translatePartialLoader.addPart('subsection');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('subsection-detail', {
            parent: 'subsection',
            url: '/subsection/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.subsection.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsection/subsection-detail.html',
                    controller: 'SubsectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsection');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Subsection', function($stateParams, Subsection) {
                    return Subsection.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subsection',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subsection-detail.edit', {
            parent: 'subsection-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsection/subsection-dialog.html',
                    controller: 'SubsectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subsection', function(Subsection) {
                            return Subsection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsection.new', {
            parent: 'subsection',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsection/subsection-dialog.html',
                    controller: 'SubsectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                order: null,
                                deleted: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subsection', null, { reload: 'subsection' });
                }, function() {
                    $state.go('subsection');
                });
            }]
        })
        .state('subsection.edit', {
            parent: 'subsection',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsection/subsection-dialog.html',
                    controller: 'SubsectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subsection', function(Subsection) {
                            return Subsection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsection', null, { reload: 'subsection' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsection.delete', {
            parent: 'subsection',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsection/subsection-delete-dialog.html',
                    controller: 'SubsectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Subsection', function(Subsection) {
                            return Subsection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsection', null, { reload: 'subsection' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
