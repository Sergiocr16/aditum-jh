(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
       .state('access-door-invited-by-house', {
            parent: 'access-door',
            url: '/{id}/invited',
            data: {
                authorities: ['ROLE_OFFICER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/access-door/access-door-invited-by-house.html',
                    controller: 'AccessDoorInvitedByHouseController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['House','CommonMethods', function(House,CommonMethods) {
                        var id = CommonMethods.decryptIdUrl($stateParams.id)
                            return House.get({id : id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('access-door', null, { reload: false,notify: false });
                }, function() {
                    $state.go('^',null,{notify: false});
                });
            }]
        }).state('main-access-door', {
               parent: 'entity',
               url: '/puertaAcceso',
               data: {
                   authorities: ['ROLE_OFFICER']
               },
               views: {
                   'access_door@': {
                       templateUrl: 'app/entities/access-door/access-door-container.html',
                       controller: 'AccessDoorController',
                       controllerAs: 'vm'
                   }
               },
           })
            .state('main-access-door.register-visitor', {
                url: '/registrarVisitante',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl:'app/entities/access-door/register-visitor.html',
                controller: 'RegisterVisitorController',
                controllerAs: 'vm',
            })
            .state('main-access-door.filiales-info', {
                url: '/infoFiliales',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl:'app/entities/access-door/houses-info-access-door.html',
                controller: 'HousesInfoAccessDoorController',
                controllerAs: 'vm',
            })
            .state('main-access-door.home-service', {
                url: '/homeService',
                data: {
                    authorities: ['ROLE_OFFICER']
                },
                templateUrl:'app/entities/access-door/home-service.html',
                controller: 'HomeServiceDoorController',
                controllerAs: 'vm',
            })
    }

})();
