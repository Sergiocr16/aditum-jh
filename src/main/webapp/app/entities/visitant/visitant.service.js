(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Visitant', Visitant);

    Visitant.$inject = ['$resource', 'DateUtils'];

    function Visitant ($resource, DateUtils) {
        var resourceUrl =  'api/visitants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.arrivaltime = DateUtils.convertDateTimeFromServer(data.arrivaltime);
                        data.invitationstaringtime = DateUtils.convertDateTimeFromServer(data.invitationstaringtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
            'findInvitedByHouseAndIdentificationNumber':{
            url: 'api/visitants/invited/findOneByHouse/:identificationNumber/:houseId/:companyId',
             method: 'GET',
             params:{
                   identificationNumber:'@identificationNumber',
                   houseId: '@houseId',
                   companyId: '@companyId',
                 },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invitationstaringtime = DateUtils.convertDateTimeFromServer(data.invitationstaringtime);
                        data.invitationlimittime = DateUtils.convertDateTimeFromServer(data.invitationlimittime);
                    }
                    return data;
                }
            },
            'findInvitedByHouse':{
            url: 'api/visitants/invited/finByHouse/:companyId/:houseId',
             method: 'GET',
             params:{
                   houseId: '@houseId',
                   companyId: '@companyId',
                 },
              isArray: true,
            },
            'findByHouseInLastMonth':{
            url: 'api/visitants/finByHouse/lastMonth/:houseId',
             method: 'GET',
             params:{
                   houseId: '@houseId'
                 },
              isArray: true,
            },
            'findBetweenDatesByHouse':{
                 method: 'GET',
                 url: 'api/visitants/between/:initial_time/:final_time/byHouse/:houseId',
                 isArray: true,
                 params:{
                  initial_time:'@initial_time',
                  final_time: '@final_time',
                  houseId: '@houseId',
                }
            },
            'findByCompanyInLastMonth':{
            url: 'api/visitants/finByCompany/lastMonth/:companyId',
             method: 'GET',
             params:{
                   companyId: '@companyId'
                 },
              isArray: true,
            },
            'findBetweenDatesByCompany':{
                 method: 'GET',
                 url: 'api/visitants/between/:initial_time/:final_time/byCompany/:companyId',
                 isArray: true,
                 params:{
                  initial_time:'@initial_time',
                  final_time: '@final_time',
                  companyId: '@companyId',
                }
            },
            'update': { method:'PUT' },
            'delete': { method:'DELETE' }
        });
    }
})();
