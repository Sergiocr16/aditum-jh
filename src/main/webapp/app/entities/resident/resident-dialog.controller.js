(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ResidentDialogController', ResidentDialogController);

    ResidentDialogController.$inject = ['$timeout','$scope', '$rootScope', '$stateParams', 'CommonMethods','previousState', 'DataUtils','$q', 'entity', 'Resident', 'User', 'Company', 'House','Principal'];

    function ResidentDialogController($timeout,$scope, $rootScope, $stateParams, CommonMethods, previousState, DataUtils, $q,entity, Resident, User, Company, House,Principal) {
        var vm = this;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.resident = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.companies = Company.query();
        vm.title = "Registrar residente";
        vm.button = "Registrar";
        CommonMethods.validateLetters();
         CommonMethods.validateNumbers();
        House.query({},onSuccessHouses);
        function onSuccessHouses(data, headers) {
            vm.houses = data;
             setTimeout(function() {
                $("#edit_resident_form").fadeIn(600);
             }, 200)
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resident.id !== null) {
                Resident.update(vm.resident, onSaveSuccess, onSaveError);
            } else {
            console.log(vm.resident.houseId);
//            Principal.identity().then(function()){}

                Resident.save(vm.resident, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:residentUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, resident) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resident.image = base64Data;
                        resident.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();





//(function() {
//    'use strict';
//
//    angular
//        .module('aditumApp')
//        .controller('ResidentDialogController', ResidentDialogController);
//
//    ResidentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Resident', 'User', 'Company', 'House'];
//
//    function ResidentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Resident, User, Company, House) {
//        var vm = this;
//
//        vm.resident = entity;
//        vm.clear = clear;
//        vm.byteSize = DataUtils.byteSize;
//        vm.openFile = DataUtils.openFile;
//        vm.save = save;
//        vm.users = User.query();
//        vm.companies = Company.query();
//        vm.houses = House.query();
//
//        $timeout(function (){
//            angular.element('.form-group:eq(1)>input').focus();
//        });
//
//        function clear () {
//            $uibModalInstance.dismiss('cancel');
//        }
//
//        function save () {
//            vm.isSaving = true;
//            if (vm.resident.id !== null) {
//                Resident.update(vm.resident, onSaveSuccess, onSaveError);
//            } else {
//                Resident.save(vm.resident, onSaveSuccess, onSaveError);
//            }
//        }
//
//        function onSaveSuccess (result) {
//            $scope.$emit('aditumApp:residentUpdate', result);
//            $uibModalInstance.close(result);
//            vm.isSaving = false;
//        }
//
//        function onSaveError () {
//            vm.isSaving = false;
//        }
//
//
//        vm.setImage = function ($file, resident) {
//            if ($file && $file.$error === 'pattern') {
//                return;
//            }
//            if ($file) {
//                DataUtils.toBase64($file, function(base64Data) {
//                    $scope.$apply(function() {
//                        resident.image = base64Data;
//                        resident.imageContentType = $file.type;
//                    });
//                });
//            }
//        };
//
//    }
//})();
