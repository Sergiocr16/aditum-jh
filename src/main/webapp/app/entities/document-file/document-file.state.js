(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('document-file', {
            parent: 'entity',
            url: '/document-file?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.documentFile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-file/document-files.html',
                    controller: 'DocumentFileController',
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
                    $translatePartialLoader.addPart('documentFile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('document-file-detail', {
            parent: 'document-file',
            url: '/document-file/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.documentFile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-file/document-file-detail.html',
                    controller: 'DocumentFileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documentFile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DocumentFile', function($stateParams, DocumentFile) {
                    return DocumentFile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-file',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('document-file-detail.edit', {
            parent: 'document-file-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-file/document-file-dialog.html',
                    controller: 'DocumentFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumentFile', function(DocumentFile) {
                            return DocumentFile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-file.new', {
            parent: 'document-file',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-file/document-file-dialog.html',
                    controller: 'DocumentFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                name: null,
                                reference: null,
                                status: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('document-file', null, { reload: 'document-file' });
                }, function() {
                    $state.go('document-file');
                });
            }]
        })
        .state('document-file.edit', {
            parent: 'document-file',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-file/document-file-dialog.html',
                    controller: 'DocumentFileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DocumentFile', function(DocumentFile) {
                            return DocumentFile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-file', null, { reload: 'document-file' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('document-file.delete', {
            parent: 'document-file',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-file/document-file-delete-dialog.html',
                    controller: 'DocumentFileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DocumentFile', function(DocumentFile) {
                            return DocumentFile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('document-file', null, { reload: 'document-file' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
