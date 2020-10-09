package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A BlockReservation.
 */
@Entity
@Table(name = "block_reservation")
public class BlockReservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocked")
    private Integer blocked;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    private House house;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public BlockReservation blocked(Integer blocked) {
        this.blocked = blocked;
        return this;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    public String getComments() {
        return comments;
    }

    public BlockReservation comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public House getHouse() {
        return house;
    }

    public BlockReservation house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
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
        BlockReservation blockReservation = (BlockReservation) o;
        if (blockReservation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blockReservation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlockReservation{" +
            "id=" + getId() +
            ", blocked=" + getBlocked() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
