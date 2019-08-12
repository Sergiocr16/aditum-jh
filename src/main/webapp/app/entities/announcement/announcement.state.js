(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('announcements', {
                parent: 'entity',
                url: '/noticias',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_MANAGER_MACRO'],
                    pageTitle: 'aditumApp.announcement.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/announcement/announcements-all.html',
                        controller: 'AnnouncementsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('announcements.announcement', {
                url: '/published',
                data: {
                    authorities: ['ROLE_MANAGER','ROLE_MANAGER_MACRO'],
                    pageTitle: 'aditumApp.announcement.home.title'
                },

                templateUrl: 'app/entities/announcement/announcements.html',
                controller: 'AnnouncementController',
                controllerAs: 'vm'
                ,
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('announcement-user', {
                parent: 'entity',
                url: '/noticias-user',
                data: {
                    authorities: ['ROLE_MANAGER', 'ROLE_JD','ROLE_USER','ROLE_MANAGER_MACRO'],
                    pageTitle: 'aditumApp.announcement.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/announcement/announcement-user.html',
                        controller: 'AnnouncementUserController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    companyUser: ['MultiCompany', function (MultiCompany) {
                        return MultiCompany.getCurrentUserCompany()
                    }]
                }
            })
            .state('announcements.announcement-sketch', {
                url: '/sketches',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.announcement.home.title'
                },

                templateUrl: 'app/entities/announcement/announcement-sketch.html',
                controller: 'AnnouncementSketchController',
                controllerAs: 'vm'
                ,
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('announcement-detail', {
                parent: 'announcements',
                url: '/announcement/{id}',
                data: {
                    authorities: ['ROLE_MANAGER'],
                    pageTitle: 'aditumApp.announcement.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/announcement/announcement-detail.html',
                        controller: 'AnnouncementDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Announcement', function ($stateParams, Announcement) {
                        return Announcement.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'announcement',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('announcement-detail.edit', {
                parent: 'announcement-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/announcement/announcement-dialog.html',
                        controller: 'AnnouncementDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Announcement', function (Announcement) {
                                return Announcement.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('announcements.new', {
                parent: 'announcements',
                url: '/new',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/announcement/announcement-dialog.html',
                        controller: 'AnnouncementDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            title: null,
                            publishingDate: null,
                            description: null,
                            status: null,
                            id: null
                        };
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('announcements.edit', {
                parent: 'announcements',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/announcement/announcement-dialog.html',
                        controller: 'AnnouncementDialogController',
                        controllerAs: 'vm',
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('announcement');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Announcement', function ($stateParams, Announcement) {
                        return Announcement.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'announcement',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            });
    }

})();
