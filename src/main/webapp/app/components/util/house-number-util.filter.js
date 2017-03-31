//(function() {
//    'use strict';
//
//    angular
//        .module('aditumApp')
//        .filter('formatHouseNumber', formatHouseNumber);
//        formatHouseNumber.$inject = [ 'House'];
//
//    function formatHouseNumber(House) {
//        return formatHouseNumberFilter;
//         var promise;
//        function formatHouseNumberFilter (input) {
//            House.get({id: input},success);
//            function success(data){
//                data.$promise.then(function(datica){
//                    console.log('datica ' + datica.housenumber)
//                     promise = datica.housenumber;
//                })
//
//            }
//
//            setTimeout(function(){
//                console.log('premise: ' + promise);
//             return promise}, 10000);
//
//        }
//    }
//})();
