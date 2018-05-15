package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EgressCategory.
 */
@Entity
@Table(name = "egress_category")
public class EgressCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_group", nullable = false)
    private String group;

    @NotNull
    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne
    private Company companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public EgressCategory group(String group) {
        this.group = group;
        return this;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCategory() {
        return category;
    }

    public EgressCategory category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public EgressCategory companyId(Company company) {
        this.companyId = company;
        return this;
    }

    public void setCompanyId(Company company) {
        this.companyId = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EgressCategory egressCategory = (EgressCategory) o;
        if (egressCategory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), egressCategory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EgressCategory{" +
            "id=" + getId() +
            ", group='" + getGroup() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
