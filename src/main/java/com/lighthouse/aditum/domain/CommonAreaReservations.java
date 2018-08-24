package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CommonAreaReservations.
 */
@Entity
@Table(name = "common_area_reservations")
public class CommonAreaReservations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "house_id", nullable = false)
    private Long houseId;

    @NotNull
    @Column(name = "resident_id", nullable = false)
    private Long residentId;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "initial_time", nullable = false)
    private String initialTime;

    @NotNull
    @Column(name = "final_time", nullable = false)
    private String finalTime;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    private CommonArea commonArea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public CommonAreaReservations houseId(Long houseId) {
        this.houseId = houseId;
        return this;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Long getResidentId() {
        return residentId;
    }

    public CommonAreaReservations residentId(Long residentId) {
        this.residentId = residentId;
        return this;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public CommonAreaReservations date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getInitialTime() {
        return initialTime;
    }

    public CommonAreaReservations initialTime(String initialTime) {
        this.initialTime = initialTime;
        return this;
    }

    public void setInitialTime(String initialTime) {
        this.initialTime = initialTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public CommonAreaReservations finalTime(String finalTime) {
        this.finalTime = finalTime;
        return this;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getComments() {
        return comments;
    }

    public CommonAreaReservations comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public CommonArea getCommonArea() {
        return commonArea;
    }

    public CommonAreaReservations commonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
        return this;
    }

    public void setCommonArea(CommonArea commonArea) {
        this.commonArea = commonArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonAreaReservations commonAreaReservations = (CommonAreaReservations) o;
        if (commonAreaReservations.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commonAreaReservations.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommonAreaReservations{" +
            "id=" + getId() +
            ", houseId='" + getHouseId() + "'" +
            ", residentId='" + getResidentId() + "'" +
            ", date='" + getDate() + "'" +
            ", initialTime='" + getInitialTime() + "'" +
            ", finalTime='" + getFinalTime() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
