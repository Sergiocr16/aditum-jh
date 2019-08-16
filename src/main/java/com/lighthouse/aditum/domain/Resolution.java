package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Resolution.
 */
@Entity
@Table(name = "resolution")
public class Resolution implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "problem", nullable = false)
    private String problem;

    @NotNull
    @Column(name = "solution", nullable = false)
    private String solution;

    @Column(name = "stated")
    private Integer stated;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "solved_times")
    private Integer solvedTimes;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @ManyToMany
    @JoinTable(name = "resolution_article",
               joinColumns = @JoinColumn(name="resolutions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="articles_id", referencedColumnName="id"))
    private Set<Article> articles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "resolution_key_words",
               joinColumns = @JoinColumn(name="resolutions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="key_words_id", referencedColumnName="id"))
    private Set<KeyWords> keyWords = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "resolution_article_category",
               joinColumns = @JoinColumn(name="resolutions_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="article_categories_id", referencedColumnName="id"))
    private Set<ArticleCategory> articleCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Resolution title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProblem() {
        return problem;
    }

    public Resolution problem(String problem) {
        this.problem = problem;
        return this;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public Resolution solution(String solution) {
        this.solution = solution;
        return this;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Integer getStated() {
        return stated;
    }

    public Resolution stated(Integer stated) {
        this.stated = stated;
        return this;
    }

    public void setStated(Integer stated) {
        this.stated = stated;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Resolution deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getSolvedTimes() {
        return solvedTimes;
    }

    public Resolution solvedTimes(Integer solvedTimes) {
        this.solvedTimes = solvedTimes;
        return this;
    }

    public void setSolvedTimes(Integer solvedTimes) {
        this.solvedTimes = solvedTimes;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Resolution creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public Resolution articles(Set<Article> articles) {
        this.articles = articles;
        return this;
    }

    public Resolution addArticle(Article article) {
        this.articles.add(article);
        return this;
    }

    public Resolution removeArticle(Article article) {
        this.articles.remove(article);
        return this;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Set<KeyWords> getKeyWords() {
        return keyWords;
    }

    public Resolution keyWords(Set<KeyWords> keyWords) {
        this.keyWords = keyWords;
        return this;
    }

    public Resolution addKeyWords(KeyWords keyWords) {
        this.keyWords.add(keyWords);
        return this;
    }

    public Resolution removeKeyWords(KeyWords keyWords) {
        this.keyWords.remove(keyWords);
        return this;
    }

    public void setKeyWords(Set<KeyWords> keyWords) {
        this.keyWords = keyWords;
    }

    public Set<ArticleCategory> getArticleCategories() {
        return articleCategories;
    }

    public Resolution articleCategories(Set<ArticleCategory> articleCategories) {
        this.articleCategories = articleCategories;
        return this;
    }

    public Resolution addArticleCategory(ArticleCategory articleCategory) {
        this.articleCategories.add(articleCategory);
        return this;
    }

    public Resolution removeArticleCategory(ArticleCategory articleCategory) {
        this.articleCategories.remove(articleCategory);
        return this;
    }

    public void setArticleCategories(Set<ArticleCategory> articleCategories) {
        this.articleCategories = articleCategories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resolution resolution = (Resolution) o;
        if (resolution.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resolution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Resolution{" +
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
