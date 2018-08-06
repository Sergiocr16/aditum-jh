(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementDetailController', AnnouncementDetailController);

    AnnouncementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Announcement', 'Company'];

    function AnnouncementDetailController($scope, $rootScope, $stateParams, previousState, entity, Announcement, Company) {
        var vm = this;

        vm.announcement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('aditumApp:announcementUpdate', function(event, result) {
            vm.announcement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
