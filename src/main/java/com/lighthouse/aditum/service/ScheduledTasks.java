package com.lighthouse.aditum.service;

/**
 * Created by Sergio on 18/05/2018.
 */

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ReservationHouseRestrictionsService reservationHouseRestrictionsService;
    private final PushNotificationService pushNotificationService;
    private final CompanyService companyService;


    public ScheduledTasks(CompanyService companyService, PushNotificationService pushNotificationService, CommonAreaService commonAreaService, ReservationHouseRestrictionsService reservationHouseRestrictionsService, FireBaseService fireBaseService, CompanyConfigurationService companyConfigurationService, RoundService roundService, RoundConfigurationService roundConfigurationService, PaymentDocumentService paymentDocumentService, BancoService bancoService, BalanceByAccountService balanceByAccountService, BalanceByAccountMapper balanceByAccountMapper, AdministrationConfigurationService administrationConfigurationService, ChargeService chargeService, HouseService houseService) {
        this.bancoService = bancoService;
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

    //    Cada 30 segundos prueba
//  @Scheduled(cron = "*/30 * * * * *")
//    Todos los dias a las 12 am
    @Scheduled(cron = "0 0 0 1/1 * ?")
    @Async
    public void registrarRecargosEnCuotas() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            if (administrationConfigurationDTO.isHasSubcharges()) {
                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
                houseDTOS.forEach(houseDTO -> {
                    this.chargeService.createSubchargeInCharges(administrationConfigurationDTO, houseDTO);
                });
            }
        });
        log.debug("Creando Recargos");
    }

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
                houseDTOS.forEach(houseDTO -> {
                    List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseAndBetweenDate(currency, houseDTO.getId(), ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0), ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59)).getContent();
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
                            this.paymentDocumentService.sendChargeEmail(administrationConfigurationDTO, houseDTO, chargeDTO);
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
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            if (administrationConfigurationDTO.isHasSubcharges()) {
                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
                houseDTOS.forEach(houseDTO -> {
                    List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouse(houseDTO.getId()).getContent();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.chargeService.sendReminderEmailAndMorosos(administrationConfigurationDTO, houseDTO, chargeDTOS);
                });
            }
        });
        log.debug("Enviando correos de cuotas");
    }


    //    Todos los dias a las 12 am
    @Scheduled(cron = "0 0 0 1/1 * ?")
    //    Cada 30 segundos prueba
//    @Scheduled(cron = "*/30 * * * * *")
    @Async
    public void crearRondas() throws ExecutionException, InterruptedException, URISyntaxException {
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

}
