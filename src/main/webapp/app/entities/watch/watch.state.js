(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('turno', {
            parent: 'entity',
            url: '/turno/{companyId}/?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER','ROLE_ADMIN','ROLE_RH'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/watch/watches.html',
                    controller: 'WatchController',
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
                    $translatePartialLoader.addPart('watch');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })  .state('watch-in-residents', {
                      parent: 'entity',
                      url: '/watch-residents?page&sort&search',
                      data: {
                          authorities: ['ROLE_USER'],
                      },
                      views: {
                          'content@': {
                              templateUrl: 'app/entities/watch/watch-in-resident.html',
                              controller: 'WatchInResidentController',
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
                              $translatePartialLoader.addPart('watch');
                              $translatePartialLoader.addPart('global');
                              return $translate.refresh();
                          }]
                      }
                  })
        .state('watch-detail', {
            parent: 'turno',
            url: '/watch/{id}',
            data: {
                authorities: ['ROLE_MANAGER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/watch/watch-detail.html',
                    controller: 'WatchDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('watch');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Watch', function($stateParams, Watch) {
                    return Watch.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'turno',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('watch-detail.edit', {
            parent: 'watch-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/watch/watch-dialog.html',
                    controller: 'WatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Watch', function(Watch) {
                            return Watch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('turno.new', {
            parent: 'turno',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/watch/watch-dialog.html',
                    controller: 'WatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                initialtime: null,
                                finaltime: null,
                                responsableofficer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('turno', null, { reload: 'turno' });
                }, function() {
                    $state.go('turno');
                });
            }]
        })
        .state('turno.edit', {
            parent: 'turno',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/watch/watch-dialog.html',
                    controller: 'WatchDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Watch', function(Watch) {
                            return Watch.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('turno', null, { reload: 'turno' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
