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

    run.$inject = ['stateHandler', 'translationHandler', '$state', '$rootScope', '$templateCache', '$http', '$filter'];

    function run(stateHandler, translationHandler, $state, vm, $templateCache, $http, $filter) {
        preloadTemplates($state, $templateCache, $http);

        function preloadTemplates($state, $templateCache, $http) {
            angular.forEach($state.get(), function (state, key) {
                if (state.templateUrl !== undefined && state.preload !== false) {
                    $http.get(state.templateUrl, {cache: $templateCache});
                }
            });
        }

        vm.domains = [
            {
                id: 1,
                condominiums:[1,2,5],
                domain: "app.aditumcr.com",
                companyName: "ADITUM CR",
                title: "ADITUM",
                favIcon: "content/images/casco_aditum.png",
                loginLogo: {
                    url: "content/images/Portada-whatsapp.png",
                    height: "150",
                    width: ""
                },
                navBarLogo: {
                    big: {
                        url: "content/images/Logo-ClaroHor645x200.png",
                        height: "43",
                        width: ""
                    }, small: {
                        url: "content/images/Logo-oscuroHor645x200.png",
                        height: "32",
                        width: ""
                    }
                },
            },
            {
                id: 2,
                condominiums:[4],
                domain: "app.convivecr.com",
                companyName: "CONVIVE",
                title: "Convive - Administración de Condominios",
                favIcon: "content/images/convive-favicon.png",
                loginLogo: {
                    url: "content/images/convive-login.png",
                    height: "150",
                    width: ""
                },
                navBarLogo: {
                    big: {
                        url: "content/images/convive-43px.png",
                        height: "43",
                        width: ""
                    }, small: {
                        url: "content/images/convive-pequeno.png",
                        height: "40",
                        width: ""
                    }
                },
            },
        ];
        vm.currentDomain = window.location.host;
        var exist = false;
        for (var i = 0; i < vm.domains.length; i++) {
            if (vm.domains[i].domain == vm.currentDomain) {
                vm.adminCompany = vm.domains[i];
                exist = true;
                changeFavicon(vm.adminCompany.favIcon)
            }
        }
        if (!exist) {
            vm.adminCompany = vm.domains[0];
            changeFavicon(vm.adminCompany.favIcon)
        }

        function changeFavicon(src) {
            var link = document.createElement('link'),
                oldLink = document.getElementById('dynamic-favicon');
            link.id = 'dynamic-favicon';
            link.rel = 'shortcut icon';
            link.href = src;
            if (oldLink) {
                document.head.removeChild(oldLink);
            }
            document.head.appendChild(link);
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
        vm.EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
        vm.showContent = false;
        vm.isShowingLoadingBar = false;
        vm.inForm = false;
        vm.inDetail = false;
        vm.secondBtnForm = true;

        vm.fMoney = function (amount) {
            var decimal = vm.currency == "$" ? 2 : 2;
            return vm.currency + " " + $filter('currency')(amount, "", decimal);
        }


        vm.fMoneyBank = function (currency, amount) {
            var decimal = currency == "$" ? 2 : 2;
            return currency + " " + $filter('currency')(amount, "", decimal);
        }

        vm.setInvalidForm = function (i) {
            vm.isInvalidForm = i;
        };
        vm.back = function () {
            window.history.back();
        };
        vm.formAction = function () {
            setTimeout(function () {
                vm.action();
            }, 30)
        }
        vm.formAction2 = function () {
            setTimeout(function () {
                vm.action2();
            }, 30)
        }
    }
})();
