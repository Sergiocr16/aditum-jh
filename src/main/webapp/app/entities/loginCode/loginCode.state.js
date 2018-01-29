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
                }
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
     .state('insertCode.createProfile', {
            parent: 'insertCode',
            url: '/crearPerfil/{loginCode}',
            data: {
                authorities: []
            },
            views: {
                'reset@': {
                    templateUrl: 'app/entities/loginCode/createProfile.html',
                    controller: 'CreateProfileController',
                    controllerAs: 'vm'
                }
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
                previousState: ["$state", function ($state,$stateParams) {
                    var currentStateData = {
                        name: $state.current.name || 'insertCode',
                        params: $state.params,

                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
    }
})();
