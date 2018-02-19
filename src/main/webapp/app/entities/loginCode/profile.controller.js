(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeProfileController', LoginCodeProfileController);

    LoginCodeProfileController.$inject = ['WSResident','User','entity','Resident','$localStorage','$scope','$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand','CommonMethods','PadronElectoral'];

    function LoginCodeProfileController (WSResident,User,entity,Resident,$localStorage,$scope,$rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand,CommonMethods,PadronElectoral) {
        angular.element(document).ready(function () {
            if($localStorage.allInformationFinished){
                $rootScope.companyUser = undefined;
                $state.go('home');
                $rootScope.menu = false;
                $rootScope.companyId = undefined;
                $rootScope.showLogin = true;
                $rootScope.inicieSesion = false;

            }
            $("#loginCodeProfilePanel").fadeIn(2000);
        });
        var vm = this;
        vm.required = 1;
        vm.isSaving=false;
        vm.user = {};
        var indentification;
        vm.loginStringCount = 0;
        vm.isAuthenticated = Principal.isAuthenticated;
        CommonMethods.validateLetters();
        vm.codeStatus = $localStorage.codeStatus;
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        loadHouse();
        $( "#profileli" ).addClass( "active" );
        $( "#residentli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#carli" ).removeClass( "active" );
        $( "#donerli" ).removeClass( "active" );
        if($localStorage.registrationProfileFinished){
            vm.registrationProfileFinished = true;
        }

        if($localStorage.profileInfo==undefined && $localStorage.codeStatus ==2){
            vm.profileInfo = {name:null,lastname:null,secondlastname:null,phonenumber:null,identificationnumber:null,email:null,isOwner:1,enabled:1,companyId:$localStorage.companyId,houseId:$localStorage.house.id,nacionality:"9",found:0,validIdentification:1,validPlateNumber:1,lockNames:true,}
        }else if($localStorage.profileInfo==undefined && $localStorage.codeStatus ==undefined){
        } else if($localStorage.profileInfo!==undefined){
            vm.profileInfo = $localStorage.profileInfo ;
            indentification = vm.profileInfo.identificationnumber;

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
                vm.isSaving = true;
                vm.confirmProfileRegistration();
            }
        }

        function loadHouse(){
    var id = CommonMethods.decryptIdUrl($state.params.loginCode)
            House.getByLoginCode({
                loginCode: id
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;
            if(vm.house.codeStatus==1){
                vm.house.codeStatus=2;
                House.update(vm.house);
            }
        }
        function validateIdNumber(){
            if (vm.house.codeStatus==false) {
                    Resident.getByCompanyAndIdentification({companyId:vm.house.companyId,identificationID:vm.profileInfo.identificationnumber},alreadyExist,insertResident)

            } else {
                if(indentification!==vm.profileInfo.identificationnumber){

                    Resident.getByCompanyAndIdentification({companyId:vm.house.companyId,identificationID:vm.profileInfo.identificationnumber},alreadyExist,updateResident)

                } else {
                    updateResident();
                }

            }


        }
        function alreadyExist(){
            toastr["error"]("La cédula ingresada ya existe.");
            vm.isSaving = false;
        }

            vm.confirmProfileRegistration = function() {

            bootbox.confirm({
             message: '<div class="text-center gray-font font-15"> <h1 class="font-20">Confirma tu información personal.</h1><h1 class="font-15">Cédula: <span class="bold font-15" id="profileID"></span></h1> <h1 class="font-15">Nombre: <span class="bold" id="profileName"></span></h1> <h1 class="font-15"><h1 class="font-15">Correo: <span class="bold font-19" id="profileEmail"></span></h1></div>',
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
                        validateIdNumber();

                    }else{
                        vm.isSaving = false;

                    }
                }
            });

                document.getElementById("profileID").innerHTML = "" + vm.profileInfo.identificationnumber;
                document.getElementById("profileName").innerHTML = "" + vm.profileInfo.name + " " + vm.profileInfo.lastname + " " + vm.profileInfo.secondlastname;
                document.getElementById("profileEmail").innerHTML = "" + vm.profileInfo.email;



            };



        vm.unlockPersonNames = function(person) {
            if(person.nacionality=="15"){
                person.lockNames = false;
            } else {
                person.lockNames = true;
            }

        }

        function insertResident(){
            vm.profileInfo.company_id=$localStorage.companyId;
            vm.profileInfo.name =  vm.profileInfo.name.toUpperCase()
            vm.profileInfo.lastname =  vm.profileInfo.lastname.toUpperCase()
            vm.profileInfo.secondlastname =  vm.profileInfo.secondlastname.toUpperCase()

            if(vm.profileInfo.identificationnumber!==undefined || vm.profileInfo.identificationnumber!== null){
                vm.profileInfo.identificationnumber = vm.profileInfo.identificationnumber.toUpperCase()
            }
            Resident.save(vm.profileInfo, onSaveSuccessProfile, onSaveError);

        }
        function updateResident(){
            indentification = vm.profileInfo.identificationnumber;
            vm.profileInfo.name =  vm.profileInfo.name.toUpperCase()
            vm.profileInfo.lastname =  vm.profileInfo.lastname.toUpperCase()
            vm.profileInfo.secondlastname =  vm.profileInfo.secondlastname.toUpperCase()
            if(vm.profileInfo.identificationnumber!==undefined || vm.profileInfo.identificationnumber!== null){
                vm.profileInfo.identificationnumber = vm.profileInfo.identificationnumber.toUpperCase()
            }
            Resident.update(vm.profileInfo, onSaveSuccessProfile, onSaveError);

        }
        function onSaveSuccessProfile (result) {
            WSResident.sendActivity(result);
            $localStorage.codeStatus = 3;
            $localStorage.registrationProfileFinished = true;
            vm.profileInfo.id = result.id;
            vm.isSaving = false;
            $state.go('loginCodeResidents');
        }


        function onSaveError () {
            vm.isSaving = false;
        }
        vm.findInPadron = function(person){
            $localStorage.profileInfo = vm.profileInfo;
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

    }
})();
