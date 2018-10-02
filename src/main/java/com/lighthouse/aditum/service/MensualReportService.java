package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MensualReportService {

    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final EgressService egressService;
    private final BancoService bancoService;
    private final PresupuestoService presupuestoService;
    private final TransferenciaService transferenciaService;
    private final BalanceByAccountService balanceByAccountService;
    private final EgressCategoryService egressCategoryService;
    private final DetallePresupuestoService detallePresupuestoService;
    public MensualReportService(ChargeService chargeService, EgressService egressService, EgressCategoryService egressCategoryService, BalanceByAccountService balanceByAccountService, BancoService bancoService, PaymentService paymentService, TransferenciaService transferenciaService, PresupuestoService presupuestoService, DetallePresupuestoService detallePresupuestoService) {
        this.chargeService = chargeService;
        this.balanceByAccountService = balanceByAccountService;
        this.egressService = egressService;
        this.egressCategoryService = egressCategoryService;
        this.bancoService = bancoService;
        this.paymentService = paymentService;
        this.transferenciaService = transferenciaService;
        this.presupuestoService = presupuestoService;
        this.detallePresupuestoService = detallePresupuestoService;
    }

    public MensualIngressReportDTO getMensualAndAnualIngressReportDTO(String initialTime, String finalTime, long companyId, int withPresupuesto){
        List<ChargeDTO> maintenanceIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,1,companyId);
        List<PaymentDTO> adelantosIngress = paymentService.findAdelantosByDatesBetweenAndCompany(initialTime,finalTime,Integer.parseInt(companyId+""));
        for (int i = 0; i <adelantosIngress.size() ; i++) {

            ChargeDTO adelanto = new ChargeDTO(adelantosIngress.get(i).getAmmountLeft(),adelantosIngress.get(i).getDate(),companyId,adelantosIngress.get(i).getId(),adelantosIngress.get(i).getHouseId());
            maintenanceIngress.add(adelanto);

        }
        List<ChargeDTO> extraOrdinaryIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,2,companyId);
        List<ChargeDTO> commonAreasIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,3,companyId);
        List<ChargeDTO> otherIngress = chargeService.findPaidChargesBetweenDatesList(initialTime,finalTime,4,companyId);

        MensualIngressReportDTO mensualAndAnualIngressReportDTO = new MensualIngressReportDTO();

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


        if(withPresupuesto==1){
           this.getIngressBudgets(mensualAndAnualIngressReportDTO,companyId,initialTime,finalTime);
           mensualAndAnualIngressReportDTO.setAllIngressCategoriesBudgetTotal();

        }

        return mensualAndAnualIngressReportDTO;
    }



    public MensualEgressReportDTO getMensualAndAnualEgressReportDTO(String initialTime, String finalTime, long companyId, MensualIngressReportDTO mensualAndAnualIngressReportDTO, int withPresupuesto){
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        Page<EgressDTO> allEgressList = egressService.findPaymentEgressByDatesBetweenAndCompany(initialTime,finalTime,companyId);

        MensualEgressReportDTO mensualEgressReportDTO = new MensualEgressReportDTO();
        mensualEgressReportDTO.setCategoriesNames(allEgressList,egressCategories);

        mensualEgressReportDTO.setFixedCosts(mensualEgressReportDTO.getSumCategoryEgress(mensualEgressReportDTO.getFixedCosts(),"Gastos fijos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(mensualEgressReportDTO.getFixedCosts(),1,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualEgressReportDTO.setVariableCosts(mensualEgressReportDTO.getSumCategoryEgress(mensualEgressReportDTO.getVariableCosts(),"Gastos variables",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(mensualEgressReportDTO.getVariableCosts(),2,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualEgressReportDTO.setOtherCosts(mensualEgressReportDTO.getSumCategoryEgress(mensualEgressReportDTO.getOtherCosts(),"Otros gastos",mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(mensualEgressReportDTO.getOtherCosts(),3,mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());
        mensualEgressReportDTO.setAllEgressTotal();
        if(withPresupuesto==1){
            this.getEgressBudgets(mensualEgressReportDTO,companyId,initialTime,finalTime,egressCategories);
            mensualEgressReportDTO.setTotalBudgetPerGroup();
            this.setEgressDiferences(mensualEgressReportDTO);
        }

        return mensualEgressReportDTO;
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
               inicialBalance = inicialBalance - Integer.parseInt(egresos.get(j).getTotal());
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

    public void getEgressBudgets(MensualEgressReportDTO mensualEgressReportDTO, Long companyId, String intialTime, String finalTime, Page<EgressCategoryDTO> egressCategories) {

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(intialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        ZonedDateTime initialBudgetTime = zd_initialTime.withMonth(1).withDayOfYear(1);
        ZonedDateTime finalBudgetTime = zd_finalTime.withMonth(1).withDayOfYear(3);

        List<PresupuestoDTO> presupuestos = presupuestoService.findByBudgetsDatesBetweenAndCompany(initialBudgetTime,finalBudgetTime,companyId);
        String a = "a";
        if(presupuestos.size()==1){
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(mensualEgressReportDTO,zd_initialTime.getMonthValue(),zd_finalTime.getMonthValue(),egressCategoriesBudget,egressCategories,j);
            }

        }
        else if(presupuestos.size()>2){
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(mensualEgressReportDTO,zd_initialTime.getMonthValue(),12,egressCategoriesBudget,egressCategories,j);

            }
            for(int x=1; x< egressCategoriesBudget.size()-1; x++){
                List<DetallePresupuestoDTO> egressCategoriesBudget2 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(x).getId()+"");
                for (int j = 0; j < egressCategoriesBudget2.size(); j++) {
                    addEgressWhenHasBudget(mensualEgressReportDTO,1,12,egressCategoriesBudget,egressCategories,j);

                }
            }
            List<DetallePresupuestoDTO> egressCategoriesBudget3 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(presupuestos.size()-1).getId()+"");
            for (int j = 0; j < egressCategoriesBudget3.size(); j++) {
                addEgressWhenHasBudget(mensualEgressReportDTO,1,zd_finalTime.getMonthValue(),egressCategoriesBudget,egressCategories,j);

            }
        }else if(presupuestos.size()==2){
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(mensualEgressReportDTO,zd_initialTime.getMonthValue(),12,egressCategoriesBudget,egressCategories,j);

            }

            List<DetallePresupuestoDTO> egressCategoriesBudget3 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(presupuestos.size()-1).getId()+"");
            for (int j = 0; j < egressCategoriesBudget3.size(); j++) {
                addEgressWhenHasBudget(mensualEgressReportDTO,1,zd_finalTime.getMonthValue(),egressCategoriesBudget,egressCategories,j);

            }

        }


    }

    private void addEgressWhenHasBudget(MensualEgressReportDTO mensualEgressReportDTO, int intialTime, int finalTime, List<DetallePresupuestoDTO> egressCategoriesBudget, Page<EgressCategoryDTO> egressCategories, int j){


        int total = getEgressBudgetValuesPerMonths(mensualEgressReportDTO,egressCategoriesBudget.get(j),intialTime,finalTime);
        if(total>0){

            for (int i =0;egressCategories.getContent().size()>i;i++) {
                if(Long.parseLong( egressCategoriesBudget.get(j).getCategory())==egressCategories.getContent().get(i).getId()){
                    SumCategoryEgressDTO object = new SumCategoryEgressDTO(egressCategories.getContent().get(i).getId(), egressCategories.getContent().get(i).getCategory(),total);

                    switch (egressCategories.getContent().get(i).getGroup()){
                        case "Gastos fijos":
                            mensualEgressReportDTO.getFixedCosts().add(object);
                            break;
                        case "Gastos variables":
                            mensualEgressReportDTO.getVariableCosts().add(object);
                            break;
                        case "Otros gastos":
                            mensualEgressReportDTO.getOtherCosts().add(object);
                            break;

                    }
                }


            }
        }

    }

    private void setEgressDiferences(MensualEgressReportDTO mensualEgressReportDTO){
        for (int i = 0; i < mensualEgressReportDTO.getFixedCosts().size() ; i++) {

            mensualEgressReportDTO.getFixedCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getFixedCosts().get(i).getBudget() - mensualEgressReportDTO.getFixedCosts().get(i).getTotal());
        }
        for (int i = 0; i < mensualEgressReportDTO.getVariableCosts().size() ; i++) {

            mensualEgressReportDTO.getVariableCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getVariableCosts().get(i).getBudget() - mensualEgressReportDTO.getVariableCosts().get(i).getTotal());

        }
        for (int i = 0; i < mensualEgressReportDTO.getOtherCosts().size() ; i++) {

            mensualEgressReportDTO.getOtherCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getOtherCosts().get(i).getBudget() - mensualEgressReportDTO.getOtherCosts().get(i).getTotal());

        }

    }

    private int getEgressBudgetValuesPerMonths(MensualEgressReportDTO mensualEgressReportDTO, DetallePresupuestoDTO egressCategoriesBudget, int initialMonth, int finalMonth){
        String[] values = egressCategoriesBudget.getValuePerMonth().split(",");
        int total = 0;
        int noEntro = 0;
        for (int i = initialMonth-1; i <=finalMonth-1; i++) {
            total = total + Integer.parseInt(values[i]);
        }
        for (int i = 0; i < mensualEgressReportDTO.getFixedCosts().size() ; i++) {

            if( mensualEgressReportDTO.getFixedCosts().get(i).getId()==Long.parseLong(egressCategoriesBudget.getCategory())){
                mensualEgressReportDTO.getFixedCosts().get(i).setBudget(total);

                noEntro++;
            }
            mensualEgressReportDTO.getFixedCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getFixedCosts().get(i).getBudget() - mensualEgressReportDTO.getFixedCosts().get(i).getTotal());
        }
        for (int i = 0; i < mensualEgressReportDTO.getVariableCosts().size() ; i++) {

            if( mensualEgressReportDTO.getVariableCosts().get(i).getId()==Long.parseLong(egressCategoriesBudget.getCategory())){
                mensualEgressReportDTO.getVariableCosts().get(i).setBudget(total);

                noEntro++;
            }
            mensualEgressReportDTO.getVariableCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getVariableCosts().get(i).getBudget() - mensualEgressReportDTO.getVariableCosts().get(i).getTotal());

        }
        for (int i = 0; i < mensualEgressReportDTO.getOtherCosts().size() ; i++) {

            if( mensualEgressReportDTO.getOtherCosts().get(i).getId()==Long.parseLong(egressCategoriesBudget.getCategory())){
                mensualEgressReportDTO.getOtherCosts().get(i).setBudget(total);

                noEntro++;
            }
            mensualEgressReportDTO.getOtherCosts().get(i).setBudgetDiference(mensualEgressReportDTO.getOtherCosts().get(i).getBudget() - mensualEgressReportDTO.getOtherCosts().get(i).getTotal());

        }
        if(total>0&&noEntro==0){
            return total;

        }else {
            return 0;
        }


    }
    public void getIngressBudgets(MensualIngressReportDTO mensualAndAnualIngressReportDTO, Long companyId, String intialTime, String finalTime) {

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(intialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        ZonedDateTime initialBudgetTime = zd_initialTime.withMonth(1).withDayOfYear(1);
        ZonedDateTime finalBudgetTime = zd_finalTime.withMonth(1).withDayOfYear(2);

        List<PresupuestoDTO> presupuestos = presupuestoService.findByBudgetsDatesBetweenAndCompany(initialBudgetTime,finalBudgetTime,companyId);
        if(presupuestos.size()==1){
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget.get(j),zd_initialTime.getMonthValue(),zd_finalTime.getMonthValue(),ingresCategoriesBudget.get(j).getCategory());

            }

        }else if(presupuestos.size()>2){
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget.get(j),zd_initialTime.getMonthValue(),12,ingresCategoriesBudget.get(j).getCategory());

            }
            for(int x=1; x< ingresCategoriesBudget.size()-1; x++){
                List<DetallePresupuestoDTO> ingresCategoriesBudget2 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(x).getId()+"");
                for (int j = 0; j < ingresCategoriesBudget2.size(); j++) {
                    getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget2.get(j),1,12,ingresCategoriesBudget.get(j).getCategory());

                }
            }
            List<DetallePresupuestoDTO> ingresCategoriesBudget3 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(presupuestos.size()-1).getId()+"");
            for (int j = 0; j < ingresCategoriesBudget3.size(); j++) {
                getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget3.get(j),1,zd_finalTime.getMonthValue(),ingresCategoriesBudget.get(j).getCategory());

            }
        }else if(presupuestos.size()==2){
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId()+"");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget.get(j),zd_initialTime.getMonthValue(),12,ingresCategoriesBudget.get(j).getCategory());

            }

            List<DetallePresupuestoDTO> ingresCategoriesBudget3 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(presupuestos.size()-1).getId()+"");
            for (int j = 0; j < ingresCategoriesBudget3.size(); j++) {
                getIngresBudgetValuesPerMonths(mensualAndAnualIngressReportDTO,ingresCategoriesBudget3.get(j),1,zd_finalTime.getMonthValue(),ingresCategoriesBudget.get(j).getCategory());

            }

        }


    }
    private void getIngresBudgetValuesPerMonths(MensualIngressReportDTO mensualAndAnualIngressReportDTO, DetallePresupuestoDTO ingresCategoriesBudget, int initialMonth, int finalMonth, String category){
        String[] values = ingresCategoriesBudget.getValuePerMonth().split(",");
        int total = 0;
        for (int i = initialMonth-1; i <=finalMonth-1; i++) {
            total = total + Integer.parseInt(values[i]);
        }
        switch (category){
            case "mantenimiento":
                mensualAndAnualIngressReportDTO.setMaintenanceBudget(mensualAndAnualIngressReportDTO.getMaintenanceBudget()+total);
                break;
            case "extraordinarias":
                mensualAndAnualIngressReportDTO.setExtraordinaryBudget(mensualAndAnualIngressReportDTO.getExtraordinaryBudget()+ total);
                break;
            case "areas comunes":
                mensualAndAnualIngressReportDTO.setCommonAreasBudget(mensualAndAnualIngressReportDTO.getCommonAreasBudget()+total);
                break;
            case "otros ingresos":
                mensualAndAnualIngressReportDTO.setOtherBudget(mensualAndAnualIngressReportDTO.getOtherBudget()+total);
                break;
        }

    }
    }
