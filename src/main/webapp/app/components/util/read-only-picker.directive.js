
(function () {
    'use strict';

    angular
        .module('aditumApp')
        .directive('readonlydatepicker', readonlydatepicker);

function readonlydatepicker(){
    return {
        restrict: 'EAC',
        link: function(scope, elem, attr) {
            if(angular.element(".datePicker input")[0]!=undefined){
                angular.element(".datePicker input")[0].setAttribute("readonly","readonly");
            }
            if(angular.element(".datePicker input")[1]!=undefined) {
                angular.element(".datePicker input")[1].setAttribute("readonly","readonly");
            }
            angular.element(".md-datepicker-button").each(function(){
                var el = this;
                var ip = angular.element(el).parent().find("input").bind('click', function(e){
                    angular.element(el).click();
                });
                angular.element(this).css('visibility', 'hidden');
            });
        }
    }
}
})();
