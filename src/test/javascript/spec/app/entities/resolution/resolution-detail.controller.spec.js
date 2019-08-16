'use strict';

describe('Controller Tests', function() {

    describe('Resolution Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockResolution, MockArticle, MockKeyWords, MockArticleCategory, MockCompany, MockAdminInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockResolution = jasmine.createSpy('MockResolution');
            MockArticle = jasmine.createSpy('MockArticle');
            MockKeyWords = jasmine.createSpy('MockKeyWords');
            MockArticleCategory = jasmine.createSpy('MockArticleCategory');
            MockCompany = jasmine.createSpy('MockCompany');
            MockAdminInfo = jasmine.createSpy('MockAdminInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Resolution': MockResolution,
                'Article': MockArticle,
                'KeyWords': MockKeyWords,
                'ArticleCategory': MockArticleCategory,
                'Company': MockCompany,
                'AdminInfo': MockAdminInfo
            };
            createController = function() {
                $injector.get('$controller')("ResolutionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'aditumApp:resolutionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
