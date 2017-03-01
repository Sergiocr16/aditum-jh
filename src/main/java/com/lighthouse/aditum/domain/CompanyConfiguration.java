package com.lighthouse.aditum.domain;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "company_configuration")
public class CompanyConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantityhouses", nullable = false)
    private Integer quantityhouses;

    @NotNull
    @Column(name = "quantityadmins", nullable = false)
    private Integer quantityadmins;

    @NotNull
    @Column(name = "quantityaccessdoor", nullable = false)
    private Integer quantityaccessdoor;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityhouses() {
        return quantityhouses;
    }

    public CompanyConfiguration quantityhouses(Integer quantityhouses) {
        this.quantityhouses = quantityhouses;
        return this;
    }

    public void setQuantityhouses(Integer quantityhouses) {
        this.quantityhouses = quantityhouses;
    }

    public Integer getQuantityadmins() {
        return quantityadmins;
    }

    public CompanyConfiguration quantityadmins(Integer quantityadmins) {
        this.quantityadmins = quantityadmins;
        return this;
    }

    public void setQuantityadmins(Integer quantityadmins) {
        this.quantityadmins = quantityadmins;
    }

    public Integer getQuantityaccessdoor() {
        return quantityaccessdoor;
    }

    public CompanyConfiguration quantityaccessdoor(Integer quantityaccessdoor) {
        this.quantityaccessdoor = quantityaccessdoor;
        return this;
    }

    public void setQuantityaccessdoor(Integer quantityaccessdoor) {
        this.quantityaccessdoor = quantityaccessdoor;
    }

    public Company getCompany() {
        return company;
    }

    public CompanyConfiguration company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyConfiguration companyConfiguration = (CompanyConfiguration) o;
        if (companyConfiguration.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyConfiguration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyConfiguration{" +
            "id=" + id +
            ", quantityhouses='" + quantityhouses + "'" +
            ", quantityadmins='" + quantityadmins + "'" +
            ", quantityaccessdoor='" + quantityaccessdoor + "'" +
            '}';
    }
}
