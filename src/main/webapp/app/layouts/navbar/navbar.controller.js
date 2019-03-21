(function () {
    'use strict';

    angular
        .module('aditumApp')
        .controller('NavbarController', NavbarController);
    NavbarController.$inject = ['WSHouse', 'WSResident', 'WSVehicle', 'WSNote', 'WSVisitor', 'WSOfficer', '$timeout', 'CommonMethods', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'MultiCompany', '$rootScope', '$scope', 'companyUser', 'Company', 'House', '$mdSidenav', '$localStorage', 'globalCompany', 'WSDeleteEntity', 'WSEmergency'];

    function NavbarController(WSHouse, WSResident, WSVehicle, WSNote, WSVisitor, WSOfficer, $timeout, CommonMethods, $state, Auth, Principal, ProfileService, LoginService, MultiCompany, $rootScope, $scope, companyUser, Company, House, $mdSidenav, $localStorage, globalCompany, WSDeleteEntity, WSEmergency) {
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

        $scope.$on("$destroy", function () {
            vm.menu = [];
        })
        vm.chargeMenu = function () {
            vm.menu = [
                {
                    title: "",
                    activeOn: "company,condons,admins,recursosHumanos,brands,destinies,dataprogress",
                    authoritites: "ROLE_ADMIN",
                    showXs: true,
                    hasContability: true,
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
                            showXs: true,
                            showLg: true,
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
                                    thirdItems: [],
                                    showXs: true,

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
                                    thirdItems: [],
                                    showXs: true,

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
                                    thirdItems: [],
                                    showXs: true,

                                },
                                {
                                    title: "Soporte",
                                    icon: "info",
                                    authoritites: "ROLE_ADMIN",
                                    activeOn: "soporte",
                                    collapsable: false,
                                    uisref: "soporte",
                                    menuId: "",
                                    hover: false,
                                    thirdItems: [],
                                    showXs: true,
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
                                    thirdItems: [],
                                    showXs: true,

                                },
                                {
                                    title: "Destinos puerta acceso",
                                    icon: "arrow_right_alt",
                                    authoritites: "ROLE_ADMIN",
                                    activeOn: "destinies",
                                    collapsable: false,
                                    uisref: "destinies",
                                    menuId: "",
                                    hover: false,
                                    showXs: true,

                                },
                                {
                                    title: "Progreso ingreso de datos",
                                    icon: "show_chart",
                                    authoritites: "ROLE_ADMIN",
                                    activeOn: "dataprogress",
                                    collapsable: false,
                                    uisref: "data-progress",
                                    menuId: "",
                                    hover: false,
                                    showXs: true
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
                            showXs: true,
                            showLg: true,
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
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
                                    thirdItems: [],
                                    showXs: true
                                },
                            ]
                        },
                    ]
                },
                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
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
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
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
                                    showXs: true
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
                                    showXs: true
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
                            showXs: true,
                            showLg: true,
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
                                    showXs: true
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
                                    showXs: true
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
                                    showXs: true
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
                                    showXs: true
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
                            showXs: true,
                            showLg: true,
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
                                    thirdItems: [],
                                    showXs: true
                                },
                                {
                                    title: "Turnos",
                                    icon: "access_time",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "watches",
                                    collapsable: false,
                                    uisref: "turno",
                                    menuId: "",
                                    hover: false,
                                    thirdItems: [],
                                    showXs: true
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
                            secondaryItems: [],
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: vm.hasContability,
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
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ver egresos",
                            icon: "remove_red_eye",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "egress",
                            collapsable: false,
                            uisref: "egress-tabs.egress",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: false,
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
                            showXs: false,
                            showLg: true,
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
                                    icon: "payment",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "newEgress",
                                    collapsable: false,
                                    uisref: "egress-tabs.new",
                                    menuId: "",
                                    hover: false,
                                    showXs: false
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
                            showXs: false,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Capturar ingreso",
                                    icon: "payment",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "generatePayment",
                                    collapsable: false,
                                    uisref: "generatePayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false
                                },
                                {
                                    title: "Capturar adelanto",
                                    icon: "redo",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "capturarAdelanto",
                                    collapsable: false,
                                    uisref: "advancePayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false
                                },
                                {
                                    title: "Otro ingreso",
                                    icon: "money",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "otroIngreso",
                                    collapsable: false,
                                    uisref: "otherPayment",
                                    menuId: "",
                                    hover: false,
                                    showXs: false
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
                            showXs: false,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Mensuales",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "mensual",
                                    collapsable: false,
                                    uisref: "mensualCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true
                                },
                                {
                                    title: "Extraordinarias",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "extraordinary",
                                    collapsable: false,
                                    uisref: "extraordinaryCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true
                                },
                                {
                                    title: "Individual",
                                    icon: "remove",
                                    authoritites: "ROLE_MANAGER",
                                    activeOn: "individual",
                                    collapsable: false,
                                    uisref: "individualCharge",
                                    menuId: "",
                                    hover: false,
                                    showXs: true
                                },

                            ]
                        },
                        {
                            title: "Comprobantes de pago",
                            icon: "description",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "paymentProof",
                            collapsable: false,
                            uisref: "paymentProof.pending",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
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
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
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
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Saldo de filiales",
                            icon: "library_books",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "balance",
                            collapsable: false,
                            uisref: "houses-balance",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

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
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        }
                    ]
                },
                {
                    title: "Áreas comunes",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Administrar",
                            icon: "view_agenda",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reservationAdministration",
                            collapsable: false,
                            uisref: "common-area-administration.common-area",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reservaciones",
                            icon: "view_comfy",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "reservations",
                            collapsable: false,
                            uisref: "common-area-administration.common-area-reservations",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Devoluciones",
                            icon: "payment",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "devolutions",
                            collapsable: false,
                            uisref: "common-area-devolution-administration.pending-devolution",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                    ]
                },
                {
                    title: "Reportes",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: false,
                    hasContability: vm.hasContability,
                    secondaryItems: [
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuentas por pagar",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_MANAGER,ROLE_JD",
                            activeOn: "cuentasPorPagar",
                            collapsable: false,
                            uisref: "egress-to-pay",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Cuotas por cobrar",
                            icon: "list",
                            authoritites: "ROLE_MANAGER,ROLE_JD",
                            activeOn: "reporteCuotasPorPagar",
                            collapsable: false,
                            uisref: "chargesReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Configuración",
                    activeOn: "",
                    authoritites: "ROLE_MANAGER",
                    showXs: false,
                    hasContability: vm.hasContability,
                    secondaryItems: [
                        {
                            title: "General",
                            icon: "build",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "administrationConfiguration",
                            collapsable: false,
                            uisref: "administration-configuration-detail",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Junta directiva",
                            icon: "record_voice_over",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "jdAccount",
                            collapsable: false,
                            uisref: "junta-directiva-account.new",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Activos fijos",
                            icon: "category",
                            authoritites: "ROLE_MANAGER",
                            activeOn: "activosFijos",
                            collapsable: false,
                            uisref: "activos-fijos",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
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
                            showXs: true,
                            showLg: true,
                        },

                    ]
                },
                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: true,
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
                            showXs: true,
                            showLg: true,
                        },

                        {
                            title: "Quejas y sugerencias",
                            icon: "sentiment_very_dissatisfied",
                            authoritites: "ROLE_USER",
                            activeOn: "complaint-user",
                            collapsable: false,
                            uisref: "complaint-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_USER",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hasContability: vm.hasContability,
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_USER",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            hasContability: vm.hasContability,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ],

                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: vm.hasContability,
                    secondaryItems: [

                        {
                            title: "Estado de cuenta",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_USER",
                            activeOn: "residentAccountStatus",
                            collapsable: false,
                            uisref: "accountStatus-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                        {
                            title: "Deudas",
                            icon: "assignment",
                            authoritites: "ROLE_USER",
                            activeOn: "chargesResidentAccount",
                            collapsable: false,
                            uisref: "chargePerHouse-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Pagos",
                            icon: "payment",
                            authoritites: "ROLE_USER",
                            activeOn: "paymentsResidentAccount",
                            collapsable: false,
                            uisref: "paymentsPerHouse-residentAccount",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Comprobantes de pago",
                            icon: "description",
                            authoritites: "ROLE_USER",
                            activeOn: "paymentProof",
                            collapsable: false,
                            uisref: "paymentProof.pending-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "ADMINISTRAR MI FILIAL",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [

                        {
                            title: "Usuarios",
                            icon: "group",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsHouses",
                            collapsable: false,
                            uisref: "residentByHouse",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Véhiculos",
                            icon: "directions_car",
                            authoritites: "ROLE_USER",
                            activeOn: "vehiculesHouses",
                            collapsable: false,
                            uisref: "vehiculeByHouse",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Bitácora de visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsVisitors",
                            collapsable: false,
                            uisref: "visitant",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Visitantes invitados",
                            icon: "account_circle",
                            authoritites: "ROLE_USER",
                            activeOn: "residentsInvitedVisitors",
                            collapsable: false,
                            uisref: "visitant-invited-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Clave de seguridad",
                            icon: "vpn_key",
                            authoritites: "ROLE_USER",
                            activeOn: "keysConfiguration",
                            collapsable: false,
                            uisref: "keysConfiguration",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        }

                    ]
                },

                {
                    title: "Reportar",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [

                        {
                            title: "Visitante",
                            icon: "perm_identity",
                            authoritites: "ROLE_USER",
                            activeOn: "reportInvitation",
                            collapsable: false,
                            uisref: "visitant-invited-user.new",
                            menuId: "",
                            hover: false,
                            showLg: true,
                            showXs: true
                        },
                        {
                            title: "Reunión o fiesta",
                            icon: "group_add",
                            authoritites: "ROLE_USER",
                            activeOn: "reportInvitationList",
                            collapsable: false,
                            uisref: "visitant-invited-user.new-list",
                            menuId: "",
                            hover: false,
                            showLg: true,
                            showXs: true
                        },
                        {
                            title: "Nota a oficial",
                            icon: "note",
                            authoritites: "ROLE_USER",
                            activeOn: "reportHomeService",
                            collapsable: false,
                            uisref: "noteNew",
                            menuId: "",
                            hover: false,
                            showLg: true,
                            showXs: true
                        },
                        {
                            title: "Reportar emergencia",
                            icon: "local_hospital",
                            authoritites: "ROLE_USER",
                            activeOn: "reportemergencyactive",
                            collapsable: false,
                            uisref: "emergency.new",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "ÁREAS COMUNES",
                    activeOn: "",
                    authoritites: "ROLE_USER",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [

                        {
                            title: "Ver todas",
                            icon: "view_agenda",
                            authoritites: "ROLE_USER",
                            activeOn: "common-area-resident-account,reservationCalendarResidentView",
                            collapsable: false,
                            uisref: "common-area-resident-account",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Reservar",
                            icon: "event_available",
                            authoritites: "ROLE_USER",
                            activeOn: "reservationDialogResidentView",
                            collapsable: false,
                            uisref: "common-area-reservation-resident-view",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Mis reservaciones",
                            icon: "view_comfy",
                            authoritites: "ROLE_USER",
                            activeOn: "allReservationsResidentsView",
                            collapsable: false,
                            uisref: "common-area-all-reservations-resident-view",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                        },

                    ]
                },


                {
                    title: "Condominio",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Dashboard",
                            icon: "dashboard",
                            authoritites: "ROLE_JD",
                            activeOn: "dashboard",
                            collapsable: false,
                            uisref: "dashboard",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: true,
                            showLg: true,
                        },
                        {
                            title: "Noticias",
                            icon: "picture_in_picture",
                            authoritites: "ROLE_JD",
                            activeOn: "userNews",
                            collapsable: false,
                            uisref: "announcement-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },
                        {
                            title: "Visitantes",
                            icon: "group_add",
                            authoritites: "ROLE_JD",
                            activeOn: "adminVisitors",
                            collapsable: false,
                            uisref: "visitant-admin",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: []
                        },
                        {
                            title: "Seguridad",
                            icon: "security",
                            authoritites: "ROLE_JD",
                            activeOn: "officers,watches",
                            collapsable: true,
                            uisref: "",
                            menuId: "seguridadMenuJD",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            thirdItems: [
                                {
                                    title: "Oficiales",
                                    icon: "people_outline",
                                    authoritites: "ROLE_JD",
                                    activeOn: "officers",
                                    collapsable: false,
                                    uisref: "officer",
                                    menuId: "",
                                    hover: false,
                                    thirdItems: [],
                                    showXs: true
                                },
                                {
                                    title: "Turnos",
                                    icon: "timer",
                                    authoritites: "ROLE_JD",
                                    activeOn: "watches",
                                    collapsable: false,
                                    uisref: "turno",
                                    menuId: "",
                                    hover: false,
                                    thirdItems: [],
                                    showXs: true
                                },
                            ]
                        },
                        {
                            title: "Sugerencias",
                            icon: "feedback",
                            authoritites: "ROLE_JD",
                            activeOn: "complaint",
                            collapsable: false,
                            uisref: "complaint",
                            menuId: "",
                            hover: false,
                            secondaryItems: [],
                            showXs: true,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Finanzas",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Presupuestos",
                            icon: "trending_up",
                            authoritites: "ROLE_JD",
                            activeOn: "presupuestos",
                            collapsable: false,
                            uisref: "presupuesto",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Bancos",
                            icon: "account_balance",
                            authoritites: "ROLE_JD",
                            activeOn: "bancos",
                            collapsable: false,
                            uisref: "banco",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Saldo de filiales",
                            icon: "library_books",
                            authoritites: "ROLE_JD",
                            activeOn: "balance",
                            collapsable: false,
                            uisref: "houses-balance",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        },
                        {
                            title: "Tabla de cobranza",
                            icon: "grid_on",
                            authoritites: "ROLE_JD",
                            activeOn: "collectionTable",
                            collapsable: false,
                            uisref: "collection-table",
                            menuId: "",
                            hover: false,
                            thirdItems: [],
                            showXs: false,
                            showLg: true,

                        }
                    ]
                },
                {
                    title: "Reportes",
                    activeOn: "",
                    authoritites: "ROLE_JD",
                    showXs: false,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Estado de resultados",
                            icon: "equalizer",
                            authoritites: "ROLE_JD",
                            activeOn: "estadoResultados",
                            collapsable: false,
                            uisref: "resultStates.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Ejec. presupuestaria",
                            icon: "monetization_on",
                            authoritites: "ROLE_JD",
                            activeOn: "budgetExecution",
                            collapsable: false,
                            uisref: "budgetExecution.mensualReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Reporte de egresos",
                            icon: "keyboard_backspace",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteGastos",
                            collapsable: false,
                            uisref: "egress-tabs.egress-report",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Reporte de ingresos",
                            icon: "trending_flat",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteIngresos",
                            collapsable: false,
                            uisref: "payment-report",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Cuentas por pagar",
                            icon: "account_balance_wallet",
                            authoritites: "ROLE_JD",
                            activeOn: "cuentasPorPagar",
                            collapsable: false,
                            uisref: "egress-to-pay",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                        {
                            title: "Cuotas por cobrar",
                            icon: "list",
                            authoritites: "ROLE_JD",
                            activeOn: "reporteCuotasPorPagar",
                            collapsable: false,
                            uisref: "chargesReport",
                            menuId: "",
                            hover: false,
                            showXs: false,
                            showLg: true,
                        },
                    ]
                },
                {
                    title: "Soporte",
                    icon: "contact_support",
                    authoritites: "ROLE_RH,ROLE_MANAGER,ROLE_USER,ROLE_JD",
                    activeOn: "soporte",
                    collapsable: false,
                    uisref: "soporte",
                    menuId: "",
                    hover: false,
                    showXs: true,
                    showLg: true,
                    hasContability: true,
                    secondaryItems: [
                        {
                            title: "Soporte",
                            icon: "live_help",
                            authoritites: "ROLE_RH,ROLE_MANAGER,ROLE_USER,ROLE_JD",
                            activeOn: "soporte-user",
                            collapsable: false,
                            uisref: "soporte-user",
                            menuId: "",
                            hover: false,
                            showXs: true,
                            showLg: true,
                            secondItems: []
                        },
                        // {
                        //     title: "Manual de usuario",
                        //     icon: "book",
                        //     authoritites: "ROLE_USER",
                        //     activeOn: "manualResidente",
                        //     collapsable: false,
                        //     uisref: "manualResidente",
                        //     menuId: "",
                        //     hover: false,
                        //     showXs: true,
                        //     showLg: true,
                        //     secondItems: []
                        // },
                    ]
                },
            ];
            showTheOneThatsActive()
        }


        vm.loadCompanyConfig = function () {
            var companyConfig = CommonMethods.getCurrentCompanyConfig(globalCompany.getId());
            if (companyConfig == "admin") {
                vm.hasContability = true;
            } else {
                if (companyConfig.hasContability == 1) {
                    vm.hasContability = true;
                } else {
                    vm.hasContability = false;
                }
            }
            vm.chargeMenu();
        };

        vm.showSecondItem = function (secondItem) {
            var secondItemsToHideIfHasNoContability = ["Devoluciones", "Ejec. presupuestaria", 'Estado de resultados', ''];
            var items = secondItemsToHideIfHasNoContability;
            var ocultar = 0;
            for (var i = 0; i < items.length; i++) {
                if (secondItem.title == items[i]) {
                    ocultar++;
                }
            }
            if (ocultar > 0 && !vm.hasContability) {
                return false;
            } else {
                return secondItem.showLg
            }
        };

        $scope.$watch(function () {
            return $localStorage.companiesConfig;
        }, function (newCodes, oldCodes) {
            vm.loadCompanyConfig(globalCompany.getId())
        });


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

        function unsubscribe() {
            WSDeleteEntity.unsubscribe(globalCompany.getId());
            WSEmergency.unsubscribe(globalCompany.getId());
            WSHouse.unsubscribe(globalCompany.getId());
            WSResident.unsubscribe(globalCompany.getId());
            WSVehicle.unsubscribe(globalCompany.getId());
            WSNote.unsubscribe(globalCompany.getId());
            WSVisitor.unsubscribe(globalCompany.getId());
            WSOfficer.unsubscribe(globalCompany.getId());
        }

        function logout() {
            collapseNavbar();
            Principal.identity().then(function (account) {
                switch (account.authorities[0]) {
                    case "ROLE_OFFICER":
                        $timeout.cancel($rootScope.timerAd);
                        unsubscribe();
                        break;
                }
            });
            Auth.logout();
            $localStorage.houseSelected = undefined;
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
            var encryptedId = CommonMethods.encryptIdUrl(globalCompany.getId())
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
                                Company.get({id: parseInt(globalCompany.getId())}, function (condo) {
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
                    case "ROLE_JD":
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
                                $rootScope.currentUserImage = null;
                                $rootScope.companyUser.name = "Junta";
                                $rootScope.companyUser.lastname = "Directiva";
                                if (data.enabled == 0) {
                                    logout();
                                }
                            });

                            $rootScope.hideFilial = true;
                        });
                        break;
                }
            })
        }

        Principal.identity().then(function (account) {
            if (account !== null) {
                $rootScope.companyUser = companyUser;
                vm.getAcount();
            }
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
            $localStorage.houseSelected = undefined;
            setTimeout(function () {
                $scope.$apply(function () {
                    vm.getAcount();
                    vm.loadCompanyConfig()
                })
            }, 300);
            $state.reload();
        };

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
