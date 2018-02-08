(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteController', SoporteController);

    SoporteController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House'];

    function SoporteController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House) {

        var vm = this;
        $rootScope.active= "soporte";
        vm.isAuthenticated = Principal.isAuthenticated;
       angular.element(document).ready(function(){
           setTimeout(function() {
                                        $("#loadingIcon").fadeOut(300);
                              }, 400)
                               setTimeout(function() {
                                   $("#all").fadeIn('slow');
                               },900 )
       })
    }
})();
