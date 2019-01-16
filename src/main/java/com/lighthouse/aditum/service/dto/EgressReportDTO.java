package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.service.dto.EgressDTO;

import java.util.ArrayList;
import java.util.List;

public class EgressReportDTO {

    private List<EgressReportItemsDTO> egressByProveedor;


    public List<EgressReportItemsDTO> getEgressByProveedor() {
        return egressByProveedor;
    }

    public void setEgressByProveedor(List<EgressReportItemsDTO> egressByProveedor) {
        this.egressByProveedor = egressByProveedor;
    }

    public EgressReportDTO(){
        this.egressByProveedor = new ArrayList<>();
    }
}
