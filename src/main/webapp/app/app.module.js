(function () {
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
            'ui.router',
            'infinite-scroll',
            'cloudinary',
            'firebase',
            'angularjs-dropdown-multiselect',
            'summernote',
            'ngMaterial',
            'ngMessages',
            'ngSanitize',
            'mdColorPicker',
            'ui.calendar',
            'googlechart',
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', '$state', '$rootScope','$templateCache','$http'];

    function run(stateHandler, translationHandler, $state, vm,$templateCache ,$http) {
        preloadTemplates($state, $templateCache, $http);
        function preloadTemplates($state, $templateCache, $http) {
            angular.forEach($state.get(), function (state, key) {
                if (state.templateUrl !== undefined && state.preload !== false) {
                    $http.get(state.templateUrl, {cache: $templateCache});
                }
            });
        }
        stateHandler.initialize();
        translationHandler.initialize();
        vm.isInLogin = $state.includes('home');
        vm.isInManual = $state.includes('manualResidente');
        vm.loadingDash = false;
        vm.backgroundSelectCompany = false;
        vm.selectedAlready = false;
        vm.$state = $state;
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

        vm.navigated = false;
        vm.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
            if (from.name) {
                vm.navigated = true;
            }
        });


        vm.showContent = false;
        vm.isShowingLoadingBar = false;
        vm.inForm = false;
        vm.inDetail = false;
        vm.secondBtnForm = true;
        vm.setInvalidForm = function(i){
            vm.isInvalidForm = i;
        };
        vm.back = function () {
            window.history.back();
        };
        vm.formAction = function () {
            setTimeout(function(){
                vm.action();
            },30)
        }
        vm.formAction2 = function () {
            setTimeout(function(){
                vm.action2();
            },30)
        }
    }
})();
