(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Vehicule', Vehicule);

    Vehicule.$inject = ['$resource'];

    function Vehicule($resource) {
        var resourceUrl = 'api/vehicules/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'findVehiculesMacroByFilter': {
                method: 'GET',
                isArray: true,
                url:'api/vehicules/macro/:macroId/filter/:filter'
            },
            'getVehicules': {
                method: 'GET',
                url: 'api/allVehicules/:companyId/:houseId/:enabled/:licencePlate',
                isArray: true
            },
            'getByCompanyAndPlate': {
                method: 'GET',
                url: 'api/vehicules/findByCompanyAndPlate/:companyId/:licensePlate',
                params: {
                    companyId: '@companyId',
                    licensePlate: '@licensePlate'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'vehiculesEnabled': {
                method: 'GET',
                url: 'api/vehiculesEnabled',
                isArray: true
            }, 'vehiculesDisabled': {
                method: 'GET',
                url: 'api/vehiculesDisabled',
                isArray: true
            }, 'findVehiculesEnabledByHouseId': {
                method: 'GET',
                url: 'api/vehiculesEnabled/byHouse',
                isArray: true
            }, 'findVehiculesDisabledByHouseId': {
                method: 'GET',
                url: 'api/vehiculesDisabled/byHouse',
                isArray: true
            }
        });
    }
})();
