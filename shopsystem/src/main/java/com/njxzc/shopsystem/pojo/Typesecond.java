package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class Typesecond implements Serializable {
    private Integer secondId;
    private String secondName;
    private Integer firstId;
    private Typethird typethird;

    public Typethird getTypethird() {
        return typethird;
    }

    public void setTypethird(Typethird typethird) {
        this.typethird = typethird;
    }

    public Typesecond() {
    }

    public Typesecond(Integer secondId, String secondName, Integer firstId) {
        this.secondId = secondId;
        this.secondName = secondName;
        this.firstId = firstId;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public void setSecondId(Integer secondId) {
        this.secondId = secondId;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Integer getFirstId() {
        return firstId;
    }

    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }

    @Override
    public String toString() {
        return "Typesecond{" +
                "secondId=" + secondId +
                ", secondName='" + secondName + '\'' +
                ", firstId=" + firstId +
                '}';
    }
}
