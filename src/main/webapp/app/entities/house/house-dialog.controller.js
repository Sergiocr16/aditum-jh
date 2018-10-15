(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['CompanyConfiguration','CommonMethods','$state','$rootScope','Principal','$timeout', '$scope', '$stateParams', 'entity', 'House','WSHouse','Balance','AdministrationConfiguration','Modal','globalCompany'];

    function HouseDialogController (CompanyConfiguration,CommonMethods,$state,$rootScope, Principal,$timeout, $scope, $stateParams,  entity, House,WSHouse, Balance,AdministrationConfiguration,Modal,globalCompany) {
        var vm = this;
        $rootScope.mainTitle = vm.title;
        $rootScope.active = "houses";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.house = entity;
        vm.isReady = false;
        if(vm.house.due==null || vm.house.due==undefined){
              vm.house.due=0;
        }else{
            vm.house.due = parseInt(vm.house.due);
        }


        if(vm.house.squareMeters==null || vm.house.squareMeters==undefined){
              vm.house.squareMeters=0;
        }else{
            vm.house.squareMeters = parseInt(vm.house.squareMeters);
        }

        if(vm.house.isdesocupated!=null ){
             vm.house.isdesocupated = vm.house.isdesocupated.toString()
        }else{
             vm.house.isdesocupated = "0";
        }
        vm.save = save;
        getConfiguration();
        function getConfiguration(){
            CompanyConfiguration.getByCompanyId({companyId: globalCompany.getId()}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data){
           angular.forEach(data, function(configuration, key) {
               vm.companyConfiguration = configuration;
             });
              loadQuantities();

        }
        function onError () {
        }

        function generateLoginCode() {
          var text = "";
          var letters = "";
          var numbers = "";
          var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

          for (var i = 0; i < 4; i++)
            letters += possible.charAt(Math.floor(Math.random() * possible.length));

          numbers = Math.floor((Math.random() * 899) + 100);
          text = numbers + "" + letters  ;
          return text.toUpperCase();
        }


        function loadQuantities(){
            House.query({ companyId: globalCompany.getId()}, onSuccess, onError);
        }
        function onSuccess(data) {
            console.log('fds')
            vm.houseQuantity = data.length;
            vm.isReady = true;
        }
        if(vm.house.id !== null){
            vm.title = "Editar casa";
            vm.button = "Editar";

        } else{
          vm.title = "Registrar casa";
          vm.button = "Registrar";
        }



        function save () {

        if(vm.house.extension == undefined){
            vm.extension = 'noTengoExtensionCODE';
        }else{
        vm.extension = vm.house.extension;
        }
             if(vm.house.due==null || vm.house.due==undefined){
                vm.house.due=0;
                }
                if(vm.house.squareMeters==null || vm.house.squareMeters==undefined){
                vm.house.squareMeters=0;
                }
            vm.isSaving = true;
            if (vm.house.id !== null) {
                  Modal.showLoadingBar();
                  House.validateUpdate({houseId: vm.house.id,houseNumber: vm.house.housenumber, extension: vm.extension, companyId: globalCompany.getId()},onSuccessUp,onErrorUp)

            } else {
                if(vm.companyConfiguration.quantityhouses <= vm.houseQuantity ){
                    Modal.toast("Ha excedido la cantidad de casas permitidas para registrar, contacte el encargado de soporte.");
                    Modal.hideLoadingBar();
                } else {
                     Modal.showLoadingBar();
                     vm.house.companyId = globalCompany.getId();
                     vm.house.desocupationinitialtime = new Date();
                     vm.house.desocupationfinaltime = new Date();
                     vm.house.loginCode = generateLoginCode();
                     vm.house.codeStatus = 0;
                     House.validate({houseNumber: vm.house.housenumber, extension: vm.extension,companyId: globalCompany.getId()},onSuccess,onError)
                }
            }
            function onSuccessUp(data){
                Modal.hideLoadingBar();
                if(vm.house.id !== data.id){

                    Modal.toast("El número de casa o de extensión ingresado ya existe.");
                }else{
                    House.update(vm.house, onSaveSuccess, onSaveError);
                }
            }
            function onErrorUp(){
                House.update(vm.house, onSaveSuccess, onSaveError);
            }

            function onSuccess(data){
                Modal.hideLoadingBar();
                Modal.toast("El número de casa o de extensión ingresado ya existe.");

            }
            function onError(){
                House.save(vm.house, onSaveSuccess, onSaveError);

            }
        }

        function onSaveSuccess (result) {
            if(entity.id==null){
                var balance = {houseId:parseInt(result.id),extraordinary:0,commonAreas:0,maintenance:0};
                Balance.save(balance);
            }


            WSHouse.sendActivity(result);
            $state.go('house');
            Modal.hideLoadingBar();
               if(vm.house.id !== null){
                   Modal.toast("Se editó la casa correctamente");
                } else{
                   Modal.toast("Se registró la casa correctamente");

                }

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

    }
})();
