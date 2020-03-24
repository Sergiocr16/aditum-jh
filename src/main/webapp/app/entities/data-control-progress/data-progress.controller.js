(function() {
    'use strict';

    angular
        .module('aditumApp')
        .controller('DataProgressController', DataProgressController);

    DataProgressController.$inject = ['Modal','$rootScope', '$state', 'Principal', '$timeout', 'Auth', 'MultiCompany', 'House', 'Company', '$localStorage','globalCompany'];

    function DataProgressController(Modal,$rootScope, $state, Principal, $timeout, Auth, MultiCompany, House, Company, $localStorage,globalCompany) {

        var vm = this;
        vm.isReady = false;
        $rootScope.active = "houses";
        vm.isAuthenticated = Principal.isAuthenticated;
        vm.house ='-1';
        vm.filterStateTemporal = "-1";
        vm.filterState = "";
        loadCompanies();
        function loadCompanies() {
            Company.query({}).$promise.then(onSuccessCompanies);
            function onSuccessCompanies(data, headers) {
                for (var i = 0; i < data.length; i++) {
                    if(data[i].id==globalCompany.getId()){
                        vm.company = data[i];
                    }
                }
                vm.loadHouses(globalCompany.getId())
            }
        }


        function residentsEnabledGraphInit() {
            var sinRedimir = 0;
            var enProgreso = 0;
            var listo = 0;
            var deshabitada = 0;
            for (var i = 0; i < vm.houses.length; i++) {
                if(vm.houses[i].isdesocupated==1){
                    deshabitada++;
                }else{
                    switch (vm.houses[i].codeStatus) {
                        case 0:
                            sinRedimir++;
                            break;
                        case 1:
                            enProgreso++;
                            break;
                        case 2:
                            enProgreso++;
                            break;
                        case 3:
                            listo++;
                            break;
                        case 4:
                            listo++;
                            break;
                    }
                }
            }
            var rows = [];
            var colums = [];
            colums.push({"v": "Sin ingreso de datos"});
            colums.push({"v": sinRedimir});
            rows.push({"c": colums})
            var colums = [];
            colums.push({"v": "En progreso"});
            colums.push({"v": enProgreso});
            rows.push({"c": colums});
            var colums = [];
            colums.push({"v": "Información ingresada"});
            colums.push({"v": listo});
            rows.push({"c": colums});
            var colums = [];
            colums.push({"v": "Deshabitada"});
            colums.push({"v": deshabitada});
            rows.push({"c": colums});
            vm.progressData = {
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
                        // {
                        //     "id": "redimido-id",
                        //     "label": "Redimido",
                        //     "type": "number"
                        // },
                        {
                            "id": "en-progreso-id",
                            "label": "En progreso",
                            "type": "number"
                        },
                        {
                            "id": "listo-id",
                            "label": "Información ingresada",
                            "type": "number"
                        },
                        {
                            "id": "deshabitada-id",
                            "label": "Deshabitada",
                            "type": "number"
                        }

                    ],
                    "rows": rows
                },
                "options": {
                    // "title": "Residentes",
                    "legend": {"position": "bottom"},
                    "isStacked": "false",
                    'chartArea': {'width': '90%', 'height': '78%'},
                    "sliceVisibilityThreshold":0,
                    "fill": 200000,
                    "animation": {
                        duration: 1000,
                        easing: 'out',
                    },
                    "displayExactValues": true,
                    colors: ['#f44336',  '#ffc107', '#4caf50', '#78909c']
                }
            }
        }
        vm.changeFilterState = function(filterStateTemporal) {
          if(filterStateTemporal=='-1'){
              vm.filterState = "";
          }else{
              vm.filterState = filterStateTemporal;
          }
        };

        vm.loadHouses = function(companyId) {
            House.query({
                companyId: companyId
            }).$promise.then(onSuccessHouses);

            function onSuccessHouses(data, headers) {
                vm.houses = data;
                residentsEnabledGraphInit()
                vm.isReady = true;
            }
        };

        vm.changeStatus = function(house, status) {
            house.codeStatus = status;


            House.update(house, onSaveSuccess, onSaveError);
        };
        function onSaveSuccess() {


            vm.loadHouses(globalCompany.getId());
        }
        function onSaveError() {
            Modal.toast("Un error inespesperado sucedió.");
            vm.isReady = true;
        }
        function saveTextAsFile(data, filename) {

            if (!data) {
                return;
            }

            if (!filename) filename = 'console.json'

            var blob = new Blob([data], {
                    type: 'text/plain'
                }),
                e = document.createEvent('MouseEvents'),
                a = document.createElement('a')
            // FOR IE:

            if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                var e = document.createEvent('MouseEvents'),
                    a = document.createElement('a');

                a.download = filename;
                a.href = window.URL.createObjectURL(blob);
                a.dataset.downloadurl = ['text/plain', a.download, a.href].join(':');
                e.initEvent('click', true, false, window,
                    0, 0, 0, 0, 0, false, false, false, false, 0, null);
                a.dispatchEvent(e);
            }
        }
        vm.exportarCodigos = function() {
            var info = "";
            info += "Condominio " + vm.company.name + "  |  ";
            info += "A la Fecha: " + moment(new Date()).format("ll") + "\r\n\r\n";
            info += "Casa            Código de Ingreso\r\n";
            info += "---------------------------------\r\n";
            for (var i = 0; i < vm.houses.length; i++) {
                info += vm.houses[i].housenumber + "                        " + vm.houses[i].loginCode + "\r\n";
            }
            info += "---------------------------------\r\n\r\n";
            info += moment(new Date()).format("YYYY") + " © Aditum CR";
            saveTextAsFile(info, "Códigos de Ingreso, Condominio " + vm.company.name)
        }

    }
})();
