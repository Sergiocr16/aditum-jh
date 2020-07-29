(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ComplaintDialogController', ComplaintDialogController);

    ComplaintDialogController.$inject = ['AditumStorageService', '$timeout', '$scope', '$stateParams', 'Complaint', 'House', 'Company', 'Resident', '$rootScope', '$state', 'globalCompany', 'Modal'];

    function ComplaintDialogController(AditumStorageService, $timeout, $scope, $stateParams, Complaint, House, Company, Resident, $rootScope, $state, globalCompany, Modal) {
        var vm = this;
        $rootScope.mainTitle = "Registrar ticket";
        vm.isReady = false;
        vm.complaint = {complaintType: "Vigilancia", complaintCategory: 1};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        var file;
        vm.searchTermFilial;
        vm.clearSearchTermFilial = function () {
            vm.searchTermFilial = '';
        };
        vm.typingSearchTermFilial = function (ev) {
            ev.stopPropagation();
        }
        Modal.enteringForm(save);

        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });
        vm.loadResidentsByHouse = loadResidentsByHouse;

        loadAll();


        function clear() {
            history.back();
        }

        function loadResidentsByHouse(houseId) {
            vm.residents = [];
            Resident.findResidentesEnabledByHouseId({houseId: houseId},
                function (data) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].fullName = data[i].name + " " + data[i].lastname + " " + data[i].secondlastname;
                        vm.residents.push(data[i]);
                    }
                    console.log(vm.residents)
                }, function () {
                    Modal.toast("Ah ocurrido un error cargando los residentes de la filial.")
                })
        }

        function loadAll() {
            House.query({
                sort: sort(),
                companyId: globalCompany.getId()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.houses = data;
                vm.isReady = true;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function makeid(length,fileName) {
            var result           = '';
            var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            var charactersLength = characters.length;
            for ( var i = 0; i < length; i++ ) {
                result += characters.charAt(Math.floor(Math.random() * charactersLength));
            }
            return result+"."+fileName.split('.').pop();
        }
        function upload() {
            var uploadTask = AditumStorageService.ref().child(globalCompany.getId() + '/complaints/' + vm.complaint.houseId + '/' + makeid(10,file.name)).put(file);
            uploadTask.on('state_changed', function (snapshot) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                    })
                }, 1)
                switch (snapshot.state) {
                    case firebase.storage.TaskState.PAUSED: // or 'paused'
                        console.log('Upload is paused');
                        break;
                    case firebase.storage.TaskState.RUNNING: // or 'running'
                        console.log('Upload is running');
                        break;
                }
            }, function (error) {
                // Handle unsuccessful uploads
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    vm.complaint.fileUrl = downloadURL;
                    if (vm.complaint.id !== null) {
                        Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
                    } else {
                        Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
                    }
                });
            });
        }

        function save() {
            Modal.confirmDialog("¿Está seguro que desea registrar la queja o sugerencia?", "",
                function () {
                    vm.isSaving = true;
                    vm.complaint.creationDate = moment(new Date).format();
                    vm.complaint.companyId = globalCompany.getId();
                    vm.complaint.status = 1;
                    vm.complaint.deleted = 0;
                    Modal.showLoadingBar();
                    if (file) {
                        upload();
                    } else {
                        if (vm.complaint.id !== null) {
                            Complaint.update(vm.complaint, onSaveSuccess, onSaveError);
                        } else {
                            Complaint.save(vm.complaint, onSaveSuccess, onSaveError);
                        }
                    }

                });


        }

        function onSaveSuccess(result) {
            Modal.hideLoadingBar();
            Modal.toast("Se registró la queja o sugerencia exitosamente.")
            $state.go('complaint');
            vm.isSaving = false;
        }

        function onSaveError() {
            Modal.hideLoadingBar();
            vm.isSaving = false;
        }
        vm.options = {
            toolbar: [
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['view', ['fullscreen']],
                ['table', ['table']],
            ]
        }
        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.resolutionDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.setFile = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                vm.file = $file;
                vm.fileName = vm.file.name;
                file = $file;
            }
        };
    }
})();
