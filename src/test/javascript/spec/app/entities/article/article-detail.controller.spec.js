'use strict';

describe('Controller Tests', function() {

    describe('Article Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockArticle, MockChapter, MockKeyWords, MockArticleCategory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockArticle = jasmine.createSpy('MockArticle');
            MockChapter = jasmine.createSpy('MockChapter');
            MockKeyWords = jasmine.createSpy('MockKeyWords');
            MockArticleCategory = jasmine.createSpy('MockArticleCategory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Article': MockArticle,
                'Chapter': MockChapter,
                'KeyWords': MockKeyWords,
                'ArticleCategory': MockArticleCategory
            };
            createController = function() {
                $injector.get('$controller')("ArticleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:articleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
