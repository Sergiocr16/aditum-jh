(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('officer-ar', {
                parent: 'entity',
                url: '/officer-ar?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER_AR'],
                    pageTitle: 'aditumApp.officerAR.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ars.html',
                        controller: 'OfficerARController',
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
                        $translatePartialLoader.addPart('officerAR');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('officer-ar-detail', {
                parent: 'officer-ar',
                url: '/officer-ar/{id}',
                data: {
                    authorities: ['ROLE_MANAGER_AR'],
                    pageTitle: 'aditumApp.officerAR.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-detail.html',
                        controller: 'OfficerARDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('officerAR');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'OfficerAR', function ($stateParams, OfficerAR) {
                        return OfficerAR.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'officer-ar',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('officer-ar-detail.edit', {
                parent: 'officer-ar-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-dialog.html',
                        controller: 'OfficerARDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['OfficerAR', function (OfficerAR) {
                                return OfficerAR.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('officer-ar.new', {
                parent: 'officer-ar',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-dialog.html',
                        controller: 'OfficerARDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            lastName: null,
                            secondLastName: null,
                            identificationNumber: null,
                            inService: null,
                            imageUrl: null,
                            annosExperiencia: null,
                            birthDate: null,
                            phoneNumber: null,
                            direction: null,
                            plateNumber: null,
                            aditionalNotes: null,
                            enabled: null,
                            deleted: null,
                            email: null,
                            id: null
                        };
                    }
                }
            })
            .state('officer-ar.edit', {
                parent: 'officer-ar',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-dialog.html',
                        controller: 'OfficerARDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['OfficerAR', function (OfficerAR) {
                                return OfficerAR.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('officer-ar', null, {reload: 'officer-ar'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('officer-ar.delete', {
                parent: 'officer-ar',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_MANAGER_AR']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/AditumAR/officer-ar/officer-ar-delete-dialog.html',
                        controller: 'OfficerARDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['OfficerAR', function (OfficerAR) {
                                return OfficerAR.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('officer-ar', null, {reload: 'officer-ar'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
