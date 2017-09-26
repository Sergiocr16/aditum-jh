(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
             .state('officerAccounts', {
              parent: 'company',
              url: '/{companyId}/officerAccounts',
              data: {
                  authorities: ['ROLE_ADMIN']
              },
              views: {
                  'content@': {
                         templateUrl: 'app/entities/officer-account/officer-accounts-by-company.html',
                         controller: 'OfficerAccountsByCompanyController',
                         controllerAs: 'vm',
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
                      $translatePartialLoader.addPart('company');
                      $translatePartialLoader.addPart('global');
                      return $translate.refresh();
                  }]
              }
          })
        .state('officer-account-detail', {
            parent: 'officer-account',
            url: '/officer-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.officerAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/officer-account/officer-account-detail.html',
                    controller: 'OfficerAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('officerAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'OfficerAccount', function($stateParams, OfficerAccount) {
                    return OfficerAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'officer-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('officer-account-detail.edit', {
            parent: 'officer-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                    controller: 'OfficerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfficerAccount', function(OfficerAccount) {
                            return OfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

           .state('company.newOfficerAccount', {
                 parent: 'entity',
                 url: '/new',
                 data: {
                       authorities: ['ROLE_USER','ROLE_ADMIN'],
                 },
                 onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                     $uibModal.open({
                         templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                         controller: 'OfficerAccountDialogController',
                         controllerAs: 'vm',
                         backdrop: 'static',
                         size: 'lg',
                         resolve: {
                             entity: function () {
                                 return {
                                     name: null,
                                     id: null
                                 };
                             }
                         }
                     }).result.then(function() {
                         $state.go('officerAccounts', null, { reload: 'officerAccounts' });
                     }, function() {
                         $state.go('officerAccounts');
                     });
                 }]
             })
        .state('officer-account.edit', {
            parent: 'officer-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                    controller: 'OfficerAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfficerAccount', function(OfficerAccount) {
                            return OfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('officer-account', null, { reload: 'officer-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('officer-account.delete', {
            parent: 'officer-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer-account/officer-account-delete-dialog.html',
                    controller: 'OfficerAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['OfficerAccount', function(OfficerAccount) {
                            return OfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('officer-account', null, { reload: 'officer-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
