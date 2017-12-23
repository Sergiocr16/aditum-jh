(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InsertLoginCodeController', InsertLoginCodeController);

    InsertLoginCodeController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','pdfDelegate'];

    function InsertLoginCodeController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,pdfDelegate) {
         angular.element(document).ready(function () {
                 $('body').removeClass("gray");
                 $rootScope.showLogin = false;
                 $rootScope.menu = false;
                 $rootScope.isInManual = true;
         });

        var vm = this;

        vm.validateLoginCode = function(){
        console.log(vm.loginCode)
            House.getByLoginCode({loginCode:vm.loginCode}).$promise.then(onSuccess, onError);
        }
         function onSuccess(data) {
            console.log(data);

        }
          function onError() {
              console.log('no sirvio');
        }

    }
})();
