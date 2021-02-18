(function () {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$mdThemingProvider', '$mdDateLocaleProvider', '$mdAriaProvider'];

    function stateConfig($stateProvider, $mdThemingProvider, $mdDateLocaleProvider, $mdAriaProvider) {
        $mdDateLocaleProvider.formatDate = function (date) {
            return date ? moment(date).format('DD-MM-YYYY') : '';
        };
        $mdThemingProvider.definePalette('aditum', {
            '50': 'f9f7f3',
            '100': 'efeae2',
            '200': 'e5ddcf',
            '300': 'dacfbc',
            '400': 'd2c4ad',
            '500': 'caba9f',
            '600': 'c5b397',
            '700': 'bdab8d',
            '800': 'b7a383',
            '900': '001e2f',
            'A100': 'ffffff',
            'A200': 'fffbf6',
            'A400': 'ffe6c3',
            'A700': 'ffdca9',
            'contrastDefaultColor': 'light',
            'contrastDarkColors': [
                '50',
                '100',
                '200',
                '300',
                '400',
                '500',
                '600',
                '700',
                '800',
                '900',
                'A100',
                'A200',
                'A400',
                'A700'
            ],
            'contrastLightColors': []
        })
        $mdThemingProvider.definePalette('aditumaccent', {
            '50': 'e0e4e6',
            '100': 'b3bcc1',
            '200': '808f97',
            '300': '4d626d',
            '400': '26404e',
            '500': '001e2f',
            '600': '001a2a',
            '700': '001623',
            '800': '00121d',
            '900': '000a12',
            'A100': 'caba9f',
            'A200': 'caba9f',
            'A400': 'caba9f',
            'A700': 'caba9f',
            'contrastDefaultColor': 'light',
            'contrastDarkColors': [
                '50',
                '100',
                '200',
                'A100'
            ],
            'contrastLightColors': [
                '300',
                '400',
                '500',
                '600',
                '700',
                '800',
                '900',
                'A200',
                'A400',
                'A700'
            ]
        });

        $mdThemingProvider.theme('default')
            .primaryPalette('aditum').accentPalette('aditumaccent')

        $mdAriaProvider.disableWarnings();
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm',
                    data: {authorities: ['ROLE_USER', 'ROLE_OWNER', 'ROLE_MANAGER', 'ROLE_ADMIN']}
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
