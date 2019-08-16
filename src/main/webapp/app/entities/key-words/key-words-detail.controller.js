(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyWordsDetailController', KeyWordsDetailController);

    KeyWordsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'KeyWords'];

    function KeyWordsDetailController($scope, $rootScope, $stateParams, previousState, entity, KeyWords) {
        var vm = this;

        vm.keyWords = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:keyWordsUpdate', function(event, result) {
            vm.keyWords = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
