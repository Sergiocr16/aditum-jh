(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('KeyConfigurationController', KeyConfigurationController);

    KeyConfigurationController.$inject = ['CommonMethods', '$state', '$rootScope', 'Principal', '$scope', '$stateParams', 'entity', 'House', 'Resident', 'WSHouse', 'companyUser', 'Modal'];

    function KeyConfigurationController(CommonMethods, $state, $rootScope, Principal, $scope, $stateParams, entity, House, Resident, WSHouse, companyUser, Modal) {
        var vm = this;
        $rootScope.active = "keysConfiguration";
        $rootScope.mainTitle= "Claves de seguridad";
        vm.isAuthenticated = Principal.isAuthenticated;
        CommonMethods.validateSpecialCharacters();
        vm.save = save;
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.isReady = false;
        House.get({id: companyUser.houseId}).$promise.then(onSuccess);

        function onSuccess(house) {
            vm.house = house;
            vm.isReady = true;
        }

//
        function save() {
            Modal.confirmDialog("¿Está seguro que desea cambiar sus claves de seguridad?","",function(){
                Modal.showLoadingBar();
                vm.isSaving = true;
                House.update(vm.house, onSaveSuccess, onSaveError);
            })
        }

        function onSaveSuccess(result) {
            WSHouse.sendActivity(result);
            Modal.hideLoadingBar();
            Modal.toast("Se establecieron las claves de seguridad correctamente");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();

