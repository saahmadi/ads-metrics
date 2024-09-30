package io.webmetric.advertisement.domain;

import org.springframework.data.annotation.Id;

public class Impression {
    @Id
    private Long id;

    private String impressionId;

    private int appId;
    private String countryCode;
    private int advertiserId;
    private Click click;

    public Impression(String impressionId, int appId, String countryCode, int advertiserId) {
        this.impressionId = impressionId;
        this.appId = appId;
        this.countryCode = countryCode;
        this.advertiserId = advertiserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public void setImpressionId(String impressionId) {
        this.impressionId = impressionId;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public Click getClick() {
        return click;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    @Override
    public String toString() {
        return "Impression{" +
                "id=" + id +
                ", impressionId=" + impressionId +
                ", appId=" + appId +
                ", countryCode='" + countryCode + '\'' +
                ", advertiserId=" + advertiserId +
                ", click=" + click +
                '}';
    }
}


