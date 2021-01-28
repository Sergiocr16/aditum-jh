(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('generatePayment.accounting-note', {
            parent: 'generatePayment',
            url: '/notas-filial/',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER'],
                pageTitle: 'aditumApp.accountingNote.home.title'
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accounting-note/accounting-note-payment.html',
                    controller: 'AccountingNotePaymentController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('generatePayment.newNote', {
                parent: 'generatePayment',
                url: '/notas-filial/',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER'],
                    pageTitle: 'aditumApp.accountingNote.home.title'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/accounting-note/accounting-note-dialog.html',
                        controller: 'AccountingNoteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
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
                        $state.go('generatePayment.accounting-note', null, { reload: 'generatePayment.accounting-note' });
                    }, function() {
                        $state.go('generatePayment.accounting-note');
                    });
                }]
            })
            .state('generatePayment.editNote', {
                parent: 'generatePayment',
                url: '/notas-filial/{id}',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_JD','ROLE_MANAGER'],
                    pageTitle: 'aditumApp.accountingNote.home.title'
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/accounting-note/accounting-note-dialog.html',
                        controller: 'AccountingNoteDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'md',
                        resolve: {
                            entity: ['AccountingNote', function(AccountingNote) {
                                return AccountingNote.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('generatePayment.accounting-note', null, { reload: 'generatePayment.accounting-note' });
                    }, function() {
                        $state.go('generatePayment.accounting-note',null, { reload: false });
                    });
                }]
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
                    $state.go('houseAdministration.notes', null, { reload: 'houseAdministration.notes' });
                }, function() {
                    $state.go('houseAdministration.notes');
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
                    size: 'md',
                    resolve: {
                        entity: ['AccountingNote', function(AccountingNote) {
                            return AccountingNote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('houseAdministration.notes', null, { reload: 'houseAdministration.notes' });
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
