(function() {
    'use strict';

    var noContentSmall = {
        bindings:{
            text:'@',
            icon:'@',
            text2:'@',
        },
        template: '<div flex  class="col-lg-12 col-md-12 col-sm-12 col-xs-12" layout="row" layout-align="center center" style="margin-top:6px">\n' +
            '            <div class="text-center">\n' +
            '                <i class="material-icons md-48 md-inactive md-dark circle-icon">{{$ctrl.icon}}</i>\n' +
            '                <h4 class="text-center" style="font-weight: 400;color: rgba(0, 0, 0, 0.50);">{{$ctrl.text}}</h4>\n' +
            '                <h4 ng-if="$ctrl.text2!=null" class="text-center" style="font-weight: 400;color: rgba(0, 0, 0, 0.50);">{{$ctrl.text2}}</h4>\n' +
            '            </div>\n' +
            '        </div>',
        controller: noContentSmallController
    };

    angular
        .module('aditumApp')
        .component('noContentSmall', noContentSmall);

    noContentSmallController.$inject = ['$scope', 'AlertService'];

    function noContentSmallController($scope, AlertService) {
        var vm = this;

    }
})();
