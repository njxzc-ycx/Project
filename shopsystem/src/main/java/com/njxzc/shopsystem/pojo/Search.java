package com.njxzc.shopsystem.pojo;

public class Search {
    private String start;
    private String end;
    private String key;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Search{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
