(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementsController', AnnouncementsController);

    AnnouncementsController.$inject = ['$rootScope','$state'];

    function AnnouncementsController($rootScope,$state) {
        var vm = this;
        $rootScope.active = 'announcements';
        $rootScope.mainTitle = 'Administrar noticias';
        vm.placeIn = $state.current == undefined ? 'selectedItem': $state.current.name;
    }
})();
