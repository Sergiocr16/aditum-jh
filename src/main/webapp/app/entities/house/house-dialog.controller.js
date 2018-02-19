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
        if(vm.house.isdesocupated!=null){
        vm.house.isdesocupated = vm.house.isdesocupated.toString()
        }else{
        vm.house.isdesocupated = "0";
        }
        vm.save = save;
        setTimeout(getConfiguration(),600);
        function getConfiguration(){
            CompanyConfiguration.getByCompanyId({companyId: 1}).$promise.then(onSuccessCompany, onError);
        }
        function onSuccessCompany (data){
           angular.forEach(data, function(configuration, key) {
               vm.companyConfiguration = configuration;
             });
              loadQuantities();
               setTimeout(function() {
                         $("#loadingIcon").fadeOut(300);
               }, 400)
                setTimeout(function() {
                    $("#edit_house_form").fadeIn('slow');
                },900 )



        }
        function onError () {
        console.log('sadfsadf'+data);
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
            House.query({ companyId: $rootScope.companyId}, onSuccess, onError);
        }
        function onSuccess(data) {
            vm.houseQuantity = data.length;

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
            vm.isSaving = true;
            if (vm.house.id !== null) {
                  CommonMethods.waitingMessage();
                  House.validateUpdate({houseId: vm.house.id,houseNumber: vm.house.housenumber, extension: vm.extension, companyId: $rootScope.companyId},onSuccessUp,onErrorUp)

            } else {
            console.log(vm.companyConfiguration);
                if(vm.companyConfiguration.quantityhouses <= vm.houseQuantity ){
                     toastr['error']("Ha excedido la cantidad de casas permitidas para registrar, contacte el encargado de soporte.")
                     bootbox.hideAll();
                } else {
                     CommonMethods.waitingMessage();
                     vm.house.companyId = $rootScope.companyId;
                     vm.house.desocupationinitialtime = new Date();
                     vm.house.desocupationfinaltime = new Date();
                     vm.house.loginCode = generateLoginCode();
                     vm.house.codeStatus = 0;
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
                console.log(vm.house);
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
