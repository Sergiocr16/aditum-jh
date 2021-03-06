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
                    $rootScope.menu = false;
                    $rootScope.companyId = undefined;
                    $rootScope.showLogin = true;
                    $rootScope.inicieSesion = false;
                    if(!$state.includes('finishReset')){
                        $state.go('home');
                    }
                    return null;
                }
            },
            getMacroId: function () {
                if ($localStorage.macroCompanyId !== undefined || $localStorage.macroCompanyId != null) {
                    return CommonMethods.decryptIdUrl($localStorage.macroCompanyId)
                } else {
                    Auth.logout();
                    $rootScope.companyUser = undefined;
                    $localStorage.companyId = undefined;
                    $rootScope.menu = false;
                    $rootScope.companyId = undefined;
                    $rootScope.macroCompanyId = undefined;
                    $rootScope.showLogin = true;
                    $rootScope.inicieSesion = false;
                    if(!$state.includes('finishReset')){
                        $state.go('home');
                    }
                    return null;
                }
            }
        };
    }
})();
