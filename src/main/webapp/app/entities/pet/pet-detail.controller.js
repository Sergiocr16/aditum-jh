(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('PetDetailController', PetDetailController);

    PetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pet', 'House', 'Company', 'Modal','$state','CommonMethods'];

    function PetDetailController($scope, $rootScope, $stateParams, previousState, entity, Pet, House, Company, Modal,$state,CommonMethods) {
        var vm = this;

        vm.pet = entity;
        vm.previousState = previousState.name;
        $rootScope.mainTitle = "Detalle de mascota";
        var unsubscribe = $rootScope.$on('aditumApp:petUpdate', function (event, result) {
            vm.pet = result;
        });
        Modal.enteringDetail();
        $scope.$on("$destroy", function () {
            Modal.leavingDetail();
        });
        vm.isReady = true;
        $scope.$on('$destroy', unsubscribe);
        vm.editPet = function (pet) {
            var encryptedId = CommonMethods.encryptIdUrl(pet.id)
            $state.go('pet.edit', {
                id: encryptedId
            })
        };
        vm.deletePet = function (pet) {
            Modal.confirmDialog("¿Está seguro que desea eliminar a la mascota?", "Una vez eliminada no podrá recuperar los datos",
                function () {
                    Modal.showLoadingBar();
                    Pet.delete({
                        id: pet.id
                    }, function () {
                        Modal.toast("Se ha eliminado la mascota correctamente.");
                        Modal.hideLoadingBar();
                        $rootScope.back();
                    });
                });
        }
    }
})();
