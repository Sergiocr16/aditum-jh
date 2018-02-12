(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeResumeController', LoginCodeResumeController);

    LoginCodeResumeController.$inject = ['User','$localStorage','$rootScope','$state'];

    function LoginCodeResumeController (User,$localStorage,$rootScope,$state) {
        var vm = this;
        $( "#donerli" ).addClass( "active" );
        $( "#residentli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#carli" ).removeClass( "active" );
        $( "#donerli" ).removeClass( "active" );
        vm.user = {};
        vm.loginStringCount = 0;
        if($localStorage.profileInfo==undefined && $localStorage.house.codeStatus ==false){
        } else if($localStorage.profileInfo!==undefined){
            vm.profileInfo = $localStorage.profileInfo ;
        }

        if($localStorage.residentsLoginCode.length>=1){
            vm.residents = $localStorage.residentsLoginCode;

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

        function onSaveLoginError (error) {
            vm.isSaving = false;
            switch(error.data.login){
                case "emailexist":
                    toastr["error"]("El correo electrónico ingresado ya existe.");

                    break;
                case "userexist":
                    vm.user.login = generateLogin(1);
                    console.log(vm.user.login)
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
        function onSaveUser () {
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
                message: '<div class="text-center gray-font font-15" style=" line-height:2;"> <h1 class="font-20">El registro de los datos de su filial se han completado exitosamente.</h1> <h1 class="font-17">Te hemos enviado un correo electrónico al correo <span id="profileEmail"></span> con los credenciales para iniciar sesión en tu cuenta desde nuestra aplicación.</h1></div>',
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
