(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountStatusController', AccountStatusController);

    AccountStatusController.$inject = ['$rootScope', '$scope', '$state', 'AccountStatus', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage'];

    function AccountStatusController($rootScope, $scope, $state, AccountStatus, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage) {

        var vm = this;
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

        function loadAll() {

            AccountStatus.query({
                houseId: $localStorage.houseSelected.id,
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                var countPassedDate = 0;

                $("#loading").fadeOut(300);
                setTimeout(function() {
                    $("#data").fadeIn("slow");
                }, 900)
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }
})();
