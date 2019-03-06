(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-proof', {
            parent: 'entity',
            url: '/payment-proof?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.paymentProof.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-proof/payment-proofs.html',
                    controller: 'PaymentProofController',
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
                    $translatePartialLoader.addPart('paymentProof');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payment-proof-detail', {
            parent: 'payment-proof',
            url: '/payment-proof/{id}',
            data: {
                authorities: ['ROLE_USER'],
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
                entity: ['$stateParams', 'PaymentProof', function($stateParams, PaymentProof) {
                    return PaymentProof.get({id : $stateParams.id}).$promise;
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
        .state('payment-proof-detail.edit', {
            parent: 'payment-proof-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-proof/payment-proof-dialog.html',
                    controller: 'PaymentProofDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentProof', function(PaymentProof) {
                            return PaymentProof.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-proof.new', {
            parent: 'payment-proof',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-proof/payment-proof-dialog.html',
                    controller: 'PaymentProofDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                imageUrl: null,
                                status: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-proof', null, { reload: 'payment-proof' });
                }, function() {
                    $state.go('payment-proof');
                });
            }]
        })
        .state('payment-proof.edit', {
            parent: 'payment-proof',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-proof/payment-proof-dialog.html',
                    controller: 'PaymentProofDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentProof', function(PaymentProof) {
                            return PaymentProof.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-proof', null, { reload: 'payment-proof' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-proof.delete', {
            parent: 'payment-proof',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-proof/payment-proof-delete-dialog.html',
                    controller: 'PaymentProofDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentProof', function(PaymentProof) {
                            return PaymentProof.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-proof', null, { reload: 'payment-proof' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
