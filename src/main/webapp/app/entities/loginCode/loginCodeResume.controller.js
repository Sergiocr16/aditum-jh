(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeResumeController', LoginCodeResumeController);

    LoginCodeResumeController.$inject = ['Resident','House','User','$localStorage','$rootScope','$state','CommonMethods'];

    function LoginCodeResumeController (Resident,House,User,$localStorage,$rootScope,$state,CommonMethods) {
        var vm = this;
        $("#loginCodeVehiculesPanel").fadeIn(1000);
        $( "#donerli" ).addClass( "active" );
        $( "#residentli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#carli" ).removeClass( "active" );
        $( "#donerli" ).removeClass( "active" );
        vm.user = {};

        loadHouse();
        vm.loginStringCount = 0;
        if($localStorage.profileInfo==undefined && $localStorage.house.codeStatus ==false){
        } else if($localStorage.profileInfo!==undefined){
            vm.profileInfo = $localStorage.profileInfo ;
            if(vm.profileInfo.type==1){
                vm.profileInfo.type = "Propietario"
            }else{
                vm.profileInfo.type = "Inquilino"
            }
        }

        if($localStorage.residentsLoginCode.length>=1){
            vm.residents = $localStorage.residentsLoginCode;
            angular.forEach(vm.residents,function(resident,i){

                if(resident.type==1){
                    resident.type = "Propietario"
                }else if(resident.type==2){
                    resident.type = "Inquilino"
                } else if(resident.type==3){
                    resident.type = "Autorizado para ingresar"
                }
            })
        }
        if($localStorage.vehiculesLoginCode.length>=1){
            vm.vehicules = $localStorage.vehiculesLoginCode;

        }
        if($localStorage.codeStatus == 5){
            createAccount();
        }
        function createAccount(){
            var authorities = ["ROLE_USER"];
            vm.user.firstName =  vm.profileInfo.name.toLowerCase();
            vm.user.lastName = vm.profileInfo.lastname.toLowerCase() + ' ' + vm.profileInfo.secondlastname.toLowerCase();
            vm.user.email = vm.profileInfo.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);

             User.save(vm.user, onSaveUser, onSaveLoginError);


        }
        function loadHouse(){
           var id = CommonMethods.decryptIdUrl($state.params.loginCode)

            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;
            if(vm.house.codeStatus==4){
                vm.house.codeStatus=5;
                House.update(vm.house);
            }
        }
        function onSaveLoginError (error) {
            vm.isSaving = false;
            switch(error.data.login){
                case "emailexist":
                    toastr["error"]("El correo electrónico ingresado ya existe.");

                    break;
                case "userexist":
                    vm.user.login = generateLogin(1);
                    User.save(vm.user, onSaveUser, onSaveLoginError);

                    break;
            }

        }

        vm.exitToLogin = function(){
            $localStorage.allInformationFinished = true;
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
            $localStorage.allInformationFinished = false;
            $localStorage.registrationProfileFinished=false;
            $localStorage.profileInfo = undefined;
            $localStorage.allInformationFinished = false;
            $localStorage.vehiculesLoginCode = undefined;
            $localStorage.codeStatus =0;
            $localStorage.residentsLoginCode = undefined;
            $localStorage.allInformationFinished = false;
        }
        function onSaveUser (data) {
            vm.profileInfo.userId = data.id
            Resident.update(vm.profileInfo);
            confirmInformationCompeleted();
            $localStorage.codeStatus = 6;
        }
        function generateLogin(config){
            function getCleanedString(cadena){
                // Definimos los caracteres que queremos eliminar
                var specialChars = "!@#$^&%*()+=-[]\/{}|:<>?,.";

                // Los eliminamos todos
                for (var i = 0; i < specialChars.length; i++) {
                    cadena= cadena.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
                }

                // Lo queremos devolver limpio en minusculas
                cadena = cadena.toLowerCase();

                // Quitamos espacios y los sustituimos por _ porque nos gusta mas asi
                cadena = cadena.replace(/ /g,"_");

                // Quitamos acentos y "ñ". Fijate en que va sin comillas el primer parametro
                cadena = cadena.replace(/á/gi,"a");
                cadena = cadena.replace(/é/gi,"e");
                cadena = cadena.replace(/í/gi,"i");
                cadena = cadena.replace(/ó/gi,"o");
                cadena = cadena.replace(/ú/gi,"u");
                cadena = cadena.replace(/ñ/gi,"n");
                return cadena;
            }
            var firstletterFirstName = vm.profileInfo.name.charAt(0);
            var firstletterSecondName = vm.profileInfo.secondlastname.charAt(0);
            if(config==1){
                vm.loginStringCount = vm.loginStringCount + 1;
                return getCleanedString(firstletterFirstName+vm.profileInfo.lastname+firstletterSecondName+vm.loginStringCount);
            }
            return getCleanedString(firstletterFirstName+vm.profileInfo.lastname+firstletterSecondName);
        }
        function confirmInformationCompeleted () {

            bootbox.confirm({
                message: '<div class="text-center gray-font font-15" style=" line-height:2;"> <h1 class="font-20">El registro de los datos de su filial se han completado exitosamente.</h1> <h1 class="font-17">Te hemos enviado un correo electrónico a <span id="profileEmail" class="bold"></span> con las credenciales para que puedas iniciar sesión en tu cuenta desde nuestra aplicación.</h1></div>',
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
                      bootbox.hideAll();
                    }else{


                    }
                }
            });
            document.getElementById("profileEmail").innerHTML = "" + vm.profileInfo.email;

        };

    }
})();
