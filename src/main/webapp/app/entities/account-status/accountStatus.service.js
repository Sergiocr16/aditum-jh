(function () {
    'use strict';
    angular
        .module('aditumApp')
        .factory('AccountStatus', AccountStatus);

    AccountStatus.$inject = ['$resource', 'DateUtils'];

    function AccountStatus($resource, DateUtils) {
        var resourceUrl = 'api/accountStatus/:houseId/:initial_time/:final_time/:resident_account/:today_time';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
             params:{
                houseId: '@houseId',
                initial_time:'@initial_time',
                final_time: '@final_time',
                resident_account: '@resident_account',
                today_time: '@today_time'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'sendAccountStatus':{
                url:'api/accountStatus/send-to/:houseId/:companyId/:emailTo/month/:monthDate',
            },
            'sendAccountStatusToAll':{
                url:'api/accountStatus/send-to-all/toAll/:toAll/:companyId/month/:monthDate',
            },
            'sendPaymentEmail':{
                url:'api/accountStatus/file/:accountStatusObject/:option',
                params:{
                    accountStatusObject: '@accountStatusObject',
                    option:'@option'
                }
            }

        });
    }
})();
