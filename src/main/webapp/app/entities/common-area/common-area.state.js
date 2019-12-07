(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('common-area-administration', {
                parent: 'entity',
                url: '/common-area-administration',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_JD'],
                },
                views: {
                    'content@': {
                        // templateUrl: 'app/entities/common-area/common-area-administration.html',
                        templateUrl: 'app/entities/company/commingSoon.html',

                        controller: 'CommonAreaAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('common-area-resident-account', {
                parent: 'entity',
                url: '/common-area-resident-account?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'aditumApp.commonArea.home.title'
                },
                views: {
                    'content@': {
                        // templateUrl: 'app/entities/common-area/common-area-resident-account.html',
                        templateUrl: 'app/entities/company/commingSoon.html',

                        controller: 'CommonAreaController',
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
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commonArea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('common-area-administration.common-area', {
                parent: 'common-area-administration',
                url: '/common-area?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD'],
                    pageTitle: 'aditumApp.commonArea.home.title'
                },

                // templateUrl: 'app/entities/common-area/common-areas.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'CommonAreaController',
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commonArea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reservation-calendar-resident-view', {
                parent: 'entity',
                url: '/{id}/reservation-calendar-resident',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        // templateUrl: 'app/entities/common-area/reservation-calendar-resident-view.html',
                        templateUrl: 'app/entities/company/commingSoon.html',

                        controller: 'ReservationCalendarResidentViewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        return CommonArea.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'reservation-calendar-resident-view',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area', {
                url: '/common-area?page&sort&search',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.commonArea.home.title'
                },
                // templateUrl: 'app/entities/common-area/common-areas.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'CommonAreaController',
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
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commonArea');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })

            .state('common-area-administration.new', {
                url: '/newcommonArea',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER']
                },

                // templateUrl: 'app/entities/common-area/common-area-dialog.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'CommonAreaDialogController',
                controllerAs: 'vm',

                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            description: null,
                            reservationCharge: null,
                            devolutionAmmount: null,
                            chargeRequired: null,
                            reservationWithDebt: null,
                            picture: null,
                            pictureContentType: null,
                            maximunHours: null,
                            id: null
                        };
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('common-area-administration.editCommonArea', {
                url: '/{id}/editCommonArea',
                data: {
                    authorities: ['ROLE_MANAGER']
                },

                // templateUrl: 'app/entities/common-area/common-area-dialog.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'CommonAreaDialogController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        return CommonArea.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('common-area-administration.reservation-calendar', {
                url: '/{id}/reservation-calendar',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD']
                },
                // templateUrl:'app/entities/common-area/reservation-calendar.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'ReservationCalendarController',
                controllerAs: 'vm',
                resolve: {
                    entity: function ($stateParams,CommonArea) {
                        return CommonArea.get({id : $stateParams.id}).$promise;
                    },
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'common-area',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('common-area-administration.general-reservation-calendar', {
                url: '/general-reservation-calendar',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_JD']
                },
                // templateUrl:'app/entities/common-area/general-reservation-calendar.html',
                templateUrl: 'app/entities/company/commingSoon.html',

                controller: 'GeneralReservationCalendarController',
                controllerAs: 'vm',
            });
    }

})();
