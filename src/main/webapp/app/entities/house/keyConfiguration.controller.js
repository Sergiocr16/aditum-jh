(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyConfigurationController', KeyConfigurationController);

    KeyConfigurationController.$inject = ['CommonMethods','$state','$rootScope','Principal', '$scope', '$stateParams', 'entity', 'House', 'Resident','WSHouse'];

    function KeyConfigurationController (CommonMethods,$state,$rootScope, Principal,$scope, $stateParams,  entity, House, Resident, WSHouse) {
        var vm = this;
        $rootScope.active = "keysConfiguration";
        vm.isAuthenticated = Principal.isAuthenticated;
     CommonMethods.validateSpecialCharacters();
        vm.save = save;
        setTimeout(function(){
                 House.get({ id: $rootScope.companyUser.houseId}).$promise.then(onSuccess);

                  function onSuccess (house) {
                     vm.house = house;
                        setTimeout(function() {
                                  $("#loadingIcon").fadeOut(300);
                        }, 400)
                         setTimeout(function() {
                             $("#all").fadeIn('slow');
                         },900 )
                    }
        },600)

//
        function save() {
        CommonMethods.waitingMessage();
            vm.isSaving = true;
                House.update(vm.house, onSaveSuccess, onSaveError);

        }

        function onSaveSuccess (result) {
            $state.go('residentByHouse');
              WSHouse.sendActivity(result);
            bootbox.hideAll();
             toastr["success"]("Se establecieron las claves de seguridad correctamente");

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();




//
//
//
//app.controller('keyConfigurationController', function($scope, $state, $rootScope, $window, $stateParams, residentsAccionsController, housesFunctions, commonMethods, residentsFunctions) {
//    $rootScope.active = "keysConguration";
//    commonMethods.validatePermisson(1);
//    commonMethods.validateSpecialCharacters();
//    residentsFunctions.get($rootScope.user.resident_id).success(function(data) {
//        housesFunctions.get(data.house_id).success(function(data) {
//            $scope.securityKey = data.securityKey;
//            $scope.emergencyKey = data.emergencyKey;
//            $("#loadingIcon").fadeOut(0);
//            setTimeout(function() {
//                $("#register_edit_form").fadeIn(300);
//            }, 200)
//        })
//    });
//    $scope.actionButton = function() {
//
//        if ($scope.securityKey == $scope.emergencyKey) {
//            toastr["error"]("Las claves no pueden ser iguales");
//        } else {
//            commonMethods.waitingMessage();
//            residentsFunctions.get($rootScope.user.resident_id).success(function(data) {
//                housesFunctions.update(data.house_id, {
//                    securityKey: $scope.securityKey,
//                    emergencyKey: $scope.emergencyKey
//                }).success(function() {
//                    bootbox.hideAll();
//                    $state.go('condominos');
//                    toastr["success"]("Se establecieron las claves de seguridad correctamente");
//
//
//                });
//            });
//        }
//    }
//})
