(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SoporteUserController', SoporteUserController);

    SoporteUserController.$inject = ['Company', '$timeout', '$scope', '$stateParams', 'Soporte', '$rootScope', 'Modal', 'globalCompany', 'Principal', 'MultiCompany'];

    function SoporteUserController(Company, $timeout, $scope, $stateParams, Soporte, $rootScope, Modal, globalCompany, Principal, MultiCompany) {
        var vm = this;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        $rootScope.active = "soporte-user";
        $rootScope.mainTitle = "Soporte";

        var TxtType = function (el, toRotate, period) {
            this.toRotate = toRotate;
            this.el = el;
            this.loopNum = 0;
            this.period = parseInt(period, 10) || 2000;
            this.txt = '';
            this.tick();
            this.isDeleting = false;
        };

        TxtType.prototype.tick = function () {
            var i = this.loopNum % this.toRotate.length;
            var fullTxt = this.toRotate[i];

            if (this.isDeleting) {
                this.txt = fullTxt.substring(0, this.txt.length - 1);
            } else {
                this.txt = fullTxt.substring(0, this.txt.length + 1);
            }

            this.el.innerHTML = '<span class="wrap">' + this.txt + '</span>';

            var that = this;
            var delta = 200 - Math.random() * 100;

            if (this.isDeleting) {
                delta /= 2;
            }

            if (!this.isDeleting && this.txt === fullTxt) {
                delta = this.period;
                this.isDeleting = true;
            } else if (this.isDeleting && this.txt === '') {
                this.isDeleting = false;
                this.loopNum++;
                delta = 500;
            }

            setTimeout(function () {
                that.tick();
            }, delta);
        };

        Company.get({id: parseInt(globalCompany.getId())}, function (condo) {
            vm.email = condo.supportEmail;
            vm.phone =  condo.supportNumber;
            console.log(condo)
        });
        angular.element(document).ready(function () {
            var elements = document.getElementsByClassName('typewrite');
            for (var i = 0; i < elements.length; i++) {
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

            setTimeout(function () {
                $(".aditumLogoLoginCode").fadeIn(1600);
            }, 2900)


            setTimeout(function () {
                $("#containerLoginCode").fadeIn(1300);
            }, 4000)

            $('body').removeClass("gray");
            $rootScope.showLogin = false;
            $rootScope.menu = false;
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function defineSoporte() {
            Principal.identity().then(function (account) {
                vm.soporte.username = account.login;
                vm.soporte.email = account.email;
                switch (account.authorities[0]) {
                    case "ROLE_MANAGER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            vm.soporte.fullName = data.name + ' ' + data.lastname + ' ' + data.secondlastname;
                            actualSave()
                        });
                        break;
                    case "ROLE_USER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            vm.soporte.fullName = data.name + ' ' + data.lastname + ' ' + data.secondlastname;
                            vm.soporte.houseId = globalCompany.getHouseId();
                            actualSave()
                        })
                        break;
                    case "ROLE_RH":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            vm.soporte.fullName = data.name + ' ' + data.lastname + ' ' + data.secondlastname;
                            actualSave()
                        })
                        break;
                    case "ROLE_JD":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            vm.soporte.fullName = "Junta Directiva";
                            actualSave()
                        });
                        break;
                }
            })
        }

        function save() {
            Modal.confirmDialog("¿Está seguro que desea enviar el mensaje?", '', function () {
                Modal.showLoadingBar();
                vm.soporte.creationDate = moment().format();
                vm.soporte.companyId = globalCompany.getId();
                vm.soporte.fullName = $rootScope.companyUser.name + ' ' + $rootScope.companyUser.lastname + ' ' + $rootScope.companyUser.secondlastname;
                vm.soporte.houseId = globalCompany.getHouseId();
                vm.soporte.status = 0;
                defineSoporte();
            })
        }

        function actualSave() {
            vm.isSaving = true;
            if (vm.soporte.id !== null) {
                Soporte.update(vm.soporte, onSaveSuccess, onSaveError);
            } else {
                Soporte.save(vm.soporte, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('aditumApp:soporteUpdate', result);
            vm.soporte = {};
            Modal.hideLoadingBar();
            Modal.toast("Hemos recibido tu solicitud, recibirás respuesta lo más rápido posible en horario de oficina.")
            $timeout(function () {
                $scope.form.$setPristine();
                $scope.form.$setUntouched();
                $scope.form.$submitted = false;
            });
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
