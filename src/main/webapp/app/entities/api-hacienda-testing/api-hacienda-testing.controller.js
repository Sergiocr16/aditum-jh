(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('ApiHaciendaTestingController', ApiHaciendaTestingController);

    ApiHaciendaTestingController.$inject = ['ApiHacienda', '$localStorage', 'DataUtils', '$scope'];

    function ApiHaciendaTestingController(ApiHacienda, $localStorage, DataUtils, $scope) {

        var vm = this;

        var file;
        ApiHacienda.postRequest({w: "ejemplo", r: "hola"}, function (data) {
            console.log(data)
        }, function (data) {
            console.log(data)
        })


        // ApiHacienda.registerUser({
        //     fullName: "Juan Prueba",
        //     userName: "aditum",
        //     pwd: "clave123",
        //     email: "lalaa@upcmaill.com"
        // }, function (data) {
        //     console.log("AAAAAAAAA")
        //     console.log(data)
        // }, function (error) {
        //     console.log(error)
        // });

        //
        // ApiHacienda.recoverPwd("walner1borbon", function () {
        //     console.log("a")
        // }, function () {
        // });

        ApiHacienda.login({userName: "aditum", pwd: "clave123"}, function (data) {
            console.log(data)
        }, function (error) {
            console.log(error)
        });
        ApiHacienda.keyCreationForXml({
            tipoCedula: "fisico",
            cedula: "116060486",
            situacion: "normal",
            codigoPais: "506",
            consecutivo: "1522773402",
            tipoDocumento: "FE"
        }, function (data) {
            data = data.resp;
            ApiHacienda.newFE({clave: data.clave, consecutivo: data.consecutivo}, function (la) {
                console.log(la)
                console.log(ApiHacienda.resultMsg(la.resp))
            }, function () {
            })
        }, function (error) {
            console.log(error)
        })

        // ApiHacienda.logOut(function (data) {
        //     console.log($localStorage.userHacienda);
        //     console.log(data);
        // }, function (error) {
        //     console.log(error)
        // })

        // ApiHacienda.keyCreationAceptacionComprobante({
        //     tipoCedula: "fisico",
        //     cedula: "116060486",
        //     situacion: "normal",
        //     codigoPais: "506",
        //     consecutivo: "1522773402"
        // }, function (data) {
        //     console.log(data);
        // }, function (error) {
        //     console.log(error)
        // })

        // ApiHacienda.checkLogin(function (data) {
        //     console.log(data)
        //     $localStorage.userHacienda.sessionKey = data.sessionKey;
        //     $localStorage.userHacienda.userName = data.userName;
        // }, function () {
        // })
        vm.uploadLlaveCriptografica = function () {
            ApiHacienda.uploadLlaveCriptografica(file, function (data) {
                console.log(data);
            }, function (error) {
                console.log(error)
            })
        }

        vm.uploadFile = function ($file) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                    });
                });
                file = $file;
            }
        };


    }
})();
