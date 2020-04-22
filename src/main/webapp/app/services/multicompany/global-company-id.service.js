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
                    if (!$state.includes('finishReset')) {
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
                    if (!$state.includes('finishReset')) {
                        $state.go('home');
                    }
                    return null;
                }
            },
            getHouseId: function () {
                if ($localStorage.houseId !== undefined || $localStorage.houseId != null) {
                    return CommonMethods.decryptIdUrl($localStorage.houseId)
                } else {
                    Auth.logout();
                    $rootScope.companyUser = undefined;
                    $localStorage.companyId = undefined;
                    $rootScope.menu = false;
                    $rootScope.companyId = undefined;
                    $rootScope.macroCompanyId = undefined;
                    $rootScope.showLogin = true;
                    $rootScope.inicieSesion = false;
                    if (!$state.includes('finishReset')) {
                        $state.go('home');
                    }
                    return null;
                }
            },
            getUserRole: function () {
                if($localStorage.userRole!=undefined) {
                    var userRole = CommonMethods.decryptIdUrl($localStorage.userRole);
                    switch (userRole) {
                        case "ROLE_MANAGER":
                            return "ROLE_MANAGER";
                            break;
                        case "ROLE_USER":
                            return "ROLE_USER";
                            break;
                        case "ROLE_OWNER":
                            return "ROLE_USER";
                            break;
                        case "ROLE_OFFICER_MACRO":
                            return "ROLE_OFFICER_MACRO";
                            break;
                        case "ROLE_OFFICER":
                            return "ROLE_OFFICER";
                            break;
                        case "ROLE_JD":
                            return "ROLE_JD";
                            break;
                    }
                }
                return undefined;
            },
            getUser: function () {
                return {
                    id: CommonMethods.decryptIdUrl($localStorage.userId),
                    idNumber: CommonMethods.decryptIdUrl($localStorage.userIdNumber),
                    type: CommonMethods.decryptIdUrl($localStorage.userType)
                }
            }
        };

    }
})();
