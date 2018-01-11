(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InsertLoginCodeController', InsertLoginCodeController);

    InsertLoginCodeController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House'];

    function InsertLoginCodeController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House) {
         angular.element(document).ready(function () {
                 $('body').removeClass("gray");
                 $rootScope.showLogin = false;
                 $rootScope.menu = false;
                 $rootScope.isInManual = true;
         });

        var vm = this;

        vm.loginCodeNotFound = 0;

        vm.validateLoginCode = function(){
            House.getByLoginCode({loginCode:vm.loginCode}).$promise.then(onSuccess, onError);
        }
         function onSuccess(data) {
             $state.go('insertCode.createProfile',{loginCode:vm.loginCode});


        }
          function onError() {
              vm.loginCodeNotFound = 1;
        }

    }
})();
