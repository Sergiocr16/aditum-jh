package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CompanyConfiguration;
import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
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
    private final CompanyConfigurationService companyConfigurationService;
    private final IndicadoresEconomicosBccr indicadoresEconomicosBccr;


    public MensualReportService(IndicadoresEconomicosBccr indicadoresEconomicosBccr,CompanyConfigurationService companyConfigurationService, ChargeService chargeService, EgressService egressService, EgressCategoryService egressCategoryService, BalanceByAccountService balanceByAccountService, BancoService bancoService, PaymentService paymentService, TransferenciaService transferenciaService, PresupuestoService presupuestoService, DetallePresupuestoService detallePresupuestoService) {
        this.chargeService = chargeService;
        this.balanceByAccountService = balanceByAccountService;
        this.egressService = egressService;
        this.egressCategoryService = egressCategoryService;
        this.bancoService = bancoService;
        this.paymentService = paymentService;
        this.transferenciaService = transferenciaService;
        this.presupuestoService = presupuestoService;
        this.detallePresupuestoService = detallePresupuestoService;
        this.companyConfigurationService = companyConfigurationService;
        this.indicadoresEconomicosBccr = indicadoresEconomicosBccr;
    }

    public MensualIngressReportDTO getMensualAndAnualIngressReportDTO(ZonedDateTime initialTime, ZonedDateTime finalTime, long companyId, int withPresupuesto) {
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<ChargeDTO> maintenanceIngress = chargeService.findPaidChargesBetweenDatesList(initialTime, finalTime, 1, companyId);
        List<PaymentDTO> adelantosIngress = paymentService.findAdelantosByDatesBetweenAndCompany(initialTime, finalTime, Integer.parseInt(companyId + ""));
        finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);

        List<ChargeDTO> ingressList = new ArrayList<>();
        for (int i = 0; i < adelantosIngress.size(); i++) {
            PaymentDTO p = adelantosIngress.get(i);
             p = this.paymentService.findOneComplete(p.getId());
            for (int c = 0; c < p.getCharges().size(); c++) {
                PaymentChargeDTO charge = p.getCharges().get(c);
//                OJO AQUI
                if(charge.getDate().isAfter(finalTime)){
                    ChargeDTO adelanto = new ChargeDTO(currency, charge.getAmmount()+"", "0", adelantosIngress.get(i).getDate(), companyId, adelantosIngress.get(i).getId(), adelantosIngress.get(i).getHouseId());
                    ingressList.add(adelanto);
                }
            }
        }
        if (!ingressList.isEmpty()) {
            List<ChargeDTO> maintenanceIngressTotal = new ArrayList<>();
            maintenanceIngressTotal.addAll(maintenanceIngress);
            maintenanceIngressTotal.addAll(ingressList);
            maintenanceIngress = maintenanceIngressTotal;
        }
        List<ChargeDTO> extraOrdinaryIngress = chargeService.findPaidChargesBetweenDatesList(initialTime, finalTime, 2, companyId);
        List<ChargeDTO> commonAreasIngress = chargeService.findPaidChargesBetweenDatesList(initialTime, finalTime, 3, companyId);
        List<ChargeDTO> multaIngress = chargeService.findPaidChargesBetweenDatesList(initialTime, finalTime, 5, companyId);
        List<ChargeDTO> waterChargeIngress = chargeService.findPaidChargesBetweenDatesList(initialTime, finalTime, 6, companyId);
        List<ChargeDTO> otherChargesIngress = chargeService.findPaidChargesBetweenDatesListAndOther(initialTime, finalTime, 7, companyId);
        List<PaymentDTO> otherPayments = paymentService.findOtherIngressByDatesBetweenAndCompany(initialTime, finalTime, Integer.parseInt(companyId + ""));
        List<ChargeDTO> otherIngress = new ArrayList<>();
        otherPayments.forEach(paymentDTO -> {
            ChargeDTO charge = new ChargeDTO();
            charge.setTotal(currency, Double.parseDouble(paymentDTO.getAmmount()));
            charge.setConcept(paymentDTO.getConcept());
            otherIngress.add(charge);
        });
        otherIngress.addAll(otherChargesIngress);
        MensualIngressReportDTO mensualAndAnualIngressReportDTO = new MensualIngressReportDTO();

        mensualAndAnualIngressReportDTO.setMaintenanceIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, maintenanceIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getMaintenanceIngress(), 1);

        mensualAndAnualIngressReportDTO.setExtraOrdinaryIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, extraOrdinaryIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getExtraOrdinaryIngress(), 2);

        mensualAndAnualIngressReportDTO.setCommonAreasIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, commonAreasIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getCommonAreasIngress(), 3);

        mensualAndAnualIngressReportDTO.setOtherIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, otherIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getOtherIngress(), 4);

        mensualAndAnualIngressReportDTO.setMultaIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, multaIngress));
        ;
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getMultaIngress(), 5);

        mensualAndAnualIngressReportDTO.setWaterChargeIngress(mensualAndAnualIngressReportDTO.getSumChargeIngress(currency, waterChargeIngress));
        mensualAndAnualIngressReportDTO.setIngressCategoryTotal(currency, mensualAndAnualIngressReportDTO.getWaterChargeIngress(), 6);

        mensualAndAnualIngressReportDTO.setAllIngressCategoriesTotal(currency);
        mensualAndAnualIngressReportDTO.setPercetagePerCategory();

        if (withPresupuesto == 1) {
            this.getIngressBudgets(mensualAndAnualIngressReportDTO, companyId, initialTime, finalTime);
            mensualAndAnualIngressReportDTO.setAllIngressCategoriesBudgetTotal(currency);
        }

        return mensualAndAnualIngressReportDTO;
    }


    public MensualEgressReportDTO getMensualAndAnualEgressReportDTO(ZonedDateTime initialTime, ZonedDateTime finalTime, long companyId, MensualIngressReportDTO mensualAndAnualIngressReportDTO, int withPresupuesto) {
        Page<EgressCategoryDTO> egressCategories = egressCategoryService.findAll(companyId);
        Page<EgressDTO> allEgressList = egressService.findPaymentEgressByDatesBetweenAndCompany(initialTime, finalTime, companyId);
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();

        MensualEgressReportDTO mensualEgressReportDTO = new MensualEgressReportDTO();
        mensualEgressReportDTO.setCategoriesNames(allEgressList, egressCategories);

        mensualEgressReportDTO.setFixedCosts(mensualEgressReportDTO.getSumCategoryEgress(currency, mensualEgressReportDTO.getFixedCosts(), "Gastos fijos", mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(currency, mensualEgressReportDTO.getFixedCosts(), 1, mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualEgressReportDTO.setVariableCosts(mensualEgressReportDTO.getSumCategoryEgress(currency, mensualEgressReportDTO.getVariableCosts(), "Gastos variables", mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(currency, mensualEgressReportDTO.getVariableCosts(), 2, mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualEgressReportDTO.setOtherCosts(mensualEgressReportDTO.getSumCategoryEgress(currency, mensualEgressReportDTO.getOtherCosts(), "Otros gastos", mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal()));
        mensualEgressReportDTO.setGroupsTotal(currency, mensualEgressReportDTO.getOtherCosts(), 3, mensualAndAnualIngressReportDTO.getAllIngressCategoriesTotal());

        mensualEgressReportDTO.setAllEgressTotal(currency);
        if (withPresupuesto == 1) {
            this.getEgressBudgets(mensualEgressReportDTO, companyId, initialTime, finalTime, egressCategories);
            mensualEgressReportDTO.setTotalBudgetPerGroup(currency);
            this.setEgressDiferences(currency, mensualEgressReportDTO);
        }

        return mensualEgressReportDTO;
    }


    public List<MensualAndAnualAccountDTO> getAccountBalance(ZonedDateTime initialTime, ZonedDateTime finalTime, long companyId) {
        List<MensualAndAnualAccountDTO> listaFinal = new ArrayList<>();
        List<BancoDTO> bancos = bancoService.findAll(companyId);
        BancoDTO bancoDTO;
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        ExchangeRateBccr e = null;
        try {
            e = this.indicadoresEconomicosBccr.obtenerTipodeCambio(ZonedDateTime.now(),ZonedDateTime.now());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ParserConfigurationException parserConfigurationException) {
            parserConfigurationException.printStackTrace();
        } catch (SAXException saxException) {
            saxException.printStackTrace();
        }

        for (int i = 0; i < bancos.size(); i++) {
            MensualAndAnualAccountDTO mensualAndAnualAccountDTO = new MensualAndAnualAccountDTO();
            ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
            ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
            if (zd_initialTime.isAfter(zd_finalTime)) {
                bancoDTO = bancoService.getInicialBalance(initialTime, bancos.get(i), initialTime);
            } else {
                bancoDTO = bancoService.getInicialBalance(initialTime, bancos.get(i), finalTime);
            }

            mensualAndAnualAccountDTO.setCurrency(currency);
            if(!bancoDTO.getCurrency().equals(currency)){
                   double saldo = Double.parseDouble(bancoDTO.getSaldo())*Double.parseDouble(e.getCompra());
                    mensualAndAnualAccountDTO.setBalance(saldo+"");
                    double temporal = bancoDTO.getCapitalInicialTemporal()*Double.parseDouble(e.getCompra());
                    mensualAndAnualAccountDTO.setInicialBalance(currency,temporal);
            }else{
                mensualAndAnualAccountDTO.setBalance(bancoDTO.getSaldo());
                mensualAndAnualAccountDTO.setInicialBalance(currency, bancoDTO.getCapitalInicialTemporal());
            }
            mensualAndAnualAccountDTO.setName(bancos.get(i).getBeneficiario());
            listaFinal.add(mensualAndAnualAccountDTO);
        }

        return listaFinal;
    }

    public void getEgressBudgets(MensualEgressReportDTO mensualEgressReportDTO, Long companyId, ZonedDateTime intialTime, ZonedDateTime finalTime, Page<EgressCategoryDTO> egressCategories) {

        ZonedDateTime zd_initialTime = intialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        ZonedDateTime initialBudgetTime = intialTime.withMonth(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime finalBudgetTime = finalTime.withMonth(1).withDayOfYear(3).withHour(23).withMinute(59).withSecond(59);
        List<PresupuestoDTO> presupuestos = presupuestoService.findByBudgetsDatesBetweenAndCompany(initialBudgetTime, finalBudgetTime, companyId);
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();

        if (presupuestos.size() == 1) {
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(currency, mensualEgressReportDTO, zd_initialTime.getMonthValue(), zd_finalTime.getMonthValue(), egressCategoriesBudget, egressCategories, j);
            }

        } else if (presupuestos.size() > 2) {
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(currency, mensualEgressReportDTO, zd_initialTime.getMonthValue(), 12, egressCategoriesBudget, egressCategories, j);

            }
            for (int x = 1; x < egressCategoriesBudget.size() - 1; x++) {
                List<DetallePresupuestoDTO> egressCategoriesBudget2 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(x).getId() + "");
                for (int j = 0; j < egressCategoriesBudget2.size(); j++) {
                    addEgressWhenHasBudget(currency, mensualEgressReportDTO, 1, 12, egressCategoriesBudget, egressCategories, j);

                }
            }
            List<DetallePresupuestoDTO> egressCategoriesBudget3 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(presupuestos.size() - 1).getId() + "");
            for (int j = 0; j < egressCategoriesBudget3.size(); j++) {
                addEgressWhenHasBudget(currency, mensualEgressReportDTO, 1, zd_finalTime.getMonthValue(), egressCategoriesBudget, egressCategories, j);

            }
        } else if (presupuestos.size() == 2) {
            List<DetallePresupuestoDTO> egressCategoriesBudget = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < egressCategoriesBudget.size(); j++) {
                addEgressWhenHasBudget(currency, mensualEgressReportDTO, zd_initialTime.getMonthValue(), 12, egressCategoriesBudget, egressCategories, j);

            }

            List<DetallePresupuestoDTO> egressCategoriesBudget3 = detallePresupuestoService.getEgressCategoriesByBudget(presupuestos.get(presupuestos.size() - 1).getId() + "");
            for (int j = 0; j < egressCategoriesBudget3.size(); j++) {
                addEgressWhenHasBudget(currency, mensualEgressReportDTO, 1, zd_finalTime.getMonthValue(), egressCategoriesBudget, egressCategories, j);

            }

        }


    }

    private void addEgressWhenHasBudget(String currency, MensualEgressReportDTO mensualEgressReportDTO, int intialTime, int finalTime, List<DetallePresupuestoDTO> egressCategoriesBudget, Page<EgressCategoryDTO> egressCategories, int j) {


        double total = getEgressBudgetValuesPerMonths(currency, mensualEgressReportDTO, egressCategoriesBudget.get(j), intialTime, finalTime);
        if (total > 0) {

            for (int i = 0; egressCategories.getContent().size() > i; i++) {
                if (Long.parseLong(egressCategoriesBudget.get(j).getCategory()) == egressCategories.getContent().get(i).getId()) {
                    SumCategoryEgressDTO object = new SumCategoryEgressDTO(currency, egressCategories.getContent().get(i).getId(), egressCategories.getContent().get(i).getCategory(), total);

                    switch (egressCategories.getContent().get(i).getGroup()) {
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

    private void setEgressDiferences(String currency, MensualEgressReportDTO mensualEgressReportDTO) {
        for (int i = 0; i < mensualEgressReportDTO.getFixedCosts().size(); i++) {

            mensualEgressReportDTO.getFixedCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getFixedCosts().get(i).getBudget() - mensualEgressReportDTO.getFixedCosts().get(i).getTotal());
        }
        for (int i = 0; i < mensualEgressReportDTO.getVariableCosts().size(); i++) {

            mensualEgressReportDTO.getVariableCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getVariableCosts().get(i).getBudget() - mensualEgressReportDTO.getVariableCosts().get(i).getTotal());

        }
        for (int i = 0; i < mensualEgressReportDTO.getOtherCosts().size(); i++) {

            mensualEgressReportDTO.getOtherCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getOtherCosts().get(i).getBudget() - mensualEgressReportDTO.getOtherCosts().get(i).getTotal());

        }

    }

    private double getEgressBudgetValuesPerMonths(String currency, MensualEgressReportDTO mensualEgressReportDTO, DetallePresupuestoDTO egressCategoriesBudget, int initialMonth, int finalMonth) {
        String[] values = egressCategoriesBudget.getValuePerMonth().split(",");
        double total = 0;
        int noEntro = 0;
        for (int i = initialMonth - 1; i <= finalMonth - 1; i++) {
            total = total + Double.parseDouble(values[i]);
        }
        for (int i = 0; i < mensualEgressReportDTO.getFixedCosts().size(); i++) {

            if (mensualEgressReportDTO.getFixedCosts().get(i).getId() == Long.parseLong(egressCategoriesBudget.getCategory())) {
                mensualEgressReportDTO.getFixedCosts().get(i).setBudget(currency, total);
                noEntro++;
            }
            mensualEgressReportDTO.getFixedCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getFixedCosts().get(i).getBudget() - mensualEgressReportDTO.getFixedCosts().get(i).getTotal());
        }
        for (int i = 0; i < mensualEgressReportDTO.getVariableCosts().size(); i++) {

            if (mensualEgressReportDTO.getVariableCosts().get(i).getId() == Long.parseLong(egressCategoriesBudget.getCategory())) {
                mensualEgressReportDTO.getVariableCosts().get(i).setBudget(currency, total);

                noEntro++;
            }
            mensualEgressReportDTO.getVariableCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getVariableCosts().get(i).getBudget() - mensualEgressReportDTO.getVariableCosts().get(i).getTotal());

        }
        for (int i = 0; i < mensualEgressReportDTO.getOtherCosts().size(); i++) {

            if (mensualEgressReportDTO.getOtherCosts().get(i).getId() == Long.parseLong(egressCategoriesBudget.getCategory())) {
                mensualEgressReportDTO.getOtherCosts().get(i).setBudget(currency, total);

                noEntro++;
            }
            mensualEgressReportDTO.getOtherCosts().get(i).setBudgetDiference(currency, mensualEgressReportDTO.getOtherCosts().get(i).getBudget() - mensualEgressReportDTO.getOtherCosts().get(i).getTotal());

        }
        if (total > 0 && noEntro == 0) {
            return total;

        } else {
            return 0;
        }


    }

    public void getIngressBudgets(MensualIngressReportDTO mensualAndAnualIngressReportDTO, Long companyId, ZonedDateTime intialTime, ZonedDateTime finalTime) {

        ZonedDateTime zd_initialTime = intialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        ZonedDateTime initialBudgetTime = zd_initialTime.withMonth(1).withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime finalBudgetTime = zd_finalTime.withMonth(1).withDayOfYear(2).withHour(23).withMinute(59).withSecond(59);
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        List<PresupuestoDTO> presupuestos = presupuestoService.findByBudgetsDatesBetweenAndCompany(initialBudgetTime, finalBudgetTime, companyId);
        if (presupuestos.size() == 1) {
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget.get(j), zd_initialTime.getMonthValue(), zd_finalTime.getMonthValue(), ingresCategoriesBudget.get(j).getCategory());
            }
        } else if (presupuestos.size() > 2) {
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget.get(j), zd_initialTime.getMonthValue(), 12, ingresCategoriesBudget.get(j).getCategory());
            }
            for (int x = 1; x < ingresCategoriesBudget.size() - 1; x++) {
                List<DetallePresupuestoDTO> ingresCategoriesBudget2 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(x).getId() + "");
                for (int j = 0; j < ingresCategoriesBudget2.size(); j++) {
                    getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget2.get(j), 1, 12, ingresCategoriesBudget.get(j).getCategory());

                }
            }
            List<DetallePresupuestoDTO> ingresCategoriesBudget3 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(presupuestos.size() - 1).getId() + "");
            for (int j = 0; j < ingresCategoriesBudget3.size(); j++) {
                getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget3.get(j), 1, zd_finalTime.getMonthValue(), ingresCategoriesBudget.get(j).getCategory());

            }
        } else if (presupuestos.size() == 2) {
            List<DetallePresupuestoDTO> ingresCategoriesBudget = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(0).getId() + "");
            for (int j = 0; j < ingresCategoriesBudget.size(); j++) {
                getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget.get(j), zd_initialTime.getMonthValue(), 12, ingresCategoriesBudget.get(j).getCategory());

            }

            List<DetallePresupuestoDTO> ingresCategoriesBudget3 = detallePresupuestoService.getIngressCategoriesByBudget(presupuestos.get(presupuestos.size() - 1).getId() + "");
            for (int j = 0; j < ingresCategoriesBudget3.size(); j++) {
                getIngresBudgetValuesPerMonths(currency, mensualAndAnualIngressReportDTO, ingresCategoriesBudget3.get(j), 1, zd_finalTime.getMonthValue(), ingresCategoriesBudget.get(j).getCategory());
            }

        }


    }

    private void getIngresBudgetValuesPerMonths(String currency, MensualIngressReportDTO mensualAndAnualIngressReportDTO, DetallePresupuestoDTO ingresCategoriesBudget, int initialMonth, int finalMonth, String category) {
        String[] values = ingresCategoriesBudget.getValuePerMonth().split(",");
        double total = 0;
        for (int i = initialMonth - 1; i <= finalMonth - 1; i++) {
            total = total + Double.parseDouble(values[i]);
        }
        switch (category) {
            case "mantenimiento":
                mensualAndAnualIngressReportDTO.setMaintenanceBudget(currency, mensualAndAnualIngressReportDTO.getMaintenanceBudget() + total);
                break;
            case "extraordinarias":
                mensualAndAnualIngressReportDTO.setExtraordinaryBudget(currency, mensualAndAnualIngressReportDTO.getExtraordinaryBudget() + total);
                break;
            case "areas comunes":
                mensualAndAnualIngressReportDTO.setCommonAreasBudget(currency, mensualAndAnualIngressReportDTO.getCommonAreasBudget() + total);
                break;
            case "otros ingresos":
                mensualAndAnualIngressReportDTO.setOtherBudget(currency, mensualAndAnualIngressReportDTO.getOtherBudget() + total);
                break;
            case "multa":
                mensualAndAnualIngressReportDTO.setMultaBudget(currency, mensualAndAnualIngressReportDTO.getMultaBudget() + total);
                break;
            case "cuota agua":
                mensualAndAnualIngressReportDTO.setWaterChargeBudget(currency, mensualAndAnualIngressReportDTO.getWaterChargeBudget() + total);
                break;
        }

    }
}
