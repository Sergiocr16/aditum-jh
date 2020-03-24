(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeResumeController', LoginCodeResumeController);

    LoginCodeResumeController.$inject = ['Modal','Resident','House','User','$localStorage','$rootScope','$state','CommonMethods'];

    function LoginCodeResumeController (Modal, Resident,House,User,$localStorage,$rootScope,$state,CommonMethods) {
        var vm = this;
        $("#loginCodeVehiculesPanel").fadeIn(1000);
        $( "#donerli" ).addClass( "active" );
        $( "#residentli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#carli" ).removeClass( "active" );
        vm.user = {};

        vm.loginStringCount = 0;
        vm.residents = $localStorage.residents;
        vm.vehicules = $localStorage.vehicules;
        vm.showAccountInformation = false;

        loadHouse();

        function loadHouse() {
            var id = CommonMethods.decryptIdUrl($state.params.loginCode);
            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;
            if(vm.house.codeStatus==3){
                createAccount();
            }else{

            }
        }
        function createAccount(opcion) {
            vm.resident = $localStorage.residentPrincipal;
            vm.opcion = opcion;
            if (vm.resident.type == 1) {
                var authorities = ["ROLE_OWNER", "ROLE_USER"];
            } else {
                var authorities = ["ROLE_USER"];
            }
            console.log(vm.resident)
            vm.user.firstName = vm.resident.name;
            vm.user.lastName = vm.resident.lastname + ' ' + vm.resident.secondlastname;
            vm.user.email = vm.resident.email;
            vm.user.activated = true;
            vm.user.authorities = authorities;
            vm.user.login = generateLogin(0);
            console.log(vm.user)
            User.save(vm.user, onSaveUser, onSaveLoginError);


        }
        function onSaveUser(result) {
            vm.resident.userId = result.id;
            console.log("aaaaaaaaaaaa")
            console.log(vm.resident)
            Resident.update(vm.resident, onUpdateSuccess, onSaveError);

        }
        function onSaveError(result) {
            Modal.toast("Un error inesperado sucedió.");
        }

        function onUpdateSuccess(result) {
            vm.house.codeStatus = 4;
            House.update(vm.house,function () {
                vm.showAccountInformation = true;
            });

        }
        vm.goHome = function () {

            $rootScope.companyUser = undefined;
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
            $state.go('home');

        }
        function generateLogin(config) {
            function getCleanedString(cadena) {
                // Definimos los caracteres que queremos eliminar
                var specialChars = "!@#$^&%*()+=-[]\/{}|:<>?,.";

                // Los eliminamos todos
                for (var i = 0; i < specialChars.length; i++) {
                    cadena = cadena.replace(new RegExp("\\" + specialChars[i], 'gi'), '');
                }

                // Lo queremos devolver limpio en minusculas
                cadena = cadena.toLowerCase();

                // Quitamos espacios y los sustituimos por _ porque nos gusta mas asi
                cadena = cadena.replace(/ /g, "_");

                // Quitamos acentos y "ñ". Fijate en que va sin comillas el primer parametro
                cadena = cadena.replace(/á/gi, "a");
                cadena = cadena.replace(/é/gi, "e");
                cadena = cadena.replace(/í/gi, "i");
                cadena = cadena.replace(/ó/gi, "o");
                cadena = cadena.replace(/ú/gi, "u");
                cadena = cadena.replace(/ñ/gi, "n");
                return cadena;
            }

            var firstletterFirstName = vm.resident.name.charAt(0);
            var firstletterSecondName = vm.resident.secondlastname.charAt(0);
            if (config == 1) {
                vm.loginStringCount = vm.loginStringCount + 1;
                return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName + vm.loginStringCount);
            }
            return getCleanedString(firstletterFirstName + vm.resident.lastname + firstletterSecondName);
        }



        function onSaveLoginError (error) {
            vm.isSaving = false;
            switch(error.data.login){
                case "emailexist":
                    $state.go('loginCodeResidents');
                    Modal.toast("El correo electrónico ingresado ya existe.");

                    break;
                case "userexist":
                    vm.user.login = generateLogin(1);
                    User.save(vm.user, onSaveUser, onSaveLoginError);

                    break;
            }

        }





    }
})();
