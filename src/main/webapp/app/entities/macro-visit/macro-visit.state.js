(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('macro-visit', {
            parent: 'entity',
            url: '/macro-visit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.macroVisit.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-visit/macro-visits.html',
                    controller: 'MacroVisitController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroVisit');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('macro-visit-detail', {
            parent: 'macro-visit',
            url: '/macro-visit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.macroVisit.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/macro-visit/macro-visit-detail.html',
                    controller: 'MacroVisitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('macroVisit');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MacroVisit', function($stateParams, MacroVisit) {
                    return MacroVisit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'macro-visit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('macro-visit-detail.edit', {
            parent: 'macro-visit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-visit/macro-visit-dialog.html',
                    controller: 'MacroVisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroVisit', function(MacroVisit) {
                            return MacroVisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-visit.new', {
            parent: 'macro-visit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-visit/macro-visit-dialog.html',
                    controller: 'MacroVisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                identificationnumber: null,
                                lastname: null,
                                secondlastname: null,
                                licenseplate: null,
                                status: null,
                                arrivaltime: null,
                                departuretime: null,
                                destiny: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('macro-visit', null, { reload: 'macro-visit' });
                }, function() {
                    $state.go('macro-visit');
                });
            }]
        })
        .state('macro-visit.edit', {
            parent: 'macro-visit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-visit/macro-visit-dialog.html',
                    controller: 'MacroVisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MacroVisit', function(MacroVisit) {
                            return MacroVisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-visit', null, { reload: 'macro-visit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('macro-visit.delete', {
            parent: 'macro-visit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/macro-visit/macro-visit-delete-dialog.html',
                    controller: 'MacroVisitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MacroVisit', function(MacroVisit) {
                            return MacroVisit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('macro-visit', null, { reload: 'macro-visit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
