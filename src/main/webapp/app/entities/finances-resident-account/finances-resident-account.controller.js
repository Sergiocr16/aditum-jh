(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('FinancesResidentController', FinancesResidentController);

    FinancesResidentController.$inject = ['AlertService','$rootScope','Principal','$state','$localStorage','Balance','House'];

    function FinancesResidentController(AlertService,$rootScope,Principal,$state,$localStorage,Balance,House) {
        var vm = this;
        $rootScope.active = "financesResidentAccount";
       console.log( $localStorage.houseSelected);

              var vm = this;
               vm.isAuthenticated = Principal.isAuthenticated;


               setTimeout(function() {
                   loadAll();
               }, 3000)

               function loadAll() {
                   Balance.queryBalances({
                       companyId: $rootScope.companyId
                   }, onSuccess, onError);

                   function onSuccess(data, headers) {

                       vm.totalItems = headers('X-Total-Count');
                       vm.queryCount = vm.totalItems;
                       angular.forEach(data, function(value, key) {
                           if (value.housenumber == 9999) {
                               value.housenumber = "Oficina"
                           }
                           value.debit = value.balance.debit;
                       })
                       vm.houses = data;
                     if ($localStorage.houseSelected != null || $localStorage.houseSelected != undefined) {
                        House.get({
                            id: $localStorage.houseSelected.id
                        }, function(result) {
                        $localStorage.houseSelected = result

                            vm.house = $localStorage.houseSelected;
                            $rootScope.houseSelected = $localStorage.houseSelected;
                        })
                    } else {
                        if (vm.houses.length > 0) {
                            $rootScope.houseSelected = vm.houses[0]
                            $localStorage.houseSelected = vm.houses[0]
                            vm.house = $rootScope.houseSelected;
                        }
                    }


                       setTimeout(function() {
                           $("#loadingIcon3").fadeOut(300);
                       }, 400)
                       setTimeout(function() {
                           $("#tableData2").fadeIn('slow');
                           $state.go('finanzasPorCasa.paymentsPerHouse');

                       }, 700)
                   }

                   function onError(error) {
                       AlertService.error(error.data.message);
                   }
               }

                    vm.expand = function(){

                               setTimeout(function () {
                                       $scope.$apply(function () {
                                            vm.expanding = !vm.expanding;
                                       });
                                   }, 200);

                               }
               vm.defineBalanceClass = function(balance) {
                   var b = parseInt(balance);
                   if (b != 0) {
                       if (b > 0) {
                           return "greenBalance";
                       } else {
                           return "redBalance";
                       }
                   }
               }
               vm.defineBalanceTotalClass = function(balance) {
                   var b = parseInt(balance);
                   if (b != 0) {
                       if (b > 0) {
                           return "deuda-total-positiva";
                       } else {
                           return "deuda-total-negativa";
                       }
                   } else {
                       return "deuda-total";
                   }
               }
    }
})();
