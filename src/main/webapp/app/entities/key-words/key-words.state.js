(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('key-words', {
            parent: 'entity',
            url: '/key-words?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.keyWords.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/key-words/key-words.html',
                    controller: 'KeyWordsController',
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
                    $translatePartialLoader.addPart('keyWords');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('key-words-detail', {
            parent: 'key-words',
            url: '/key-words/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'aditumApp.keyWords.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/key-words/key-words-detail.html',
                    controller: 'KeyWordsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('keyWords');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'KeyWords', function($stateParams, KeyWords) {
                    return KeyWords.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'key-words',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('key-words-detail.edit', {
            parent: 'key-words-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-words/key-words-dialog.html',
                    controller: 'KeyWordsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeyWords', function(KeyWords) {
                            return KeyWords.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('key-words.new', {
            parent: 'key-words',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-words/key-words-dialog.html',
                    controller: 'KeyWordsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('key-words', null, { reload: 'key-words' });
                }, function() {
                    $state.go('key-words');
                });
            }]
        })
        .state('key-words.edit', {
            parent: 'key-words',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-words/key-words-dialog.html',
                    controller: 'KeyWordsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['KeyWords', function(KeyWords) {
                            return KeyWords.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('key-words', null, { reload: 'key-words' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('key-words.delete', {
            parent: 'key-words',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/key-words/key-words-delete-dialog.html',
                    controller: 'KeyWordsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['KeyWords', function(KeyWords) {
                            return KeyWords.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('key-words', null, { reload: 'key-words' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
