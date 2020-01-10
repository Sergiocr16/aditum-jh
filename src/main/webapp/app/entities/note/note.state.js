(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider

        .state('noteNew', {
            parent: 'entity',
            url: '/home-service/new',
            data: {
                authorities: ['ROLE_USER','ROLE_OWNER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/note/note-dialog.html',
                    controller: 'NoteDialogController',
                    controllerAs: 'vm'
                }
            },
        });
    }

})();
