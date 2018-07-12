package com.lighthouse.aditum.service.dto;

import java.util.List;

public class EgressCategoryByMonthDTO {
    private String name;
    private List<String> totalByMonth;
    private int acumulado = 0;

    public EgressCategoryByMonthDTO(String name){

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTotalByMonth() {
        return totalByMonth;
    }

    public void setTotalByMonth(List<String> totalByMonth) {
        this.totalByMonth = totalByMonth;
    }

    public int getAcumulado() {
        return acumulado;
    }

    public void setAcumulado(int acumulado) {
        this.acumulado = this.acumulado + acumulado;
    }
}
