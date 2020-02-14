(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('CommonAreaController', CommonAreaController);

    CommonAreaController.$inject = ['$state', 'DataUtils', 'CommonArea', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$rootScope','CommonMethods','globalCompany','Modal'];

    function CommonAreaController($state, DataUtils, CommonArea, ParseLinks, AlertService, paginationConstants, pagingParams,$rootScope,CommonMethods,globalCompany,Modal) {

        var vm = this;
        if(globalCompany.getUserRole() === 'ROLE_MANAGER'){
            $rootScope.active = "reservationAdministration";

        }else{
            $rootScope.active = "common-area-resident-account";

        }


        var data = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
        if (data.hasContability == 1) {
            vm.hasContability = true;
        } else {
            vm.hasContability = false;
        }

        vm.isReady = false;
        $rootScope.mainTitle = "Áreas comunes";
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

       loadAll();

        function loadAll () {
            CommonArea.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
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
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.commonAreas = data;
                vm.page = pagingParams.page;
                vm.isReady = true;
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
            Modal.confirmDialog("¿Está seguro que desea eliminar el área común?","",
                function(){
                    Modal.showLoadingBar();
                    commonArea.deleted = 1;
                    CommonArea.update(commonArea, onDeleteSuccess, onSaveError);

                });

        };

        function onSaveError(error) {
            Modal.hideLoadingBar();
            Modal.toast("Un error inesperado ocurrió");
            AlertService.error(error.data.message);
        }
        function onDeleteSuccess (result) {

            loadAll();
            Modal.toast("Se eliminó el área común correctamente");
            Modal.hideLoadingBar();
            // $state.go('common-area-administration.common-area-all-reservations');
            //
        }
    }
})();
