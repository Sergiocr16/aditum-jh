package com.lighthouse.aditum.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A WaterConsumption.
 */
@Entity
@Table(name = "water_consumption")
public class WaterConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consumption")
    private String consumption;

    @Column(name = "month")
    private String month;

    @Column(name = "record_date")
    private ZonedDateTime recordDate;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    private House house;

    @ManyToOne
    private Charge charge;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsumption() {
        return consumption;
    }

    public WaterConsumption consumption(String consumption) {
        this.consumption = consumption;
        return this;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getMonth() {
        return month;
    }

    public WaterConsumption month(String month) {
        this.month = month;
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public ZonedDateTime getRecordDate() {
        return recordDate;
    }

    public WaterConsumption recordDate(ZonedDateTime recordDate) {
        this.recordDate = recordDate;
        return this;
    }

    public void setRecordDate(ZonedDateTime recordDate) {
        this.recordDate = recordDate;
    }

    public Integer getStatus() {
        return status;
    }

    public WaterConsumption status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public House getHouse() {
        return house;
    }

    public WaterConsumption house(House house) {
        this.house = house;
        return this;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public Charge getCharge() {
        return charge;
    }

    public WaterConsumption charge(Charge charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
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
        WaterConsumption waterConsumption = (WaterConsumption) o;
        if (waterConsumption.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), waterConsumption.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WaterConsumption{" +
            "id=" + getId() +
            ", consumption='" + getConsumption() + "'" +
            ", month='" + getMonth() + "'" +
            ", recordDate='" + getRecordDate() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
