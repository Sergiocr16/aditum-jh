(function() {
    'use strict';
    /* globals SockJS, Stomp */

    angular
        .module('aditumApp')
        .factory('StompManager', StompManager);

    StompManager.$inject = ['$window', '$q', 'AuthServerProvider', '$timeout'];

    function StompManager ($window, $q, AuthServerProvider, $timeout) {

        var END_POINT = 'websocket/tracker';
        var RECONECT_TIME = 7000;

        var stompClient = null;
        var connected = $q.defer();
        var subscribeMap = {};


        var service = {
            connect: reconnect,
            disconnect: disconnect,
            getListener: getListener,
            send: send,
            subscribe: subscribe,
            unsubscribe: unsubscribe,
            isConnected: isConnected,
            isSubcribed:isSubscribed
        };

        return service;

        function connect (headers) {
            headers = headers || {};
            var socket = new SockJS(buildUrl());
            stompClient = Stomp.over(socket);
            stompClient.connect(headers, onSuccess, onError);

            function onSuccess() {
                connected.resolve('success');
            }

            function onError() {
                onConnectionLost(headers);
            }

            return connected.promise;
        }

        function onConnectionLost(headers) {
            stompClient = null;
            connected.reject('Connection_Lost');
            connected = $q.defer();
            if(AuthServerProvider.getToken()) {
                $timeout(function () {
                    reconnect(headers);
                },  RECONECT_TIME);
            }
        }

        function isConnected() {
            return stompClient !== null && stompClient.connected;
        }

        function buildUrl() {
            var loc = $window.location;
            var url = '//' + loc.host + loc.pathname + END_POINT;

            var authToken = AuthServerProvider.getToken();
            if(authToken){
                url += '?access_token=' + authToken;
            }
            return url;
        }

        function disconnect () {
            connected = $q.defer();
            if (stompClient !== null) {
                stompClient.disconnect();
                stompClient = null;
            }
        }

        function getListener (url) {
            var value = subscribeMap[url];
            if(!value) {
                throw 'there is no subscription to url = ' + url + '.';
            }
            return value.listener.promise;
        }
        function isSubscribed (url) {
            var value = subscribeMap[url];
            if(!value) {
                return false;
            }else{
                return true;
            }
        }

        function send(url, payload, headers) {
            headers = headers || {};
            connected.promise.then(sendOnConnected);
            function sendOnConnected() {
                if (isConnected()) {
                    stompClient.send(url, headers, angular.toJson(payload));
                }
            }
        }

        function reconnect(headers) {
            headers = headers || {};
            connect(headers);
            var urls =  Object.keys(subscribeMap);
            angular.forEach(urls, function (url) {
                subscribeKeyValue(url, subscribeMap[url]);
            });
            return connected.promise;
        }

        function subscribe (url) {
            if(!isSubscribed(url)){
                var value = subscribeMap[url]
                    || { subscriber: null, listener: $q.defer() };
                subscribeMap[url] =  value;
                subscribeKeyValue(url, value);
            }
        }
        function subscribeKeyValue(url, value) {
            connected.promise.then(subscribeOnConnected);
            function subscribeOnConnected() {
                if (isConnected() && value) {
                    value.subscriber = stompClient.subscribe(url, function (data) {
                        value.listener.notify(angular.fromJson(data.body));
                    });
                }
            }
        }

        function unsubscribe (url) {
            var value = subscribeMap[url];
            if (value && value.subscriber !== null) {
                value.subscriber.unsubscribe();
            }
            subscribeMap[url] = undefined;
        }
    }
})();
