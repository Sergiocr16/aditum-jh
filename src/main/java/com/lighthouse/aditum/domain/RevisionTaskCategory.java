package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A RevisionTaskCategory.
 */
@Entity
@Table(name = "revision_task_category")
public class RevisionTaskCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "jhi_type")
    private Integer type;

    @ManyToOne
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public RevisionTaskCategory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public RevisionTaskCategory deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getOrder() {
        return order;
    }

    public RevisionTaskCategory order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getType() {
        return type;
    }

    public RevisionTaskCategory type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Company getCompany() {
        return company;
    }

    public RevisionTaskCategory company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        RevisionTaskCategory revisionTaskCategory = (RevisionTaskCategory) o;
        if (revisionTaskCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), revisionTaskCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RevisionTaskCategory{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", deleted=" + getDeleted() +
            ", order=" + getOrder() +
            ", type=" + getType() +
            "}";
    }
}
