(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountStatusController', AccountStatusController);

    AccountStatusController.$inject = ['$rootScope', '$scope', '$state', 'AccountStatus', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'House', 'CommonMethods', '$localStorage'];

    function AccountStatusController($rootScope, $scope, $state, AccountStatus, ParseLinks, AlertService, paginationConstants, pagingParams, House, CommonMethods, $localStorage) {

        var vm = this;
        var date = new Date(), y = date.getFullYear(), m = date.getMonth();
        var firstDay = new Date(y, m - 6, 1);
        var lastDay = new Date(y, m + 2, 0);
        vm.searchType = 1;
        vm.isReady = false;
        vm.openCalendar = openCalendar;
        vm.loadAll = loadAll;
        vm.datePickerOpenStatus = {};
        vm.dates = {
            initial_time: firstDay,
            final_time: lastDay
        };

        loadAll();


        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.datePassed = function (cuota) {
            var rightNow = new Date();
            var chargeDate = new Date(moment(cuota.date))
            return ((chargeDate.getTime() > rightNow.getTime()))
        }

        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {
            vm.isReady = true;
            loadAll();
            vm.isEditing = false;
        });


        vm.showDetail = function (item) {
            item.showDetail = !item.showDetail;

        }
        vm.searchByType = function (type) {
            switch (type) {
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

        vm.consult = function () {
            $("#loading2").fadeIn(0);
            $("#accountStatusContainer").fadeOut(0);
            loadAll();
        }

        function loadAll() {

            AccountStatus.query({
                houseId: $localStorage.houseSelected.id,
                initial_time: moment(vm.dates.initial_time).format(),
                final_time: moment(vm.dates.final_time).format(),
                resident_account: false,
                today_time: moment(new Date()).format(),

            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.initial_time = vm.dates.initial_time
                vm.final_time = vm.dates.final_time
                var countPassedDate = 0;
                angular.forEach(data.listaAccountStatusItems, function (item, i) {

                    var rightNow = new Date();
                    var chargeDate = new Date(moment(item.date))
                    if (chargeDate.getTime() > rightNow.getTime()) {
                        item.datePassed = true;
                        if (countPassedDate == 0) {
                            item.definedFirstDatePassed = true;
                            countPassedDate++;
                        }
                    }
                })
                vm.accountStatusItems = data;
                vm.isReady = true;
            }

            vm.formatearNumero = function (nStr) {

                var x = nStr.split('.');
                var x1 = x[0];
                var x2 = x.length > 1 ? ',' + x[1] : '';
                var rgx = /(\d+)(\d{3})/;
                while (rgx.test(x1)) {
                    x1 = x1.replace(rgx, '$1' + ',' + '$2');
                }
                return x1 + x2;
            }

            vm.updatePicker = function () {
                vm.picker1 = {
                    datepickerOptions: {
                        maxDate: vm.dates.final_time,
                        enableTime: false,
                        showWeeks: false,
                    }
                };
                vm.picker2 = {
                    datepickerOptions: {
                        minDate: vm.dates.initial_time,
                        enableTime: false,
                        showWeeks: false,
                    }
                }
            }
            vm.datePickerOpenStatus.initialtime = false;
            vm.datePickerOpenStatus.finaltime = false;

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }
})();
