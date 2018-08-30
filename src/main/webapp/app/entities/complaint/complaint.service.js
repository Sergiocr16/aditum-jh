(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Complaint', Complaint);

    Complaint.$inject = ['$resource', 'DateUtils'];

    function Complaint ($resource, DateUtils) {
        var resourceUrl =  'api/complaints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', url:"api/complaints/admin/:companyId", isArray: true},
            'queryByStatus': { method: 'GET', url:"api/complaints/admin/:companyId/status/:status", isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.resolutionDate = DateUtils.convertDateTimeFromServer(data.resolutionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
