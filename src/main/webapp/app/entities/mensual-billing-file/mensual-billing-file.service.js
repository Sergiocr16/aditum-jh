(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('MensualBillingFile', MensualBillingFile);

    MensualBillingFile.$inject = ['$resource'];

    function MensualBillingFile ($resource) {
        var resourceUrl =  'api/mensual-billing-files/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'findAll':{
                method:'GET',isArray:true,url:"api/mensual-billing-files/:companyId/:month/:year"
            },
            'findAllWithPrivacy':{
                method:'GET',isArray:true,url:"api/mensual-billing-files-resident/:companyId/:month/:year"
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
