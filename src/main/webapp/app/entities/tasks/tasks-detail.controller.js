(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('TasksDetailController', TasksDetailController);

    TasksDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tasks', 'Modal'];

    function TasksDetailController($scope, $rootScope, $stateParams, previousState, entity, Tasks, Modal) {
        var vm = this;

        vm.tasks = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:tasksUpdate', function(event, result) {
            vm.tasks = result;
        });
        vm.isReady = true;
        $scope.$on('$destroy', unsubscribe);

        $scope.$on('$destroy', unsubscribe);
        $rootScope.mainTitle = "Detalle de la tarea";
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
    }
})();
