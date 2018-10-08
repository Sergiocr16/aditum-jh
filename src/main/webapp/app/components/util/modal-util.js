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
            confirmDialog: confirmDialog,
            confirmDialogCustom: confirmDialogCustom,
            showLoadingBar: showLoadingBar,
            hideLoadingBar: hideLoadingBar
        };
        var $scope = {};
        var last = {
            bottom: false,
            top: true,
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
        }

        function showLoadingBar() {
            $rootScope.isShowingLoadingBar = true;
            $("#content").addClass("disabledDiv");
        }

    }
})
();
