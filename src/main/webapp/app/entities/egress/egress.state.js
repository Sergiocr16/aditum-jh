(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('egress', {
            parent: 'entity',
            url: '/egress?page&sort&search',
            data: {
                 authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.egress.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/egress/egresses.html',
                    controller: 'EgressController',
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
                    $translatePartialLoader.addPart('egress');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        }).state('egress-report', {
              parent: 'egress',
              url: '/report',
              data: {
                   authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                  pageTitle: 'aditumApp.egress.home.title'
              },
              views: {
                  'content@': {
                      templateUrl: 'app/entities/egress/generateReport.html',
                      controller: 'EgressGenerateReportController',
                      controllerAs: 'vm'
                  }
              },
              resolve: {
                  translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                      $translatePartialLoader.addPart('egress');
                      $translatePartialLoader.addPart('global');
                      return $translate.refresh();
                  }]
              }
          })
        .state('egress-detail', {
            parent: 'egress',
            url: '/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
                pageTitle: 'aditumApp.egress.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/egress/egress-detail.html',
                    controller: 'EgressDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('egress');
                    return $translate.refresh();
                }],
                 entity: ['$stateParams', 'Egress','CommonMethods', function($stateParams, Egress, CommonMethods) {
                 var id = CommonMethods.decryptIdUrl($stateParams.id)
                       return Egress.get({id : id}).$promise;
                   }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'egress',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('egress-detail.edit', {
            parent: 'egress-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/egress/egress-dialog.html',
                    controller: 'EgressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Egress', function(Egress) {
                            return Egress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
          .state('egress.new', {
                  parent: 'egress',
                                url: '/new',
                                data: {
                                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                                },
                                views: {
                                    'content@': {
                                        templateUrl: 'app/entities/egress/egress-dialog.html',
                                        controller: 'EgressDialogController',
                                        controllerAs: 'vm',
                                    }
                                },
                                resolve: {
                                    entity: function() {
                                        return {
                                             date: null,
                                               folio: null,
                                               account: null,
                                               category: null,
                                               paymentMethod: null,
                                               concept: null,
                                               total: null,
                                               reference: null,
                                               comments: null,
                                               proveedor: null,
                                               paymentDate: null,
                                               expirationDate: null,
                                               id: null
                                        };
                                    },
                                    previousState: ["$state", function($state) {
                                        var currentStateData = {
                                            name: $state.current.name || 'egress',
                                            params: $state.params,
                                            url: $state.href($state.current.name, $state.params)
                                        };
                                        return currentStateData;
                                    }]
                                }
                })
        .state('egress.edit', {
            parent: 'egress',
            url: '/{id}/reportPayment',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER']
            },
              views: {
                    'content@': {
                       templateUrl: 'app/entities/egress/reportPayment.html',
                       controller: 'EgressDialogController',
                       controllerAs: 'vm',
                    }
                },

               resolve: {
                   entity: ['$stateParams', 'Egress','CommonMethods', function($stateParams, Egress, CommonMethods) {
                      var id = CommonMethods.decryptIdUrl($stateParams.id)
                            return Egress.get({id : id}).$promise;
                        }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'egress',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/entities/egress/egress-dialog.html',
//                    controller: 'EgressDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: ['Egress', function(Egress) {
//                            return Egress.get({id : $stateParams.id}).$promise;
//                        }]
//                    }
//                }).result.then(function() {
//                    $state.go('egress', null, { reload: 'egress' });
//                }, function() {
//                    $state.go('^');
//                });
//            }]
        })
        .state('egress.delete', {
            parent: 'egress',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/egress/egress-delete-dialog.html',
                    controller: 'EgressDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Egress', function(Egress) {
                            return Egress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('egress', null, { reload: 'egress' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
