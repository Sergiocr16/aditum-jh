(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('contract', {
                parent: 'entity',
                url: '/contract',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.contract.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/contract/contracts.html',
                        controller: 'ContractController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('contract-detail', {
                parent: 'contract',
                url: '/contract/{id}',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.contract.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/contract/contract-detail.html',
                        controller: 'ContractDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('contract');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Contract', 'CommonMethods', function ($stateParams, Contract, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Contract.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'contract',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('contract-detail.edit', {
                parent: 'contract-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['Contract', '$stateParams', 'CommonMethods', function (Contract, $stateParams, CommonMethods) {

                        return Contract.get({id: $stateParams.id}).$promise;
                    }]
                }
            })
            .state('contract.new', {
                parent: 'contract',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.contract.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            id: null,
                            fileUrl: "a"
                        };
                    },
                }
            })
            .state('contract.edit', {
                parent: 'contract',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/contract/contract-dialog.html',
                        controller: 'ContractDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['Contract', 'CommonMethods', '$stateParams', function (Contract, CommonMethods, $stateParams) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Contract.get({id: id}).$promise;
                    }]
                }
            })
            .state('contract.delete', {
                parent: 'contract',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/contract/contract-delete-dialog.html',
                        controller: 'ContractDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Contract', function (Contract) {
                                return Contract.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('contract', null, {reload: 'contract'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
