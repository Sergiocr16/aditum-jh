(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('insertCode', {
            parent: 'account',
            url: '/registrarse',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/entities/loginCode/insert-logincode.html',
                    controller: 'InsertLoginCodeController',
                    controllerAs: 'vm'
                },

            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }]
            },
           onEnter: ['$rootScope', function($rootScope) {
              $rootScope.showLogin= false;

           }],
           onExit: ['$rootScope', function($rootScope) {
             $rootScope.showLogin= true;
          }]
        })
     .state('createProfile', {
            parent: 'entity',
            url: '/crearPerfil/{loginCode}',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/entities/loginCode/createProfile.html',
                    controller: 'CreateProfileController',
                    controllerAs: 'vm'
                },
            },
            resolve: {

                entity: function () {
                    return {
                        name: null,
                        lastname: null,
                        secondlastname: null,
                        identificationnumber: null,
                        phonenumber: null,
                        image: null,
                        imageContentType: null,
                        email: null,
                        isOwner: null,
                        enabled: null,
                        id: null

                    };
                },
                companyUser: ['MultiCompany',function(MultiCompany){
                    return MultiCompany.getCurrentUserCompany()
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insertCode',
                        params: $state.params,

                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })

            .state('loginCodeprofile', {
                parent: 'createProfile',
                url: '/perfil',
                views: {
                    'loginCodeTabs@createProfile': {
                        templateUrl: 'app/entities/loginCode/profile.html',
                        controller: 'LoginCodeProfileController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('loginCodeCars', {
                parent: 'createProfile',
                url: '/informacion-vehiculos',
                views: {
                    'loginCodeTabs@createProfile': {
                        templateUrl: 'app/entities/loginCode/carInformation.html',
                        controller: 'LoginCodeCarsRegisterController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('loginCodeResidents', {
                parent: 'createProfile',
                url: '/informacion-residentes',
                views: {
                    'loginCodeTabs@createProfile': {
                        templateUrl: 'app/entities/loginCode/residentsInformation.html',
                        controller: 'LoginCodeResidentsController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('loginCodeResume', {
                parent: 'createProfile',
                url: '/informacion-resume',
                views: {
                    'loginCodeTabs@createProfile': {
                        templateUrl: 'app/entities/loginCode/loginCodeResume.html',
                        controller: 'LoginCodeResumeController',
                        controllerAs: 'vm'
                    }
                }
            })
            .state('loginCodeWelcome', {
                parent: 'createProfile',
                url: '/bienvenida',
                views: {
                    'loginCodeTabs@createProfile': {
                        templateUrl: 'app/entities/loginCode/loginCodeWelcome.html',
                        controller: 'CreateProfileController',
                        controllerAs: 'vm'
                    }
                }
            })

    }
})();
