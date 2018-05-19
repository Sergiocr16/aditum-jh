package com.lighthouse.aditum.service;

/**
 * Created by Sergio on 18/05/2018.
 */
import java.time.ZonedDateTime;
import java.util.List;

import com.lighthouse.aditum.domain.BalanceByAccount;
import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.service.mapper.BalanceByAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private BancoService bancoService;
    private BalanceByAccountService balanceByAccountService;
    private BalanceByAccountMapper balanceByAccountMapper;
    public ScheduledTasks(BancoService bancoService,BalanceByAccountService balanceByAccountService,BalanceByAccountMapper balanceByAccountMapper) {
        this.bancoService = bancoService;
        this.balanceByAccountService = balanceByAccountService;
        this.balanceByAccountMapper = balanceByAccountMapper;
    }
//    @Scheduled(cron="0 0/5 * 1/1 * ?") Cada 5 minutos prueba
   @Scheduled(cron="0 0 12 1 1/1 ?")
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
}
