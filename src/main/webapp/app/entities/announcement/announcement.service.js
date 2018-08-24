(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Announcement', Announcement);

    Announcement.$inject = ['$resource', 'DateUtils'];

    function Announcement($resource, DateUtils) {
        var resourceUrl = 'api/announcements/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'queryAsAdmin': {method: 'GET', url: "api/announcements/asAdmin/:companyId", isArray: true},
            'queryAsUser': {method: 'GET', url: "api/announcementsPerCompany/:companyId", isArray: true},
            'querySketches': {method: 'GET', url: "api/announcements/sketches/:companyId", isArray: true},
            'getComments': {method: 'GET', url: "api/announcement-comments/:announcementId", isArray: true},
            'saveComment': {method: 'POST', url: "api/announcement-comments"},
            'editComment': {method: 'PUT', url: "api/announcement-comments"},
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
            'update': {method: 'PUT'}
        });
    }
})();
