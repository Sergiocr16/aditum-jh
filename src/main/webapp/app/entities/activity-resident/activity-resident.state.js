(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity-resident', {
            parent: 'entity',
            url: '/activity-resident?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.activityResident.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-resident/activity-residents.html',
                    controller: 'ActivityResidentController',
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
                    $translatePartialLoader.addPart('activityResident');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activity-resident-detail', {
            parent: 'activity-resident',
            url: '/activity-resident/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.activityResident.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-resident/activity-resident-detail.html',
                    controller: 'ActivityResidentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityResident');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActivityResident', function($stateParams, ActivityResident) {
                    return ActivityResident.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'activity-resident',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('activity-resident-detail.edit', {
            parent: 'activity-resident-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-resident/activity-resident-dialog.html',
                    controller: 'ActivityResidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityResident', function(ActivityResident) {
                            return ActivityResident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-resident.new', {
            parent: 'activity-resident',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-resident/activity-resident-dialog.html',
                    controller: 'ActivityResidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                date: null,
                                seen: null,
                                image: null,
                                type: null,
                                user: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity-resident', null, { reload: 'activity-resident' });
                }, function() {
                    $state.go('activity-resident');
                });
            }]
        })
        .state('activity-resident.edit', {
            parent: 'activity-resident',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-resident/activity-resident-dialog.html',
                    controller: 'ActivityResidentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityResident', function(ActivityResident) {
                            return ActivityResident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-resident', null, { reload: 'activity-resident' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-resident.delete', {
            parent: 'activity-resident',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-resident/activity-resident-delete-dialog.html',
                    controller: 'ActivityResidentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActivityResident', function(ActivityResident) {
                            return ActivityResident.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-resident', null, { reload: 'activity-resident' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
