package com.lighthouse.aditum.service;

/**
 * Created by Sergio on 18/05/2018.
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterConstants;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final BancoService bancoService;
    private final BalanceByAccountService balanceByAccountService;
    private final BalanceByAccountMapper balanceByAccountMapper;
    private final AdministrationConfigurationService administrationConfigurationService;
    private final ChargeService chargeService;
    private final HouseService houseService;
    private final PaymentDocumentService paymentDocumentService;
    private final RoundConfigurationService roundConfigurationService;
    private final RoundService roundService;
    private final CompanyConfigurationService companyConfigurationService;
    private final FireBaseService fireBaseService;
    private final CommonAreaService commonAreaService;
    private final CommonAreaReservationsService commonAreaReservationsService;
    private final ReservationHouseRestrictionsService reservationHouseRestrictionsService;
    private final PushNotificationService pushNotificationService;
    private final CompanyService companyService;
    private final Environment env;
    private final CustomChargeTypeService customChargeTypeService;
    private final HistoricalPositiveService historicalPositiveService;
    private final HistoricalDefaulterService historicalDefaulterService;
    private final PaymentService paymentService;
    private final BalanceService balanceService;


    public ScheduledTasks(BalanceService balanceService, PaymentService paymentService, HistoricalPositiveService historicalPositiveService, HistoricalDefaulterService historicalDefaulterService, CustomChargeTypeService customChargeTypeService, CommonAreaReservationsService commonAreaReservationsService, Environment env, CompanyService companyService, PushNotificationService pushNotificationService, CommonAreaService commonAreaService, ReservationHouseRestrictionsService reservationHouseRestrictionsService, FireBaseService fireBaseService, CompanyConfigurationService companyConfigurationService, RoundService roundService, RoundConfigurationService roundConfigurationService, PaymentDocumentService paymentDocumentService, BancoService bancoService, BalanceByAccountService balanceByAccountService, BalanceByAccountMapper balanceByAccountMapper, AdministrationConfigurationService administrationConfigurationService, ChargeService chargeService, HouseService houseService) {
        this.bancoService = bancoService;
        this.commonAreaReservationsService = commonAreaReservationsService;
        this.balanceByAccountService = balanceByAccountService;
        this.balanceByAccountMapper = balanceByAccountMapper;
        this.administrationConfigurationService = administrationConfigurationService;
        this.chargeService = chargeService;
        this.houseService = houseService;
        this.paymentDocumentService = paymentDocumentService;
        this.roundConfigurationService = roundConfigurationService;
        this.roundService = roundService;
        this.companyConfigurationService = companyConfigurationService;
        this.fireBaseService = fireBaseService;
        this.commonAreaService = commonAreaService;
        this.reservationHouseRestrictionsService = reservationHouseRestrictionsService;
        this.pushNotificationService = pushNotificationService;
        this.companyService = companyService;
        this.env = env;
        this.customChargeTypeService = customChargeTypeService;
        this.historicalPositiveService = historicalPositiveService;
        this.historicalDefaulterService = historicalDefaulterService;
        this.paymentService = paymentService;
        this.balanceService = balanceService;
    }

    @Async
    public void formatAllOptimize() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
            "INICIA arreglo balance si no tiene",
            ""));
//        for (int i = 0; i < administrationConfigurationDTOS.size(); i++) {
//            Long companyId = administrationConfigurationDTOS.get(i).getCompanyId();
//            this.houseService.formatIfDoesntHaveBalance(companyId,0,administrationConfigurationDTOS.size());
//            this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
//                "Progreso:" + i + "/" + administrationConfigurationDTOS.size(),
//                "Listo "+i));
//        }
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
            "TODO LISTO  arreglo balance si no tiene",
            "Suerte :)"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
            "INICIA formato morosidad historica diciembre",
            ""));
//        for (int i = 0; i < administrationConfigurationDTOS.size(); i++) {
//            Long companyId = administrationConfigurationDTOS.get(i).getCompanyId();
            this.historicalDefaulterService.formatCompanyDefaulterNotWorking(Long.parseLong("2"));
        this.historicalDefaulterService.formatCompanyDefaulterNotWorking(Long.parseLong("15"));
//            this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
//                "Progreso:" + i + "/" + administrationConfigurationDTOS.size(),
//                "Listo "+i));
//        }
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(2 + ""), this.pushNotificationService.createPushNotification(
            "TODO LISTO formato morosidad historica diciembre",
            "Suerte :)"));
    }

    public void formatOptimizeAsync(Long companyId, int progress, int total) throws URISyntaxException {
        CompanyDTO c = this.companyService.findOne(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " INICIA " + c.getName(),
            ""));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (1/4) INICIO formateando histórico de saldos a favor"));
        this.historicalPositiveService.formatHistoricalPositiveReportCompany(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (1/4) FIN formateando histórico de saldos a favor"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (2/4) INICIO formateando histórico de morosos"));
        this.historicalDefaulterService.formatHistorialDefaulterReportCompany(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (2/4) FIN formateando histórico de morosos"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (3/4) INICIO formateando nuevos pagos"));
        this.paymentService.formatNewPayments(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (3/4) FIN formateando nuevos pagos"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (4/4) INICIO formateando nuevas cuotas"));
        this.paymentService.formatOldCharges(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (4/4) FIN formateando nuevas cuotas"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " FINALIZA " + c.getName(),
            ""));
    }

    public void formatOptimize(Long companyId, int progress, int total) throws URISyntaxException {
        CompanyDTO c = this.companyService.findOne(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " INICIA " + c.getName(),
            ""));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (1/4) INICIO formateando histórico de saldos a favor"));
        this.historicalPositiveService.formatHistoricalPositiveReportCompany(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (1/4) FIN formateando histórico de saldos a favor"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (2/4) INICIO formateando histórico de morosos"));
        this.historicalDefaulterService.formatHistorialDefaulterReportCompany(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (2/4) FIN formateando histórico de morosos"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (3/4) INICIO formateando nuevos pagos"));
        this.paymentService.formatNewPayments(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (3/4) FIN formateando nuevos pagos"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (4/4) INICIO formateando nuevas cuotas"));
        this.paymentService.formatOldCharges(companyId);
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " " + c.getName(),
            "PASO (4/4) FIN formateando nuevas cuotas"));
        this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1 + ""), this.pushNotificationService.createPushNotification(
            progress + "/" + total + " FINALIZA " + c.getName(),
            ""));
    }


    //Cada inicio de mes
    @Scheduled(cron = "0 0 12 1 1/1 ?")
    @Async
    public void registrarMensualBalancePorBanco() {
        List<Banco> bancos = bancoService.findAllCompanies(null);
        String a = "a";
        bancos.forEach(banco -> {
            BalanceByAccount newBalanceAccount = new BalanceByAccount();
            newBalanceAccount.setAccountId(banco.getId());
            newBalanceAccount.setBalance(banco.getSaldo() + "");
            newBalanceAccount.setDate(ZonedDateTime.now());
            balanceByAccountService.save(balanceByAccountMapper.toDto(newBalanceAccount));
        });
        log.debug("Registrando Balance Mensual");
    }

    //Cada inicio de mes
    @Scheduled(cron = "0 0 12 1 1/1 ?")
    @Async
    public void formatearSaldosAFiliales() {
        List<Banco> bancos = bancoService.findAllCompanies(null);
        String a = "a";
        bancos.forEach(banco -> {
            BalanceByAccount newBalanceAccount = new BalanceByAccount();
            newBalanceAccount.setAccountId(banco.getId());
            newBalanceAccount.setBalance(banco.getSaldo() + "");
            newBalanceAccount.setDate(ZonedDateTime.now());
            balanceByAccountService.save(balanceByAccountMapper.toDto(newBalanceAccount));
        });
        log.debug("Registrando Balance Mensual");
    }

    //    Cada 30 segundos prueba
//  @Scheduled(cron = "*/30 * * * * *")
//    Todos los dias a las 12 am
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Async
    public void registrarRecargosEnCuotas() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
