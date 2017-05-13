(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('admin-info', {
            parent: 'entity',
            url: '/admin-infos?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.adminInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/admin-info/admin-infos.html',
                    controller: 'AdminInfoController',
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
                    $translatePartialLoader.addPart('adminInfo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('admin-info-edit', {
            parent: 'entity',
            url: '/admin-info',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.adminInfo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/admin-info/admin-info-edit.html',
                    controller: 'AdminInfoEditDialogController',
                    controllerAs: 'vm'
                }
            },
        })
        .state('admin-info-detail', {
            parent: 'admin-info',
            url: '/admin-info/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.adminInfo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/admin-info/admin-info-detail.html',
                    controller: 'AdminInfoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adminInfo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AdminInfo', function($stateParams, AdminInfo) {
                    return AdminInfo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'admin-info',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('admin-info-detail.edit', {
            parent: 'admin-info-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                    controller: 'AdminInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdminInfo', function(AdminInfo) {
                            return AdminInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('admin-info.new', {
            parent: 'admin-info',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                    controller: 'AdminInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lastname: null,
                                secondlastname: null,
                                identificationnumber: null,
                                email: null,
                                image: null,
                                imageContentType: null,
                                enabled: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('admin-info', null, { reload: 'admin-info' });
                }, function() {
                    $state.go('admin-info');
                });
            }]
        })
        .state('admin-info.edit', {
            parent: 'admin-info',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/admin-info/admin-info-dialog.html',
                    controller: 'AdminInfoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdminInfo', function(AdminInfo) {
                            return AdminInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('admin-info', null, { reload: 'admin-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('admin-info.delete', {
            parent: 'admin-info',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/admin-info/admin-info-delete-dialog.html',
                    controller: 'AdminInfoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdminInfo', function(AdminInfo) {
                            return AdminInfo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('admin-info', null, { reload: 'admin-info' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
