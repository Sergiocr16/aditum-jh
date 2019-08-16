package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Article.
 */
@Entity
@Table(name = "article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "equal_to_law")
    private Integer equalToLaw;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private Chapter chapter;

    @ManyToMany
    @JoinTable(name = "article_reference",
               joinColumns = @JoinColumn(name="articles_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="references_id", referencedColumnName="id"))
    private Set<Article> references = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Article name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Article description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEqualToLaw() {
        return equalToLaw;
    }

    public Article equalToLaw(Integer equalToLaw) {
        this.equalToLaw = equalToLaw;
        return this;
    }

    public void setEqualToLaw(Integer equalToLaw) {
        this.equalToLaw = equalToLaw;
    }

    public Integer getOrder() {
        return order;
    }

    public Article order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Article deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getNotes() {
        return notes;
    }

    public Article notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Article chapter(Chapter chapter) {
        this.chapter = chapter;
        return this;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Set<Article> getReferences() {
        return references;
    }

    public Article references(Set<Article> articles) {
        this.references = articles;
        return this;
    }

    public Article addReference(Article article) {
        this.references.add(article);
        article.getReferences().add(this);
        return this;
    }

    public Article removeReference(Article article) {
        this.references.remove(article);
        article.getReferences().remove(this);
        return this;
    }

    public void setReferences(Set<Article> articles) {
        this.references = articles;
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
        Article article = (Article) o;
        if (article.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", equalToLaw=" + getEqualToLaw() +
            ", order=" + getOrder() +
            ", deleted=" + getDeleted() +
            ", notes='" + getNotes() + "'" +
            "}";
    }


}
