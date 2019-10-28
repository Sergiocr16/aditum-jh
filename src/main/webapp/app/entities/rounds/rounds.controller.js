(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RoundsController', RoundsController);

    RoundsController.$inject = ['Modal', '$rootScope', 'firebase', 'Rounds', '$state', 'CommonMethods'];

    function RoundsController(Modal, $rootScope, firebase, Rounds, $state, CommonMethods) {

        var vm = this;
        vm.isReady = false;
        $rootScope.active = "rounds";
        $rootScope.mainTitle = 'Rondas de oficiales';

        vm.rounds = [];
        vm.isReady = false;
        var initial_time = new Date();
        initial_time.setHours(0, 0, 0, 0);
        var final_time = new Date();
        final_time.setHours(23, 59, 0, 0);

        vm.dates = {
            initial_time: initial_time,
            final_time: final_time
        };
        vm.loadAll = function () {
            vm.isReady = false;
            Rounds.getAllByCompanyAndDates({
                companyId: 1,
                initialDate: vm.dates.initial_time,
                finalDate: vm.dates.final_time
            }, function (data) {
                vm.rounds = data;
                vm.isReady = true;
            })
        };
        vm.loadAll();
        vm.viewDetail = function (round) {
            var encryptedId = CommonMethods.encryptIdUrl(round.uid)
            $state.go('round-detail', {
                id: encryptedId
            })
        }
    }
})();
