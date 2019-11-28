package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A SubsidiaryType.
 */
@Entity
@Table(name = "subsidiary_type")
public class SubsidiaryType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "jhi_size")
    private String size;

    @Column(name = "joint_ownership_percentage")
    private Double jointOwnershipPercentage;

    @Column(name = "ammount")
    private String ammount;

    @Column(name = "jhi_limit")
    private Integer limit;

    @Column(name = "subsidiary_type")
    private Integer subsidiaryType;

    @ManyToOne
    private SubsidiaryCategory subsidiaryCategory;

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

    public SubsidiaryType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public SubsidiaryType size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getJointOwnershipPercentage() {
        return jointOwnershipPercentage;
    }

    public SubsidiaryType jointOwnershipPercentage(Double jointOwnershipPercentage) {
        this.jointOwnershipPercentage = jointOwnershipPercentage;
        return this;
    }

    public void setJointOwnershipPercentage(Double jointOwnershipPercentage) {
        this.jointOwnershipPercentage = jointOwnershipPercentage;
    }

    public String getAmmount() {
        return ammount;
    }

    public SubsidiaryType ammount(String ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public Integer getLimit() {
        return limit;
    }

    public SubsidiaryType limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSubsidiaryType() {
        return subsidiaryType;
    }

    public SubsidiaryType subsidiaryType(Integer subsidiaryType) {
        this.subsidiaryType = subsidiaryType;
        return this;
    }

    public void setSubsidiaryType(Integer subsidiaryType) {
        this.subsidiaryType = subsidiaryType;
    }

    public SubsidiaryCategory getSubsidiaryCategory() {
        return subsidiaryCategory;
    }

    public SubsidiaryType subsidiaryCategory(SubsidiaryCategory subsidiaryCategory) {
        this.subsidiaryCategory = subsidiaryCategory;
        return this;
    }

    public void setSubsidiaryCategory(SubsidiaryCategory subsidiaryCategory) {
        this.subsidiaryCategory = subsidiaryCategory;
    }

    public Company getCompany() {
        return company;
    }

    public SubsidiaryType company(Company company) {
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
        SubsidiaryType subsidiaryType = (SubsidiaryType) o;
        if (subsidiaryType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subsidiaryType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubsidiaryType{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", size='" + getSize() + "'" +
            ", jointOwnershipPercentage=" + getJointOwnershipPercentage() +
            ", ammount='" + getAmmount() + "'" +
            ", limit=" + getLimit() +
            ", subsidiaryType=" + getSubsidiaryType() +
            "}";
    }
}
