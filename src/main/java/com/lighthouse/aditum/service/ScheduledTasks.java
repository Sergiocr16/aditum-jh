package com.lighthouse.aditum.service;

/**
 * Created by Sergio on 18/05/2018.
 */

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.AdministrationConfigurationDTO;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.RoundConfigurationDTO;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    public ScheduledTasks(CompanyConfigurationService companyConfigurationService, RoundService roundService, RoundConfigurationService roundConfigurationService, PaymentDocumentService paymentDocumentService, BancoService bancoService, BalanceByAccountService balanceByAccountService, BalanceByAccountMapper balanceByAccountMapper, AdministrationConfigurationService administrationConfigurationService, ChargeService chargeService, HouseService houseService) {
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
    }

    //Cada inicio de mes
    @Scheduled(cron = "0 0 12 1 1/1 ?")
    @Async
    public void registrarMensualBalancePorBanco() {
        List<Banco> bancos = bancoService.findAllCompanies(null);
        bancos.forEach(banco -> {
            BalanceByAccount newBalanceAccount = new BalanceByAccount();
            newBalanceAccount.setAccountId(banco.getId());
            newBalanceAccount.setBalance(Integer.parseInt(banco.getSaldo()));
            newBalanceAccount.setDate(ZonedDateTime.now());
            balanceByAccountService.save(balanceByAccountMapper.toDto(newBalanceAccount));
        });
        log.debug("Registrando Balance Mensual");
    }

    //    Cada 30 segundos prueba
//    @Scheduled(cron = "*/30 * * * * *")
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
    @Scheduled(cron = "0 0 6 * * ?")
    @Async
    public void enviarCorreosDeCuotas() {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        administrationConfigurationDTOS.forEach(administrationConfigurationDTO -> {
            if (administrationConfigurationDTO.isHasSubcharges()) {
                List<HouseDTO> houseDTOS = this.houseService.findAll(administrationConfigurationDTO.getCompanyId()).getContent();
                houseDTOS.forEach(houseDTO -> {
                    List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseAndBetweenDate(houseDTO.getId(), ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0), ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59)).getContent();
                    chargeDTOS.forEach(chargeDTO -> {
                        this.paymentDocumentService.sendChargeEmail(administrationConfigurationDTO, houseDTO, chargeDTO);
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
                    this.chargeService.sendReminderEmailAndMorosos(administrationConfigurationDTO, houseDTO, chargeDTOS);
                });
            }
        });
        log.debug("Enviando correos de cuotas");
    }


    //    Cada 30 segundos prueba
//    Todos los dias a las 12 am
//    @Scheduled(cron = "0 0 0 1/1 * ?")
//    @Scheduled(cron = "* */2 * * * *")
//    @Scheduled(cron = "*/30 * * * * *")

    @Scheduled(cron = "0 0 0 1/1 * ?")
//    @Scheduled(cron = "* */2 * * * *")
//    @Scheduled(cron = "*/30 * * * * *")
    @Async
    public void crearRondas() throws ExecutionException, InterruptedException {
        List<AdministrationConfigurationDTO> administrationConfigurationDTOS = this.administrationConfigurationService.findAll(null).getContent();
        for (int i = 0; i < administrationConfigurationDTOS.size(); i++) {
            AdministrationConfigurationDTO administrationConfigurationDTO = administrationConfigurationDTOS.get(i);
            Long companyId = administrationConfigurationDTO.getCompanyId();
            boolean hasRounds = this.companyConfigurationService.getOneByCompanyId(companyId).isHasRounds();
            String b ="";
            if (hasRounds) {
                try {
                    List<RoundConfigurationDTO> rConfigs = this.roundConfigurationService.getAllByCompany(companyId + "");
                    String a = "";
                    this.roundService.createRounds(rConfigs, companyId);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
