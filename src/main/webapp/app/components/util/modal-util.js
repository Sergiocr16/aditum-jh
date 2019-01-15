(function () {
    'use strict';

    angular
        .module('aditumApp')
        .factory('Modal', Modal);
    Modal.$inject = ['$mdToast', '$mdDialog', '$rootScope'];

    function Modal($mdToast, $mdDialog, $rootScope) {
        var service = {
            toast: toast,
            actionToast: actionToast,
            dialog: dialog,
            customDialog:customDialog,
            confirmDialog: confirmDialog,
            confirmDialogCustom: confirmDialogCustom,
            showLoadingBar: showLoadingBar,
            hideLoadingBar: hideLoadingBar,
            enteringForm: enteringForm,
            leavingForm: leavingForm,
            enteringDetail:enteringDetail,
            leavingDetail:leavingDetail
        };
        var $scope = {};
        var last = {
            bottom: true,
            top: false,
            left: false,
            right: true
        };

        $scope.toastPosition = angular.extend({}, last);

        $scope.getToastPosition = function () {
            sanitizePosition();

            return Object.keys($scope.toastPosition)
                .filter(function (pos) {
                    return $scope.toastPosition[pos];
                })
                .join(' ');
        };

        function sanitizePosition() {
            var current = $scope.toastPosition;

            if (current.bottom && last.top) current.top = false;
            if (current.top && last.bottom) current.bottom = false;
            if (current.right && last.left) current.left = false;
            if (current.left && last.right) current.right = false;

            last = angular.extend({}, current);
        }

        var pinTo = $scope.getToastPosition();

        return service;

        function toast(text) {
            $mdToast.show(
                $mdToast.simple()
                    .textContent(text)
                    .position(pinTo)
                    .hideDelay(3000)
            );
        }

        function actionToast(text, actionText, action) {
            var toast = $mdToast.simple()
                .textContent(text)
                .action(actionText)
                .highlightAction(true)
                .highlightClass('md-accent')// Accent is used by default, this just demonstrates the usage.
                .position(pinTo);

            $mdToast.show(toast).then(function (response) {
                if (response == 'ok') {
                    action()
                }
            });
        };

        function dialog(title, content, okText) {
            var ev = new Event('build');
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title(title)
                    .textContent(content)
                    .ariaLabel('Alert Dialog Demo')
                    .ok(okText)
                    .targetEvent(ev)
            );
        }
        function customDialog(template) {
            var ev = new Event('build');
            $mdDialog.show({
                template: template,
                parent: angular.element(document.querySelector('#popupContainer')),
                targetEvent: ev,
                escapeToClose: true,
                clickOutsideToClose:true,

            })
        };

        function confirmDialog(title, content, ok) {
            var ev = new Event('build');
            var confirm = $mdDialog.confirm()
                .title(title)
                .textContent(content)
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok('Aceptar')
                .cancel('Cancelar');

            $mdDialog.show(confirm).then(function () {
                ok()
            }, function () {

            });
        };

        function confirmDialogCustom(title, content, okText, cancelText, ok) {
            var ev = new Event('build');
            var confirm = $mdDialog.confirm()
                .title(title)
                .textContent(content)
                .ariaLabel('Lucky day')
                .targetEvent(ev)
                .ok(okText)
                .cancel(cancelText);
            $mdDialog.show(confirm).then(function () {
                ok()
            }, function () {

            });
        };

        function hideLoadingBar() {
            $rootScope.isShowingLoadingBar = false;
            $("#content").removeClass("disabledDiv");
            $("#content-officer").removeClass("disabledDiv2");

        }

        function showLoadingBar() {
            $rootScope.isShowingLoadingBar = true;
            $("#content").addClass("disabledDiv");
            $("#content-officer").addClass("disabledDiv2");
        }

        function enteringForm(f,text,f2,text2) {
            $rootScope.action = f;
            if(f2!=undefined){
                $rootScope.action2 = f2;
                $rootScope.secondBtnForm = true;
                if (text2 == undefined) {
                    $rootScope.formActionText = "Aceptar";
                } else {
                    $rootScope.formActionText2 = text2;
                }
            }
            if (text == undefined) {
                $rootScope.formActionText = "Aceptar";
            } else {
                $rootScope.formActionText = text;
            }

            $rootScope.inForm = true;
        }

        function enteringDetail() {
            $rootScope.inDetail = true;
        }
        function leavingDetail() {
            $rootScope.inDetail = false;
        }

        function leavingForm() {
            $rootScope.inForm = false;
            $rootScope.secondBtnForm = false;
        }


        function findBootstrapEnvironment() {
            var envs = ['xs', 'sm', 'md', 'lg'];

            var $el = $('<div>');
            $el.appendTo($('body'));

            for (var i = envs.length - 1; i >= 0; i--) {
                var env = envs[i];

                $el.addClass('hidden-' + env);
                if ($el.is(':hidden')) {
                    $el.remove();
                    return env;
                }
            }
        }

        function isScreenSizeSmall() {
            var envs = ['xs', 'sm', 'md'];
            var e = 0;
            for (var i = 0; i < envs.length; i++) {
                if (envs[i] === findBootstrapEnvironment()) {
                    e++;
                }
            }
            if (e > 0) {
                return true;
            }
            return false;
        }

    }
})
();
