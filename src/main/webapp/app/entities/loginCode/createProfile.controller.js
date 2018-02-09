(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CreateProfileController', CreateProfileController);

    CreateProfileController.$inject = ['$localStorage','$scope','$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand','CommonMethods',"PadronElectoral"];

    function CreateProfileController ($localStorage,$scope,$rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand,CommonMethods,PadronElectoral) {
        var vm = this;
        vm.piregistered = 1;
        vm.disableButtons = true;
        vm.personInfoLock = true;
        $('a[title]').tooltip();
        angular.element(document).ready(function () {
            $('body').removeClass("gray");
            $rootScope.showLogin = false;
            $rootScope.menu = false;
            $rootScope.isInManual = true;
            if($localStorage.allInformationFinished){
                $rootScope.companyUser = undefined;
                $state.go('home');
                $rootScope.menu = false;
                $rootScope.companyId = undefined;
                $rootScope.showLogin = true;
                $rootScope.inicieSesion = false;
            }


        });
        vm.exitToLogin = function(){
            console.log('af')
            $rootScope.showLogin = true;

        }
        loadHouse();

        angular.element(document).ready(function () {
            ColorPicker.init();
        });

        function loadHouse(){
            House.getByLoginCode({
                loginCode: $state.params.loginCode
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;
            if(data.codeStatus ==false){
                $localStorage.codeStatus = 1;
            }
            loadCompany();

        }
        function loadCompany(){
            Company.get({
                id: vm.house.companyId
            }).$promise.then(onSuccessCompany);

        }

        function onSuccessCompany(data) {
            vm.company = data;
            Brand.query({}, onSuccessBrand);

            $localStorage.house = vm.house;
            $localStorage.companyId = vm.company.id;
        }
        function onSuccessBrand(brands){
            vm.brands = brands;


        }
        vm.beginConfiguration = function () {
            $localStorage.codeStatus = 2;
            $state.go('loginCodeprofile');
        }

        vm.changeState = function(view){
            switch(view){
                case 1:
                    $( "homeli" ).addClass( "active" );
                    $state.go('loginCodeWelcome');

                    break;
                case 2:
                    $( "profileli" ).addClass( "active" );
                    $state.go('loginCodeprofile');

                    // $("#home").fadeOut(0);
                    // $("#homeli").removeClass("active");
                    // $("#addCar").fadeOut(0);
                    // $("#carli").removeClass("active");
                    // $("#resident").fadeOut(0);
                    // $("#residentli").removeClass("active");
                    // $("#doner").fadeOut(0);
                    // $("#donerli").removeClass("active");
                    //
                    // $("#profileli").addClass( "active" );
                    // $("#profile").fadeIn(500);
                    break;
                case 3:
                    $state.go('loginCodeResidents');
                    break;
                case 4:
                    $state.go('loginCodeCars');
                    break;
                case 5:
                    $state.go('loginCodeResume');
                    break;
            }

        }




        vm.profileInfoReady = function () {

            var nombreError = 0;
            var errorCedula = 0;
            var errorCedLenght = 0;
                var residentValidation = vm.validate(vm.profileInfo)
                if(residentValidation.errorCedula){
                    errorCedula++;
                }
                if(residentValidation.errorNombreInvalido>0){
                    nombreError++;
                }
                if(residentValidation.errorCedulaCorta){
                    errorCedLenght++;
                }

                if(errorCedula>0){
                    toastr["error"]("No puede ingresar ningún caracter especial o espacio en blanco en la cédula.");

                }
                if(nombreError>0){
                    toastr["error"]("No puede ingresar ningún caracter especial en el nombre.");

                }
                if(errorCedLenght>0){
                    toastr["error"]("La cédula de costarricense debe tener 9 dígitos. ");
                }

                if(errorCedula==0 && nombreError==0 && errorCedLenght == 0){
                    vm.piregistered = 2;
                    vm.changeState(3);
                }
        }






    }
})();
