(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('OfficerDialogController', OfficerDialogController);

    OfficerDialogController.$inject = ['$rootScope','$state','Principal','$timeout', 'CommonMethods','$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Officer', 'User', 'Company','SaveImageCloudinary'];

    function OfficerDialogController ($rootScope,$state, Principal, $timeout, CommonMethods, $scope, $stateParams, $q, DataUtils, entity, Officer, User, Company, SaveImageCloudinary) {
        var vm = this;
        var fileImage = null;
        vm.isAuthenticated = Principal.isAuthenticated;
         vm.required = 1;
        vm.officer = entity;
         if(vm.officer.image_url==undefined){
            vm.officer.image_url = null;
          }
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.user = entity;
        moment.locale('es');

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        var indentification = vm.officer.identificationnumber;
        CommonMethods.validateLetters();
        vm.loginStringCount = 0;
        CommonMethods.validateNumbers();
        vm.birthdate = new Date().setYear(new Date().getYear()-18);
        $rootScope.active = "officers";
        if(vm.officer.id !== null){
            vm.title = "Editar oficial";
            vm.button = "Editar";
            vm.birthdate = new Date(vm.officer.fechanacimiento);
             vm.birthdateToShow = moment(vm.birthdate).format("D MMM YYYY");
        } else{
            vm.title = "Registrar oficial";
            vm.button = "Registrar";
        }

        setTimeout(function() {
            $("#edit_officer_form").fadeIn(300);
        }, 200)


        function save () {
            vm.isSaving = true;
             CommonMethods.waitingMessage();
            if (vm.officer.id !== null) {
              if(indentification!==vm.officer.identificationnumber){
               Officer.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.officer.identificationnumber},alreadyExist,allClear)
                function alreadyExist(data){
                 toastr["error"]("La cédula ingresada ya existe.");
                  bootbox.hideAll();
               }
                 function allClear(data){
                    updateOfficer();
                 }
              } else {
                updateOfficer();
              }
            } else {
                Officer.getByCompanyAndIdentification({companyId:$rootScope.companyId,identificationID:vm.officer.identificationnumber},alreadyExist,allClear)
                    function alreadyExist(data){
                    toastr["error"]("La cédula ingresada ya existe.");
                     bootbox.hideAll();
                }
                 function allClear(data){
                        vm.officer.name = CommonMethods.capitalizeFirstLetter(vm.officer.name);
                        vm.officer.lastname = CommonMethods.capitalizeFirstLetter(vm.officer.lastname);
                        vm.officer.secondlastname = CommonMethods.capitalizeFirstLetter(vm.officer.secondlastname);
                        Principal.identity().then(function(account){
                        if(account.authorities[0]!="ROLE_RH"){
                         vm.officer.companyId = $rootScope.companyId;
                        }
                        insertOfficer();
                        })

                 }
            }
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function insertOfficer(){

            vm.officer.userId = 1;
            vm.officer.inservice = 0;
            vm.officer.enable = true;
            vm.officer.fechanacimiento = vm.birthdate;

            vm.imageUser = {user: "a"};
           if(fileImage!==null){
                SaveImageCloudinary
                .save(fileImage, vm.imageUser)
                .then(onSaveImageSuccess, onSaveError, onNotify);
            function onNotify(info) {
                   vm.progress = Math.round((info.loaded / info.total) * 100);
             }
            function onSaveImageSuccess(data) {
                  vm.officer.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                  Officer.save(vm.officer, onSaveSuccess, onSaveError);
              function onSaveSuccess (result) {
                  vm.isSaving = false;
                    Principal.identity().then(function(account){
                        if(account.authorities[0]=="ROLE_RH"){
                         $state.go('officer-rh');
                        }else{
                         $state.go('officer');
                        }
                            bootbox.hideAll();
                            toastr["success"]("Se ha registrado el oficial correctamente.");
                    })


              }
            }
        }else{
         Officer.save(vm.officer, onSaveSuccess, onSaveError);
         function onSaveSuccess (result) {
             vm.isSaving = false;
            Principal.identity().then(function(account){
                if(account.authorities[0]=="ROLE_RH"){
                 $state.go('officer-rh');
                }else{
                 $state.go('officer');
                }
                    bootbox.hideAll();
                    toastr["success"]("Se ha registrado el oficial correctamente.");
            })

         }
        }

        }
        function updateOfficer(){
         vm.officer.fechanacimiento = vm.birthdate;
         console.log(vm.officer)
            vm.imageUser = {user: vm.officer.identificationnumber};
            if(fileImage!==null){
                 SaveImageCloudinary
                .save(fileImage, vm.imageUser)
                .then(onSaveImageSuccess, onSaveError, onNotify);
                function onNotify(info) {
                   vm.progress = Math.round((info.loaded / info.total) * 100);
                 }
                function onSaveImageSuccess(data) {
                  vm.officer.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";

                  Officer.update(vm.officer, onUpdateSuccess, onSaveError);
                }
            }else{
               Officer.update(vm.officer, onUpdateSuccess, onSaveError);
            }

        }
        function onUpdateSuccess (result) {
                vm.isSaving = false;
               Principal.identity().then(function(account){
                   if(account.authorities[0]=="ROLE_RH"){
                    $state.go('officer-rh');
                   }else{
                    $state.go('officer');
                   }
                       bootbox.hideAll();
                       toastr["success"]("Se ha editado el oficial correctamente.");
               })
            }
        function onSaveSuccess (result) {
               Principal.identity().then(function(account){
                   if(account.authorities[0]=="ROLE_RH"){
                    $state.go('officer-rh');
                   }else{
                    $state.go('officer');
                   }

               })
            vm.isSaving = false;
        }

            vm.picker = {
                date: new Date().setYear(new Date().getYear()-18),
                datepickerOptions: {
                    maxDate: new Date().setYear(new Date().getYear()-18),
                    date: new Date().setYear(new Date().getYear()-18),
                    enableTime: false,
                    showWeeks: false,
                }
            };

      vm.setImage = function ($file) {
                if ($file && $file.$error === 'pattern') {
                    return;
                }
                if ($file) {
                    DataUtils.toBase64($file, function(base64Data) {
                        $scope.$apply(function() {
                            vm.displayImage = base64Data;
                            vm.displayImageType = $file.type;
                        });
                    });
                    fileImage = $file;
                }
       };
        vm.datePickerOpenStatus.birthdate = false;
         function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
