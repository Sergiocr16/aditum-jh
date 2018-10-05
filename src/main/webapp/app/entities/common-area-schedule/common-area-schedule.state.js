(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('common-area-schedule', {
            parent: 'entity',
            url: '/common-area-schedule',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonAreaSchedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedules.html',
                    controller: 'CommonAreaScheduleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commonAreaSchedule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('common-area-schedule-detail', {
            parent: 'common-area-schedule',
            url: '/common-area-schedule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.commonAreaSchedule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedule-detail.html',
                    controller: 'CommonAreaScheduleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commonAreaSchedule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CommonAreaSchedule', function($stateParams, CommonAreaSchedule) {
                    return CommonAreaSchedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'common-area-schedule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('common-area-schedule-detail.edit', {
            parent: 'common-area-schedule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedule-dialog.html',
                    controller: 'CommonAreaScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaSchedule', function(CommonAreaSchedule) {
                            return CommonAreaSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area-schedule.new', {
            parent: 'common-area-schedule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedule-dialog.html',
                    controller: 'CommonAreaScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                lunes: null,
                                martes: null,
                                miercoles: null,
                                jueves: null,
                                viernes: null,
                                sabado: null,
                                domingo: null,
                                commonAreaId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('common-area-schedule', null, { reload: 'common-area-schedule' });
                }, function() {
                    $state.go('common-area-schedule');
                });
            }]
        })
        .state('common-area-schedule.edit', {
            parent: 'common-area-schedule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedule-dialog.html',
                    controller: 'CommonAreaScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaSchedule', function(CommonAreaSchedule) {
                            return CommonAreaSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-schedule', null, { reload: 'common-area-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area-schedule.delete', {
            parent: 'common-area-schedule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-schedule/common-area-schedule-delete-dialog.html',
                    controller: 'CommonAreaScheduleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CommonAreaSchedule', function(CommonAreaSchedule) {
                            return CommonAreaSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-schedule', null, { reload: 'common-area-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
