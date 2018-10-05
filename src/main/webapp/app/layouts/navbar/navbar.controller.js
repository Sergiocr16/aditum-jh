(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'MultiCompany', '$rootScope', '$scope', 'companyUser', 'Company', 'House', '$mdSidenav'];

    function NavbarController(CommonMethods, $state, Auth, Principal, ProfileService, LoginService, MultiCompany, $rootScope, $scope, companyUser, Company, House, $mdSidenav) {
        var vm = this;
        vm.colors = {primary: "#01579B", secondary: "#E1F5FE",normalColorFont:"##c3c3c3"};
        vm.colorsMenu = {
            mainButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonActive: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:' + vm.colors.secondary + '!important',
            },
            mainButtonHover:{
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonActive: {
                color: 'color:black!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonHover:{
                color: 'color:black!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonActive: {
                color: 'color:#474747!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonHover:{
                color: 'color:#474747!important',
                backgroundColor: 'background-color:white!important',
            }
        };

        // MENU EXAMPLE STRUDCTURE
        // vm.menu = [
        //     {
        //         title: "Administración",
        //         icon: "assessment",
        //         authoritites: "ROLE_ADMIN",
        //         activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress",
        //         collapsable: true,
        //         uisref: "",
        //         menuId: "administracionMenu",
        //         secondaryItems: [
        //             {
        //                 title: "Condominios",
        //                 icon: "home",
        //                 authoritites: "ROLE_ADMIN",
        //                 activeOn: "company",
        //                 collapsable: true,
        //                 uisref: "",
        //                 menuId: "condosMenu",
        //                 thirdItems: [
        //                     {
        //                         title: "Carros",
        //                         icon: "home",
        //                         authoritites: "ROLE_ADMIN",
        //                         activeOn: "company",
        //                         uisref: "admins"
        //                     },
        //                 ]
        //             }
        //         ]
        //     }
        // ];
        // END MENU EXAMPLE STRUCTURE
        vm.menu = [
            {
                title: "Administración",
                icon: "assessment",
                authoritites: "ROLE_ADMIN",
                activeOn: "condons,admins,admins,recursosHumanos,brands,destinies,dataprogress",
                collapsable: true,
                uisref: "",
                menuId: "administracionSuperAdminMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Condominios",
                        icon: "home",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "condons",
                        collapsable: false,
                        uisref: "company",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Administradores",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "admins",
                        collapsable: false,
                        uisref: "admin-info",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Recursos humanos",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "recursosHumanos",
                        collapsable: false,
                        uisref: "rh-account",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Marcas vehículos",
                        icon: "directions_car",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "brands",
                        collapsable: false,
                        uisref: "brand",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Destinos puerta acceso",
                        icon: "arrow_right_alt",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "destinies",
                        collapsable: false,
                        uisref: "destinies",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Progreso ingreso de datos",
                        icon: "show_chart",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "dataprogress",
                        collapsable: false,
                        uisref: "data-progress",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Aditum Jhipster",
                icon: "bar_chart",
                authoritites: "ROLE_ADMIN",
                activeOn: "user-management,tracker,metrics,health,configuration,audits,logs,docs",
                collapsable: true,
                uisref: "",
                menuId: "aditumJhipsterMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Gestión de usuarios",
                        icon: "supervised_user_circle",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "user-management",
                        collapsable: false,
                        uisref: "user-management",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Tracker",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "tracker",
                        collapsable: false,
                        uisref: "jhi-tracker",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Métricas",
                        icon: "pie_chart",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "metrics",
                        collapsable: false,
                        uisref: "jhi-metrics",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Salud",
                        icon: "sentiment_satisfied_alt",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "health",
                        collapsable: false,
                        uisref: "jhi-health",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Configuración",
                        icon: "build",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "configuration",
                        collapsable: false,
                        uisref: "jhi-configuration",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Auditorías",
                        icon: "people",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "audits",
                        collapsable: false,
                        uisref: "audits",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Logs",
                        icon: "pan_tool",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "logs",
                        collapsable: false,
                        uisref: "logs",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "API",
                        icon: "library_books",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "docs",
                        collapsable: false,
                        uisref: "docs",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Dashboard",
                icon: "dashboard",
                authoritites: "ROLE_MANAGER",
                activeOn: "dashboard",
                collapsable: false,
                uisref: "dashboard",
                menuId: "",
                hover:false,
                secondaryItems: []
            },
            {
                title: "Noticias",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "announcements,userNews",
                collapsable: true,
                uisref: "",
                menuId: "noticiasMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Ver noticias",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "userNews",
                        collapsable: false,
                        uisref: "announcement-user",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Administrar noticias",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "announcements",
                        collapsable: false,
                        uisref: "announcement",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },

            {
                title: "Administración",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "residents,vehicules,houses,adminVisitors",
                collapsable: true,
                uisref: "",
                menuId: "administracionMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Residentes",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "residents",
                        collapsable: false,
                        uisref: "resident",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Filiales",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "houses",
                        collapsable: false,
                        uisref: "house",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Vehículos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "vehicules",
                        collapsable: false,
                        uisref: "vehicule",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Visitantes",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "adminVisitors",
                        collapsable: false,
                        uisref: "visitant-admin",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Seguridad",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "officers,watches",
                collapsable: true,
                uisref: "",
                menuId: "seguridadMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Oficiales",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "officers",
                        collapsable: false,
                        uisref: "officer",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Turnos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "watches",
                        collapsable: false,
                        uisref: "vm.viewWatch()",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },


                ]
            },
            {
                title: "Seleccionar condominio",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "selectCondominio",
                collapsable: false,
                uisref: "dashboard.selectCompany",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
            {
                title: "Finanzas",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "egress,newEgress,houseAdministration",
                collapsable: true,
                uisref: "",
                menuId: "finanzasMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Contabilidad filiales",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "houseAdministration",
                        collapsable: false,
                        uisref: "houseAdministration.accountStatus",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Egresos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "egress,newEgress",
                        collapsable: true,
                        uisref: "",
                        menuId: "egresosMenu",
                        hover:false,
                        thirdItems: [
                            {
                                title: "Ver gastos",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "egress",
                                collapsable: false,
                                uisref: "egress",
                                menuId: "",
                                hover:false
                            },
                            {
                                title: "Capturar gasto",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "newEgress",
                                collapsable: false,
                                uisref: "egress.new",
                                menuId: "",
                                hover:false
                            },
                        ]
                    },
                    {
                        title: "Ingresos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "generatePayment,capturarAdelanto",
                        collapsable: true,
                        uisref: "",
                        menuId: "ingresosMenu",
                        hover:false,
                        thirdItems: [
                            {
                                title: "Capturar ingreso",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "generatePayment",
                                collapsable: false,
                                uisref: "generatePayment",
                                menuId: "",
                                hover:false
                            },
                            {
                                title: "Capturar adelanto",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "capturarAdelanto",
                                collapsable: false,
                                uisref: "advancePayment",
                                menuId: "",
                                hover:false
                            },

                        ]
                    },
                    {
                        title: "Cuotas",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "mensual,extraordinary,individual",
                        collapsable: true,
                        uisref: "",
                        menuId: "cuotasMenu",
                        hover:false,
                        thirdItems: [
                            {
                                title: "Generar cuotas mensuales",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "mensual",
                                collapsable: false,
                                uisref: "mensualCharge",
                                menuId: "",
                                hover:false
                            },
                            {
                                title: "Generar cuotas extraordinarias",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "extraordinary",
                                collapsable: false,
                                uisref: "extraordinaryCharge",
                                menuId: "",
                                hover:false
                            },
                            {
                                title: "Generar cuota individual",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "individual",
                                collapsable: false,
                                uisref: "individualCharge",
                                menuId: "",
                                hover:false
                            },

                        ]
                    },
                    {
                        title: "Presupuestos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "presupuestos",
                        collapsable: false,
                        uisref: "presupuesto",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Bancos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "bancos",
                        collapsable: false,
                        uisref: "banco",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Saldos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "balance",
                        collapsable: false,
                        uisref: "houses-balance",
                        menuId: "",
                        hover:false,
                        thirdItems: []

                    },
                    {
                        title: "Tabla de cobranza",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "collectionTable",
                        collapsable: false,
                        uisref: "collection-table",
                        menuId: "",
                        hover:false,
                        thirdItems: []

                    },
                    {
                        title: "Configuración",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "administrationConfiguration",
                        collapsable: false,
                        uisref: "administration-configuration-detail",
                        menuId: "",
                        hover:false,
                        thirdItems: []

                    },
                ]
            },
            {
                title: "Reportes",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "estadoResultados,reporteGastos,reporteIngresos",
                collapsable: true,
                uisref: "",
                menuId: "reportesMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Estado de resultados",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "estadoResultados",
                        collapsable: false,
                        uisref: "budgetExecution.mensualReport",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Reporte de gastos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "reporteGastos",
                        collapsable: false,
                        uisref: "egress-report",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Reporte de ingresos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "reporteIngresos",
                        collapsable: false,
                        uisref: "payment-report",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },

                ]
            },
            {
                title: "Áreas comunes",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "estadoResultados,reporteGastos,reporteIngresos",
                collapsable: true,
                uisref: "",
                menuId: "areasComunesMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Administrar",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "estadoResultados",
                        collapsable: false,
                        uisref: "budgetExecution.mensualReport",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Ver calendario",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "reporteGastos",
                        collapsable: false,
                        uisref: "egress-report",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Reservar",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "reporteIngresos",
                        collapsable: false,
                        uisref: "payment-report",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },

                ]
            },
            {
                title: "Configuración",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "proovedor,configureCharges,bancoConfiguration,egressCategories",
                collapsable: true,
                uisref: "",
                menuId: "areasComunesMenu",
                hover:false,
                secondaryItems: [
                    {
                        title: "Proveedores",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "proovedor",
                        collapsable: false,
                        uisref: "proveedor",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Cuotas",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "configureCharges",
                        collapsable: false,
                        uisref: "configureCharges",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Cuentas de banco",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "bancoConfiguration",
                        collapsable: false,
                        uisref: "banco-configuration",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Categoría de egresos",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "egressCategories",
                        collapsable: false,
                        uisref: "egress-category",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Gestionar quejas",
                icon: "picture_in_picture",
                authoritites: "ROLE_MANAGER",
                activeOn: "complaint",
                collapsable: false,
                uisref: "complaint",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
            {
                title: "Noticias",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "userNews",
                collapsable: false,
                uisref: "announcement-user",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },

            {
                title: "Administrar",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "reportInvitation,reportInvitationList,reportHomeService",
                collapsable: true,
                uisref: "",
                menuId: "administrarMenuUser",
                hover:false,
                secondaryItems: [
                    {
                        title: "Residentes",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_USER",
                        activeOn: "reportInvitation",
                        collapsable: false,
                        uisref: "visitant-invited-user.new",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Véhiculos",
                        icon: "build",
                        authoritites: "ROLE_USER",
                        activeOn: "reportInvitationList",
                        collapsable: false,
                        uisref: "visitant-invited-user.new-list",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Bitácora de visitantes",
                        icon: "build",
                        authoritites: "ROLE_USER",
                        activeOn: "residentsVisitors",
                        collapsable: false,
                        uisref: "visitant",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Visitantes invitados",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "residentsInvitedVisitors",
                        collapsable: false,
                        uisref: "visitant-invited-user",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Clave de seguridad",
                        icon: "build",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "keysConguration",
                        collapsable: false,
                        uisref: "keysConguration",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                ]
            },
            {
                title: "Finanzas",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "financesResidentAccount",
                collapsable: false,
                uisref: "finanzasPorCasa",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
            {
                title: "Reportar emergencia",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "reportemergencyactive",
                collapsable: false,
                uisref: "emergency.new",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
            // {
            //     title: "Manual de usuario",
            //     icon: "picture_in_picture",
            //     authoritites: "ROLE_USER,ROLE_RH",
            //     activeOn: "soporte",
            //     collapsable: false,
            //     uisref: "soporte",
            //     menuId: "",
            //     hover:false,
            //     secondaryItems: [ ]
            // },
            {
                title: "Reportar",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "reportInvitation,reportInvitationList,reportHomeService",
                collapsable: true,
                uisref: "",
                menuId: "reportarMenuUser",
                hover:false,
                secondaryItems: [
                    {
                        title: "Visitante",
                        icon: "remove_red_eye",
                        authoritites: "ROLE_USER",
                        activeOn: "reportInvitation",
                        collapsable: false,
                        uisref: "visitant-invited-user.new",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Reunión o fiesta",
                        icon: "build",
                        authoritites: "ROLE_USER",
                        activeOn: "reportInvitationList",
                        collapsable: false,
                        uisref: "visitant-invited-user.new-list",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },
                    {
                        title: "Servicio a domicilio",
                        icon: "build",
                        authoritites: "ROLE_USER",
                        activeOn: "reportHomeService",
                        collapsable: false,
                        uisref: "note.new",
                        menuId: "",
                        hover:false,
                        thirdItems: []
                    },

                ]
            },
            {
                title: "Quejas y sugerencias",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER",
                activeOn: "complaint-user",
                collapsable: false,
                uisref: "complaint-user",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },

            {
                title: "Soporte",
                icon: "picture_in_picture",
                authoritites: "ROLE_USER,ROLE_RH",
                activeOn: "soporte",
                collapsable: false,
                uisref: "soporte",
                menuId: "",
                hover:false,
                secondaryItems: [ ]
            },
        ];




        vm.defineStyleSecondButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.secondButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.secondButtonActive
            } else {
                return vm.colorsMenu.secondButton
            }
        };
        vm.defineStyleThirdButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.thirdButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.thirdButtonActive
            } else {
                return vm.colorsMenu.thirdButton
            }
        };
        vm.defineStyleMainButton = function (item) {
            if(item.hover){
                return vm.colorsMenu.mainButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.mainButtonActive
            } else {
                return vm.colorsMenu.mainButton
            }
        };
        vm.defineActive = function (item) {
            var items = item.activeOn.split(",");
            var count = 0;
            for (var i = 0; i < items.length; i++) {
                if ($rootScope.active == items[i]) {
                    count++;
                }
            }
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        };

        vm.go = function (uisref) {
            $rootScope.toggleLeft();
            $state.go(uisref)
        };

        vm.collapseAllOther = function (item,fatherItem) {
            if(fatherItem!=null) {
                $(".collapse:not(#" + item.menuId + ",#" + fatherItem.menuId + ")").collapse('hide');
            }else{
                $(".collapse:not(#" + item.menuId + ")").collapse('hide');

            }
        };


        $rootScope.isAuthenticated = Principal.isAuthenticated;
        vm.isAuthenticated = Principal.isAuthenticated;
        $rootScope.toggleLeft = buildToggler('left');

        function buildToggler(componentId) {
            return function () {
                $mdSidenav(componentId).toggle();
            };
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $rootScope.companyUser = undefined;
            $state.go('home');
            $rootScope.menu = false;
            $rootScope.companyId = undefined;
            $rootScope.showLogin = true;
            $rootScope.inicieSesion = false;
        }

        if ($rootScope.inicieSesion == undefined) {
            $rootScope.inicieSesion = true;
        }
        $rootScope.$on('$stateChangeStart',
            function (event, toState, toParams, fromState, fromParams) {
                MultiCompany.getCurrentUserCompany().then(function (data) {
                    if (data != undefined) {
                        if (data.enable == 0 || data.enabled == 0) {
                            logout();
                        }
                    }
                })
            })
        vm.annoActual = moment(new Date()).format("YYYY");
        vm.editMyInfoAsManager = function () {
            $state.go('admin-info-edit')
        }
        vm.editMyInfoAsUser = function () {
            var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyUser.id)
            $state.go('residentByHouse.edit', {
                id: encryptedId
            })
        }
        angular.element(document).ready(function () {
            $('body').addClass("gray");
        });
        vm.viewWatch = function () {
            var encryptedId = CommonMethods.encryptIdUrl($rootScope.companyId)
            $state.go('turno', {companyId: encryptedId})
        }

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function (response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;


        vm.getAcount = function () {
            Principal.identity().then(function (account) {
                vm.account = account;
                switch (account.authorities[0]) {
                    case "ROLE_ADMIN":
                        vm.contextLiving = "Dios de Aditum";
                        $rootScope.contextLiving = vm.contextLiving;
                        $rootScope.currentUserImage = null;
                        $rootScope.hideFilial = true;
                        break;
                    case "ROLE_MANAGER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            if (data.companies.length > 1) {
                                $rootScope.showSelectCompany = true;
                            }
                            if (data.companies.length > 1 && $rootScope.companyId == undefined) {
                                $state.go('dashboard.selectCompany');
                            } else {
                                if ($rootScope.companyId == undefined) {
                                    $rootScope.companyUser = data;
                                    $rootScope.companyUser.companyId = data.companies[0].id;
                                    $rootScope.companyId = data.companies[0].id;

                                }
                                Company.get({id: $rootScope.companyId}, function (condo) {
                                    vm.contextLiving = condo.name;
                                    $rootScope.companyName = condo.name;
                                    $rootScope.contextLiving = vm.contextLiving;
                                    $rootScope.currentUserImage = data.image_url;
                                    if (data.enabled == 0) {
                                        logout();
                                    }
                                })
                            }
                            $rootScope.hideFilial = true;
                        })
                        break;
                    case "ROLE_OFFICER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $rootScope.companyId = data.companyId;
                            if (data != null) {
                                vm.contextLiving = $rootScope.companyUser.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_USER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            House.get({id: parseInt(data.houseId)}, function (house) {
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.hideFilial = false;
                                $rootScope.filialNumber = house.housenumber;
                                $rootScope.companyId = data.companyId;
                                $rootScope.currentUserImage = data.image_url;
                                $rootScope.companyUser = data;
                                Company.get({id: parseInt($rootScope.companyId)}, function (condo) {
                                    vm.contextLiving = condo.name;
                                    $rootScope.contextLiving = vm.contextLiving;
                                    if (condo.active == 0 || data.enabled == 0) {
                                        logout();
                                    }
                                })
                            })
                        })
                        break;
                    case "ROLE_RH":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            if (data != null) {
                                vm.contextLiving = " / " + data.enterprisename;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = null;
                            }
                            if (data.enable == 0) {
                                logout();
                            }
                        })
                        break;
                }
            })
        }

        setTimeout(function () {
            Principal.identity().then(function (account) {
                if (account !== null) {
                    $rootScope.companyUser = companyUser;
                    vm.getAcount();
                }
            })
        })
        var subLogin = $scope.$on('authenticationSuccess', vm.getAcount);


        function login() {
            collapseNavbar();
            LoginService.open();
        }


        vm.openManual = function () {
            window.open('/#/manual-residente', '_blank')
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

//        $scope.$on('$destroy', subChangeState);
        $scope.$on('$destroy', subLogin);
    }
})
();
