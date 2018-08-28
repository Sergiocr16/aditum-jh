(function() {
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
                authorities: ['ROLE_USER'],
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
        .state('complaint-detail', {
            parent: 'complaint',
            url: '/complaint/{id}',
            data: {
                authorities: ['ROLE_USER'],
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
                entity: ['$stateParams', 'Complaint', function($stateParams, Complaint) {
                    return Complaint.get({id : $stateParams.id}).$promise;
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
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint/complaint-dialog.html',
                    controller: 'ComplaintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Complaint', function(Complaint) {
                            return Complaint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('complaint.new', {
            parent: 'complaint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint/complaint-dialog.html',
                    controller: 'ComplaintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                complaintType: null,
                                status: null,
                                deleted: null,
                                creationDate: null,
                                resolutionDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('complaint', null, { reload: 'complaint' });
                }, function() {
                    $state.go('complaint');
                });
            }]
        })
        .state('complaint.edit', {
            parent: 'complaint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint/complaint-dialog.html',
                    controller: 'ComplaintDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Complaint', function(Complaint) {
                            return Complaint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('complaint', null, { reload: 'complaint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('complaint.delete', {
            parent: 'complaint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/complaint/complaint-delete-dialog.html',
                    controller: 'ComplaintDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Complaint', function(Complaint) {
                            return Complaint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('complaint', null, { reload: 'complaint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
