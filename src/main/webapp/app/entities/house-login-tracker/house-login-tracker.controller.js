(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('HouseLoginTrackerController', HouseLoginTrackerController);

    HouseLoginTrackerController.$inject = ['HouseLoginTracker','globalCompany'];

    function HouseLoginTrackerController(HouseLoginTracker,globalCompany) {

        var vm = this;
        vm.false = true;
        vm.houseLoginTrackers = [];

        loadAll();

        function loadAll() {


            HouseLoginTracker.query({
                companyId: globalCompany.getId()
            }, function(result) {
                vm.houseLoginTrackers = result;
                vm.searchQuery = null;
                vm.isReady = true;
                loginTrackerGraphInit()
            }, onError);
        }

        function loginTrackerGraphInit() {
            var ingresado = 0;
            var sinIngresar = 0;
            for (var i = 0; i < vm.houseLoginTrackers.length; i++) {
                switch (vm.houseLoginTrackers[i].status) {
                    case 0:
                        sinIngresar++;
                        break;
                    case 1:
                        ingresado++;
                        break;
                }
            }
            var rows = [];
            var colums = [];
            colums.push({"v": "Ingresado"});
            colums.push({"v": ingresado});
            rows.push({"c": colums})
            var colums = [];
            colums.push({"v": "Sin ingresar"});
            colums.push({"v": sinIngresar});
            rows.push({"c": colums});
            vm.loginTraker = {
                "type": "PieChart",
                "displayed": false,
                "cssStyle": "height:600px;width: 100%",
                "data": {
                    "cols": [
                        {
                            "id": "enable",
                            "label": "enable",
                            "type": "string"
                        },
                        {
                            "id": "ingresado-id",
                            "label": "Ingresado",
                            "type": "number"
                        },
                        {
                            "id": "sinIngresar-id",
                            "label": "Sin ingresar",
                            "type": "number"
                        }

                    ],
                    "rows": rows
                },
                "options": {
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    'chartArea': {'width': '100%', 'height': '78%'},
                    "sliceVisibilityThreshold":0,
                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    colors: ['#4caf50','#f44336']
                }
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
        }
    }
})();
