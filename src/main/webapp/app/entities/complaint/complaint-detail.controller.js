(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDetailController', ComplaintDetailController);

    ComplaintDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Complaint', 'House'];

    function ComplaintDetailController($scope, $rootScope, $stateParams, previousState, entity, Complaint, House) {
        var vm = this;

        vm.complaint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:complaintUpdate', function(event, result) {
            vm.complaint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
