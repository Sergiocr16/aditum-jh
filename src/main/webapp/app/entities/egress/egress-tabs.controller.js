(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('EgressTabsController', EgressTabsController);

    EgressTabsController.$inject = [];

    function EgressTabsController() {
        var vm = this;
        vm.findBootstrapEnvironment = function () {
            var envs = ['xs', 'sm', 'md', 'lg'];

            var $el = $('<div>');
            $el.appendTo($('body'));

            for (var i = envs.length - 1; i >= 0; i--) {
                var env = envs[i];

                $el.addClass('hidden-' + env);
                if ($el.is(':hidden')) {
                    $el.remove();
                    return env;
                }
            }
        }
        vm.isScreenSizeSmall = function () {
            var envs = ['xs', 'sm', 'md'];
            var e = 0;
            for (var i = 0; i < envs.length; i++) {
                if (envs[i] === vm.findBootstrapEnvironment()) {
                    e++;
                }
            }
            if (e > 0) {
                return true;
                console.log('a')
            }else{
                return false;
                console.log('e')
            }

        }

    }
})();
