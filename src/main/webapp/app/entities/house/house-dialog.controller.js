(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['CompanyConfiguration','CommonMethods','$state','$rootScope','Principal','$timeout', '$scope', '$stateParams', 'entity', 'House','WSHouse'];

    function HouseDialogController (CompanyConfiguration,CommonMethods,$state,$rootScope, Principal,$timeout, $scope, $stateParams,  entity, House,WSHouse) {
        var vm = this;
        $rootScope.active = "houses";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.house = entity;
        vm.save = save;
        getConfiguration();
        function getConfiguration(){
            CompanyConfiguration.getByCompanyId({companyId: $rootScope.companyId}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data){
           angular.forEach(data, function(configuration, key) {
               vm.companyConfiguration = configuration;
             });
              loadQuantities();

        }
        function onError () {

        }


        function loadQuantities(){
            House.query({ companyId: $rootScope.companyId}, onSuccess, onError);
        }
        function onSuccess(data) {
            vm.houseQuantity = data.length;

        }
        if(vm.house.id !== null){
            vm.title = "Editar house";
            vm.button = "Editar";

        } else{
          vm.title = "Registrar casa";
          vm.button = "Registrar";
        }


             setTimeout(function() {
            $("#edit_house_form").fadeIn(600);
         }, 200)


        function save () {

        if(vm.house.extension == undefined){
            vm.extension = 'noTengoExtensionCODE';
        }else{
        vm.extension = vm.house.extension;
        }
            vm.isSaving = true;
            if (vm.house.id !== null) {
                  CommonMethods.waitingMessage();
            House.validateUpdate({houseId: vm.house.id,houseNumber: vm.house.housenumber, extension: vm.extension, companyId: $rootScope.companyId},onSuccessUp,onErrorUp)

            } else {
                if(vm.companyConfiguration.quantityhouses <= vm.houseQuantity ){
                      toastr['error']("Ha excedido la cantidad de casas permitidas para registrar, contacte el encargado de soporte.")
                       bootbox.hideAll();
                } else {
                CommonMethods.waitingMessage();
                vm.house.companyId = $rootScope.companyId;
                vm.house.isdesocupated = 0;
                 vm.house.desocupationinitialtime = new Date();
                 vm.house.desocupationfinaltime = new Date();
                 House.validate({houseNumber: vm.house.housenumber, extension: vm.extension,companyId: $rootScope.companyId},onSuccess,onError)
                }
            }
            function onSuccessUp(data){
                bootbox.hideAll();
                if(vm.house.id !== data.id){
                    toastr['error']("El número de casa o de extensión ingresado ya existe.")
                }else{
                    House.update(vm.house, onSaveSuccess, onSaveError);
                }
            }
            function onErrorUp(){
                House.update(vm.house, onSaveSuccess, onSaveError);
            }

            function onSuccess(data){
                bootbox.hideAll();
                toastr['error']("El número de casa o de extensión ingresado ya existe.")

            }
            function onError(){

                House.save(vm.house, onSaveSuccess, onSaveError);

            }
        }

        function onSaveSuccess (result) {

            WSHouse.sendActivity(result);
            $state.go('house');
            bootbox.hideAll();
               if(vm.house.id !== null){
                    toastr["success"]("Se editó la casa correctamente");
                } else{
                    toastr["success"]("Se registró la casa correctamente");
                }

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
