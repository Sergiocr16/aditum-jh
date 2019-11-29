(function() {
    'use strict';

    angular
        .module('aditumApp')
        .filter('formatDate', formatDate);

    function formatDate() {
        return formatDateFilter;

        function formatDateFilter (input) {
        moment.locale('es')
            if(input==null){
             return "Aún en progreso";
            }else{
             return moment(input).format('LL h:mm a');
            }
        }
    }
})();
