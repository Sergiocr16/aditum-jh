(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('common-area-administration.common-area-reservations', {

            url: '/common-area-reservations?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER'],
                pageTitle: 'aditumApp.commonAreaReservations.home.title'
            },
            templateUrl: 'app/entities/common-area-reservations/common-area-reservations.html',
            controller: 'CommonAreaReservationsController',
            controllerAs: 'vm',

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
                    $translatePartialLoader.addPart('commonAreaReservations');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            .state('common-area-administration.common-area-all-reservations', {

                url: '/common-area-all-reservations?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-all-reservations.html',
                controller: 'CommonAreaAllReservationsController',
                controllerAs: 'vm',

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
                    }]
                }
            })
            .state('common-area-all-reservations-resident-view', {
                parent: 'entity',
                url: '/common-area-all-reservations-by-resident?page&sort&search',
                data: {
                    authorities: ['ROLE_USER']
                },

                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-all-reservation-resident-view.html',
                        controller: 'CommonAreaAllReservationsResidentViewController',
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                }
            })


        .state('common-area-reservations-detail.edit', {
            parent: 'common-area-reservations-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                    controller: 'CommonAreaReservationsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('common-area-administration.newReservation', {
            url: '/new',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
            controller: 'CommonAreaReservationsDialogController',
            controllerAs: 'vm',
            resolve: {
                entity: function () {
                    return {
                        houseId: null,
                        residentId: null,
                        initalDate: null,
                        finalDate: null,
                        initialTime: null,
                        finalTime: null,
                        comments: null,
                        id: null
                    };
                },
                companyUser: ['MultiCompany', function (MultiCompany) {
                    return MultiCompany.getCurrentUserCompany()
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'common-area-reservations',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })    .state('common-area-administration.common-area-reservations.reservationDetail', {
            parent: 'common-area-administration.common-area-reservations',
            url: '/{id}/detail',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                    controller: 'CommonAreaReservationsDetailController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-administration.common-area-reservations', null, { reload: 'common-area-administration.common-area-reservations' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
            .state('common-area-administration.common-area-all-reservations.reservationDetail', {
                parent: 'common-area-administration.common-area-all-reservations',
                url: '/{id}/detail',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                        controller: 'CommonAreaReservationsDetailController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.common-area-all-reservations.reservationDetail', null, { reload: 'common-area-administration.common-area-all-reservations.reservationDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.common-area-all-reservations.acceptedReservationsDetail', {
                parent: 'common-area-administration.common-area-all-reservations',
                url: '/{id}/accepted-reservation-detail',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/reservation-calendar-accepted-reservations.html',
                        controller: 'ReservationsCalentarAcceptedReservations',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-administration.common-area-all-reservations.acceptedReservationsDetail', null, { reload: 'common-area-administration.common-area-all-reservations.acceptedReservationsDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-all-reservations-resident-view.reservationsDetail', {
                parent: 'common-area-all-reservations-resident-view',
                url: '/{id}/accepted-reservation-detail-resident-view',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/reservation-calendar-accepted-reservations.html',
                        controller: 'ReservationsCalentarAcceptedReservations',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                                return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function() {
                        $state.go('common-area-all-reservations-resident-view.reservationsDetail', null, { reload: 'common-area-all-reservations-resident-view.reservationsDetail' });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('common-area-administration.edit', {
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                controller: 'CommonAreaReservationsDialogController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams,CommonAreaReservations) {
                        return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area-reservations',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-new-reservation-resident-view', {
                parent: 'entity',
                url: '/{id}/new-reservation-resident',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog-resident-view.html',
                        controller: 'CommonAreaReservationsDialogResidentViewController',
                        controllerAs: 'vm',
                    }
                },

                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        if($stateParams!==null){
                            return CommonArea.get({id : $stateParams.id}).$promise;
                        }else{
                            return null;
                        }

                    },
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area-new-reservation-resident-view',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-reservation-resident-view', {
                parent: 'entity',
                url: '/new-reservation-resident',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog-resident-view.html',
                        controller: 'CommonAreaReservationsDialogResidentViewController',
                        controllerAs: 'vm'
                    }
                },

                resolve: {
                    entity: function () {
                        return {
                            houseId: null,
                            residentId: null,
                            initalDate: null,
                            finalDate: null,
                            initialTime: null,
                            finalTime: null,
                            comments: null,
                            id: null,
                            chargeIdId: null
                        };
                    },
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area-resident-account',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
        .state('common-area-reservations.delete', {
            parent: 'common-area-reservations',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/common-area-reservations/common-area-reservations-delete-dialog.html',
                    controller: 'CommonAreaReservationsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CommonAreaReservations', function(CommonAreaReservations) {
                            return CommonAreaReservations.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('common-area-reservations', null, { reload: 'common-area-reservations' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
