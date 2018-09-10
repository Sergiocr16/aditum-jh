
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
            'firebase',
            'ngNotify',
            'angularjs-dropdown-multiselect',
            'angular-popover',
            'ngPopover',
            'ui.calendar',
            'summernote'

        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', '$state', '$rootScope', '$window'];

    function run(stateHandler, translationHandler, $state, vm, $window) {

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
        $('body').addClass("white-bg");

        function detectswipe(el, func) {
            var swipe_det = new Object();
            swipe_det.sX = 0;
            swipe_det.sY = 0;
            swipe_det.eX = 0;
            swipe_det.eY = 0;
          var min_x = 200; //min x swipe for horizontal swipe
          var max_x = 40; //max x difference for vertical swipe
          var min_y = 40; //min y swipe for vertical swipe
          var max_y = 600; //max y difference for horizontal swipe
            var direc = "";
            var ele = document.getElementById(el);
            ele.addEventListener('touchstart', function(e) {
                var t = e.touches[0];
                swipe_det.sX = t.screenX;
                swipe_det.sY = t.screenY;
            }, false);
            ele.addEventListener('touchmove', function(e) {
                e.preventDefault();
                var t = e.touches[0];
                swipe_det.eX = t.screenX;
                swipe_det.eY = t.screenY;
            }, false);
            ele.addEventListener('touchend', function(e) {
                //horizontal detection
                if ((((swipe_det.eX - min_x > swipe_det.sX) || (swipe_det.eX + min_x < swipe_det.sX)) && ((swipe_det.eY < swipe_det.sY + max_y) && (swipe_det.sY > swipe_det.eY - max_y)))) {
                    if (swipe_det.eX > swipe_det.sX) direc = "r";
                    else direc = "l";
                }
                //vertical detection
                if ((((swipe_det.eY - min_y > swipe_det.sY) || (swipe_det.eY + min_y < swipe_det.sY)) && ((swipe_det.eX < swipe_det.sX + max_x) && (swipe_det.sX > swipe_det.eX - max_x)))) {
                    if (swipe_det.eY > swipe_det.sY) direc = "d";
                    else direc = "u";
                }

                if (direc != "") {
                    if (typeof func == 'function') func(el, direc);
                }
                direc = "";
            }, false);
        }

        function swiping(el, d) {
            if (d == 'r') {
                history.back();
            } else if (d == 'l') {
                history.go(1);
            }

        }

        vm.navigated = false;
        vm.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
            if (from.name) {
                vm.navigated = true;
            }
        });

        detectswipe('body', swiping);
    }
})();
