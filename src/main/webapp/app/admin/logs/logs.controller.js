(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LogsController', LogsController);

    LogsController.$inject = ['LogsService','Principal'];

    function LogsController (LogsService,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.changeLevel = changeLevel;
        vm.loggers = LogsService.findAll();

        function changeLevel (name, level) {
            LogsService.changeLevel({name: name, level: level}, function () {
                vm.loggers = LogsService.findAll();
            });
        }
    }
})();
