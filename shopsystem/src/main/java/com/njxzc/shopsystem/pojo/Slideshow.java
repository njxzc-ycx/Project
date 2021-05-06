package com.njxzc.shopsystem.pojo;

public class Slideshow {
    private Integer slideshowId;
    private String slideshowUrl;
    private Integer slideshowStatus;

    public Slideshow() {
    }

    public Slideshow(Integer slideshowId, String slideshowUrl, Integer slideshowStatus) {
        this.slideshowId = slideshowId;
        this.slideshowUrl = slideshowUrl;
        this.slideshowStatus = slideshowStatus;
    }

    public Integer getSlideshowId() {
        return slideshowId;
    }

    public void setSlideshowId(Integer slideshowId) {
        this.slideshowId = slideshowId;
    }

    public String getSlideshowUrl() {
        return slideshowUrl;
    }

    public void setSlideshowUrl(String slideshowUrl) {
        this.slideshowUrl = slideshowUrl;
    }

    public Integer getSlideshowStatus() {
        return slideshowStatus;
    }

    public void setSlideshowStatus(Integer slideshowStatus) {
        this.slideshowStatus = slideshowStatus;
    }

    @Override
    public String toString() {
        return "Slideshow{" +
                "slideshowId=" + slideshowId +
                ", slideshowUrl='" + slideshowUrl + '\'' +
                ", slideshowStatus=" + slideshowStatus +
                '}';
    }
}
