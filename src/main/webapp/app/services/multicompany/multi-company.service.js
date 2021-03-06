(function() {
    'use strict';

    angular
        .module('aditumApp')
        .service('MultiCompany', MultiCompany);

    MultiCompany.$inject = ['Principal','AdminInfo','Resident','OfficerAccount','RHAccount','$rootScope','JuntaDirectivaAccount','MacroOfficerAccount','MacroAdminAccount'];

    function MultiCompany (Principal,AdminInfo,Resident,OfficerAccount, RHAccount, $rootScope,JuntaDirectivaAccount, MacroOfficerAccount,MacroAdminAccount) {
        var companyId;
        var service = {
            getCurrentUserCompany:getCurrentUserCompany,
        };
        return service;
        function getCurrentUserCompany(){
            return Principal.identity().then(function(account){
            if(account!=undefined){
            if(account.authorities.length >= 2){
              return null;
            }
                switch (account.authorities[0]){
                    case "ROLE_ADMIN":
                         return undefined;
                     break;
                    case "ROLE_MANAGER":
                       return isManager(account.id);
                    break;
                    case "ROLE_OFFICER":
                       return isOfficer(account.id);
                    break;
                     case "ROLE_USER":
                       return isResident(account.id);
                    break;
                   case "ROLE_RH":
                      return isRH(account.id);
                   break;
                    case "ROLE_JD":
                        return isJD(account.id);
                    break;
                    case "ROLE_OFFICER_MACRO":
                        return isOfficerMacro(account.id);
                    break;
                    case "ROLE_MANAGER_MACRO":
                        return isAdminMacro(account.id);
                        break;
                }
                }
            })

        }


        function isOfficer(accountId){
              return OfficerAccount.findByUserId({id: accountId}).$promise
       }

        function isManager(accountId){
             return AdminInfo.findByUserId({id: accountId}).$promise
         }


        function isResident(accountId){
             return Resident.findByUserId({id: accountId}).$promise
       }

        function isRH(accountId){
                return RHAccount.findByUserId({id: accountId}).$promise
          }
        function isJD(accountId){
            return JuntaDirectivaAccount.findByUserId({id: accountId}).$promise
        }
        function isOfficerMacro(accountId){
            return MacroOfficerAccount.findByUserId({id: accountId}).$promise
        }
        function isAdminMacro(accountId){
            return MacroAdminAccount.findByUserId({id: accountId}).$promise
        }

    }
})();
