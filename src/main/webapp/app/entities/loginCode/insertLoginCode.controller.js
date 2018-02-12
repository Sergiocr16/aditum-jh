(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InsertLoginCodeController', InsertLoginCodeController);

    InsertLoginCodeController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','$localStorage'];

    function InsertLoginCodeController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,$localStorage) {
        var vm = this;
        vm.loginCodeNotFound = 0;
        $("#code_login_input").attr("placeholder", "Ingrese el código");

        angular.element(document).ready(function () {

             $("#insertCode").fadeIn(700);
             setTimeout(function() {
                 $(".aditumLogoLoginCode").fadeIn(1000);
             },800)
             setTimeout(function() {
                 $("#containerLoginCode").fadeIn(1300);
             },1700)

                 $('body').removeClass("gray");
                 $rootScope.showLogin = false;
                 $rootScope.menu = false;
                 $rootScope.isInManual = true;


         });


        vm.changeStatus = function () {
            vm.loginCodeNotFound = 0;
            if(vm.loginCode == "" ){
                $("#code_login_input").css("text-transform", "none");
                $("#code_login_input").attr("placeholder", "Ingrese el código");
            } else {
                $("#code_login_input").css("text-transform", "uppercase");
            }

        }
        vm.validateLoginCode = function(){
            House.getByLoginCode({loginCode:vm.loginCode}).$promise.then(onSuccess, onError);
        }
         function onSuccess(data) {
          $state.go('loginCodeWelcome',{loginCode:vm.loginCode});
        }

        vm.back = function(){
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
        }
          function onError() {
              vm.loginCodeNotFound = 1;
        }

    }
})();
