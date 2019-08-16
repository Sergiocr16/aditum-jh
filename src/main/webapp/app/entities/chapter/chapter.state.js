(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chapter', {
            parent: 'entity',
            url: '/chapter?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.chapter.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chapter/chapters.html',
                    controller: 'ChapterController',
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
                    $translatePartialLoader.addPart('chapter');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('chapter-detail', {
            parent: 'chapter',
            url: '/chapter/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.chapter.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chapter/chapter-detail.html',
                    controller: 'ChapterDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chapter');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Chapter', function($stateParams, Chapter) {
                    return Chapter.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chapter',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chapter-detail.edit', {
            parent: 'chapter-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chapter/chapter-dialog.html',
                    controller: 'ChapterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chapter', function(Chapter) {
                            return Chapter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chapter.new', {
            parent: 'chapter',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chapter/chapter-dialog.html',
                    controller: 'ChapterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                order: null,
                                isVisible: null,
                                deleted: null,
                                notes: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chapter', null, { reload: 'chapter' });
                }, function() {
                    $state.go('chapter');
                });
            }]
        })
        .state('chapter.edit', {
            parent: 'chapter',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chapter/chapter-dialog.html',
                    controller: 'ChapterDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chapter', function(Chapter) {
                            return Chapter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chapter', null, { reload: 'chapter' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chapter.delete', {
            parent: 'chapter',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chapter/chapter-delete-dialog.html',
                    controller: 'ChapterDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Chapter', function(Chapter) {
                            return Chapter.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chapter', null, { reload: 'chapter' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
