(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm',
                       data: {authorities: ['ROLE_USER','ROLE_MANAGER','ROLE_ADMIN']}
                },
                'menu@': {
                    templateUrl: 'app/layouts/navbar/menu.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'footer@': {
                    templateUrl: 'app/layouts/navbar/footer.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                 'login@': {
                    templateUrl: 'app/components/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                },
                 'access_door@': {
                  templateUrl: 'app/entities/access-door/main-access-door.html',
                  controller: 'AccessDoorController',
                  controllerAs: 'vm',
                       data: {authorities: ['ROLE_OFFICER']}

                  }


            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                companyUser: ['MultiCompany',function(MultiCompany){
                                     return MultiCompany.getCurrentUserCompany()
                 }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });


    }
})();
