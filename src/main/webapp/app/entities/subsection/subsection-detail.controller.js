(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('SubsectionDetailController', SubsectionDetailController);

    SubsectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Subsection', 'Article'];

    function SubsectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Subsection, Article) {
        var vm = this;

        vm.subsection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:subsectionUpdate', function(event, result) {
            vm.subsection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
