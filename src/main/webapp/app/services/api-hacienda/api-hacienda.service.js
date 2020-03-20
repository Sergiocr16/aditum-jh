(function () {
    'use strict';

    angular
        .module('aditumApp')
        .service('ApiHacienda', ApiHacienda);

    ApiHacienda.$inject = ["$localStorage"];

    function ApiHacienda($localStorage) {
        var vm = this;


        /****************************************************************************
         /*!
         * Cala Framework: To make your life simpler
         *
         * Copyright (c) 2018 CRLibre
         * License: AGPL - CalaApi
         * Include this AT THE BOTTOM of your pages, that is all you need to do.
         *
         *           | |
         *   ___ __ _| | __ _
         *  / __/ _` | |/ _` |
         * | (_| (_| | | (_| |
         *  \___\__,_|_|\__,_|
         *
         *****************************************************************************/
// Version 0.1

        /*******************************/
        /*            CONS             */
        /*******************************/

// Users
        var ERROR_NO_VALID_USER = "-300";
        var ERROR_USER_WRONG_LOGIN_INFO = "-301";
        var ERROR_USER_NO_VALID_SESSION = "-302"; // Not in use I think
        var ERROR_USER_ACCESS_DENIED = "-303";
        var ERROR_USER_EXISTS = "-304";
        var ERROR_USERS_NO_TOKEN = "-305";

//Database
        var ERROR_DB_NO_RESULTS_FOUND = "-200";
        var ERROR_BAD_REQUEST = "-1";

//Others
        var ERROR_ERROR = "-2";
        var ERROR_MODULE_UNDEFINED = "-3";
        var ERROR_MODULE_NOT_FOUND = "-4";
        var ERROR_FUNCTION_NOT_FOUND = "-5";
        var SUCCESS_ALL_GOOD = "1";


        /*******************************/
        /*          Settings           */
        /*******************************/
//This is the url where your api.php is located
        vm.calaApi_url = "https://aditum-fe.herokuapp.com/api.php";
        // vm.calaApi_url = "https://api-demo.crlibre.org/api.php";
        vm.calaApi_url = "http://localhost/aditum-fe/api.php";

//Default frontpage or index
        var calaApi_front = "index.html";

//Var to store user data
        var calaApi_user = "";

//If you want to debug or not
        var calaApi_debugMode = true;
        /***************************************************/
        /* Register a new user                             */
        /* Requiere:                                       */
        /* fullName, userName, email, about, country, pwd  */

        /***************************************************/
        function calaApi_registerUser(userData, success, error) {

            var req = {
                w: "users",
                r: "users_register",
                fullName: userData.fullName,
                userName: userData.userName,
                email: userData.userEmail,
                pwd: userData.pwd,
                about: userData.about,
                country: userData.userCountry,
                inst: userData.inst
            };

            calaApi_postRequest(req,
                function (d) {
                    calaApi_setLocalStorage('userNameHacienda', d.resp.userName);
                    calaApi_setLocalStorage('sessionKeyHacienda', d.resp.sessionKey);
                    success(d);
                }, function (d) {
                    error(d);
                });
        }

        /*********************************************/
        /* Function to check login users             */
        /* Req success, error                        */

        /*********************************************/
        function calaApi_checkLogin(success, error, timeout) {
            var req = {
                w: "users",
                r: "users_get_my_details"
            };

            calaApi_postRequest(req,
                function (data) {
                    if (data.resp != ERROR_USER_WRONG_LOGIN_INFO && data.resp != ERROR_USER_ACCESS_DENIED) {
                        if (success != null) {
                            calaApi_debug("It seems we are logged in as " + data.resp.userName);
                            calaApi_user = data.resp.userName;
                            success(data);
                        }
                    } else {
                        if (error != null) {
                            error(data);
                        }
                    }
                },
                function () {
                    calaApi_debug("Exec error func");
                    if (error != null) {
                        error();
                    }
                },
                function () {
                    calaApi_debug("Exec callback func");
                    if (timeout != null) {
                        timeout();
                    }
                });
        }

        /*********************************************/
        /* Function to create users                   */
        /* Req userData, func success, func error    */

        /*********************************************/
        function calaApiApi_login(userData, success, error) {
            var req = {
                w: "users",
                r: "users_log_me_in",
                userName: userData.userName,
                pwd: userData.pwd
            };

            calaApi_postRequest(req,
                function (data) {
                    if (data.resp != ERROR_USER_WRONG_LOGIN_INFO) {
                        $localStorage.userHacienda = {};
                        $localStorage.userHacienda.userName = data.resp.userName;
                        $localStorage.userHacienda.sessionKey = data.resp.sessionKey;
                        calaApi_setLocalStorage('userName', data.resp.userName);
                        calaApi_setLocalStorage('sessionKey', data.resp.sessionKey);
                        if (success != null) {
                            success(data);
                        }
                    } else {
                        if (error != null) {
                            error(data);
                        }
                    }
                },
                function (data) {
                    if (error != null) {
                        error(data);
                    }
                });
        }

        /*********************************************/
        /* Function to log out               */
        /* Req userData, func success, func error    */

        /*********************************************/
        function calaApiApi_logOut(success, error) {
            var req = {
                w: "users",
                r: "users_log_me_out",
                iam: $localStorage.userHacienda.userName,
                sessionKey: $localStorage.userHacienda.sessionKey
            };

            calaApi_postRequest(req,
                function (data) {
                    if (data.resp != ERROR_USER_WRONG_LOGIN_INFO) {
                        $localStorage.userHacienda = {};
                        calaApi_setLocalStorage('userName', null);
                        calaApi_setLocalStorage('sessionKey', null);
                        if (success != null) {
                            success(data);
                        }
                    } else {
                        if (error != null) {
                            error(data);
                        }
                    }
                },
                function (data) {
                    if (error != null) {
                        error(data);
                    }
                });
        }

        /*********************************************/
        /* Function to recover pss                   */
        /* Req userName                              */

        /*********************************************/
        function calaApiApi_recoverPwd(userName, success, error) {
            var req = {
                w: "users",
                r: "users_recover_pwd",
                userName: userName,
            };

            calaApi_postRequest(req,
                function (data) {
                    if (data.resp == SUCCESS_ALL_GOOD) {
                        if (success != null) {
                            success(data);
                        }
                    } else {
                        if (error != null) {
                            error(data);
                        }
                    }
                },
                function (data) {
                    if (error != null) {
                        error(data);
                    }
                });
        }

        /*********************************************/
        /* Function set local storage                */
        /* Req key, value                            */

        /*********************************************/
        function calaApi_setLocalStorage(k, v) {
            calaApi_debug("Saving in storage K: " + k + " V: " + v);
            localStorage.setItem(k, v);
        }

        /*********************************************/
        /* Function set local storage                */
        /* Req key                                   */
        /* Return value                              */

        /*********************************************/
        function calaApi_getLocalStorage(k) {
            var v = localStorage.getItem(k);
            calaApi_debug("Getting from storage K: " + k + " GOT: " + v);
            return v;
        }

        /*********************************************/
        /* Function to make post reqs                */
        /* Req request data, func success, func error*/

        /*********************************************/
        function calaApi_postRequest(req, success, error, timeout, times) {
            calaApi_debug("Making a post request to " + vm.calaApi_url);
            console.log("Making a post request to " + vm.calaApi_url)
            /*generate the form*/
            var _data = new FormData();

            for (var key in req) {
                var value = req[key];
                calaApi_debug("Adding " + key + " -> " + value);
                _data.append(key, value);
            }

            _data.append("iam", calaApi_getLocalStorage('userName'));
            _data.append("sessionKey", calaApi_getLocalStorage('sessionKey'));

            var oReq = new XMLHttpRequest();
            oReq.open("POST", vm.calaApi_url, true);

            oReq.timeout = timeout;

            oReq.onload = function (oEvent) {
                if (oReq.status == 200) {
                    var r = oReq.responseText;
                    r = JSON.parse(r);
                    calaApi_debug("Done!");
                    success(r);
                } else {
                    var r = oReq.responseText;
                    calaApi_debug("There was an error");
                    error(r);
                }
            };

            oReq.ontimeout = function (e) {
                times++;
                if (times < 3) {
                    calaApi_postRequest(req, success, error, timeout, (times++));
                    calaApi_debug("Timeout " + times + ", lets try again");
                } else {
                    calaApi_doSomethingAfter(function () {
                        calaApi_debug("Timeout does not work, retrying in a sec");
                        calaApi_postRequest(req, success, error, timeout, 0);
                        calaApi_debug("Function called...");
                    }, 3000);
                }
            }
            oReq.send(_data);
        }

        /*********************************************/
        /* Function to debug                         */
        /* Requieres msg                             */

        /*********************************************/
        function calaApi_debug(msg) {
            if (calaApi_debugMode) {
                console.log("[CalApi]->" + msg);
            }
        }

        /*********************************************/
        /* Function to do somethong after some time  */
        /* Requieres function, time                  */

        /*********************************************/
        function calaApi_doSomethingAfter(f, t) {
            var timer = setTimeout(function () {
                f();
                clearTimeout(timer);
            }, t);
        }

        /*********************************************/
        /* Function to do somethong after some time  */
        /* Requieres function, time                  */

        /*********************************************/
        function calaApi_resultToMsg(r) {
            if (r == ERROR_NO_VALID_USER) {
                return "No valid user, the user may not exist";
            } else if (r == ERROR_USER_WRONG_LOGIN_INFO) {
                return "Wrong login info";
            } else if (r == ERROR_USER_NO_VALID_SESSION) {
                return "No valid session, maybe is too late";
            } else if (r == ERROR_USER_ACCESS_DENIED) {
                return "The user is banned 'status' = 0";
            } else if (r == ERROR_USER_EXISTS) {
                return "";
            } else if (r == ERROR_USERS_NO_TOKEN) {
                return "Error with token";
            } else if (r == ERROR_DB_NO_RESULTS_FOUND) {
                return "No results found in db query";
            } else if (r == ERROR_BAD_REQUEST) {
                return "Bad request, are all params good?";
            } else if (r == ERROR_ERROR) {
                return "Standard error";
            } else if (r == ERROR_MODULE_UNDEFINED) {
                return "There is no module to ask or run, 'w' param is not setted";
            } else if (r == ERROR_MODULE_NOT_FOUND) {
                return "The module in 'w' does not exist";
            } else if (r == ERROR_FUNCTION_NOT_FOUND) {
                return "The function in param 'r' not found";
            } else if (r == SUCCESS_ALL_GOOD) {
                return "The request was successful";
            } else {
                return "Is this an error?    " + r + " You coud add the error and make a git pull request. :)";
            }
        }

        function getRandomNumber(length) {
            return Math.floor(Math.pow(10, length - 1) + Math.random() * 9 * Math.pow(10, length - 1));
        }

        function registerUserApi(user, success, error) {
            var user = {
                w: "users",
                r: "users_register",
                fullName: user.fullName,
                userName: user.userName,
                userEmail: user.email,
                about: "Usuario creado mediante ADITUM",
                userCountry: "Costa Rica",
                pwd: user.pwd,
                inst: "1"
            }
            calaApi_registerUser(user, success, error);
        }

        function keyCreationAceptacionComprobante(req, success, error) {
            req.tipoDocumento = "CCE";
            keyCreationForXml(req, success, error);
        }

        function keyCreationAceptacionParcialComprobante(req, success, error) {
            req.tipoDocumento = "CPCE";
            keyCreationForXml(req, success, error)
        }

        function keyCreationRechazoComprobante(req, success, error) {
            req.tipoDocumento = "RCE";
            keyCreationForXml(req, success, error)
        }

        function keyCreationForXml(req, success, error) {
            // tipoDocumento tiene las siguientes opciones:
            // FE Factura Electronica
            // ND Nota de Debito
            // NC Nota de Credito
            // TE Tiquete Electronico
            // CCE Confirmacion Comprabante Electronico
            // CPCE Confirmacion Parcial Comprbante Electronico
            // RCE Rechazo Comprobante Electronico
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //     tipoCedula = fisico o juridico
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // situacion = normal En el caso de que la situación se normal
            // contingencia En el caso de que se tenga que enviar una clave de contingencia
            // sininternet En el caso de que se tenga que hacer sin internet
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // Para enviar a creación de la clave se envían los siguientes paramentros (En mi caso Cedula Fisica)
            //
            // w=clave
            //
            // r=clave
            //
            // tipoCedula= fisico o juridico
            //
            // cedula= Numero de Cedula 702320717
            //
            // codigoPais= código del país 506
            //
            // consecutivo= codigo de 10 numeros 1522773402 (Numero de Factura)
            //
            // situacion= nomal contingencia sininternet normal
            //
            // codigoSeguridad= codigo de 8 numeros 07756342 (Numero aleatorio)
            //
            // tipoDocumento= FE ND NC TE CCE CPCE RCE FE
            var req1 = {
                w: "clave",
                r: "clave",
                tipoCedula: req.tipoCedula,
                cedula: req.cedula,
                situacion: req.situacion,
                codigoPais: req.codigoPais,
                consecutivo: req.consecutivo,
                codigoSeguridad: getRandomNumber(8),
                tipoDocumento: req.tipoDocumento
            };
            calaApi_postRequest(req1, success, error)
        }


        function uploadLlaveCriptografica(fileToUpload, success, error) {
            var req = {
                w: "fileUploader",
                r: "subir_certif",
                sessionKey: $localStorage.userHacienda.sessionKey,
                fileToUpload: fileToUpload,
                iam: $localStorage.userHacienda.userName,
            };
            console.log(req)
            calaApi_postRequest(req, success, error)
        }

        function newFE(data, success, error) {
            // Los Datos son:
            //
            // w : genXML
            // r : gen_xml_fe
            // clave : 50613051800070232071700100001011522773408107756348
            // consecutivo : 00100001011522773408
            // fecha_emision : 2018-05-13T15 : 30 : 00-06 : 00
            // emisor_nombre : Walner Borbon
            // emisor_tipo_indetif : 01
            // emisor_num_identif : 702320717
            // nombre_comercial : Walner Borbon
            // emisor_provincia : 6
            // emisor_canton : 02
            // emisor_distrito : 03
            // emisor_barrio : 01
            // emisor_otras_senas : Frente a la escuela
            // emisor_cod_pais_tel : 506
            // emisor_tel : 64206205
            // emisor_cod_pais_fax : 506
            // emisor_fax : 00000000
            // emisor_email : walner1borbon@gmail.com
            // receptor_nombre : Julian Subiros
            // receptor_tipo_identif : 01
            // receptor_num_identif : 114480790
            // receptor_provincia : 6
            // receptor_canton : 02
            // receptor_distrito : 03
            // receptor_barrio : 01
            // receptor_cod_pais_tel : 506
            // receptor_tel : 84922891
            // receptor_cod_pais_fax : 506
            // receptor_fax : 00000000
            // receptor_email : julisubiros@hotmail.com
            // condicion_venta : 01
            // plazo_credito : 0
            // medio_pago : 01
            // cod_moneda : CRC
            // tipo_cambio : 564.48
            // total_serv_gravados : 0
            // total_serv_exentos : 200000
            // total_merc_gravada : 0
            // total_merc_exenta : 0
            // total_gravados : 0
            // total_exentos : 200000
            // total_ventas : 200000
            // total_descuentos : 0
            // total_ventas_neta : 200000
            // total_impuestos : 0
            // total_comprobante : 200000
            // otros : Muchas gracias
            // detalles : {"1" : {"cantidad" : "1","unidadMedida" : "Sp","detalle" : "Impresora","precioUnitario" : "10000","montoTotal" : "10000","subtotal" : "9900","montoTotalLinea" : "9900","montoDescuento" : "100","naturalezaDescuento" : "Pronto pago&quot;}, "2" : {"cantidad" : "1","unidadMedida" : "Unid","detalle" : "producto","precioUnitario" : "10000","montoTotal" : "10000","subtotal" : "10000","montoTotalLinea" : "11170","impuesto" : {"1" : {"codigo" : "01","tarifa" : "11.7","monto" : "1170"}}}}
            //
            console.log(data)

            var req = {
                w: "genXML",
                r: "gen_xml_fe",
                clave: "50613051800070232071700100001011522773408107756348",
                codigo_actividad: "12",
                consecutivo: "00100001011522773408",
                fecha_emision: "2018-05-13T15:30:00-06:00",
                emisor_nombre: "12",
                emisor_tipo_identif: "01",
                emisor_num_identif: "12",
                emisor_nombre_comercial: "12",
                emisor_provincia: "06",
                emisor_canton: "02",
                emisor_distrito: "01",
                emisor_barrio: "01",
                emisor_otras_senas: "12",
                emisor_cod_pais_tel: "506",
                emisor_tel: "620225452",
                emisor_cod_pais_fax: "4454456",
                emisor_fax: "12",
                emisor_email: "qw@qw.qw",
                omitir_receptor: "12",
                receptor_nombre: "12",
                receptor_tipo_identif: "01",
                receptor_num_identif: "116054855",
                receptor_identif_extranjero: "12",
                receptor_nombre_comercial: "12",
                receptor_provincia: "01",
                receptor_canton: "02",
                receptor_distrito: "03",
                receptor_barrio: "01",
                receptor_otras_senas: "12",
                receptor_otras_senas_extranjero: "12",
                receptor_cod_pais_tel: "506",
                receptor_tel: "12",
                receptor_cod_pais_fax: "12",
                receptor_fax: "12",
                receptor_email: "asd@asd.sd",
                condicion_venta: "12",
                plazo_credito: "12",
                medio_pago: "12",
                medios_pago: [],
                cod_moneda: "12",
                tipo_cambio: "570",
                total_serv_gravados: "1",
                total_serv_exentos: "1",
                total_serv_exonerados: "1",
                total_merc_gravada: "1",
                total_merc_exenta: "1",
                total_merc_exonerada: "1",
                total_gravados: "1",
                total_exento: "1",
                total_exonerado: "1",
                total_ventas: "1",
                total_descuentos: "1",
                total_ventas_neta: "1",
                total_impuestos: "1",
                totalIVADevuelto: "1",
                totalOtrosCargos: "1",
                total_comprobante: "1",
                otros: "1",
                otrosType: "1",
                detalles: {"1": ["1", "Sp", "Honorarios", "100000", "100000", "100000", "100000", "1000", "Pronto pago", "0"]},
                infoRefeTipoDoc: "1",
                infoRefeNumero: "1",
                infoRefeFechaEmision: "1",
                infoRefeCodigo: "1",
                infoRefeRazon: "1",
                otrosCargos: []
            }
            console.log(req)
            calaApi_postRequest(req, success, error)
        }

        var service = {
            registerUser: registerUserApi,
            checkLogin: calaApi_checkLogin,
            login: calaApiApi_login,
            logOut: calaApiApi_logOut,
            recoverPwd: calaApiApi_recoverPwd,
            resultMsg: calaApi_resultToMsg,
            setLocalStorage: calaApi_setLocalStorage,
            getLocalStorage: calaApi_getLocalStorage,
            debug: calaApi_debug,
            postRequest: calaApi_postRequest,
            doSomethingAfter: calaApi_doSomethingAfter,
            keyCreationForXml: keyCreationForXml,
            keyCreationAceptacionComprobante: keyCreationAceptacionComprobante,
            keyCreationAceptacionParcialComprobante: keyCreationAceptacionParcialComprobante,
            keyCreationRechazoComprobante: keyCreationRechazoComprobante,
            uploadLlaveCriptografica: uploadLlaveCriptografica,
            newFE: newFE
        };

        return service;
    }
})();
