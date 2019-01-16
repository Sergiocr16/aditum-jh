package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Presupuesto.
 */
@Entity
@Table(name = "presupuesto")
public class Presupuesto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @Column(name = "modification_date")
    private ZonedDateTime modificationDate;

    @NotNull
    @Column(name = "anno", nullable = false)
    private Integer anno;

    @Column(name = "deleted")
    private Integer deleted;

    @ManyToOne
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Presupuesto date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public Presupuesto modificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
        return this;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getAnno() {
        return anno;
    }

    public Presupuesto anno(Integer anno) {
        this.anno = anno;
        return this;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Presupuesto deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Company getCompany() {
        return company;
    }

    public Presupuesto company(Company company) {
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
        Presupuesto presupuesto = (Presupuesto) o;
        if (presupuesto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), presupuesto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Presupuesto{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", modificationDate='" + getModificationDate() + "'" +
            ", anno='" + getAnno() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
