(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AdminInfoEditDialogController', AdminInfoEditDialogController);

    AdminInfoEditDialogController.$inject = ['$rootScope','$state', '$scope', '$stateParams', 'Principal', 'DataUtils', 'AdminInfo', 'User', 'Company','SaveImageCloudinary'];

    function AdminInfoEditDialogController ($rootScope,$state, $scope, $stateParams, Principal, DataUtils, AdminInfo, User, Company,SaveImageCloudinary) {
        var vm = this;
          var fileImage = null;

        setTimeout(function(){
        if($rootScope.companyUser.image_url==undefined){
              $rootScope.companyUser.image_url = null;
        }

        AdminInfo.get({id:$rootScope.companyUser.id},function(result){
        vm.adminInfo = result

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = updateAccount;
        vm.isAuthenticated = Principal.isAuthenticated;
        angular.element(document).ready(function () {
            $("#myInfo").fadeIn(600);

          User.getUserById({id: $rootScope.companyUser.userId},onSuccess);

             function onSuccess(user, headers) {
                 vm.user = user;
                 vm.adminInfo.email  = vm.user.email;
                 }
                        });
                         })
                 },500)
        function save () {
            vm.isSaving = true;
            if (vm.adminInfo.id !== null) {
                        vm.imageUser = {user: vm.adminInfo.id};
                          if(fileImage!==null){
                      SaveImageCloudinary
                                        .save(fileImage, vm.imageUser)
                                        .then(onSaveImageSuccess, onSaveError, onNotify);
                        function onNotify(info) {
                                    vm.progress = Math.round((info.loaded / info.total) * 100);
                         }
                        function onSaveImageSuccess(data) {
                        vm.adminInfo.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                              AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
                        }
                        }else{
                     AdminInfo.update(vm.adminInfo, onSaveSuccess, onSaveError);
                        }


            } else {
             vm.imageUser = {user: vm.adminInfo.id};
                                      if(fileImage!==null){
                                  SaveImageCloudinary
                                                    .save(fileImage, vm.imageUser)
                                                    .then(onSaveImageSuccess, onSaveError, onNotify);
                                    function onNotify(info) {
                                                vm.progress = Math.round((info.loaded / info.total) * 100);
                                     }
                                    function onSaveImageSuccess(data) {
                                    vm.adminInfo.image_url= "https://res.cloudinary.com/aditum/image/upload/v1501920877/"+data.imageUrl+".jpg";
                                            AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);
                                    }
                                    }else{
                                  AdminInfo.save(vm.adminInfo, onSaveSuccess, onSaveError);
                                    }

            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('aditumApp:adminInfoUpdate', result);

            toastr["success"]("Se ha editado tu información correctamente.");
            console.log(result)
          $rootScope.companyUser = result;
          $rootScope.companyUser.image_url = vm.adminInfo.image_url;
           $state.go('dashboard',null, { reload: true });
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

    }
})();
