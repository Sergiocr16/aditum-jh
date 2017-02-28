(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('emergency', {
            parent: 'entity',
            url: '/emergency?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.emergency.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/emergency/emergencies.html',
                    controller: 'EmergencyController',
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
                    $translatePartialLoader.addPart('emergency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('emergency-detail', {
            parent: 'emergency',
            url: '/emergency/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.emergency.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/emergency/emergency-detail.html',
                    controller: 'EmergencyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('emergency');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Emergency', function($stateParams, Emergency) {
                    return Emergency.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'emergency',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('emergency-detail.edit', {
            parent: 'emergency-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emergency/emergency-dialog.html',
                    controller: 'EmergencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Emergency', function(Emergency) {
                            return Emergency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('emergency.new', {
            parent: 'emergency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emergency/emergency-dialog.html',
                    controller: 'EmergencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                observation: null,
                                isAttended: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('emergency', null, { reload: 'emergency' });
                }, function() {
                    $state.go('emergency');
                });
            }]
        })
        .state('emergency.edit', {
            parent: 'emergency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emergency/emergency-dialog.html',
                    controller: 'EmergencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Emergency', function(Emergency) {
                            return Emergency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('emergency', null, { reload: 'emergency' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('emergency.delete', {
            parent: 'emergency',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emergency/emergency-delete-dialog.html',
                    controller: 'EmergencyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Emergency', function(Emergency) {
                            return Emergency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('emergency', null, { reload: 'emergency' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
