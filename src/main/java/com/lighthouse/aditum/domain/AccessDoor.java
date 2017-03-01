package com.lighthouse.aditum.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AccessDoor.
 */
@Entity
@Table(name = "access_door")
public class AccessDoor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Watch watch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AccessDoor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public AccessDoor company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Watch getWatch() {
        return watch;
    }

    public AccessDoor watch(Watch watch) {
        this.watch = watch;
        return this;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessDoor accessDoor = (AccessDoor) o;
        if (accessDoor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, accessDoor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccessDoor{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
