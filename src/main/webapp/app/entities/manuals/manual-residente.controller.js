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
  $rootScope.menu = false;
 $rootScope.isInManual = true;
       });

        var vm = this;
        vm.currentPage = 0;
        vm.pdfUrl = 'https://github.com/Sergiocr16/aditum-jh/blob/AddingManualPage/src/main/webapp/content/fonts/manualusuario.pdf?raw=true';
       vm.images = [
       "http://res.cloudinary.com/aditum/image/upload/v1507930853/Manual/pagina_1.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930860/Manual/Pagina_2.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930863/Manual/Pagina_3.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930867/Manual/Pagina_4.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930870/Manual/Pagina_5.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930874/Manual/Pagina_6.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930879/Manual/Pagina_7.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930888/Manual/Pagina_8.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930891/Manual/Pagina_9.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930896/Manual/Pagina_10.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930901/Manual/Pagina_11.png",
       "http://res.cloudinary.com/aditum/image/upload/v1507930905/Manual/Pagina_12.png"
       ]

       $rootScope.menu = false;
       vm.showPage = vm.images[0]
       setTimeout(function(){
           $("#tableData").fadeIn('slow');

       },700)


    vm.nextPage = function(){
     vm.currentPage =  vm.currentPage + 1;
     vm.showPage = vm.images[ vm.currentPage]
    }
    vm.backPage = function(){
     vm.currentPage =  vm.currentPage - 1;
         vm.showPage = vm.images[ vm.currentPage]
    }
    $rootScope.$on('$stateChangeStart',
    function(event, toState, toParams, fromState, fromParams){
        $rootScope.menu = true;
        $rootScope.isInManual = false;
    })
    vm.goToPage = function(index){
    vm.currentPage = index;
     vm.showPage = vm.images[index]
    }

    }
})();
