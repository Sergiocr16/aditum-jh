(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('common-area', {
            parent: 'entity',
            url: '/common-area?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonArea.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area/common-areas.html',
                    controller: 'CommonAreaController',
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
                    $translatePartialLoader.addPart('commonArea');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('common-area-detail', {
            parent: 'common-area',
            url: '/common-area/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonArea.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area/common-area-detail.html',
                    controller: 'CommonAreaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commonArea');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CommonArea', function($stateParams, CommonArea) {
                    return CommonArea.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'common-area',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('common-area-detail.edit', {
            parent: 'common-area-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area/common-area-dialog.html',
                    controller: 'CommonAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonArea', function(CommonArea) {
                            return CommonArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area.new', {
            parent: 'common-area',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area/common-area-dialog.html',
                    controller: 'CommonAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                reservationCharge: null,
                                devolutionAmmount: null,
                                chargeRequired: null,
                                reservationWithDebt: null,
                                picture: null,
                                pictureContentType: null,
                                maximunHours: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('common-area', null, { reload: 'common-area' });
                }, function() {
                    $state.go('common-area');
                });
            }]
        })
        .state('common-area.edit', {
            parent: 'common-area',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area/common-area-dialog.html',
                    controller: 'CommonAreaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonArea', function(CommonArea) {
                            return CommonArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area', null, { reload: 'common-area' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area.delete', {
            parent: 'common-area',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area/common-area-delete-dialog.html',
                    controller: 'CommonAreaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CommonArea', function(CommonArea) {
                            return CommonArea.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area', null, { reload: 'common-area' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
