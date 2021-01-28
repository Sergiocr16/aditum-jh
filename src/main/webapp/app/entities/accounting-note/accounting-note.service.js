(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AccountingNote', AccountingNote);

    AccountingNote.$inject = ['$resource', 'DateUtils'];

    function AccountingNote ($resource, DateUtils) {
        var resourceUrl =  'api/accounting-notes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.modificationDate = DateUtils.convertDateTimeFromServer(data.modificationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
