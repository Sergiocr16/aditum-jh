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

           vm.pdfUrl = 'https://github.com/Sergiocr16/aditum-jh/blob/AddingManualPage/src/main/webapp/content/fonts/manualusuario.pdf?raw=true';

       var oReq = new XMLHttpRequest();
       oReq.open("GET", vm.pdfUrl, true);
       oReq.responseType = "blob";

       oReq.onload = function(oEvent) {
         var blob = oReq.response;
         console.log(blobl)
         // ...
       };

//       oReq.send();
//    $.ajax({
//      url:  vm.pdfUrl,
//      dataType:'blob'
//    },function(file){
//    console.log(file)
//        vm.loadNewFile = function(url) {
//          pdfDelegate
//            .$getByHandle('my-pdf-container')
//            .setFile(file);
//        };
//    })
//
//url = $window.URL || $window.webkitURL;
//url.createObjectURL(blob);
//console.log()
    vm.goToPage = function(){
//    pdfDelegate.$getByHandle('my-pdf-container').zoomOut();
    }

    }
})();
