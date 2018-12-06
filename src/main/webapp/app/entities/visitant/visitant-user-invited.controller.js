(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitedUserController', VisitantInvitedUserController);

    VisitantInvitedUserController.$inject = ['Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', '$state', 'CommonMethods', 'WSVisitor', 'WSDeleteEntity', 'companyUser', 'globalCompany', 'Modal'];

    function VisitantInvitedUserController(Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, $state, CommonMethods, WSVisitor, WSDeleteEntity, companyUser, globalCompany, Modal) {
        var vm = this;
        vm.Principal;
        $rootScope.active = "residentsInvitedVisitors";
        $rootScope.mainTitle = "Visitantes invitados";
        vm.isReady = false;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll() {
            vm.isReady = false;

            Visitant.findInvitedByHouse({
                companyId: globalCompany.getId(),
                houseId: companyUser.houseId
            }).$promise.then(onSuccess);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data) {

                angular.forEach(data, function (value, key) {
                    value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
                    if (value.identificationnumber == "") {
                        value.identificationnumber = null;
                    }
                })
                vm.visitants = data;
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

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.hasPermission = function (visitor) {


            if (visitor.isinvited == 2) {
                return false;
            }
          return vm.isBetweenDate(visitor)
        }

        vm.isBetweenDate = function(visitor){
            var currentTime = new Date().getTime();
            var intiTime = new Date(visitor.invitationstaringtime).getTime();
            var finalTime = new Date(visitor.invitationlimittime).getTime();
            if (intiTime <= currentTime && currentTime <= finalTime) {
                return true;
            }else{
                return false
            }
        }

        vm.renewVisitor = function (visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-user.edit', {
                id: encryptedId
            })
        }

        vm.deleteInvitedVisitor = function (visitor) {
            Modal.confirmDialog("¿Está seguro que desea eliminar el registro?", "", function () {
                Modal.showLoadingBar();

                Visitant.delete({
                    id: visitor.id
                }, success);

            })


            function success(data) {
                loadAll();
                Modal.toast("Se ha eliminado el registro correctamente");
                Modal.hideLoadingBar();
                WSDeleteEntity.sendActivity({type: 'visitor', id: visitor.id})

            }
        }


        vm.cancelInvitation = function (visitor) {
            Modal.confirmDialog("¿Está seguro que desea revocar el permiso de acceso a " + visitor.name + " " + visitor.lastname + "?", "", function () {
                Modal.showLoadingBar();
                visitor.isinvited = 2;
                Visitant.update(visitor, success)
            })
        }

        function success(data) {
            WSVisitor.sendActivity(data);
            Modal.hideLoadingBar();
            Modal.toast("Se ha cancelado la invitación correctamente");
        }

    }
})();
