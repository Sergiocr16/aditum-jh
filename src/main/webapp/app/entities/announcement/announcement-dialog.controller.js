(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AnnouncementDialogController', AnnouncementDialogController);

    AnnouncementDialogController.$inject = ['$state', '$rootScope','$timeout', '$scope', '$stateParams', 'entity', 'Announcement', 'Company'];

    function AnnouncementDialogController ($state, $rootScope, $timeout, $scope, $stateParams, entity, Announcement, Company) {
        var vm = this;

        vm.announcement = entity;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.saveAsSketch = saveAsSketch;
        vm.isCreatingOne = $state.includes('announcement.new');

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        setTimeout(function() {$("#loadingIcon").fadeOut(300);
           }, 400)
            setTimeout(function() {
                $("#form").fadeIn('slow');
            },900 )

        function save () {
            vm.isSaving = true;
            vm.announcement.publishingDate = moment(new Date()).format();
            vm.announcement.status = 2;
            vm.announcement.companyId = $rootScope.companyId;
            if (vm.announcement.id !== null) {
                Announcement.update(vm.announcement, onSaveSuccess, onSaveError);
            } else {
                Announcement.save(vm.announcement, onSaveSuccess, onSaveError);
            }
        }

        function saveAsSketch () {
            vm.isSaving = true;
            vm.announcement.publishingDate = moment(new Date()).format();
            vm.announcement.status = 1;
            vm.announcement.companyId = $rootScope.companyId;
            if (vm.announcement.id !== null) {
                Announcement.update(vm.announcement, onSaveSuccessSketch, onSaveError);
            } else {
                Announcement.save(vm.announcement, onSaveSuccessSketch, onSaveError);
            }
        }
        function onSaveSuccess (result) {
        vm.announcement = result;
            toastr["success"]("Se ha publicado exitosamente el anuncio en el condominio.")
            vm.isSaving = false;
        }
        function onSaveSuccessSketch (result) {
        vm.announcement = result;
            toastr["info"]("Se ha guardado exitosamente el anuncio como borrador.")
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
            toastr["error"]("Ah ocurrido un error, es posible que el contenido del anuncio sea demasiado largo o tenga muchas imagenes en el mismo.")
        }

        vm.datePickerOpenStatus.publishingDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.clear = function(){
        history.back();
        }
    }
})();
