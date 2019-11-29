(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('emergency', {
            parent: 'entity',
            url: '/emergency?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN','ROLE_MANAGER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/emergency/emergency-index.html',
                    controller: 'EmergencyController',
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
                    $translatePartialLoader.addPart('emergency');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('emergency.new', {
            parent: 'emergency',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
             views: {
                'content@': {
                    templateUrl: 'app/entities/emergency/emergency-dialog.html',
                    controller: 'EmergencyDialogController',
                    controllerAs: 'vm'
                }
            }
        })
        .state('emergency.edit', {
            parent: 'emergency',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/emergency/emergency-dialog.html',
                    controller: 'EmergencyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Emergency', function(Emergency) {
                            return Emergency.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('emergency', null, { reload: 'emergency' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
