(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['CommonMethods', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'MultiCompany', '$rootScope', '$scope', 'companyUser', 'Company', 'House', '$mdSidenav', '$localStorage', 'globalCompany'];

    function NavbarController(CommonMethods, $state, Auth, Principal, ProfileService, LoginService, MultiCompany, $rootScope, $scope, companyUser, Company, House, $mdSidenav, $localStorage, globalCompany) {
        var vm = this;



        vm.colors = {primary: "rgb(0,150,136)", secondary: "#E1F5FE", normalColorFont: "#37474f"};
        $rootScope.colors = vm.colors;
        vm.colorsMenu = {
            mainButton: {
                color: 'color:' + '#37474f',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonActive: {
                color: 'color:' + '#37474f',
                backgroundColor: 'background-color:white!important',
            },
            mainButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            secondButtonActive: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:' + vm.colors.secondary + '!important',
            },
            secondButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButton: {
                color: 'color:' + vm.colors.normalColorFont + '!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonActive: {
                color: 'color:' + '#f37960!important',
                backgroundColor: 'background-color:white!important',
            },
            thirdButtonHover: {
                color: 'color:' + vm.colors.primary + '!important',
                backgroundColor: 'background-color:white!important',
            }
        };

        vm.menu = [
            {
                title: "",
                activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress",
                authoritites: "ROLE_ADMIN",
                secondaryItems: [
                    {
                        title: "Administración",
                        icon: "assessment",
                        authoritites: "ROLE_ADMIN",
                        activeOn: "condons,admins,admins,recursosHumanos,brands,destinies,dataprogress",
                        collapsable: true,
                        uisref: "",
                        menuId: "administracionSuperAdminMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Condominios",
                                icon: "home",
                                authoritites: "ROLE_ADMIN",
                                activeOn: "condons",
                                collapsable: false,
                                uisref: "company",
                                menuId: "",
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false
                            },
                            {
                                title: "Progreso ingreso de datos",
                                icon: "show_chart",
                                authoritites: "ROLE_ADMIN",
                                activeOn: "dataprogress",
                                collapsable: false,
                                uisref: "data-progress",
                                menuId: "",
                                hover: false
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
                        hover: false,
                        thirdItems: [
                            {
                                title: "Gestión de usuarios",
                                icon: "supervised_user_circle",
                                authoritites: "ROLE_ADMIN",
                                activeOn: "user-management",
                                collapsable: false,
                                uisref: "user-management",
                                menuId: "",
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
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
                                hover: false,
                                thirdItems: []
                            },
                        ]
                    },
                ]
            },
            {
                title: "",
                activeOn: "dashboard",
                authoritites: "ROLE_MANAGER",
                secondaryItems: [
                    {
                        title: "Dashboard",
                        icon: "dashboard",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "dashboard",
                        collapsable: false,
                        uisref: "dashboard",
                        menuId: "",
                        hover: false,
                        thirdItems: []
                    },
                    {
                        title: "Noticias",
                        icon: "picture_in_picture",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "announcements,userNews",
                        collapsable: true,
                        uisref: "",
                        menuId: "noticiasMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Ver noticias",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "userNews",
                                collapsable: false,
                                uisref: "announcement-user",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Administrar noticias",
                                icon: "view_agenda",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "announcements",
                                collapsable: false,
                                uisref: "announcements.announcement",
                                menuId: "",
                                hover: false,
                            },
                        ]
                    },

                    {
                        title: "Administración",
                        icon: "location_city",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "residents,vehicules,houses,adminVisitors",
                        collapsable: true,
                        uisref: "",
                        menuId: "administracionMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Usuarios",
                                icon: "group",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "residents",
                                collapsable: false,
                                uisref: "resident",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Filiales",
                                icon: "home",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "houses",
                                collapsable: false,
                                uisref: "houses-tabs.house",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Vehículos",
                                icon: "directions_car",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "vehicules",
                                collapsable: false,
                                uisref: "vehicule",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Visitantes",
                                icon: "group_add",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "adminVisitors",
                                collapsable: false,
                                uisref: "visitant-admin",
                                menuId: "",
                                hover: false,
                            },
                        ]
                    },
                    {
                        title: "Seguridad",
                        icon: "security",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "officers,watches",
                        collapsable: true,
                        uisref: "",
                        menuId: "seguridadMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Oficiales",
                                icon: "people_outline",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "officers",
                                collapsable: false,
                                uisref: "officer",
                                menuId: "",
                                hover: false,
                                thirdItems: []
                            },
                            {
                                title: "Turnos",
                                icon: "timer",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "watches",
                                collapsable: false,
                                uisref: "turno",
                                menuId: "",
                                hover: false,
                                thirdItems: []
                            },
                        ]
                    },
                ]
            },
            {
                title: "Finanzas",
                activeOn: "",
                authoritites: "ROLE_MANAGER",
                secondaryItems: [
                    {
                        title: "Contabilidad filiales",
                        icon: "dvr",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "houseAdministration",
                        collapsable: false,
                        uisref: "houseAdministration.accountStatus",
                        menuId: "",
                        hover: false,
                        thirdItems: []
                    },
                    {
                        title: "Egresos",
                        icon: "call_received",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "egress,newEgress",
                        collapsable: true,
                        uisref: "",
                        menuId: "egresosMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Ver egresos",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "egress",
                                collapsable: false,
                                uisref: "egress-tabs.egress",
                                menuId: "",
                                hover: false
                            },
                            {
                                title: "Capturar egresos",
                                icon: "plus_one",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "newEgress",
                                collapsable: false,
                                uisref: "egress-tabs.new",
                                menuId: "",
                                hover: false
                            },
                        ]
                    },
                    {
                        title: "Ingresos",
                        icon: "call_made",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "generatePayment,capturarAdelanto",
                        collapsable: true,
                        uisref: "",
                        menuId: "ingresosMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Capturar ingreso",
                                icon: "payment",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "generatePayment",
                                collapsable: false,
                                uisref: "generatePayment",
                                menuId: "",
                                hover: false
                            },
                            {
                                title: "Capturar adelanto",
                                icon: "redo",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "capturarAdelanto",
                                collapsable: false,
                                uisref: "advancePayment",
                                menuId: "",
                                hover: false
                            },

                        ]
                    },
                    {
                        title: "Generar cuotas",
                        icon: "turned_in",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "mensual,extraordinary,individual",
                        collapsable: true,
                        uisref: "",
                        menuId: "cuotasMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Mensuales",
                                icon: "remove",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "mensual",
                                collapsable: false,
                                uisref: "mensualCharge",
                                menuId: "",
                                hover: false
                            },
                            {
                                title: "Extraordinarias",
                                icon: "remove",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "extraordinary",
                                collapsable: false,
                                uisref: "extraordinaryCharge",
                                menuId: "",
                                hover: false
                            },
                            {
                                title: "Individual",
                                icon: "remove",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "individual",
                                collapsable: false,
                                uisref: "individualCharge",
                                menuId: "",
                                hover: false
                            },

                        ]
                    },
                    {
                        title: "Presupuestos",
                        icon: "trending_up",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "presupuestos",
                        collapsable: false,
                        uisref: "presupuesto",
                        menuId: "",
                        hover: false,
                        thirdItems: []
                    },
                    {
                        title: "Bancos",
                        icon: "account_balance",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "bancos",
                        collapsable: false,
                        uisref: "banco",
                        menuId: "",
                        hover: false,
                        thirdItems: []
                    },
                    {
                        title: "Saldos",
                        icon: "library_books",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "balance",
                        collapsable: false,
                        uisref: "houses-balance",
                        menuId: "",
                        hover: false,
                        thirdItems: []

                    },
                    {
                        title: "Tabla de cobranza",
                        icon: "grid_on",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "collectionTable",
                        collapsable: false,
                        uisref: "collection-table",
                        menuId: "",
                        hover: false,
                        thirdItems: []

                    },
                    {
                        title: "Reportes",
                        icon: "pie_chart",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "estadoResultados,reporteGastos,reporteIngresos",
                        collapsable: true,
                        uisref: "",
                        menuId: "reportesMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Estado de resultados",
                                icon: "equalizer",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "estadoResultados",
                                collapsable: false,
                                uisref: "budgetExecution.mensualReport",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Reporte de egresos",
                                icon: "keyboard_backspace",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "reporteGastos",
                                collapsable: false,
                                uisref: "egress-tabs.egress-report",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Reporte de ingresos",
                                icon: "trending_flat",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "reporteIngresos",
                                collapsable: false,
                                uisref: "payment-report",
                                menuId: "",
                                hover: false,
                            },
                        ]
                    },
                    {
                        title: "Áreas comunes",
                        icon: "local_florist",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "reservationAdministration,generaCalendar,createReservation",
                        collapsable: true,
                        uisref: "",
                        menuId: "areasComunesMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Administrar",
                                icon: "view_agenda",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "reservationAdministration",
                                collapsable: false,
                                uisref: "common-area-administration.common-area",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Ver calendario",
                                icon: "event_note",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "generaCalendar",
                                collapsable: false,
                                uisref: "common-area-administration.general-reservation-calendar",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Reservar",
                                icon: "event_available",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "createReservation",
                                collapsable: false,
                                uisref: "common-area-administration.newReservation",
                                menuId: "",
                                hover: false,
                            },

                        ]
                    },
                    {
                        title: "Configuración",
                        icon: "settings",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "proovedor,configureCharges,bancoConfiguration,egressCategories,proovedores",
                        collapsable: true,
                        uisref: "",
                        menuId: "configMenu",
                        hover: false,
                        thirdItems: [
                            {
                                title: "General",
                                icon: "build",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "administrationConfiguration",
                                collapsable: false,
                                uisref: "administration-configuration-detail",
                                menuId: "",
                                hover: false
                            },
                            {
                                title: "Proveedores",
                                icon: "store_mall_directory",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "proovedores",
                                collapsable: false,
                                uisref: "proveedor",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Cuotas",
                                icon: "turned_in",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "configureCharges",
                                collapsable: false,
                                uisref: "configureCharges",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Cuentas de banco",
                                icon: "account_balance",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "bancoConfiguration",
                                collapsable: false,
                                uisref: "banco-configuration",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Categoría de egresos",
                                icon: "call_received",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "egressCategories",
                                collapsable: false,
                                uisref: "egress-category",
                                menuId: "",
                                hover: false,
                            },
                        ]
                    },
                    {
                        title: "Gestionar quejas",
                        icon: "sentiment_very_dissatisfied",
                        authoritites: "ROLE_MANAGER",
                        activeOn: "complaint",
                        collapsable: false,
                        uisref: "complaint",
                        menuId: "",
                        hover: false,
                        secondaryItems: []
                    },
                ]
            },
            {
                title: "",
                activeOn: "",
                authoritites: "ROLE_USER",
                secondaryItems: [
                    {
                        title: "Noticias",
                        icon: "picture_in_picture",
                        authoritites: "ROLE_USER",
                        activeOn: "userNews",
                        collapsable: false,
                        uisref: "announcement-user",
                        menuId: "",
                        hover: false,
                    },
                    {
                        title: "Administrar",
                        icon: "picture_in_picture",
                        authoritites: "ROLE_USER",
                        activeOn: "reportInvitation,reportInvitationList,reportHomeService",
                        collapsable: true,
                        uisref: "",
                        menuId: "administrarMenuUser",
                        hover: false,
                        thirdItems: [
                            {
                                title: "Residentes",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_USER",
                                activeOn: "reportInvitation",
                                collapsable: false,
                                uisref: "visitant-invited-user.new",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Véhiculos",
                                icon: "build",
                                authoritites: "ROLE_USER",
                                activeOn: "reportInvitationList",
                                collapsable: false,
                                uisref: "visitant-invited-user.new-list",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Bitácora de visitantes",
                                icon: "build",
                                authoritites: "ROLE_USER",
                                activeOn: "residentsVisitors",
                                collapsable: false,
                                uisref: "visitant",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Visitantes invitados",
                                icon: "build",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "residentsInvitedVisitors",
                                collapsable: false,
                                uisref: "visitant-invited-user",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Clave de seguridad",
                                icon: "build",
                                authoritites: "ROLE_MANAGER",
                                activeOn: "keysConguration",
                                collapsable: false,
                                uisref: "keysConguration",
                                menuId: "",
                                hover: false,
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
                        hover: false,
                    },
                    {
                        title: "Reportar emergencia",
                        icon: "picture_in_picture",
                        authoritites: "ROLE_USER",
                        activeOn: "reportemergencyactive",
                        collapsable: false,
                        uisref: "emergency.new",
                        menuId: "",
                        hover: false,
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
                        hover: false,
                        thirdItems: [
                            {
                                title: "Visitante",
                                icon: "remove_red_eye",
                                authoritites: "ROLE_USER",
                                activeOn: "reportInvitation",
                                collapsable: false,
                                uisref: "visitant-invited-user.new",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Reunión o fiesta",
                                icon: "build",
                                authoritites: "ROLE_USER",
                                activeOn: "reportInvitationList",
                                collapsable: false,
                                uisref: "visitant-invited-user.new-list",
                                menuId: "",
                                hover: false,
                            },
                            {
                                title: "Servicio a domicilio",
                                icon: "build",
                                authoritites: "ROLE_USER",
                                activeOn: "reportHomeService",
                                collapsable: false,
                                uisref: "note.new",
                                menuId: "",
                                hover: false,
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
                        hover: false,
                    },

                    {
                        title: "Soporte",
                        icon: "picture_in_picture",
                        authoritites: "ROLE_USER,ROLE_RH,ROLE_MANAGER",
                        activeOn: "soporte",
                        collapsable: false,
                        uisref: "soporte",
                        menuId: "",
                        hover: false,
                    },
                ]
            },

        ];


        vm.defineStyleSecondButton = function (item) {
            if (item.hover) {
                return vm.colorsMenu.secondButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.secondButtonActive
            } else {
                return vm.colorsMenu.secondButton
            }
        };
        vm.defineStyleThirdButton = function (item) {
            if (item.hover) {
                return vm.colorsMenu.thirdButtonHover;
            }
            if (this.defineActive(item) == true) {
                return vm.colorsMenu.thirdButtonActive
            } else {
                return vm.colorsMenu.thirdButton
            }
        };
        vm.defineStyleMainButton = function (item) {
            if (item.hover) {
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

        vm.collapseAllOther = function (item, fatherItem) {
            if (fatherItem != null) {
                $(".collapse:not(#" + item.menuId + ",#" + fatherItem.menuId + ")").collapse('hide');
            } else {
                $(".collapse:not(#" + item.menuId + ")").collapse('hide');

            }
        };
        setTimeout(function () {
            showTheOneThatsActive()
        }, 500)

        function showTheOneThatsActive() {
            for (var i = 0; i < vm.menu.length; i++) {
                for (var j = 0; j < vm.menu[i].secondaryItems.length; j++) {
                    var secondaryItem = vm.menu[i].secondaryItems[j];
                    if (secondaryItem.collapsable) {
                        for (var k = 0; k < secondaryItem.thirdItems.length; k++) {
                            if (secondaryItem.thirdItems[k].activeOn.includes($rootScope.active)) {
                                $("#" + secondaryItem.menuId).collapse('show');
                            }
                        }
                    }
                }
            }

        }


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
                            if ($localStorage.companyId == undefined) {
                                $rootScope.companyUser = data;
                                $rootScope.companyUser.companyId = data.companies[0].id;
                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companies[0].id);
                            }
                            Company.get({id: globalCompany.getId()}, function (condo) {
                                vm.contextLiving = condo.name;
                                $rootScope.companyName = condo.name;
                                $rootScope.contextLiving = vm.contextLiving;
                                $rootScope.currentUserImage = data.image_url;
                                if (data.enabled == 0) {
                                    logout();
                                }
                            });

                            $rootScope.hideFilial = true;
                        });
                        break;
                    case "ROLE_OFFICER":
                        MultiCompany.getCurrentUserCompany().then(function (data) {
                            $rootScope.companyUser = data;
                            $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
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
                                $localStorage.companyId = CommonMethods.encryptIdUrl(data.companyId);
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


        vm.selectCompany = function (company) {
            $localStorage.companyId = CommonMethods.encryptIdUrl(company.id);
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.getAcount();
                })
            }, 300)
            $state.reload();
        }

        vm.defineSelectCompanyColor = function (company) {
            if (company.id == globalCompany.getId()) {
                return vm.colorsMenu.secondButtonActive;
            } else {
                return vm.colorsMenu.secondButton;
            }
        };
//        $scope.$on('$destroy', subChangeState);
        $scope.$on('$destroy', subLogin);

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
            }
            return false;
        }
    }
})
();
