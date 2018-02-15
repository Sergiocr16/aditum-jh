(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BioStarController', BioStarController);

    BioStarController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','BioStar','AuthServerProvider','$q','$sessionStorage','$localStorage','$http','$cookies'];

    function BioStarController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, BioStar,AuthServerProvider,$q,$sessionStorage,$localStorage,$http,$cookies) {

        var vm = this;
        $rootScope.active= "soporte";
        vm.isAuthenticated = Principal.isAuthenticated;

                function storeAuthenticationToken(jwt, rememberMe) {
                    if(rememberMe){
                        $localStorage.bscloudsessionid = jwt;
                    } else {
                        $sessionStorage.bscloudsessionid = jwt;
                    }
                }
function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}
//        $http.defaults.withCredentials = true
//        $http.defaults.headers.post.Cookies = readCookie("bs-cloud-session-id");
       vm.login= function(){
       var user = {name:"aditum-prueba",user_id:"sergio",password:"@Ankara06"}
        $.ajax({
                url: "https://api.biostar2.com/v1/login",
                type: "post",
                contentType: "application/x-www-form-urlencoded",
                data:user,
                    xhrFields: {
                           withCredentials: true
                         },
//                         beforeSend:setHeader
              });
       function setHeader(xhr) {
  xhr.setRequestHeader('Access-Control-Allow-Origin', "http://localhost:7777");

  }
//        BioStar.login(JSON.stringify(user),function(a,b,h){
//        console.log(localStorage.getItem("bs-cloud-session-id"))
//         console.log(document.cookie)
//               var bearerToken= a.config.headers.Authorization
//if (angular.isDefined(bearerToken)  && bearerToken.slice(0, 7) === 'Bearer ') {
//                    var jwt = bearerToken;
//                    storeAuthenticationToken(jwt, true);
//                    return jwt;
//                }
//                console.log("AAAAAAAAAAAAAAAAAAAA")
//
//              })
//       login(user,function(a){
//        console.log(a)
//       })
//       login(user,function(a){
//       console.log(a)
//       })

       }

       vm.logout = function(){
       $.ajax({
         url: "https://api.biostar2.com/v1/logout",
         type: "post",
  xhrFields: {
                           withCredentials: true
                         },
          beforeSend: setHeader,
       });
       function setHeader(xhr) {
  xhr.setRequestHeader('Access-Control-Allow-Origin', "http://localhost:7777");

  }
//       BioStar.logout({},function(a){
//       console.log(a)
//       },function(a){
//       console.log(a)
//       })
       }

       vm.lockDoor = function(){

//$http.post("https://api.biostar2.com/v1/doors/:door_id/lock", {door_id}, {
//  withCredentials: true
//});
//        BioStar.lockDoor({door_id:1},function(a){
//        console.log(a)
//        },function(a){
//        console.log(a)
//        })
$.ajax({
         url: "https://api.biostar2.com/v1/doors/:door_id/lock",
         type: "post",
         data:{door_id:1},
xhrFields: {
                           withCredentials: true
                         },
crossDomain: true,
//beforeSend:setHeader
       });

             function setHeader(xhr) {
         xhr.setRequestHeader('Cookie', "s%3A5apr09bZeXwSJfchsJj8Huxw1kCgD81k.oGkJ8KgxlEA4jWvQfJsKRadtlikcxMks7QSUAXsR1fA");
         }
       }
        function login (credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthServerProvider.loginBioStar(credentials)
                .then(loginThen)
                .catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen (data) {
                Principal.identity(true).then(function(account) {
                console.log(account)
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    if (account!== null) {
//                        $translate.use(account.langKey).then(function () {
//                            $translate.refresh();
//                        });

                    }
//                    JhiTrackerService.sendActivity();
 $rootScope.$broadcast('authenticationSuccess');
                    deferred.resolve(data);
                });
                return cb();
            }
            return deferred.promise;
        }

    }
})();
