package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the Article entity.
 */
public class ArticleDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private Integer equalToLaw;

    private Integer order;

    private Integer deleted;

    private String notes;

    private Long chapterId;

    private List<SubsectionDTO> subsections = new ArrayList<>();

    private Set<ArticleDTO> references = new HashSet<>();

    private Set<KeyWordsDTO> keyWords = new HashSet<>();

    private Set<ArticleCategoryDTO> articleCategories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEqualToLaw() {
        return equalToLaw;
    }

    public void setEqualToLaw(Integer equalToLaw) {
        this.equalToLaw = equalToLaw;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Set<ArticleDTO> getReferences() {
        return references;
    }

    public void setReferences(Set<ArticleDTO> articles) {
        this.references = articles;
    }

    public Set<KeyWordsDTO> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWordsDTO> keyWords) {
        this.keyWords = keyWords;
    }

    public Set<ArticleCategoryDTO> getArticleCategories() {
        return articleCategories;
    }

    public void setArticleCategories(Set<ArticleCategoryDTO> articleCategories) {
        this.articleCategories = articleCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if(articleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), articleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", equalToLaw=" + getEqualToLaw() +
            ", order=" + getOrder() +
            ", deleted=" + getDeleted() +
            ", notes='" + getNotes() + "'" +
            "}";
    }

    public List<SubsectionDTO> getSubsections() {
        return subsections;
    }

    public void setSubsections(List<SubsectionDTO> subsections) {
        this.subsections = subsections;
    }
}
