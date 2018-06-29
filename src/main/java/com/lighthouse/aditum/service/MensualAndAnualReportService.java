package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Banco;
import com.lighthouse.aditum.domain.Charge;
import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MensualAndAnualReportService {

    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final EgressService egressService;
    private final BancoService bancoService;
    private final TransferenciaService transferenciaService;
    private final BalanceByAccountService balanceByAccountService;
    private final EgressCategoryService egressCategoryService;
    public MensualAndAnualReportService(ChargeService chargeService,EgressService egressService, EgressCategoryService egressCategoryService,BalanceByAccountService balanceByAccountService,BancoService bancoService,PaymentService paymentService,TransferenciaService transferenciaService) {
        this.chargeService = chargeService;
        this.balanceByAccountService = balanceByAccountService;
        this.egressService = egressService;
        this.egressCategoryService = egressCategoryService;
        this.bancoService = bancoService;
        this.paymentService = paymentService;
        this.transferenciaService = transferenciaService;
    }

    public MensualAndAnualIngressReportDTO getMensualAndAnualIngressReportDTO(String initialTime, String finalTime, long companyId){
        List<ChargeDTO> maintenanceIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,1,companyId);
        List<PaymentDTO> adelantosIngress = paymentService.findAdelantosByDatesBetweenAndCompany(initialTime,finalTime,Integer.parseInt(companyId+""));
        for (int i = 0; i <adelantosIngress.size() ; i++) {
            ChargeDTO adelanto = new ChargeDTO(adelantosIngress.get(i).getAmmount(),adelantosIngress.get(i).getDate(),companyId,adelantosIngress.get(i).getId(),adelantosIngress.get(i).getHouseId());
            maintenanceIngress.add(adelanto);
        }
        List<ChargeDTO> extraOrdinaryIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,2,companyId);
        List<ChargeDTO> commonAreasIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,3,companyId);
        List<ChargeDTO> otherIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,4,companyId);

        MensualAndAnualIngressReportDTO mensualAndAnualIngressReportDTO = new MensualAndAnualIngressReportDTO();

        mensualAndAnualIngressReportDTO.setMaintenanceIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(maintenanceIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getMaintenanceIngress(),1);

        mensualAndAnualIngressReportDTO.setExtraOrdinaryIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(extraOrdinaryIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getExtraOrdinaryIngress(),2);

        mensualAndAnualIngressReportDTO.setCommonAreasIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(commonAreasIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getCommonAreasIngress(),3);

        mensualAndAnualIngressReportDTO.setOtherIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(otherIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(mensualAndAnualIngressReportDTO.getOtherIngress(),4);

        mensualAndAnualIngressReportDTO.setAllIngressCategoriesTotal();
        mensualAndAnualIngressReportDTO.setPercetagePerCategory();

        return mensualAndAnualIngressReportDTO;
    }



    public MensualAndAnualEgressReportDTO getMensualAndAnualEgressReportDTO( String initialTime, String finalTime, long companyId,MensualAndAnualIngressReportDTO mensualAndAnualIngressReportDTO){
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        Page<EgressDTO> allEgressList = egressService.findByDatesBetweenAndCompany(initialTime,finalTime,companyId);

        MensualAndAnualEgressReportDTO mensualAndAnualEgressReportDTO = new MensualAndAnualEgressReportDTO();
        mensualAndAnualEgressReportDTO.setCategoriesNames(allEgressList,egressCategories);

        mensualAndAnualEgressReportDTO.setFixedCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getFixedCosts(),"Gastos fijos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getFixedCosts(),1,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualAndAnualEgressReportDTO.setVariableCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getVariableCosts(),"Gastos variables",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getVariableCosts(),2,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualAndAnualEgressReportDTO.setOtherCosts(mensualAndAnualEgressReportDTO.getSumCategoryEgress(mensualAndAnualEgressReportDTO.getOtherCosts(),"Otros gastos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualAndAnualEgressReportDTO.setGroupsTotal(mensualAndAnualEgressReportDTO.getOtherCosts(),3,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        return mensualAndAnualEgressReportDTO;
    }

    public List<MensualAndAnualAccountDTO> getAccountBalance(String initialTime, String finalTime, long companyId){
        List<MensualAndAnualAccountDTO> listaFinal = new ArrayList<>();
        List<BancoDTO> bancos = bancoService.findAll(companyId);
        for (int i = 0; i <bancos.size() ; i++) {
           MensualAndAnualAccountDTO MensualAndAnualAccountDTO = new MensualAndAnualAccountDTO();
           int inicialBalance;
           List<BalanceByAccountDTO> balances = balanceByAccountService.findByDatesBetweenAndAccount(initialTime,finalTime,bancos.get(i).getId());
           if(balances.size()>0){
               inicialBalance = balances.get(0).getBalance();
           }else{
               inicialBalance = Integer.parseInt(bancos.get(i).getCapitalInicial());
           }
           List<EgressDTO> egresos = egressService.findByDatesBetweenAndCompanyAndAccount(initialTime,finalTime,companyId,bancos.get(i).getId()+"");
           for (int j = 0; j < egresos.size(); j++) {
               inicialBalance = inicialBalance - Integer.parseInt(egresos.get(i).getTotal());
           }
            List<PaymentDTO> ingresos = paymentService.findByDatesBetweenAndCompanyAndAccount(initialTime,finalTime,Integer.parseInt(companyId+""),bancos.get(i).getId()+"");
            for (int j = 0; j < ingresos.size(); j++) {
                inicialBalance = inicialBalance + Integer.parseInt(ingresos.get(j).getAmmount());
            }
            List<Transferencia> transferenciasEntrantes = transferenciaService.getBetweenDatesByInComingTransfer(initialTime,finalTime,Integer.parseInt(bancos.get(i).getId()+""));
            for (int j = 0; j < transferenciasEntrantes.size(); j++) {
                inicialBalance = inicialBalance + Integer.parseInt(transferenciasEntrantes.get(j).getMonto());
            }
            List<Transferencia> transferenciasSalientes = transferenciaService.getBetweenDatesByOutgoingTransfer(initialTime,finalTime,Integer.parseInt(bancos.get(i).getId()+""));
            for (int j = 0; j < transferenciasSalientes.size(); j++) {
                inicialBalance = inicialBalance - Integer.parseInt(transferenciasSalientes.get(j).getMonto());
            }
           MensualAndAnualAccountDTO.setBalance(bancos.get(i).getSaldo());
           MensualAndAnualAccountDTO.setName(bancos.get(i).getBeneficiario());
           MensualAndAnualAccountDTO.setInicialBalance(inicialBalance);
           listaFinal.add(MensualAndAnualAccountDTO);
        }

        return listaFinal;
    }


}
