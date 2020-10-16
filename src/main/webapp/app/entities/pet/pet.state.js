(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('pet', {
                parent: 'entity',
                url: '/pet',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER'],
                    pageTitle: 'aditumApp.pet.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pet/pets.html',
                        controller: 'PetController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pet');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pet-detail', {
                parent: 'pet',
                url: '/pet/{id}',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER'],
                    pageTitle: 'aditumApp.pet.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pet/pet-detail.html',
                        controller: 'PetDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pet');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pet', function ($stateParams, Pet) {
                        return Pet.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'pet',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('pet-detail.edit', {
                parent: 'pet-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pet/pet-dialog.html',
                        controller: 'PetDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['Pet', '$stateParams', function (Pet, $stateParams) {
                        return Pet.get({id: $stateParams.id}).$promise;
                    }]
                },
            })
            .state('pet.new', {
                parent: 'pet',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pet/pet-dialog.html',
                        controller: 'PetDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: null,
                            deleted: null,
                            raze: null,
                            type: null,
                            description: null,
                            contact: null,
                            imageUrl: null,
                            id: null
                        };
                    }
                }
            })
            .state('pet.edit', {
                parent: 'pet',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/pet/pet-dialog.html',
                        controller: 'PetDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['Pet', '$stateParams','CommonMethods', function (Pet, $stateParams,CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                        return Pet.get({id:id}).$promise;
                    }]
                },
            })
            .state('pet.delete', {
                parent: 'pet',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/pet/pet-delete-dialog.html',
                        controller: 'PetDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Pet', function (Pet) {
                                return Pet.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('pet', null, {reload: 'pet'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
