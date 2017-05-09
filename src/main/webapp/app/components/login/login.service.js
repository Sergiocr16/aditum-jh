(function() {
    'use strict';

    angular
        .module('aditumApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$uibModal'];

    function LoginService ($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open () {
        toastr['error']('No tienes permiso para esa acción o debes de iniciar sesión primeramente')
//            if (modalInstance !== null) return;
//            modalInstance = $uibModal.open({
//                animation: true,
//                templateUrl: 'app/components/login/login.html',
//                controller: 'LoginController',
//                controllerAs: 'vm',
//                resolve: {
//                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                        $translatePartialLoader.addPart('login');
//                        return $translate.refresh();
//                    }]
//                }
//            });
//            modalInstance.result.then(
//                resetModal,
//                resetModal
//            );
        }
    }
})();
