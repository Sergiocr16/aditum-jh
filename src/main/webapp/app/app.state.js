(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider','$mdThemingProvider','$mdDateLocaleProvider','$mdAriaProvider'];
    // stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider,$mdThemingProvider,$mdDateLocaleProvider,$mdAriaProvider) {
        $mdDateLocaleProvider.formatDate = function(date) {
            return date ? moment(date).format('DD-MM-YYYY'): '';
        };
        $mdThemingProvider.theme('default')
            .primaryPalette('teal')
            .accentPalette('orange');
        $mdAriaProvider.disableWarnings();

        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm',
                    data: {authorities: ['ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN']}
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
                companyUser: ['MultiCompany', function (MultiCompany) {
                    return MultiCompany.getCurrentUserCompany()
                }],
                globalCompanyId: ['CommonMethods', '$localStorage', 'Auth', '$rootScope', '$state', function (CommonMethods, $localStorage, Auth, $rootScope, $state) {
                    if ($localStorage.companyId != undefined || $localStorage.companyId != null) {
                        return CommonMethods.decryptIdUrl($localStorage.companyId)
                    } else {
                        Auth.logout();
                        $rootScope.companyUser = undefined;
                        $localStorage.companyId = undefined;
                        $state.go('home');
                        $rootScope.menu = false;
                        $rootScope.companyId = undefined;
                        $rootScope.showLogin = true;
                        $rootScope.inicieSesion = false;
                        return null;
                    }
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });


    }
})();
