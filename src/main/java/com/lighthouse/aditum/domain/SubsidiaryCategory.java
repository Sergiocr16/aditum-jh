package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A SubsidiaryCategory.
 */
@Entity
@Table(name = "subsidiary_category")
public class SubsidiaryCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category_type")
    private Integer categoryType;

    @ManyToOne
    private Company company;

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

    public SubsidiaryCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public SubsidiaryCategory categoryType(Integer categoryType) {
        this.categoryType = categoryType;
        return this;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Company getCompany() {
        return company;
    }

    public SubsidiaryCategory company(Company company) {
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
        SubsidiaryCategory subsidiaryCategory = (SubsidiaryCategory) o;
        if (subsidiaryCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiaryCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubsidiaryCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", categoryType=" + getCategoryType() +
            "}";
    }
}
