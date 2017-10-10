(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ManualResidenteController', ManualResidenteController);

    ManualResidenteController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','pdfDelegate'];

    function ManualResidenteController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,pdfDelegate) {
 angular.element(document).ready(function () {
                          $('body').removeClass("gray");
         $rootScope.showLogin = false;
       });
    var vm = this;
    vm.pdfUrl = 'content/manuals/manualusuario.pdf';
    vm.loadNewFile = function(url) {
      pdfDelegate
        .$getByHandle('my-pdf-container')
        .load(url);
    };
    vm.goToPage = function(){
    pdfDelegate.$getByHandle('my-pdf-container').goToPage(vm.pageNumber);
    }

    }
})();
