(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('banco', {
            parent: 'entity',
            url: '/banco?page&sort&search',
            data: {
         authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banco/bancos.html',
                    controller: 'BancoController',
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
                    $translatePartialLoader.addPart('banco');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })    .state('banco-configuration', {
                      parent: 'entity',
                      url: '/banco-configuration?page&sort&search',
                      data: {
                   authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                      },
                      views: {
                          'content@': {
                              templateUrl: 'app/entities/banco/bancos-configuration.html',
                              controller: 'BancoController',
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
                              $translatePartialLoader.addPart('banco');
                              $translatePartialLoader.addPart('global');
                              return $translate.refresh();
                          }]
                      }
                  })
        .state('banco-detail', {
            parent: 'banco',
            url: '/banco/{id}',
            data: {
               authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banco/banco-detail.html',
                    controller: 'BancoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('banco');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Banco', function($stateParams, Banco) {
                    return Banco.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'banco',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('banco-detail.edit', {
            parent: 'banco-detail',
            url: '/detail/edit',
            data: {
                  authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banco/banco-dialog.html',
                    controller: 'BancoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Banco', function(Banco) {
                            return Banco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banco-configuration.new', {
            parent: 'banco-configuration',
            url: '/new',
            data: {
             authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banco/banco-dialog.html',
                    controller: 'BancoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                beneficiario: null,
                                cedula: null,
                                cuentaCorriente: null,
                                cuentaCliente: null,
                                moneda: null,
                                cuentaContable: null,
                                capitalInicial: null,
                                mostrarFactura: null,
                                fechaCapitalInicial: null,
                                saldo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('banco-configuration', null, { reload: 'banco-configuration' });
                }, function() {
                    $state.go('banco-configuration');
                });
            }]
        })
        .state('banco.transferencia', {
            parent: 'banco',
            url: '/new',
            data: {
                      authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transferencia/transferencia-dialog.html',
                    controller: 'TransferenciaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                concepto: null,
                                cuentaOrigen: null,
                                cuentaDestino: null,
                                monto: null,
                                idCompany: null,
                                fecha: null,
                                idBancoDestino: null,
                                idBancoOrigen: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                   $state.go('banco', null, { reload: 'banco' });
                }, function() {
                 $state.go('banco');
                });
            }]
        })
        .state('banco-configuration.edit', {
            parent: 'banco-configuration',
            url: '/{id}/edit',
            data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banco/banco-dialog.html',
                    controller: 'BancoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Banco', function(Banco) {
                            return Banco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banco-configuration', null, { reload: 'banco-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banco.delete', {
            parent: 'banco',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banco/banco-delete-dialog.html',
                    controller: 'BancoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Banco', function(Banco) {
                            return Banco.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banco', null, { reload: 'banco' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
