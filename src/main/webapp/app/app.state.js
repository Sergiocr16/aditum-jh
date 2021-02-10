(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider','$mdThemingProvider','$mdDateLocaleProvider','$mdAriaProvider'];

    function stateConfig($stateProvider,$mdThemingProvider,$mdDateLocaleProvider,$mdAriaProvider) {
        $mdDateLocaleProvider.formatDate = function(date) {
            return date ? moment(date).format('DD-MM-YYYY'): '';
        };
        $mdThemingProvider.theme('default')
            .primaryPalette('grey')
            .accentPalette('indigo');
        $mdAriaProvider.disableWarnings();

        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm',
                    data: {authorities: ['ROLE_USER','ROLE_OWNER','ROLE_MANAGER', 'ROLE_ADMIN']}
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
                'footerMenu@': {
                    templateUrl: 'app/layouts/navbar/footer-menu.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'login@': {
                    templateUrl: 'app/components/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });


    }
})();
