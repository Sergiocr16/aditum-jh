(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoDialogController', AdminInfoDialogController);

    AdminInfoDialogController.$inject = ['$rootScope','$state', '$scope', '$stateParams', 'Principal', 'DataUtils', 'AdminInfo', 'User', 'Company'];

    function AdminInfoDialogController ($rootScope,$state, $scope, $stateParams, Principal, DataUtils, AdminInfo, User, Company) {
        var vm = this;

        vm.adminInfo =$rootScope.companyUser;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = updateAccount;
        vm.isAuthenticated = Principal.isAuthenticated;
        angular.element(document).ready(function () {
            $("#myInfo").fadeIn(600);
        });

          User.getUserById({id: vm.adminInfo.userId},onSuccess);

             function onSuccess(user, headers) {
                 vm.user = user;
                 vm.adminInfo.email  = vm.user.email;
                 }
        function save () {
            vm.isSaving = true;
            if (vm.adminInfo.id !== null) {
                AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
            } else {
                AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:adminInfoUpdate', result);
            $state.go('home');
            toastr["success"]("Se ha editado tu información correctamente.");
         $rootScope.companyUser = result;
                        $rootScope.currentUserImage = result.image;
                        $rootScope.currentUserImageContentType = result.imageContentType;
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        function updateAccount(){
                 vm.user.id = vm.adminInfo.userId;
                 vm.user.activated = 1;
                 vm.user.firstName =  vm.adminInfo.name;
                 vm.user.lastName = vm.adminInfo.lastname + ' ' + vm.adminInfo.secondlastname;
                 vm.user.email = vm.adminInfo.email;
                 User.update(vm.user,onSuccessUser);
                 function onSuccessUser(data, headers) {
                    save();
                  }
              }



        vm.setImage = function ($file, adminInfo) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        adminInfo.image = base64Data;
                        adminInfo.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
