(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('officer', {
            parent: 'entity',
            url: '/officer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.officer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/officer/officers.html',
                    controller: 'OfficerController',
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
                    $translatePartialLoader.addPart('officer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('officer-detail', {
            parent: 'officer',
            url: '/officer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.officer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/officer/officer-detail.html',
                    controller: 'OfficerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('officer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Officer', function($stateParams, Officer) {
                    return Officer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'officer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('officer-detail.edit', {
            parent: 'officer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer/officer-dialog.html',
                    controller: 'OfficerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Officer', function(Officer) {
                            return Officer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('officer.new', {
            parent: 'officer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer/officer-dialog.html',
                    controller: 'OfficerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                lastname: null,
                                secondlastname: null,
                                image: null,
                                imageContentType: null,
                                email: null,
                                identificationnumber: null,
                                inservice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('officer', null, { reload: 'officer' });
                }, function() {
                    $state.go('officer');
                });
            }]
        })
        .state('officer.edit', {
            parent: 'officer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer/officer-dialog.html',
                    controller: 'OfficerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Officer', function(Officer) {
                            return Officer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('officer', null, { reload: 'officer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('officer.delete', {
            parent: 'officer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer/officer-delete-dialog.html',
                    controller: 'OfficerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Officer', function(Officer) {
                            return Officer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('officer', null, { reload: 'officer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
