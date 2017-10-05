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
       .state('officerAccounts.new', {
          parent: 'officerAccounts',
                    url: '/new',
                    data: {
                      authorities: ['ROLE_ADMIN']
                    },
                    views: {
                        'content@': {
                            templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                            controller: 'OfficerAccountDialogController',
                            controllerAs: 'vm'
                        }
                    },
                    resolve: {

                     entity: function () {
                           return {
                               name: null,
                               id: null
                           };
                       },
                        previousState: ['$stateParams', '$state', function($stateParams, $state) {
                            var currentStateData = {
                                name: $state.current.name || 'officerAccounts',
                                params: $state.params,
                                url: $state.href($state.current.name, $state.params)
                            };
                            return currentStateData;
                        }]
                    }
        })
//        .state('officerAccounts.edit', {
//         parent: 'entity',
//            url: '/{id}/detalle',
//            data: {
//             authorities: ['ROLE_ADMIN'],
//                pageTitle: 'aditumApp.officerAccount.detail.title'
//            },
//            views: {
//                'content@': {
//                        templateUrl: 'app/entities/resident/resident-form.html',
//                                         controller: 'ResidentDialogController',
//                    controllerAs: 'vm'
//                }
//            },
//            resolve: {
//                 entity: ['OfficerAccount', function(OfficerAccount) {
//                     return OfficerAccount.get({id : $stateParams.id}).$promise;
//                 }],
//                previousState: ["$state", function ($state) {
//                    var currentStateData = {
//                        name: $state.current.name || 'officerAccounts',
//                        params: $state.params,
//                        url: $state.href($state.current.name, $state.params)
//                    };
//                    return currentStateData;
//                }]
//            }
//
//  })

        .state('officerAccounts.edit', {
            parent: 'officerAccounts',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                    controller: 'OfficerAccountEditController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['OfficerAccount', function(OfficerAccount) {
                            return OfficerAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('officerAccounts', null, { reload: 'officerAccounts' });
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
