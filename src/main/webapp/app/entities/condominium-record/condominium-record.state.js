(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('condominium-record', {
            parent: 'entity',
            url: '/condominium-record',
            data: {
                authorities: ['ROLE_USER','ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.condominiumRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condominium-record/condominium-records.html',
                    controller: 'CondominiumRecordController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('condominiumRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('condominium-record-detail', {
            parent: 'condominium-record',
            url: '/condominium-record/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_MANAGER','ROLE_JD'],
                pageTitle: 'aditumApp.condominiumRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condominium-record/condominium-record-detail.html',
                    controller: 'CondominiumRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('condominiumRecord');
                    return $translate.refresh();
                }],
                entity: ['CondominiumRecord','CommonMethods','$stateParams', function(CondominiumRecord,CommonMethods,$stateParams) {
                    var id = CommonMethods.decryptIdUrl($stateParams.id)
                    return CondominiumRecord.get({id : id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'condominium-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('condominium-record-detail.edit', {
            parent: 'condominium-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condominium-record/condominium-record-dialog.html',
                    controller: 'CondominiumRecordDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['CondominiumRecord','CommonMethods','$stateParams', function(CondominiumRecord,CommonMethods,$stateParams) {
                    var id = CommonMethods.decryptIdUrl($stateParams.id)
                    return CondominiumRecord.get({id : id}).$promise;
                }]
            }
        })
        .state('condominium-record.new', {
            parent: 'condominium-record',
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condominium-record/condominium-record-dialog.html',
                    controller: 'CondominiumRecordDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: null,
                        description: null,
                        fileUrl: null,
                        fileName: null,
                        uploadDate: null,
                        deleted: null,
                        status: null,
                        id: null
                    };
                }
            }
        })
        .state('condominium-record.edit', {
            parent: 'condominium-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condominium-record/condominium-record-dialog.html',
                    controller: 'CondominiumRecordDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['CondominiumRecord','CommonMethods','$stateParams', function(CondominiumRecord,CommonMethods,$stateParams) {
                    var id = CommonMethods.decryptIdUrl($stateParams.id)
                    return CondominiumRecord.get({id : id}).$promise;
                }]
            }
        })
        .state('condominium-record.delete', {
            parent: 'condominium-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/condominium-record/condominium-record-delete-dialog.html',
                    controller: 'CondominiumRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CondominiumRecord', function(CondominiumRecord) {
                            return CondominiumRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('condominium-record', null, { reload: 'condominium-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
