(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('accounting-note', {
            parent: 'entity',
            url: '/accounting-note?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER'],
                pageTitle: 'aditumApp.accountingNote.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accounting-note/accounting-notes.html',
                    controller: 'AccountingNoteController',
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
                    $translatePartialLoader.addPart('accountingNote');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('accounting-note-detail', {
            parent: 'accounting-note',
            url: '/accounting-note/{id}',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER'],
                pageTitle: 'aditumApp.accountingNote.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accounting-note/accounting-note-detail.html',
                    controller: 'AccountingNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountingNote');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccountingNote', function($stateParams, AccountingNote) {
                    return AccountingNote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'accounting-note',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('houseAdministration.notes.detail.edit', {
            parent: 'houseAdministration.notes',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accounting-note/accounting-note-dialog.html',
                    controller: 'AccountingNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountingNote', function(AccountingNote) {
                            return AccountingNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('houseAdministration.notes.new', {
            parent: 'houseAdministration.notes',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accounting-note/accounting-note-dialog.html',
                    controller: 'AccountingNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                color: null,
                                fixed: null,
                                deleted: null,
                                creationDate: null,
                                modificationDate: null,
                                fileName: null,
                                fileUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('accounting-note', null, { reload: 'accounting-note' });
                }, function() {
                    $state.go('accounting-note');
                });
            }]
        })
        .state('houseAdministration.notes.edit', {
            parent: 'houseAdministration.notes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accounting-note/accounting-note-dialog.html',
                    controller: 'AccountingNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountingNote', function(AccountingNote) {
                            return AccountingNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accounting-note', null, { reload: 'accounting-note' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accounting-note.delete', {
            parent: 'accounting-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accounting-note/accounting-note-delete-dialog.html',
                    controller: 'AccountingNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountingNote', function(AccountingNote) {
                            return AccountingNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accounting-note', null, { reload: 'accounting-note' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
