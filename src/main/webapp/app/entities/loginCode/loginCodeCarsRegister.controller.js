(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeCarsRegisterController', LoginCodeCarsRegisterController);

    LoginCodeCarsRegisterController.$inject = ['WSVehicle','Vehicule','$localStorage','$scope','$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand','CommonMethods',"PadronElectoral"];

    function LoginCodeCarsRegisterController  (WSVehicle,Vehicule,$localStorage,$scope,$rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand,CommonMethods,PadronElectoral) {
        angular.element(document).ready(function () {
            if($localStorage.allInformationFinished){
                $rootScope.companyUser = undefined;
                $state.go('home');
                $rootScope.menu = false;
                $rootScope.companyId = undefined;
                $rootScope.showLogin = true;
                $rootScope.inicieSesion = false;
            }
            $("#loginCodeVehiculesPanel").fadeIn(1000);
        });
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.vehicules = [];
        vm.required = 1;
        vm.required2 = 1;
        vm.countSaved = 0;
        vm.countVehicules = 0;
        vm.codeStatus = $localStorage.codeStatus;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        $( "#carli" ).addClass( "active" );
        $( "#profileli" ).removeClass( "active" );
        $( "#residentli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#donerli" ).removeClass( "active" );

        loadHouse();
        if($localStorage.vehiculesRegistrationFinished){
            vm.vehiculesRegistrationFinished = true;

        }
        function loadHouse(){
                     var id = CommonMethods.decryptIdUrl($state.params.loginCode)

            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;
            if(vm.house.codeStatus==3){
                vm.house.codeStatus=4;
                House.update(vm.house);
            }
        }

        Brand.query({}, onSuccessBrand);
        function onSuccessBrand(brands){

            vm.brands = brands;
            // angular.forEach(vm.brands,function(brand,i){
            //     if(brand.brand===vm.vehicule.brand){
            //         vm.vehicule.brand = brand;
            //     }
            // })

        }
        vm.addVehiculeToList = function(){
            var vehicule = {licenseplate:null,brand:null,color:"#ffff",enabled:true,type:null,companyId:$localStorage.companyId,houseId:$localStorage.house.id}
            vm.vehicules.push(vehicule);
            setTimeout(function(){
                $scope.$apply(function(){
                    $('.colorpicker-default').colorpicker({
                        format: 'hex'
                    });
                })
            },100)
        }

        if($localStorage.vehiculesLoginCode==undefined){
            vm.addVehiculeToList();
        }else{
            vm.vehicules = $localStorage.vehiculesLoginCode;

        }
        vm.saveInfo = function (vehicule) {
            $localStorage.vehiculesLoginCode = vm.vehicules;
            vm.validate(vehicule);
        }
        vm.deleteVehiculeFromList = function(index){
            vm.vehicules.splice(index,1)
        }
        vm.submitColor = function(vehicule,index) {
            setTimeout(function(){
                $scope.$apply(function(){
                    vehicule.color = $('#'+index).css('background-color');
                    $('#'+index+'btn').css('background-color',vehicule.color);
                })
            },100)
        }


        vm.vehiculesInfoReady = function () {
            vm.countVehicules = 0;
            $localStorage.vehiculesLoginCode = vm.vehicules;

            if(vm.vehicules.length==1 && vm.vehicules[0].licenseplate == "" || vm.vehicules.length==1 && vm.vehicules[0].licenseplate == undefined || vm.vehicules.length==1 && vm.vehicules[0].licenseplate == null){
                noVehiculesConfirmation()

            }else{

                if(vm.validArray()==true){
                    vehiculesConfirmation();


                }
            }
        }
        function noVehiculesConfirmation() {
            bootbox.confirm({
                message: '<h4>¿No se registró ningun vehículo autorizado, desea continuar de igual forma?</h4>',
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {

                    if (result) {
                        $localStorage.vehiculesRegistrationFinished = true;
                        $localStorage.codeStatus = 5;
                        $state.go('loginCodeResume');

                    }else{

                    }
                }
            });


        }

        function vehiculesConfirmation() {
            bootbox.confirm({
                message: '<h4>¿Deseas confirmar el registro de esta información?</h4>',
                buttons: {
                    confirm: {
                        label: 'Aceptar',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'Cancelar',
                        className: 'btn-danger'
                    }
                },
                callback: function(result) {

                    if (result) {
                        angular.forEach(vm.vehicules,function(val,i){

                            Vehicule.getByCompanyAndPlate({companyId: vm.house.companyId,licensePlate:val.licenseplate},alreadyExist,insertVehiculeCount)
                            function alreadyExist(){
                                toastr["error"]("La placa ingresada ya existe.");
                            }
                            function insertVehiculeCount(){
                                vm.countVehicules++;
                                if(vm.countVehicules==vm.vehicules.length){

                                    angular.forEach(vm.vehicules,function(vehicule,i){
                                        vehicule.brand = vehicule.brand.brand
                                        vehicule.enabled = 1;
                                        vm.isSaving = true;
                                        Vehicule.save(vehicule,onSaveSuccess, onSaveError);
                                    })
                                }
                            }

                        })

                    }else{

                    }
                }
            });

        }

        function onSaveSuccess(result) {
            WSVehicle.sendActivity(result);
            $scope.$emit('aditumApp:vehiculeUpdate', result);
            vm.countSaved++;
            if(vm.countSaved==vm.vehicules.length){
                $state.go('loginCodeResume');
                $localStorage.vehiculesRegistrationFinished = true;
                $localStorage.codeStatus = 5;
                vm.isSaving = false;
            }
        }

        function onSaveError() {
            vm.isSaving = false;
        }
        function haswhiteCedula(s){
            return /\s/g.test(s);
        }


        vm.validate = function(car){

            var invalidLic = false;
            if(hasCaracterEspecial(car.licenseplate || haswhiteCedula(car.licenseplate))){
                car.validLicense = 0;
                invalidLic = true;
            }
            else{
                car.licenseplate = car.licenseplate.replace(/\s/g,'')
                car.validLicense = 1;
            }

            return {errorPlaca:invalidLic}
        }
        function hasCaracterEspecial(s){
            var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿","#","!"]
            var invalido = 0;
            angular.forEach(caracteres,function(val,index){
                if (s!=undefined){
                    for(var i=0;i<s.length;i++){
                        if(s.charAt(i)==val){
                            invalido++;
                        }
                    }
                }
            })
            if(invalido==0){
                return false;
            }else{
                return true;
            }
        }
        vm.validArray = function(){
            var errorPlaca = 0;

            angular.forEach(vm.vehicules,function(car,i){
                var carValidation = vm.validate(car)
                if(carValidation.errorPlaca){
                    errorPlaca++;
                }
            })


            if(errorPlaca>0){

                toastr["error"]("No puede ingresar ningún caracter especial o espacio en blanco en la placa.");

            }


            if(errorPlaca==0){
                return true;
            }else{
                return false;
            }






        }

    }
})();
