(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('water-consumption', {
                parent: 'entity',
                url: '/water-consumption',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.waterConsumption.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/water-consumption/water-consumptions.html',
                        controller: 'WaterConsumptionController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('waterConsumption');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('water-consumption.createCharge', {
                parent: 'water-consumption',
                url: '/create-charge/{id}',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/water-consumption/create-water-consumption-charge.html',
                        controller: 'CreateWaterConsumptionChargeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['WaterConsumption', 'CommonMethods', function (WaterConsumption, CommonMethods) {
                                var decryptedId = CommonMethods.decryptIdUrl($stateParams.id);
                                return WaterConsumption.get({id: decryptedId}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: true});
                    }, function () {
                        $state.go('^', {}, {reload: true});
                    });
                }]
            })
            .state('water-consumption-detail', {
                parent: 'water-consumption',
                url: '/water-consumption/{id}',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.waterConsumption.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/water-consumption/water-consumption-detail.html',
                        controller: 'WaterConsumptionDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('waterConsumption');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'WaterConsumption', function ($stateParams, WaterConsumption) {
                        return WaterConsumption.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'water-consumption',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('water-consumption-detail.edit', {
                parent: 'water-consumption-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/water-consumption/water-consumption-dialog.html',
                        controller: 'WaterConsumptionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['WaterConsumption', function (WaterConsumption) {
                                return WaterConsumption.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('water-consumption.new', {
                parent: 'water-consumption',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/water-consumption/water-consumption-dialog.html',
                        controller: 'WaterConsumptionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    consumption: null,
                                    month: null,
                                    recordDate: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('water-consumption', null, {reload: 'water-consumption'});
                    }, function () {
                        $state.go('water-consumption');
                    });
                }]
            })
            .state('water-consumption.edit', {
                parent: 'water-consumption',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/water-consumption/water-consumption-dialog.html',
                        controller: 'WaterConsumptionDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['WaterConsumption', function (WaterConsumption) {
                                return WaterConsumption.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('water-consumption', null, {reload: 'water-consumption'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('water-consumption.delete', {
                parent: 'water-consumption',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/water-consumption/water-consumption-delete-dialog.html',
                        controller: 'WaterConsumptionDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['WaterConsumption', function (WaterConsumption) {
                                return WaterConsumption.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('water-consumption', null, {reload: 'water-consumption'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
