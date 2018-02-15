(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('InsertLoginCodeController', InsertLoginCodeController);

    InsertLoginCodeController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','$localStorage'];

    function InsertLoginCodeController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,$localStorage) {
        var vm = this;
        vm.loginCodeNotFound = 0;
        $("#code_login_input").attr("placeholder", "Ingrese el código");

        var TxtType = function(el, toRotate, period) {
            this.toRotate = toRotate;
            this.el = el;
            this.loopNum = 0;
            this.period = parseInt(period, 10) || 2000;
            this.txt = '';
            this.tick();
            this.isDeleting = false;
        };

        TxtType.prototype.tick = function() {
            var i = this.loopNum % this.toRotate.length;
            var fullTxt = this.toRotate[i];

            if (this.isDeleting) {
                this.txt = fullTxt.substring(0, this.txt.length - 1);
            } else {
                this.txt = fullTxt.substring(0, this.txt.length + 1);
            }

            this.el.innerHTML = '<span class="wrap">'+this.txt+'</span>';

            var that = this;
            var delta = 200 - Math.random() * 100;

            if (this.isDeleting) { delta /= 2; }

            if (!this.isDeleting && this.txt === fullTxt) {
                delta = this.period;
                this.isDeleting = true;
            } else if (this.isDeleting && this.txt === '') {
                this.isDeleting = false;
                this.loopNum++;
                delta = 500;
            }

            setTimeout(function() {
                that.tick();
            }, delta);
        };


        angular.element(document).ready(function () {
            var elements = document.getElementsByClassName('typewrite');
            for (var i=0; i<elements.length; i++) {
                var toRotate = elements[i].getAttribute('data-type');
                var period = elements[i].getAttribute('data-period');
                if (toRotate) {
                    new TxtType(elements[i], JSON.parse(toRotate), period);
                }
            }
            // INJECT CSS
            var css = document.createElement("style");
            css.type = "text/css";
            css.innerHTML = ".typewrite > .wrap { border-right: 0.08em solid #fff}";
            document.body.appendChild(css);
            //  $("#loginCodeWelcomeAgilText").fadeIn(1000);
            //
            //
            // setTimeout(function() {
            //     $("#loginCodeWelcomeSeguroText").fadeIn(1400);
            // },900)
            //
            // setTimeout(function() {
            //     $("#loginCodeWelcomeConfiableText").fadeIn(1400);
            // },2000)

             setTimeout( function() {
                     $(".aditumLogoLoginCode").fadeIn(1600);
             },2900)



            setTimeout(function() {
                 $("#containerLoginCode").fadeIn(1300);
             },4000)

                 $('body').removeClass("gray");
                 $rootScope.showLogin = false;
                 $rootScope.menu = false;
                 $rootScope.isInManual = true;


         });


        vm.changeStatus = function () {
            vm.loginCodeNotFound = 0;
            if(vm.loginCode == "" ){
                $("#code_login_input").css("text-transform", "none");
                $("#code_login_input").attr("placeholder", "Ingrese el código");
            } else {
                $("#code_login_input").css("text-transform", "uppercase");
            }

        }
        vm.validateLoginCode = function(){
            House.getByLoginCode({loginCode:vm.loginCode}).$promise.then(onSuccess, onError);
        }
         function onSuccess(data) {
          $state.go('loginCodeWelcome',{loginCode:vm.loginCode});
        }

        vm.back = function(){
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
        }
          function onError() {
              vm.loginCodeNotFound = 1;
        }

    }
})();
