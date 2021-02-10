(function () {
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
                    authorities: ['ROLE_MANAGER', 'ROLE_JD'],
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
                    authorities: ['ROLE_MANAGER', 'ROLE_JD']
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
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-all-reservation-resident-view.html',
                        //    templateUrl: 'app/entities/company/commingSoon.html',
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
                }
            })


            .state('common-area-reservations-detail.edit', {
                parent: 'common-area-reservations-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                        //    templateUrl: 'app/entities/company/commingSoon.html',
                        controller: 'CommonAreaReservationsDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['CommonAreaReservations', function (CommonAreaReservations) {
                                return CommonAreaReservations.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
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
                //    templateUrl: 'app/entities/company/commingSoon.html',
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
            .state('common-area-administration.newCommonAreaReservation', {
                url: '/{date}/{commonAreaId}/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                //  templateUrl: 'app/entities/company/commingSoon.html',
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
            .state('common-area-administration.newCommonAreaReservationDate', {
                url: '/{date}/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                //    templateUrl: 'app/entities/company/commingSoon.html',
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
            .state('common-area-administration.reservationDetail', {
                url: '/{id}/detail',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_JD', 'ROLE_ADMIN']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-reservations-detail.html',
                // templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'CommonAreaReservationsDetailController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams, CommonAreaReservations) {
                        return CommonAreaReservations.get({id: $stateParams.id}).$promise;
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

            .state('common-area-administration.acceptedReservationsDetail', {
                url: '/{id}/accepted-reservation-detail',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_USER', 'ROLE_OWNER', 'ROLE_JD', 'ROLE_ADMIN']
                },
                templateUrl: 'app/entities/common-area-reservations/reservation-calendar-accepted-reservations.html',
                //   templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'ReservationsCalentarAcceptedReservations',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams, CommonAreaReservations) {
                        return CommonAreaReservations.get({id: $stateParams.id}).$promise;
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


            .state('common-area-administration.edit', {
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog.html',
                //    templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'CommonAreaReservationsDialogController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams, CommonAreaReservations) {
                        return CommonAreaReservations.get({id: $stateParams.id}).$promise;
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
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog-resident-view.html',
                        //   templateUrl: 'app/entities/company/commingSoon.html',
                        controller: 'CommonAreaReservationsDialogResidentViewController',
                        controllerAs: 'vm',
                    }
                },

                resolve: {
                    entity: function ($stateParams, CommonArea) {
                        if ($stateParams !== null) {
                            return CommonArea.get({id: $stateParams.id}).$promise;
                        } else {
                            return null;
                        }

                    },
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

            .state('common-area-new-reservation-resident-view-date', {
                parent: 'entity',
                url: '/{id}/new-reservation-resident/{date}/',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog-resident-view.html',
                        //    templateUrl: 'app/entities/company/commingSoon.html',
                        controller: 'CommonAreaReservationsDialogResidentViewController',
                        controllerAs: 'vm',
                    }
                },

                resolve: {
                    entity: function ($stateParams, CommonArea) {
                        if ($stateParams !== null) {
                            return CommonArea.get({id: $stateParams.id}).$promise;
                        } else {
                            return null;
                        }

                    },
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
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservations-dialog-resident-view.html',
                        //    templateUrl: 'app/entities/company/commingSoon.html',
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
            .state('common-area-devolution-administration', {
                parent: 'entity',
                url: '/common-area-devolution-administration',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-devolution-administration.html',
                        //  templateUrl: 'app/entities/company/commingSoon.html',
                        controller: 'CommonAreaDevolutionAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('common-area-devolution-administration.pending-devolution', {
                url: '/pending-devolution',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                templateUrl: 'app/entities/common-area-reservations/reservation-devolution-charge.html',
                //   templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'ReservationDevolutionChargeController',
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
            .state('common-area-devolution-administration.done-devolution', {
                url: '/done-devolution',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.commonAreaReservations.home.title'
                },
                templateUrl: 'app/entities/common-area-reservations/reservation-devolution-done.html',
                //   templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'ReservationDevolutionDoneController',
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
            .state('common-area-devolution-administration.reservationDevolutionDialog', {
                url: '/{id}/reservation-devolution-dialog',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                templateUrl: 'app/entities/common-area-reservations/reservation-devolution-dialog.html',
                //     templateUrl: 'app/entities/company/commingSoon.html',
                controller: 'ReservationDevolutionDialogReservations',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams, CommonAreaReservations) {
                        return CommonAreaReservations.get({id: $stateParams.id}).$promise;
                    },
                    egress: function () {
                        return {
                            date: null,
                            folio: null,
                            account: null,
                            category: null,
                            paymentMethod: null,
                            concept: null,
                            total: null,
                            reference: null,
                            comments: null,
                            proveedor: null,
                            paymentDate: null,
                            expirationDate: null,
                            id: null
                        };
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
            .state('common-area-reservations-detail-resident-view', {
                parent: 'entity',
                url: '/{id}/reservation-detail-resident-view',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/common-area-reservations/common-area-reservation-detail-resident-view.html',
                        //  templateUrl: 'app/entities/company/commingSoon.html',
                        controller: 'ReservationsCalentarAcceptedReservations',
                        controllerAs: 'vm',
                    }
                },

                resolve: {
                    entity: function ($stateParams, CommonAreaReservations) {
                        return CommonAreaReservations.get({id: $stateParams.id}).$promise;
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


    }

})();
