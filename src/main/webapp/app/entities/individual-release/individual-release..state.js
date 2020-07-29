(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('individual-release', {
                parent: 'entity',
                url: '/individual-release',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD'],
                    pageTitle: 'aditumApp.complaint.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/individual-release/individual-release.html',
                        controller: 'IndividualReleaseController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('individual-release-user', {
                parent: 'entity',
                url: '/individual-release-user',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.complaint.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/individual-release/individual-release-user.html',
                        controller: 'IndividualReleaseUserController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('individual-release-detail', {
                parent: 'individual-release',
                url: '/detail/{id}',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_USER','ROLE_OWNER','ROLE_JD'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/individual-release/individual-release-detail.html',
                        controller: 'IndividualReleaseDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Complaint', 'CommonMethods', function ($stateParams, Complaint, CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Complaint.get({id: id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'complaint',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('individual-release-detail.edit', {
                parent: 'individual-release-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/individual-release/individual-release-dialog.html',
                        controller: 'IndividualReleaseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Complaint', function (Complaint) {
                                return Complaint.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('individual-release-new', {
                parent: 'entity',
                url: '/individual-release/new',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/individual-release/individual-release-dialog.html',
                        controller: 'IndividualReleaseDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                        };
                    },
                }
            })
            .state('individual-release-user.new', {
                parent: 'individual-release-user',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/individual-release/individual-release-user-dialog.html',
                        controller: 'IndividualReleaseUserDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        return $translate.refresh();
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'complaint',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('individual-release.edit', {
                parent: 'individual-release',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/individual-release/individual-release-dialog.html',
                        controller: 'IndividualReleaseDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Complaint', function (Complaint) {
                                return Complaint.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('complaint', null, {reload: 'complaint'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
