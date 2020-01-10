(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('complaint', {
                parent: 'entity',
                url: '/complaint',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD'],
                    pageTitle: 'aditumApp.complaint.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/complaint/complaints.html',
                        controller: 'ComplaintController',
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
            .state('complaint-user', {
                parent: 'entity',
                url: '/complaint-user',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.complaint.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/complaint/complaints-user.html',
                        controller: 'ComplaintUserController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('complaint-detail', {
                parent: 'complaint',
                url: '/{id}',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_USER','ROLE_OWNER','ROLE_JD'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/complaint/complaint-detail.html',
                        controller: 'ComplaintDetailController',
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
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
            .state('complaint-detail.edit', {
                parent: 'complaint-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/complaint/complaint-dialog.html',
                        controller: 'ComplaintDialogController',
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
            .state('complaint.new', {
                parent: 'complaint',
                url: '/nueva',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/complaint/complaint-dialog.html',
                        controller: 'ComplaintDialogController',
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
            .state('complaint-user.new', {
                parent: 'complaint-user',
                url: '/nueva',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.complaint.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/complaint/complaint-user-dialog.html',
                        controller: 'ComplaintUserDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('complaint');
                        return $translate.refresh();
                    }],
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
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
            .state('complaint.edit', {
                parent: 'complaint',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/complaint/complaint-dialog.html',
                        controller: 'ComplaintDialogController',
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
