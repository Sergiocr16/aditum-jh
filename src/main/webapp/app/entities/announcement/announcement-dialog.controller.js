(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementDialogController', AnnouncementDialogController);

    AnnouncementDialogController.$inject = ['$state', '$rootScope', '$timeout', '$scope', '$stateParams', 'entity', 'Announcement', 'CommonMethods', 'Modal'];

    function AnnouncementDialogController($state, $rootScope, $timeout, $scope, $stateParams, entity, Announcement, CommonMethods, Modal) {
        var vm = this;

        vm.announcement = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.saveAsSketch = saveAsSketch;
        vm.isCreatingOne = $state.includes('announcement.new');
        $rootScope.mainTitle = 'Crear/Editar noticia';


        function defineImageSize(str) {
            var res;
            var n1 = str.search('style="width: 25%;"');
            var n2 = str.search('style="width: 50%;"');
            var n3 = str.search('style="width: 100%;"');
            if (n1 != -1) {
                res = str.replace("<img", '<img width="25%"');
            }
            if (n2 != -1) {
                res = str.replace("<img", '<img width="50%"');
            }
            if (n3 != -1) {
                res = str.replace("<img", '<img width="100%"');
            }
            return res;
        }

        function save() {
            Modal.confirmDialog("¿Está seguro que desea publicar la noticia?", "Una vez publicada será visible para los condóminos", function () {
                Modal.showLoadingBar()
                vm.announcement.publishingDate = moment(new Date()).format();
                vm.announcement.status = 2;
                vm.announcement.companyId = $rootScope.companyId;
                var str = vm.announcement.description;
                vm.announcement.description = defineImageSize(str);
                if (vm.announcement.id !== null) {
                    Announcement.update(vm.announcement, onSaveSuccess, onSaveError);
                } else {
                    Announcement.save(vm.announcement, onSaveSuccess, onSaveError);
                }
            })

        }

        function saveAsSketch() {
            vm.isSaving = true;
            Modal.showLoadingBar();
            vm.announcement.publishingDate = moment(new Date()).format();
            vm.announcement.status = 1;
            vm.announcement.companyId = $rootScope.companyId;
            if (vm.announcement.id !== null) {
                Announcement.update(vm.announcement, onSaveSuccessSketch, onSaveError);
            } else {
                Announcement.save(vm.announcement, onSaveSuccessSketch, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            vm.announcement = result;
            Modal.hideLoadingBar();
            Modal.toast("Se ha publicado exitosamente el anuncio en el condominio.");
            $state.go("announcement");
            vm.isSaving = false;
        }

        function onSaveSuccessSketch(result) {
            vm.announcement = result;
            Modal.hideLoadingBar();
            Modal.toast("Guardado como borrador.");
            $state.go("announcement-sketch");
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
            Modal.toast("Ah ocurrido un error, es posible que el contenido del anuncio sea demasiado largo o tenga muchas imagenes en el mismo.")
            Modal.hideLoadingBar();
        }

        vm.datePickerOpenStatus.publishingDate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.clear = function () {
            history.back();
        }
    }
})();
