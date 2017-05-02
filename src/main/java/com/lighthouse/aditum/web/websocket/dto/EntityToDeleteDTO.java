package com.lighthouse.aditum.web.websocket.dto;

/**
 * Created by Sergio on 26/04/2017.
 */
public class EntityToDeleteDTO {

    private String type;

    private Long id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "ActivityDTO{" +
            "type='" + type + '\'' +
            ", id='" + id + '\'' +
            '}';
    }

}
