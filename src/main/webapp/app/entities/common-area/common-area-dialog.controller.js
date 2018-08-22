(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaDialogController', CommonAreaDialogController);

    CommonAreaDialogController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'entity', 'CommonArea','CommonMethods'];

    function CommonAreaDialogController ($timeout, $scope, $stateParams, DataUtils, entity, CommonArea,CommonMethods) {
        var vm = this;

        vm.commonArea = entity;

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.paymentRequired=false;
        CommonMethods.validateNumbers();
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.daysOfWeek = [{day:'Lunes',selected:false},{day:'Martes',selected:false},{day:'Miercoles',selected:false},{day:'Jueves',selected:false},{day:'Viernes',selected:false},{day:'Sábado',selected:false},{day:'Domingo',selected:false}];
        vm.hours = [{value:500,time:'5:00AM'},{value:600,time:'6:00AM'}];
        vm.hoursPerDay=[]
        if (vm.commonArea.id !== null) {
                vm.button="Editar";
        } else {
            vm.button="Registrar";
        }

        vm.selectDay = function(index){
            vm.daysOfWeek[index].selected = !vm.daysOfWeek[index].selected;
        }

        function save () {
            vm.isSaving = true;
            if (vm.commonArea.id !== null) {
                CommonArea.update(vm.commonArea, onSaveSuccess, onSaveError);
            } else {
                CommonArea.save(vm.commonArea, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {

            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPicture = function ($file, commonArea) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        commonArea.picture = base64Data;
                        commonArea.pictureContentType = $file.type;
                    });
                });
            }
        };

    }
})();
