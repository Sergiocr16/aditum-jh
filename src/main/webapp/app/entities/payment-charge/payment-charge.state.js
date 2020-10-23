(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-charge', {
            parent: 'entity',
            url: '/payment-charge',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.paymentCharge.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-charge/payment-charges.html',
                    controller: 'PaymentChargeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentCharge');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payment-charge-detail', {
            parent: 'payment-charge',
            url: '/payment-charge/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'aditumApp.paymentCharge.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-charge/payment-charge-detail.html',
                    controller: 'PaymentChargeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentCharge');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaymentCharge', function($stateParams, PaymentCharge) {
                    return PaymentCharge.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-charge',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-charge-detail.edit', {
            parent: 'payment-charge-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-charge/payment-charge-dialog.html',
                    controller: 'PaymentChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentCharge', function(PaymentCharge) {
                            return PaymentCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-charge.new', {
            parent: 'payment-charge',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-charge/payment-charge-dialog.html',
                    controller: 'PaymentChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type: null,
                                date: null,
                                concept: null,
                                consecutive: null,
                                originalCharge: null,
                                ammount: null,
                                leftToPay: null,
                                abonado: null,
                                oldStyle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-charge', null, { reload: 'payment-charge' });
                }, function() {
                    $state.go('payment-charge');
                });
            }]
        })
        .state('payment-charge.edit', {
            parent: 'payment-charge',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-charge/payment-charge-dialog.html',
                    controller: 'PaymentChargeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentCharge', function(PaymentCharge) {
                            return PaymentCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-charge', null, { reload: 'payment-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-charge.delete', {
            parent: 'payment-charge',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-charge/payment-charge-delete-dialog.html',
                    controller: 'PaymentChargeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentCharge', function(PaymentCharge) {
                            return PaymentCharge.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-charge', null, { reload: 'payment-charge' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
