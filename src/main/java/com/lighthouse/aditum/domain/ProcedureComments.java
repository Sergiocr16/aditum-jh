package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A ProcedureComments.
 */
@Entity
@Table(name = "procedure_comments")
public class ProcedureComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_comment", nullable = false)
    private String comment;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "creation_date")
    private String creationDate;

    @ManyToOne
    private Procedures procedures;

    @ManyToOne
    private AdminInfo adminInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public ProcedureComments comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public ProcedureComments deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public ProcedureComments creationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Procedures getProcedures() {
        return procedures;
    }

    public ProcedureComments procedures(Procedures procedures) {
        this.procedures = procedures;
        return this;
    }

    public void setProcedures(Procedures procedures) {
        this.procedures = procedures;
    }

    public AdminInfo getAdminInfo() {
        return adminInfo;
    }

    public ProcedureComments adminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
        return this;
    }

    public void setAdminInfo(AdminInfo adminInfo) {
        this.adminInfo = adminInfo;
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
        ProcedureComments procedureComments = (ProcedureComments) o;
        if (procedureComments.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureComments.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureComments{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", deleted=" + getDeleted() +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
