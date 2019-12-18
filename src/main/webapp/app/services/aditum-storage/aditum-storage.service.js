(function() {
    'use strict';

    angular
        .module('aditumApp')
        .factory('AditumStorageService', AditumStorageService);

    AditumStorageService.$inject = ['$rootScope'];
    var firebaseConfigAditumStorage = {
        apiKey: "AIzaSyC23W7cx8J7nl2lQWFmJlLyoy3P-YHUzFw",
        authDomain: "aditum-storage.firebaseapp.com",
        databaseURL: "https://aditum-storage.firebaseio.com",
        projectId: "aditum-storage",
        storageBucket: "aditum-storage.appspot.com",
        messagingSenderId: "157583321641",
        appId: "1:157583321641:web:92be6aa22d55f444b80a38",
        measurementId: "G-NH8FXYDS5K"
    };
    var aditumStorage = firebase.initializeApp(firebaseConfigAditumStorage, "aditumStorage");

    function AditumStorageService() {

        var services = {
            ref: ref
        };
        return services;

        function ref() {
            return aditumStorage.storage().ref();
        }

    }
})();
