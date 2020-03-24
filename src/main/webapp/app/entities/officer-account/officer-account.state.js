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
              url: '/officerAccounts',
              data: {
                  authorities: ['ROLE_ADMIN','ROLE_RH']
              },
              views: {
                  'content@': {
                         templateUrl: 'app/entities/officer-account/officer-accounts.html',
                         controller: 'OfficerAccountController',
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
                      authorities: ['ROLE_ADMIN','ROLE_RH']
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
            .state('officerAccounts.edit', {
                parent: 'company',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_RH']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/officer-account/officer-account-dialog.html',
                        controller: 'OfficerAccountDialogController',
                        controllerAs: 'vm'
                    }
                },

                resolve: {
                    entity: ['$stateParams','OfficerAccount','CommonMethods', function($stateParams,OfficerAccount,CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return OfficerAccount.get({id : id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'officerAccounts',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })

        .state('officer-account.delete', {
            parent: 'officer-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER','ROLE_RH']
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
