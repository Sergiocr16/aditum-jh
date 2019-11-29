(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LogsController', LogsController);

    LogsController.$inject = ['LogsService','Principal','$rootScope'];

    function LogsController (LogsService,Principal, $rootScope) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.changeLevel = changeLevel;
        vm.loggers = LogsService.findAll();
        $rootScope.active = "logs";
        function changeLevel (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                vm.loggers = LogsService.findAll();
            });
        }
    }
})();
