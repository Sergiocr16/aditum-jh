(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Announcement', Announcement);

    Announcement.$inject = ['$resource', 'DateUtils'];

    function Announcement ($resource, DateUtils) {
        var resourceUrl =  'api/announcements/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'queryAsAdmin': { method: 'GET',url:"api/announcements/asAdmin/:companyId", isArray: true},
            'querySketches': { method: 'GET',url:"api//announcements/sketches/:companyId", isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.publishingDate = DateUtils.convertDateTimeFromServer(data.publishingDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
