(function() {
    'use strict';

    angular
        .module('aditumApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'cloudinary',
             'pdf',
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler','$state','$rootScope'];

    function run(stateHandler, translationHandler,$state,vm) {
        stateHandler.initialize();
        translationHandler.initialize();
        toastr.options = {
                          "closeButton": false,
                          "debug": false,
                          "newestOnTop": false,
                          "progressBar": false,
                          "positionClass": "toast-top-right",
                          "preventDuplicates": false,
                          "onclick": null,
                          "showDuration": "300",
                          "hideDuration": "1000",
                          "timeOut": "5000",
                          "extendedTimeOut": "1000",
                          "showEasing": "swing",
                          "hideEasing": "linear",
                          "showMethod": "fadeIn",
                          "hideMethod": "fadeOut"
                        }
        vm.isInLogin = $state.includes('home');
        vm.isInManual = $state.includes('manualResidente');
       $('body').removeClass("gray");
       vm.menu = true;
       // Initialize Firebase
         var config = {
           apiKey: "AIzaSyC72bE53igXS39tKAZXUsWAsLimMiKnEEs",
           authDomain: "padron-electoral-lh.firebaseapp.com",
           databaseURL: "https://padron-electoral-lh.firebaseio.com",
           projectId: "padron-electoral-lh",
           storageBucket: "padron-electoral-lh.appspot.com",
           messagingSenderId: "720753236578"
         };
         firebase.initializeApp(config);
    }
})();
