(function () {
    'use strict';
    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('access-door', {
                parent: 'entity',
                url: '/security',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                views: {
                    'access_door@': {
                        templateUrl: 'app/entities/access-door/access-door-container.html',
                        controller: 'AccessDoorContainerController',
                        controllerAs: 'vm'
                    }
                },
            })
            .state('access-door.access', {
                url: '/access',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/access-door.html',
                controller: 'AccessDoorController',
                controllerAs: 'vm',
            })
            .state('access-door.register-visitor', {
                url: '/register-visitor',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/register-visitor.html',
                controller: 'RegisterVisitorController',
                controllerAs: 'vm',
            })
            .state('access-door.houses', {
                url: '/houses-info',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/houses-info-access-door.html',
                controller: 'HousesInfoAccessDoorController',
                controllerAs: 'vm',
            })
            .state('access-door.notes', {
                url: '/notes',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/access-door-notes.html',
                controller: 'AccessDoorNotesController',
                controllerAs: 'vm',
            })
            // .state('access-door.visitant-admin', {
            //     url: '/visitants',
            //     data: {
            //         authorities: ['ROLE_OFFICER']
            //     },
            //     templateUrl: 'app/entities/visitant/visitants-admin.html',
            //     controller: 'VisitantAdminController',
            //     controllerAs: 'vm'
            // })
            .state('access-door.visitant-admin', {
                url: '/visitant/manage/?page&sort&search',
                data: {
                    authorities: ['ROLE_OFFICER']

                },
                templateUrl: 'app/entities/access-door/access-door-bitacora.html',
                controller: 'VisitantBitacoraAccessDoorController',
                controllerAs: 'vm',
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
                    pagingParams: ['$stateParams', 'PaginationUtil', function($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('access-door.notes.new', {
                parent: 'access-door.notes',
                url: '/new',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/access-door/access-note-dialog.html',
                        controller: 'AccessNoteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {

                                };
                            }
                        }

                    }).result.then(function() {
                        $state.go('access-door.notes', null, { reload: 'access-door.notes' });
                    }, function() {
                        $state.go('access-door.notes');
                    });
                }]
            })
            .state('access-door.notes.edit', {
                parent: 'access-door.notes',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/access-door/access-note-dialog.html',
                        controller: 'AccessNoteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: ['Note', function(Note) {
                                return Note.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('access-door.notes', null, { reload: 'access-door.notes' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('access-door.common-area-all-reservations', {
                url: '/common-area-all-reservations?page&sort&search',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl: 'app/entities/access-door/access-door-reservations.html',
                controller: 'CommonAreaAccessDoorAllReservationsController',
                controllerAs: 'vm',
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
                    }]
                }
            })
    }

})();
