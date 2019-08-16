package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Resolution entity.
 */
public class ResolutionDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String problem;

    @NotNull
    private String solution;

    private Integer stated;

    private Integer deleted;

    private Integer solvedTimes;

    private ZonedDateTime creationDate;

    private Set<ArticleDTO> articles = new HashSet<>();

    private Set<KeyWordsDTO> keyWords = new HashSet<>();

    private Set<ArticleCategoryDTO> articleCategories = new HashSet<>();

    private Long companyId;

    private Long adminInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Integer getStated() {
        return stated;
    }

    public void setStated(Integer stated) {
        this.stated = stated;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getSolvedTimes() {
        return solvedTimes;
    }

    public void setSolvedTimes(Integer solvedTimes) {
        this.solvedTimes = solvedTimes;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<ArticleDTO> getArticles() {
        return articles;
    }

    public void setArticles(Set<ArticleDTO> articles) {
        this.articles = articles;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getAdminInfoId() {
        return adminInfoId;
    }

    public void setAdminInfoId(Long adminInfoId) {
        this.adminInfoId = adminInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResolutionDTO resolutionDTO = (ResolutionDTO) o;
        if(resolutionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resolutionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResolutionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", problem='" + getProblem() + "'" +
            ", solution='" + getSolution() + "'" +
            ", stated=" + getStated() +
            ", deleted=" + getDeleted() +
            ", solvedTimes=" + getSolvedTimes() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
