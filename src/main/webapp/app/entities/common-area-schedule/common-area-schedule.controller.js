(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaScheduleController', CommonAreaScheduleController);

    CommonAreaScheduleController.$inject = ['CommonAreaSchedule'];

    function CommonAreaScheduleController(CommonAreaSchedule) {

        var vm = this;

        vm.commonAreaSchedules = [];

        loadAll();

        function loadAll() {
            CommonAreaSchedule.query(function(result) {
                vm.commonAreaSchedules = result;
                vm.searchQuery = null;
            });
        }
    }
})();
