(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('VisitantInvitedUserController', VisitantInvitedUserController);

    VisitantInvitedUserController.$inject = ['Visitant', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'Principal', '$rootScope', '$state', 'CommonMethods'];

    function VisitantInvitedUserController(Visitant, ParseLinks, AlertService, paginationConstants, pagingParams, Principal, $rootScope, $state, CommonMethods) {
        var vm = this;
        vm.Principal;
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        setTimeout(function(){loadAll();},300)

        angular.element(document).ready(function() {
            $("#all").fadeIn("slow");
        });

        function loadAll() {
            Visitant.findInvitedByHouse({
                companyId: $rootScope.companyId,
                houseId: $rootScope.companyUser.houseId
            }).$promise.then(onSuccess);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data) {
            console.log(data)
                angular.forEach(data, function(value, key) {
                    value.fullName = value.name + " " + value.lastname + " " + value.secondlastname;
                })
                vm.visitants = data;
                vm.page = pagingParams.page;
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

        vm.hasPermission = function(visitor) {
            var currentTime = new Date();
            var intiTime = new Date(visitor.invitationstaringtime);
            var finalTime = new Date(visitor.invitationlimittime);
            if (visitor.isinvited == 2) {
                return false;
            }
            if (currentTime <= finalTime) {
                return true;
            }
        }

        vm.renewVisitor = function(visitor) {
            var encryptedId = CommonMethods.encryptIdUrl(visitor.id)
            $state.go('visitant-invited-user.edit', {
                id: encryptedId
            })
        }

        vm.deleteInvitedVisitor = function(visitor) {
            bootbox.confirm({
                message: "¿Está seguro que desea eliminar el registro?",
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
                        $("#all").fadeOut();
                        Visitant.delete({
                            id: visitor.id
                        }, success);

                        function success(data) {
                            loadAll();
                            $("#all").fadeIn();
                            toastr["success"]("Se ha eliminado el registro correctamente");
                            bootbox.hideAll();
                        }
                    }
                }
            });

        }


        vm.cancelInvitation = function(visitor) {
            bootbox.confirm({
                message: "¿Está seguro que desea revocar el permiso de acceso a " + visitor.name + " " + visitor.lastname + "?",
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
                        visitor.isinvited = 2;
                        Visitant.update(visitor, success)

                        function success(data) {
                            bootbox.hideAll();
                            toastr["success"]("Se ha cancelado la invitación correctamente");
                        }
                    }
                }
            });
        }

    }
})();
