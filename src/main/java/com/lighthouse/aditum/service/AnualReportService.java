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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class AnualReportService {

    private final ChargeService chargeService;
    private final PaymentService paymentService;
    private final EgressService egressService;
    private final BancoService bancoService;
    private final PresupuestoService presupuestoService;
    private final TransferenciaService transferenciaService;
    private final BalanceByAccountService balanceByAccountService;
    private final EgressCategoryService egressCategoryService;
    private final DetallePresupuestoService detallePresupuestoService;

    public AnualReportService(ChargeService chargeService, EgressService egressService, EgressCategoryService egressCategoryService, BalanceByAccountService balanceByAccountService, BancoService bancoService, PaymentService paymentService, TransferenciaService transferenciaService, PresupuestoService presupuestoService, DetallePresupuestoService detallePresupuestoService) {
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




}
