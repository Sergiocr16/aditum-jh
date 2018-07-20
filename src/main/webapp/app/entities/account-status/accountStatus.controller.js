(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountStatusController', AccountStatusController);

    AccountStatusController.$inject = ['$rootScope', '$scope', '$state', 'AccountStatus', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage'];

    function AccountStatusController($rootScope, $scope, $state, AccountStatus, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage) {

        var vm = this;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m-6, 1);
        var lastDay = new Date(y, m + 1, 0);
        vm.searchType = 1;
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };

        setTimeout(function() {
            loadAll();
        }, 4000)


        vm.datePassed = function(cuota){
        var rightNow = new Date();
        var chargeDate = new Date(moment(cuota.date))
        return ((chargeDate.getTime()>rightNow.getTime()))
        }

        $scope.$watch(function() {
            return $rootScope.houseSelected;
        }, function() {
            $("#data").fadeOut(0);
            $("#loading").fadeIn("slow");
            loadAll();
            vm.isEditing = false;
        });


        vm.openCalendar = function(charge) {

            charge.openDate = true;

        }


        vm.searchByType = function(type){
            switch(type){
                case 1:
                   vm.searchType = 1;
                break;
                case 2:
                   vm.searchType = 2;
                break;
                case 3:
                    vm.searchType = 3;
                break;
                case 4:
                    vm.searchType = 4;
                break;

            }

        }
        function loadAll() {

            AccountStatus.query({
                houseId: $localStorage.houseSelected.id,
                initial_time:  moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
            }, onSuccess, onError);


            function onSuccess(data, headers) {
            vm.initial_time = vm.dates.initial_time
            vm.final_time = vm.dates.final_time
               console.log(data);
                vm.accountStatusItems = data;
                $("#loading").fadeOut(300);
                setTimeout(function() {
                    $("#data").fadeIn("slow");
                }, 900)
            }
    vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
             var rgx = /(\d+)(\d{3})/;
             while (rgx.test(x1)) {
                     x1 = x1.replace(rgx, '$1' + ',' + '$2');
             }
             return x1 + x2;
         }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }
})();
