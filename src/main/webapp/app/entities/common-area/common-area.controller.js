(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaController', CommonAreaController);

    CommonAreaController.$inject = ['$state', 'DataUtils', 'CommonArea', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope','CommonMethods'];

    function CommonAreaController($state, DataUtils, CommonArea, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope,CommonMethods) {

        var vm = this;
        $rootScope.active = "reservationAdministration";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        setTimeout(function(){loadAll();},1000)

        function loadAll () {
            CommonArea.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                companyId: $rootScope.companyId
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.commonAreas = data;
                vm.page = pagingParams.page;
                setTimeout(function() {
                    $("#loadingIcon").fadeOut(300);
                }, 400)
                setTimeout(function() {
                    $("#tableDatas").fadeIn(300);
                },900 )
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }
        vm.formatearNumero = function(nStr) {

            var x = nStr.split('.');
            var x1 = x[0];
            var x2 = x.length > 1 ? ',' + x[1] : '';
            var rgx = /(\d+)(\d{3})/;
            while (rgx.test(x1)) {
                x1 = x1.replace(rgx, '$1' + ',' + '$2');
            }
            return x1 + x2;
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.deleteReservation = function(commonArea) {

            bootbox.confirm({

                message: "¿Está seguro que desea eliminar el área común?",

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
                        CommonMethods.waitingMessage();
                        commonArea.deleted = 1;
                        CommonArea.update(commonArea, onDeleteSuccess, onSaveError);

                    }
                }
            });
        };

        function onSaveError(error) {
            bootbox.hideAll()
            toastr["error"]("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
        }
        function onDeleteSuccess (result) {

            loadAll();
            toastr["success"]("Se eliminó el área común correctamente");
            bootbox.hideAll()
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }
    }
})();
