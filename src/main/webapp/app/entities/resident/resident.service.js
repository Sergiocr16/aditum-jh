(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Resident', Resident);

    Resident.$inject = ['$resource'];

    function Resident ($resource) {
        var resourceUrl =  'api/residents/:id';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'findResidentsMacroByFilter': {
                method: 'GET',
                isArray: true,
                url:'api/residents/macro/:macroId/filter/:filter'
            },
            'update': { method:'PUT' },
            'getResidents': {
                method: 'GET',
                url: 'api/allResidents/:companyId/:enabled/:houseId/:owner/:name',
                isArray: true
             }
             ,'residentsDisabled': {
                method: 'GET',
                url: 'api/residentsDisabled/:companyId',
                isArray: true
            },'findByUserId':{
                url:'api/residents/findByUserId/:id',
                method:'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },'findResidentesEnabledByHouseId': {
                 method: 'GET',
                 url: 'api/residentsEnabled/byHouse',
                 isArray: true
             },'findResidentesDisabledByHouseId': {
                   method: 'GET',
                   url: 'api/residentsDisabled/byHouse',
                   isArray: true
               },
              'getByCompanyAndIdentification': {
                   method: 'GET',
                   url: 'api/residents/findByCompanyAndIdentification/:companyId/:identificationID',
                   params:{
                      companyId:'@companyId',
                      licensePlate: '@identificationID'
                    },
                   transformResponse: function (data) {
                       if (data) {
                           data = angular.fromJson(data);
                       }
                       return data;
                   }
                }
        });
    }
})();
