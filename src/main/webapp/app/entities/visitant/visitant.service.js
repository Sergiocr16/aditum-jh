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
            'update': { method:'PUT' }
        });
    }
})();
