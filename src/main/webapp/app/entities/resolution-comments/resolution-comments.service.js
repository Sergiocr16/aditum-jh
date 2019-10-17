(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('ResolutionComments', ResolutionComments);

    ResolutionComments.$inject = ['$resource', 'DateUtils'];

    function ResolutionComments ($resource, DateUtils) {
        var resourceUrl =  'api/resolution-comments/:id';

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
