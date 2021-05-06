package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class Typefirst implements Serializable {
    private Integer firstId;
    private String firstName;

    public Typefirst() {
    }

    public Typefirst(Integer firstId, String firstName) {
        this.firstId = firstId;
        this.firstName = firstName;
    }

    public Integer getFirstId() {
        return firstId;
    }

    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Typefirst{" +
                "firstId=" + firstId +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}
