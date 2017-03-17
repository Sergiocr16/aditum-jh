(function() {
    'use strict';

    angular
        .module('aditumApp')
        .service('MultiCompany', MultiCompany);

    MultiCompany.$inject = ['Principal','AdminInfo','Resident','$rootScope'];

    function MultiCompany (Principal,AdminInfo,Resident,$rootScope) {

        var companyId;
        var service = {
            getCurrentUserCompany,
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
                         return isManager(account.id);
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
                }
                }
            })

        }


        function isOfficer(accountId){
              return Officer.findByUserId({id: accountId}).$promise
       }

        function isManager(accountId){
             return AdminInfo.findByUserId({id: accountId}).$promise
         }


        function isResident(accountId){
             return Resident.findByUserId({id: accountId}).$promise
       }

    }
})();
