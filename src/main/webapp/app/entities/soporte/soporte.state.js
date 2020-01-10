(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('soporte', {
            parent: 'entity',
            url: '/soporte',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.soporte.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/soporte/soportes.html',
                    controller: 'SoporteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('soporte');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            .state('soporte-user', {
                parent: 'entity',
                url: '/soporte-user',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER','ROLE_JD','ROLE_RH'],
                    pageTitle: 'Aditum'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/soporte/soporte-user.html',
                        controller: 'SoporteUserController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('soporte');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        .state('soporte-detail', {
            parent: 'soporte',
            url: '/soporte/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.soporte.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/soporte/soporte-detail.html',
                    controller: 'SoporteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('soporte');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Soporte', function($stateParams, Soporte) {
                    return Soporte.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'soporte',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('soporte-detail.edit', {
            parent: 'soporte-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/soporte/soporte-dialog.html',
                    controller: 'SoporteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Soporte', function(Soporte) {
                            return Soporte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('soporte.new', {
            parent: 'soporte',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/soporte/soporte-dialog.html',
                    controller: 'SoporteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                fullName: null,
                                email: null,
                                subject: null,
                                username: null,
                                creationDate: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('soporte', null, { reload: 'soporte' });
                }, function() {
                    $state.go('soporte');
                });
            }]
        })
        .state('soporte.edit', {
            parent: 'soporte',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/soporte/soporte-dialog.html',
                    controller: 'SoporteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Soporte', function(Soporte) {
                            return Soporte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('soporte', null, { reload: 'soporte' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('soporte.delete', {
            parent: 'soporte',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/soporte/soporte-delete-dialog.html',
                    controller: 'SoporteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Soporte', function(Soporte) {
                            return Soporte.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('soporte', null, { reload: 'soporte' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
