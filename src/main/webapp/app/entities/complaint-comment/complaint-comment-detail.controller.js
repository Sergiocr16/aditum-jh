(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintCommentDetailController', ComplaintCommentDetailController);

    ComplaintCommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ComplaintComment', 'Resident', 'AdminInfo', 'Complaint'];

    function ComplaintCommentDetailController($scope, $rootScope, $stateParams, previousState, entity, ComplaintComment, Resident, AdminInfo, Complaint) {
        var vm = this;

        vm.complaintComment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:complaintCommentUpdate', function(event, result) {
            vm.complaintComment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
