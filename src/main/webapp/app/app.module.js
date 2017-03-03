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
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler','$state','$rootScope'];

    function run(stateHandler, translationHandler,$state,vm) {
        stateHandler.initialize();
        translationHandler.initialize();
        vm.isInLogin = $state.includes('home');
        console.log($state.includes('home'));
    }
})();
