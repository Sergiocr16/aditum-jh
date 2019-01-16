(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ComplaintComment', ComplaintComment);

    ComplaintComment.$inject = ['$resource', 'DateUtils'];

    function ComplaintComment ($resource, DateUtils) {
        var resourceUrl =  'api/complaint-comments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.editedDate = DateUtils.convertDateTimeFromServer(data.editedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
