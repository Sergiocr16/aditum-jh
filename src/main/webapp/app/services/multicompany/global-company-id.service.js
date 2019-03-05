(function () {
    'use strict';

    angular
        .module('aditumApp')
        .service('globalCompany', globalCompany);

    globalCompany.$inject = ['CommonMethods', '$localStorage', 'Auth', '$rootScope', '$state'];

    function globalCompany(CommonMethods, $localStorage, Auth, $rootScope, $state) {

        return {
            getId: function () {
                if ($localStorage.companyId !== undefined || $localStorage.companyId != null) {
                    return CommonMethods.decryptIdUrl($localStorage.companyId)
                } else {
                    Auth.logout();
                    $rootScope.companyUser = undefined;
                    $localStorage.companyId = undefined;
                    $state.go('home');
                    $rootScope.menu = false;
                    $rootScope.companyId = undefined;
                    $rootScope.showLogin = true;
                    $rootScope.inicieSesion = false;
                    return null;
                }
            }
        };
    }
})();
