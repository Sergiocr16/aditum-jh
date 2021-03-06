(function() {
    'use strict';

    var noContent = {
        bindings:{
            text:'@',
            icon:'@',
            text2:'@',
        },
        template: '<div flex style="align-items: stretch;justify-content: center;">\n' +
            '            <div class="text-center">\n' +
            '                <i class="material-icons md-48 md-inactive md-dark circle-icon">{{$ctrl.icon}}</i>\n' +
            '                <h3 class="text-center" style="font-weight: 400;color: rgba(0, 0, 0, 0.50);">{{$ctrl.text}}</h3>\n' +
            '                <h4 ng-if="$ctrl.text2!=null" class="text-center" style="font-weight: 400;color: rgba(0, 0, 0, 0.50);">{{$ctrl.text2}}</h4>\n' +
            '            </div>\n' +
            '        </div>',
        controller: noContentController
    };

    angular
        .module('aditumApp')
        .component('noContent', noContent);

    noContentController.$inject = ['$scope', 'AlertService'];

    function noContentController($scope, AlertService) {
        var vm = this;

    }
})();
