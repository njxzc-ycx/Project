package com.njxzc.shopsystem.pojo;

public class DiscussType {

    private Integer discussTypeId;
    private String discussTypeName;

    public DiscussType() {
    }

    public DiscussType(Integer discussTypeId, String discussTypeName) {
        this.discussTypeId = discussTypeId;
        this.discussTypeName = discussTypeName;
    }

    public Integer getDiscussTypeId() {
        return discussTypeId;
    }

    public void setDiscussTypeId(Integer discussTypeId) {
        this.discussTypeId = discussTypeId;
    }

    public String getDiscussTypeName() {
        return discussTypeName;
    }

    public void setDiscussTypeName(String discussTypeName) {
        this.discussTypeName = discussTypeName;
    }

    @Override
    public String toString() {
        return "DiscussType{" +
                "discussTypeId=" + discussTypeId +
                ", discussTypeName='" + discussTypeName + '\'' +
                '}';
    }
}
