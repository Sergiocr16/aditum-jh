(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AccountStatus', AccountStatus);

    AccountStatus.$inject = ['$resource', 'DateUtils'];

    function AccountStatus ($resource, DateUtils) {
        var resourceUrl =  'api/accountStatus/:houseId/:initial_time/:final_time';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
             params:{
                houseId: '@houseId',
                initial_time:'@initial_time',
                final_time: '@final_time'

            },
            transformResponse: function (data) {
                if (data) {
                    data = angular.fromJson(data);
                }
                return data;
            }},

        });
    }
})();
