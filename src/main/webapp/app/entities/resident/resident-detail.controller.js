(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDetailController', ResidentDetailController);

    ResidentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House','Principal'];

    function ResidentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Resident, User, Company, House,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;

        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        if (vm.resident.isOwner == 1) {
            vm.authorizer = "SI";
        } else {
            vm.authorizer = "NO";
        }
        House.get({id:vm.resident.houseId},onSuccessHouses);
        function onSuccessHouses(house, headers) {
        console.log(house);
            vm.resident.houseId = house.housenumber;
            $("#residentInformation").fadeIn(300);
            if (house.securityKey == null) {
              vm.securitykey = "No definida"
          } else {
              vm.securitykey = house.securityKey;
          }
          if (house.emergencyKey == null) {
              vm.emergencykey = "No definida"
          } else {
              vm.emergencykey = house.emergencyKey;
          }
        }


        vm.title = vm.resident.name + " " + vm.resident.lastname + " " + vm.resident.secondlastname;
        var unsubscribe = $rootScope.$on('aditumApp:residentUpdate', function(event, result) {
            vm.resident = result;
        });
        $scope.$on('$destroy', unsubscribe);

//                    if (vm.resident.id !== null) {
//                        Resident.update(vm.resident, onSaveSuccess, onSaveError);
//                    } else {
    }
})();
