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
               url: '/main-access-door',
               data: {
                   authorities: ['ROLE_OFFICER']
               },
               views: {
                   'access_door@': {
                       templateUrl: 'app/entities/access-door/main-access-door.html',
                       controller: 'AccessDoorController',
                       controllerAs: 'vm'
                   }
               },
           })
    }

})();
