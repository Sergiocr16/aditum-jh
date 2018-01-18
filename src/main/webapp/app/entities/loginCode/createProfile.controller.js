(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CreateProfileController', CreateProfileController);

    CreateProfileController.$inject = ['$scope','$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand','CommonMethods',"PadronElectoral"];

    function CreateProfileController ($scope,$rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand,CommonMethods,PadronElectoral) {
        var vm = this;
        vm.piregistered = 1;
        vm.personInfoLock = true;
        vm.required = 1;
        vm.required2 = 1;
        vm.vehicules = [];
        vm.residents = [];

        $('a[title]').tooltip();
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        angular.element(document).ready(function () {
            $('body').removeClass("gray");
            $rootScope.showLogin = false;
            $rootScope.menu = false;
            $rootScope.isInManual = true;
        });
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
            vm.changeState(2);
        }
        function onSuccessBrand(brands){
            vm.brands = brands;

            vm.profileInfo = {name:null,lastname:null,secondlastame:null,phonenumber:null,identificationnumber:null,email:null,isOwner:false,company_id:vm.company.id,house_id:vm.house.id,nacionality:"9",found:0,validIdentification:1,validPlateNumber:1,lockNames:true}
            vm.addVehiculeToList();
            vm.addResidentToList();
        }
        vm.changeState = function(view){
            switch(view){
                case 1:
                    $("#profile").fadeOut(0);
                    $("#profileli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");
                    $("#doner").fadeOut(0);
                    $("#donerli").removeClass("active");

                    $("#home").fadeIn(500);
                    $("#homeli").addClass( "active" );


                    break;
                case 2:
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");
                    $("#doner").fadeOut(0);
                    $("#donerli").removeClass("active");

                    $("#profileli").addClass( "active" );
                    $("#profile").fadeIn(500);
                    break;
                case 3:
                    $("#profile").fadeOut(0);
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#profileli").removeClass("active");
                    $("#addCar").fadeOut(0);
                    $("#carli").removeClass("active");
                    $("#doner").fadeOut(0);
                    $("#donerli").removeClass("active");


                    $("#residentli").addClass( "active" );
                    $("#resident").fadeIn(500);
                    break;
                case 4:
                    $("#profile").fadeOut(0);
                    $("#profileli").removeClass("active");
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");
                    $("#doner").fadeOut(0);
                    $("#donerli").removeClass("active");

                    $("#carli").addClass( "active" );
                    $("#addCar").fadeIn(500);
                    break;
                case 5:
                    $("#profile").fadeOut(0);
                    $("#profileli").removeClass("active");
                    $("#home").fadeOut(0);
                    $("#homeli").removeClass("active");
                    $("#resident").fadeOut(0);
                    $("#residentli").removeClass("active");
                    $("#carli").removeClass( "active" );
                    $("#addCar").fadeOut(0);

                    $("#donerli").addClass( "active" );
                    $("#doner").fadeIn(500);
                    break;
            }

        }


        vm.addVehiculeToList = function(){
            var vehicule = {licensePlate:null,brand:null,color:"#ffff",enabled:true,type:null,house_id:vm.house.id,company_id:vm.company.id}
            vm.vehicules.push(vehicule);
            setTimeout(function(){
                $scope.$apply(function(){
                    $('.colorpicker-default').colorpicker({
                        format: 'hex'
                    });
                })
            },100)
        }
        vm.deleteVehiculeFromList = function(index){
            vm.vehicules.splice(index,1)
        }

        vm.addResidentToList = function(){
            var resident = {name:null,lastname:null,secondlastame:null,phonenumber:null,identificationnumber:null,email:null,isOwner:false,company_id:vm.company.id,house_id:vm.house.id,nacionality:"9",found:0,validIdentification:1,validPlateNumber:1,lockNames:true}
            vm.residents.push(resident);

        }
        vm.deleteResidentFromList = function(index){

               vm.residents.splice(index,1)

        }
        vm.residentsInfoReady = function () {

            if(vm.validArray()==true){
                vm.piregistered = 2;
                vm.changeState(4);
            }

        }
        vm.vehiculesInfoReady = function () {
            vm.changeState(5);

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

        vm.submitColor = function(vehicule,index) {
            setTimeout(function(){
                $scope.$apply(function(){
                    vehicule.color = $('#'+index).css('background-color');
                    $('#'+index+'btn').css('background-color',vehicule.color);
                })
            },100)
        }
        vm.unlockPersonNames = function(person) {
            if(person.nacionality=="15"){
                person.lockNames = false;
            } else {
                person.lockNames = true;
            }

        }

        vm.findInPadron = function(person){

            if (person == undefined && person.nacionality == "9") {
                $scope.$apply(function(){
                    person.lockNames=true;
                    person.name = "";
                    person.lastname = "";
                    person.secondlastname = "";
                })
            } else {
                if(hasCaracterEspecial(person.identificationnumber) || haswhiteCedula(person.identificationnumber)){
                        person.validIdentification = 0;
                    }else{
                        person.validIdentification = 1;
                    }
                    if(person.nacionality=="9" && person.identificationnumber != undefined){
                        if(person.identificationnumber.trim().length==9){
                            PadronElectoral.find(person.identificationnumber,function(info){
                                setTimeout(function(){
                                    $scope.$apply(function(){
                                        var nombre = info.nombre.split(",");
                                        person.name = nombre[0];
                                        person.lastname = nombre[1];
                                        person.secondlastname = nombre[2];
                                        person.found = 1;
                                    })
                                },100)
                            },function(){
                                $scope.$apply(function () {
                                    person.lockNames=false;

                                });

                                toastr["error"]("No se han encontrado datos en el padrón electoral, por favor ingresarlos manualmente")
                            })


                        }else{
                            setTimeout(function(){
                                $scope.$apply(function(){
                                    person.lockNames=true;
                                    person.name = "";
                                    person.lastname = "";
                                    person.secondlastname = "";
                                })
                            },100)
                        }
                    }else{
                        person.found = 0;
                    }
            }
        }

        function haswhiteCedula(s){
            return /\s/g.test(s);
        }
        vm.hasNumbersOrSpecial = function(s){
            var caracteres = ["1","2","3","4","5","6","7","8","9","0",",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿","#","!","}","{",'"',";","_","^"]
            var invalido = 0;
            angular.forEach(caracteres,function(val,index){
                if (s!=undefined){
                    for(var i=0;i<s.length;i++){
                        if(s.charAt(i).toUpperCase()==val.toUpperCase()){
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
        vm.hasLettersOrSpecial = function(s){
            var caracteres = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","´ñ","o","p","q","r","s","t","u","v","w","x","y","z",",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿","#","!","}","{",'"',";","_","^"]
            var invalido = 0;
            angular.forEach(caracteres,function(val,index){
                if (s!=undefined){
                    for(var i=0;i<s.length;i++){
                        if(s.charAt(i).toUpperCase()==val.toUpperCase()){
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
        function hasCaracterEspecial(s){
            var caracteres = [",",".","-","$","@","(",")","=","+","/",":","%","*","'","",">","<","?","¿","#"]
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
        vm.validate = function(person){
            var invalido = 0;
            var invalidCed = false;
            var invalidCedLength = false;
            if(hasCaracterEspecial(person.identificationnumber || haswhiteCedula(person.identificationnumber))){
                person.validIdentification = 0;
                invalidCed = true;
            }
            else if(person.nacionality == "9" &&  person.identificationnumber.length < 9){

                    person.validIdentification = 0;
                    invalidCedLength = true;
                }
            else{
                    person.identificationnumber = person.identificationnumber.replace(/\s/g,'')
                    person.validIdentification = 1;
                }

            if(person.name == undefined || person.lastname == undefined || person.secondlastname == undefined || person.name == "" || person.lastname == "" || person.secondlastname == ""){
                invalido++;
            }else if(hasCaracterEspecial(person.name)|| hasCaracterEspecial(person.lastname)|| hasCaracterEspecial(person.secondlastname)){
                invalido++;
            }else if(person.identificationnumber != undefined){

            }
            return {errorNombreInvalido:invalido,errorCedula:invalidCed,errorCedulaCorta:invalidCedLength}
        }

            vm.validArray = function(){
                var nombreError = 0;
                var errorCedula = 0;
                var errorCedLenght = 0;
                setTimeout(function(){
                        $scope.$apply(function(){
                            angular.forEach(vm.residents,function(resident,i){
                                resident.name = resident.name.toUpperCase()
                                resident.lastname = resident.lastname.toUpperCase()
                                resident.secondlastname = resident.secondlastname.toUpperCase()
                                if(resident.phonenumber==undefined || resident.phonenumber==""){
                                    resident.phonenumber='No se registró';
                                }
                                var residentValidation = vm.validate(resident)
                                if(residentValidation.errorCedula){
                                    errorCedula++;
                                }
                                if(residentValidation.errorNombreInvalido>0){
                                    nombreError++;
                                }
                                if(residentValidation.errorCedulaCorta){
                                    errorCedLenght++;
                                }
                            })
                            if(errorCedula>0){
                                toastr["error"]("No puede ingresar ningún caracter especial o espacio en blanco en la cédula.");

                            }
                            if(nombreError>0){
                                toastr["error"]("No puede ingresar ningún caracter especial en el nombre.");

                            }
                            if(errorCedLenght>0){
                                toastr["error"]("Si la nacionalidad es costarricense, debe ingresar el número de cédula igual que aparece en la cédula de identidad para obtener la información del padrón electoral de Costa Rica. Ejemplo: 10110111.");
                            }
                        })
                    }
                ,100)

            if(errorCedula==0 && nombreError==0 && errorCedLenght == 0){
                return true;
            }else{
                return false;
            }
        }



    }
})();
