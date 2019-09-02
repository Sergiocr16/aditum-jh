package com.lighthouse.aditum.service.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoriesKeyWordsQueryDTO {

    private List<Long> categories = new ArrayList<>();
    private List<Long> keyWords = new ArrayList<>();
    private RegulationDTO regulationDTO;



    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }

    public List<Long> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<Long> keyWords) {
        this.keyWords = keyWords;
    }

    public RegulationDTO getRegulationDTO() {
        return regulationDTO;
    }

    public void setRegulationDTO(RegulationDTO regulationDTO) {
        this.regulationDTO = regulationDTO;
    }
}
