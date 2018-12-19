(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressCategoryController', EgressCategoryController);

    EgressCategoryController.$inject = ['EgressCategory', '$rootScope', 'globalCompany','Modal'];

    function EgressCategoryController(EgressCategory, $rootScope, globalCompany,Modal) {

        $rootScope.active = "egressCategories";
        $rootScope.mainTitle ="Categoría de egresos";

        var vm = this;
        vm.isReady = false;
        vm.groups = [{
            id: 1,
            name: 'Gastos fijos'
        }, {
            id: 2,
            name: 'Gastos variables'
        }, {
            id: 3,
            name: 'Otros gastos'
        }]
        vm.egressCategories = [];
        vm.repitedCategories = 0;

        vm.validate = function (egressCategory, type) {
            if (type == 1) {
                if (egressCategory.group == "" || egressCategory.group == null || egressCategory.group == undefined) {

                    egressCategory.groupIsEmpty = true;

                } else {
                    egressCategory.groupIsEmpty = false;
                }
            } else {
                if (egressCategory.category == "" || egressCategory.category == null || egressCategory.category == undefined) {

                    egressCategory.categoryIsEmpty = true;

                } else {
                    egressCategory.categoryIsEmpty = false;
                }

            }


        }

        vm.addEgressCategory = function () {
            var egressCategory = {
                id: null,
                group: null,
                category: null,
                companyId: globalCompany.getId()
            }
            vm.egressCategories.push(egressCategory);
        }


        vm.saveCategory = function (egressCategory, type, index) {
            if (egressCategory.groupIsEmpty == false && egressCategory.categoryIsEmpty == false) {
                EgressCategory.query({
                    companyId: globalCompany.getId()
                }).$promise.then(function (result) {
                    vm.repitedCategories = 0;
                    angular.forEach(result, function (category, key) {
                        if (egressCategory.group == category.group && egressCategory.category == category.category && category.id != egressCategory.id) {
                            if (result[index] != undefined) {

                                egressCategory.category = result[index].category;
                                egressCategory.group = result[index].group;
                            } else {
                                egressCategory.category = "";
                            }
                            vm.repitedCategories++;
                        }

                    })
                    if (vm.repitedCategories > 0) {
                        Modal.toast("No puede haber dos categorías con el mismo nombre en el mismo grupo.")
                    } else {
                        if (egressCategory.id !== null) {
                            if (egressCategory.categoryIsEmpty == false) {
                                EgressCategory.update(egressCategory, function (result) {
                                    Modal.toast("Guardado.")
                                })
                            }

                        } else {
                            EgressCategory.save(egressCategory, function (result) {
                                Modal.toast("Guardado.")
                            })

                        }

                    }

                })
            } else {
                Modal.toast("No puede dejar el campo vacio")
            }
        };

        vm.confirmDeleteCategory = function (index, egressCategory) {

            Modal.confirmDialog("¿Está seguro que desea eliminar esta categoría de gastos " + "?","Una vez eliminado no podrá recuperar los datos",
                function(){
                    Modal.toast("Eliminado")
                    vm.deleteCategory(index, egressCategory)
                });



        };

        vm.deleteCategory = function (index, egressCategory) {
            if (egressCategory.id !== null) {
                EgressCategory.delete({
                        id: egressCategory.id
                    },
                    function () {
                        loadAll();
                    });
            } else {
                loadAll();
            }
        }


            loadAll()


        function loadAll() {

            EgressCategory.query({
                companyId: globalCompany.getId()
            }).$promise.then(onSuccessEgressCategories);

            function onSuccessEgressCategories(data, headers) {
                vm.searchQuery = null;
                angular.forEach(data, function (value, key) {
                    value.groupIsEmpty = false
                    value.categoryIsEmpty = false;
                })

                vm.egressCategories = data;
                vm.isReady = true;
            }

        }
    }
})();
