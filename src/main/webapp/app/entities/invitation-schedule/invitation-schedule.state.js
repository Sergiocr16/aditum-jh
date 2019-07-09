(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invitation-schedule', {
            parent: 'entity',
            url: '/invitation-schedule?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.invitationSchedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedules.html',
                    controller: 'InvitationScheduleController',
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
                    $translatePartialLoader.addPart('invitationSchedule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('invitation-schedule-detail', {
            parent: 'invitation-schedule',
            url: '/invitation-schedule/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.invitationSchedule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedule-detail.html',
                    controller: 'InvitationScheduleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invitationSchedule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'InvitationSchedule', function($stateParams, InvitationSchedule) {
                    return InvitationSchedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'invitation-schedule',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('invitation-schedule-detail.edit', {
            parent: 'invitation-schedule-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedule-dialog.html',
                    controller: 'InvitationScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvitationSchedule', function(InvitationSchedule) {
                            return InvitationSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })

        .state('invitation-schedule.new', {
            parent: 'invitation-schedule',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedule-dialog.html',
                    controller: 'InvitationScheduleDialogController',
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
                                visitantInvitationId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invitation-schedule', null, { reload: 'invitation-schedule' });
                }, function() {
                    $state.go('invitation-schedule');
                });
            }]
        })
        .state('invitation-schedule.edit', {
            parent: 'invitation-schedule',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedule-dialog.html',
                    controller: 'InvitationScheduleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['InvitationSchedule', function(InvitationSchedule) {
                            return InvitationSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invitation-schedule', null, { reload: 'invitation-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invitation-schedule.delete', {
            parent: 'invitation-schedule',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invitation-schedule/invitation-schedule-delete-dialog.html',
                    controller: 'InvitationScheduleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['InvitationSchedule', function(InvitationSchedule) {
                            return InvitationSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invitation-schedule', null, { reload: 'invitation-schedule' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
