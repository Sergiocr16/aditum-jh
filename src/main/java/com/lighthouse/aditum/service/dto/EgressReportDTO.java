package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.dto.EgressDTO;

import java.util.ArrayList;
import java.util.List;

public class EgressReportDTO {

    private List<EgressReportItemsDTO> egressByProveedor;
    private double total = 0;

    public List<EgressReportItemsDTO> getEgressByProveedor() {
        return egressByProveedor;
    }

    public void setEgressByProveedor(List<EgressReportItemsDTO> egressByProveedor) {
        this.egressByProveedor = egressByProveedor;
    }

    public EgressReportDTO(){
        this.egressByProveedor = new ArrayList<>();

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
