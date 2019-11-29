package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
public class Chapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "is_visible")
    private Boolean isVisible;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private Regulation regulation;

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

    public Chapter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Chapter description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public Chapter order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean isIsVisible() {
        return isVisible;
    }

    public Chapter isVisible(Boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Chapter deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getNotes() {
        return notes;
    }

    public Chapter notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Regulation getRegulation() {
        return regulation;
    }

    public Chapter regulation(Regulation regulation) {
        this.regulation = regulation;
        return this;
    }

    public void setRegulation(Regulation regulation) {
        this.regulation = regulation;
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
        Chapter chapter = (Chapter) o;
        if (chapter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chapter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Chapter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", isVisible='" + isIsVisible() + "'" +
            ", deleted=" + getDeleted() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
