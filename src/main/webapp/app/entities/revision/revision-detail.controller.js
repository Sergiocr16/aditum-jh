(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('RevisionDetailController', RevisionDetailController);

    RevisionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Revision', 'RevisionTask', 'Company'];

    function RevisionDetailController($scope, $rootScope, $stateParams, previousState, entity, Revision, RevisionTask, Company) {
        var vm = this;

        vm.revision = entity;
        vm.previousState = previousState.name;
        console.log(vm.revision);
        vm.month = moment(vm.revision.executionDate).format("MMMM");
        var unsubscribe = $rootScope.$on('aditumApp:revisionUpdate', function (event, result) {
            vm.revision = result;
        });
        vm.categories = [{
            name: "Limpieza",
            type: 1,
            tasks:[]
        },{
            name: "Infraestructura",
            type: 2,
            tasks:[]
        },{
            name: "Seguridad",
            type: 3,
            tasks:[]
        }];
        formatCategories(vm.revision.revisionTasks);
        vm.weekOfMonth = function (d) {
            var m = moment(d);
            return m.week();
        };

        function formatCategories(array){
            for (var i = 0; i < vm.categories.length; i++) {
                for (var j = 0; j < array.length; j++) {
                    if(array[j].category == vm.categories[i].type){
                        vm.categories.tasks.push(array[j])
                    }
                }
            }
        }

        $scope.$on('$destroy', unsubscribe);
    }
})();
