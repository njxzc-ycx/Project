package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class Typethird implements Serializable {
    private Integer thirdId;
    private String thirdName;
    private String thirdPicture;
    private Integer secondId;

    private Typefirst typefirst;
    private Typesecond typesecond;

    public Typefirst getTypefirst() {
        return typefirst;
    }

    public void setTypefirst(Typefirst typefirst) {
        this.typefirst = typefirst;
    }

    public Typesecond getTypesecond() {
        return typesecond;
    }

    public void setTypesecond(Typesecond typesecond) {
        this.typesecond = typesecond;
    }

    public Typethird() {
    }

    public Typethird(Integer thirdId, String thirdName, String thirdPicture, Integer secondId) {
        this.thirdId = thirdId;
        this.thirdName = thirdName;
        this.thirdPicture = thirdPicture;
        this.secondId = secondId;
    }

    public Integer getThirdId() {
        return thirdId;
    }

    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getThirdPicture() {
        return thirdPicture;
    }

    public void setThirdPicture(String thirdPicture) {
        this.thirdPicture = thirdPicture;
    }

    public Integer getSecondId() {
        return secondId;
    }

    public void setSecondId(Integer secondId) {
        this.secondId = secondId;
    }

    @Override
    public String toString() {
        return "Typethird{" +
                "thirdId=" + thirdId +
                ", thirdName='" + thirdName + '\'' +
                ", thirdPicture='" + thirdPicture + '\'' +
                ", secondId=" + secondId +
                '}';
    }
}
