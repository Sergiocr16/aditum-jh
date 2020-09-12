(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('mensual-billing-file', {
                parent: 'entity',
                url: '/mensual-billing-file',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD','ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.mensualBillingFile.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-files.html',
                        controller: 'MensualBillingFileController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mensualBillingFile');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mensual-billing-file-detail', {
                parent: 'mensual-billing-file',
                url: '/detail',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD','ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.mensualBillingFile.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-file-detail.html',
                        controller: 'MensualBillingFileDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mensualBillingFile');
                        return $translate.refresh();
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'mensual-billing-file',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('mensual-billing-file-detail.edit', {
                parent: 'mensual-billing-file-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-file-dialog.html',
                        controller: 'MensualBillingFileDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    entity: ['MensualBillingFile', 'CommonMethods', '$stateParams', function (MensualBillingFile, CommonMethods, $stateParams) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return MensualBillingFile.get({id: id}).$promise;
                    }]
                }
            })
            .state('mensual-billing-file.new', {
                parent: 'mensual-billing-file',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-file-dialog.html',
                        controller: 'MensualBillingFileDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            url: null,
                            month: null,
                            year: null,
                            status: null,
                            description: null,
                            deleted: null,
                            id: null
                        };
                    }
                }
            })
            .state('mensual-billing-file.edit', {
                parent: 'mensual-billing-file',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-file-dialog.html',
                        controller: 'MensualBillingFileDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    entity: ['MensualBillingFile', 'CommonMethods', '$stateParams', function (MensualBillingFile, CommonMethods, $stateParams) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return MensualBillingFile.get({id: id}).$promise;
                    }]
                }
            })
            .state('mensual-billing-file.delete', {
                parent: 'mensual-billing-file',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/mensual-billing-file/mensual-billing-file-delete-dialog.html',
                        controller: 'MensualBillingFileDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['MensualBillingFile', function (MensualBillingFile) {
                                return MensualBillingFile.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('mensual-billing-file', null, {reload: 'mensual-billing-file'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
