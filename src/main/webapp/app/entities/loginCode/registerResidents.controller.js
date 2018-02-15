(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('LoginCodeResidentsController', LoginCodeResidentsController);

    LoginCodeResidentsController.$inject = ['Resident','WSResident','$localStorage','$scope','$rootScope', '$state','Principal', '$timeout', 'Auth','MultiCompany','House','Company','Brand','CommonMethods',"PadronElectoral"];

    function LoginCodeResidentsController (Resident,WSResident,$localStorage,$scope,$rootScope, $state,Principal, $timeout, Auth,MultiCompany, House,Company,Brand,CommonMethods,PadronElectoral) {
        var vm = this;
        angular.element(document).ready(function () {
            if($localStorage.allInformationFinished){
                $rootScope.companyUser = undefined;
                $state.go('home');
                $rootScope.menu = false;
                $rootScope.companyId = undefined;
                $rootScope.showLogin = true;
                $rootScope.inicieSesion = false;
            }
            $("#loginCodeResidentsPanel").fadeIn(1000);
        });
        vm.residents = [];
        vm.required = 1;
        vm.required2 = 1;
        vm.countSaved = 0;
        vm.residentsEmpty = false;
        vm.codeStatus = $localStorage.codeStatus;
        CommonMethods.validateLetters();
        CommonMethods.validateNumbers();
        CommonMethods.validateSpecialCharacters();
        CommonMethods.validateSpecialCharactersAndVocals();
        $( "#residentli" ).addClass( "active" );
        $( "#profileli" ).removeClass( "active" );
        $( "#homeli" ).removeClass( "active" );
        $( "#carli" ).removeClass( "active" );
        $( "#donerli" ).removeClass( "active" );

        loadHouse();
        vm.addResidentToList = function(){
            vm.arrayIsEmpty = false;
            var resident = {name:null,lastname:null,secondlastame:null,phonenumber:null,identificationnumber:null,email:null,isOwner:0,enabled:1,nacionality:"9",found:0,companyId:$localStorage.companyId,houseId:$localStorage.house.id, validIdentification:1,validPlateNumber:1,lockNames:true}
            vm.residents.push(resident);

        }
        function loadHouse(){
            House.getByLoginCode({
                loginCode: $state.params.loginCode
            }).$promise.then(onSuccessHouse);

        }
        function onSuccessHouse(data) {
            vm.house = data;

        }
        if($localStorage.residentsRegistrationFinished){
            vm.residentsRegistrationFinished = true;

        }

        if($localStorage.residentsLoginCode==undefined){
            vm.addResidentToList();
        }else{
            vm.residents = $localStorage.residentsLoginCode;
        }

        vm.deleteResidentFromList = function(index){
            vm.residents.splice(index,1)
        }
        vm.residentsInfoReady = function () {
            vm.countResidents = 0;
            $localStorage.residentsLoginCode =  vm.residents;
            if(vm.residents.length==1 && vm.residents[0].identificationnumber == "" || vm.residents.length==1 && vm.residents[0].identificationnumber == undefined || vm.residents.length==1 && vm.residents[0].identificationnumber == null){
                noResidentsConfirmation()

            }else{
                if(vm.validArray()==true){
                    residentsConfirmation()
                }


            }

        }
        function noResidentsConfirmation() {
            bootbox.confirm({
                message: '<h4>¿No se registró ninguna persona autorizada, desea continuar de igual forma?</h4>',
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
                        $localStorage.residentsRegistrationFinished = true;
                        $localStorage.codeStatus = 4;
                        $state.go('loginCodeCars');

                    }else{

                    }
                }
            });


        }

        function residentsConfirmation() {
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
                        angular.forEach(vm.residents,function(val,i){
                            validateIdNumber(val)
                        })

                    }else{

                    }
                }
            });

        }
        function validateIdNumber(val){
             Resident.getByCompanyAndIdentification({companyId:vm.house.companyId,identificationID:val.identificationnumber},alreadyExist,insertResident)

            function insertResident() {
                vm.countResidents++;
                if(vm.countResidents==vm.residents.length){

                    angular.forEach(vm.residents,function(resident,i){
                        vm.isSaving = true;
                        Resident.save(resident,onSaveSuccess, onSaveError);
                    })
                }


            }
            function alreadyExist(){
                toastr["error"]("La cédula ingresada ya existe.");
                vm.isSaving = false;
            }
        }


        function onSaveSuccess(result) {

            WSResident.sendActivity(result);
            $scope.$emit('aditumApp:residentUpdate', result);
            vm.countSaved++;
            if(vm.countSaved==vm.residents.length){
                $state.go('loginCodeCars');
                $localStorage.residentsRegistrationFinished = true;
                $localStorage.codeStatus = 4;
                vm.isSaving = false;

            }
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.unlockPersonNames = function(person) {
            if(person.nacionality=="15"){
                person.lockNames = false;
            } else {
                person.lockNames = true;
            }

        }

        // vm.verifyArrayIsEmpty = function(person){
        //     console.log(person.identificationnumber)
        //     if(vm.residents.length==1 && vm.residents[0].identificationnumber == "" || vm.residents[0].identificationnumber == undefined){
        //         vm.arrayIsEmpty = true;
        //     } else {
        //         vm.arrayIsEmpty = false;
        //     }
        // }
        vm.findInPadron = function(person){
            $localStorage.residentsLoginCode = vm.residents;
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

            angular.forEach(vm.residents,function(resident,i){
                resident.name = resident.name.toUpperCase()
                resident.lastname = resident.lastname.toUpperCase()
                resident.secondlastname = resident.secondlastname.toUpperCase()

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


            if(errorCedula==0 && nombreError==0 && errorCedLenght == 0){
                return true;
            }else{
                return false;
            }






        }



    }
})();