//        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
//            if (administrationConfigurationDTO.isHasSubcharges()) {
//                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
//                houseDTOS.forEach(houseDTO -> {
//                    this.chargeService.createSubchargeInCharges(administrationConfigurationDTO, houseDTO);
//                });
//            }
//        });
        log.debug("Creando Recargos");
    }
    //    Cada 30 segundos prueba
//  @Scheduled(cron = "*/30 * * * * *")
//    //Cada inicio de mes
////    @Scheduled(cron = "0 0 12 1 1/1 ?")
//    @Async
//    public void formateandoSaldosFiliales() {
//        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
//        ZonedDateTime m = ZonedDateTime.now().plusHours(2);
//        m.withMonth(m.getMonthValue()-1);
//        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
//            if(administrationConfigurationDTO.getCompanyId()==2){
//            List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
//            List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(administrationConfigurationDTO.getCompanyId());
//            houseDTOS.forEach(houseDTO -> {
//                this.historicalDefaulterService.formatResetHouse(houseDTO.getId(),m.withMonth(m.getMonthValue()-1),customChargeTypes);
//            });
//            }
//        });
//        try {
//            this.pushNotificationService.sendNotificationToSpecificAdmin(Long.parseLong(1+""),this.pushNotificationService.createPushNotification(
//                "FORMATEADO DE SALDOS A FAVOR E HISTORICOS LISTO",
//                "REVISAR"));
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        log.debug("REVISAR!");
//    }


    //TODOS LOS DIAS A LA 6 am
