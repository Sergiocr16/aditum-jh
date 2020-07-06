(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDialogController', CommonAreaDialogController);

    CommonAreaDialogController.$inject = ['SaveImageCloudinary', '$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'CommonArea', 'CommonMethods', 'CommonAreaSchedule', '$state', '$rootScope', 'Principal', 'Modal', 'globalCompany', 'CompanyConfiguration'];

    function CommonAreaDialogController(SaveImageCloudinary, $timeout, $scope, $stateParams, DataUtils, entity, CommonArea, CommonMethods, CommonAreaSchedule, $state, $rootScope, Principal, Modal, globalCompany, CompanyConfiguration) {
        var vm = this;
        $rootScope.active = "reservationAdministration";
        vm.isAuthenticated = Principal.isAuthenticated;
        if (entity.pictureContentType == undefined) {
            entity.pictureContentType = null;
        }
        vm.commonArea = entity;
        vm.isReady = false;
        var fileImage = null;

        $rootScope.mainTitle = "Nueva área común";
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        var data = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        if (data.hasContability == 1) {
            vm.hasContability = true;
        } else {
            vm.hasContability = false;
        }
        vm.save = save;
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });
        vm.daysOfWeek = [{
            day: 'Lunes',
            selected: false,
            initialTime: "",
            finalTime: "",
            blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Martes',
            selected: false,
            initialTime: "",
            finalTime: "", blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Miercoles',
            selected: false,
            initialTime: "",
            finalTime: "",
            blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Jueves',
            selected: false,
            initialTime: "",
            finalTime: "", blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Viernes',
            selected: false,
            initialTime: "",
            finalTime: "",
            blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Sábado',
            selected: false,
            initialTime: "",
            finalTime: "", blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }, {
            day: 'Domingo',
            selected: false,
            initialTime: "",
            finalTime: "",
            blocks: [{selected: true, initialTime: "", finalTime: ""}]
        }];


        vm.bloques = [{value: "-1", hour: "Todo el día"}, {value: 1, hour: "1 hora"}, {
            value: 2,
            hour: "2 horas"
        }, {value: 3, hour: "3 horas"}, {value: 4, hour: "4 horas"}, {value: 5, hour: "5 horas"}, {
            value: 6,
            hour: "6 horas"
        }, {value: 7, hour: "7 horas"}, {value: 8, hour: "8 horas"}, {value: 9, hour: "9 horas"}, {
            value: 10,
            hour: "10 horas"
        }, {value: 11, hour: "11 horas"}, {value: 12, hour: "12 horas"}, {value: 13, hour: "13 horas"}]
        vm.hours = [];
        Modal.enteringForm(save);
        $scope.$on("$destroy", function () {
            Modal.leavingForm();
        });

        addHourseToSelect();
        function onNotify(info) {
            vm.progress = Math.round((info.loaded / info.total) * 100);
        }
        function addHourseToSelect() {
            var item = {value: '0', time: '12:00AM'};
            vm.hours.push(item);
            for (var i = 1; i < 12; i++) {
                var item = {value: i, time: i + ':00AM'};
                vm.hours.push(item);
            }
            var item2 = {value: '12', time: '12:00PM'};
            vm.hours.push(item2);
            for (var i = 1; i < 12; i++) {
                var item = {value: i + 12, time: i + ':00PM'};
                vm.hours.push(item);
            }

            if (vm.commonArea.id !== null) {
                if (vm.commonArea.maximunHours == 0) {
                    vm.commonArea.maximunHours = -1;
                }
                vm.button = "Editar";
                var autorizadorStatus = vm.commonArea.chargeRequired;
                if (vm.commonArea.chargeRequired == 1) {

                    vm.paymentRequired = true;
                    vm.paymentRequiredUpdate = 1;
                } else {
                    vm.paymentRequiredUpdate = 2;
                }
                // if( vm.commonArea.reservationWithDebt==1){
                //     vm.reservationWithDebts = 1;
                //
                // }else{
                //     vm.reservationWithDebts = 2;
                // }
                loadSchedule();
            } else {
                vm.isReady = true;
                vm.button = "Registrar";
                vm.commonArea.reservationWithDebt = 2;
                vm.commonArea.reservationCharge = 0;
                vm.commonArea.devolutionAmmount = 0;
                vm.commonArea.chargeRequired = 0;


                vm.commonArea.hasDaysBeforeToReserve = 0;
                vm.commonArea.hasDaysToReserveIfFree = 0;
                vm.commonArea.hasDistanceBetweenReservations = 0;
                vm.commonArea.needsApproval = 0;
                vm.commonArea.hasReservationsLimit = 0;
            }
        }


        function loadSchedule() {
            CommonAreaSchedule.findSchedulesByCommonArea({
                commonAreaId: vm.commonArea.id
            }, onSuccessSchedule);
        }

        function onSuccessSchedule(data) {
            vm.scheduleId = data[0].id;
            console.log(data)
            if(vm.commonArea.hasBlocks==0) {
                if (data[0].lunes !== "-") {
                    vm.daysOfWeek[0].selected = true;
                    vm.lunesSelected = true;
                    var times = data[0].lunes.split("-");
                    vm.daysOfWeek[0].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[0].finalTime = parseInt(times[1]);
                }
                if (data[0].martes !== "-") {
                    vm.daysOfWeek[1].selected = true;
                    vm.martesSelected = true;
                    var times = data[0].martes.split("-");
                    vm.daysOfWeek[1].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[1].finalTime = parseInt(times[1]);
                }
                if (data[0].miercoles !== "-") {
                    vm.daysOfWeek[2].selected = true;
                    vm.miercolesSelected = true;
                    var times = data[0].miercoles.split("-");
                    vm.daysOfWeek[2].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[2].finalTime = parseInt(times[1]);
                }
                if (data[0].jueves !== "-") {
                    vm.daysOfWeek[3].selected = true;
                    vm.juevesSelected = true;
                    var times = data[0].jueves.split("-");
                    vm.daysOfWeek[3].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[3].finalTime = parseInt(times[1]);

                }
                if (data[0].viernes !== "-") {
                    vm.daysOfWeek[4].selected = true;
                    vm.viernesSelected = true;
                    var times = data[0].viernes.split("-");
                    vm.daysOfWeek[4].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[4].finalTime = parseInt(times[1]);
                }
                if (data[0].sabado !== "-") {
                    vm.daysOfWeek[5].selected = true;
                    vm.sabadoSelected = true;
                    var times = data[0].sabado.split("-");
                    vm.daysOfWeek[5].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[5].finalTime = parseInt(times[1]);
                }
                if (data[0].domingo !== "-") {
                    vm.daysOfWeek[6].selected = true;
                    vm.domingoSelected = true;
                    var times = data[0].domingo.split("-");
                    vm.daysOfWeek[6].initialTime = parseInt(times[0]);
                    vm.daysOfWeek[6].finalTime = parseInt(times[1]);
                }
            }else{
                if (data[0].lunes !== "-") {
                    vm.daysOfWeek[0].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].lunes.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                 vm.daysOfWeek[0].blocks = blocksFormatted;
                }
                if (data[0].martes !== "-") {
                    vm.daysOfWeek[1].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].martes.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[1].blocks = blocksFormatted;
                }
                if (data[0].miercoles !== "-") {
                    vm.daysOfWeek[2].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].miercoles.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[2].blocks = blocksFormatted;
                }
                if (data[0].jueves !== "-") {
                    vm.daysOfWeek[3].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].jueves.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[3].blocks = blocksFormatted;
                }
                if (data[0].viernes !== "-") {
                    vm.daysOfWeek[4].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].viernes.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[4].blocks = blocksFormatted;
                }
                if (data[0].sabado !== "-") {
                    vm.daysOfWeek[5].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].sabado.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[5].blocks = blocksFormatted;
                }
                if (data[0].domingo !== "-") {
                    vm.daysOfWeek[6].selected = true;
                    vm.lunesSelected = true;
                    var blocks = data[0].domingo.split(",");
                    var blocksFormatted = [];
                    for (var i = 0; i < blocks.length; i++) {
                        var times = blocks[i].split("-");
                        var block = {}
                        block.initialTime = parseInt(times[0]);
                        block.finalTime = parseInt(times[1]);
                        block.selected = true;
                        blocksFormatted.push(block)
                    }
                    vm.daysOfWeek[6].blocks = blocksFormatted;
                }
            }
            vm.isReady = true;
        }

        vm.selectDay = function (index) {
            vm.spaceInvalid3 = false;
            vm.daysOfWeek[index].selected = !vm.daysOfWeek[index].selected;
            if (vm.daysOfWeek[index].selected == false) {
                vm.daysOfWeek[index].initialTime = "";
                vm.daysOfWeek[index].finalTime = "";
            }
        }
        vm.validateDaysHours = function (index, item) {
            if (item.initialTime !== undefined && item.finalTime !== undefined) {
                if (parseInt(item.initialTime) >= parseInt(item.finalTime)) {
                    setTimeout(function () {
                        $scope.$apply(function () {
                            item.isValid = false;
                        });
                    }, 200);
                    Modal.toast("Debe seleccionar una hora final posterior a la hora anterior");
                } else {
                    item.isValid = true;
                    // if(vm.commonArea.hasBlocks==1 && item.initialTime!="" && item.finalTime!=""){
                    //     var bloque = {initialTime:item.initialTime, finalTime:item.finalTime}
                    //     item.blocks.push(bloque);
                    // }
                }
            }
        };

        function validateForm() {
            if (!vm.isAnyDaySelected()) {
                setTimeout(function () {
                    $scope.$apply(function () {
                        vm.spaceInvalid3 = true;
                    });
                }, 200);

                Modal.toast("Debe seleccionar al menos un día permitido para reservar");
            } else {
                if (vm.isAllHoursValid() == false) {
                    Modal.toast("Debe corregir las horas permitidas para reservar");


                } else {
                    if (vm.commonArea.daysToReserveIfFreeMaximunValid == false) {
                        Modal.toast("El día mínimo de días con antelación debe ser menor al máximo");
                    } else {
                        save();
                    }

                }
            }


        }

        vm.validateDaysToReserveIfFree = function () {
            if (vm.commonArea.daysToReserveIfFreeMaximun < vm.commonArea.daysToReserveIfFreeMinimun) {
                Modal.toast("El día mínimo debe ser menor al máximo");
                vm.commonArea.daysToReserveIfFreeMaximunValid = false;
            } else {
                vm.commonArea.daysToReserveIfFreeMaximunValid = true;
            }
        }

        function save() {
            Modal.showLoadingBar();
            if (vm.commonArea.hasDaysToReserveIfFree == 1) {
                vm.commonArea.daysToReserveIfFree = vm.commonArea.daysToReserveIfFreeMinimun + "-" + vm.commonArea.daysToReserveIfFreeMaximun;
            }

            if (vm.commonArea.maximunHours == -1) {
                vm.commonArea.maximunHours = 0;
            }

            vm.commonArea.hasMaximunDaysInAdvance = vm.commonArea.hasMaximunDaysInAdvance == 1 ? true : false;
            vm.commonArea.hasDefinePeopleQuantity = vm.commonArea.hasDefinePeopleQuantity == 1 ? true : false;

            if (vm.commonArea.id !== null) {
                if (vm.commonArea.maximunHours == null || vm.commonArea.maximunHours === "") {
                    vm.commonArea.maximunHours = 0;
                }
                if (fileImage !== null) {
                    vm.imageUser = {user: vm.commonArea.id};
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify)
                } else {
                    CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
                }
            } else {
                if (vm.commonArea.maximunHours == null || vm.commonArea.maximunHours === "") {
                    vm.commonArea.maximunHours = 0;
                }
                vm.commonArea.companyId = globalCompany.getId();
                vm.commonArea.deleted = 0;
                if (fileImage !== null) {
                    SaveImageCloudinary
                        .save(fileImage, vm.imageUser)
                        .then(onSaveImageSuccess, onSaveError, onNotify)
                } else {
                    CommonArea.save(vm.commonArea, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveImageSuccess(data) {
            vm.commonArea.pictureContentType = "https://res.cloudinary.com/aditum/image/upload/v1501920877/" + data.imageUrl + ".jpg";
            if (vm.commonArea.id !== null) {
                CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
            } else {
                CommonArea.save(vm.commonArea, onSaveSuccess, onSaveError);
            }
        }

        vm.addBlockToDay = function (item) {
            var bloque = {selected: true, initialTime: "", finalTime: ""};
            item.blocks.push(bloque);
        }
        vm.deleteBlock = function (item) {
            item.selected = false;
            item.initialTime = "";
            item.finalTime = "";
        }
        vm.isAnyDaySelected = function () {

            var selectedDays = 0;
            angular.forEach(vm.daysOfWeek, function (item, key) {
                if (item.selected) {
                    selectedDays++;
                }

            })
            if (selectedDays > 0) {

                return true;
            } else {

                return false;
            }


        }
        vm.isAllHoursValid = function () {

            var invalid = 0;
            if (vm.commonArea.hasBlocks == 0) {
                angular.forEach(vm.daysOfWeek, function (item, key) {
                    if (item.isValid == false) {
                        invalid++;
                    }


                })

            } else {

                angular.forEach(vm.daysOfWeek, function (item, key) {

                    angular.forEach(item.blocks, function (item, key) {
                        if (item.isValid == false) {
                            invalid++;
                        }
                    });


                });

            }

            if (invalid > 0) {

                return false;
            } else {

                return true;
            }
        }

        function onSaveSuccess(result) {
            var commonAreaScheadule = formatScheaduleToInsert();
            commonAreaScheadule.commonAreaId = result.id;
            if (vm.commonArea.id !== null) {
                commonAreaScheadule.id = vm.scheduleId;
                CommonAreaSchedule.update(commonAreaScheadule, onSaveScheduleSuccess, onSaveError);
            } else {
                CommonAreaSchedule.save(commonAreaScheadule, onSaveScheduleSuccess, onSaveError);
            }
        }

        function formatScheaduleToInsert() {
            var commonAreaScheadule = {};
            commonAreaScheadule.lunes = "";
            commonAreaScheadule.martes = "";
            commonAreaScheadule.miercoles = "";
            commonAreaScheadule.jueves = "";
            commonAreaScheadule.viernes = "";
            commonAreaScheadule.sabado = "";
            commonAreaScheadule.domingo = "";
            if (vm.commonArea.hasBlocks == 0) {
                commonAreaScheadule.lunes = vm.daysOfWeek[0].initialTime + "-" + vm.daysOfWeek[0].finalTime;
                commonAreaScheadule.martes = vm.daysOfWeek[1].initialTime + "-" + vm.daysOfWeek[1].finalTime;
                commonAreaScheadule.miercoles = vm.daysOfWeek[2].initialTime + "-" + vm.daysOfWeek[2].finalTime;
                commonAreaScheadule.jueves = vm.daysOfWeek[3].initialTime + "-" + vm.daysOfWeek[3].finalTime;
                commonAreaScheadule.viernes = vm.daysOfWeek[4].initialTime + "-" + vm.daysOfWeek[4].finalTime;
                commonAreaScheadule.sabado = vm.daysOfWeek[5].initialTime + "-" + vm.daysOfWeek[5].finalTime;
                commonAreaScheadule.domingo = vm.daysOfWeek[6].initialTime + "-" + vm.daysOfWeek[6].finalTime;
                return commonAreaScheadule;
            } else if (vm.commonArea.hasBlocks == 1) {
                for (var i = 0; i < vm.daysOfWeek[0].blocks.length; i++) {
                    if (vm.daysOfWeek[0].blocks[i].selected) {
                        commonAreaScheadule.lunes = commonAreaScheadule.lunes + vm.daysOfWeek[0].blocks[i].initialTime + "-" + vm.daysOfWeek[0].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[1].blocks.length; i++) {
                    if (vm.daysOfWeek[1].blocks[i].selected) {
                        commonAreaScheadule.martes = commonAreaScheadule.martes + vm.daysOfWeek[1].blocks[i].initialTime + "-" + vm.daysOfWeek[1].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[2].blocks.length; i++) {
                    if (vm.daysOfWeek[2].blocks[i].selected) {
                        commonAreaScheadule.miercoles = commonAreaScheadule.miercoles + vm.daysOfWeek[2].blocks[i].initialTime + "-" + vm.daysOfWeek[2].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[3].blocks.length; i++) {
                    if (vm.daysOfWeek[3].blocks[i].selected) {
                        commonAreaScheadule.jueves = commonAreaScheadule.jueves + vm.daysOfWeek[3].blocks[i].initialTime + "-" + vm.daysOfWeek[3].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[4].blocks.length; i++) {
                    if (vm.daysOfWeek[4].blocks[i].selected) {
                        commonAreaScheadule.viernes = commonAreaScheadule.viernes + vm.daysOfWeek[4].blocks[i].initialTime + "-" + vm.daysOfWeek[4].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[5].blocks.length; i++) {
                    if (vm.daysOfWeek[5].blocks[i].selected) {
                        commonAreaScheadule.sabado = commonAreaScheadule.sabado + vm.daysOfWeek[5].blocks[i].initialTime + "-" + vm.daysOfWeek[5].blocks[i].finalTime + ","
                    }
                }
                for (var i = 0; i < vm.daysOfWeek[6].blocks.length; i++) {
                    if (vm.daysOfWeek[6].blocks[i].selected) {
                        commonAreaScheadule.domingo = commonAreaScheadule.domingo + vm.daysOfWeek[6].blocks[i].initialTime + "-" + vm.daysOfWeek[6].blocks[i].finalTime + ","
                    }
                }
                commonAreaScheadule.lunes = commonAreaScheadule.lunes.slice(0, -1);
                commonAreaScheadule.martes = commonAreaScheadule.martes.slice(0, -1);
                commonAreaScheadule.miercoles = commonAreaScheadule.miercoles.slice(0, -1);
                commonAreaScheadule.jueves = commonAreaScheadule.jueves.slice(0, -1);
                commonAreaScheadule.viernes = commonAreaScheadule.viernes.slice(0, -1);
                commonAreaScheadule.sabado = commonAreaScheadule.sabado.slice(0, -1);
                commonAreaScheadule.domingo = commonAreaScheadule.domingo.slice(0, -1);
                return commonAreaScheadule;
            }
        }

        function onSaveScheduleSuccess(result) {
            Modal.hideLoadingBar();
            $state.go('common-area-administration.common-area');
            Modal.toast("Se ha gestionado el área común correctamente.")

            vm.isSaving = false;
        }

        function onSaveError() {
            Modal.hideLoadingBar()
            vm.isSaving = false;
        }

        vm.setPicture = function ($file, commonArea) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        commonArea.picture = base64Data;
                        commonArea.pictureContentType = $file.type;
                    });
                });
            }
        };
        vm.setImage = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        vm.displayImage = base64Data;
                        vm.displayImageType = $file.type;
                    });
                });
                fileImage = $file;
            }
        };
        vm.confirmMessage = function () {

            Modal.confirmDialog("¿Está seguro que desea registrar el área común?", "",
                function () {
                    validateForm()


                });

        }

    }
})();
