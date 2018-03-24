package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Collection.
 */
@Entity
@Table(name = "collection")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "mensual_balance", nullable = false)
    private String mensualBalance;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @ManyToOne(optional = false)
    @NotNull
    private House house;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Collection date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getMensualBalance() {
        return mensualBalance;
    }

    public Collection mensualBalance(String mensualBalance) {
        this.mensualBalance = mensualBalance;
        return this;
    }

    public void setMensualBalance(String mensualBalance) {
        this.mensualBalance = mensualBalance;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Collection deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public House getHouse() {
        return house;
    }

    public Collection house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Collection collection = (Collection) o;
        if (collection.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), collection.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Collection{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", mensualBalance='" + getMensualBalance() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
