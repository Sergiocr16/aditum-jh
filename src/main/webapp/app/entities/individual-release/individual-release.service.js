(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('IndividualRelease', IndividualRelease);

    IndividualRelease.$inject = ['$resource', 'DateUtils'];

    function IndividualRelease ($resource, DateUtils) {
        var resourceUrl =  'api/complaints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', url:"api/complaints/admin/:companyId/:category", isArray: true},
            'queryByStatus': { method: 'GET', url:"api/complaints/admin/:companyId/status/:status/:category", isArray: true},
            'queryAsResident': { method: 'GET', url:"api/complaints/user/:residentId/:category", isArray: true},
            'queryAsResidentByStatus': { method: 'GET', url:"api/complaints/user/:residentId/status/:status/:category", isArray: true},
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