//   @Scheduled(cron = "*/30 * * * * *")
    @Scheduled(cron = "0 0 6 * * ?")
    @Async
    public void enviarCorreosDeCuotas() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            if (administrationConfigurationDTO.isHasSubcharges()) {
                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
                String currency = companyConfigurationService.getByCompanyId(null, administrationConfigurationDTO.getCompanyId()).getContent().get(0).getCurrency();
                List<CustomChargeTypeDTO> customChargeTypes = this.customChargeTypeService.findAllByCompany(administrationConfigurationDTO.getCompanyId());
                houseDTOS.forEach(houseDTO -> {
                    List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseAndBetweenDate(currency, houseDTO.getId(), ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0), ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59), customChargeTypes).getContent();
                    chargeDTOS.forEach(chargeDTO -> {
                        if (chargeDTO.getState() == 1) {
                            try {
                                this.pushNotificationService.sendNotificationsToOwnersByHouse(chargeDTO.getHouseId(),
                                    this.pushNotificationService.createPushNotification(chargeDTO.getConcept() + " - " + houseDTO.getHousenumber(),
                                        "Se ha creado una nueva cuota en su filial por un monto de " + currency + "" + formatMoney(currency, Double.parseDouble(chargeDTO.getAmmount())) + "."));
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                this.paymentDocumentService.sendChargeEmail(administrationConfigurationDTO, houseDTO, chargeDTO);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });
            }
        });
        log.debug("Enviando correos de cuotas");
    }

    //     Cada 5 dias a las 6 am
    @Scheduled(cron = "0 0 6 1/5 * ?")
//   @Scheduled(cron = "*/30 * * * * *")
    @Async
    public void enviarRecordatorioCuotas() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
//        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
//            if (administrationConfigurationDTO.isHasSubcharges()) {
//                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
//                houseDTOS.forEach(houseDTO -> {
//                    List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouse(houseDTO.getId()).getContent();
//                    try {
//                        Thread.sleep(10000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    this.chargeService.sendReminderEmailAndMorosos(administrationConfigurationDTO, houseDTO, chargeDTOS);
//                });
//            }
//        });
        log.debug("Enviando correos de cuotas");
    }


    //    Todos los dias a las 12 am
    @Scheduled(cron = "0 0 0 1/1 * ?")
    //    Cada 30 segundos prueba
//    @Scheduled(cron = "*/30 * * * * *")
    @Async
    public void crearRondas() throws ExecutionException, InterruptedException, URISyntaxException {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        for (int i = 0; i < administrationConfigurationDTOS.size(); i++) {
            AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationDTOS.get(i);
            Long companyId = administrationConfigurationDTO.getCompanyId();
            boolean hasRounds = this.companyConfigurationService.getOneByCompanyId(companyId).isHasRounds();
            if (hasRounds) {
                try {
                    List<RoundConfigurationDTO> rConfigs = this.roundConfigurationService.getAllByCompany(companyId + "");
                    this.roundService.createRounds(rConfigs, companyId);
                    this.pushNotificationService.sendNotificationAllAdminsByCompanyId(companyId,
                        this.pushNotificationService.createPushNotification(
                            "Rondas de oficiales - " + this.companyService.findOne(companyId).getName()
                            , "Se han creado las rondas de los oficiales del día de hoy correctamente."));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            }
        }
    }


    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Async
    public void formatearReservasPorPeriodo() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withHour(1).withSecond(0).withMinute(0).withNano(0);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaDTO> commonAreas = this.commonAreaService.findAll(null, administrationConfigurationDTO.getCompanyId().intValue()).getContent();
            commonAreas.forEach(commonAreaDTO -> {
                if (commonAreaDTO.getHasReservationsLimit() == 1) {
                    if (commonAreaDTO.getPeriodBegin().plusMonths(commonAreaDTO.getPeriodMonthEnd()).withHour(1).withSecond(0).withMinute(0).withNano(0).equals(now)) {
                        List<ReservationHouseRestrictionsDTO> reservationHouseRestrictions = this.reservationHouseRestrictionsService.findAllByCommonArea(commonAreaDTO.getId());
                        reservationHouseRestrictions.forEach(reservationHouseRestrictionsDTO -> {
                            reservationHouseRestrictionsDTO.setReservationQuantity(0);
                            this.reservationHouseRestrictionsService.save(reservationHouseRestrictionsDTO);
                        });
                        commonAreaDTO.setPeriodBegin(now);
                        this.commonAreaService.save(commonAreaDTO);
                    }
                }
            });
        });
        log.debug("Formateando reservas por periodo");
    }

    //    Cada 2 horas
