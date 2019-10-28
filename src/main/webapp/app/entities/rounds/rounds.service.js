(function() {
    'use strict';
    angular
        .module('aditumApp')
        .factory('Rounds', Rounds);

    Rounds.$inject = ['$resource', 'DateUtils'];

    function Rounds ($resource, DateUtils) {
        var resourceUrl =  'api/rounds/:id';
        return $resource(resourceUrl, {}, {
            'getAllByCompanyAndDates': {
                method: 'GET',
                url:'api/rounds/:companyId/dates/:initialDate/:finalDate',
                isArray:true
            },
            'getOne': {
                method: 'GET',
                url:'api/rounds/get/:uid',
            },
        });
    }
})();
