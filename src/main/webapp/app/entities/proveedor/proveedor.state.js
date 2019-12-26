(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('proveedor', {
                parent: 'entity',
                url: '/proveedor?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                    pageTitle: 'aditumApp.proveedor.home.title'
                },
                views: {
                    'content@': {
                       templateUrl: 'app/entities/proveedor/proveedors.html',
                        //  templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'ProveedorController',
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
                        $translatePartialLoader.addPart('proveedor');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('proveedor.new', {
                parent: 'proveedor',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/proveedor/proveedor-dialog.html',
                        //templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'ProveedorDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    empresa: null,
                                    responsable: null,
                                    telefono: null,
                                    email: null,
                                    comentarios: null,
                                    deleted: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('proveedor', null, {reload: 'proveedor'});
                    }, function () {
                        $state.go('proveedor');
                    });
                }]
            })
            .state('proveedor.edit', {
                parent: 'proveedor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                       templateUrl: 'app/entities/proveedor/proveedor-dialog.html',
                        // templateUrl: 'app/entities/company/commingSoonFinanzes.html',

                        controller: 'ProveedorDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Proveedor', function (Proveedor) {
                                return Proveedor.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('proveedor', null, {reload: 'proveedor'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
