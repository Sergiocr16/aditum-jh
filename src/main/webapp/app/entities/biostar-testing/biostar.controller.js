(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('BioStarController', BioStarController);

    BioStarController.$inject = ['$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','BioStar','AuthServerProvider','$q','$sessionStorage','$localStorage','$cookieStore','$http'];

    function BioStarController ($rootScope, $state,Principal, $timeout, Auth,MultiCompany, BioStar,AuthServerProvider,$q,$sessionStorage,$localStorage,$cookies,$cookieStore,$http) {

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
       vm.login= function(){
       var user = {name:"aditum-prueba",user_id:"sergio",password:"@Ankara06"}
        BioStar.login(JSON.stringify(user),function(a,b,h){
//        console.log("AAAAAAAAAAAAAAAAA")
//         console.log($cookieStore.get("bs-cloud-session-id"))
               var bearerToken= a.config.headers.Authorization
if (angular.isDefined(bearerToken)  && bearerToken.slice(0, 7) === 'Bearer ') {
                    var jwt = bearerToken;
                    storeAuthenticationToken(jwt, true);
                    return jwt;
                }
                console.log("AAAAAAAAAAAAAAAAAAAA")

              })
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
         }
       });
//       BioStar.logout({},function(a){
//       console.log(a)
//       },function(a){
//       console.log(a)
//       })
       }

       vm.lockDoor = function(){
       var cookie = $cookies.get("bs-cloud-session-id"); // suppose you already set $cookies.myCookie= 'xxx';
//          $http.defaults.headers.post.Cookies = cookie;
          console.log(document.cookie)
//$http.post("https://api.biostar2.com/v1/doors/:door_id/lock", {door_id}, {
//  withCredentials: true
//});
        BioStar.lockDoor({door_id:1},function(a){
        console.log(a)
        },function(a){
        console.log(a)
        })
//$.ajax({
//         url: "https://api.biostar2.com/v1/doors/:door_id/lock",
//         type: "post",
//         data:{door_id:1},
//
//       });
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