//        @Scheduled(cron = "*/30 * * * * *")
    @Scheduled(cron = "0 0 * ? * *")
    @Async
    public void enviarRecordatorioDeReserva2HorasAntes() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime inTwoHours = now.plusHours(2);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaReservationsDTO> commonAreaReservations = this.commonAreaReservationsService.findByDatesBetweenAndCompanyHours(inTwoHours, administrationConfigurationDTO.getCompanyId()).getContent();
            commonAreaReservations.forEach(commonAreaReservationsDTO -> {
                if (commonAreaReservationsDTO.getStatus() == 2) {
                    try {
                        this.pushNotificationService.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),
                            this.pushNotificationService.createPushNotification(
                                "¡Recuerda tu reserva en " + commonAreaReservationsDTO.getCommonArea().getName() + "!"
                                , "Realizaste la reserva para hoy a las " + formatter.format(commonAreaReservationsDTO.getInitalDate()) + ", sino utilizarás la amenidad por favor cancela tu reservación."));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        log.debug("Enviando recordatorios de reservas 2 horas antes");
    }

    //    Cada hora
    @Scheduled(cron = "0 0 * ? * *")
    @Async
    public void enviarRecordatorioDeReserva1HorasAntes() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime inTwoHours = now.plusHours(1);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaReservationsDTO> commonAreaReservations = this.commonAreaReservationsService.findByDatesBetweenAndCompanyHours(inTwoHours, administrationConfigurationDTO.getCompanyId()).getContent();
            commonAreaReservations.forEach(commonAreaReservationsDTO -> {
                if (commonAreaReservationsDTO.getStatus() == 2) {
                    try {
                        this.pushNotificationService.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),
                            this.pushNotificationService.createPushNotification(
                                "¡Recuerda tu reserva en " + commonAreaReservationsDTO.getCommonArea().getName() + "!"
                                , "Realizaste la reserva para hoy a las " + formatter.format(commonAreaReservationsDTO.getInitalDate()) + ", sino utilizarás la amenidad por favor cancela tu reservación."));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        log.debug("Enviando recordatorios de reservas 1 hora antes");
    }

    //    Cada hora
    @Scheduled(cron = "0 0 * ? * *")
    @Async
    public void notificarInicioDeReserva() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaReservationsDTO> commonAreaReservations = this.commonAreaReservationsService.findByDatesBetweenAndCompanyHours(now, administrationConfigurationDTO.getCompanyId()).getContent();
            commonAreaReservations.forEach(commonAreaReservationsDTO -> {
                if (commonAreaReservationsDTO.getStatus() == 2) {
                    try {
                        this.pushNotificationService.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),
                            this.pushNotificationService.createPushNotification(
                                "¡Comienza tu reserva en " + commonAreaReservationsDTO.getCommonArea().getName() + "!"
                                , "Tu tiempo de uso finaliza: " + formatter.format(commonAreaReservationsDTO.getFinalDate()) + "."));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        log.debug("Enviando notifiacion inicio de reserva");
    }

    @Scheduled(cron = "0 0 * ? * *")
    @Async
    public void notificarFinDeReserva() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaReservationsDTO> commonAreaReservations = this.commonAreaReservationsService.findByDatesBetweenAndCompanyHoursEnd(now, administrationConfigurationDTO.getCompanyId()).getContent();
            commonAreaReservations.forEach(commonAreaReservationsDTO -> {
                if (commonAreaReservationsDTO.getStatus() == 2) {
                    try {
                        this.pushNotificationService.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),
                            this.pushNotificationService.createPushNotification(
                                "¡Final de tu reservación en " + commonAreaReservationsDTO.getCommonArea().getName() + "!"
                                , formatter.format(commonAreaReservationsDTO.getFinalDate()) + " - Esperamos hayas disfrutado de la amenidad :)"));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        log.debug("Enviando recordatorios de reservas finalizacion");
    }

    //    Cada 50 minutos de la hora
    @Scheduled(cron = "0 50/50 * ? * *")
    @Async
    public void enviarRecordatorioDeReserva10MinAntes() throws URISyntaxException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        ZonedDateTime now = ZonedDateTime.now().withMinute(50).withSecond(0).withNano(0);
        ZonedDateTime inTwoHours = now.plusMinutes(10);
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(locale);
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            List<CommonAreaReservationsDTO> commonAreaReservations = this.commonAreaReservationsService.findByDatesBetweenAndCompanyHours(inTwoHours, administrationConfigurationDTO.getCompanyId()).getContent();
            commonAreaReservations.forEach(commonAreaReservationsDTO -> {
                if (commonAreaReservationsDTO.getStatus() == 2) {
                    try {
                        this.pushNotificationService.sendNotificationToResident(commonAreaReservationsDTO.getResidentId(),
                            this.pushNotificationService.createPushNotification(
                                "¡Tu reserva comienza en 10 minutos!"
                                , "Sino utilizarás " + commonAreaReservationsDTO.getCommonArea().getName() + " por favor cancela tu reservación."));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        log.debug("Enviando recordatorios de reservas 10 min antes");
    }
}
