(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope','$rootScope', 'Principal', 'LoginService', '$state','Dashboard'];

    function HomeController ($scope,$rootScope, Principal, LoginService, $state, Dashboard) {
        var vm = this;
        vm.isInLogin = $state.includes('home');
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
//            if(Principal.isAuthenticated){
//            Principal.identity().then(function(account){
//            if(account !== null){
//            if(account.authorities[0] === 'ROLE_USER'){
//                $state.go('residentByHouse');
//             }
//             }
//            })
//            }

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });


        getAccount();
//        loadAll();

        function loadAll() {
            Dashboard.query({companyId : $rootScope.companyId},function(result) {
                vm.dashboard = result;
                console.log(vm.dashboard)
                showData();
                vm.watch = formatWatch(vm.dashboard.currentWatch);
            });
        }
        function formatResponsableOfficer(stringOfficer) {
            var variables = stringOfficer.split(';')
            var officer = {};
            officer.id = variables[0];
            officer.identificationnumber = variables[1];
            officer.name = variables[2];
            return officer;
        }
        function getformatResponsableOfficers(watch) {
            var formattedOfficers = [];
            var stringOfficers = watch.responsableofficer.slice(0, -2);
            var officers = stringOfficers.split('||');
            angular.forEach(officers, function(officer, key) {
                formattedOfficers.push(formatResponsableOfficer(officer))
            })
            return formattedOfficers;
        }
        function formatWatch(watch) {
            watch.initialtime = moment(watch.initialtime).format('h:mm a');
            if (watch.finaltime === null) {
                watch.finaltime = 'Aún en progreso'
            } else {
                watch.finaltime = moment(watch.finaltime).format('h:mm a');
            }
            watch.officers = getformatResponsableOfficers(watch);
            return watch;
        }
        function showData(){

        angular.element(document).ready(function () {
        $('#all').fadeIn("300");

        $('.counter').counterUp({
                                  delay: 10,
                                  time: 1000
                              });


         });

        }
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
