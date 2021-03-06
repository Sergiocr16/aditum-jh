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
    }

})();
