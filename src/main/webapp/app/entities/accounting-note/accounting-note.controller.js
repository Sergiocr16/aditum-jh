(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('AccountingNoteController', AccountingNoteController);

    AccountingNoteController.$inject = ['$rootScope', '$scope', '$localStorage', 'CommonMethods', 'Modal', '$state', 'AccountingNote'];

    function AccountingNoteController($rootScope, $scope, $localStorage, CommonMethods, Modal, $state, AccountingNote) {
        var vm = this;
        vm.loadPage = loadPage;
        vm.transition = transition;
        vm.isReady = false;
        loadAll();

        vm.divide = function () {
            vm.others = [];
            vm.fixeds = [];
            for (var i = 0; i < vm.accountingNotes.length; i++) {
                if (vm.accountingNotes[i].fixed == 1) {
                    vm.fixeds.push(vm.accountingNotes[i]);
                } else {
                    vm.others.push(vm.accountingNotes[i]);
                }
            }
            vm.isReady = true;
        }
        $scope.$watch(function () {
            return $rootScope.houseSelected;
        }, function () {
            vm.isReady = false;
            loadAll();
        });
        vm.setColor = function (accountingNote, posColor) {
            var colors = ['accounting-note-white', 'accounting-note-red', 'accounting-note-orange', 'accounting-note-yellow', 'accounting-note-purple', 'accounting-note-blue'];
            accountingNote.color = colors[posColor];
            save(accountingNote);
        }

        vm.fix = function (accountingNote) {
            accountingNote.fixed = accountingNote.fixed == 1 ? 0 : 1;
            save(accountingNote);
            vm.divide();
        }


        function save(accountingNote) {
            vm.isSaving = true;
            if (accountingNote.id !== null) {
                AccountingNote.update(accountingNote, onSaveSuccess, onSaveError);
            } else {
                AccountingNote.save(accountingNote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.delete = function (accountingNote) {
            Modal.confirmDialog("¿Está seguro que desea eliminar la nota?", "", function () {
                AccountingNote.delete({id: accountingNote.id},
                    function () {
                        loadAll()
                        Modal.toast("Nota eliminada correctamente.")
                    });
            })
        }

        function loadAll() {
            AccountingNote.getByHouse({
                page: 0,
                size: 100,
                houseId: $localStorage.houseSelected.id,
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.accountingNotes = data;
                vm.divide();
            }

            function onError(error) {
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
    }
})();
