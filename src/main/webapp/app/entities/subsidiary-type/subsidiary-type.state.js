(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subsidiary-type', {
            parent: 'entity',
            url: '/subsidiary-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.subsidiaryType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-types.html',
                    controller: 'SubsidiaryTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiaryType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('subsidiary-type-detail', {
            parent: 'subsidiary-type',
            url: '/subsidiary-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.subsidiaryType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-type-detail.html',
                    controller: 'SubsidiaryTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('subsidiaryType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SubsidiaryType', function($stateParams, SubsidiaryType) {
                    return SubsidiaryType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'subsidiary-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('subsidiary-type-detail.edit', {
            parent: 'subsidiary-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-type-dialog.html',
                    controller: 'SubsidiaryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubsidiaryType', function(SubsidiaryType) {
                            return SubsidiaryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary-type.new', {
            parent: 'subsidiary-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-type-dialog.html',
                    controller: 'SubsidiaryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                size: null,
                                jointOwnershipPercentage: null,
                                ammount: null,
                                limit: null,
                                subsidiaryType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subsidiary-type', null, { reload: 'subsidiary-type' });
                }, function() {
                    $state.go('subsidiary-type');
                });
            }]
        })
        .state('subsidiary-type.edit', {
            parent: 'subsidiary-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-type-dialog.html',
                    controller: 'SubsidiaryTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubsidiaryType', function(SubsidiaryType) {
                            return SubsidiaryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary-type', null, { reload: 'subsidiary-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subsidiary-type.delete', {
            parent: 'subsidiary-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subsidiary-type/subsidiary-type-delete-dialog.html',
                    controller: 'SubsidiaryTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubsidiaryType', function(SubsidiaryType) {
                            return SubsidiaryType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('subsidiary-type', null, { reload: 'subsidiary-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
