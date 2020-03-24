(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('complaint-comment', {
            parent: 'entity',
            url: '/complaint-comment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.complaintComment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/complaint-comment/complaint-comments.html',
                    controller: 'ComplaintCommentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('complaintComment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('complaint-comment-detail', {
            parent: 'complaint-comment',
            url: '/complaint-comment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.complaintComment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/complaint-comment/complaint-comment-detail.html',
                    controller: 'ComplaintCommentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('complaintComment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ComplaintComment', function($stateParams, ComplaintComment) {
                    return ComplaintComment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'complaint-comment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('complaint-comment-detail.edit', {
            parent: 'complaint-comment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint-comment/complaint-comment-dialog.html',
                    controller: 'ComplaintCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComplaintComment', function(ComplaintComment) {
                            return ComplaintComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('complaint-comment.new', {
            parent: 'complaint-comment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint-comment/complaint-comment-dialog.html',
                    controller: 'ComplaintCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                creationDate: null,
                                editedDate: null,
                                deleted: null,
                                fileUrl: null,
                                fileName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('complaint-comment', null, { reload: 'complaint-comment' });
                }, function() {
                    $state.go('complaint-comment');
                });
            }]
        })
        .state('complaint-comment.edit', {
            parent: 'complaint-comment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint-comment/complaint-comment-dialog.html',
                    controller: 'ComplaintCommentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ComplaintComment', function(ComplaintComment) {
                            return ComplaintComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('complaint-comment', null, { reload: 'complaint-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('complaint-comment.delete', {
            parent: 'complaint-comment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint-comment/complaint-comment-delete-dialog.html',
                    controller: 'ComplaintCommentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ComplaintComment', function(ComplaintComment) {
                            return ComplaintComment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('complaint-comment', null, { reload: 'complaint-comment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
