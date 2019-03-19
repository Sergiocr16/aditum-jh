(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider

            .state('paymentProof', {
                parent: 'entity',
                url: '/comprobanteDePagos',
                data: {
                    authorities: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_USER'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/payment-proof/payment-proof-administration.html',
                        controller: 'PaymentProofAdministrationController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('paymentProof.pending', {
                url: '/sinrevisar',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/payment-proof/payment-proofs-pending.html',
                controller: 'PaymentProofPendingController',
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
                        $translatePartialLoader.addPart('paymentProof');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('paymentProof.checked', {
                url: '/revisados',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_MANAGER'],
                },
                templateUrl: 'app/entities/payment-proof/payment-proofs-pending.html',
                controller: 'PaymentProofCheckedController',
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
                        $translatePartialLoader.addPart('paymentProof');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('paymentProof.pending-user', {
                url: '/sin-revisar-por-filial',
                data: {
                    authorities: ['ROLE_USER'],
                },
                templateUrl: 'app/entities/payment-proof/payment-proofs-pending.html',
                controller: 'PaymentProofPendingUserController',
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
                        $translatePartialLoader.addPart('paymentProof');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('paymentProof.checked-user', {
                url: '/revisadas-por-filial',
                data: {
                    authorities: ['ROLE_USER'],
                },
                templateUrl: 'app/entities/payment-proof/payment-proofs-pending.html',
                controller: 'PaymentProofCheckedUserController',
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
                        $translatePartialLoader.addPart('paymentProof');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }

            })
            .state('payment-proof-detail', {
                parent: 'entity',
                url: '/payment-proof/{id}',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_USER'],
                    pageTitle: 'aditumApp.paymentProof.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/payment-proof/payment-proof-detail.html',
                        controller: 'PaymentProofDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paymentProof');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PaymentProof', function ($stateParams, PaymentProof) {
                        return PaymentProof.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'payment-proof',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })

            .state('paymentProof.new', {
                parent: 'entity',
                url: '/comprobanteDePagos/nuevo',
                data: {
                    authorities: ['ROLE_ADMIN', 'ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/payment-proof/payment-proof-dialog.html',
                        controller: 'PaymentProofDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            imageUrl: null,
                            status: null,
                            description: null,
                            subject: null,
                            id: null
                        };
                    }
                },
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-proof',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            })


    }

})();
