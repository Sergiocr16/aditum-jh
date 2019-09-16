package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the KeyWords entity.
 */
public class KeyWordsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KeyWordsDTO keyWordsDTO = (KeyWordsDTO) o;
        if(keyWordsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), keyWordsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KeyWordsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
