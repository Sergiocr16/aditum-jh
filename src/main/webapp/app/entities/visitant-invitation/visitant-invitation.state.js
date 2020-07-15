(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('load-automatic-visitor', {
                parent: 'entity',
                url: '/load-automatic-visitor',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant-invitation/visitant-massive-invitations.html',
                        controller: 'LoadAutomaticVisitorController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('visitant-invitation', {
                parent: 'entity',
                url: '/visitant-invitation?page&sort&search',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.visitantInvitation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitations.html',
                        controller: 'VisitantInvitationController',
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
                        $translatePartialLoader.addPart('visitantInvitation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('visitant-invitation-detail', {
                parent: 'visitant-invitation',
                url: '/visitant-invitation/{id}',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER'],
                    pageTitle: 'aditumApp.visitantInvitation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitation-detail.html',
                        controller: 'VisitantInvitationDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitantInvitation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'VisitantInvitation', function ($stateParams, VisitantInvitation) {
                        return VisitantInvitation.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'visitant-invitation',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('visitant-invitation-detail.edit', {
                parent: 'visitant-invitation-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitation-dialog.html',
                        controller: 'VisitantInvitationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VisitantInvitation', function (VisitantInvitation) {
                                return VisitantInvitation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('visitant-invitation.new', {
                parent: 'visitant-invitation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitation-dialog.html',
                        controller: 'VisitantInvitationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    lastname: null,
                                    secondlastname: null,
                                    identificationnumber: null,
                                    invitationstartingtime: null,
                                    invitationlimittime: null,
                                    licenseplate: null,
                                    hasschedule: null,
                                    destiny: null,
                                    houseId: null,
                                    companyId: null,
                                    adminId: null,
                                    scheduleId: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('visitant-invitation', null, {reload: 'visitant-invitation'});
                    }, function () {
                        $state.go('visitant-invitation');
                    });
                }]
            })
            .state('visitant-invitation.edit', {
                parent: 'visitant-invitation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitation-dialog.html',
                        controller: 'VisitantInvitationDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VisitantInvitation', function (VisitantInvitation) {
                                return VisitantInvitation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('visitant-invitation', null, {reload: 'visitant-invitation'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('visitant-invitation.delete', {
                parent: 'visitant-invitation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-invitation-delete-dialog.html',
                        controller: 'VisitantInvitationDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['VisitantInvitation', function (VisitantInvitation) {
                                return VisitantInvitation.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('visitant-invitation', null, {reload: 'visitant-invitation'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('visitant-invited-user.new', {
                parent: 'visitant-invited-user',
                url: 'new',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant-invitation/visitant-dialog.html',
                        controller: 'VisitantDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('visitant');
                        return $translate.refresh();
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'visitant',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }

            })

            .state('visitant-invited-user.editSchedule', {
                parent: 'visitant-invited-user',
                url: 'renew/schedule/:id',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', 'CommonMethods', function ($stateParams, $state, $uibModal, CommonMethods) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-dialog-renew.html',
                        controller: 'VisitantRenewWithScheduleController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VisitantInvitation', function (VisitantInvitation) {
                                var visitorId = CommonMethods.decryptIdUrl($stateParams.id)
                                return VisitantInvitation.get({
                                    id: visitorId
                                }).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {
                            reload: true
                        });
                    }, function () {
                        $state.go('^', {}, {
                            reload: false
                        });
                    });
                }]
            })
            .state('visitant-invited-user.edit', {
                parent: 'visitant-invited-user',
                url: 'renew/:id',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', 'CommonMethods', function ($stateParams, $state, $uibModal, CommonMethods) {
                    $uibModal.open({
                        templateUrl: 'app/entities/visitant-invitation/visitant-dialog-renew.html',
                        controller: 'VisitantDialogRenewController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['VisitantInvitation', function (VisitantInvitation) {
                                var visitorId = CommonMethods.decryptIdUrl($stateParams.id)
                                return VisitantInvitation.get({
                                    id: visitorId
                                }).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {
                            reload: true
                        });
                    }, function () {
                        $state.go('^', {}, {
                            reload: false
                        });
                    });
                }]
            })
            .state('visitant-invited-user', {
                parent: 'entity',
                url: '/visitant/invited/user/?page&sort&search',
                data: {
                    authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER']

                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/visitant-invitation/visitants-user-invited.html',
                        controller: 'VisitantInvitedUserController',
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
                        $translatePartialLoader.addPart('visitant');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })  .state('visitant-invited-admin-view', {
            parent: 'entity',
            url: '/visitant/invited/adminview/?page&sort&search',
            data: {
                authorities: ['ROLE_MANAGER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/visitant-invitation/visitant-invitation-admin-view.html',
                    controller: 'VisitantInvitedAdminViewController',
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
                    $translatePartialLoader.addPart('visitant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });

    }

})();
