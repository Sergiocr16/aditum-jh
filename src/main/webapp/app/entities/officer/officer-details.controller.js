(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDetailsController', OfficerDetailsController);

    OfficerDetailsController.$inject = ['$rootScope','$state','Principal','$timeout', 'CommonMethods','$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company','Modal'];

    function OfficerDetailsController ($rootScope,$state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company,Modal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.officer = entity;
        $rootScope.active = "officers";
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        $rootScope.mainTitle = 'Detalle de oficial';
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });

        vm.edad = moment(new Date()).format('YYYY') - moment(vm.officer.fechanacimiento).format("YYYY");
        vm.isReady = true;
        $("#officerInformation").fadeIn(300);
        vm.title = vm.officer.name + " " + vm.officer.lastname + " " + vm.officer.secondlastname;
        var unsubscribe = $rootScope.$on('aditumApp:officerUpdate', function(event, result) {
            vm.officer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
