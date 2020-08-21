(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('admin-notes', {
                url: '/admin-notes',
                parent: 'entity',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/note/notes.html',
                        controller: 'NoteController',
                        controllerAs: 'vm',
                    }
                },
            })
            .state('admin-notes.new', {
                parent: 'admin-notes',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/note/note-dialog-admin.html',
                        controller: 'NoteDialogAdmin',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: function () {
                                return {};
                            }
                        }
                    }).result.then(function () {
                        $state.go('admin-notes', null, {reload: 'admin-notes'});
                    }, function () {
                        $state.go('admin-notes');
                    });
                }]
            })
            .state('admin-notes.edit', {
                parent: 'admin-notes',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/note/note-dialog-admin.html',
                        controller: 'NoteDialogAdmin',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: ['Note', function (Note) {
                                return Note.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('access-door.notes', null, {reload: 'access-door.notes'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('noteNew', {
                parent: 'entity',
                url: '/home-service/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/note/note-dialog.html',
                        controller: 'NoteDialogController',
                        controllerAs: 'vm'
                    }
                },
            });
    }

})();
